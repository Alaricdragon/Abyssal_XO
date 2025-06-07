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
    private ShipAPI ship;
    private float time = 0;
    private float cost;
    private static Logger log = Global.getLogger(NanoThief_ShipStats.class);
    public NanoThief_ShipStats(ShipAPI ship,Nano_Thief_Stats stats){
        this.ship = ship;
        this.stats = stats;
        this.rpc = stats.getGetReclaimPerControl();
        cost = stats.getModifiedCost(ship);
    }
    public void addReclaim(float amount){
        log.info("increasing reclaim by: "+amount);
        reclaim+=amount;
    }
    @Override
    public void advance(float amount) {
        attemptToDisplayStats();
        time+=amount;
        if (time < 1) return;
        time = 0;
        for (int a = swarms.size()-1; a >= 0; a--){
            ShipAPI swarm = swarms.get(a);
            if (swarm.isHulk() || !swarm.isAlive()){
                swarms.remove(a);
            }
        }
        int control = controlAmount();
        while (swarms.size() < control && reclaim >= cost){
            swarms.add(stats.createCombatSwarm(ship));
            reclaim-=cost;
        }
    }
    private int controlAmount(){
        return (int)Math.round((reclaim / rpc+0.49));
    }
    private void attemptToDisplayStats(){
        if (!ship.equals(Global.getCombatEngine().getPlayerShip())){
            ship = Global.getCombatEngine().getPlayerShip();
        }
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF+"_1", "graphics/icons/hullsys/temporal_shell.png",
                "Stored Reclaim", (int)reclaim+" reclaim available", false);
        int control = controlAmount();
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF, "graphics/icons/hullsys/temporal_shell.png",
                "Deployed Swarms", swarms.size()+"/"+control, false);
    }
}
