package Abyssal_XO.data.scripts.threat.animation;

import Abyssal_XO.data.scripts.threat.AI.Nano_Thief_MasteryConstructionScript;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_BattleListener;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_10;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.combat.threat.RoilingSwarmEffect;
import org.lwjgl.util.vector.Vector2f;

public class NanoThief_A_MasteryModules extends Nano_Thief_MasteryConstructionScript {
    private boolean canWork2 = false;
    public NanoThief_A_MasteryModules(ShipAPI ship, FleetMemberAPI toConstruct, ShipAPI source, float delay, float fadeInTime, float crAtCreation, double reclaim, Nano_Thief_Stats stats) {
        super(toConstruct, source, delay, fadeInTime, crAtCreation, reclaim, stats);
        canWork2 = true;
        this.ship = ship;
        spawnShip();
    }
    @Override
    protected void spawnShip() {
        if (!canWork2) return;
        float facing = source.getFacing() + 15f * ((float) Math.random() - 0.5f);

        Vector2f loc = new Vector2f(source.getLocation());

        CombatEngineAPI engine = Global.getCombatEngine();
        //CombatFleetManagerAPI fleetManager = engine.getFleetManager(source.getOriginalOwner());
        //boolean wasSuppressed = fleetManager.isSuppressDeploymentMessages();
        //fleetManager.setSuppressDeploymentMessages(true);

        //toConstruct.setOwner(source.getOriginalOwner());
        //ship = engine.getFleetManager(source.getOriginalOwner()).spawnShipOrWing(variantId, loc, facing, 0f, null);
        //engine.getFleetManager(source.getOriginalOwner()).soawn
        //ship = engine.getFleetManager(source.getOriginalOwner()).spawnFleetMember(toConstruct,loc,facing,0f);//spawnShipOrWing(variantId, loc, facing, 0f, null);
        //if (true) return;//temp code
        //Settings.log.info("GOT MEMBER ID AS (c): "+ship.getFleetMember().getId());

        ship.setCurrentCR(crAtCreation);
        ship.resetDefaultAI();
        //ship.setOwner(source.getOriginalOwner());
        //for (ship.getAIFlags().hasFlag(AIF))
        //ship;
        /*if (Global.getCombatEngine().isInCampaign() || Global.getCombatEngine().isInCampaignSim()) {
            FactionAPI faction = Global.getSector().getFaction(Factions.THREAT);
            if (faction != null) {
                String name = faction.pickRandomShipName();
                ship.setName(toConstruct.getShipName()+name);
            }
        }*/
        //ship.setName(toConstruct.getShipName() + "#"+(int)(Math.random() * 10000));
        //ship;
        //fleetManager.setSuppressDeploymentMessages(wasSuppressed);
        collisionClass = ship.getCollisionClass();


        /*RoilingSwarmEffect swarm = RoilingSwarmEffect.getSwarmFor(ship);
        RoilingSwarmEffect sourceSwarm = RoilingSwarmEffect.getSwarmFor(source);
        if (swarm != null) {
            swarm.getParams().withInitialMembers = false;
            swarm.getParams().withRespawn = false;
        }
        if (sourceSwarm != null) {
            origMaxSpeed = sourceSwarm.getParams().maxSpeed;
//			sourceSwarm.params.maxSpeed *= 0.25f;
            sourceSwarm.getParams().outspeedAttachedEntityBy = 0f;
            if (swarm != null) {
                swarm.getParams().withInitialMembers = false;
                swarm.getParams().flashFringeColor	= sourceSwarm.getParams().flashFringeColor;
            }
        }*/
        //NanoThief_BattleListener.reclaimOverride.put(ship,(int)(reclaim* NanoThief_10.maxReclaimPercent));
        //ship.getCustomData().put(NANO_THIEF_CUSTOM_MASTERY_RECLAIM_MEMERY_KEY,(int)reclaim*NanoThief_10.maxReclaimPercent);
        ship.setAlphaMult(0);
        ship.addTag(SHIP_UNDER_CONSTRUCTION);
        source.addTag(SWARM_CONSTRUCTING_SHIP);
        source.setCollisionClass(CollisionClass.NONE);
        source.getMutableStats().getHullDamageTakenMult().modifyMult("ThreatShipConstructionScript", 0f);

        ship.setShipAI(null);
        for (WeaponGroupAPI g : ship.getWeaponGroupsCopy()) {
            g.toggleOff();
        }
        hasCreated = true;
        //Settings.log.info("GOT MEMBER ID AS (d): "+ship.getFleetMember().getId());
    }
}
