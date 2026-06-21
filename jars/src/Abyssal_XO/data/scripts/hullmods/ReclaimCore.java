package Abyssal_XO.data.scripts.hullmods;

import Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_SawrmSpawner;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.animation.NanoThief_A_FighterSpawn;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_ShipStats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import org.apache.log4j.Logger;

import static Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_SawrmSpawner.IDOfData1;
import static Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_SawrmSpawner.IDOfData2;

public class ReclaimCore extends BaseHullMod {
    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        super.applyEffectsBeforeShipCreation(hullSize, stats, id);
        stats.getFighterRefitTimeMult().modifyFlat("Abyssal_XO",10000);
        stats.getDynamic().getStat(Stats.REPLACEMENT_RATE_INCREASE_MULT).modifyMult(id, 0);
        stats.getDynamic().getMod(Stats.FIGHTER_REARM_TIME_EXTRA_FLAT_MOD).modifyFlat(id, 1000);
    }

    @Override
    public void applyEffectsAfterShipAddedToCombatEngine(ShipAPI ship, String id) {
        ship.setNoDamagedExplosions(true);
        //ship.getMutableStats().getTimeMult().modifyFlat("Abyssal_XO",1.5f);
        ship.getMutableStats().getHullDamageTakenMult().modifyMult("Abyssal_XO",0);
        if (!ship.getLaunchBaysCopy().isEmpty()) ship.getLaunchBaysCopy().get(0).setCurrRate(0);
        ship.setAlphaMult(0f);
    }

    @Override
    public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
        if (ship.getCustomData().containsKey(IDOfData1) && (boolean)ship.getCustomData().get(IDOfData1)) {
            Global.getCombatEngine().removeEntity(fighter);
            ship.getLaunchBaysCopy().get(0).getWing().removeMember(fighter);
            //Logger log = Global.getLogger(Nano_Thief_Stats.class);
            //log.info("removed a fighter that would have been spawned in");
            return;
        }

        Nano_Thief_AI_SawrmSpawner spawner = (Nano_Thief_AI_SawrmSpawner)ship.getCustomData().get(IDOfData2);
        spawner.addSpawnedFighter(fighter);
        //a.addTag("independent_of_carrier");
        Nano_Thief_Stats.modifySingleFighter(fighter,ship);
        fighter.getMutableStats().getMinCrewMod().modifyMult("Abyssal_XO",0);

        //NanoThief_ShipStats stats = (NanoThief_ShipStats) ship.getCustomData().get(IDOfData);
        //stats.getStats().modifySingleFighter(fighter,stats.getShip());
        //todo: I can listin here for when a fighter is launched, allowing for defensive cores to only use reclaim when a fighter is created.
        //      note: fighters spawned done have any allowed animations before they are fully deployed.
        //            this sucks =(
        //Global.getCombatEngine().addPlugin(new NanoThief_A_FighterSpawn(fighter));
        //fighter.setAlphaMult(0);
    }
}
