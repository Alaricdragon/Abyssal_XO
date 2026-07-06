package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.Settings;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.fleet.FleetMemberAPI;

import java.util.ArrayList;
import java.util.List;

public class NanoThief_MasteryShipStats {
    public FleetMemberAPI ship;
    public double weight;
    public double cost;
    public String name;
    public double reloadTime;
    public double buildTime;
    private static final String[] dmos = {
            //"comp_armor",
            //"comp_hull",
            //"degraded_engines",
            "degraded_drive_field",
            //"faulty_grid",
            //"unstable_coils",
            //"comp_structure",
            //"glitched_sensors",
            //"malfunctioning_comms",
            //"defective_manufactory",
            //"damaged_deck",
            //"fragile_subsystems",
            "comp_storage",
            "increased_maintenance",
            "erratic_injector"
            //"faulty_auto",
            //"damaged_mounts",
            //"degraded_life_support",
            //"degraded_shields"
    };
    public NanoThief_MasteryShipStats(FleetMemberAPI ship, double weight,String name){
        ArrayList<String> temp = new ArrayList<>(List.of(dmos));
        this.ship = ship;
        this.weight = weight;
        /*double costIncrease = (ship.getVariant().getSMods().size()*NanoThief_10.sModCost);
        int dmods = 0;
        for (String a : ship.getVariant().getPermaMods()){
            if (Global.getSettings().getHullModSpec(a).getTags().contains("dmod") && !temp.contains(a)) dmods++;
        }
        double costDecrease = (dmods*NanoThief_10.dModDiscount);
        costDecrease = Math.min(costDecrease,NanoThief_10.dModmin);
        cost = (getDP() * NanoThief_10.costPerDP) + NanoThief_10.baseCost;
        double smodBonus = cost * costIncrease;
        double dmodNegitive = cost * costDecrease;
        cost += smodBonus;
        cost -= dmodNegitive;*/

        cost = getBaseCost();
        cost += getSModCost();
        cost -= getDModReduction();
        //Settings.log.info("a: "+ship.getDeployCost()+", b: "+ship.getDeploymentPointsCost()+", c: "+ship.getBaseDeployCost()+", d: "+ship.getDeploymentCostSupplies());
        reloadTime = getReloadTime();
        buildTime = getBuildTime();
        this.name = name;
    }
    private float getDP(){
        return ship.getHullSpec().getHullId().equals("fabricator_unit") ? NanoThief_Base.fabracatorDPForNanothiefCalculation : ship.getDeploymentPointsCost();//ship.getHullSpec().getSuppliesToRecover();
    }
    public double getReloadTime(){
        double rechargeTimePerDP=0;
        double rechargeTimeBase=0;
        switch (ship.getHullSpec().getHullSize()){
            case FIGHTER:
            case FRIGATE:
            case DEFAULT:
                rechargeTimePerDP=NanoThief_10.rechargeTimePerDPs[0];
                rechargeTimeBase=NanoThief_10.rechargeTimeBases[0];
                break;
            case DESTROYER:
                rechargeTimePerDP=NanoThief_10.rechargeTimePerDPs[1];
                rechargeTimeBase=NanoThief_10.rechargeTimeBases[1];
                break;
            case CRUISER:
                rechargeTimePerDP=NanoThief_10.rechargeTimePerDPs[2];
                rechargeTimeBase=NanoThief_10.rechargeTimeBases[2];
                break;
            case CAPITAL_SHIP:
                rechargeTimePerDP=NanoThief_10.rechargeTimePerDPs[3];
                rechargeTimeBase=NanoThief_10.rechargeTimeBases[3];
                break;
        }
        return Math.max(rechargeTimePerDP+rechargeTimeBase,(getDP() * rechargeTimePerDP)+rechargeTimeBase);
    }
    public double getBuildTime(){
        double buildTimePerDP=0;
        double builtTimeBase=0;
        switch (ship.getHullSpec().getHullSize()){
            case FIGHTER:
            case FRIGATE:
            case DEFAULT:
                buildTimePerDP=NanoThief_10.buildTimePerDPs[0];
                builtTimeBase=NanoThief_10.builtTimeBases[0];
                break;
            case DESTROYER:
                buildTimePerDP=NanoThief_10.buildTimePerDPs[1];
                builtTimeBase=NanoThief_10.builtTimeBases[1];
                break;
            case CRUISER:
                buildTimePerDP=NanoThief_10.buildTimePerDPs[2];
                builtTimeBase=NanoThief_10.builtTimeBases[2];
                break;
            case CAPITAL_SHIP:
                buildTimePerDP=NanoThief_10.buildTimePerDPs[3];
                builtTimeBase=NanoThief_10.builtTimeBases[3];
                break;
        }
        return Math.max(buildTimePerDP+builtTimeBase,(getDP() * buildTimePerDP)+builtTimeBase);
    }
    public int getBaseCost(){
        double costPerDP=0;
        double baseCost=0;
        switch (ship.getHullSpec().getHullSize()){
            case FIGHTER:
            case FRIGATE:
            case DEFAULT:
                costPerDP=NanoThief_10.costPerDPs[0];
                baseCost=NanoThief_10.baseCosts[0];
                break;
            case DESTROYER:
                costPerDP=NanoThief_10.costPerDPs[1];
                baseCost=NanoThief_10.baseCosts[1];
                break;
            case CRUISER:
                costPerDP=NanoThief_10.costPerDPs[2];
                baseCost=NanoThief_10.baseCosts[2];
                break;
            case CAPITAL_SHIP:
                costPerDP=NanoThief_10.costPerDPs[3];
                baseCost=NanoThief_10.baseCosts[3];
                break;
        }
        return (int) ((getDP() * costPerDP)+baseCost);
    }
    public int getSModCost(){
        double costIncrease = (ship.getVariant().getSMods().size()*NanoThief_10.sModCost);
        return (int) (getBaseCost() * costIncrease);
    }
    public int getDModReduction(){
        ArrayList<String> temp = new ArrayList<>(List.of(dmos));
        int dmods = 0;
        for (String a : ship.getVariant().getPermaMods()){
            if (Global.getSettings().getHullModSpec(a).getTags().contains("dmod") && !temp.contains(a)) dmods++;
        }
        double costDecrease = (dmods*NanoThief_10.dModDiscount);
        costDecrease = Math.min(costDecrease,NanoThief_10.dModmin);
        int cost = getBaseCost();
        return (int) (cost * costDecrease);
    }
}
