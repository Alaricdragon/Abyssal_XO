package Abyssal_XO.data.scripts.threat.listiners;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_OVERRIDE;
import Abyssal_XO.data.scripts.threat.AI.Nano_Thief_NoneCombatAI;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.combat.threat.ThreatCombatStrategyAI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import org.apache.log4j.Logger;
import org.lwjgl.util.vector.Vector2f;
import second_in_command.SCUtils;
import second_in_command.specs.SCOfficer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static Abyssal_XO.data.scripts.Settings.TAG_HASRECLAMED;

public class NanoThief_BattleListener extends BaseEveryFrameCombatPlugin {
    private static Logger log = Global.getLogger(NanoThief_BattleListener.class);
    private HashMap<String,Nano_Thief_Stats> friendlyCaptions;
    private HashMap<String,Nano_Thief_Stats> hostileCaptions;

    public NanoThief_BattleListener(){
        //Nano_Thief_AI_OVERRIDE.reset();
        //Nano_Thief_NoneCombatAI.init();
        CombatEngineAPI engine = Global.getCombatEngine();
        log.info("(NanoThief)attempting to get commanders for friendly fleet...");
        friendlyCaptions = getCommanders(engine.getFleetManager(FleetSide.PLAYER).getAllFleetCommanders());
        log.info("(NanoThief)attempting to get commanders for hostile fleet...");
        hostileCaptions = getCommanders(engine.getFleetManager(FleetSide.ENEMY).getAllFleetCommanders());
        //SCUtils.getFleetData()
        //startup data....
    }
    /*private HashMap<CampaignFleetAPI,Nano_Thief_Stats> getPlayerForce(CombatEngineAPI engine){
        HashMap<CampaignFleetAPI,Nano_Thief_Stats> output = new HashMap<>();
        log.info("attempting to add player SiC to force...");
        if (Global.getSector() == null) return output;
        if (Global.getSector().getPlayerFleet() == null) return output;
        for (SCOfficer b : SCUtils.getFleetData(Global.getSector().getPlayerFleet()).getActiveOfficers()){
            log.info("      checking SiC officer of atrubuteID: "+b.getAptitudeId());
            if (!b.getAptitudeId().equals("SiC_NanoThief_NanoThief")) continue;
            log.info("      added player SiC officer to list of commanders....");
            output.put(Global.getSector().getPlayerFleet(),new Nano_Thief_Stats(Global.getSector().getPlayerFleet(),b));
            break;
        }
        return output;
    }*/
    private HashMap<String,Nano_Thief_Stats> getCommanders(java.util.List<com.fs.starfarer.api.characters.PersonAPI> commanders){
        HashMap<String,Nano_Thief_Stats> output = new HashMap<>();
        CombatEngineAPI engine = Global.getCombatEngine();
        for (PersonAPI a : commanders){
            if (a == null) continue;
            String customFighter = null;
            log.info("  checking commander "+a.getId());
            CampaignFleetAPI fleet;
            if (a.isPlayer()){
                fleet = Global.getSector().getPlayerFleet();
                if(Global.getSector().getPlayerPerson().getMemoryWithoutUpdate().contains(Settings.NANO_THIEF_CUSTOM_WING_MEMORY_KEY)) {
                    customFighter = Global.getSector().getPlayerPerson().getMemoryWithoutUpdate().getString(Settings.NANO_THIEF_CUSTOM_WING_MEMORY_KEY);
                }else{
                    customFighter = Settings.NANO_THIEF_PALYER_BASEWING;
                }
            }else{
                if(a.getMemoryWithoutUpdate().contains(Settings.NANO_THIEF_CUSTOM_WING_MEMORY_KEY)) {
                    customFighter = a.getMemoryWithoutUpdate().getString(Settings.NANO_THIEF_CUSTOM_WING_MEMORY_KEY);
                }
                fleet = a.getFleet();
            }
            if (fleet == null) continue;
            log.info("  commander does in fact have a fleet.");
            for (SCOfficer b : SCUtils.getFleetData(fleet).getActiveOfficers()){
                log.info("      checking SiC officer of atrubuteID: "+b.getAptitudeId());
                if (!b.getAptitudeId().equals("Abyssal_NanoThief")) continue;
                log.info("      added Sic officer from fleet "+a.getId()+" to list of commanders....");
                output.put(a.getId(),new Nano_Thief_Stats(a.getId(),b,customFighter));//a.getFleet() is required.
                break;
            }
            log.info("  finished check for commander");
        }
        log.info("  got "+output.size()+" valid fleet commanders.");
        return output;
    }
    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        if (amount == 0) return;
        for (ShipAPI a : Global.getCombatEngine().getShips()) {
           attemptToTranformTarget(a,amount);
        }
        Nano_Thief_AI_OVERRIDE.advanceAll(amount);
    }
    public void attemptToTranformTarget(ShipAPI target,float amount){
        if (target == null) return;
        if (target.isFighter()) return;
        if (!target.isHulk()) return;
        if (target.hasTag(TAG_HASRECLAMED)) return;
        if (ThreatCombatStrategyAI.isFabricator(target)) return;
        //if (!ship.isHulk() || ship.hasTag(TAG_HASRECLAMED)) return;
        //if (ThreatCombatStrategyAI.isFabricator(ship)) return;

        float elapsedAsHulk = 0f;
        String key = "SiC_NanoThief_elapsedAsHulkKey";
        if (target.getCustomData().containsKey(key)) {
            elapsedAsHulk = (float) target.getCustomData().get(key);
        }
        elapsedAsHulk += amount;
        target.setCustomData(key, elapsedAsHulk);
        if (elapsedAsHulk > 1f) {
            CombatEngineAPI engine = Global.getCombatEngine();
            //int owner = target.getOriginalOwner();
            boolean found = false;
            /*for (ShipAPI curr : engine.getShips()) {
                //if (curr == target || curr.getOwner() != owner) continue;
                //if (curr.isHulk() || curr.getOwner() == 100) continue;
                //if (!Nano_Thief_Utils.canAcceptReclaim(curr)) continue;
                //if (curr.getCurrentCR() >= 1f) continue;
                break;
            }*/
            Pair<Nano_Thief_Stats, Integer> force0 = getPreferredCommander(target,friendlyCaptions);
            Pair<Nano_Thief_Stats, Integer> force1 = getPreferredCommander(target,hostileCaptions);
            if (force0.one != null){
                Global.getCombatEngine().addPlugin(new NanoThief_RecreationScript(force0,target, 3f));
                found = true;
            }
            if (force1.one != null){
                Global.getCombatEngine().addPlugin(new NanoThief_RecreationScript(force1,target, 3f));
            }
            if (!found) {
                target.setCustomData(key, 0f);
            }
        }

    }

    public Pair<Nano_Thief_Stats,Integer> getPreferredCommander(ShipAPI target,HashMap<String,Nano_Thief_Stats> commanders){
        Pair<Nano_Thief_Stats,Integer> output = new Pair<Nano_Thief_Stats,Integer>();
        float distance = Float.MAX_VALUE;
        CombatEngineAPI engine = Global.getCombatEngine();
        engine.getFleetManager(target.getOriginalOwner());
        Vector2f pointA = target.getLocation();
        for (ShipAPI curr : engine.getShips()){
            if (curr == null) continue;
            if (curr.isHulk()) continue;
            if (curr.equals(target)) continue;
            if (curr.getFleetCommander() == null) continue;
            if (curr.isFighter()) continue;
            //log.info("  has fleet commander");
            if (!commanders.containsKey(curr.getFleetCommander().getId())) continue;
            //log.info("  has valid fleet commander");
            /*log.info("  has fleetmember");
            if (curr.getFleetMember().getFleetData() == null) continue;
            log.info("  has fleetdata");
            if (curr.getFleetMember().getFleetData().getFleet() == null) continue;
            log.info("  has fleet");
            if (!commanders.containsKey(curr.getFleetMember().getFleetData().getCommander().getFleet())) continue;*/
            //log.info("  has commander");
            Vector2f pointB = curr.getLocation();
            float c = Misc.getDistance(pointA,pointB);
            if (c < distance){
                //log.info("got valid target. setting possible target as: "+curr.getFleetCommander().getId());
                distance = c;
                output.one = commanders.get(curr.getFleetCommander().getId());
                output.two = curr.getOriginalOwner();
            }
        }
        return output;
    }
    /*public ShipAPI getPrefuredTarget(ShipAPI target,CombatEngineAPI engine){

    }
    public Pair<Nano_Thief_Stats,ShipAPI> getPreferredReclaimTarget(ShipAPI target, HashMap<CampaignFleetAPI,Nano_Thief_Stats> commanders){
        Pair<Nano_Thief_Stats,ShipAPI> output = new Pair<>();
        Nano_Thief_Stats commander = null;

        //CombatEngineAPI engine = Global.getCombatEngine();
        //Vector2f pointA = target.getLocation();
        commander = getPreferredCommander(target,commanders);
        //if (commander == null) return output;


        if (output.two != null) log.info("reclaim target has been chosen as "+output.two.toString());
        return output;
    }*/

    /*@Override
    public void advance(float amount) {

    }*/
}
