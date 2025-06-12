package Abyssal_XO.data.scripts.threat.subsystems;

import Abyssal_XO.data.scripts.threat.skills.NanoThief_1;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.ShipAPI;
import org.apache.log4j.Logger;
import org.magiclib.subsystems.MagicSubsystem;

public class Overcharge_System extends MagicSubsystem {
    private static final String key = "AbyssalXO_Nano_Thief_System_Overcharge";
    private static Logger log = Global.getLogger(DamageOverTime_System.class);
    public float range;
    //private static final float interval = 1;
    public Overcharge_System(ShipAPI ship,float range) {
        super(ship);
        this.range = range;
        //log.info("adding the overcharged system to a single fighter...");
    }

    @Override
    protected float getRange() {
        return this.range;
    }
    /*@Override
    protected float getRange() {
        ship.getAllWeapons().get(0);
    }*/

    @Override
    public void activate() {
        ship.getMutableStats().getTimeMult().modifyFlat(key, NanoThief_1.getTimeFlowMod());
    }

    @Override
    public void onFinished() {
        ship.getMutableStats().getTimeMult().unmodifyFlat(key);
        //do damage to the ship.
        // cause the swarm (or what's left of it) to despawn
    }
    @Override
    public float getBaseActiveDuration() {
        return NanoThief_1.getTimeFlowDur();
    }

    @Override
    public float getBaseCooldownDuration() {
        return 120;
    }

    @Override
    public boolean targetOnlyEnemies() {
        return true;
    }

    @Override
    public boolean requiresTarget() {
        return true;
    }

    @Override
    public boolean shouldActivateAI(float amount) {
        //log.info("attempting to actavate system....");
        return true;//ship.getAIFlags().hasFlag(ShipwideAIFlags.AIFlags.WING_NEAR_ENEMY);
    }

    @Override
    public void onActivate() {
        //log.info("activated system...");
    }

    @Override
    public String getDisplayText() {
        return "Decay";
    }
}
