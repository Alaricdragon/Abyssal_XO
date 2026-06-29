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
        for (ShipAPI b : skills.getChildShips()){
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
            if (a.getSpec().getSize().equals(WeaponAPI.WeaponSize.SMALL)) b = 2;
            if (a.getSpec().getSize().equals(WeaponAPI.WeaponSize.MEDIUM)) b = 1;
            if (a.getSpec().getSize().equals(WeaponAPI.WeaponSize.LARGE)) b = 0;
            weponsT.get(b).add(a);
            Pair<Double, Float> pair = getStatsForGun(a);
            if (pair.one > maxCost) maxCost = pair.one;
        }
        if (!weponsT.get(0).isEmpty() || !weponsT.get(1).isEmpty() || !weponsT.get(2).isEmpty()){
            wepons.add(weponsT);
        }
    }

    public float cooldown = 0f;
    public float coolDownInstance = 0f;
    public float secondsPerReceack = 3f;
    public boolean onCooldown = false;

    public double lowestCurrentCost = 0;
    @Override
    public void advance(float amount) {
        cooldown-=amount;
        if (cooldown > 0) return;
        //cooldown = 0.5f;//0.5 seconds between checks.
        //if (skills.getTotalReclaim() == 0) return;
        for (ArrayList<ArrayList<WeaponAPI>> weponsT : wepons){
            for (ArrayList<WeaponAPI> a : weponsT) {
                for (WeaponAPI b : a) {
                    if (b.getAmmo() != 0) continue;
                    Pair<Double,Float> pair = getStatsForGun(b);
                    double cost = pair.one;
                    if (cost < lowestCurrentCost || lowestCurrentCost == 0) lowestCurrentCost = cost;
                    if (skills.getTotalReclaim() >= skills.getModifiedCost(cost)) {
                        skills.useReclaim(skills.getModifiedCost(cost));
                        cooldown = pair.two;
                        coolDownInstance = cooldown;
                        lowestCurrentCost = 0;
                        //b.getAmmoTracker().setAmmo(b.getAmmoTracker().getMaxAmmo());
                        b.setAmmo(b.getMaxAmmo());
                        playSoundIfPlayerShip();
                        onCooldown = true;
                        animate(b);
                        //log.info("forging missile...");
                        return;
                    }
                }
            }
        }
        //log.info("got current lowest cost as: "+lowestCurrentCost);
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
        cost = cost * ( (double) b.getMaxAmmo() / (double) Math.max(1,b.getSpec().getMaxAmmo()));

        Pair<Double,Float> out = new Pair<>();
        out.one = cost;
        out.two = cooldownT;
        return out;
    }
    private void playSoundIfPlayerShip(){
        if (!ship.equals(Global.getCombatEngine().getPlayerShip())) return;
    }

    private void animate(WeaponAPI b){
    }
    @Override
    public void displayStats() {
    }

    @Override
    public double getMaxCost() {
        return maxCost;
    }
}
