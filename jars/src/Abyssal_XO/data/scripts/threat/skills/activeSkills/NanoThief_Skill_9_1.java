package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.threat.skills.NanoThief_9;
import com.fs.starfarer.api.combat.ShipAPI;

public class NanoThief_Skill_9_1 extends NanoThief_SkillBase{
    public NanoThief_Skill_9_1(NanoThief_ShipSkills skills, ShipAPI ship) {
        super(skills, ship);
    }
    @Override
    public boolean alwaysAdvance() {
        //not always advance, because effects only happen when reclaim is available.
        return false;
    }
    float time = 1;
    float crRegen = 0;
    @Override
    public void advance(float amount) {
        if (time >= 0){
            time -= amount;
            return;
        }
        time -= amount;
        time = -(time-1);
        if (ship.getCurrentCR() >= NanoThief_9.crStart){
            skills.removeCostMod("9_1");
            skills.removeSpeedMod("9_1");
            time = 1;
            return;
        }
        advanceCrRepair(time);
        skills.addSpeedMod("9_1", (float) NanoThief_9.crSkillSpeed(ship.getCurrentCR()));
        skills.addCostMod("9_1",NanoThief_9.crSkillCost(ship.getCurrentCR()));
        time = 1;
    }
    public void adjustCRSpeed(){
        if (ship.getCurrentCR() >= NanoThief_9.crStart){
            crRegen = 0;
            return;
        }
        crRegen = (float) NanoThief_9.crRegenSpeed(ship.getCurrentCR()) / 100f;

    }
    public void advanceCrRepair(float amount){
        adjustCRSpeed();
        if (crRegen <= 0) return;
        float crRegen2 = crRegen*amount;
        double cost = skills.getModifiedCost(crRegen2*NanoThief_9.reclaimCostPerCR* skills.stats.skillMulti[9]);
        cost = Math.min(cost,skills.getTotalReclaim());
        //double newCR = NanoThief_9.reclaimCostPerCR*(cost - ((skills.costMulti-1)*cost));
        double newCR = cost / skills.getModifiedCost(NanoThief_9.reclaimCostPerCR* skills.stats.skillMulti[9]);
        ship.setCurrentCR((float) (newCR+ship.getCurrentCR()));
        skills.useReclaim(cost);
    }
}
