package Abyssal_XO.data.scripts.threat.AI;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_6;
import Abyssal_XO.data.scripts.threat.skills.Nano_Thief_Skill_Base;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import org.apache.log4j.Logger;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
@Deprecated
public class Nano_Thief_AI_OffienviveFighterHolder implements ShipAIPlugin {
    protected static Logger log = Global.getLogger(Nano_Thief_Skill_Base.class);
    private ShipAPI ship;
    private ShipAPI motherShip;
    private Nano_Thief_Stats stats;
    private FighterWingAPI wing;
    private ArrayList<ShipAPI> fighters = new ArrayList<>();
    private CombatEngineAPI engine;
    public Nano_Thief_AI_OffienviveFighterHolder(ShipAPI ship, ShipAPI motherShip, FighterWingAPI wing, Nano_Thief_Stats stats){
        /*todo: fuck this is hard.
        *  ok... ok...
        *  the solution is simple: ask alex if there is a way to stop a ship from rebuilding fighters. if I can do that, I have all the tools to do anything.
        *
        * theory: in the hullmod, I can detect when a fighter is created. make it so that kills all newly created fighters at will.*/
        this.ship = ship;
        this.motherShip = motherShip;
        this.stats = stats;
        this.wing = wing;
        engine = Global.getCombatEngine();
        fighters.addAll(wing.getWingMembers());
        //todo: apply wing data (tags and set to offensive.)
        wing.setSourceShip(ship);
        for (ShipAPI a : wing.getWingMembers()){
            a.addTag("independent_of_carrier");
        }
        wing.setSourceBay(ship.getLaunchBaysCopy().get(0));
        //if (ship.getLaunchBaysCopy().get(0).getWing() != null){
            //log.info("fighter bay is detected... and its not empty!!!!");
            log.info("got type of fighter as: "+ship.getLaunchBaysCopy().get(0).getWing().getSpec().getId());
        //}
    }
    @Override
    public void setDoNotFireDelay(float amount) {

    }

    @Override
    public void forceCircumstanceEvaluation() {

    }
    private float time = 0;
    private boolean returnFighters = false;
    private static final int timeToRetarget = 5;
    @Override
    public void advance(float amount) {
        log.info("advancing: "+time);
        if (removeSelfIfRequired()) return;
        log.info("  pull back fighters...");
        ship.setPullBackFighters(false);
        log.info("  move");
        move();
        log.info("  return fighters if required");
        if (returnFighters) returnShipsToCarrier();
        log.info("  getting timed items...");
        time+=amount;
        if (time >= NanoThief_6.CustomSwarm_TTL){
            log.info("      return fighters active");
            returnFighters = true;
            retarget();
            time = 0;
            return;
        }
        if (((int) time) % timeToRetarget == timeToRetarget - 1){
            log.info("      retarget");
            retarget();
        }
    }
    private boolean removeSelfIfRequired(){
        if (fighters.isEmpty()){
            stats.getOffinciveFighterCores().remove(ship);//(ship);
            Global.getCombatEngine().removeEntity(ship);
            log.info("removing self do to a lack of fighters...");
            return true;
        }
        return false;
    }
    private void move(){
        //float angle = VectorUtils.getAngle(ship.getLocation(),motherShip.getLocation());
        //float speed = Global.
        Vector2f loc = motherShip.getLocation();
        if (loc == null) loc = ship.getLocation();
        ship.getLocation().set(loc);
        //engine.headInDirectionWithoutTurning(ship,angle,motherShip.getMaxSpeed());
    }
    private void returnShipsToCarrier(){
        /*if (wing.getWingMembers().isEmpty()){
            log.info("attempting to return fighters from a empty wing...");
            wing.getWingMembers().addAll(fighters);
        }*/
        for (int a = 0; a < wing.getWingMembers().size(); a++){
            wing.orderReturn(wing.getWingMembers().get(a));
        }
        for (FighterWingAPI.ReturningFighter a : wing.getReturning()){
            a.bay = ship.getLaunchBaysCopy().get(0);
            //wing.orderReturn(a.fighter);
        }
    }
    private void retarget(){
        boolean needNewTarget = false;
        if (!motherShip.isAlive() || motherShip.isHulk()){
            needNewTarget = true;
        }
        float distance = Float.MAX_VALUE;
        float apX = 0;
        float apY = 0;
        if (!needNewTarget) {
            int forceNoTargetChangeRange = 750;
            Vector2f loc2 = motherShip.getLocation();
            for (ShipAPI a : wing.getWingMembers()) {
                Vector2f loc = a.getLocation();
                float d = getDistance(loc,loc2);
                apX += loc.x;
                apY += loc.y;
                if (d <= forceNoTargetChangeRange){
                    return;
                }
                if (d >= distance){
                    distance = d;
                }
            }
            apX /= wing.getWingMembers().size();
            apY /= wing.getWingMembers().size();
        }
        ShipAPI newTarget = null;
        for (ShipAPI b : stats.getAvailableShips()){
            Vector2f loc = b.getLocation();
            float d = getDistance(apX,apY,loc);
            if (d < distance){
                distance = d;
                newTarget = b;
            }
        }
        if (newTarget != null){
            this.motherShip = newTarget;
        }
    }
    private float getDistance(float x, float y, Vector2f loc2){
        return getDistance(new Vector2f(x,y),loc2);
    }
    private float getDistance(Vector2f loc,Vector2f loc2){
        //float angle = VectorUtils.getAngle(loc, loc2);
        float x2 = loc.x + loc2.x;
        x2 = Math.max(x2, -x2);
        float y2 = loc.y + loc2.y;
        y2 = Math.max(y2, -y2);
        float d = x2+y2;
        return d;
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
