package Abyssal_XO.data.scripts.hullmods;

import com.fs.starfarer.api.combat.ShipAPI;

public class ReclaimCoreOffincive extends ReclaimCore{
    @Override
    public void applyEffectsAfterShipAddedToCombatEngine(ShipAPI ship, String id) {
        super.applyEffectsAfterShipAddedToCombatEngine(ship, id);
        ship.getMutableStats().getFighterWingRange().modifyFlat("Abyssal_XO",500000);
    }
    @Override
    public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
        super.applyEffectsToFighterSpawnedByShip(fighter, ship, id);
        fighter.addTag("independent_of_carrier");
    }

}
