package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_2;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.util.Pair;

import java.util.ArrayList;

public class NanoThief_Skill_2 extends NanoThief_SkillBase {
    ArrayList<ArrayList<ArrayList<WeaponAPI>>> wepons = new ArrayList<>();
    private double maxCost = 0;
    public NanoThief_Skill_2(NanoThief_ShipSkills skills, ShipAPI ship) {
        super(skills,ship);
        //skills.ship.getVariant().getWeaponGroups().get(0).getSlots();
        prepareForSingleModule(ship);
        for (ShipAPI b : skills.ship.getChildModulesCopy()){
            prepareForSingleModule(b);
        }
        if (wepons.isEmpty()){
            skills.suppressListener(this);
        }
    }
    private void prepareForSingleModule(ShipAPI ship){
        ArrayList<ArrayList<WeaponAPI>> weponsT = new ArrayList<>();
        weponsT.add(new ArrayList<>());
        weponsT.add(new ArrayList<>());
        weponsT.add(new ArrayList<>());
        for (WeaponAPI a : ship.getAllWeapons()){
            //a.getAmmoTracker().setAmmo(0);
            if (a.getMaxAmmo() == 0) continue;
            if (a.getAmmoTracker().getAmmoPerSecond() != 0) continue;
            int b = 0;
            if (a.getSpec().getSize().equals(WeaponAPI.WeaponSize.SMALL)) b = 0;
            if (a.getSpec().getSize().equals(WeaponAPI.WeaponSize.MEDIUM)) b = 1;
            if (a.getSpec().getSize().equals(WeaponAPI.WeaponSize.LARGE)) b = 2;
            weponsT.get(b).add(a);
            Pair<Double, Float> pair = getStatsForGun(a);
            if (pair.one > maxCost) maxCost = pair.one;
        }
        if (!weponsT.get(0).isEmpty() || !weponsT.get(1).isEmpty() || !weponsT.get(2).isEmpty()){
            wepons.add(weponsT);
        }
    }

    private float cooldown = 0f;
    private static final float secondsPerReceack = 3f;
    private boolean onCooldown = false;
    @Override
    public void advance(float amount) {
        cooldown-=amount;
        if (cooldown > 0) return;
        //if (skills.getTotalReclaim() == 0) return;
        for (ArrayList<ArrayList<WeaponAPI>> weponsT : wepons){
            for (ArrayList<WeaponAPI> a : weponsT) {
                for (WeaponAPI b : a) {
                    if (b.getAmmo() != 0) continue;
                    Pair<Double,Float> pair = getStatsForGun(b);
                    if (skills.getTotalReclaim() >= skills.getModifiedCost(pair.one)) {
                        skills.useReclaim(skills.getModifiedCost(pair.one));
                        cooldown = pair.two;
                        //b.getAmmoTracker().setAmmo(b.getAmmoTracker().getMaxAmmo());
                        b.setAmmo(b.getMaxAmmo());
                        playSoundIfPlayerShip();
                        onCooldown = true;
                        //log.info("forging missile...");
                        return;
                    }
                }
            }
        }
        //log.info("activating temp cooldown...");
        onCooldown = false;
        cooldown=secondsPerReceack;
    }
    private Pair<Double,Float> getStatsForGun(WeaponAPI b){
        double costPerOpp = 0;
        float cooldownT = 0;
        if (b.getSpec().getSize().equals(WeaponAPI.WeaponSize.SMALL)) {
            costPerOpp = NanoThief_2.costSmall;
            cooldownT = NanoThief_2.timeSmall;
        }
        if (b.getSpec().getSize().equals(WeaponAPI.WeaponSize.MEDIUM)) {
            costPerOpp = NanoThief_2.costMid;
            cooldownT = NanoThief_2.timeMid;
        }
        if (b.getSpec().getSize().equals(WeaponAPI.WeaponSize.LARGE)) {
            costPerOpp = NanoThief_2.costLarge;
            cooldownT = NanoThief_2.timeLarge;
        }
        double cost = Math.max(b.getSpec().getOrdnancePointCost(skills.ship.getCaptain().getStats(), skills.ship.getMutableStats()),1) * costPerOpp;
        Pair<Double,Float> out = new Pair<>();
        out.one = cost;
        out.two = cooldownT;
        return out;
    }
    private void playSoundIfPlayerShip(){
        if (!ship.equals(Global.getCombatEngine().getPlayerShip())) return;
    }

    @Override
    public void displayStats() {
        if (onCooldown){
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_2", "graphics/icons/hullsys/temporal_shell.png",
                    "Scrapwork Microforge", "On cooldown for "+((int)cooldown)+" seconds",true);
            return;
        }
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_2", "graphics/icons/hullsys/temporal_shell.png",
                "Scrapwork Microforge", "Ready to forge additional ammo",false);
    }

    @Override
    public double getMaxCost() {
        return maxCost;
    }
}
