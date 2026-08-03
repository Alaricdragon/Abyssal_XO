package Abyssal_XO.data.scripts.shroudDweller.activeSkills;

import Abyssal_XO.data.scripts.shroudDweller.ShroudDweller_Stats;
import lombok.Getter;

public abstract class ShroudDweller_ActiveSkillBase {
    protected float skillEntropy, skillTimeBase, skillTimeReducePerCharge;
    protected ShroudDweller_Stats stats;
    ShroudDweller_ActiveSkillBase(float skillEntropy, float skillTimeBase, float skillTimeReducePerCharge, ShroudDweller_Stats stats){
        cooldown = skillTimeBase;
        this.skillEntropy = skillEntropy;
        this.skillTimeBase = skillTimeBase;
        this.skillTimeReducePerCharge = skillTimeReducePerCharge;
        this.stats = stats;
    }
    //so basicly... this needs a few things
    @Getter
    protected int charges;
    protected float charge;
    protected float cooldown;
    public void advance(float time){
        cooldown -= time;
        if (cooldown > 0) return;
        if (getCharges() == 0) cooldown = calculateChargeTime();
        int actavate = 0;
        do {
            actavate++;
            cooldown = calculateChargeTime(getCharges()-actavate);
        }while (cooldown <= 0);
        activateSkill(actavate);
        charges-=actavate;
    }
    protected float calculateChargeTime(){
        return calculateChargeTime(getCharges());
    }
    protected float calculateChargeTime(int charges){
        return skillTimeBase - (skillTimeReducePerCharge*charges);
    }
    protected float entropyTemp = 0;
    public void advanceEntropy(float entropy){
        entropyTemp+=entropy;
        if (entropyTemp < skillEntropy) return;
        int gain = (int) (entropyTemp / skillEntropy);
        charge+= gain;
        entropyTemp -= gain * skillEntropy;
    }
    public abstract void activateSkill(int times);
}
