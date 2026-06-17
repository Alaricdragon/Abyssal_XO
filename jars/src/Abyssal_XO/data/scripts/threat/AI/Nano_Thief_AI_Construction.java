package Abyssal_XO.data.scripts.threat.AI;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_MasteryShipStats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.combat.threat.*;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class Nano_Thief_AI_Construction implements ShipAIPlugin{
    public static float COHESION_RANGE_MAX = 300f;

    public static class FlockingData {
        public Vector2f loc;
        public Vector2f vel;
        public float minA;
        public float maxA;
        public float minR;
        public float maxR;
        public float repelAtAngleDist;
        public float minC;
        public float maxC;
        public float attractWeight;
        public float repelWeight;
        public float cohesionWeight;
        public float facing;
    }

    protected ShipAPI ship;

    protected IntervalUtil updateInterval = new IntervalUtil(0.5f, 1.5f);
    protected IntervalUtil headingInterval = new IntervalUtil(0.5f, 1.5f);

    protected ShipAPI fabricator = null;

    protected List<ThreatSwarmAI.FlockingData> flockingData = new ArrayList<>();
    protected float desiredHeading = 0f;
    protected float headingChangeRate = 0f;
    protected float elapsedSincePrevHeadingUpdate = 0f;

    protected float elapsed = 0f;

    protected boolean startedConstruction = false;
    //private ConstructionSwarmSystemScript.SwarmConstructionData constructionData;
    private NanoThief_MasteryShipStats constructionDatas;
    private ThreatShipConstructionScript constructionScript;

    private float avoidDistance;
    private int dp;
    private Nano_Thief_Stats stats;
    private float cr;
    public Nano_Thief_AI_Construction(ShipAPI ship, NanoThief_MasteryShipStats constructionDatas,float cr, Nano_Thief_Stats stats){
        this.ship = ship;
        this.constructionDatas = constructionDatas;
        avoidDistance = (constructionDatas.ship.getHullSpec().getCollisionRadius() * 20) + 10;//twice raid
        this.stats = stats;
        this.dp = constructionDatas.ship.getFleetPointCost();
        this.cr = cr;
        ship.setAlphaMult(0);
        Settings.log.info("attempting to add dp of:"+dp);
        ship.getMutableStats().getDynamic().getStat(Stats.DEPLOYMENT_POINTS_MOD).setBaseValue(dp);
        Settings.log.info("got final dp on swarm as: "+ship.getMutableStats().getDynamic().getStat(Stats.DEPLOYMENT_POINTS_MOD).base);
        /*todo:
        *  1: add max reclaim stat (here or elsewere)
        *  2: adjust swarm size (here or elsewere)
        *   note: ConstructionSwarmSystemScript.launchSwarm has an example of how this goes
        * */
    }
    public static boolean isConstructionSwarm(ShipAPI ship) {
        return ship != null && ship.getVariant().getHullVariantId().equals(SwarmLauncherEffect.CONSTRUCTION_SWARM_VARIANT);
    }

    @Override
    public void advance(float amount) {
        /*todo: I think the simple way to do this is to have 3 steps:
           1: move to 'empty location' (how to find: unknown)
           2: start to build ship (also make swarm mush larger for graphics reasons)
           3: once build, despawn construction swarm.
           -
           4: make it so construction swarms cost dp? maybe? (might not be a issue. I will need to save what construction swarms are active though, to avoid issues with over production)

         notes:
            1: ThreatShipConstructionScript is how a ship is constructed, as per threats calculation.
                -this should be very simple to modify for my actions. the following needs to be changed:
                1: I need to change it to take a variant object instead of id (simple)
                2: I need to change it so the name is based on the name of the constructed ship. (can just do this afterword?
                3: add in a memory tag for the max amount of reclaim from this ship.
                -:'delay' is the time it takes for the swarm to start construction.
                -:'fade in time' is the time it takes to build.
         */
        elapsed+=amount;
        DesideConstruction();
        updateInterval.advance(amount);
        if (updateInterval.intervalElapsed()) {
            updateFlockingData();
        }
        headingInterval.advance(amount * 5f);
        if (headingInterval.intervalElapsed()) {
            computeDesiredHeading();
            elapsedSincePrevHeadingUpdate = 0f;
        }

        giveMovementCommands();

        //I think this code is just to 'link' this sawrm to its construction location.
        if (constructionScript != null && constructionScript.getShip() != null) {
            ship.giveCommand(ShipCommand.DECELERATE, null, 0);
            return;
        }
    }
    private void DesideConstruction(){
        /*
            todo:
             1: add in a second 'can build' calculation (based on size of to build ship * 2 not inside of any nearby ship size * 2 (so room for manuvering is ready)
        */
        //first part interesting. I think this just selects a ship to build...? ?
        /*if (constructionData == null) {
            RoilingSwarmEffect swarm = RoilingSwarmEffect.getSwarmFor(ship);
            if (swarm != null) {
                constructionData = (ConstructionSwarmSystemScript.SwarmConstructionData) swarm.custom1;
            }
        }*/
        //second part: strange. I dont understand why its asking about construction data. can it be null after being set so fast?
        //if (constructionData != null) {
        if (isConstructing()) return;
        boolean canSetup = true;
        for (ShipAPI a : Global.getCombatEngine().getShips()) if (a != null && a.getFleetMember() != null && a.getFleetMember().getHullSpec() != null && Misc.getDistance(a.getLocation(),ship.getLocation()) < avoidDistance + (a.getFleetMember().getHullSpec().getCollisionRadius()*2)){
            canSetup = false;
            break;
        }
        if (canSetup){
            startConstruction();
        }
        int maxTimeUntillBuild = 10;
        if (elapsed > maxTimeUntillBuild && !startedConstruction) {
            startConstruction();
        }
        //}
    }
    private void adjustSwarmSizeForConstruction(){

    }
    protected void giveMovementCommands() {
        //so... this code. is interesting. basicly, this should only actavate when I am building the ship I wanna build right now.
        if (constructionScript != null && constructionScript.getShip() != null) {
        //if (isConstructing()){
            ship.giveCommand(ShipCommand.DECELERATE, null, 0);
            return;
        }

        String source = "swarm_wingman_catch_up_speed_bonus";
        MutableShipStatsAPI stats = ship.getMutableStats();
        if (ship.isWingLeader() || ship.getWingLeader() == null) {
            stats.getMaxSpeed().unmodifyMult(source);
            stats.getAcceleration().unmodifyMult(source);
            stats.getDeceleration().unmodifyMult(source);
        } else {
            ShipAPI leader = ship.getWingLeader();
            float dist = Misc.getDistance(ship.getLocation(), leader.getLocation());
            float mult = (dist - COHESION_RANGE_MAX * 0.5f -
                    ship.getCollisionRadius() * 0.5f - leader.getCollisionRadius() * 0.5f) / COHESION_RANGE_MAX;
            if (mult < 0f) mult = 0f;
            if (mult > 1f) mult = 1f;
            stats.getMaxSpeed().modifyMult(source, 1f + .25f * mult);
            stats.getAcceleration().modifyMult(source, 1f + 0.5f * mult);
            stats.getDeceleration().modifyMult(source, 1f + 0.5f * mult);
        }

        float useHeading = desiredHeading;
        //useHeading += headingChangeRate * elapsedSincePrevHeadingUpdate;

        CombatEngineAPI engine = Global.getCombatEngine();
        engine.headInDirectionWithoutTurning(ship, useHeading, 10000);
        Misc.turnTowardsFacingV2(ship, useHeading, 0f);
    }
    private void updateFlockingData(){
        CombatEngineAPI engine = Global.getCombatEngine();
        //how does this work? I think, for each ship, it trys to move away from every other ship? I think?
        //(conclusion from high 'repel' weight, and low 'attraction' weight.)
        flockingData.clear();
        float radius = ship.getCollisionRadius() * 0.5f;
        for (ShipAPI curr : engine.getShips()) {
            float currRadius = curr.getCollisionRadius() * 2f;
            ThreatSwarmAI.FlockingData data = new ThreatSwarmAI.FlockingData();
            data.facing = curr.getFacing();
            data.loc = curr.getLocation();
            data.vel = curr.getVelocity();
            data.attractWeight = 0f;
            data.repelWeight = getShipWeight(curr) * 1f;
            data.minA = 0f;
            data.maxA = 0f;
            data.minR = radius + currRadius;
            data.maxR = radius + currRadius + Math.min(100f, currRadius * 1f);
            data.repelAtAngleDist = (data.maxR - data.minR) * 0.5f;
            flockingData.add(data);
        }
    }
    protected void computeDesiredHeading() {

        Vector2f loc = ship.getLocation();
        Vector2f vel = ship.getVelocity();
        float facing = ship.getFacing();

        Vector2f total = new Vector2f();

        for (ThreatSwarmAI.FlockingData curr : flockingData) {
            float dist = Misc.getDistance(curr.loc, loc);
            if (curr.maxR > 0 && dist < curr.maxR) {
                float repelWeight = curr.repelWeight;
                if (dist > curr.minR && curr.maxR > curr.minR) {
                    repelWeight = (dist - curr.minR)  / (curr.maxR - curr.minR);
                    if (repelWeight > 1f) repelWeight = 1f;
                    repelWeight = 1f - repelWeight;
                    repelWeight *= curr.repelWeight;
                }

                Vector2f dir = Misc.getUnitVector(curr.loc, loc);

                float distIntoRepel = curr.maxR - dist;
                float repelAdjustmentAngle = 0f;
                if (distIntoRepel < curr.repelAtAngleDist && curr.repelAtAngleDist > 0) {
                    float repelMult = (1f - distIntoRepel / curr.repelAtAngleDist);
                    repelAdjustmentAngle = 90f * repelMult;
                    repelWeight *= (1f - repelMult);

                    float repelAngle = Misc.getAngleInDegrees(dir);
                    float turnDir = Misc.getClosestTurnDirection(dir, vel);
                    repelAdjustmentAngle *= turnDir;
                    dir = Misc.getUnitVectorAtDegreeAngle(repelAngle + repelAdjustmentAngle);
                }

                dir.scale(repelWeight);
                Vector2f.add(total, dir, total);
            }

            if (curr.maxA > 0 && dist < curr.maxA) {
                float attractWeight = curr.attractWeight;
                if (dist > curr.minA && curr.maxA > curr.minA) {
                    attractWeight = (dist - curr.minA)  / (curr.maxA - curr.minA);
                    if (attractWeight > 1f) attractWeight = 1f;
                    attractWeight = 1f - attractWeight;
                    attractWeight *= curr.attractWeight;
                }

                Vector2f dir = Misc.getUnitVector(loc, curr.loc);
                dir.scale(attractWeight);
                Vector2f.add(total, dir, total);
            }

            if (curr.maxC > 0 && dist < curr.maxC) {
                float cohesionWeight = curr.cohesionWeight;
                if (dist > curr.minC && curr.maxC > curr.minC) {
                    cohesionWeight = (dist - curr.minC)  / (curr.maxC - curr.minC);
                    if (cohesionWeight > 1f) cohesionWeight = 1f;
                    cohesionWeight = 1f - cohesionWeight;
                    cohesionWeight *= curr.cohesionWeight;
                }

                Vector2f dir = new Vector2f(curr.vel);
                Misc.normalise(dir);
                dir.scale(cohesionWeight);
                Vector2f.add(total, dir, total);
            }
        }

        if (total.length() <= 0) {
            desiredHeading = ship.getFacing();
            headingChangeRate = ship.getAngularVelocity() * 0.5f;
        } else {
//			Vector2f currDir = new Vector2f(vel);
//			Misc.normalise(currDir);
//			currDir.scale(total.length() * 0.25f);
//			Vector2f.add(total, currDir, total);

            float prev = desiredHeading;
            desiredHeading = Misc.getAngleInDegrees(total);
            if (elapsedSincePrevHeadingUpdate > 0) {
                headingChangeRate = Misc.getAngleDiff(prev, desiredHeading) / elapsedSincePrevHeadingUpdate;
            } else {
                headingChangeRate = ship.getAngularVelocity() * 0.5f;
            }
        }
    }
    @Override
    public void setDoNotFireDelay(float amount) {

    }

    @Override
    public void forceCircumstanceEvaluation() {

    }

    public static float getShipWeight(ShipAPI ship) {
        return getShipWeight(ship, true);
    }
    public static float getShipWeight(ShipAPI ship, boolean adjustForNonCombat) {
        boolean nonCombat = ship.isNonCombat(false);
        float weight = 0;
        switch (ship.getHullSize()) {
            case CAPITAL_SHIP: weight += 8; break;
            case CRUISER: weight += 4; break;
            case DESTROYER: weight += 2; break;
            case FRIGATE: weight += 1; break;
            case FIGHTER: weight += 1; break;
        }
        if (nonCombat && adjustForNonCombat) weight *= 0.25f;
        if (ship.isDrone()) weight *= 0.1f;
        return weight;
    }

    private boolean isConstructing(){
        return startedConstruction;
    }

    private void startConstruction(){
        startedConstruction = true;
        RoilingSwarmEffect swarm = RoilingSwarmEffect.getSwarmFor(ship);
        if (swarm != null) {
            FleetMemberAPI memberCopy = Global.getSettings().createFleetMember(constructionDatas.ship.getType(), constructionDatas.ship.getVariant().clone());
            memberCopy.setOwner(ship.getOwner());
            memberCopy.setShipName(constructionDatas.name);
            //FleetMemberAPI memberCopy = constructionDatas.ship;

            //memberCopy.getStats().getMaxCombatReadiness().setBaseValue(9999);
            //memberCopy;
            //memberCopy.getStats().getMaxCombatReadiness().modifyFlat();
            //memberCopy;
            constructionScript = new Nano_Thief_MasteryConstructionScript(
                    memberCopy, ship, 1f, (float) constructionDatas.buildTime,cr);
            Global.getCombatEngine().addPlugin(constructionScript);
            adjustSwarmSizeForConstruction();
        }
        //stats
    }


    @Override
    public boolean needsRefit() {
        return false;
    }

    @Override
    public ShipwideAIFlags getAIFlags() {
        return new ShipwideAIFlags();
    }

    @Override
    public void cancelCurrentManeuver() {

    }

    @Override
    public ShipAIConfig getConfig() {
        return new ShipAIConfig();
    }
}
