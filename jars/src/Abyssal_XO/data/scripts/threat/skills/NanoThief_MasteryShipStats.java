package Abyssal_XO.data.scripts.threat.skills;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipVariantAPI;

import java.util.ArrayList;
import java.util.List;

public class NanoThief_MasteryShipStats {
    public ShipVariantAPI ship;
    public double weight;
    public double cost;
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
    public NanoThief_MasteryShipStats(ShipVariantAPI ship,double weight){
        ArrayList<String> temp = new ArrayList<>(List.of(dmos));
        this.ship = ship;
        this.weight = weight;
        double costIncrease = 1 + (ship.getSMods().size()*NanoThief_10.sModCost);
        int dmods = 0;
        for (String a : ship.getPermaMods()){
            if (Global.getSettings().getHullModSpec(a).getTags().contains("dmod") && !temp.contains(a)) dmods++;
        }
        double costDecrease = 1 - (dmods*NanoThief_10.dModDiscount);
        costDecrease = Math.min(costDecrease,NanoThief_10.dModmin);
        cost = ship.getHullSpec().getSuppliesToRecover() * NanoThief_10.costPerDP;
        cost *= costDecrease*costIncrease;
    }
}
