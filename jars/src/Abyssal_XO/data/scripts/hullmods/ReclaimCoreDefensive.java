package Abyssal_XO.data.scripts.hullmods;

import Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_SawrmSpawner;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.campaign.ids.Tags;

import static Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_SawrmSpawner.*;

public class ReclaimCoreDefensive extends ReclaimCore{
    @Override
    public void applyEffectsAfterShipAddedToCombatEngine(ShipAPI ship, String id) {
        super.applyEffectsAfterShipAddedToCombatEngine(ship, id);
        ship.getMutableStats().getFighterWingRange().modifyMult("Abyssal_XO",0);
    }
    @Override
    public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
        super.applyEffectsToFighterSpawnedByShip(fighter, ship, id);
        if (fighter.getWing() != null && fighter.getWing().getSpec() != null) {
            if (fighter.getWing().getSpec().isRegularFighter() ||
                    fighter.getWing().getSpec().isAssault() ||
                    fighter.getWing().getSpec().isBomber() ||
                    fighter.getWing().getSpec().isInterceptor()) {
                fighter.addTag(Tags.WING_STAY_IN_FRONT_OF_SHIP);
            }
        }
        if (ship.getCustomData().containsKey(IDOfData3))fighter.getMutableStats().getMaxSpeed().modifyFlat("abyssalXO_NANOTHIEF_defSpeed", (Float) ship.getCustomData().get(IDOfData3));
        if (ship.getCustomData().containsKey(IDOfData4))fighter.getMutableStats().getAcceleration().modifyFlat("abyssalXO_NANOTHIEF_defAcl", (Float) ship.getCustomData().get(IDOfData4));
        if (ship.getCustomData().containsKey(IDOfData5))fighter.getMutableStats().getDeceleration().modifyFlat("abyssalXO_NANOTHIEF_defDcl", (Float) ship.getCustomData().get(IDOfData5));
    }
}
