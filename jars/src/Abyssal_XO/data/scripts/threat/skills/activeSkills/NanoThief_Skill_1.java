package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.threat.skills.NanoThief_1;

public class NanoThief_Skill_1 extends NanoThief_SkillBase {
    private double repairPercentage;
    public NanoThief_Skill_1(NanoThief_ShipSkills skills) {
        super(skills);
    }

    @Override
    public void advance(float amount) {
        if (skills.ship.getHitpoints() >= skills.ship.getMaxHitpoints()) return;
        float repairSpeed = currentSpeed(skills.ship.getHitpoints(),skills.ship.getMaxHitpoints()) * amount;
        if (repairSpeed * NanoThief_1.getReclaimPerHull() >= skills.getTotalReclaim()){
            repairSpeed = (float) (skills.getTotalReclaim() * NanoThief_1.getReclaimPerHull());
        }
        if (repairSpeed <= 0) return;
        float hp = Math.min(skills.ship.getMaxHitpoints(), skills.ship.getHitpoints()+repairSpeed);
        skills.ship.setHitpoints(hp);

    }
    public static float currentSpeed(float hull,float maxHull){//this is HP per second.
        //this returns the hull repaired every second.
        float percent = maxHull / hull;
        if (percent >= NanoThief_1.getHullMax()) return NanoThief_1.getSpeedMax()*maxHull;
        if (percent <= NanoThief_1.getHullMin()) return NanoThief_1.getSpeedMin()*maxHull;
        //float thing  = NanoThief_1.getHullMax() - NanoThief_1.getHullMin();
        percent -= NanoThief_1.getHullMin();//
        percent /= NanoThief_1.getHullRange();
        percent *= NanoThief_1.getHullRepairAdvradge();
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
