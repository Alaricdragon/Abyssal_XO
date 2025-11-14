package Abyssal_XO.data.scripts.threat.AI;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_ShipStats;
import Abyssal_XO.data.scripts.threat.skills.Nano_Thief_Skill_Base;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_Skill_6;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import org.apache.log4j.Logger;
import org.lazywizard.lazylib.VectorUtils;

public class Nano_Thief_AI_SawrmSpawner implements ShipAIPlugin {
    private ShipAPI ship;
    private ShipAPI motherShip;
    private Nano_Thief_Stats stats;
    private String wing;
    private CombatEngineAPI engine;
    public Nano_Thief_AI_SawrmSpawner(ShipAPI ship,ShipAPI motherShip, String wing, Nano_Thief_Stats stats){
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
        engine = Global.getCombatEngine();
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
        //ship.set
        float angle = VectorUtils.getAngle(ship.getLocation(),motherShip.getLocation());
        //float speed = Global.
        engine.headInDirectionWithoutTurning(ship,angle,motherShip.getMaxSpeed());
        time+=amount;
        if (time >= interval){
            time = 0;
            switch (stage){
                case 0:
                    stage0();
                    break;
                case 1:
                    stage1(amount);
                    break;
                case 2:
                    stage2();
                    break;
                case 3:
                    stage2();
                    break;
            }
        }
        if (stage != 0 && ship.getLaunchBaysCopy().get(0).getNumLost() >= stats.OF_wingSize){
            stage2();
            //despawn core when all ships are dead =(
        }
    }
    private void stage0(){
        if (/*!ship.equals(stats.getReclaimCore()) && */ship.getLaunchBaysCopy().get(0).getWing() != null && (ship.getLaunchBaysCopy().get(0).getWing().getWingMembers().size()+ship.getLaunchBaysCopy().get(0).getNumLost() >= stats.OF_wingSize)){
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

                tests compleated:
                    1) I know its NOT limited to threat_old. all none player forces get this issue.
                    2) I know its not limited to the baseWing. the base wing works for me, but not for hostiles.
                    3) I know its not a additional stat being added to my things.

                    4) I know the refit rare is always 0 when it breaks (normaly it is 33ish)
                    5) I know the 'Fast Replacements' is 0 when it breaks (normaly its at least one)

                    at this ponit, my only theory is that it has something to do with the command I am useing to spawn in the wing. I will need additional data.
                    idea:
                    try changing out the fleet spawning logic to use something like 'engine.getFleetManager(23).spawnShipOrWing("",null,0f,0f);'. maybe this will help? who knows!
                 */

            //log.info("attempting to spawn a new wing of intended ID: "+wing);
            //log.info("The wing ended up with a true ID of: "+ship.getLaunchBaysCopy().get(0).getWing().getSpec().getId());
            FighterWingAPI wing = ship.getLaunchBaysCopy().get(0).getWing();
            wing.setSourceShip(ship);

            //wing.setSourceShip(stats.getReclaimCore());
            //stats.addWingToList(wing.getLeader());
            //stats.removeReclaimCore(ship);

            stage = 1;
        }else{
        }
        //spwan ships
    }
    float timeAlive = 0;
    private void stage1(float amount){
        timeAlive += amount;
        if (timeAlive >= stats.OF_ttl){
            stage = 2;
        }
        //keep attacking and relocating
    }
    public void stage2(){
        //return to carrier.
        //afterwords, set stage to 3.
    }
    private void stage3(){
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
