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
        double costIncrease = (ship.getVariant().getSMods().size()*NanoThief_10.sModCost);
        int dmods = 0;
        for (String a : ship.getVariant().getPermaMods()){
            if (Global.getSettings().getHullModSpec(a).getTags().contains("dmod") && !temp.contains(a)) dmods++;
        }
        double costDecrease = (dmods*NanoThief_10.dModDiscount);
        costDecrease = Math.min(costDecrease,NanoThief_10.dModmin);
        cost = getDP() * NanoThief_10.costPerDP;
        double smodBonus = cost * costIncrease;
        double dmodNegitive = cost * costDecrease;
        cost += smodBonus;
        cost -= dmodNegitive;
        //Settings.log.info("a: "+ship.getDeployCost()+", b: "+ship.getDeploymentPointsCost()+", c: "+ship.getBaseDeployCost()+", d: "+ship.getDeploymentCostSupplies());
        reloadTime = Math.max(NanoThief_10.rechargeTimePerDP,getDP() * NanoThief_10.rechargeTimePerDP);

        buildTime = Math.max(NanoThief_10.buildTimePerDP,getDP() * NanoThief_10.buildTimePerDP);
        this.name = name;
    }
    private float getDP(){
        return ship.getHullSpec().getHullId().equals("fabricator_unit") ? NanoThief_10.fabricatorDPOverride : ship.getDeploymentPointsCost();//ship.getHullSpec().getSuppliesToRecover();
    }
    public int getBaseCost(){
        return (int) (getDP() * NanoThief_10.costPerDP);
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
