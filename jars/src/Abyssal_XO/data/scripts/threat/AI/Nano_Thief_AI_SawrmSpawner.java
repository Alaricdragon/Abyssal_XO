package Abyssal_XO.data.scripts.threat.AI;

import Abyssal_XO.data.scripts.Utils;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.skills.Nano_Thief_Skill_Base;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_ShipSkills;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_SkillBase;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_Skill_7;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.util.Misc;
import org.apache.log4j.Logger;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;

public class Nano_Thief_AI_SawrmSpawner implements ShipAIPlugin {
    private static final String idOfModifiers = "AbyssalXO_NanoThief_FighterMods";
    private ShipAPI ship;
    private ShipAPI motherShip;
    public Nano_Thief_Stats stats;
    private String wing;
    private CombatEngineAPI engine;
    private final boolean isOffensive;
    public ArrayList<ShipAPI> fighters = new ArrayList<>();

    public static final String IDOfData1 = "$Nano_Thief_AI_SawrmSpawner_data_1";
    public static final String IDOfData2 = "$Nano_Thief_AI_SawrmSpawner_data_2";
    public static final String IDOfData3 = "$Nano_Thief_AI_SawrmSpawner_data_3";
    public static final String IDOfData4 = "$Nano_Thief_AI_SawrmSpawner_data_4";
    public static final String IDOfData5 = "$Nano_Thief_AI_SawrmSpawner_data_5";
    private double timeToReturn;
    private double returnReclaim;
    private int wingSize;
    public float cost_per_fighter;

    private ArrayList<ShipAPI> arrayOfCores;
    public Nano_Thief_AI_SawrmSpawner(ShipAPI ship, ShipAPI motherShip, String wing, Nano_Thief_Stats stats, boolean isOffensive, NanoThief_SkillBase skill){
        //todo: please note that removing a fighter from a wing forces it to return to its carrier. possability of useing a holder instead of this mess is present again, but needs testing
        //      example: for (ShipAPI a : fighters) ship.getLaunchBaysCopy().get(0).getWing().removeMember(a); will return a wing to the carrier
        this.ship = ship;
        this.motherShip = motherShip;
        this.stats = stats;
        this.wing = wing;
        this.isOffensive = isOffensive;
        if (isOffensive){
            timeToReturn = stats.OF_ttl;
            returnReclaim = stats.OF_recyclePerFighter;
            arrayOfCores = stats.getOffinciveFighterCores();
            wingSize = stats.OF_wingSize;
            cost_per_fighter = stats.OF_swarmCost / wingSize;
        }else{
            timeToReturn = stats.DF_ttl;
            returnReclaim = stats.DF_recyclePerFighter;
            arrayOfCores = ((NanoThief_Skill_7)skill).getInterface7().defenders;;
            wingSize = stats.DF_wingSize;
            cost_per_fighter = stats.DF_swarmCost / wingSize;
            float speed = Global.getSettings().getFighterWingSpec(wing).getVariant().getHullSpec().getEngineSpec().getMaxSpeed();
            float acceleration = Global.getSettings().getFighterWingSpec(wing).getVariant().getHullSpec().getEngineSpec().getAcceleration();
            float deceleration = Global.getSettings().getFighterWingSpec(wing).getVariant().getHullSpec().getEngineSpec().getDeceleration();
            speed = Math.max((motherShip.getMaxSpeed()+100) - speed,0);
            acceleration = Math.max((motherShip.getAcceleration()+50) - acceleration,0);
            deceleration = Math.max((motherShip.getDeceleration()+50) - deceleration,0);
            ship.setCustomData(IDOfData3,speed);
            ship.setCustomData(IDOfData4,acceleration);
            ship.setCustomData(IDOfData5,deceleration);
        }
        //log.info("got timeToReturn as: "+timeToReturn);
        //log.info("got return reclaim as: "+returnReclaim);
        engine = Global.getCombatEngine();
        ship.setCustomData(IDOfData2,this);
        //stage0();
        //ship.getMutableStats().fighter
        /*/
        log.info("preparing sawrm spawner for a single wing:");
        log.info("  stats:");
        displayStats("FighterRefitTime: ","       ",ship.getMutableStats().getFighterRefitTimeMult());
        displayStats("Max Bays: ","       ",ship.getMutableStats().getNumFighterBays());
        displayStats("own wing recovery mod: ","      ",ship.getMutableStats().getDynamic().getStat(Stats.OWN_WING_RECOVERY_MOD));
        displayStats("swarm launcher mod: ","      ",ship.getMutableStats().getDynamic().getStat(Stats.SWARM_LAUNCHER_WING_SIZE_MOD));
        /**/

        //ship.getMutableStats().fighter
        /**/
        /*ship.setCurrentCR(100);
        ship.getLaunchBaysCopy().get(0).setFastReplacements(100);
        ship.getLaunchBaysCopy().get(0).setCurrRate(0.3f);*/
        //log.info("getting a new spawner with a wing of: "+wing);
        //log.info("the fighters ID was given as: "+ship.getLaunchBaysCopy().get(0).getWing().getSpec().getId());
    }
    public void returnFighterAsReclaim(){
        NanoThief_ShipSkills skills = stats.getSkills(motherShip);
        if (skills == null){
            log.info("ERROR: failed to return reclaim to target");
            return;
        }
        skills.addReclaim(returnReclaim);
    }
    private void displayStats(String startString,String indents,MutableStat stats){
        log.info(indents+startString+"getting multi mods");
        for (String a : stats.getMultMods().keySet()){
            //ship.getMutableStats().getFighterWingRange().unmodifyMult(a);
            log.info(indents+"      "+a+": "+stats.getMultMods().get(a).getValue());
        }
        log.info(indents+startString+"getting flat mods");
        for (String a : stats.getFlatMods().keySet()){
            //ship.getMutableStats().getFighterWingRange().unmodifyFlat(a);
            log.info(indents+"      "+a+": "+stats.getFlatMods().get(a).getValue());
        }
    }
    @Override
    public void setDoNotFireDelay(float amount) {

    }

    @Override
    public void forceCircumstanceEvaluation() {

    }
    float time;
    private static final float interval = 1;
    protected static Logger log = Global.getLogger(Nano_Thief_Skill_Base.class);

    int stage = 0;
    private int tempStage = 0;
    @Override
    public void advance(float amount) {
        //ship.set
        //float speed = Global.
        move();
        time+=amount;
        //logFighterStatus();
        //if (stage == 0) stage0();
        if (stage >= 1){
            if (removeSelfIfRequired()) return;
        }
        if (time >= interval){
            //log.info("runing advance for fighter spwaner...");
            switch (stage){
                case 0:
                    //log.info("  runing stage 0...");
                    stage0();
                    break;
                case 1:
                    //log.info("  runing stage 1...");
                    stage1(time);
                    retargetPredictably();
                    break;
                case 2:
                    //log.info("  runing stage 2...");
                    stage2();
                    retargetPredictably();
                    break;
            }
            time = 0;
            //log.info("  compleat fighter spawner");
        }
        /*if (stage != 0 && ship.getLaunchBaysCopy().get(0).getNumLost() >= stats.OF_wingSize){
        }*/
    }
    private void move(){
        //float angle = VectorUtils.getAngle(ship.getLocation(),motherShip.getLocation());
        //engine.headInDirectionWithoutTurning(ship,angle,motherShip.getMaxSpeed());
        Vector2f loc = motherShip.getLocation();
        if (loc == null) loc = ship.getLocation();
        ship.getLocation().set(loc);
    }
    private void logFighterStatus(){
        log.info("getting swarms stats...");
        FighterLaunchBayAPI bay = ship.getLaunchBaysCopy().get(0);
        log.info("  fast replacements:"+bay.getFastReplacements());
        log.info("  cur rate:"+bay.getCurrRate());
        log.info("  time to replace:"+bay.getTimeUntilNextReplacement());
        //ship.getMutableStats().getDynamic().getStat(Stats.REPLACEMENT_RATE_DECREASE_MULT).modifyMult(idOfModifiers, 0);
        //ship.getMutableStats().getDynamic().getStat(Stats.REPLACEMENT_RATE_INCREASE_MULT).modifyMult(idOfModifiers, 0);
    }
    private void stage0(){
        FighterLaunchBayAPI bay = ship.getLaunchBaysCopy().get(0);
        if (bay == null || bay.getWing() == null) return;
        int count = bay.getNumLost();
        count+=bay.getWing().getWingMembers().size();

        if (count >= wingSize){
            stage = 1;
            ship.setCustomData(IDOfData1,true);
            ship.setPullBackFighters(false);
        }
    }

    private float timeAlive = 0;
    private void stage1(float amount){
        timeAlive += amount;

        if (timeAlive >= timeToReturn){
            stage = 2;
            retarget();
            returnShipsToCarrier();
        }
    }
    private void stage2(){
        returnShipsToCarrier();
    }
    public void addSpawnedFighter(ShipAPI a){
        fighters.add(a);
        //log.info("added fighter...");
        /*fighters.addAll(bay.getWing().getWingMembers());
        for (FighterWingAPI.ReturningFighter a : bay.getWing().getReturning()){
            fighters.add(a.fighter);
        }*/
    }
    private void insureIntergityOfFighters(){
        ArrayList<ShipAPI> removed = new ArrayList<>();
        for (int a = 0; a < fighters.size(); a++){
            ShipAPI b = fighters.get(a);
            if (!b.isAlive() || b.isHulk() || b.isFinishedLanding()){
                if (b.isFinishedLanding()) {
                    //log.info("  returned fighter as reclaim. yay!");
                    returnFighterAsReclaim();
                }
                removed.add(b);
                engine.removeEntity(b);
                ship.getLaunchBaysCopy().get(0).getWing().removeMember(b);
                //log.info("  removing fighter...");
            }
        }
        for (ShipAPI a : removed) fighters.remove(a);
    }
    private boolean removeSelfIfRequired(){
        insureIntergityOfFighters();
        //FighterLaunchBayAPI bay = ship.getLaunchBaysCopy().get(0);
        if (fighters.isEmpty()){
            arrayOfCores.remove(ship);//(ship);
            Global.getCombatEngine().removeEntity(ship);
            //log.info("  removing self do to a lack of fighters...");
            return true;
        }
        return false;
    }
    private void returnShipsToCarrier(){
        FighterLaunchBayAPI bay = ship.getLaunchBaysCopy().get(0);
        for (int a = 0; a < bay.getWing().getWingMembers().size(); a++){
            bay.getWing().orderReturn(bay.getWing().getWingMembers().get(a));
        }
    }
    private void retargetPredictably(){
        tempStage++;
        if (tempStage >= 10){
            retarget();
            tempStage = 0;
        }
    }
    private void retarget(){
        if (!isOffensive){
            retargetDef();
            return;
        };
        boolean needNewTarget = !motherShip.isAlive() || motherShip.isHulk();
        float distance = Float.MAX_VALUE;
        if (!needNewTarget) {
            int forceNoTargetChangeRange = 750;
            Vector2f loc2 = motherShip.getLocation();
            for (ShipAPI a : fighters) {
                Vector2f loc = a.getLocation();
                float d = Misc.getDistance(loc,loc2);
                if (d <= forceNoTargetChangeRange){
                    return;
                }
                if (d >= distance){
                    distance = d;
                }
            }
        }
        forceRetarget();
    }
    private void retargetDef(){
        if (!motherShip.isAlive() || motherShip.isHulk()) {
            //log.info("force retargeting...");
            forceRetarget();
            stage = 2;
        }
    }
    private void forceRetarget(){
        float distance = Float.MAX_VALUE;
        float apX = 0;
        float apY = 0;
        for (ShipAPI a : fighters) {
            Vector2f loc = a.getLocation();
            apX += loc.x;
            apY += loc.y;
        }
        apX /= fighters.size();
        apY /= fighters.size();
        ShipAPI newTarget = null;
        stats.makeSureSavedShipsAreAlive();
        for (ShipAPI b : stats.getAvailableShips()){
            Vector2f loc = b.getLocation();
            float d = Utils.getDistance(apX,apY,loc);
            if (d < distance){
                distance = d;
                newTarget = b;
            }
        }
        if (newTarget != null){
            this.motherShip = newTarget;
            return;
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
