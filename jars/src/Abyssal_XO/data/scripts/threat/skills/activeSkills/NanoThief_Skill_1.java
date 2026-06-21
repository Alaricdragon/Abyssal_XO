package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.Utils;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_1;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import org.apache.log4j.Logger;

public class NanoThief_Skill_1 extends NanoThief_SkillBase {
    private double repairPercentage;
    public NanoThief_Skill_1(NanoThief_ShipSkills skills, ShipAPI ship) {
        super(skills,ship);
    }

    @Override
    public void advance(float amount) {
        if (amount == 0) return;
        advanceSingle(amount, skills.ship);
        for (ShipAPI b : skills.getChildShips()){
            advanceSingle(amount,b);
        }
    }
    private void advanceSingle(float amount, ShipAPI ship){
        if (ship.getHitpoints() >= ship.getMaxHitpoints()) return;
        double repairSpeed = currentSpeed(ship.getHitpoints(),ship.getMaxHitpoints()) * amount;
        double costPerHull = skills.getModifiedCost(NanoThief_1.getHullPerReclaim());
        if (repairSpeed / costPerHull > skills.getTotalReclaim()){
            repairSpeed = (double) (costPerHull * skills.getTotalReclaim());//(skills.getTotalReclaim() * NanoThief_1.getHullPerReclaim());
        }
        if (repairSpeed <= 0) return;
        //log.info("repairing at: amount, repairSpeed, speedPerSecond: "+amount+", "+repairSpeed+", "+(repairSpeed/amount));
        //log.info("  max hp: "+ship.getMaxHitpoints());
        //log.info("  currentHP: "+ship.getHitpoints());
        //log.info("  percent per second, suppose to be percent per second:" + (((repairSpeed/amount)) / ship.getMaxHitpoints())+", "+(currentSpeedLogs(ship.getHitpoints(),ship.getMaxHitpoints()) / ship.getMaxHitpoints()));
        repairSpeed = Math.min(ship.getMaxHitpoints()-ship.getHitpoints(),repairSpeed);
        ship.setHitpoints((float) (ship.getHitpoints() + repairSpeed));//Math.min(ship.getMaxHitpoints(),ship.getHitpoints() + repairSpeed));
        //log.info("  got final HP as: "+ship.getHitpoints());
        skills.useReclaim(repairSpeed/costPerHull);

    }
    /*public static double currentSpeedLogs(float hull,float maxHull){//this is HP per second.
        //this returns the hull repaired every second.
        double percent = hull / maxHull; //200 / 50 = 4. //50 / 200 = 0.25
        if (percent >= NanoThief_1.getHullMax()) return NanoThief_1.getSpeedMin()*maxHull;
        if (percent <= NanoThief_1.getHullMin()) return NanoThief_1.getSpeedMax()*maxHull;
        //float thing  = NanoThief_1.getHullMax() - NanoThief_1.getHullMin();
        // (100 / range) * (? / percent) ?
        // 1 / 0.5 = 2.
        // 0.5 / 1 = 0.5
        //note: percent needs to be reduced by min.
        // R/100    P/?     = P*100/R/100 // the /100 at the end is because we want the range out of 1. so maybe....
        // R/1      P/?     =P/R        //P = 0.5, R = 0.9.
        log.info("      CS: percent: "+percent);
        //percent of the way though 'range'.
        //
        percent -= NanoThief_1.getHullMin();//
        percent = 1 - percent;
        log.info("      CS: rounded: "+percent);
        percent = (percent) * NanoThief_1.getHullRange();
        log.info("      CS: cross multied: "+percent);
        percent = (percent * NanoThief_1.getHullRepairThing())+NanoThief_1.getSpeedMin();
        //log.info("      CS: final: "+percent);//((int)(percent*1000))/1000d);//to get rid of long values.
        log.info("      CS: final: "+((int)(percent*1000))/1000d);//to get rid of long values.
        return percent * maxHull;
    }*/
    public static double currentSpeed(float hull,float maxHull){//this is HP per second.
        //this returns the hull repaired every second.
        double percent = hull / maxHull; //200 / 50 = 4. //50 / 200 = 0.25
        percent = NanoThief_1.getSpeed()*Utils.getExpenseValue(percent,1,NanoThief_1.getRepairExspensalThreshold(),NanoThief_1.getRepairExspensal());
        /*if (percent >= NanoThief_1.getHullMax()) return NanoThief_1.getSpeedMin()*maxHull;
        if (percent <= NanoThief_1.getHullMin()) return NanoThief_1.getSpeedMax()*maxHull;
        percent -= NanoThief_1.getHullMin();//
        percent = 1 - percent;
        percent = (percent) * NanoThief_1.getHullRange();
        percent = (percent * NanoThief_1.getHullRepairThing())+NanoThief_1.getSpeedMin();*/
        return percent * maxHull;
    }
}
