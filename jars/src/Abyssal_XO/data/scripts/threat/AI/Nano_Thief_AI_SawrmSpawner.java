package Abyssal_XO.data.scripts.threat.AI;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.skills.Nano_Thief_Skill_Base;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_ShipSkills;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.loading.VariantSource;
import com.fs.starfarer.api.util.Misc;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;

public class Nano_Thief_AI_SawrmSpawner implements ShipAIPlugin {
    private static final String idOfModifiers = "AbyssalXO_NanoThief_FighterMods";
    private ShipAPI ship;
    private ShipAPI motherShip;
    private Nano_Thief_Stats stats;
    private String wing;
    private CombatEngineAPI engine;
    @Getter
    private boolean isOffensive;
    public ArrayList<ShipAPI> fighters = new ArrayList<>();

    public static final String IDOfData1 = "$Nano_Thief_AI_SawrmSpawner_data_1";
    public static final String IDOfData2 = "$Nano_Thief_AI_SawrmSpawner_data_2";
    private double timeToReturn;
    private double returnReclaim;
    public Nano_Thief_AI_SawrmSpawner(ShipAPI ship,ShipAPI motherShip, String wing, Nano_Thief_Stats stats,boolean isOffensive){
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
        }else{
            timeToReturn = stats.DF_ttl;
            returnReclaim = stats.DF_recyclePerFighter;
        }
        log.info("got timeToReturn as: "+timeToReturn);
        log.info("got return reclaim as: "+returnReclaim);
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
            log.info("runing advance for fighter spwaner...");
            switch (stage){
                case 0:
                    log.info("  runing stage 0...");
                    stage0();
                    break;
                case 1:
                    log.info("  runing stage 1...");
                    stage1(time);
                    retargetPredictably();
                    break;
                case 2:
                    log.info("  runing stage 2...");
                    stage2();
                    retargetPredictably();
                    break;
            }
            time = 0;
            log.info("  compleat fighter spawner");
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

        if (count >= stats.OF_wingSize){
            //log.info("got total number of fighters as "+count+". moving to stage 1.");
        //if (count >= bay.getWing().getSpec().getNumFighters()){
            stage = 1;
            ship.setCustomData(IDOfData1,true);
            ship.setPullBackFighters(false);
        }
    }
    private void stage0_old(){
        if (ship.getLaunchBaysCopy().get(0) == null || ship.getLaunchBaysCopy().get(0).getWing() == null) return;
        FighterLaunchBayAPI bay = ship.getLaunchBaysCopy().get(0);
        for (int a = 0; a < bay.getWing().getWingMembers().size(); a++){
            bay.getWing().getWingMembers().remove(a);
            bay.getWing().orderReturn(bay.getWing().getWingMembers().get(a));
            //bay.getWing().getWingMembers().get(a).getAIFlags().hasFlag();
        }
        for (ShipAPI a : ship.getLaunchBaysCopy().get(0).getWing().getWingMembers()){
            engine.removeEntity(a);
        }


        //bay.setExtraDuration(30);



        /*ship.getLaunchBaysCopy().get(0).setFastReplacements(0);
        ship.getLaunchBaysCopy().get(0).setCurrRate(0f);
        ship.getMutableStats().getDynamic().getStat(Stats.REPLACEMENT_RATE_DECREASE_MULT).modifyMult(idOfModifiers, 0);
        ship.getMutableStats().getDynamic().getStat(Stats.REPLACEMENT_RATE_INCREASE_MULT).modifyMult(idOfModifiers, 0);
        ship.getMutableStats().getDynamic().getStat(Stats.FIGHTER_REARM_TIME_EXTRA_FLAT_MOD).modifyFlat(idOfModifiers,10000000);*/
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
    private void stage1_old(){
        log.info("started stage one...");
        ShipAPI primary = ship;//stats.getShip();
        CombatEngineAPI engine = Global.getCombatEngine();
        //CombatFleetManagerAPI manager = engine.getFleetManager(FleetSide.ENEMY);//engine.getFleetManager(primary.getOwner());
        CombatFleetManagerAPI manager = engine.getFleetManager(primary.getOwner());
        manager.setSuppressDeploymentMessages(true);

        Vector2f loc = primary.getLocation();
        float facing = (float) Math.random() * 360f;
        //log.info("attempting to create a attack swarm at "+loc.x+", "+loc.y+" at ship of "+primary.getName()+" who's location is "+primary.getLocation().x+", "+primary.getLocation().y);
        ShipAPI fighter = null;
        //Global.getSettings().getVariant("");


        FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP,"Abyssal_XO_ReclaimCore_Blank");
        //Global.getFactory().createFleetMember
        ShipVariantAPI OVERWRITER = member.getVariant();//Global.getSettings().getVariant("Abyssal_XO_ReclaimCore_Blank").clone();
        OVERWRITER.setSource(VariantSource.REFIT);
        //OVERWRITER.setWingId(0,Settings.NANO_THIEF_BASEWING);//'broadsword_wing' from settings causes strange fragment swarm to spawn??? WTF?
        //OVERWRITER.setWingId(0,Settings.NANO_THIEF_PALYER_BASEWING);//'broadsword_wing' from settings causes strange fragment swarm to spawn??? WTF?
        OVERWRITER.setWingId(0,"abyssal_XO_wasp_wing");
        //OVERWRITER.getWing(0).addTag("independent_of_carrier");
        //OVERWRITER.getWing(0).addTag("auto_fighter");
        //OVERWRITER.setWingId(1,stats.getFighterToBuild());
        //OVERWRITER.setWingId(2,stats.getFighterToBuild());
        member.setOwner(primary.getOwner());
        member.setVariant(OVERWRITER,false,true);

        fighter = manager.spawnFleetMember(member,loc, facing, 0f);
        //engine.ship
        //fighter.setOwner(primary.getOwner());
        //fighter = manager.spawnShipOrWing(member,loc,facing,0f);

        //if (!isAlly) fighter.setAlly(false);
        //log.info("spawning spawner with a wing of: "+this.fighterToBuild);
        //log.info("the fighters ID was given as: "+OVERWRITER.getWing(0).getId());
        //log.info("temp thing: "+fighter.getWing().getSpec());//no wing...? //maybe wing only exsists a short time after creation?
        //log.info("temp thing 2:"+fighter.getLaunchBaysCopy().get(0).getTimeUntilNextReplacement());
        //log.info("got the true ID of the wing as: "+fighter.getLaunchBaysCopy().get(0).getWing().getSpec().getId());
        //fighter.setShipAI(new Nano_Thief_AI_SawrmSpawner(fighter,primary,skills.stats.OF_fighterToBuild,skills.stats,true));
        //note: this is usefull for making the guys follow your primary ship. not yet compleated.
        /*if (stats.getReclaimCore() == null){
            ShipAPI core = manager.spawnShipOrWing("Abyssal_XO_ReclaimCore_Blank",loc, facing, 0f,null);
            core.setShipAI(new Nano_Thief_AI_ReclaimCoreBlank(core,primary));
            //core.setAlphaMult(0);
            stats.setReclaimCore(core);
        }*/
        //fighter.setCustomData(ReclaimCore.IDOfData,this);

        manager.removeDeployed(fighter,false);



        //fighter = manager.spawnShipOrWing(Settings.NANO_THIEF_CREATER_SHIP, loc, facing, 0f, null);
        //fighter.getWing().setSourceShip(primary);//sets to ifself to prevent min ingagment rage from triggering. might remove if i build a custom AI for the ships.
        manager.setSuppressDeploymentMessages(false);
        Vector2f takeoffVel = Misc.getUnitVectorAtDegreeAngle(facing);
        takeoffVel.scale(fighter.getMaxSpeed() * 1f);


        if (isOffensive){
            //OVERWRITER.setWingId(0,"abyssal_XO_wasp_wing");
            log.info("creating offince xiholder...");
            FighterLaunchBayAPI bay = ship.getLaunchBaysCopy().get(0);
            fighter.setShipAI(new Nano_Thief_AI_OffienviveFighterHolder(fighter,motherShip,bay.getWing(),stats));
            log.info("offince holder created...");
        }else{
            FighterLaunchBayAPI bay = ship.getLaunchBaysCopy().get(0);
            fighter.setShipAI(new Nano_Thief_AI_OffienviveFighterHolder(fighter,motherShip,bay.getWing(),stats));
        }
        stats.getOffinciveFighterCores().remove(ship);
        engine.removeEntity(ship);
        stats.getOffinciveFighterCores().add(fighter);
        stage = 2;
    }
    private void stage2(){
        returnShipsToCarrier();
    }
    public void stage3(){
        //return to carrier.
        //afterwords, set stage to 3.
    }
    private void stage4(){
        stats.getOffinciveFighterCores().remove(ship);
        Global.getCombatEngine().removeEntity(ship);
    }

    public void addSpawnedFighter(ShipAPI a){
        fighters.add(a);
        log.info("added fighter...");
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
                    log.info("  returned fighter as reclaim. yay!");
                    returnFighterAsReclaim();
                }
                removed.add(b);
                engine.removeEntity(b);
                ship.getLaunchBaysCopy().get(0).getWing().removeMember(b);
                log.info("  removing fighter...");
            }
        }
        for (ShipAPI a : removed) fighters.remove(a);
    }
    private boolean removeSelfIfRequired(){
        insureIntergityOfFighters();
        //FighterLaunchBayAPI bay = ship.getLaunchBaysCopy().get(0);
        if (fighters.isEmpty()){
            stats.getOffinciveFighterCores().remove(ship);//(ship);
            Global.getCombatEngine().removeEntity(ship);
            log.info("  removing self do to a lack of fighters...");
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
        if (!isOffensive) return;
        tempStage++;
        if (tempStage >= 10){
            retarget();
            tempStage = 0;
        }
    }
    private void retarget(){
        if (!isOffensive) return;
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
            for (ShipAPI a : fighters) {
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
            apX /= fighters.size();
            apY /= fighters.size();
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
