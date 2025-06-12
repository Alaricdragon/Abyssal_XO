package Abyssal_XO.data.scripts.threat;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.AI.*;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_RecreationScript;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_ShipStats;
import Abyssal_XO.data.scripts.threat.skills.Nano_Thief_SKill_Base;
import Abyssal_XO.data.scripts.threat.subsystems.DamageOverTime_System;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.impl.combat.threat.FragmentSwarmHullmod;
import com.fs.starfarer.api.impl.combat.threat.RoilingSwarmEffect;
import com.fs.starfarer.api.impl.combat.threat.SwarmLauncherEffect;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import com.fs.starfarer.api.loading.WingRole;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.lwjgl.util.vector.Vector2f;
import org.magiclib.subsystems.MagicSubsystemsManager;
import second_in_command.SCUtils;
import second_in_command.specs.SCBaseSkillPlugin;
import second_in_command.specs.SCOfficer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Nano_Thief_Stats {

    private boolean wingSet = false;
    private float productionTime = 1;
    private float swarmCost = 100;
    private static Logger log = Global.getLogger(Nano_Thief_Stats.class);
    private String commanderID;
    private SCOfficer officer;
    private boolean closest = true;
    @Getter
    ShipAPI centralFab = null;

    ArrayList<Nano_Thief_SKill_Base> skills = new ArrayList<>();

    private float reclaimPerControl = 1000;
    private float ttl = 60;
    private float range;

    private String fighterToBuild = Settings.NANO_THIEF_BASEWING;
    private ShipHullSpecAPI fighterHullSpec;
    public Nano_Thief_Stats(String fighterToBuild) {
        if (fighterToBuild != null) this.fighterToBuild = fighterToBuild;
    }
    public Nano_Thief_Stats(String commanderID, SCOfficer officer,String fighterToBuild){
        if (fighterToBuild != null) this.fighterToBuild = fighterToBuild;
        this.commanderID = commanderID;
        this.officer = officer;
        log.info("creating commander data for a new commander with "+officer.getActiveSkillPlugins().size()+" skills"+" and a fighter to build of "+this.fighterToBuild);
        for (SCBaseSkillPlugin a : officer.getActiveSkillPlugins()){
            Nano_Thief_SKill_Base b = (Nano_Thief_SKill_Base) a;
            log.info("  adding skill to commander of: "+b.getName());
            skills.add(b);
            //SC_muti *= b.swarmCostMulti;
            //SC_add += b.swarmCostAdd;

            //SQ_muti *= b.qualityMulti;
            //SQ_add += b.qualityAdd;
            if (b.getId().equals("SiC_NanoThief_skill_6")){
                closest = true;
            }
        }
        //this.fighterToBuild = "claw_wing";//"warthog_wing";//"warthog_wing";//"trident_wing";//"dagger_wing";//"broadsword_wing";
        getBaseStatsForFighter(Global.getSettings().getFighterWingSpec(this.fighterToBuild),this);
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
        if (closest){
            return getTargetClosest(reclaim,engine);
        }
        return getPriorityClosest(reclaim, engine);
    }
    private ShipAPI getTargetClosest(ShipAPI reclaim, CombatEngineAPI engine){
        ShipAPI output = null;
        Vector2f pointA = reclaim.getLocation();
        float distance = Float.MAX_VALUE;
        for (ShipAPI curr : engine.getShips()) {
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
            for (ShipAPI curr : engine.getShips()) {
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
        int amount=0;
        log.info("calculating total increase in reclaim for a "+reclaim.getHullSpec().getHullSize()+" ship");
        log.info("  name:"+reclaim.getName());
        switch (reclaim.getHullSpec().getHullSize()){
            case CAPITAL_SHIP -> amount = Settings.NANO_THIEF_RECLAIM_GAIN[3];
            case CRUISER -> amount = Settings.NANO_THIEF_RECLAIM_GAIN[2];
            case DESTROYER -> amount = Settings.NANO_THIEF_RECLAIM_GAIN[1];
            default -> amount = Settings.NANO_THIEF_RECLAIM_GAIN[0];
        }
        return amount;
    }

    private static final String NanoThiefStorgeKey = "$NanoThief_StoredReclaim_Base";
    //private HashMap<ShipAPI,Integer> reclaimGathered = new HashMap<>();
    public int randomIntTemp = 0;
    public void applyEffectsWhenAbsorbed(ShipAPI target,ShipAPI reclaim,int reclaimValue){
        NanoThief_ShipStats listiner = null;
        if (!target.hasListenerOfClass(NanoThief_ShipStats.class)) {
            listiner = new NanoThief_ShipStats(target,this);
            target.addListener(listiner);
            randomIntTemp++;
            //log.info("adding listener for ship of: "+target.getName());
        }else{
            List<NanoThief_ShipStats> a = target.getListenerManager().getListeners(NanoThief_ShipStats.class);
            listiner = a.get(0);
            //log.info("got listener for ship of: "+listiner.getShip().getName());
        }
        //log.info("adding reclaim to ship");
        //int currentReclaim = reclaimValue;
        listiner.addReclaim(reclaimValue);
        /*float cost = swarmCost;
        currentReclaim += (int)target.getCustomData().getOrDefault(NanoThiefStorgeKey,0);

        currentReclaim += reclaimGathered.getOrDefault(target,0);

        while(currentReclaim >= cost){
            log.info("adding a combat swarm....");
            currentReclaim-=cost;
            createCombatSwarm(target,getModifiedQuality(target));
        }
        log.info("got remaining reclaim as: "+currentReclaim);
        reclaimGathered.put(target,currentReclaim);
        log.info("chacking memory as: "+reclaimGathered.get(target));*/
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
            a.ApplyChangeOnReclaim(target,reclaim,reclaimValue,this);
        }
    }
    public float getModifiedCost(ShipAPI target){
        float quality = this.swarmCost;
        for (Nano_Thief_SKill_Base a : skills){
            quality = a.costChange(quality,target,this);
        }
        return quality;

    }
    public float getModifedProductionTime(ShipAPI target){
        float prod = this.productionTime;
        for (Nano_Thief_SKill_Base a : skills){
            prod = a.manufactureTimeChange(prod,target,this);
        }
        return prod;
    }
    public float getModifedReclaimPerControl(ShipAPI target){
        float prod = reclaimPerControl;
        for (Nano_Thief_SKill_Base a : skills){
            prod = a.reclaimPerControlChange(prod,target,this);
        }
        return prod;

    }
    public float getModifedTTL(ShipAPI target){
        float prod = ttl;
        for (Nano_Thief_SKill_Base a : skills){
            prod = a.timeToLiveChange(prod,target,this);
        }
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

        for (Nano_Thief_SKill_Base a : skills){
            a.changeReclaimStats(fighter,this);
        }
        return fighter;
    }
    public ShipAPI createCombatSwarm(ShipAPI primary){

        String wingId = fighterToBuild;//Settings.NANO_THIEF_BASEWING;//SwarmLauncherEffect.RECLAMATION_SWARM_WING;//"attack_swarm_wing"
        CombatEngineAPI engine = Global.getCombatEngine();
        CombatFleetManagerAPI manager = engine.getFleetManager(primary.getOriginalOwner());
        manager.setSuppressDeploymentMessages(true);

        Vector2f loc = primary.getLocation();
        float facing = (float) Math.random() * 360f;
        //log.info("attempting to create a attack swarm at "+loc.x+", "+loc.y+" at ship of "+primary.getName()+" who's location is "+primary.getLocation().x+", "+primary.getLocation().y);
        ShipAPI fighter = manager.spawnShipOrWing(wingId, loc, facing, 0f, null);
        fighter.getWing().setSourceShip(primary);//sets to ifself to prevent min ingagment rage from triggering. might remove if i build a custom AI for the ships.

        manager.setSuppressDeploymentMessages(false);


        Vector2f takeoffVel = Misc.getUnitVectorAtDegreeAngle(facing);
        takeoffVel.scale(fighter.getMaxSpeed() * 1f);

        if (wingId.equals(Settings.NANO_THIEF_BASEWING)){
            modifiyBaseShip(fighter);
        }else{
            modifiyCustomShip(fighter);
        }

        Vector2f.add(fighter.getVelocity(), takeoffVel, fighter.getVelocity());



        /*for (Nano_Thief_SKill_Base a : skills){
        }*/
        return fighter;
    }
    public void modifiyBaseShip(ShipAPI fighter){
        //getBaseStatsForFighter(fighter);
        fighter.setDoNotRender(true);
        fighter.setExplosionScale(0f);
        fighter.setHulkChanceOverride(0f);
        fighter.setImpactVolumeMult(SwarmLauncherEffect.IMPACT_VOLUME_MULT);
        fighter.getArmorGrid().clearComponentMap(); // no damage to weapons/engines
        RoilingSwarmEffect swarm = FragmentSwarmHullmod.createSwarmFor(fighter);
        //fighter.setShipAI(new Nano_Thief_AI_CustomSwarm_Shell(fighter,this,120));
        float ttl = getModifedTTL(fighter);
        for (ShipAPI a : fighter.getWing().getWingMembers()){
            MagicSubsystemsManager.addSubsystemToShip(a, new DamageOverTime_System(a, ttl,range));
            for (Nano_Thief_SKill_Base b : skills) {
                b.changeCombatSwarmStats(a,this);
            }
        }
        for (Nano_Thief_SKill_Base b : skills) {
            b.changeCombatSwarmStats(fighter.getWing(),this);
        }

    }
    public void modifiyCustomShip(ShipAPI fighter){
        //ship.getAllWeapons().get(0).isFiring();
        //so, thats a thing. I could 100% do that now. fuck me and fuck you all.
        //fighter.getWing();
        //fighter.getMutableStats();
        //fighter.setShipAI(new Nano_Thief_AI_CustomSwarm_Shell(fighter,this));
        //fighter.getWing().setSourceShip(fighter);

        //so, this worked. but it only wokred in the contect of having the ships act strange. (if I removed the set sorce ship null it worked but only in engagment range)/
        //I am keeping this line, to better remember how mush I am fucking pissed what the hell game?
        //at least its all fixed now.... hopefully.
        //fighter.getWing().setSourceShip(null);
        //new Nano_Thief_AI_OVERRIDE(fighter,this);

        CombatEngineAPI engine = Global.getCombatEngine();
        CombatFleetManagerAPI manager = engine.getFleetManager(fighter.getOriginalOwner());
        manager.setSuppressDeploymentMessages(true);
        Vector2f loc = fighter.getLocation();
        float facing = (float) Math.random() * 360f;
        //log.info("attempting to create a attack swarm at "+loc.x+", "+loc.y+" at ship of "+primary.getName()+" who's location is "+primary.getLocation().x+", "+primary.getLocation().y);
        ShipAPI temp = manager.spawnShipOrWing("talon_Interceptor", loc, facing, 0f, null);
        temp.getMutableStats().getFighterWingRange().modifyFlat("Abyssal_XO",5000000);
        fighter.getWing().setSourceShip(temp);
        //manager.removeDeployed(temp,false);
        /*fighter.setDoNotRender(true);
        fighter.setExplosionScale(0f);
        fighter.setHulkChanceOverride(0f);
        fighter.setImpactVolumeMult(SwarmLauncherEffect.IMPACT_VOLUME_MULT);
        fighter.getArmorGrid().clearComponentMap(); // no damage to weapons/engines*/
        engine.removeEntity(temp);
        manager.removeDeployed(temp,false);

        manager.setSuppressDeploymentMessages(false);

        //fighter.getWing().getSpec().setRange(1000000);

        //fighter.getWing().setLeader(fighter);
        //fighter.getWing().getSpec().setRange(1000000);
        //fighter.getMutableStats().getFighterWingRange().unmodify();
        //fighter.getWing();
        //fighter.getMutableStats().getFighterWingRange().modifyFlat("Abyssal_XO_WING_MULTI",1000000);

        float ttl = getModifedTTL(fighter);
        for (ShipAPI a : fighter.getWing().getWingMembers()){
            //fighter.setShipAI(new Nano_Thief_AI_CustomSwarm_Shell(fighter,this));
            //fighter.setShipAI(new Nano_Thief_NoneCombatAI(fighter));
            MagicSubsystemsManager.addSubsystemToShip(a, new DamageOverTime_System(a, ttl,range));
            a.getMutableStats().getMinCrewMod().modifyMult("Abyssal_XO",0);
            //a.addTag("swarm_fighter");//hopefully, this helps. but it might not be. or maybe I should be puting this on the figher? mmm...
            for (Nano_Thief_SKill_Base b : skills) {
                b.changeCombatSwarmStats(fighter,this);
            }
        }

    }
    public ShipAPI createDefenseSwarm(ShipAPI primary){
        /*for (Nano_Thief_SKill_Base a : skills){
            a.changeDefenderSwarmStats(primary,quality);
        }*/
        return createCombatSwarm(primary);
    }


    private static void getBaseStatsForFighter(FighterWingSpecAPI a,Nano_Thief_Stats spec){
        /*if (wingSet) return;
        wingSet = true;*/
        spec.fighterHullSpec = a.getVariant().getHullSpec();
        float range=0;
        for (String b : a.getVariant().getFittedWeaponSlots()){
            if (b == null) continue;
            float c = a.getVariant().getWeaponSpec(b).getMaxRange();
            if (c > range) range = c;
        }
        spec.range = Math.max(1000,range+200);
        if (a.getId().equals(Settings.NANO_THIEF_BASEWING)){
            spec.swarmCost = Settings.NANO_THIEF_BASESWARM_COST;
            spec.productionTime = Settings.NANO_THIEF_BASESWARM_BUILDTIME;
            spec.ttl = Settings.NANO_THIEF_BASESWARM_TTL;
            log.info("got swarm of ID: "+a.getId()+" stats as: cost: "+spec.swarmCost+", productionTime: "+spec.productionTime+", time to live"+spec.ttl+", and range: "+spec.range);
            return;
        }
        spec.productionTime = a.getNumFighters() * a.getRefitTime() * Settings.NANO_THIEF_CustomSwarm_BUILDTIME_PREREFIT;
        if (a.getRole().equals(WingRole.BOMBER)){
            spec.ttl = Settings.NANO_THIEF_CustomSwarm_Bomber_TTL;
        }else {
            spec.ttl = Settings.NANO_THIEF_CustomSwarm_TTL;
        }
        spec.swarmCost = (a.getOpCost(a.getVariant().getStatsForOpCosts())*Settings.NANO_THIEF_CustomSwarm_COST_PEROP)+Settings.NANO_THIEF_CustomSwarm_COST_BASE;
        log.info("got swarm of ID: "+a.getId()+" stats as: cost: "+spec.swarmCost+", productionTime: "+spec.productionTime+", time to live"+spec.ttl+", and range: "+spec.range);
    }
    public static void displayStatsForFighterWithoutModification(TooltipMakerAPI panel,FighterWingSpecAPI a){
        Nano_Thief_Stats spec = null;//new Nano_Thief_Stats(a.getId());
        for (SCOfficer b : SCUtils.getFleetData(Global.getSector().getPlayerFleet()).getActiveOfficers()){
            //log.info("      checking SiC officer of atrubuteID: "+b.getAptitudeId());
            if (!b.getAptitudeId().equals("Abyssal_NanoThief")) continue;
            //log.info("      added Sic officer from fleet "+a.getId()+" to list of commanders....");
            spec = new Nano_Thief_Stats(a.getId(),b,a.getId());
            break;
        }
        if (spec == null){
            spec = new Nano_Thief_Stats(a.getId());
        }
        getBaseStatsForFighter(a,spec);
        spec.ttl = spec.getModifedTTL(null);
        spec.productionTime = spec.getModifedProductionTime(null);
        spec.swarmCost = spec.getModifiedCost(null);
        //spec.getModifed
        //I want ttl, swarmCost, and productiontime.
        panel.addPara("Simulacrum Fighter Wing stats for fleet: ",5);
        panel.addPara("Time to live: %s",5,Misc.getTextColor(), Misc.getHighlightColor(),""+(int)spec.ttl);
        panel.addPara("Production time: %s",5,Misc.getTextColor(), Misc.getHighlightColor(),""+(int)spec.productionTime);
        panel.addPara("Reclaim cost: %s",5,Misc.getTextColor(), Misc.getHighlightColor(),""+(int)spec.swarmCost);
        panel.addPara("NOTE: This does not incude conditional bonuses, like the 'Centralized Production' skill",5,Misc.getNegativeHighlightColor());

    }
    /*
    private void getBaseStatsForFighter(ShipAPI a){
        if (wingSet) return;
        wingSet = true;
        float range=0;
        for (WeaponAPI b : a.getAllWeapons()){
            if (b.getRange() > range) range = b.getRange();
        }
        this.range = Math.min(1000,range+200);
        if (a.getWing().getWingId().equals(Settings.NANO_THIEF_BASEWING)){
            log.info("creating a base wing...");
            this.swarmCost = Settings.NANO_THIEF_BASESWARM_COST;
            this.productionTime = Settings.NANO_THIEF_BASESWARM_BUILDTIME;
            this.ttl = Settings.NANO_THIEF_BASESWARM_TTL;
            return;
        }
        this.swarmCost = a.getWing().getSpec().getOpCost(a.getMutableStats()) * Settings.NANO_THIEF_CustomSwarm_COST_PEROP;
        this.productionTime = a.getWing().getSpec().getNumFighters() * a.getWing().getSpec().getRefitTime() * Settings.NANO_THIEF_CustomSwarm_BUILDTIME_PREREFIT;
        this.ttl = Settings.NANO_THIEF_BASESWARM_TTL;
    }*/
}
