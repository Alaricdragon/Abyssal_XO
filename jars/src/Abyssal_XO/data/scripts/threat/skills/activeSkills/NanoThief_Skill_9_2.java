package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.threat.skills.NanoThief_9;
import com.fs.starfarer.api.combat.ShipAPI;

public class NanoThief_Skill_9_2 extends NanoThief_SkillBase{
    public NanoThief_Skill_9_2(NanoThief_ShipSkills skills, ShipAPI ship) {
        super(skills, ship);
    }
    @Override
    public boolean alwaysAdvance() {
        return true;
    }

    @Override
    public void advance(float amount) {
        //log.info("running advance for overload changes.");
        if (isBuffed && buffTimeLeft <= 0){
            isBuffed = false;
            skills.removeSpeedMod("9_2");
            skills.removeCostMod("9_2");
        }
        if (recharging){
            rechargeTime-=amount;
            if (rechargeTime <= 0) recharging = false;
            return;
        }
        if (ship.getFluxTracker().isOverloaded()) activate();
    }
    float rechargeTime = 0;
    boolean isBuffed = false;
    boolean recharging = false;
    //two states:
    //1: waiting.
    //2: recharging (untill overload is done)

    //2 buff stats:
    //when ability starts, buff last untill timer is out.
    //when timer is out, remove buffs.
    float buffTimeLeft = 0;
    public void activate(){
        isBuffed = true;
        buffTimeLeft = (float) NanoThief_9.overloadEffectTime;
        double ct = ship.getFluxTracker().getOverloadTimeRemaining();
        //log.info("running overload data with a time of: "+ct);
        if (ct <= 1) return;

        float removedTime = (float) (ct * NanoThief_9.overloadRemoved);
        if (ct - removedTime < 1){
            removedTime = (float) (ct - 1);//ct - removeTime = ?. make this = 1. ct is static. ct - removedTime = 1: removedTime must be removedTime + 1 = ct.
        }
        //log.info("got base removed time as: "+removedTime);

        double cost = skills.getModifiedCost((removedTime)*NanoThief_9.overloadCostPerSecond);
        cost = Math.min(cost,skills.getTotalReclaim());
        removedTime = (float) (cost / skills.getModifiedCost(NanoThief_9.overloadCostPerSecond));

        //log.info("got cost modified remove time as: "+removedTime);
        if (ct - removedTime <= 1) return;
        rechargeTime = (float) (ct-removedTime);
        recharging = true;
        ship.getFluxTracker().setOverloadDuration((float) (ct-removedTime));
        skills.addCostMod("9_2",NanoThief_9.overloadSkillCost);
        skills.addSpeedMod("9_2", (float) (NanoThief_9.overloadSkillSpeed));
        //log.info("set overload time to "+(ct-removedTime)+" with "+cost+" reclaim");
    }
}
