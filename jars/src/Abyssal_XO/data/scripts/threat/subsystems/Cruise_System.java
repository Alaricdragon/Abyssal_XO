package Abyssal_XO.data.scripts.threat.subsystems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import org.apache.log4j.Logger;
import org.lazywizard.lazylib.MathUtils;
import org.magiclib.subsystems.MagicSubsystem;

public class Cruise_System extends MagicSubsystem {
    private static final String key = "AbyssalXO_Nano_Thief_System_Overcharge";
    private static Logger log = Global.getLogger(DamageOverTime_System.class);
    public float range;
    private float speed;
    //private static final float interval = 1;
    public Cruise_System(ShipAPI ship, float range, float speed) {
        super(ship);
        this.range = range+300;
        this.speed = speed;
        //log.info("adding the overcharged system to a single fighter...");
    }

    /*@Override
    protected float getRange() {
        return this.range;
    }*/
    /*@Override
    protected float getRange() {
        ship.getAllWeapons().get(0);
    }*/

    /*@Override
    public boolean isToggle() {
        return false;
    }*/

    /*@Override
    public float getBaseActiveDuration() {
        return NanoThief_1.getTimeFlowDur();
    }*/

    @Override
    public boolean hasCharges() {
        return false;
    }

    @Override
    public float getBaseActiveDuration() {
        return 5;
    }

    @Override
    public float getBaseCooldownDuration() {
        return 0;
    }

    private static int intival = 5;
    private float time = 0;
    private ShipAPI target=null;
    @Override
    public boolean shouldActivateAI(float amount) {
        //todo: put this on a timer so it does not spam? or is it fine????
        /*ok, so here is the plan:
        * 1) add a timer to this. maybe 3 seconds.
        * 2) when the timer goes off, mark the target. use that as a thing.
        * 3) */
        //ShipwideAIFlags.AIFlags.IN_ATTACK_RUN;
        //ShipwideAIFlags.AIFlags.WING_NEAR_ENEMY
        /*time+=amount;
        if (time >= intival){
            time = 0;*/
        target = ship.getShipTarget();
        //if (target == null) log.info("no target...");
        //log.info("the target is: "+target.getName());
        //}
        return target == null || MathUtils.getDistance(ship,target) >= range;
        //return !ship.getAIFlags().hasFlag(ShipwideAIFlags.AIFlags.WING_NEAR_ENEMY) && !ship.getAIFlags().hasFlag(ShipwideAIFlags.AIFlags.IN_ATTACK_RUN) && !ship.getAIFlags().hasFlag(ShipwideAIFlags.AIFlags.WING_WAS_NEAR_ENEMY) && !ship.getAIFlags().hasFlag(ShipwideAIFlags.AIFlags.WING_SHOULD_GET_SOME_DISTANCE);//ship.getAIFlags().hasFlag(ShipwideAIFlags.AIFlags.RUN_QUICKLY);
        //return (ship.getShipTarget() == null) || MathUtils.getDistance(ship,ship.getShipTarget()) >= range;
    }
    @Override
    public void onActivate() {
        super.onActivate();
        log.info("onActivate...");
        ship.getMutableStats().getMaxSpeed().modifyFlat(key,speed);
        ship.setApplyExtraAlphaToEngines(true);
        //ship.getMutableStats().getTimeMult().modifyFlat(key, NanoThief_1.getTimeFlowMod());
    }
    @Override
    public void onFinished() {
        super.onFinished();
        log.info("onFinished...");
        ship.getMutableStats().getMaxSpeed().unmodifyFlat(key);
        ship.setApplyExtraAlphaToEngines(false);
    }

    @Override
    public String getDisplayText() {
        return "Overcharge";
    }
}
