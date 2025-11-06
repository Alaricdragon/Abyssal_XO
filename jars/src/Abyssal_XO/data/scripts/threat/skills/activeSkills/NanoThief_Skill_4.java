package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_4;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.combat.listeners.DamageTakenModifier;
import org.lwjgl.util.vector.Vector2f;

public class NanoThief_Skill_4 extends NanoThief_SkillBase{
    public float cooldown;
    public boolean ready = true;
    public double totalHull = 0;//for module ships that have a strange total hull ponit amount

    //note: this is held here so that when I apply this effect to all modules, I can keep track of stats.
    public float damageLastFewSeconds = 0;
    public float timeActive;
    public boolean isActive = false;
    public NanoThief_Skill_4(NanoThief_ShipSkills skills, ShipAPI ship) {
        //todo: make sure that the sound + animation is working. look at RAT 'MagicSubsystem's. they have good sounds and animations.
        super(skills, ship);
        totalHull = ship.getMaxHitpoints();
        if (!ship.hasListenerOfClass(DamageModifier.class)){
            ship.addListener(new DamageModifier(this));
        }
        //this is here to make sure that 'desperate measures' does not reduce the duration of this ability.
        if (!ship.hasListenerOfClass(trueTimeListener.class)){
            ship.addListener(new trueTimeListener(this));
        }
        for (ShipAPI b : skills.ship.getChildModulesCopy()){
            if (!b.hasListenerOfClass(DamageModifier.class)){
                b.addListener(new DamageModifier(this));
                totalHull+=b.getMaxHitpoints();
            }
        }
    }

    @Override
    public void advance(float amount) {
        cooldown -= amount;
        if (!isActive && cooldown <= 0) ready = true;
    }

    @Override
    public void displayStats() {
        if (ready){
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_4", "graphics/icons/hullsys/temporal_shell.png",
                    "Scrapy Fortification", "is ready to activate",false);
            return;
        }
        if (isActive){
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_4", "graphics/icons/hullsys/temporal_shell.png",
                    "Scrapy Fortification", "is active for "+(NanoThief_4.time-timeActive)+" seconds",false);
            return;
        }
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_4", "graphics/icons/hullsys/temporal_shell.png",
                "Scrapy Fortification", "On cooldown for "+((int)cooldown)+" seconds",true);
    }
}
class DamageModifier implements DamageTakenModifier{
    NanoThief_Skill_4 skill;
    public DamageModifier(NanoThief_Skill_4 skill){
        this.skill = skill;
    }
    @Override
    public String modifyDamageTaken(Object param, CombatEntityAPI target, DamageAPI damage, Vector2f point, boolean shieldHit) {
        if (!shieldHit && skill.isActive){
            skill.skills.useReclaim((damage.getDamage()*(1-NanoThief_4.resistance))/NanoThief_4.damagePerCost);
            return null;
        }
        if (!shieldHit && skill.ready){
            skill.damageLastFewSeconds += damage.getDamage();
        }
        return null;
    }
}
class trueTimeListener implements AdvanceableListener{
    NanoThief_Skill_4 skill;
    public trueTimeListener(NanoThief_Skill_4 skill){
        this.skill = skill;
    }
    @Override
    public void advance(float amount) {
        skill.damageLastFewSeconds *= Math.max(0,1-(amount / NanoThief_4.activeTime));
        if (!skill.isActive){
            //todo: this line needs work. it is always active. also I need tow ork on the other systems
            if (skill.damageLastFewSeconds <= NanoThief_4.activeDamage && 1-(skill.totalHull/skill.damageLastFewSeconds) <= NanoThief_4.activePercent) return;
            activate();
        }
        skill.timeActive+=amount;
        if (skill.timeActive >= NanoThief_4.time){
            deactivate();
        }
    }
    private void activate(){
        skill.isActive = true;
        skill.timeActive = 0;
        skill.damageLastFewSeconds = 0;
        activate(skill.ship);
        for (ShipAPI b : skill.ship.getChildModulesCopy()){
            activate(b);
        }
    }
    private void activate(ShipAPI ship){
        ship.getMutableStats().getHullDamageTakenMult().modifyMult(NanoThief_4.modifier, NanoThief_4.resistance);
        ship.getMutableStats().getArmorDamageTakenMult().modifyMult(NanoThief_4.modifier, NanoThief_4.resistance);
        ship.getMutableStats().getEmpDamageTakenMult().modifyMult(NanoThief_4.modifier, NanoThief_4.resistance);
    }
    private void deactivate(){
        skill.isActive = false;
        skill.cooldown = NanoThief_4.cooldown;
        deactivate(skill.ship);
        for (ShipAPI b : skill.ship.getChildModulesCopy()){
            activate(b);
        }
    }
    private void deactivate(ShipAPI ship){
        ship.getMutableStats().getHullDamageTakenMult().unmodifyMult(NanoThief_4.modifier);
        ship.getMutableStats().getArmorDamageTakenMult().unmodifyMult(NanoThief_4.modifier);
        ship.getMutableStats().getEmpDamageTakenMult().unmodifyMult(NanoThief_4.modifier);
    }
}
