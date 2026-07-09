package Abyssal_XO.data.scripts.combatability;

import com.fs.starfarer.api.combat.ShipAPI;
import second_in_command.SCData;
import second_in_command.hullmods.SCControllerHullmod;

public class CombatabilityUtility {
    /// this is just to add seperation to a class that does not exsist outside of context.
    public static void addSiC_MidFight(ShipAPI ship, SCData data){
        SCControllerHullmod.Companion.addHullmodAfterShipCreation(ship,data);
    }
}
