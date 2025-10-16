package Abyssal_XO.data.scripts.threat.AI;

import Abyssal_XO.data.scripts.threat.listiners.NanoThief_ShipStats;
import Abyssal_XO.data.scripts.threat.skills.Nano_Thief_SKill_Base;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import org.apache.log4j.Logger;
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
        //log.info("getting a new spawner with a wing of: "+wing);
        //log.info("the fighters ID was given as: "+ship.getLaunchBaysCopy().get(0).getWing().getSpec().getId());
    }
    @Override
    public void setDoNotFireDelay(float amount) {

    }

    @Override
    public void forceCircumstanceEvaluation() {

    }
    float time;
    private static final float interval = 1;
    protected static Logger log = Global.getLogger(Nano_Thief_SKill_Base.class);
    @Override
    public void advance(float amount) {
        //ship.set
        float angle = VectorUtils.getAngle(ship.getLocation(),motherShip.getLocation());
        //float speed = Global.
        engine.headInDirectionWithoutTurning(ship,angle,motherShip.getMaxSpeed());
        time+=amount;
        if (time >= interval){
            time = 0;
            if (/*!ship.equals(stats.getReclaimCore()) && */ship.getLaunchBaysCopy().get(0).getWing() != null && (ship.getLaunchBaysCopy().get(0).getWing().getWingMembers().size()+ship.getLaunchBaysCopy().get(0).getNumLost() >= Global.getSettings().getFighterWingSpec(wing).getNumFighters())){
                /*
                so this... is interesting.
                it would seam that the wing cannot be spawned from a hostile force. wether that is all hostile forces, or just this one is unclear.
                I will do the following tests tomorrow:
                1: test and see if this works with other hostile forces (force this atrubute to be used by everyone.)
                    -if so, ask alex if there is something preventing fighter spawns in hostile forces.
                    -if not, ask alex if I am suppose to use a diffrent way to handle this.
                2: test and run the 'wing does not exist' text with a timer for how long that wing is going to take to spawn.
                    ship.getLaunchBaysCopy().get(0).getTimeUntilNextReplacement();
                    ship.getLaunchBaysCopy().get(0).getFastReplacements();
                    ship.getLaunchBaysCopy().get(0).getExtraDeploymentLimit();
                    ship.getLaunchBaysCopy().get(0).getExtraDeployments();


                once I have this issue fixed: make sure to replace the set wing back to swamers in the settings (and also give that a test)
                also make sure to disable all the logs. it would cause combat lag.

                 */

                log.info("attempting to spawn a new wing of intended ID: "+wing);
                log.info("The wing ended up with a true ID of: "+ship.getLaunchBaysCopy().get(0).getWing().getSpec().getId());
                FighterWingAPI wing = ship.getLaunchBaysCopy().get(0).getWing();
                wing.setSourceShip(ship);
                //wing.setSourceShip(stats.getReclaimCore());
                stats.addWingToList(wing.getLeader());
                stats.removeReclaimCore(ship);
                Global.getCombatEngine().removeEntity(ship);
            }else{
                log.info(ship.getId()+"wing does not exist. continuing loop...");
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
