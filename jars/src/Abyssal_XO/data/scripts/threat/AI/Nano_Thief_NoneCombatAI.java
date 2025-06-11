package Abyssal_XO.data.scripts.threat.AI;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import org.apache.log4j.Logger;
import org.lwjgl.util.vector.Vector2f;

import java.util.HashMap;
import java.util.Random;

public class Nano_Thief_NoneCombatAI implements ShipAIPlugin {
    private static Logger log = Global.getLogger(Nano_Thief_Stats.class);
    public static void init(){
        targetMap = new HashMap<>();
        timeMap = new HashMap<>();
        //lastcheckedTime = new HashMap<>();

    }
    public static ShipAPI target;
    private static float interval = 20;
    private static float intervalForNoneCombat = 1;
    protected static HashMap<Integer,ShipAPI> targetMap = new HashMap<>();
    protected static HashMap<Integer,Float> timeMap = new HashMap<>();
    //private static HashMap<Integer,Float> lastcheckedTime = new HashMap<>();
    private static ShipAPI getSharedTarget(float amount,ShipAPI ship){
        float time = Global.getCombatEngine().getTotalElapsedTime(false);
        int owner = ship.getOwner();
        if (!targetMap.containsKey(owner)){
            targetMap.put(owner,null);
            timeMap.put(owner,-100f);
        }
        if (timeMap.get(owner)+interval <= time || targetMap.get(owner) == null || (timeMap.get(owner)+intervalForNoneCombat <= time && target.getOwner() == owner)){
            timeMap.put(owner,time);
            ShipAPI target = targetMap.get(owner);
            CombatEngineAPI engine = Global.getCombatEngine();
            Global.getCombatEngine().isAwareOf(owner,target);
            ShipAPI escort;
            WeightedRandomPicker<ShipAPI> pickerH = new WeightedRandomPicker<>();
            WeightedRandomPicker<ShipAPI> pickerF = new WeightedRandomPicker<>();
            for (ShipAPI curr : engine.getShips()) {
                if (curr == ship) continue;
                if (curr.isFighter()) continue;
                if (curr.isHulk() || curr.getOwner() == 100) continue;

                if (curr.getOwner() != owner && engine.isAwareOf(owner, curr)) {
                    pickerH.add(curr,getShipWeight(curr,true));
                }
                if (curr.getOwner() == owner){
                    pickerF.add(curr,getShipWeight(curr,true));
                }
            }
            if (pickerH.isEmpty()){
                ShipAPI target2 = pickerH.pick();
                targetMap.put(owner,target2);
                return target2;
            }else{
                ShipAPI target2 = pickerF.pick();
                targetMap.put(owner,target2);
                return target2;
            }
        }else{
            return targetMap.get(owner);
        }
    }
    public static float getShipWeight(ShipAPI ship) {
        return getShipWeight(ship, true);
    }
    public static float getShipWeight(ShipAPI ship, boolean adjustForNonCombat) {
        boolean nonCombat = ship.isNonCombat(false);
        float weight = 0;
        switch (ship.getHullSize()) {
            case CAPITAL_SHIP: weight += 8; break;
            case CRUISER: weight += 4; break;
            case DESTROYER: weight += 2; break;
            case FRIGATE: weight += 0.1f; break;
            case FIGHTER: weight += 0.01f; break;
        }
        if (nonCombat && adjustForNonCombat) weight *= 0.25f;
        if (ship.isDrone()) weight *= 0.1f;
        return weight;
    }
    ShipAPI ship;
    private Vector2f waypoint=null;
    private float time=0;
    private float wayponitInterval = 1;
    ShipwideAIFlags flags;
    ShipAIConfig config;
    public Nano_Thief_NoneCombatAI(ShipAPI ship){
        this.ship = ship;
        ShipAIPlugin combatAI = ship.getShipAI();
        this.flags = combatAI.getAIFlags();
        this.config = combatAI.getConfig();

    }
    public Nano_Thief_NoneCombatAI(ShipAPI ship,ShipwideAIFlags flags, ShipAIConfig config){
       this.ship = ship;
       this.flags = flags;
       this.config = config;
    }
    @Override
    public void setDoNotFireDelay(float amount) {

    }

    @Override
    public void forceCircumstanceEvaluation() {

    }

    @Override
    public void advance(float amount) {
        /*if (ship.isWingLeader()){
            CommandMovement(amount);
        }*/
    }
    public void CommandMovement(float amount){
        getSharedTarget(amount,ship);
        time+=amount;
        if (time >= interval || waypoint==null){
            createWaypoint();
        }
        move();

    }
    public void move(){
        if (waypoint == null) return;
        CombatEngineAPI engine = Global.getCombatEngine();
        float useHeading = Misc.getClosestTurnDirection(ship.getLocation(),waypoint);
        engine.headInDirectionWithoutTurning(ship, useHeading, 10000);
        Misc.turnTowardsFacingV2(ship, useHeading, 0f);

    }
    private static Random ran = new Random();
    public void createWaypoint(){
        ShipAPI target = getSharedTarget(0,ship);
        if (target == null) return;
        target.getLocation();
        float angle = (float) (ran.nextFloat()*2*Math.PI);
        target.getLocation();
        int rag = 500;
        waypoint = new Vector2f();
        waypoint.x = (float) (target.getLocation().x+ (rag * Math.cos(angle)));
        waypoint.y = (float) (target.getLocation().y+(rag * Math.sin(angle)));
        log.info("got target: "+target.getLocation().x+", "+target.getLocation().y+", wayponit: "+waypoint.x+", "+waypoint.y);
    }
    @Override
    public boolean needsRefit() {
        return false;
    }

    @Override
    public ShipwideAIFlags getAIFlags() {
        return flags;
    }

    @Override
    public void cancelCurrentManeuver() {

    }

    @Override
    public ShipAIConfig getConfig() {
        return config;
    }
}
