package Abyssal_XO.data.scripts.hullmods;

import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.campaign.ids.Tags;

public class ReclaimCoreDefensive extends ReclaimCore{
    @Override
    public void applyEffectsAfterShipAddedToCombatEngine(ShipAPI ship, String id) {
        super.applyEffectsAfterShipAddedToCombatEngine(ship, id);
        ship.getMutableStats().getFighterWingRange().modifyMult("Abussal_XO",0);
    }
    @Override
    public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
        super.applyEffectsToFighterSpawnedByShip(fighter, ship, id);
        fighter.addTag(Tags.WING_STAY_IN_FRONT_OF_SHIP);
    }
}
