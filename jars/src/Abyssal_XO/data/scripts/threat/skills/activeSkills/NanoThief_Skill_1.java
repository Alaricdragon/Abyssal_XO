package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.threat.skills.NanoThief_1;
import com.fs.starfarer.api.Global;
import org.apache.log4j.Logger;

public class NanoThief_Skill_1 extends NanoThief_SkillBase {
    private double repairPercentage;
    private static Logger log = Global.getLogger(NanoThief_SkillBase.class);
    public NanoThief_Skill_1(NanoThief_ShipSkills skills) {
        super(skills);
    }

    @Override
    public void advance(float amount) {
        if (amount == 0) return;
        if (skills.ship.getHitpoints() >= skills.ship.getMaxHitpoints()) return;
        double repairSpeed = currentSpeed(skills.ship.getHitpoints(),skills.ship.getMaxHitpoints()) * amount;
        if (repairSpeed / NanoThief_1.getHullPerReclaim() > skills.getTotalReclaim()){
            repairSpeed = (double) (NanoThief_1.getHullPerReclaim() * skills.getTotalReclaim());//(skills.getTotalReclaim() * NanoThief_1.getHullPerReclaim());
        }
        // max/100 * speed/
        if (repairSpeed <= 0) return;
        log.info("repairing at: amount, repairSpeed, speedPerSecond: "+amount+", "+repairSpeed+", "+(repairSpeed/amount));
        log.info("  max hp: "+skills.ship.getMaxHitpoints());
        log.info("  currentHP: "+skills.ship.getHitpoints());
        log.info("  percent per second, suppose to be percent per second:" + (((repairSpeed/amount)) / skills.ship.getMaxHitpoints())+", "+(currentSpeedLogs(skills.ship.getHitpoints(),skills.ship.getMaxHitpoints()) / skills.ship.getMaxHitpoints()));
        repairSpeed = Math.min(skills.ship.getMaxHitpoints()-skills.ship.getHitpoints(),repairSpeed);
        skills.ship.setHitpoints((float) (skills.ship.getHitpoints() + repairSpeed));//Math.min(skills.ship.getMaxHitpoints(),skills.ship.getHitpoints() + repairSpeed));
        log.info("  got final HP as: "+skills.ship.getHitpoints());
        skills.useReclaim(repairSpeed/NanoThief_1.getHullPerReclaim());

    }
    public static double currentSpeedLogs(float hull,float maxHull){//this is HP per second.
        //this returns the hull repaired every second.
        double percent = hull / maxHull; //200 / 50 = 4. //50 / 200 = 0.25
        if (percent >= NanoThief_1.getHullMax()) return NanoThief_1.getSpeedMax()*maxHull;
        if (percent <= NanoThief_1.getHullMin()) return NanoThief_1.getSpeedMin()*maxHull;
        //float thing  = NanoThief_1.getHullMax() - NanoThief_1.getHullMin();
        // (100 / range) * (? / percent) ?
        // 1 / 0.5 = 2.
        // 0.5 / 1 = 0.5
        //note: percent needs to be reduced by min.
        // R/100    P/?     = P*100/R/100 // the /100 at the end is because we want the range out of 1. so maybe....
        // R/1      P/?     =P/R        //P = 0.5, R = 0.9.
        log.info("      CS: percent: "+percent);
        percent -= NanoThief_1.getHullMin();//
        percent = 1 - percent;
        log.info("      CS: rounded: "+percent);
        percent = (percent) * NanoThief_1.getHullRange();
        log.info("      CS: cross multied: "+percent);
        percent = (percent * NanoThief_1.getHullRepairThing())+NanoThief_1.getSpeedMin();
        log.info("      CS: final: "+percent);//((int)(percent*1000))/1000d);//to get rid of long values.
        log.info("      CS: final: "+((int)(percent*1000))/1000d);//to get rid of long values.
        return percent * maxHull;
        // H / maxH  *  ? / thing.
        //thing * H / maxH = percent of total value.
        /*
         * what do I want? I want to get what % of the way the percent of hull is through hullmax - hullmin. (example: max = 0.9, min = 0.05, total = 0.85)
         * so first thing: take percent-hullMin and / by 'range' to get the percent of effect.
         * secondly, to get effect it is maxRepair + minRepair / 2 to get advradge repair
         *
         *
         * */
    }
    public static double currentSpeed(float hull,float maxHull){//this is HP per second.
        //this returns the hull repaired every second.
        double percent = hull / maxHull; //200 / 50 = 4. //50 / 200 = 0.25
        if (percent >= NanoThief_1.getHullMax()) return NanoThief_1.getSpeedMax()*maxHull;
        if (percent <= NanoThief_1.getHullMin()) return NanoThief_1.getSpeedMin()*maxHull;
        //float thing  = NanoThief_1.getHullMax() - NanoThief_1.getHullMin();
        // range / 100 * percent / ?
        //note: percent needs to be reduced by min.
        percent -= NanoThief_1.getHullMin();//
        percent = 1 - percent;
        percent = (percent) * NanoThief_1.getHullRange();
        percent = (percent * NanoThief_1.getHullRepairThing())+NanoThief_1.getSpeedMin();
        return percent * maxHull;
        // H / maxH  *  ? / thing.
        //thing * H / maxH = percent of total value.
        /*
        * what do I want? I want to get what % of the way the percent of hull is through hullmax - hullmin. (example: max = 0.9, min = 0.05, total = 0.85)
        * so first thing: take percent-hullMin and / by 'range' to get the percent of effect.
        * secondly, to get effect it is maxRepair + minRepair / 2 to get advradge repair
        *
        *
        * */
    }
}
