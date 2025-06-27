package Abyssal_XO.data.scripts.threat.AI;

import Abyssal_XO.data.scripts.threat.listiners.NanoThief_ShipStats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import org.lazywizard.lazylib.VectorUtils;

public class Nano_Thief_AI_SawrmSpawner implements ShipAIPlugin {
    private ShipAPI ship;
    private ShipAPI motherShip;
    private NanoThief_ShipStats stats;
    private String wing;
    private CombatEngineAPI engine;
    public Nano_Thief_AI_SawrmSpawner(ShipAPI ship,ShipAPI motherShip, String wing, NanoThief_ShipStats stats){
        this.ship = ship;
        this.motherShip = motherShip;
        this.stats = stats;
        this.wing = wing;
        engine = Global.getCombatEngine();
    }
    @Override
    public void setDoNotFireDelay(float amount) {

    }

    @Override
    public void forceCircumstanceEvaluation() {

    }
    float time;
    private static final float interval = 1;
    @Override
    public void advance(float amount) {
        //ship.set
        float angle = VectorUtils.getAngle(ship.getLocation(),motherShip.getLocation());
        //float speed = Global.
        engine.headInDirectionWithoutTurning(ship,angle,motherShip.getMaxSpeed());
        time+=amount;
        if (time >= interval){
            time = 0;
            if (/*!ship.equals(stats.getReclaimCore()) && */ship.getLaunchBaysCopy().get(0).getWing() == null || (ship.getLaunchBaysCopy().get(0).getWing().getWingMembers().size()+ship.getLaunchBaysCopy().get(0).getNumLost() >= Global.getSettings().getFighterWingSpec(wing).getNumFighters())){
                FighterWingAPI wing = ship.getLaunchBaysCopy().get(0).getWing();
                wing.setSourceShip(ship);
                //wing.setSourceShip(stats.getReclaimCore());
                stats.addWingToList(wing.getLeader());
                stats.removeReclaimCore(ship);
                Global.getCombatEngine().removeEntity(ship);
            }
        }
    }

    @Override
    public boolean needsRefit() {
        return false;
    }

    @Override
    public ShipwideAIFlags getAIFlags() {
        return new ShipwideAIFlags();
    }

    @Override
    public void cancelCurrentManeuver() {

    }

    @Override
    public ShipAIConfig getConfig() {
        return new ShipAIConfig();
    }
}
