package Abyssal_XO.data.scripts.AI;

import Abyssal_XO.data.scripts.Settings;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FleetAssignment;
import com.fs.starfarer.api.campaign.SectorEntityToken;
import com.fs.starfarer.api.util.Misc;

public class ThreatBossController implements EveryFrameScript {
    private CampaignFleetAPI fleet;
    public ThreatBossController(CampaignFleetAPI fleet){
        this.fleet = fleet;
    }
    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean runWhilePaused() {
        return false;
    }

    @Override
    public void advance(float amount) {
        if (fleet.getCurrentAssignment() == null) {
            pickNext();
        }
        displayThings();
    }
    private void displayThings(){
        CampaignFleetAPI player = Global.getSector().getPlayerFleet();
        if (player != null) {
            float distFromPlayer = Misc.getDistance(fleet, player);
            Settings.log.info("(THREAT_BOSS_LOG) got distance from player as: "+distFromPlayer+". assignment is: "+fleet.getCurrentAssignment().getActionText());
        }
    }
    private void pickNext(){
        if (Global.getSector().getPlayerFleet() == null) return;
        if (fleet == null || !fleet.isAlive() || fleet.getStarSystem() == null) return;
        fleet.clearAssignments();
        if (!Global.getSector().getPlayerFleet().isInHyperspace() && Global.getSector().getPlayerFleet().getStarSystem().equals(fleet.getStarSystem())){
            float distFromPlayer = Misc.getDistance(fleet, Global.getSector().getPlayerFleet());
           // SectorEntityToken target = system.createToken(0, 0);
            if (distFromPlayer < fleet.getSensorProfile() + 300) {
                Settings.log.info("(THREAT_BOSS_AI) attacking player.");
                fleet.addAssignment(FleetAssignment.INTERCEPT, Global.getSector().getPlayerFleet(), 999, "(A): Investigating");
                return;
            }
            Settings.log.info("(THREAT_BOSS_AI) seeking out player.");
            fleet.addAssignment(FleetAssignment.GO_TO_LOCATION, Global.getSector().getPlayerFleet(), 1, "(B): Investigating");
            return;
        }
        float days = 1f + (float) Math.random();
        SectorEntityToken target = fleet.getStarSystem().createToken(0, 0);
        float distFromTarget = Misc.getDistance(fleet, target);
        if (distFromTarget < 7000f) target = null;

        CampaignFleetAPI player = Global.getSector().getPlayerFleet();
        if (player != null) {
            float distFromPlayer = Misc.getDistance(fleet, player);
            if (distFromPlayer < 4000f) target = null;
        }

        Settings.log.info("(THREAT_BOSS_AI) patrolling system");
        fleet.addAssignment(FleetAssignment.PATROL_SYSTEM, target, days, "(C): cruising");
    }
}
