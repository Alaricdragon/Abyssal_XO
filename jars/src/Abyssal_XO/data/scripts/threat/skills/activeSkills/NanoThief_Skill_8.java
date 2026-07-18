package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.animation.NanoThief_A_ReclaimSpawn;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_RecreationScript;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_8;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;

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
        if (time >= 3){
            createReclaimIfRequired();
            time = 0;
        }

    }
    public void createReclaimIfRequired(){
        if (skills.getTotalReclaim() < skills.getModifiedCost(getAmountToHold+1000)) return;
        int available = (int) (skills.getTotalReclaim() - skills.getModifiedCost(getAmountToHold));
        ArrayList<NanoThief_ShipSkills> ships = new ArrayList<>();
        int lowestReclaim;// in groups devided by 1000
        int totalSize=0;
        //Settings.log.info("CENTRAL FABRACATOR: looking for ships.");
        for (ShipAPI a : skills.stats.getAvailableShips()) {
            if (!skills.stats.canAcceptReclaim(a)) continue;
            if (a.equals(skills.stats.getCentralFab())) continue;
            NanoThief_ShipSkills b = skills.stats.getSkills(a);
            if (b == null) continue;
            int total = (int) b.getTotalReclaimIncludingIncomeing();
            if (total > available - 1000) continue;//only send packages in groups of 1k at least.
            //Settings.log.info("CENTRAL FABRACATOR:  found a ship of id: "+b.ship.getId()+". adding to array..." );
            //int groupID = total / 1000;
            //ArrayList<NanoThief_ShipSkills> group = ships.getOrDefault(groupID,new ArrayList<>());
            //organize by distance. not by reclaim.
            //todo: change this to be a 'value' based on missing reclaim, and distance. with some multiplyers.
            addItemToOrganizedList(b,ships,available);
            /*String logs = "     PROGRESS: got list order as:";
            for (NanoThief_ShipSkills E : ships){
                logs+=(int)getOrder(E,available)+",";
            }
            Settings.log.info(logs);*/
            //ships.put(groupID,group);
            totalSize++;

        }
        //at this ponit, ships contains all possable targets.
        //processing target rules:
        /*
            1: I must send to the lowest reclaim group first.
            2: I must send reclaim in groups of 1k.
            3: I must send reclaim to the closest ships second.

            organizeation:
            ships are organized first by 'reclaim group'
            second by 'distance.'
         */
        /*
            how this will work:
            1: for each 1k reclaim required, add one 'share'.
            200 (so 100 to give)
            25
            10
            2

            25 = 75
            10 = 90
            2 = 98
            total = 188 + 75 = 262 shares.
            100 / 262 = 0.4 (give or take)
            75 = 25*0.4 = 6.25
            90 = 10*0.4 = 4
            2  = 98*0.4 = 39.2
         */
        /*String logs = "FINAL: got list order as:";
        for (NanoThief_ShipSkills b : ships){
            logs+=(int)getOrder(b,available)+",";
        }
        Settings.log.info(logs);*/
        ArrayList<ShipAPI> adding = new ArrayList<>();
        ArrayList<Integer> shares = new ArrayList<>();
        int totalShares = 0;
        int max = available / 1000;//max number of ships
        //Settings.log.info("CENTRAL FABRACATOR: preparing to create reclaim. got max number of service as: "+max);
        for (NanoThief_ShipSkills b : ships){
            int share = Math.max((int) ((available - b.getTotalReclaimIncludingIncomeing()) / 1000f),1);
            totalShares+=share;
            adding.add(b.ship);
            shares.add(share);
            //Settings.log.info("CENTRAL FABRACATOR:  looking reclaim: share"+share+", total:"+totalShares+" already added: "+shares.size());
            if (adding.size() >= max){
                createReclaimPackages(available,totalShares,adding,shares);
                return;
            }
        }
        createReclaimPackages(available,totalShares,adding,shares);
    }
    private void addItemToOrganizedList(NanoThief_ShipSkills b, ArrayList<NanoThief_ShipSkills> list,int available){
        //todo: fix this fucking list organization. please. Its terrable.
        /*double valueA = getOrder(b,available);
        boolean added = false;
        for (int c = 0; c < list.size(); c++){
            double valueB = getOrder(list.get(c),available);//(Misc.getDistance(ship.getLocation(),list.get(c).ship.getLocation()) * NanoThief_8.distanceTargetMulti) + ((available-list.get(c).getTotalReclaimIncludingIncomeing())*NanoThief_8.reclaimTargetMulti);
            if (valueA < valueB){
                list.add(c,b);
                added = true;
            }
        }
        if (!added) list.add(b);*/


        int target  =(list.size()-1) / 2;
        if (target == 0){
            if (list.isEmpty()){
                list.add(b);
                return;
            }
            if (getOrder(b,available) > getOrder(list.get(0),available)) list.add(0,b);
            else list.add(b);
            return;
        }
        int min = 0;
        int max = list.size() - 1;
        double valueA = getOrder(b,available);
        //1,2
        //Settings.log.info("organize list: attempting to organize list. got new item as"+valueA+"...");
        int last_max = Integer.MAX_VALUE;
        int last_min = Integer.MIN_VALUE;
        while (max != min){//target > min && target < max){
            //Settings.log.info(" min,max,target:"+min+", "+max+", "+target);
            double valueB = getOrder(list.get(target),available);
            if (valueA > valueB){
                int lastmax = max;
                max = target;
                if (max == lastmax) max--;
                //int oldTarget = target;
                target = (max+min) / 2;
                //if (oldTarget == target) target--;

                /*if (last_max == max && last_min == min){

                }
                last_max = max;*/
                //Settings.log.info("     --");
            }else if (valueA < valueB){
                int lastmin = min;
                min = target;
                if (min == lastmin) min++;
                //int oldTarget = target;
                target = (max+min) / 2;
                //if (oldTarget == target) target++;
                /*if (last_max == max && last_min == min){

                }
                last_min = min;*/
                //Settings.log.info("     ++");
            }else{
                list.add(target,b);
                //Settings.log.info("     done, placed item at slot "+b);
                return;
            }
        }
        //Settings.log.info("     running backup for target of: "+target);
        if (max == min) {
            target = max;
            //Settings.log.info("     runing equals backup of: "+target);
            if (valueA > getOrder(list.get(target), available)) list.add(target, b);
            else {
                if (target < list.size() - 1) {
                    list.add(target + 1, b);
                    return;
                }
                list.add(b);
            }
            return;
        }
        //Settings.log.info("     runing between max and min backup");
        //in any other type, the value will always be between max and min. so add at max.
        if (valueA > getOrder(list.get(min),available)) list.add(min,b);
        else if (valueA > getOrder(list.get(max),available)) list.add(max,b);
        else if (max == list.size() - 1) list.add(b);
        else list.add(max+1,b);

    }
    private void createReclaimPackages(int availableReclaim,int totalShares,ArrayList<ShipAPI> adding,ArrayList<Integer> shares){
        if (totalShares == 0) return;
        int reclaimPerShare = availableReclaim / totalShares;
        int gone = 0;
        for (int a = 0; a < adding.size(); a++){
            int used = Math.min(reclaimPerShare * shares.get(a) * 1000,1000);
            createReclaimPackage(used,adding.get(a));
            skills.useReclaim(used);
            gone+=used;
            if (gone >= availableReclaim-500) return;//return when all reclaim used up. a backup.
        }
    }
    private double getOrder(NanoThief_ShipSkills b,int available){
        double distance = (Misc.getDistance(ship.getLocation(),b.ship.getLocation()) * NanoThief_8.distanceTargetMulti);
        double reclaim = ((available-b.getTotalReclaimIncludingIncomeing())*NanoThief_8.reclaimTargetMulti);
        double valueA = reclaim - distance;
        return valueA;
    }
    public void createReclaimIfRequired_OLD(){
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
