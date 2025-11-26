package Abyssal_XO.data.scripts.hullmods;

import Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_SawrmSpawner;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_ShipStats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import org.apache.log4j.Logger;

import static Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_SawrmSpawner.IDOfData1;
import static Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_SawrmSpawner.IDOfData2;

public class ReclaimCore extends BaseHullMod {

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
        if (ship.getCustomData().containsKey(IDOfData1) && (boolean)ship.getCustomData().get(IDOfData1)) {
            Global.getCombatEngine().removeEntity(fighter);
            ship.getLaunchBaysCopy().get(0).getWing().removeMember(fighter);
            Logger log = Global.getLogger(Nano_Thief_Stats.class);
            log.info("removed a fighter that would have been spawned in");
            return;
        }

        Nano_Thief_AI_SawrmSpawner spawner = (Nano_Thief_AI_SawrmSpawner)ship.getCustomData().get(IDOfData2);
        if (spawner.isOffensive()){
            fighter.addTag("independent_of_carrier");
        }else{
            fighter.addTag(Tags.WING_STAY_IN_FRONT_OF_SHIP);
        }
        spawner.addSpawnedFighter(fighter);
        //a.addTag("independent_of_carrier");
        Nano_Thief_Stats.modifySingleFighter(fighter,ship);
        fighter.getMutableStats().getMinCrewMod().modifyMult("Abyssal_XO",0);

        //NanoThief_ShipStats stats = (NanoThief_ShipStats) ship.getCustomData().get(IDOfData);
        //stats.getStats().modifySingleFighter(fighter,stats.getShip());
        //todo: I can listin here for when a fighter is launched, allowing for defensive cores to only use reclaim when a fighter is created.
        //todo: this is were I can add the animation for sim fighter creation.
    }
}
