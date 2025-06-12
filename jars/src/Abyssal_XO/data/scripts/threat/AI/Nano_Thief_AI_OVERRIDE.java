package Abyssal_XO.data.scripts.threat.AI;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.FighterWingAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.mission.FleetSide;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import com.fs.starfarer.combat.entities.Ship;
import org.apache.log4j.Logger;
import org.lazywizard.lazylib.MathUtils;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Nano_Thief_AI_OVERRIDE {
    private static Logger log = Global.getLogger(Nano_Thief_AI_OVERRIDE.class);
    private static ArrayList<Nano_Thief_AI_OVERRIDE> wings;
    public static void reset(){
        wings = new ArrayList<>();
    }
    public static void advanceAll(float amount){

        for (Nano_Thief_AI_OVERRIDE wing : wings){
            wing.advance(amount);
        }
    }


    FighterWingAPI wing;
    private float range;
    //private ShipAPI target;
    HashMap<ShipAPI,Nano_Thief_AI_CustomSwarm> swarmAIAttempt;
    public Nano_Thief_AI_OVERRIDE(ShipAPI ship, Nano_Thief_Stats stats){
        /*so, I have naerly got this fucking working. at last.
        * what is happening:
        * if I set the fighters source ship to null, they will basicly ram into the enamy ships, instead of normal attack runs.
        * if I set the fighters source to themselves, they break.
        * if I set the fighters source to there to the ship that spawned them, they will not have infinite engagment range.
        * solution: when range from source ship - target is >= to engagement range:
        * get closest hostile to target, and set that ship to the source ship.
        * therefore, 'jumping' my engagement range between targets.
        *
        * */

        this.wing = ship.getWing();
        //ship.getWing().getSpec();
        //Global.getCombatEngine().getFleetManager(0).ship
        //range = wing.getRange() - 500;
        //range = wing.getSpec().getAttackRunRange()*0.9f;
        range = wing.getRange();
        //ship.setPullBackFighters(false);
        wings.add(this);
        swarmAIAttempt = new HashMap<>();
        //wing.setSourceShip(null);
        //wing.set
        //wing.
        //ship.getWing().getSpec().addTag("independent_of_carrier");
        for (ShipAPI curr : wing.getWingMembers()) {
            //curr.addTag("independent_of_carrier");
            //curr.getAIFlags().setFlag(ShipwideAIFlags.AIFlags.);
            //curr.getMutableStats().getFighterWingRange().modifyFlat("Abyssal_XO_Range",200000);
            swarmAIAttempt.put(curr,new Nano_Thief_AI_CustomSwarm(curr, stats));
        }
        log.info("creating a fighter with a range of: "+range);
    }
    private static float interval = 1;
    private float time=0;
    public void advance(float amount){
        ShipAPI leader = wing.getLeader();
        //log.info("is pull back fighters?: "+leader.isPullBackFighters());
        //leader.setPullBackFighters(false);
        if (MoveToShip != null && !MoveToShip.isHulk() && MoveToShip.isAlive() && MathUtils.getDistance(leader,MoveToShip) <= range) {
            //wing.setSourceShip(leader);
            //Global.getCombatEngine();
            //wing.setSourceShip(MoveToShip); is very interesting. it almost worked. like, almost. I think it made the fighter target based on what the target was targeting. so they swarmed around me.
            //wing.setSourceShip(MoveToShip);
            //if (leader.getShipTarget().)
            //if (leader.getShipTarget() != null)log.info("get leader target: "+leader.getShipTarget().getName());
            //leader.setShipTarget(MoveToShip);
            //log.info("already has valid target. ignoring advance");
            return;
        }
        if (swarmAIAttempt.containsKey(leader)){
            //log.info("preforming COMMAND MOVEMENT!");
            /*time+=amount;
            if (time >= interval){
                log.info("  attempting to get closest hostile....");
                time = 0;
                getClosestHostile(range);
            }*/
            CommandMovement(amount);
            //swarmAIAttempt.get(leader).advance(amount);
        }
    }
    private ShipAPI getClosestHostile(){
        return getClosestHostile(range+2000000);
    }
    private ShipAPI getClosestHostile(float range){
        ShipAPI ship = wing.getLeader();
        //int owner = ship.getOwner();
        //CombatEngineAPI engine = Global.getCombatEngine();
        MoveToShip = Misc.findClosestShipEnemyOf(ship,ship.getLocation(), ShipAPI.HullSize.FRIGATE,range,false);
        //if (MoveToShip != null)log.info("      get new MoveToShip as: "+MoveToShip.getName());
        return MoveToShip;
        /*/
        for (ShipAPI curr : engine.getShips()) {
            log.info("  looking at ship...");
            if (curr == ship) continue;
            log.info("      ship is not me");
            if (curr.isFighter()) continue;
            log.info("      ship is not fighter...");
            if (curr.isHulk() || curr.getOwner() == 100 || !curr.isAlive()) continue;
            log.info("      hostile is alive");
            if (curr.getOwner() == owner) continue;
            log.info("      hostile is not part of our force");
            float range = getRange(curr,ship);
            if (range < this.range){
                target = curr;
                log.info("      get new target as: "+target.getName());
                return target;
            }
        }/**/
    }
    /**/
    float time2=0;
    private static float interval2 = 10;
    //private Vector2f waypoint=null;
    private ShipAPI MoveToShip = null;
    //private ShipAPI targetTemp=null;
    public void CommandMovement(float amount){
        //if (shouldBeInCombatMode()) return;
        time2+=amount;
        if (time2 >= interval2 || MoveToShip == null || MoveToShip.isHulk() || !MoveToShip.isAlive() || MoveToShip.getOwner() == 100){//waypoint==null){
            //createWaypoint();
            getMoveToShip();
        }
        move();

    }
    /*public boolean shouldBeInCombatMode(){
        return targetTemp != null && !targetTemp.isHulk() && targetTemp.isAlive() && targetTemp.getOwner() != wing.getLeader().getOwner();
    }*/
    private void getMoveToShip(){
        //log.info("  attempting to get closest hostile....");
        getClosestHostile();
    }
    public void move(){
        if (MoveToShip == null) return;
        CombatEngineAPI engine = Global.getCombatEngine();
        ShipAPI ship = wing.getLeader();
        //float useHeading = Misc.getClosestTurnDirection(ship.getLocation(),waypoint);
        //float useHeading = Misc.getClosestTurnDirection(ship.getLocation(),MoveToShip.getLocation());
        float angle = VectorUtils.getAngle(ship.getLocation(),MoveToShip.getLocation());
        engine.headInDirectionWithoutTurning(ship, angle, 10000);
        if (MathUtils.getShortestRotation(angle,ship.getFacing()) >= 1f) {
            Misc.turnTowardsFacingV2(ship, angle, 0f);
        }

    }
    private static Random ran = new Random();
    /*public void createWaypoint(){
        ShipAPI ship = wing.getLeader();
        ShipAPI target = getClosestTargetIfRequired(ship);
        if (target == null){
            waypoint = null;
            return;
        }
        target.getLocation();
        float angle = (float) (ran.nextFloat()*2*Math.PI);
        target.getLocation();
        int rag = 500;
        waypoint = new Vector2f();
        waypoint.x = (float) (target.getLocation().x+ (rag * Math.cos(angle)));
        waypoint.y = (float) (target.getLocation().y+(rag * Math.sin(angle)));
        //log.info("got target: "+target.getLocation().x+", "+target.getLocation().y+", wayponit: "+waypoint.x+", "+waypoint.y);
    }
    public ShipAPI getClosestTargetIfRequired(ShipAPI ship){
        if (targetTemp == null || !targetTemp.isAlive() || targetTemp.isHulk() || targetTemp.getOwner() == ship.getOwner()){
            return getClosestHostile();
        }
        return targetTemp;
    }
    private ShipAPI getClosesTarget(){
        ShipAPI ship = wing.getLeader();
        int owner = ship.getOwner();
        CombatEngineAPI engine = Global.getCombatEngine();
        float rangeH= Float.MAX_VALUE;
        float rangeF= Float.MAX_VALUE;
        ShipAPI closestH=null;
        ShipAPI closestF=null;
        for (ShipAPI curr : engine.getShips()) {
            if (curr == ship) continue;
            if (curr.isFighter()) continue;
            if (curr.isHulk() || curr.getOwner() == 100) continue;
            float range = getRange(curr,ship);
            if (curr.getOwner() != owner && engine.isAwareOf(owner, curr) && range < rangeH) {
                rangeH = range;
                closestH = curr;
            }
            if (curr.getOwner() == owner && range < rangeF){
                rangeF = range;
                closestF = curr;
            }
        }
        targetTemp = closestF;
        if (closestH != null){
            targetTemp = closestH;
        }
        return targetTemp;
    }*//**/




    private float getRange(ShipAPI ship,ShipAPI ship2){
        Vector2f pointA = ship2.getLocation();//this.wing.getLeader().getLocation();
        Vector2f pointB = ship.getLocation();
        return Misc.getDistance(pointA,pointB);
    }
}
