package Abyssal_XO.data.scripts.threat;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.hullmods.ReclaimCore;
import Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_Construction;
import Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_Reclaim;
import Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_SawrmSpawner;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_BattleListener;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_RecreationScript;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_6;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_8;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_MasteryShipStats;
import Abyssal_XO.data.scripts.threat.skills.Nano_Thief_Skill_Base;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.*;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.impl.combat.threat.*;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import com.fs.starfarer.api.loading.VariantSource;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import com.fs.starfarer.api.util.SkillData;
import com.fs.starfarer.util.DynamicStats;
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
import java.util.HashSet;
import java.util.List;

import static Abyssal_XO.data.scripts.Settings.NANO_THIEF_RECLAIM_RECYCLE_PERCENT;

public class Nano_Thief_Stats {
    private static Logger log = Global.getLogger(Nano_Thief_Stats.class);
    public NanoThief_MasteryShipStats[] masteryShips;
    //public HashMap<> reservedDP;

    public float DF_productionTime = 1;
    public float DF_swarmCost = 100;
    public float DF_recyclePerFighter = 0;
    public float DF_ttl = 60;
    public String DF_fighterToBuild = Settings.NANO_THIEF_BASEWING;//Settings.NANO_THIEF_PALYER_BASEWING;
    public ShipHullSpecAPI DF_fighterHullSpec;
    public int DF_wingSize;


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
    private String commanderID;
    private SCOfficer officer;
    public boolean closest = true;
    @Getter
    ShipAPI centralFab = null;

    @Getter
    private ArrayList<Nano_Thief_Skill_Base> skills = new ArrayList<>();

    @Deprecated
    private float reclaimPerControl = Settings.NANO_THIEF_ReclaimPerControl_BASE;
    private boolean isAlly = false;

    @Getter
    private HashSet<ShipAPI> availableShips=new HashSet<>();
    public int deployedDP=0;
    @Getter
    private static float playerExstraReclaim = 0;
    private static Nano_Thief_Stats playerStats;

    private boolean centralFabAlive = true;
    //@Getter
    //private ArrayList<FighterWingAPI> OffinciveFighterWings = new ArrayList<>();
    @Getter
    private ArrayList<ShipAPI> OffinciveFighterCores = new ArrayList<>();//might not be needed we will see.
    public static void setPlayerExstraReclaimIfRequired(){
        if (playerStats == null) return;
        playerStats.makeSureSavedShipsAreAlive();
        playerExstraReclaim = 0;
        for (ShipAPI ship : playerStats.availableShips){
            NanoThief_ShipSkills listiner = null;
            if (ship.hasListenerOfClass(NanoThief_ShipSkills.class)){
                List<NanoThief_ShipSkills> b = ship.getListenerManager().getListeners(NanoThief_ShipSkills.class);
                listiner = b.get(0);
                if (playerStats.centralFabAlive && ship.equals(playerStats.centralFab)){
                    playerExstraReclaim += listiner.getTotalReclaimIncludingIncomeing();
                    int toRefine = 0;
                    for (NanoThief_SkillBase a : listiner.getAlwaysSkills()) if (a instanceof NanoThief_Skill_8){
                        toRefine += ((NanoThief_Skill_8) a).fakeReclaim * NanoThief_8.reclaimRaito;
                        break;
                    }
                    playerExstraReclaim += (toRefine*NanoThief_8.reclaimRaito);
                }else {
                    playerExstraReclaim += listiner.getTotalReclaimIncludingIncomeing();
                }
            }else{
                //target.addListener(new NanoThief_ShipSkills(this,target));
                log.info("WARNING: NO LISTINER! SPOOKY!");
            }
        }
        playerStats = null;
    }
    /*public Nano_Thief_Stats(String OF_fighterToBuild) {
        if (OF_fighterToBuild != null) this.OF_fighterToBuild = OF_fighterToBuild;
    }*/
    public PersonAPI commander;
    public FleetDataAPI fleet;
    public int owner;
    public FactionAPI faction;
    public Nano_Thief_Stats(NanoThief_MasteryShipStats[] ships){
        getBastStatsForMastery(ships,this);
    }
    public Nano_Thief_Stats(String fighter,boolean isOffincive){
        //this is for creating a stat line that only looks has fighter data.
        if(isOffincive) {
            OF_fighterToBuild = fighter;
            NanoThief_Skill_6.getStats(this,Global.getSettings().getFighterWingSpec(OF_fighterToBuild));
            return;
        }
        DF_fighterToBuild = fighter;
        NanoThief_Skill_7.getStats(this,Global.getSettings().getFighterWingSpec(DF_fighterToBuild));

    }

    public Nano_Thief_Stats(PersonAPI commander, FleetDataAPI fleet, String commanderID, boolean isAlly, SCOfficer officer, int owner,FactionAPI faction){
        this.commander = commander;
        this.fleet = fleet;
        this.commanderID = commanderID;
        this.officer = officer;
        this.isAlly = isAlly;
        this.owner = owner;
        this.faction = faction;
        log.info("creating commander data for a new commander with "+officer.getActiveSkillPlugins().size()+" skills"+" and a fighter to build of "+this.OF_fighterToBuild);
        for (SCBaseSkillPlugin a : officer.getActiveSkillPlugins()){
            Nano_Thief_Skill_Base b = (Nano_Thief_Skill_Base) a;
            log.info("  adding skill to commander of: "+b.getName());
            skills.add(b);
            b.initStats(this);
            if (b.getId().equals("SiC_NanoThief_skill_8")){
                closest = false;
            }
        }
        //getBaseStatsForFighter(Global.getSettings().getFighterWingSpec(this.OF_fighterToBuild),this,true);
        if (Global.getSector().getPlayerPerson().getId().equals(commanderID)){
            playerStats = this;
        }
    }
    public NanoThief_ShipSkills getSkills(ShipAPI shipAPI){
        if (!availableShips.contains(shipAPI)) return null;
        NanoThief_ShipSkills listiner = null;
        if (shipAPI.hasListenerOfClass(NanoThief_ShipSkills.class)){
            List<NanoThief_ShipSkills> a = shipAPI.getListenerManager().getListeners(NanoThief_ShipSkills.class);
            listiner = a.get(0);
            return listiner;
        }
        log.info("WARNING: NO LISTINER! SPOOKY!");
        return null;
    }
    public boolean centralFabAlive(){
        if (centralFab == null) return false;
        return centralFab.isAlive() && !centralFab.isHulk();
    }

    public boolean canAcceptReclaim(ShipAPI ship){
        if (ship.getParentStation() != null) return false;
        if (!isValidReclaimTarget(ship)) return false;
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
        if (ship.getTags().contains(ThreatShipConstructionScript.SHIP_UNDER_CONSTRUCTION)) return false;//never send reclaim to a ship untill after it is fully built..
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
        return closest ? getTargetClosest(reclaim, engine) : getPriorityClosest(reclaim, engine);
        /*if (closest){
            return getTargetClosest(reclaim,engine);
        }
        return getPriorityClosest(reclaim, engine);*/
    }
    private ShipAPI getTargetClosest(ShipAPI reclaim, CombatEngineAPI engine){
        log.info("gettig target closest");
        makeSureSavedShipsAreAlive();
        ShipAPI output = null;
        Vector2f pointA = reclaim.getLocation();
        float distance = Float.MAX_VALUE;
        for (ShipAPI curr : availableShips) {
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
            NanoThief_ShipSkills data = getSkills(curr);
            double rangeChange = 0;
            if (data != null) rangeChange = data.getTotalReclaimIncludingIncomeing() / 5;
            c+=rangeChange;

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
        log.info("gettig priority closest");
        if (centralFab == null){
            makeSureSavedShipsAreAlive();
            float priority = 0;
            float mass = 0;
            for (ShipAPI curr : availableShips) {
                if (curr == null) continue;
                if (!canAcceptReclaim(curr)) continue;
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
                    mass = curr.getMassWithModules();
                    priority = pTemp;
                }
            }
            if (centralFab != null){
                log.info("found central fabracator as a ship named "+centralFab.getName()+"...");
                getSkills(centralFab).addListener(new NanoThief_Skill_8(getSkills(centralFab),centralFab),centralFab);
            }else{
                log.warn("FAILED TO FIND CENTRAL FABRACATOR FOR FORCE. WARNING WARNING. THIS IS BAD. force was force"+(commander == null ? "ERROR: no command found" : commander.getNameString()));
            }
        }
        if (centralFab != null && centralFab.isAlive() && !centralFab.isHulk()){
            log.info("found central fabricator, avoiding question of target.");
            return centralFab;
        }else{
            log.info("central fab not alive for force of "+reclaim.getOwner()+". is null / allive / hulk?: "+centralFab == null+", "+(centralFab == null ? false : centralFab.isAlive())+", "+(centralFab == null ? false : centralFab.isHulk()));
            centralFabAlive = false;
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
            additional /= 2;//represents reclaim war over additional reclaim
        }
        if (reclaim.getCustomData().containsKey(Settings.NANO_THIEF_CUSTOM_MASTERY_RECLAIM_MEMERY_KEY)){
            amount = Math.min(amount, (int) reclaim.getCustomData().get(Settings.NANO_THIEF_CUSTOM_MASTERY_RECLAIM_MEMERY_KEY));
        }
        return (int) (amount+additional);
    }

    private static final String NanoThiefStorgeKey = "$NanoThief_StoredReclaim_Base";
    //private HashMap<ShipAPI,Integer> reclaimGathered = new HashMap<>();

    public void applyEffectsWhenAbsorbed(ShipAPI target,ShipAPI reclaim,int reclaimValue,boolean isRefined){
        NanoThief_ShipSkills listiner = getSkills(target);
        if (!closest){
            if (target.equals(centralFab)){
                if (isRefined){
                    listiner.addReclaim(reclaimValue);
                    return;
                }
                for (NanoThief_SkillBase a : getSkills(target).getAlwaysSkills()){
                    if (a instanceof NanoThief_Skill_8){
                        ((NanoThief_Skill_8) a).fakeReclaim+=reclaimValue;
                        break;
                    }
                }
                return;
            }
            if (!isRefined) reclaimValue *= NanoThief_8.baseReclaimEfficiencyMod;
        }
        listiner.addReclaim(reclaimValue);//,false);

    }
    public float getAvailableControl(){
        float prod = reclaimPerControl;
        return prod;

    }
    public float getModifedTTL(ShipAPI target){
        float prod = OF_ttl;
        return prod;
    }
    public ShipAPI createReclaim(ShipAPI primary,int forceID,int amount, boolean isRefined,ShipAPI targetOverride){
        if (amount == 0) return null;
        String wingId = SwarmLauncherEffect.RECLAMATION_SWARM_WING;//"attack_swarm_wing";SwarmLauncherEffect.RECLAMATION_SWARM_WING;//"attack_swarm_wing"

        CombatEngineAPI engine = Global.getCombatEngine();
        CombatFleetManagerAPI manager = engine.getFleetManager(forceID);
        manager.setSuppressDeploymentMessages(true);

        Vector2f loc = primary.getLocation();
        float facing = (float) Math.random() * 360f;

        ShipAPI fighter = manager.spawnShipOrWing(wingId, loc, facing, 0f, null);
        fighter.getWing().setSourceShip(primary);
        //fighter.removeTag(Tags.THREAT_SWARM_AI);
        log.info("calculated reclaim as: "+amount);
        fighter.setShipAI(new Nano_Thief_AI_Reclaim(fighter,this,amount,isRefined,targetOverride));
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

        int maxSwarmSize = (amount / 100) + 20;//base size is 20. + 10 per 1k size. so its 30,40,50,60 in size.
        swarm.getParams().initialMembers = 0;
        swarm.getParams().baseMembersToMaintain = amount;

        fighter.getMutableStats().getSightRadiusMod().modifyMult("Abyssal_XO",0.1f);
        if (isRefined) fighter.getMutableStats().getMaxSpeed().modifyMult("Abyssal_XO", (float) NanoThief_8.speedMod);
        return fighter;
    }
    public ShipAPI createReclaim(ShipAPI primary,int forceID){
        /*for (Nano_Thief_Skill_Base a : skills){
            a.changeReclaimStats(fighter,this);
        }*/
        int amount = getReclaimValue(primary);
        return createReclaim(primary,forceID,amount,false,null);
    }

    public void getShipSkills(ShipAPI ship){

    }

    public static void modifySingleFighter(ShipAPI fighter,ShipAPI frabacator){
        fighter.getMutableStats().getMinCrewMod().modifyMult("Abyssal_XO",0);
        if (fighter.getWing().getSpec().getId().equals(Settings.NANO_THIEF_BASEWING)) {
            modifiyBaseShip(fighter);
        }
    }
    public static void modifiyBaseShip(ShipAPI fighter){
        fighter.setDoNotRender(true);
        fighter.setExplosionScale(0f);
        fighter.setHulkChanceOverride(0f);
        fighter.setImpactVolumeMult(SwarmLauncherEffect.IMPACT_VOLUME_MULT);
        fighter.getArmorGrid().clearComponentMap(); // no damage to weapons/engines
        RoilingSwarmEffect swarm = FragmentSwarmHullmod.createSwarmFor(fighter);

    }
    public void makeSureSavedShipsAreAlive(){
        ArrayList<ShipAPI> remove = new ArrayList<>();
        for (ShipAPI b : this.availableShips){
            if (!b.isAlive() || b.isHulk()) remove.add(b);
        }
        for (ShipAPI a : remove){
            this.availableShips.remove(a);
        }
    }
    public int getDeployedPonits(){
        /*try {
            Global.getCombatEngine().getFleetManager(owner).getDeployedCopyDFM().get(0);
        }catch (Exception e){
            Settings.log.warn("failed to get deployment ponits. reason unknown.");
            return 0;
        }*/
        makeSureSavedShipsAreAlive();
        int output = 0;
        for (ShipAPI a : this.availableShips){
            if (a.getFleetMember() == null) continue;
            //a.getMutableStats();

            output += a.getFleetMember().getDeploymentPointsCost();
            //output += a.getMutableStats().getSuppliesToRecover().getModifiedInt();
            //qoutput += availableShips.get(a).getDeployCost();
            //availableShips.get(a).getMutableStats().
            //availableShips.get(a).getMutableStats().getDynamic().getMod(Stats.DEPLOYMENT_POINTS_MOD).computeEffective();
            //availableShips.get(a).getMutableStats();
            //log.info("checking ship of: "+availableShips.get(a).getName());
        }
        //log.info("got total deployed ponits as: "+output);
        return output;
    }
    public static void getBastStatsForMastery(NanoThief_MasteryShipStats[] ships,Nano_Thief_Stats stats){
        //????
        /*stats.MasteryStats = new ArrayList<>();
        for (Pair<ShipVariantAPI, Double> a : ships) {
            //stats.MasteryStats.add(new NanoThief_MasteryShipStats(a.one,a.two));
        }*/
    }
    private static void getBaseStatsForFighter(FighterWingSpecAPI a,Nano_Thief_Stats spec,boolean offincive){
        if (offincive){
            log.info("getting 'display stats'");
            NanoThief_Skill_6.getStats(spec,a);
            return;
        }
    }
    public static void displayStatsForFighterWithoutModification(TooltipMakerAPI panel,FighterWingSpecAPI a,boolean offincive){
        NanoThief_6.displayStats(panel,a,offincive);
    }

    public ArrayList<Nano_Thief_AI_Construction> constructors = new ArrayList<>();
    public int getDPWithToBeConstructed(int owner){
        //todo: this was copyed from another area. it looks like a good way to make sure I have anouth dp. lots of good dp code here.
        CombatEngineAPI engine = Global.getCombatEngine();
        CombatFleetManagerAPI manager = engine.getFleetManager(owner);//ship.getOwner());
        int dpLeft = manager.getMaxStrength() - manager.getCurrStrength();
        for (int a = constructors.size() - 1; a >= 0; a--){
            Nano_Thief_AI_Construction b = constructors.get(a);
            if (!b.ship.isAlive() || b.ship.isHulk() || b.getHasCreated()){
                constructors.remove(a);
                continue;
            }
            dpLeft -= b.dp;
        }
        return dpLeft;
    }
}
