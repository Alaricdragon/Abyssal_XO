package Abyssal_XO.data.scripts.threat.listiners;

import Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_OVERRIDE_Lander;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import org.apache.log4j.Logger;

public class NanoThief_FighterRetreatChecker implements AdvanceableListener {
    /*continue tests on this. I need to know:
    * if fighters that -are not- bombers return the 'post attack run' tag when they are attacking or not. (they do).
    * there seems to be no valid way to fix this... maybe if I were to like... um....
    * ok so:
    * the best possable way --for now-- provided it works:
    * make it so bombers when they run out of ammow get there AI taken over and....
    *   -ISSUE: I now need to deal with systems and all sorts of things. not easy. important.....
    *   ok... ok... so:
    *   how about I do some tests with changing the source ship? if I can set the source ship of each bomber, it might just work. if i am lucky.
    *
    *
    * update:
    *   ship.getWing().setSourceBay(stats.getTargetForReclaim(ship,Global.getCombatEngine()).getLaunchBaysCopy().get(0));
    * that works, provided the bay already has a fighter in it. as far as I am able to tell, it just kinda works maybe?
    * very interesting.
    * If i can find a way to forcefully set locations, this might work? mmmm....
    * ok, so: a theory:
    * 1) create a new wing. this wing will be completely empty of fighters, in effect. (max size of zero)
    * 2) when a wing spawns, do the following:
    *   1) create a reclaim core on the ship with the empty fighter, and +999999 speed.
    *   2) set the source bay to the new reclaim cores bay.
    *   3) add a listener that listings for when the wing lands. hopefully that ends up helping...? (I think a boolean gets set then it lands...?)
    * 3) when the listener determines that a wing lands, do the following:
    *   1) add a certen amount of reclaim to the ship.
    *   2) (maybe latter) add a certen amount of 'fast build' to the ship, effectivly letting the ship build fighter wings faster. maybe have it equal to like, 25% of a wing per wing?
    *  */
    ShipAPI ship;
    Nano_Thief_Stats stats;
    private static Logger log = Global.getLogger(NanoThief_FighterRetreatChecker.class);
    public NanoThief_FighterRetreatChecker(ShipAPI ship, Nano_Thief_Stats stats){
        this.ship = ship;
        this.stats = stats;
    }
    public static final ShipwideAIFlags.AIFlags[] flags = {
            ShipwideAIFlags.AIFlags.CUSTOM1,
            ShipwideAIFlags.AIFlags.CUSTOM2,
            ShipwideAIFlags.AIFlags.CUSTOM3,
            ShipwideAIFlags.AIFlags.CUSTOM4,
            ShipwideAIFlags.AIFlags.CUSTOM5,
            ShipwideAIFlags.AIFlags.RUN_QUICKLY,
            ShipwideAIFlags.AIFlags.POST_ATTACK_RUN,
            ShipwideAIFlags.AIFlags.BACKING_OFF,
            ShipwideAIFlags.AIFlags.DRONE_MOTHERSHIP,
            ShipwideAIFlags.AIFlags.WING_SHOULD_GET_SOME_DISTANCE,

            /*ShipwideAIFlags.AIFlags.MANEUVER_TARGET
            ShipwideAIFlags.AIFlags.CUSTOM1
            ShipwideAIFlags.AIFlags.BACKING_OFF
            ShipwideAIFlags.AIFlags.CUSTOM2
            ShipwideAIFlags.AIFlags.CUSTOM3
            ShipwideAIFlags.AIFlags.CUSTOM4
            ShipwideAIFlags.AIFlags.CUSTOM5
            ShipwideAIFlags.AIFlags.DRONE_MOTHERSHIP
            ShipwideAIFlags.AIFlags.POST_ATTACK_RUN
            ShipwideAIFlags.AIFlags.RUN_QUICKLY
            ShipwideAIFlags.AIFlags.WING_SHOULD_GET_SOME_DISTANCE
            ShipwideAIFlags.AIFlags.AUTO_FIRING_AT_PHASE_SHIP
            ShipwideAIFlags.AIFlags.AVOIDING_BORDER
            ShipwideAIFlags.AIFlags.BACK_OFF
            ShipwideAIFlags.AIFlags.BACK_OFF_MAX_RANGE
            ShipwideAIFlags.AIFlags.BACK_OFF_MIN_RANGE
            ShipwideAIFlags.AIFlags.BIGGEST_THREAT
            ShipwideAIFlags.AIFlags.CAMP_LOCATION
            ShipwideAIFlags.AIFlags.CARRIER_FIGHTER_TARGET
            ShipwideAIFlags.AIFlags.DELAY_STRIKE_FIRE
            ShipwideAIFlags.AIFlags.DO_NOT_AUTOFIRE_NON_ESSENTIAL_GROUPS
            ShipwideAIFlags.AIFlags.DO_NOT_AVOID_BORDER
            ShipwideAIFlags.AIFlags.DO_NOT_BACK_OFF
            ShipwideAIFlags.AIFlags.DO_NOT_BACK_OFF_EVEN_WHILE_VENTING
            ShipwideAIFlags.AIFlags.DO_NOT_PURSUE
            ShipwideAIFlags.AIFlags.DO_NOT_USE_FLUX*/
    };
    private boolean hasLanded = false;
    private boolean setReturnCarrier = false;
    private ShipAPI target = null;
    @Override
    public void advance(float amount) {
        if (target == null || !target.isAlive()) setReturnCarrier = false;
        if (ship.getAIFlags().hasFlag(ShipwideAIFlags.AIFlags.POST_ATTACK_RUN) && !setReturnCarrier){
            setReturnCarrier = true;
            stats.applyDataToReturnFighterToFriendlyShip(ship);
            ship.getWing().setSourceBay(stats.getTargetForReclaim(ship,Global.getCombatEngine()).getLaunchBaysCopy().get(0));
        }
        if (!hasLanded && ship.isLanding() && target != null){
            NanoThief_ShipStats stats1 = stats.getShipStats(target);//(NanoThief_ShipStats) ship.getWing().getSource().getShip().getCustomData().get("");
            stats1.addReclaim(stats.getRecycleCostPerFighter());
            hasLanded = true;
        }
        //determin if the fighter is returning home yet... this will require testing.
        /**/
        log.info("_");
        for (ShipwideAIFlags.AIFlags a : flags){
            if (ship.getAIFlags().hasFlag(a)){
                log.info("has AI flag: "+a.name());
            }
        }
        /*
        //ShipwideAIFlags.AIFlags.MANEUVER_TARGET;
        //ship.getLaunchBaysCopy().get(0).getWing().setSourceBay();
        if (ship.getAIFlags().hasFlag(ShipwideAIFlags.AIFlags.POST_ATTACK_RUN)){
            //ship.getWing().setSourceShip(stats.getTargetForReclaim(ship,Global.getCombatEngine()));
            ship.getWing().setSourceBay(stats.getTargetForReclaim(ship,Global.getCombatEngine()).getLaunchBaysCopy().get(0));

            //ship.beginLandingAnimation(stats.getTargetForReclaim(ship,Global.getCombatEngine()));

            //ship.land
            //log.info("post attack run");
            //new Nano_Thief_AI_OVERRIDE_Lander(ship,stats);
        }
        //ShipwideAIFlags.AIFlags.
        */
    }
}
