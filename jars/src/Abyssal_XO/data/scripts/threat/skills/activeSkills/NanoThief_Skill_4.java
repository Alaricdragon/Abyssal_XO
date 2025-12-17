package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_4;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEntityAPI;
import com.fs.starfarer.api.combat.DamageAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.combat.listeners.DamageTakenModifier;
import org.apache.log4j.Logger;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;

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
            ship.addListener(new DamageModifier(this,ship));
        }
        //this is here to make sure that 'desperate measures' does not reduce the duration of this ability.
        if (!ship.hasListenerOfClass(trueTimeListener.class)){
            ship.addListener(new trueTimeListener(this));
        }
        for (ShipAPI b : skills.ship.getChildModulesCopy()){
            if (!b.hasListenerOfClass(DamageModifier.class)){
                b.addListener(new DamageModifier(this,b));
                totalHull+=b.getMaxHitpoints();
            }
        }
    }

    @Override
    public void advance(float amount) {
        cooldown -= amount;
        log.info("cooling down to: "+cooldown);
        if (!isActive && cooldown <= 0) ready = true;
    }

    @Override
    public void displayStats() {
        if (isActive){
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_4", "graphics/icons/hullsys/temporal_shell.png",
                    "Scrapy Fortification", "is active for "+(int)(NanoThief_4.time-timeActive)+" seconds",false);
            return;
        }
        if (skills.getTotalReclaim() < skills.getModifiedCost(NanoThief_4.activeCost)){
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_4", "graphics/icons/hullsys/temporal_shell.png",
                    "Scrapy Fortification", "Cannot activate well under "+NanoThief_4.activeCost+" reclaim",true);
            return;
        }
        if (ready){
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_4", "graphics/icons/hullsys/temporal_shell.png",
                    "Scrapy Fortification", "is ready to activate",false);
            return;
        }
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_4", "graphics/icons/hullsys/temporal_shell.png",
                "Scrapy Fortification", "On cooldown for "+((int)cooldown)+" seconds",true);
    }
    public void animate(){
        animate(ship);
    }
    private static final Color jitterColor = new Color(255,165,90,55);
    private static final Color jitterUnderColor = new Color(255,165,90,155);
    private void animate(ShipAPI ship){
        ship.setJitter(Settings.DISPLAYID_NANOTHIEF+"_skill_4", jitterColor, 1, 2, 0f, 5);
        ship.setJitterUnder(Settings.DISPLAYID_NANOTHIEF+"_skill_4", jitterUnderColor, 1, 25, 0f, 7);
        for (ShipAPI b : skills.ship.getChildModulesCopy()){
            animate(b);
        }
    }
    /*public void deanimate(){
        deanimate(ship);
    }
    private void deanimate(ShipAPI ship){
    }*/
}
class DamageModifier implements DamageTakenModifier{
    NanoThief_Skill_4 skill;
    ShipAPI ship;
    public DamageModifier(NanoThief_Skill_4 skill,ShipAPI ship){
        this.skill = skill;
        this.ship = ship;
    }
    @Override
    public String modifyDamageTaken(Object param, CombatEntityAPI target, DamageAPI damage, Vector2f point, boolean shieldHit) {
        if (shieldHit) return null;
        if (skill.isActive){
            double cost = skill.skills.getModifiedCost((damage.getDamage()*(1-NanoThief_4.resistance))/NanoThief_4.damagePerCost);
            if (cost >= skill.skills.getTotalReclaim()){
                skill.skills.resetReclaim();
                return null;
            }
            skill.skills.useReclaim(cost);
            return null;
        }
        if (skill.ready){
            skill.damageLastFewSeconds += damage.getDamage();
            /*if (skill.damageLastFewSeconds <= NanoThief_4.activeDamage && (skill.damageLastFewSeconds/ skill.totalHull) >= NanoThief_4.activePercent){
                ship.setNextHitHullDamageThresholdMult(damage.getDamage(),1);
            }*/
        }
        return null;
    }
}
class trueTimeListener implements AdvanceableListener{
    NanoThief_Skill_4 skill;
    protected static Logger log = Global.getLogger(NanoThief_SkillBase.class);
    public trueTimeListener(NanoThief_Skill_4 skill){
        this.skill = skill;
    }
    @Override
    public void advance(float amount) {
        if (skill.isActive) skill.animate();
        if (skill.ready){
            skill.damageLastFewSeconds *= Math.max(0,1-(amount / NanoThief_4.activeTime));
            //return;
        }
        if (!skill.isActive){
            //todo: this line needs work. it is always active. also I need tow ork on the other systems
            if (!(skill.damageLastFewSeconds >= NanoThief_4.activeDamage || ((1+skill.damageLastFewSeconds)/ skill.totalHull) >= NanoThief_4.activePercent)) return;
            if (skill.skills.getTotalReclaim() < skill.skills.getModifiedCost(NanoThief_4.activeCost)) return;
            activate();
            //log.info("DLS: "+skill.damageLastFewSeconds+", totalHull: "+skill.totalHull);
            //log.info("damage ratio: " + ((1+skill.damageLastFewSeconds)/ skill.totalHull));
            //log.info("active true: "+(skill.damageLastFewSeconds >= NanoThief_4.activeDamage));
            //log.info("total true: "+(((1+skill.damageLastFewSeconds)/ skill.totalHull)>= NanoThief_4.activePercent));
            return;
        }
        if (skill.skills.getTotalReclaim() <= 0){
            deactivate();
        }
        skill.timeActive+=amount;
        if (skill.timeActive >= NanoThief_4.time){
            deactivate();
        }
    }
    private void activate(){
        skill.ready = false;
        skill.isActive = true;
        skill.timeActive = 0;
        skill.damageLastFewSeconds = 0;
        activate(skill.ship);
        for (ShipAPI b : skill.ship.getChildModulesCopy()){
            activate(b);
        }
        skill.skills.useReclaim(skill.skills.getModifiedCost(NanoThief_4.activeCost));
        //skill.animate();
        //log.info("activate");
    }
    private void activate(ShipAPI ship){
        ship.getMutableStats().getHullDamageTakenMult().modifyMult(NanoThief_4.modifier, NanoThief_4.resistance);
        ship.getMutableStats().getArmorDamageTakenMult().modifyMult(NanoThief_4.modifier, NanoThief_4.resistance);
        ship.getMutableStats().getEmpDamageTakenMult().modifyMult(NanoThief_4.modifier, NanoThief_4.resistance);

        ship.setNextHitHullDamageThresholdMult(200,0);
        //log.info("deactivate");
    }
    private void deactivate(){
        skill.isActive = false;
        skill.cooldown = NanoThief_4.cooldown;
        deactivate(skill.ship);
        for (ShipAPI b : skill.ship.getChildModulesCopy()){
            activate(b);
        }
        //skill.deanimate();
    }
    private void deactivate(ShipAPI ship){
        ship.getMutableStats().getHullDamageTakenMult().unmodifyMult(NanoThief_4.modifier);
        ship.getMutableStats().getArmorDamageTakenMult().unmodifyMult(NanoThief_4.modifier);
        ship.getMutableStats().getEmpDamageTakenMult().unmodifyMult(NanoThief_4.modifier);
    }
}
