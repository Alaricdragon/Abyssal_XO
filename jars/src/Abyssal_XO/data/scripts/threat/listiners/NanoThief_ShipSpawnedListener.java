package Abyssal_XO.data.scripts.threat.listiners;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.hullmods.SICSkillControllerBackup;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DeployedFleetMemberAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.FleetMemberDeploymentListener;
import second_in_command.SCData;
import second_in_command.SCUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static Abyssal_XO.data.scripts.Settings.NANO_THIEF_SIC_HULLMOD_DATA_KEY;
import static com.fs.starfarer.api.impl.combat.threat.ThreatShipConstructionScript.SHIP_UNDER_CONSTRUCTION;

public class NanoThief_ShipSpawnedListener implements FleetMemberDeploymentListener {
    public static boolean forceAllowAllShipsToConvert = false;
    public static boolean allowShipModulesToHaveSiC_Backup = false;
    public NanoThief_ShipSpawnedListener(){
        HashMap<Integer,SCData> map = new HashMap<>();
        CombatEngineAPI engine = Global.getCombatEngine();
        //this loop is here because this plugin is added after the first ship is created. basicly gets all 'on spawned' ships and adds them to relevant listeners.
        for (ShipAPI a : engine.getShips()) {
            if (!isShip(a)) continue;
            if (alreadyReady(a)){
                //log?.info("     HERE: got already has hullmod")
                SCData data = getSCData(a);
                if (data == null) continue;
                map.put(a.getOriginalOwner(),data);
                addModules(a,data);
                //log?.info("     HERE: finished already has hullmod")
            }
        }
        Global.getCombatEngine().getCustomData().put("SiC_SCDataMap",map);
    }
    //todo: THIS IS NOT TESTED. PLEASE RUN TESTS TO MAKE SURE FULL FUNCTIONALITY.
    @Override
    public void reportFleetMemberDeployed(DeployedFleetMemberAPI member) {
        ShipAPI a = member.getShip();
        //this is because tags are not added before the ship is fucking spawned.
        Global.getCombatEngine().addPlugin(new ShipSpawnedDelayLisinter(a));
    }
    public static SCData getSCData(ShipAPI shipAPI){
        if (shipAPI.getCustomData().containsKey(NANO_THIEF_SIC_HULLMOD_DATA_KEY)){
            //Settings.log.info("got saved data for ship of: "+shipAPI.getId());
            return (SCData) shipAPI.getCustomData().get(NANO_THIEF_SIC_HULLMOD_DATA_KEY);
        }
        var member = shipAPI.getFleetMember();
        if (member == null) member = shipAPI.getMutableStats().getFleetMember();
        var fleet = member.getFleetData().getFleet();
        if (member != null && fleet != null) {
            if (!fleet.isPlayerFleet() && Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy().contains(member)) {
                //Fix for battles where you join an ally, as those set the members fleet to theirs.
                fleet = Global.getSector().getPlayerFleet();
            }
            if (fleet != null && fleet.getFleetData() != null) return SCUtils.getFleetData(fleet);
        }
        return null;
    }
    public static boolean isValidShipToConvert(ShipAPI a){
        Settings.log.info("got ship to add tags as: "+a.getTags().toString());
        //todo: set this to only effect threat, or to effect all ships on a setting change.
        //if (a.getVariant().hasHullMod(noSkillTagHullmodID)) return false;
        //if (a.getVariant().hasTag(noSkillTagHullmodID)) return false;
        if (a.hasTag(SHIP_UNDER_CONSTRUCTION) || forceAllowAllShipsToConvert) return true;
        return false;
    }
    public static boolean alreadyReady(ShipAPI shipAPI){
        //todo: please note: I removed the memory check, because this uses a diffrent hullmod to compleat my objectives.
        return shipAPI.getVariant().hasHullMod("sc_skill_controller") && shipAPI.getFleetMember().getFleetData() != null;
    }
    public static boolean isShip(ShipAPI shipAPI){
        return !shipAPI.isHulk() && shipAPI.isAlive() && shipAPI.getHullSize() != ShipAPI.HullSize.FIGHTER && !shipAPI.isStationModule() && shipAPI.getParentStation() == null;
    }
    public static void refitShip(ShipAPI shipAPI, SCData data){
        if (data == null || shipAPI == null) return;
        SICSkillControllerBackup.addShipAfterShipSpawns(shipAPI, data);
    }
    public static void addModules(ShipAPI shipAPI, SCData data){
        if (!allowShipModulesToHaveSiC_Backup) return ;
        ArrayList<ShipAPI> childs = new ArrayList<>();
        childs.addAll(shipAPI.getChildModulesCopy());
        var b = 0;
        while (b != childs.size()){
            ShipAPI a = childs.get(b);
            List<ShipAPI> aLinks = a.getChildModulesCopy();
            if (aLinks != null) {
                for (ShipAPI c : aLinks){
                    if (childs.contains(c)) continue;
                    childs.add(c);
                }
            }
            if (a != null && !alreadyReady(a) && isValidShipToConvert(a)) refitShip(a,data);
            b++;
        }
    }
}
