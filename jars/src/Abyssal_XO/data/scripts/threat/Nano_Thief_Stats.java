package Abyssal_XO.data.scripts.threat;

import Abyssal_XO.data.scripts.listiners.NanoThief_RecreationScript;
import Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_Reclaim;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.impl.combat.threat.FragmentSwarmHullmod;
import com.fs.starfarer.api.impl.combat.threat.RoilingSwarmEffect;
import com.fs.starfarer.api.impl.combat.threat.SwarmLauncherEffect;
import com.fs.starfarer.api.util.Misc;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.lwjgl.util.vector.Vector2f;
import second_in_command.specs.SCBaseSkillPlugin;
import second_in_command.specs.SCOfficer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class Nano_Thief_Stats {
    private float swarmCost = 100;
    private float swarmQuality = 1;
    private static Logger log = Global.getLogger(Nano_Thief_Stats.class);
    @Getter
    private CampaignFleetAPI fleet;
    private SCOfficer officer;
    @Getter
    private boolean closest;

    ArrayList<Nano_Thief_SKill_Base> skills = new ArrayList<>();
    public Nano_Thief_Stats(CampaignFleetAPI fleet, SCOfficer officer){
        this.fleet = fleet;
        this.officer = officer;
        officer.getSkillPlugins();
        float SC_add = 0;
        float SC_muti = 1;
        float SQ_add = 0;
        float SQ_muti = 1;
        for (SCBaseSkillPlugin a : officer.getSkillPlugins()){
            Nano_Thief_SKill_Base b = (Nano_Thief_SKill_Base) a;
            skills.add(b);
            SC_muti *= b.swarmCostMulti;
            SC_add += b.swarmCostAdd;

            SQ_muti *= b.qualityMulti;
            SQ_add += b.qualityAdd;
        }
        swarmCost += SC_add;
        swarmCost *= SC_muti;

        swarmQuality += SQ_add;
        swarmQuality *= SQ_muti;
    }
    public void spawnReclaim() {

    }
    public boolean canAcceptReclaim(){
        return true;
    }
    public boolean isValidReclaimTarget(ShipAPI ship){
        return true;
    }
    public float getReclaimTargetDistanceMulti(ShipAPI ship){
        float out = 1;
        ShipVariantAPI variant = ship.getVariant();
        if (variant.hasHullMod("dweller_hullmod") || variant.hasHullMod("shrouded_mantle") || variant.hasHullMod("shrouded_thunderhead") || variant.hasHullMod("shrouded_lens")) out *= 4;
        if (variant.hasHullMod("fragment_swarm") || /*variant.hasHullMod("fragment_coordinator") || variant.hasHullMod("secondary_fabricator") || */variant.hasHullMod("threat_hullmod")) out *= 0.5f;
        return out;
    }
    public float getReclaimTargetPriority(ShipAPI ship){
        float out = 1;
        ShipVariantAPI variant = ship.getVariant();
        if (closest) return out;
        switch (ship.getHullSize()){
            case CAPITAL_SHIP:
                break;
            case CRUISER:
                out *= 0.75f;
                break;
            case DESTROYER:
                out *= 0.5f;
                break;
            case FRIGATE:
            case DEFAULT:
                out *= 0.25f;
        }
        return out;
    }
    public ShipAPI getTargetForReclaim(ShipAPI reclaim, CombatEngineAPI engine){
        ShipAPI output = null;
        Vector2f pointA = reclaim.getLocation();
        float reclaimPriority = 0;
        float distance = Float.MAX_VALUE;
        for (ShipAPI curr : engine.getShips()) {
            if (curr == null) continue;
            if (curr.isHulk()) continue;
            if (curr.equals(reclaim)) continue;
            if (curr.getFleetMember() == null) continue;
            log.info("  has fleetmember");
            if (curr.getFleetMember().getFleetData() == null) continue;
            log.info("  has fleetdata");
            if (curr.getFleetMember().getFleetData().getFleet() == null) continue;
            log.info("  has fleet");
            if (!curr.getFleetMember().getFleetData().getFleet().equals(fleet)) continue;
            log.info("  has right commander...");
            if (!isValidReclaimTarget(curr)) continue;
            float priority = getReclaimTargetPriority(curr);
            if (priority < reclaimPriority) continue;
            log.info("  got valid reclaim target. comparing position....");
            Vector2f pointB = curr.getLocation();
            float c = Misc.getDistance(pointA,pointB);
            /*float a = pointA.x - pointB.x;
            float b = pointA.y - pointB.y;
            float c = (float) Math.sqrt( a*a + b*b);*/
            c *= getReclaimTargetDistanceMulti(curr);
            if (c < distance){
                log.info("  getting a new valid reclaim target of distance "+c);
                distance = c;
                output = curr;
                reclaimPriority = priority;
            }
        }
        if (output != null) log.info("reclaim target has been chosen as "+output.toString());
        return output;
    }

    private static final String NanoThiefStorgeKey = "$NanoThief_StoredReclaim_Base";
    private HashMap<ShipAPI,Integer> reclaimGathered = new HashMap<>();
    public void applyEffectsWhenAbsorbed(ShipAPI target,ShipAPI reclaim,int reclaimValue){
        log.info("attempting to create reclaim");
        int currentReclaim = reclaimValue;
        float cost = swarmCost;
        currentReclaim += (int)target.getCustomData().getOrDefault(NanoThiefStorgeKey,0);

        currentReclaim += reclaimGathered.getOrDefault(target,0);

        while(currentReclaim >= cost){
            log.info("adding a combat swarm....");
            currentReclaim-=cost;
            createCombatSwarm(target,getModifiedQuality(target));
        }
        log.info("got remaining reclaim as: "+currentReclaim);
        reclaimGathered.put(target,currentReclaim);
        log.info("chacking memory as: "+reclaimGathered.get(target));
        //
        /*if (Global.getCombatEngine().getPlayerShip().equals(target)){
            playerReclaimDisplay thing = null;
            if (Global.getCombatEngine().getListenerManager().hasListenerOfClass(playerReclaimDisplay.class)){
                thing = Global.getCombatEngine().getListenerManager().getListeners(playerReclaimDisplay.class).get(0);
            }else{
                thing = new playerReclaimDisplay(target);
                Global.getCombatEngine().getListenerManager().addListener(thing);
            }
            thing.setValue(currentReclaim);
        }*/

        for (Nano_Thief_SKill_Base a : skills){
            a.ApplyChangeOnReclaim(target,reclaim,reclaimValue);
        }
    }
    public int getModifiedQuality(ShipAPI target){
        float quality = this.swarmQuality;
        for (Nano_Thief_SKill_Base a : skills){
            quality = a.qualityChange(quality,target);
        }
        return (int)quality;
    }
    public ShipAPI createReclaim(ShipAPI primary,int forceID){
        String wingId = SwarmLauncherEffect.RECLAMATION_SWARM_WING;//"attack_swarm_wing";SwarmLauncherEffect.RECLAMATION_SWARM_WING;//"attack_swarm_wing"

        CombatEngineAPI engine = Global.getCombatEngine();
        CombatFleetManagerAPI manager = engine.getFleetManager(forceID);
        manager.setSuppressDeploymentMessages(true);

        Vector2f loc = primary.getLocation();
        float facing = (float) Math.random() * 360f;

        ShipAPI fighter = manager.spawnShipOrWing(wingId, loc, facing, 0f, null);
        fighter.getWing().setSourceShip(primary);
        //fighter.removeTag(Tags.THREAT_SWARM_AI);
        int amount = 100;
        log.info("calculating total increase in reclaim for a "+primary.getHullSpec().getHullSize()+" ship");
        log.info("  name:"+primary.getName());
        switch (primary.getHullSpec().getHullSize()){
            case CAPITAL_SHIP -> amount *= 8;
            case CRUISER -> amount *= 4;
            case DESTROYER -> amount *= 2;
        }
        log.info("calculated reclaim as: "+amount);
        fighter.setShipAI(new Nano_Thief_AI_Reclaim(fighter,this,amount));//todo: learn if this is even doing anything I guess????
        manager.setSuppressDeploymentMessages(false);

        fighter.getMutableStats().getMaxSpeed().modifyMult("construction_swarm", NanoThief_RecreationScript.RECLAMATION_SWARM_SPEED_MULT);

        Vector2f takeoffVel = Misc.getUnitVectorAtDegreeAngle(facing);
        takeoffVel.scale(fighter.getMaxSpeed() * 1f);

        fighter.setDoNotRender(true);
        fighter.setExplosionScale(0f);
        fighter.setHulkChanceOverride(0f);
        fighter.setImpactVolumeMult(SwarmLauncherEffect.IMPACT_VOLUME_MULT);
        fighter.getArmorGrid().clearComponentMap(); // no damage to weapons/engines
        Vector2f.add(fighter.getVelocity(), takeoffVel, fighter.getVelocity());

        RoilingSwarmEffect swarm = FragmentSwarmHullmod.createSwarmFor(fighter);
        RoilingSwarmEffect.getFlockingMap().remove(swarm.getParams().flockingClass, swarm);
        swarm.getParams().flockingClass = FragmentSwarmHullmod.RECLAMATION_SWARM_FLOCKING_CLASS;
        RoilingSwarmEffect.getFlockingMap().add(swarm.getParams().flockingClass, swarm);
        swarm.getParams().memberExchangeClass = FragmentSwarmHullmod.RECLAMATION_SWARM_EXCHANGE_CLASS;


        //swarm.params.flashFringeColor = VoltaicDischargeOnFireEffect.EMP_FRINGE_COLOR;
        swarm.getParams().flashFrequency = 5f;
        swarm.getParams().flashProbability = 1f;

        // brownish/rusty
        //swarm.params.flashFringeColor = new Color(255,95,50,50);

        swarm.getParams().flashFringeColor = new Color(255,70,30,50);
        swarm.getParams().flashCoreRadiusMult = 0f;

        swarm.getParams().springStretchMult = 1f;

        //swarm.params.baseSpriteSize *= RECLAMATION_SWARM_FRAGMENT_SIZE_MULT;
        //swarm.params.flashFringeColor = VoltaicDischargeOnFireEffect.EMP_FRINGE_COLOR;

        float collisionMult = NanoThief_RecreationScript.RECLAMATION_SWARM_COLLISION_MULT;
        float hpMult = NanoThief_RecreationScript.RECLAMATION_SWARM_HP_MULT;

        for (BoundsAPI.SegmentAPI s : fighter.getExactBounds().getOrigSegments()) {
            s.getP1().scale(collisionMult);
            s.getP2().scale(collisionMult);
            s.set(s.getP1().x, s.getP1().y, s.getP2().x, s.getP2().y);
        }
        fighter.setCollisionRadius(fighter.getCollisionRadius() * collisionMult);

        fighter.setMaxHitpoints(fighter.getMaxHitpoints() * hpMult);
        fighter.setHitpoints(fighter.getHitpoints() * hpMult);

        swarm.getParams().maxOffset *= NanoThief_RecreationScript.RECLAMATION_SWARM_RADIUS_MULT;

        swarm.getParams().initialMembers = 0;
        swarm.getParams().baseMembersToMaintain = 50;

        for (Nano_Thief_SKill_Base a : skills){
            a.changeReclaimStats(primary,getModifiedQuality(null));
        }
        return fighter;
    }
    public void createCombatSwarm(ShipAPI primary,int quality){
        String wingId = "attack_swarm_wing";//SwarmLauncherEffect.RECLAMATION_SWARM_WING;//"attack_swarm_wing"

        CombatEngineAPI engine = Global.getCombatEngine();
        CombatFleetManagerAPI manager = engine.getFleetManager(primary.getOriginalOwner());
        manager.setSuppressDeploymentMessages(true);

        Vector2f loc = primary.getLocation();
        float facing = (float) Math.random() * 360f;

        ShipAPI fighter = manager.spawnShipOrWing(wingId, loc, facing, 0f, null);
        fighter.getWing().setSourceShip(primary);
        //fighter.removeTag(Tags.THREAT_SWARM_AI);
        /*int amount = 100;
        switch (primary.getHullSize()){
            case CAPITAL_SHIP -> amount *= 8;
            case CRUISER -> amount *= 4;
            case DESTROYER -> amount *= 2;
        }
        fighter.setShipAI(new Nano_Thief_AI_Reclaim(fighter,this,amount));//todo: learn if this is even doing anything I guess????
        */

        manager.setSuppressDeploymentMessages(false);

        fighter.getMutableStats().getMaxSpeed().modifyMult("construction_swarm", NanoThief_RecreationScript.RECLAMATION_SWARM_SPEED_MULT);

        Vector2f takeoffVel = Misc.getUnitVectorAtDegreeAngle(facing);
        takeoffVel.scale(fighter.getMaxSpeed() * 1f);

        fighter.setDoNotRender(true);
        fighter.setExplosionScale(0f);
        fighter.setHulkChanceOverride(0f);
        fighter.setImpactVolumeMult(SwarmLauncherEffect.IMPACT_VOLUME_MULT);
        fighter.getArmorGrid().clearComponentMap(); // no damage to weapons/engines
        Vector2f.add(fighter.getVelocity(), takeoffVel, fighter.getVelocity());

        RoilingSwarmEffect swarm = FragmentSwarmHullmod.createSwarmFor(fighter);

        /*
        RoilingSwarmEffect.getFlockingMap().remove(swarm.getParams().flockingClass, swarm);
        swarm.getParams().flockingClass = FragmentSwarmHullmod.RECLAMATION_SWARM_FLOCKING_CLASS;
        RoilingSwarmEffect.getFlockingMap().add(swarm.getParams().flockingClass, swarm);
        swarm.getParams().memberExchangeClass = FragmentSwarmHullmod.RECLAMATION_SWARM_EXCHANGE_CLASS;


        //swarm.params.flashFringeColor = VoltaicDischargeOnFireEffect.EMP_FRINGE_COLOR;
        swarm.getParams().flashFrequency = 5f;
        swarm.getParams().flashProbability = 1f;

        // brownish/rusty
        //swarm.params.flashFringeColor = new Color(255,95,50,50);

        swarm.getParams().flashFringeColor = new Color(255,70,30,50);
        swarm.getParams().flashCoreRadiusMult = 0f;

        swarm.getParams().springStretchMult = 1f;

        //swarm.params.baseSpriteSize *= RECLAMATION_SWARM_FRAGMENT_SIZE_MULT;
        //swarm.params.flashFringeColor = VoltaicDischargeOnFireEffect.EMP_FRINGE_COLOR;

        float collisionMult = NanoThief_RecreationScript.RECLAMATION_SWARM_COLLISION_MULT;
        float hpMult = NanoThief_RecreationScript.RECLAMATION_SWARM_HP_MULT;

        for (BoundsAPI.SegmentAPI s : fighter.getExactBounds().getOrigSegments()) {
            s.getP1().scale(collisionMult);
            s.getP2().scale(collisionMult);
            s.set(s.getP1().x, s.getP1().y, s.getP2().x, s.getP2().y);
        }
        fighter.setCollisionRadius(fighter.getCollisionRadius() * collisionMult);

        fighter.setMaxHitpoints(fighter.getMaxHitpoints() * hpMult);
        fighter.setHitpoints(fighter.getHitpoints() * hpMult);

        swarm.getParams().maxOffset *= NanoThief_RecreationScript.RECLAMATION_SWARM_RADIUS_MULT;

        swarm.getParams().initialMembers = 0;
        swarm.getParams().baseMembersToMaintain = 50;*/

        //return fighter;

        for (Nano_Thief_SKill_Base a : skills){
            a.changeCombatSwarmStats(primary,quality);
        }

    }
    public void createDefenseSwarm(ShipAPI primary,int quality){
        for (Nano_Thief_SKill_Base a : skills){
            a.changeDefenderSwarmStats(primary,quality);
        }
    }

    private class playerReclaimDisplay implements AdvanceableListener{
        //an attempt to create a display. failed.
        public playerReclaimDisplay(ShipAPI ship){
            this.ship = ship;
        }
        @Setter
        int value;
        @Getter
        @Setter
        ShipAPI ship;

        @Override
        public void advance(float amount) {
            if (!ship.equals(Global.getCombatEngine().getPlayerShip())){
                ship = Global.getCombatEngine().getPlayerShip();
                value = reclaimGathered.getOrDefault(ship,0);
            }
            if (ship == null) return;
            if (value != 0) {
                Global.getCombatEngine().maintainStatusForPlayerShip(NanoThiefStorgeKey, "graphics/icons/hullsys/temporal_shell.png",
                    "Reclaim Gathered", value + " reclaim ready", false);
            }
        }
    }
}
