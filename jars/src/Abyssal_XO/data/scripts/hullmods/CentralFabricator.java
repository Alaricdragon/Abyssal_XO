package Abyssal_XO.data.scripts.hullmods;

import Abyssal_XO.data.scripts.threat.skills.NanoThief_8;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;

public class CentralFabricator extends BaseHullMod {
    @Override
    public String getSModDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        return (int)((NanoThief_8.sModBonus-1)*100)+"%";
    }

    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        super.applyEffectsBeforeShipCreation(hullSize, stats, id);
        //stats.getDynamic().getMod(Stats.DO_NOT_FIRE_THROUGH).modifyFlat(id, -1);
    }

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        super.applyEffectsAfterShipCreation(ship, id);
    }

    @Override
    public void applyEffectsAfterShipAddedToCombatEngine(ShipAPI ship, String id) {
        super.applyEffectsAfterShipAddedToCombatEngine(ship, id);
    }
}
