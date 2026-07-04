package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.threat.skills.NanoThief_8;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_9;
import com.fs.starfarer.api.combat.ShipAPI;

public class NanoThief_Skill_9_3 extends NanoThief_SkillBase{
    public NanoThief_Skill_9_3(NanoThief_ShipSkills skills, ShipAPI ship) {
        super(skills, ship);
    }
    public boolean alwaysAdvance() {
        return true;
    }
    public int getAmountToHold;
    @Override
    public void onAddListener() {
        getAmountToHold = (int) Math.max(NanoThief_9.keptReclaim,skills.getMaxUse()*NanoThief_9.keptReclaimAbility);
    }
    float timeLeft = 0;
    public void advance(float amount) {
        if (timeLeft >= 0){
            timeLeft-=amount;
            return;
        }
        timeLeft = 0.25f;
        if (skills.getTotalReclaim() <= getAmountToHold){
            skills.removeSpeedMod("9_3");
            skills.removeCostMod("9_3");
            return;
        }
        double multi = (skills.getTotalReclaim() - getAmountToHold) / NanoThief_9.keptReclaimReclaimPerBoast;
        skills.addCostMod("9_3",(multi*NanoThief_9.keptReclaimCost) * skills.stats.skillMulti[9]);
        skills.addSpeedMod("9_3", (float) ((multi*NanoThief_9.keptReclaimSpeed)* skills.stats.skillMulti[9]));
    }
}
