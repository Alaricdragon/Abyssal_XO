package Abyssal_XO.data.scripts.threat.listiners;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.hullmods.SICSkillControllerBackup;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.DeployedFleetMemberAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.FleetMemberDeploymentListener;
import second_in_command.SCData;
import second_in_command.SCUtils;

import static Abyssal_XO.data.scripts.Settings.NANO_THIEF_SIC_HULLMOD_DATA_KEY;
import static com.fs.starfarer.api.impl.combat.threat.ThreatShipConstructionScript.SHIP_UNDER_CONSTRUCTION;

public class NanoThief_ShipSpawnedListienr implements FleetMemberDeploymentListener {
    //todo: THIS IS NOT TESTED. PLEASE RUN TESTS TO MAKE SURE FULL FUNCTIONALITY.
    @Override
    public void reportFleetMemberDeployed(DeployedFleetMemberAPI member) {
        CombatEngineAPI engine = Global.getCombatEngine();
        ShipAPI a = member.getShip();
        if (a == null) return;
        if (a.isHulk()) return;
        if (a.getHullSize() == ShipAPI.HullSize.FIGHTER) return;
        if (a.hasTag(SHIP_UNDER_CONSTRUCTION) && !a.getVariant().getHullMods().contains(Settings.SIC_CONTROL_HULLMOD) && !a.getVariant().getHullMods().contains("sc_skill_controller")) {
            int force = a.getOriginalOwner();
            for (ShipAPI b : engine.getShips()) {
                if (b.getOriginalOwner() != force) continue;
                if (b.getFleetMember() != null && b.getFleetMember().getFleetData() != null && b.getFleetMember().getFleetData().getFleet() != null) {
                    SCData data=getSCData(b);
                    if (data == null) continue;
                    refitShip(a,data);
                    break;
                }
            }
        }
    }
    private SCData getSCData(ShipAPI shipAPI){
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
    private void refitShip(ShipAPI shipAPI, SCData data){
        SICSkillControllerBackup.addShipAfterShipSpawns(shipAPI, data);
    }
}
