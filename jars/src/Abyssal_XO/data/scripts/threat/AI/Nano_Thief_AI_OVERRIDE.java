package Abyssal_XO.data.scripts.threat.AI;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.FighterWingAPI;
import com.fs.starfarer.api.combat.ShipAPI;
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
    private ShipAPI target;
    HashMap<ShipAPI,Nano_Thief_AI_CustomSwarm> swarmAIAttempt;
    public Nano_Thief_AI_OVERRIDE(ShipAPI ship, Nano_Thief_Stats stats){
        this.wing = ship.getWing();
        //range = wing.getRange() - 500;
        range = wing.getRange()*0.0f;
        wings.add(this);
        swarmAIAttempt = new HashMap<>();
        for (ShipAPI curr : wing.getWingMembers()) {
            swarmAIAttempt.put(curr,new Nano_Thief_AI_CustomSwarm(curr, stats));
        }
        log.info("creating a fighter with a range of: "+range);
    }
    private static float interval = 1;
    private float time=0;
    public void advance(float amount){
        ShipAPI leader = wing.getLeader();
        if (target != null && !target.isHulk() && target.isAlive() && getRange(target,leader) <= range-10000) {
            //Global.getCombatEngine();
            log.info("already has valid target. ignoring advance");
            return;
        }
        if (swarmAIAttempt.containsKey(leader)){
            time+=amount;
            if (time >= interval){
                log.info("  attempting to get closest hostile....");
                time = 0;
                getClosestHostile(range);
            }
            CommandMovement(amount);
            //swarmAIAttempt.get(leader).advance(amount);
        }
    }
    private ShipAPI getClosestHostile(){
        return getClosestHostile(range+2000000);
    }
    private ShipAPI getClosestHostile(float range){
        ShipAPI ship = wing.getLeader();
        int owner = ship.getOwner();
        CombatEngineAPI engine = Global.getCombatEngine();
        target = Misc.findClosestShipEnemyOf(ship,ship.getLocation(), ShipAPI.HullSize.FRIGATE,range,false);
        if (target != null)log.info("      get new target as: "+target.getName());
        return target;
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
        if (time2 >= interval2 || MoveToShip == null || MoveToShip.isHulk() || !MoveToShip.isAlive()){//waypoint==null){
            //createWaypoint();
            getMoveToShip();
        }
        move();

    }
    /*public boolean shouldBeInCombatMode(){
        return targetTemp != null && !targetTemp.isHulk() && targetTemp.isAlive() && targetTemp.getOwner() != wing.getLeader().getOwner();
    }*/
    private void getMoveToShip(){
        MoveToShip = getClosestHostile();
    }
    public void move(){
        if (MoveToShip == null) return;
        CombatEngineAPI engine = Global.getCombatEngine();
        ShipAPI ship = wing.getLeader();
        //float useHeading = Misc.getClosestTurnDirection(ship.getLocation(),waypoint);
        float useHeading = Misc.getClosestTurnDirection(ship.getLocation(),MoveToShip.getLocation());
        engine.headInDirectionWithoutTurning(ship, useHeading, 10000);
        float angle = VectorUtils.getAngle(ship.getLocation(),MoveToShip.getLocation());
        if (MathUtils.getShortestRotation(angle,ship.getFacing()) <= 5f) {
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
