package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.animation.NanoThief_A_ReclaimSpawn;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_RecreationScript;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_8;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.util.Pair;
import lombok.Getter;

import java.util.ArrayList;

public class NanoThief_Skill_8 extends NanoThief_SkillBase{
    public double fakeReclaim = 0;
    public int getAmountToHold;

    public NanoThief_Skill_8(NanoThief_ShipSkills skills, ShipAPI ship) {
        super(skills, ship);
    }

    @Override
    public void onAddListener() {
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
        //log.info("advancing central fabricator...");
        amount *= skills.timeflow;//most 'always advanced' skills wont be effected by timeflow. But this one is.
        refineReclaim(amount);
        time+=amount;
        if (time >= 10){
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
        //log.info("attempting to create reclaim packages");
        if (skills.getTotalReclaim() < skills.getModifiedCost(getAmountToHold+1000)) return;
        ArrayList<ShipAPI> targets = new ArrayList<>();
        int muti = ((int) (skills.getTotalReclaim() / 1000f)) - 1;//cannot give more reclaim them I have.  //Integer.MAX_VALUE;
        int maxSpend = (int) ((skills.getTotalReclaim() - skills.getModifiedCost(getAmountToHold))/1000f);
        skills.stats.makeSureSavedShipsAreAlive();
        for (ShipAPI a : skills.stats.getAvailableShips()){
            if (!skills.stats.canAcceptReclaim(a)) continue;
            if (a.equals(skills.stats.getCentralFab())) continue;
            NanoThief_ShipSkills b = skills.stats.getSkills(a);
            if (b == null) continue;
            int spendType = (int) (b.getTotalReclaimIncludingIncomeing() / 1000f);
            //Settings.log.info(b.ship.getName()+" spend type as: "+spendType);
            spendType = Math.max(1,spendType);
            if (spendType < muti){
                muti = spendType;
                targets = new ArrayList<>();
                targets.add(a);
                continue;
            }
            if (spendType == muti) targets.add(a);
        }
        //log.info("  got "+targets.size()+" targets of multi: "+muti+" and max spend: "+maxSpend);
        if (muti > maxSpend) return;
        /*
        so, spend per target:
            at the end of the calculation, The central fabracator should hold just as mush reclaim as targets.

            say I have 10 targets with 1k reclaim, and I hold 110k reclaim.
            110k / (10+1) = 11k (per unit). (but I have 10k total. so....)
            110k-(1k*10) / (10+1) = 9k (per unit)
            give = (totalReclaim-totalHeldTargetReclaim) / targets + 1

            say I have 1 targets with 0k reclaim, and I hold 50k reclaim
            50k-0k*1 / (1+1) = 25k.
            this EQ hold.
        */

        double hardMin = skills.getModifiedCost(getAmountToHold);
        double availbleReclaim = skills.getTotalReclaim();
        int toSpendPerTarget = (int) ((availbleReclaim - (muti*1000*targets.size())) / (targets.size()+1));
        toSpendPerTarget = Math.max(1000,toSpendPerTarget);
        int spent = 0;
        //log.info("available: "+availbleReclaim+", spendPerTarget: "+toSpendPerTarget+", targets: "+targets.size()+", "+hardMin);
        for (int a = 0; spent+toSpendPerTarget <= availbleReclaim-spent-toSpendPerTarget && a < targets.size(); a++){
            if (availbleReclaim-spent-toSpendPerTarget < hardMin){
                toSpendPerTarget = (int) (availbleReclaim-spent-hardMin);
                if (toSpendPerTarget < 1000) return;
                //log.info("  creating reclaim package with "+toSpendPerTarget+" reclaim in it (backup)");
                createReclaimPackage(toSpendPerTarget,targets.get(a));
                //spent+=toSpendPerTarget;
                skills.useReclaim(toSpendPerTarget);
                return;
            }
            //log.info("  creating reclaim package with "+toSpendPerTarget+" reclaim in it");
            createReclaimPackage(toSpendPerTarget,targets.get(a));
            spent+=toSpendPerTarget;
            skills.useReclaim(toSpendPerTarget);
        }
    }
    public void createReclaimPackage(int spendPerTarget, ShipAPI target){
        Pair<Nano_Thief_Stats,Integer> data = new Pair<>();
        data.one = skills.stats;
        data.two = ship.getOriginalOwner();
        Global.getCombatEngine().addPlugin(new NanoThief_A_ReclaimSpawn(data,ship, 1f,spendPerTarget,true,true,target));
        //skills.stats.createReclaim(ship,ship.getOriginalOwner(),spendPerTarget,true,target);

    }
    public void refineReclaim(float amount){
        double mutli = (fakeReclaim / NanoThief_8.reclaimPerSpeedBost);
        double cost = Math.min(amount*((NanoThief_8.reclaimPerSecondPerBost*mutli)+NanoThief_8.reclaimPerSecondBase), fakeReclaim);
        double gainTemp = 1+(skills.stats.skillMulti[8]*(NanoThief_8.reclaimRaito-1));
        if (ship.getVariant().getSMods().contains("Abyssal_XO_CF")){
            gainTemp += NanoThief_8.sModBonus-1;
        }
        double gain = gainTemp*cost;
        //log.info("get refined stats as: "+mutli+", "+cost+", "+gain);
        if (gain <= 0) return;
        fakeReclaim-= cost;
        skills.addReclaim(gain);
    }
}
