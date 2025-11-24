package Abyssal_XO.data.scripts.hullmods;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_ShipStats;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;

public class ReclaimCore extends BaseHullMod {
    public static final String IDOfData = "$AbyssalXO_Nano_Thief_SavedStats";

    @Override
    public void applyEffectsAfterShipAddedToCombatEngine(ShipAPI ship, String id) {
        ship.setNoDamagedExplosions(true);
        //ship.getMutableStats().getTimeMult().modifyFlat("Abyssal_XO",1.5f);
        ship.getMutableStats().getHullDamageTakenMult().modifyMult("Abyssal_XO",0);
        ship.getMutableStats().getFighterWingRange().modifyFlat("Abussal_XO",500000);
        if (!ship.getLaunchBaysCopy().isEmpty()) ship.getLaunchBaysCopy().get(0).setCurrRate(0);
        ship.setAlphaMult(0f);
    }

    @Override
    public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
        if (!ship.getCustomData().containsKey(IDOfData)) return;
        Nano_Thief_Stats.modifySingleFighter(fighter,ship);
        fighter.getMutableStats().getMinCrewMod().modifyMult("Abyssal_XO",0);
        //NanoThief_ShipStats stats = (NanoThief_ShipStats) ship.getCustomData().get(IDOfData);
        //stats.getStats().modifySingleFighter(fighter,stats.getShip());
        //todo: I can listin here for when a fighter is launched, allowing for defensive cores to only use reclaim when a fighter is created.
        //todo: this is were I can add the animation for sim fighter creation.
    }
}
