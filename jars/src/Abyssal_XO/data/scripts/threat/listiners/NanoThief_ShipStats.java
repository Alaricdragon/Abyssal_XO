package Abyssal_XO.data.scripts.threat.listiners;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import org.apache.log4j.Logger;

import java.util.ArrayList;

public class NanoThief_ShipStats implements AdvanceableListener {
    private Nano_Thief_Stats stats;
    private float reclaim = 0;
    private ArrayList<ShipAPI> swarms = new ArrayList<>();
    private float rpc;
    //@Getter
    private ShipAPI ship;
    private float time = 0;
    private float cost;
    private float creationTime;
    private float progress = 0;
    private static final float interval = 0.25f;
    private int control;
    private boolean isCentralFabricate=false;
    private static Logger log = Global.getLogger(NanoThief_ShipStats.class);
    public NanoThief_ShipStats(ShipAPI ship,Nano_Thief_Stats stats){
        this.ship = ship;
        this.stats = stats;
        this.rpc = stats.getModifedReclaimPerControl(ship);
        this.cost = stats.getModifiedCost(ship);
        this.creationTime = stats.getModifedProductionTime(ship);
        if (ship.equals(stats.getCentralFab())){
            isCentralFabricate = true;
        }
        //log.info("created swarm controller for ship of "+this.ship.getName());
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
        for (int a = swarms.size()-1; a >= 0; a--){
            ShipAPI swarm = swarms.get(a);
            if (swarm.getWing().getWingMembers().isEmpty()){//swarm.isHulk() || !swarm.isAlive()){
                swarms.remove(a);
            }
        }
        control = controlAmount();
        if (swarms.size() < control && reclaim >= cost){
            if (ship.isPhased()) return;
            if (progress >= creationTime){
                swarms.add(stats.createCombatSwarm(ship));
                reclaim-=cost;
                this.creationTime = stats.getModifedProductionTime(ship);
                //log.info("creating time for next swarm gotten as: "+this.creationTime);
                progress = 0;
            }
        }else{
            progress = 0;
        }
    }
    private int controlAmount(){
        return (int)Math.round((reclaim / rpc+0.49));
    }
    private void attemptToDisplayStats(){
        if (!ship.equals(Global.getCombatEngine().getPlayerShip())) return;
        if (control > swarms.size()) {
            if (isCentralFabricate) {
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_3", "graphics/icons/hullsys/temporal_shell.png",
                        "Central Fabricator", "The center of all Simulacrum Fighter Wing in your fleet", false);
            }
            if (progress >= creationTime) {
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_2", "graphics/icons/hullsys/temporal_shell.png",
                        "Production Status", "Ready to launch fighter wing", false);
            } else {
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_2", "graphics/icons/hullsys/temporal_shell.png",
                        "Production Status", "Fighter wing "+(int)((progress / creationTime) * 100)+"% complete", false);
            }
        }
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF+"_1", "graphics/icons/hullsys/temporal_shell.png",
                "Stored Reclaim", (int)reclaim+" reclaim available", false);
        int control = controlAmount();
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF, "graphics/icons/hullsys/temporal_shell.png",
                "Deployed Swarms", swarms.size()+"/"+control, false);
    }
}
