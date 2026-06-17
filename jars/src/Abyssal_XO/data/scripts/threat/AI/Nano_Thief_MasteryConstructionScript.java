package Abyssal_XO.data.scripts.threat.AI;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Factions;
import com.fs.starfarer.api.impl.combat.threat.RoilingSwarmEffect;
import com.fs.starfarer.api.impl.combat.threat.ThreatShipConstructionScript;
import org.lwjgl.util.vector.Vector2f;

public class Nano_Thief_MasteryConstructionScript extends ThreatShipConstructionScript {
    private FleetMemberAPI toConstruct;
    private boolean canSpawn = false;
    private float crAtCreation;
    public Nano_Thief_MasteryConstructionScript(FleetMemberAPI toConstruct, ShipAPI source, float delay, float fadeInTime,float crAtCreation) {
        super(toConstruct.getVariant().getHullVariantId(), source, delay, fadeInTime);
        this.toConstruct = toConstruct;
        this.crAtCreation = crAtCreation;
        canSpawn = true;
        spawnShip();
    }
    @Override
    protected void spawnShip() {
        if (!canSpawn) return;
        float facing = source.getFacing() + 15f * ((float) Math.random() - 0.5f);

        Vector2f loc = new Vector2f(source.getLocation());

        CombatEngineAPI engine = Global.getCombatEngine();
        CombatFleetManagerAPI fleetManager = engine.getFleetManager(source.getOriginalOwner());
        boolean wasSuppressed = fleetManager.isSuppressDeploymentMessages();
        fleetManager.setSuppressDeploymentMessages(true);

        toConstruct.setOwner(source.getOriginalOwner());
        ship = engine.getFleetManager(source.getOriginalOwner()).spawnFleetMember(toConstruct,loc,facing,0f);//spawnShipOrWing(variantId, loc, facing, 0f, null);
        ship.setCurrentCR(crAtCreation);
        if (Global.getCombatEngine().isInCampaign() || Global.getCombatEngine().isInCampaignSim()) {
            FactionAPI faction = Global.getSector().getFaction(Factions.THREAT);
            if (faction != null) {
                String name = faction.pickRandomShipName();
                ship.setName(toConstruct.getShipName()+name);
            }
        }
        fleetManager.setSuppressDeploymentMessages(wasSuppressed);
        collisionClass = ship.getCollisionClass();


        RoilingSwarmEffect swarm = RoilingSwarmEffect.getSwarmFor(ship);
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
        }

        ship.addTag(SHIP_UNDER_CONSTRUCTION);
        source.addTag(SWARM_CONSTRUCTING_SHIP);
        source.setCollisionClass(CollisionClass.NONE);
        source.getMutableStats().getHullDamageTakenMult().modifyMult("ThreatShipConstructionScript", 0f);

        ship.setShipAI(null);
        for (WeaponGroupAPI g : ship.getWeaponGroupsCopy()) {
            g.toggleOff();
        }
    }
}
