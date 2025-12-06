package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.threat.skills.NanoThief_8;
import com.fs.starfarer.api.combat.ShipAPI;
import lombok.Getter;

import java.util.ArrayList;

public class NanoThief_Skill_8 extends NanoThief_SkillBase{
    public double fakeReclaim = 0;
    public int getAmountToHold;

    public NanoThief_Skill_8(NanoThief_ShipSkills skills, ShipAPI ship) {
        super(skills, ship);
        getAmountToHold = (int) Math.max(NanoThief_8.keeptReclaim,skills.getMaxUse()*NanoThief_8.keeptReclaimAbility);
    }

    @Override
    public boolean alwaysAdvance() {
        return true;
    }

    //not needed because I am not adding this skill normaly.
    /*@Override
    public boolean shouldUse(ShipAPI ship) {
        return ship.equals(skills.stats.getCentralFab());
    }*/
    private float time = 0;
    @Override
    public void advance(float amount) {
        log.info("advancing central fabricator...");
        amount *= skills.timeflow;//most 'always advanced' skills wont be effected by timeflow. But this one is.
        refineReclaim(amount);
        time+=amount;
        if (time > 1){
            createReclaimIfRequired();
            time = 0;
        }

    }
    public void createReclaimIfRequired(){
        /*
        so here is were the magic happens.
        so, what I want:
        1) I want to beable to send reclaim to multiple ships all at once, if it is necessary.

        so: I divide the amount of reclaim I can send out into groups of 1K.
            so if I have 10K reclaim, and need to hold 4, I havev 6K reclaim to send.
            but, my ship should always hold more reclaim then it is going to send.
            run though all ships, and divide them into 'send category'.
        */
        if (skills.getTotalReclaim() < getAmountToHold+1000) return;
        ArrayList<ShipAPI> targets = new ArrayList<>();
        int muti = Integer.MAX_VALUE;
        int maxSpend = (int) ((skills.getTotalReclaim() - getAmountToHold)/1000);
        skills.stats.makeSureSavedShipsAreAlive();
        for (ShipAPI a : skills.stats.getAvailableShips()){
            NanoThief_ShipSkills b = skills.stats.getSkills(a);
            if (b == null) continue;
            int spendType = (int) (b.getTotalReclaimIncludingIncomeing() / 1000);
            if (spendType < muti){
                muti = spendType;
                targets = new ArrayList<>();
                targets.add(a);
                continue;
            }
            if (spendType == muti) targets.add(a);
        }
        if (muti >= maxSpend) return;
        //maxSpend*1000;
        int maxTargets = maxSpend / muti;
        int spendPerTarget = Math.min(targets.size(),maxTargets);
        spendPerTarget = (int) (spendPerTarget / (skills.getTotalReclaim() - getAmountToHold));
        for (int a = 0; a <= maxTargets && a < targets.size(); a++){
            createReclaimPackage(spendPerTarget,targets.get(a));
            skills.useReclaim(spendPerTarget);
        }
    }
    public void createReclaimPackage(int spendPerTarget, ShipAPI target){
        //todo: make it so the reclaim package calls little nano swarms from the fabracator. (like it does for salvage)
        skills.stats.createReclaim(ship,ship.getOriginalOwner(),spendPerTarget,true,target);

    }
    public void refineReclaim(float amount){
        double mutli = 1+(NanoThief_8.speedPerBost * (fakeReclaim / NanoThief_8.reclaimPerSpeedBost));
        double cost = Math.min(NanoThief_8.reclaimCost*amount*mutli, fakeReclaim);
        double gain = NanoThief_8.reclaimRaito*cost;
        log.info("get refined stats as: "+mutli+", "+cost+", "+gain);
        if (gain <= 0) return;
        fakeReclaim-= cost;
        skills.addReclaim(gain);
    }
}
