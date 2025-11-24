package Abyssal_XO.data.scripts.threat.AI;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.skills.Nano_Thief_Skill_Base;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.loading.VariantSource;
import com.fs.starfarer.api.util.Misc;
import org.apache.log4j.Logger;
import org.lazywizard.lazylib.VectorUtils;
import org.lwjgl.util.vector.Vector2f;

public class Nano_Thief_AI_SawrmSpawner implements ShipAIPlugin {
    private static final String idOfModifiers = "AbyssalXO_NanoThief_FighterMods";
    private ShipAPI ship;
    private ShipAPI motherShip;
    private Nano_Thief_Stats stats;
    private String wing;
    private CombatEngineAPI engine;
    private boolean isOffensive;
    public Nano_Thief_AI_SawrmSpawner(ShipAPI ship,ShipAPI motherShip, String wing, Nano_Thief_Stats stats,boolean isOffensive){
        /*todo:
                so, here is the new idea:
                1) spawn the fighters
                    -fighter spawner will move to ship that created it at normal speed
                2) once fighters spawned, create a new craft without a fighter bay (no replacements)
                    -fighter holder will move to the ship its targeting at distance speed (instant teleportation)
                    -will retarget target every 10 seconds, or when it orders fighters to return.
                3) the new craft will count to its timer, then order the fighters to return with bay.getWing().orderReturn(bay.getWing().getWingMembers().get(a));.
                4) the new craft will despawn, and leave the list when it runs out of fighters to command.

        */

        /*todo: 5 requirements here
                1) make it so this only spawns one wings of swarms, and no more. NONE. ever. I don't fucking care no no no!.
                    -in theory, this can be done by copying reserve deployments effects.
                2) make it so this can set the swarms to attack, the moment they are all deployed.
                    -and make it so they never fucking stop
                3) make it so after the TTL is gone, the sawrms return to the carrier
                    -in theory, stage 1 can handle this maybe?
                4) make it so when this ship runs out of fighters it dies.
                    -simple I hope
                5) make it so this ship TPs to the closest ship to the fighters every 10 seconds
                    -I hate this one.


         */
        this.ship = ship;
        this.motherShip = motherShip;
        this.stats = stats;
        this.wing = wing;
        this.isOffensive = isOffensive;
        engine = Global.getCombatEngine();
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
    @Override
    public void advance(float amount) {
        log.info("runing advance for fighter spwaner...");
        //ship.set
        //float speed = Global.
        move();
        time+=amount;
        //logFighterStatus();
        //if (stage == 0) stage0();
        if (time >= interval){
            time = 0;
            switch (stage){
                case 0:
                    log.info("  runing stage 1...");
                    stage0();
                    break;
                case 1:
                    log.info("  runing stage 2...");
                    stage1();
                    break;
            }
        }
        if (stage != 0 && ship.getLaunchBaysCopy().get(0).getNumLost() >= stats.OF_wingSize){
            //stage3();
            //despawn core when all ships are dead =(
        }
        log.info("  compleat fighter spawner");
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
    private void stage1(){
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
    float timeAlive = 0;
    private void stage2(float amount){
        //timeAlive += amount;
        if (timeAlive >= stats.OF_ttl){
            stage = 2;
        }
        //keep attacking and relocating
    }
    public void stage3(){
        //return to carrier.
        //afterwords, set stage to 3.
    }
    private void stage4(){
        stats.getOffinciveFighterCores().remove(ship);
        Global.getCombatEngine().removeEntity(ship);
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
