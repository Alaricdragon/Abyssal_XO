package Abyssal_XO.data.scripts.threat.listiners;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatFleetManagerAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.loading.VariantSource;
import com.fs.starfarer.combat.entities.Ship;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;

public class NanoThief_ShipStats implements AdvanceableListener {
    @Getter
    private Nano_Thief_Stats stats;
    @Getter
    private float reclaim = 0;
    private ArrayList<ShipAPI> swarms = new ArrayList<>();
    private float rpc;
    @Getter
    private ShipAPI ship;
    private float time = 0;
    private float cost;
    private float creationTime;
    private float progress = 0;
    private static final float interval = 0.25f;
    private int control;
    private int maxStorge;
    private int stored = 0;
    private boolean isCentralFabricate=false;
    private static Logger log = Global.getLogger(NanoThief_ShipStats.class);
    public NanoThief_ShipStats(ShipAPI ship,Nano_Thief_Stats stats){
        this.ship = ship;
        this.stats = stats;
        if (ship.equals(stats.getCentralFab())){
            isCentralFabricate = true;
        }
        this.rpc = stats.getModifedReclaimPerControl(ship);
        this.cost = stats.getModifiedCost(ship);
        this.creationTime = stats.getModifedProductionTime(ship);
        this.maxStorge = stats.getModifedStoredSwarms(ship);
    }
    public void addReclaim(float amount){
        //log.info("increasing reclaim by: "+amount);
        reclaim+=amount;
    }
    @Override
    public void advance(float amount) {
        attemptToDisplayStats();
        time+=amount;
        if (time <= interval) return;
        if (ship.isHulk() || !ship.isAlive()){
            ship.getListenerManager().removeListener(this);
        }
        if (ship.getFluxTracker().isOverloadedOrVenting()) {
            time = 0;
            return;
        }
        progress+=time;
        time = 0;
        //log.info("running swarm controler for ship of: "+ship.getName());
        boolean isWing =swarms.size() != 0 && swarms.get(0).getWing() != null;
        for (int a = swarms.size()-1; a >= 0; a--){
            ShipAPI swarm = swarms.get(a);
            if (!isWing){
                if (!swarm.isAlive()){
                    swarms.remove(a);
                }
                break;
            }
            if (swarm.getWing().getWingMembers().isEmpty()){//swarm.isHulk() || !swarm.isAlive()){
                swarms.remove(a);
            }
        }
        control = controlAmount();
        if (swarms.size() < control && stored != 0 && !ship.isPhased()){
            stored--;
            processReclaimCore();
        }
        if (reclaim >= cost){
            if (swarms.size() < control && !ship.isPhased()) {
                if (progress >= creationTime) {
                    processReclaimCore();
                    reclaim -= cost;
                    this.creationTime = stats.getModifedProductionTime(ship);
                    this.maxStorge = stats.getModifedStoredSwarms(ship);
                    //log.info("creating time for next swarm gotten as: "+this.creationTime);
                    progress = 0;
                }
                return;
            }
            if (stored < maxStorge) {
                //if (ship.isPhased()) return;
                if (progress >= creationTime) {
                    this.stored++;
                    reclaim -= cost;
                    this.creationTime = stats.getModifedProductionTime(ship);
                    this.maxStorge = stats.getModifedStoredSwarms(ship);
                    progress = 0;
                }
                return;
            }
        }else{
            progress = 0;
        }
    }

    private void processReclaimCore(){
        ShipAPI ship = stats.createCombatSwarmCore(this);
        swarms.add(ship.getLaunchBaysCopy().get(0).getWing().getLeader());
    }
    private int controlAmount(){
        return (int)Math.round((reclaim / rpc+0.49));
    }
    private void attemptToDisplayStats(){
        if (!ship.equals(Global.getCombatEngine().getPlayerShip())) return;
        if (control > swarms.size() || maxStorge > stored) {
            if (progress >= creationTime) {
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_2", "graphics/icons/hullsys/temporal_shell.png",
                        "Production Status", "Ready to launch Simulacrum Fighter Wing", false);
            } else {
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_2", "graphics/icons/hullsys/temporal_shell.png",
                        "Production Status", "Simulacrum Fighter Wing "+(int)((progress / creationTime) * 100)+"% complete", false);
            }
        }
        if (isCentralFabricate) {
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_3", "graphics/icons/hullsys/temporal_shell.png",
                    "Central Fabricator", "The center of all Simulacrum Fighter Wing production in your fleet", false);
        }
        /*String display = "";
        display = "Stored Reclaim:"+ (int)reclaim+" reclaim available";
        display += "\n Deployed Simulacrum Fighter Wings: "+swarms.size()+"/"+controlAmount();

        String buildStatus = "";
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF+"_0","graphics/icons/hullsys/temporal_shell.png",
                "Simulacrum Fighter Wings status",display,false);*/


        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF+"_1", "graphics/icons/hullsys/temporal_shell.png",
                "Stored Reclaim", (int)reclaim+" reclaim available", false);
        int control = controlAmount();
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF, "graphics/icons/hullsys/temporal_shell.png",
                "Deployed Simulacrum Fighter Wing", swarms.size()+"/"+control, false);
        if (stored == 0) return;
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF+"_4", "graphics/icons/hullsys/temporal_shell.png",
                "Prepared Simulacrum Fighter Wing", stored+"/"+maxStorge, false);

    }


    /*
    private void addMoreStrangeThing(){
        //ShipVariantAPI OVERWRITER = reclaimCore.getVariant();//.clone();//Global.getSettings().getVariant("Abyssal_XO_ReclaimCore_Blank").clone();
        //OVERWRITER.setSource(VariantSource.REFIT);
        //OVERWRITER.setWingId(tmepSizeThings,stats.getFighterToBuild());
        tmepSizeThings+=3;
        reclaimCore.getLaunchBaysCopy().get(0).setExtraDeployments(tmepSizeThings);
        reclaimCore.getLaunchBaysCopy().get(0).setFastReplacements(tmepSizeThings);
        reclaimCore.getLaunchBaysCopy().get(0).setExtraDeploymentLimit(tmepSizeThings);
        reclaimCore.getLaunchBaysCopy().get(0).setExtraDuration(999999999f);

        //reclaimCore.getLaunchBaysCopy().get(tmepSizeThings).getWing();
        tmepSizeThings++;
        //reclaimCore.getFleetMember().setVariant(OVERWRITER,false,true);
    }
    private ShipAPI reclaimCore;
    int tmepSizeThings = 3;
    private void spawnStrangeThingWtihWings(){
        CombatFleetManagerAPI manager = Global.getCombatEngine().getFleetManager(ship.getOriginalOwner());
        manager.setSuppressDeploymentMessages(true);
        Vector2f loc = ship.getLocation();
        float facing = (float) Math.random() * 360f;
        //log.info("attempting to create a attack swarm at "+loc.x+", "+loc.y+" at ship of "+primary.getName()+" who's location is "+primary.getLocation().x+", "+primary.getLocation().y);
        int attempts = 0;
        ShipAPI fighter = null;
        //Global.getSettings().getVariant("");
        ;
        FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP,"Abyssal_XO_ReclaimCore_Blank");
        ShipVariantAPI OVERWRITER = member.getVariant();//Global.getSettings().getVariant("Abyssal_XO_ReclaimCore_Blank").clone();
        OVERWRITER.setSource(VariantSource.REFIT);
        OVERWRITER.setWingId(0,stats.getFighterToBuild());
        //OVERWRITER.getWing(0).addTag("auto_fighter");
        //auto_fighter, no_drop, no_sell, rapid_reform, leader_no_swarm, wingmen_no_swarm, match_leader_facing, attack_at_an_angle, independent_of_carrier, restricted, hide_in_codex
        //OVERWRITER.getWing(0).addTag("leader_no_swarm");
        //OVERWRITER.getWing(0).addTag("wingmen_no_swarm");
        OVERWRITER.getWing(0).addTag("independent_of_carrier");

        //OVERWRITER.getWing(0).addTag("auto_fighter");
        //OVERWRITER.setWingId(1,stats.getFighterToBuild());
        //OVERWRITER.setWingId(2,stats.getFighterToBuild());
        member.setVariant(OVERWRITER,false,true);
        ShipAPI reclaimCore = manager.spawnFleetMember(member,loc, facing, 0f);
        this.reclaimCore = reclaimCore;

        //Global.getSector().getPlayerFleet().getFleetData().addFleetMember();
        manager.setSuppressDeploymentMessages(false);
    }*/
}
