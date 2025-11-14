package Abyssal_XO.data.scripts.threat;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.hullmods.ReclaimCore;
import Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_Reclaim;
import Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_SawrmSpawner;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_BattleListener;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_RecreationScript;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_ShipStats;
import Abyssal_XO.data.scripts.threat.skills.Nano_Thief_Skill_Base;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_ShipSkills;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_Skill_6;
import Abyssal_XO.data.scripts.threat_old.subsystems.DamageOverTime_System;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.combat.threat.FragmentSwarmHullmod;
import com.fs.starfarer.api.impl.combat.threat.RoilingSwarmEffect;
import com.fs.starfarer.api.impl.combat.threat.SwarmLauncherEffect;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import com.fs.starfarer.api.loading.VariantSource;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.lwjgl.util.vector.Vector2f;
import org.magiclib.subsystems.MagicSubsystemsManager;
import second_in_command.SCUtils;
import second_in_command.specs.SCBaseSkillPlugin;
import second_in_command.specs.SCOfficer;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static Abyssal_XO.data.scripts.Settings.NANO_THIEF_RECLAIM_RECYCLE_PERCENT;

public class Nano_Thief_Stats {
    public float OF_productionTime = 1;
    public float OF_swarmCost = 100;
    public float OF_recyclePerFighter = 0;
    public float OF_ttl = 60;
    @Deprecated
    public float OF_range;
    public String OF_fighterToBuild = Settings.NANO_THIEF_BASEWING;//Settings.NANO_THIEF_PALYER_BASEWING;
    public ShipHullSpecAPI OF_fighterHullSpec;
    public int OF_wingSize;


    private boolean wingSet = false;
    private static Logger log = Global.getLogger(Nano_Thief_Stats.class);
    private String commanderID;
    private SCOfficer officer;
    private boolean closest = true;
    @Getter
    ShipAPI centralFab = null;

    @Getter
    private ArrayList<Nano_Thief_Skill_Base> skills = new ArrayList<>();

    @Deprecated
    private float reclaimPerControl = Settings.NANO_THIEF_ReclaimPerControl_BASE;
    private boolean isAlly = false;

    @Getter
    private HashMap<String,ShipAPI> availableShips=new HashMap<>();
    public int deployedDP=0;
    @Getter
    private static float playerExstraReclaim = 0;
    private static Nano_Thief_Stats playerStats;

    //@Getter
    //private ArrayList<FighterWingAPI> OffinciveFighterWings = new ArrayList<>();
    @Getter
    private ArrayList<ShipAPI> OffinciveFighterCores = new ArrayList<>();//might not be needed we will see.
    public static void setPlayerExstraReclaimIfRequired(){
        if (playerStats == null) return;
        playerExstraReclaim = 0;
        for (String a : playerStats.availableShips.keySet()){
            ShipAPI ship = playerStats.availableShips.get(a);
            NanoThief_ShipSkills listiner = null;
            if (ship.hasListenerOfClass(NanoThief_ShipSkills.class)){
                List<NanoThief_ShipSkills> b = ship.getListenerManager().getListeners(NanoThief_ShipSkills.class);
                listiner = b.get(0);
                playerExstraReclaim+=listiner.getTotalReclaim();
            }else{
                //target.addListener(new NanoThief_ShipSkills(this,target));
                log.info("WARNING: NO LISTINER! SPOOKY!");
            }
        }
        playerStats = null;
    }
    public Nano_Thief_Stats(String OF_fighterToBuild) {
        if (OF_fighterToBuild != null) this.OF_fighterToBuild = OF_fighterToBuild;
    }
    public Nano_Thief_Stats(String commanderID, boolean isAlly, SCOfficer officer, String OF_fighterToBuild){
        if (OF_fighterToBuild != null) this.OF_fighterToBuild = OF_fighterToBuild;
        this.commanderID = commanderID;
        this.officer = officer;
        this.isAlly = isAlly;
        log.info("creating commander data for a new commander with "+officer.getActiveSkillPlugins().size()+" skills"+" and a fighter to build of "+this.OF_fighterToBuild);
        for (SCBaseSkillPlugin a : officer.getActiveSkillPlugins()){
            Nano_Thief_Skill_Base b = (Nano_Thief_Skill_Base) a;
            log.info("  adding skill to commander of: "+b.getName());
            skills.add(b);
            //SC_muti *= b.swarmCostMulti;
            //SC_add += b.swarmCostAdd;

            //SQ_muti *= b.qualityMulti;
            //SQ_add += b.qualityAdd;
            if (b.getId().equals("SiC_NanoThief_skill_6")){
                closest = false;
            }
        }
        //this.fighterToBuild = "claw_wing";//"warthog_wing";//"warthog_wing";//"trident_wing";//"dagger_wing";//"broadsword_wing";
        getBaseStatsForFighter(Global.getSettings().getFighterWingSpec(this.OF_fighterToBuild),this,true);
        if (Global.getSector().getPlayerPerson().getId().equals(commanderID)){
            playerStats = this;
        }
    }
    public void spawnReclaim() {

    }
    public boolean canAcceptReclaim(ShipAPI ship){
        if (ship.getParentStation() != null) return false;
        if (ship.getVariant() != null){
            for (String a : Settings.NanoThief_Banned){
                if (ship.getVariant().getSMods().contains(a)) return false;
                if (ship.getVariant().getPermaMods().contains(a)) return false;
                if (ship.getVariant().getHullMods().contains(a)) return false;
            }
        }
        return true;
    }
    public boolean isValidReclaimTarget(ShipAPI ship){
        return true;
    }
    public float getReclaimTargetDistanceMulti(ShipAPI ship){
        float out = 1;
        ShipVariantAPI variant = ship.getVariant();
        if (variant.hasHullMod("dweller_hullmod") || variant.hasHullMod("shrouded_mantle") || variant.hasHullMod("shrouded_thunderhead") || variant.hasHullMod("shrouded_lens")) out *= 1.1f;
        if (variant.hasHullMod("fragment_swarm") || /*variant.hasHullMod("fragment_coordinator") || variant.hasHullMod("secondary_fabricator") || */variant.hasHullMod("threat_hullmod")) out *= 0.9f;
        return out;
    }
    public float getReclaimTargetPriority(ShipAPI ship){
        float out = 1;
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
        //todo: change this to use the intiernal list of ships that can collect reclaim.
        if (closest){
            return getTargetClosest(reclaim,engine);
        }
        return getPriorityClosest(reclaim, engine);
    }
    private ShipAPI getTargetClosest(ShipAPI reclaim, CombatEngineAPI engine){
        ShipAPI output = null;
        Vector2f pointA = reclaim.getLocation();
        float distance = Float.MAX_VALUE;
        for (ShipAPI curr : availableShips.values()) {
            /*if (curr == null) continue;
            if (curr.isHulk()) continue;
            if (curr.equals(reclaim)) continue;
            if (curr.getFleetMember() == null) continue;
            //log.info("  has fleetmember");
            if (curr.getFleetMember().getFleetData() == null) continue;
            //log.info("  has fleetdata");
            if (curr.getFleetMember().getFleetData().getFleet() == null) continue;
            //log.info("  has fleet");
            if (!curr.getFleetMember().getFleetData().getFleet().equals(fleet)) continue;
            //log.info("  has right commander...");
            if (!isValidReclaimTarget(curr)) continue;*/
            if (curr == null) continue;
            if (!canAcceptReclaim(curr)) continue;
            if (curr.isHulk()) continue;
            if (curr.equals(reclaim)) continue;
            if (curr.getFleetCommander() == null) continue;
            //log.info("  has fleet commander");
            if (!curr.getFleetCommander().getId().equals(this.commanderID)) continue;
            //log.info("  has valid fleet commander");

            //log.info("  got valid reclaim target. comparing position....");
            Vector2f pointB = curr.getLocation();
            float c = Misc.getDistance(pointA,pointB);
            /*float a = pointA.x - pointB.x;
            float b = pointA.y - pointB.y;
            float c = (float) Math.sqrt( a*a + b*b);*/
            c *= getReclaimTargetDistanceMulti(curr);
            if (c < distance){
                //log.info("  getting a new valid reclaim target of distance "+c);
                distance = c;
                output = curr;
            }
        }
        //if (output != null) log.info("reclaim target has been chosen as "+output.toString());
        return output;
    }
    private ShipAPI getPriorityClosest(ShipAPI reclaim, CombatEngineAPI engine){
        if (centralFab == null){
            float priority = 0;
            float mass = 0;
            for (ShipAPI curr : availableShips.values()) {
                if (!canAcceptReclaim(curr)) continue;
                if (curr == null) continue;
                if (curr.isHulk()) continue;
                if (curr.equals(reclaim)) continue;
                if (curr.isFighter()) continue;
                if (curr.getFleetCommander() == null) continue;
                //log.info("  has fleet commander");
                if (!curr.getFleetCommander().getId().equals(this.commanderID)) continue;
                //log.info("  has valid fleet commander");
                if (curr.getVariant().hasHullMod(Settings.HULLMOD_CENTRAL_FAB)){
                    centralFab = curr;
                    break;
                }
                float pTemp = getReclaimTargetPriority(curr);
                if (pTemp > priority || (pTemp == priority && curr.getMassWithModules() > mass)){
                    centralFab = curr;
                    mass = pTemp;
                    priority = curr.getMassWithModules();
                }
            }
        }
        if (centralFab.isAlive() && !centralFab.isHulk()){
            log.info("found central fabricator, avoiding question of target.");
            return centralFab;
        }
        ShipAPI output = null;
        return getTargetClosest(reclaim,engine);
        /*Vector2f pointA = reclaim.getLocation();
        float reclaimPriority = 0;
        float distance = Float.MAX_VALUE;
        for (ShipAPI curr : engine.getShips()) {
            if (curr == null) continue;
            if (curr.isHulk()) continue;
            if (curr.equals(reclaim)) continue;
            if (curr.getFleetCommander() == null) continue;
            log.info("  has fleet commander");
            if (!curr.getFleetCommander().getId().equals(this.commanderID)) continue;
            log.info("  has valid fleet commander");

            float priority = getReclaimTargetPriority(curr);
            if (priority < reclaimPriority) continue;
            //log.info("  got valid reclaim target. comparing position....");
            Vector2f pointB = curr.getLocation();
            float c = Misc.getDistance(pointA,pointB);
            c *= getReclaimTargetDistanceMulti(curr);
            if (c < distance){
                //log.info("  getting a new valid reclaim target of distance "+c);
                distance = c;
                output = curr;
                reclaimPriority = priority;
            }
        }
        if (output != null) log.info("reclaim target has been chosen as "+output.toString());
        return output;*/

    }

    public int getReclaimValue(ShipAPI reclaim){
        log.info("calculating total increase in reclaim for a "+reclaim.getHullSpec().getHullSize()+" ship");
        log.info("  name:"+reclaim.getName());
        int amount;
        int skills = getSkills().size()-1;
        switch (reclaim.getHullSpec().getHullSize()){
            case CAPITAL_SHIP -> amount = Settings.NANO_THIEF_RECLAIM_GAIN[3]*skills;
            case CRUISER -> amount = Settings.NANO_THIEF_RECLAIM_GAIN[2]*skills;
            case DESTROYER -> amount = Settings.NANO_THIEF_RECLAIM_GAIN[1]*skills;
            default -> amount = Settings.NANO_THIEF_RECLAIM_GAIN[0]*skills;
        }
        double additional = NanoThief_BattleListener.getReclaimInShip(reclaim) * NANO_THIEF_RECLAIM_RECYCLE_PERCENT;
        if (!NanoThief_BattleListener.getHostileCaptions().isEmpty() && !NanoThief_BattleListener.getFriendlyCaptions().isEmpty()){
            additional /= 2;
        }
        return (int) (amount+additional);
    }

    private static final String NanoThiefStorgeKey = "$NanoThief_StoredReclaim_Base";
    //private HashMap<ShipAPI,Integer> reclaimGathered = new HashMap<>();

    public void applyEffectsWhenAbsorbed(ShipAPI target,ShipAPI reclaim,int reclaimValue){
        NanoThief_ShipSkills listiner = null;
        if (target.hasListenerOfClass(NanoThief_ShipSkills.class)){
            List<NanoThief_ShipSkills> a = target.getListenerManager().getListeners(NanoThief_ShipSkills.class);
            listiner = a.get(0);
        }else{
           //target.addListener(new NanoThief_ShipSkills(this,target));
            log.info("WARNING: NO LISTINER! SPOOKY!");
        }
        listiner.addReclaim(reclaimValue,false);

    }
    public float getModifiedCost(ShipAPI target){
        float quality = this.OF_swarmCost;
        return quality;

    }
    public float getModifedProductionTime(ShipAPI target){
        float prod = this.OF_productionTime;
        return prod;
    }
    public float getAvailableControl(){
        float prod = reclaimPerControl;
        return prod;

    }
    public float getModifedTTL(ShipAPI target){
        float prod = OF_ttl;
        return prod;
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
        int amount = getReclaimValue(primary);
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

        int maxSwarmSize = (amount / 1000) - 20;//-10,0,??? no....
        swarm.getParams().initialMembers = 0;
        swarm.getParams().baseMembersToMaintain = 40;

        /*for (Nano_Thief_Skill_Base a : skills){
            a.changeReclaimStats(fighter,this);
        }*/
        return fighter;
    }

    public void modifySingleFighter(ShipAPI fighter,ShipAPI frabacator){
        if (fighter.getWing().getSpec().getId().equals(Settings.NANO_THIEF_BASEWING)){
            modifiyBaseShip(fighter);
        }else{
            //modifiyCustomShip(fighter);
        }
        float ttl = getModifedTTL(fighter);
        fighter.getMutableStats().getMinCrewMod().modifyMult("Abyssal_XO",0);
        //log.info("changing swarm stats for a single fighter...");

        /*for (Nano_Thief_Skill_Base b : skills) {
            b.changeCombatSwarmStats(fighter,frabacator,this);
        }*/
        MagicSubsystemsManager.addSubsystemToShip(fighter, new DamageOverTime_System(fighter, ttl, OF_range));
    }
    public void modifiyBaseShip(ShipAPI fighter){
        fighter.setDoNotRender(true);
        fighter.setExplosionScale(0f);
        fighter.setHulkChanceOverride(0f);
        fighter.setImpactVolumeMult(SwarmLauncherEffect.IMPACT_VOLUME_MULT);
        fighter.getArmorGrid().clearComponentMap(); // no damage to weapons/engines
        RoilingSwarmEffect swarm = FragmentSwarmHullmod.createSwarmFor(fighter);

    }

    public int getDeployedPonits(){
        int output = 0;
        for (String a : this.availableShips.keySet()){
            output += availableShips.get(a).getDeployCost();
        }
        return output;
    }
    private static void getBaseStatsForFighter(FighterWingSpecAPI a,Nano_Thief_Stats spec,boolean offincive){
        if (offincive){
            NanoThief_Skill_6.getStats(spec,a);
            return;
        }
    }
    public static void displayStatsForFighterWithoutModification(TooltipMakerAPI panel,FighterWingSpecAPI a,boolean offincive){
        if (offincive){
            NanoThief_Skill_6.displayStats(panel,a);
            return;
        }
    }

}
