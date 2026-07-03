package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.animation.NanoThief_A_Skill_2;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_2;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.util.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.Set;

public class NanoThief_Skill_2 extends NanoThief_SkillBase {
    ArrayList<WeaponData> wepons = new ArrayList<>();
    private double maxCost = 0;
    public NanoThief_Skill_2(NanoThief_ShipSkills skills, ShipAPI ship) {
        super(skills,ship);
        //skills.ship.getVariant().getWeaponGroups().get(0).getSlots();
        prepareForSingleModule(ship);
        for (ShipAPI b : skills.getChildShips()){
            prepareForSingleModule(b);
        }
        //if (wepons.isEmpty()){
            //skills.suppressListener(this);
        //}
    }
    private void prepareForSingleModule(ShipAPI ship){
        //ArrayList<ArrayList<WeaponAPI>> weponsT = new ArrayList<>();
        //weponsT.add(new ArrayList<>());
        //weponsT.add(new ArrayList<>());
        //weponsT.add(new ArrayList<>());
        for (WeaponAPI a : ship.getAllWeapons()){
            //a.getAmmoTracker().setAmmo(0);
            if (a.getMaxAmmo() == 0) continue;
            if (a.getAmmoTracker().getAmmoPerSecond() != 0) continue;
            if (!a.getAmmoTracker().usesAmmo()) continue;
            if (a.getSlot().isBuiltIn()) continue;
            //int b = 0;
            //if (a.getSpec().getSize().equals(WeaponAPI.WeaponSize.SMALL)) b = 2;
            //if (a.getSpec().getSize().equals(WeaponAPI.WeaponSize.MEDIUM)) b = 1;
            //if (a.getSpec().getSize().equals(WeaponAPI.WeaponSize.LARGE)) b = 0;
            //weponsT.get(b).add(a);
            //Settings.log.info("got weapon of id: "+a.getSpec().getWeaponId());
            WeaponData temp = new WeaponData(a,skills);
            wepons.add(temp);
            //Pair<Double, Float> pair = getStatsForGun(a);
            if (temp.totalCost > maxCost) maxCost = temp.totalCost;
        }
        //if (!weponsT.get(0).isEmpty() || !weponsT.get(1).isEmpty() || !weponsT.get(2).isEmpty()){
        //    wepons.add(weponsT);
        //}
    }

    @Override
    public boolean shouldUse(ShipAPI ship) {
        return !wepons.isEmpty();
    }

    public float cooldown = 0f;
    public float coolDownInstance = 0f;
    public float secondsPerReceack = 1f;
    public boolean onCooldown = false;

    public double lowestCurrentCost = 0;
    @Override
    public void advance(float amount) {
        cooldown-=amount;
        if (cooldown > 0) return;
        if (ship.isFinishedLanding()){
            onCooldown = false;
            cooldown=secondsPerReceack;
            return;
        }
        //cooldown = 0.5f;//0.5 seconds between checks.
        //if (skills.getTotalReclaim() == 0) return;
        //int start = (int) (Math.random()*wepons.size());//starting location. will loop untill it reaches this again.
        //Settings.log.info("got start as: "+start+", of size:"+wepons.size());
        lowestCurrentCost = 0;
        ArrayList<WeaponData> canWeapons = new ArrayList<>();
        for (WeaponData a : wepons){
            //if (c >= wepons.size()) c = 0;
            //Settings.log.info("got c as: "+c);
            //WeaponData a = wepons.get(c);
            if (!a.isReady()) continue;
            if (a.minCost < lowestCurrentCost || lowestCurrentCost == 0) lowestCurrentCost = a.minCost;//min cost of a reload.
            if (skills.getModifiedCost(a.minCost) > skills.getTotalReclaim()) continue;
            a.getStatsForGun();//recalculates stats for the gun here.
            canWeapons.add(a);
            /*
            if (!a.isReady()){
                if (start == c) break;//break for end of loop eq.
                continue;
            }
            Pair<Double,Float> pair = a.getCurrentCost();
            double cost = pair.one;
            if (cost < lowestCurrentCost || lowestCurrentCost == 0) lowestCurrentCost = cost;
            if (skills.getTotalReclaim() >= skills.getModifiedCost(cost)){
                cooldown = pair.two;
                coolDownInstance = cooldown;
                lowestCurrentCost = 0;
                animate(a.b,(cooldown*NanoThief_2.animationTimeMult)+1);
                playSoundIfPlayerShip();
                onCooldown = true;
                skills.useReclaim(skills.getModifiedCost(cost));
                a.b.setAmmo(a.b.getMaxAmmo());
                return;
            }
            if (start == c) break;//break for end of loop eq.
             */
        }

        if (!canWeapons.isEmpty()){
            WeaponData a = canWeapons.get((int) (Math.random()*canWeapons.size()));

            //Pair<Double,Float> pair = a.getCurrentCost();
            int multi = (int) Math.min(skills.getTotalReclaim()/skills.getModifiedCost(a.costPer),a.b.getMaxAmmo()-a.b.getAmmoTracker().getAmmo());
            double cost = multi * a.costPer;
            Settings.log.info("got multi and cost as: "+multi+", "+cost);
            //if (cost < lowestCurrentCost || lowestCurrentCost == 0) lowestCurrentCost = cost;
            if (skills.getTotalReclaim() >= skills.getModifiedCost(cost)){
                cooldown = multi * a.cooldownPer;
                coolDownInstance = cooldown;
                lowestCurrentCost = 0;
                onCooldown = true;
                animate(a.b,(cooldown*NanoThief_2.animationTimeMult)+1);
                playSoundIfPlayerShip();
                skills.useReclaim(skills.getModifiedCost(cost));
                a.b.setAmmo(a.b.getAmmoTracker().getAmmo()+multi);
                return;
            }
        }
        //log.info("got current lowest cost as: "+lowestCurrentCost);
        onCooldown = false;
        cooldown=secondsPerReceack;
    }
    private void playSoundIfPlayerShip(){
        if (!ship.equals(Global.getCombatEngine().getPlayerShip())) return;

    }

    private void animate(WeaponAPI b,float time){
        new NanoThief_A_Skill_2(b,time);
        //b.getGlowSpriteAPI();//
        //b.setGlowAmount(100,new Color(255,255,255));

        //ship.setJitter(Settings.DISPLAYID_NANOTHIEF+"_skill_4", jitterColor, 1, 2, 0f, 5);
        //ship.setJitterUnder(Settings.DISPLAYID_NANOTHIEF+"_skill_4", jitterUnderColor, 1, 25, 0f, 7);
        //b;
    }
    @Override
    public void displayStats() {
    }

    @Override
    public double getMaxCost() {
        return maxCost;
    }
}
class WeaponData{
    protected int minReload;//min amount of ammo per reload
    protected float minCooldown;
    protected double minCost;//min cost per reload.
    protected double totalCost;
    protected float cooldown;
    protected NanoThief_ShipSkills skills;
    WeaponAPI b;
    protected double costPer;
    protected float cooldownPer;
    protected WeaponData(WeaponAPI b,NanoThief_ShipSkills skills){
        this.b = b;
        this.skills = skills;
        getStatsForGun();
    }
    protected boolean isReady(){
        //Settings.log.info("got 'ready' ammo as: max,current,needed,responce: "+b.getMaxAmmo()+", "+b.getAmmo()+", "+minReload+", "+(b.getMaxAmmo()-b.getAmmo() >= minReload));
        return b.getAmmo() == 0 || b.getMaxAmmo()-b.getAmmo() >= minReload; //4-3 = 0. 1
    }
    protected Pair<Double,Float> getCurrentCost(){
        Pair<Double, Float> out = new Pair<Double,Float>();
        getStatsForGun();
        if (b.getAmmo() == 0){
            out.one = totalCost;
            out.two = cooldown;
            return out;
        }
        double reloads = ((double) (b.getMaxAmmo() - b.getAmmo()) / minReload);
        out.one = minCost*reloads;
        out.two = (float) (minCooldown * reloads);
        return out;
    }
    public void getStatsForGun(){
        double costPerOpp = 0;
        float cooldownT = 0;
        double costBase = 0;
        int op = 0;
        if (b.getSpec().getSize().equals(WeaponAPI.WeaponSize.SMALL)) {
            costPerOpp = NanoThief_2.costSmall;
            cooldownT = NanoThief_2.timeSmall;
            costBase = NanoThief_2.baseCostSmall;
        }
        if (b.getSpec().getSize().equals(WeaponAPI.WeaponSize.MEDIUM)) {
            costPerOpp = NanoThief_2.costMid;
            cooldownT = NanoThief_2.timeMid;
            costBase = NanoThief_2.baseCostMid;
        }
        if (b.getSpec().getSize().equals(WeaponAPI.WeaponSize.LARGE)) {
            costPerOpp = NanoThief_2.costLarge;
            cooldownT = NanoThief_2.timeLarge;
            costBase = NanoThief_2.baseCostLarge;
        }
        double cost = Math.max(b.getSpec().getOrdnancePointCost(skills.ship.getCaptain().getStats(), skills.ship.getMutableStats()),1) * costPerOpp;
        cost = cost * ( (double) b.getMaxAmmo() / (double) Math.max(0,b.getSpec().getMaxAmmo()));
        cost += costBase;

        //Pair<Double,Float> out = new Pair<>();
        //out.one = cost;
        //out.two = cooldownT;
        totalCost = cost;
        cooldown = cooldownT;


        minReload = (int) Math.min(b.getMaxAmmo(),b.getSpec().getBurstSize());
        minCost = ((double) minReload / b.getMaxAmmo()) * totalCost;
        minCooldown = ((float) minReload / b.getMaxAmmo()) * cooldown;

        costPer = cost / b.getMaxAmmo();
        cooldownPer = cooldown / b.getMaxAmmo();
        //Settings.log.info("min reload, cost, cooldown: "+minReload+", "+minCost+", "+minCooldown);


        // 5, 10 = 5 / 10 = 0.5.
        //return out;
    }
}
