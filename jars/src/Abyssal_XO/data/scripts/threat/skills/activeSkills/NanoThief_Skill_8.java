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
            addItemToOrganizedList(b,ships,available);
            totalSize++;

        }
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
            if (gone >= availableReclaim) return;//return when all reclaim used up. a backup.
        }
    }
    private double getOrder(NanoThief_ShipSkills b,int available){
        double distance = (Misc.getDistance(ship.getLocation(),b.ship.getLocation()) * NanoThief_8.distanceTargetMulti);
        double reclaim = ((available-b.getTotalReclaimIncludingIncomeing())*NanoThief_8.reclaimTargetMulti);
        double valueA = reclaim - distance;
        return valueA;
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
