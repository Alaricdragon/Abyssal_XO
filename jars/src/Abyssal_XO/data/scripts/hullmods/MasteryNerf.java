package Abyssal_XO.data.scripts.hullmods;

import Abyssal_XO.data.scripts.threat.skills.NanoThief_10;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;

public class MasteryNerf extends BaseHullMod {
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        super.applyEffectsBeforeShipCreation(hullSize, stats, id);
        stats.getPeakCRDuration().modifyMult("Abyssal_XO", (float) NanoThief_10.peakCRDuration);
    }

    @Override
    public String getDescriptionParam(int index, ShipAPI.HullSize hullSize) {
        return (int)(100 - (100*NanoThief_10.peakCRDuration))+"%";
    }
}
