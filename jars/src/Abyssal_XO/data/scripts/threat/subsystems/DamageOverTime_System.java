package Abyssal_XO.data.scripts.threat.subsystems;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import org.apache.log4j.Logger;
import org.magiclib.subsystems.MagicSubsystem;

public class DamageOverTime_System extends MagicSubsystem {
    private static Logger log = Global.getLogger(DamageOverTime_System.class);
    public float HPLossPerSecond;
    public float range;
    private static final float interval = 1;
    public DamageOverTime_System(ShipAPI ship, float ttl,float range) {
        super(ship);
        //this.interval = interval;
        HPLossPerSecond = (ship.getHitpoints() / ttl) * interval;
        this.range = range;
        //log.info("ADDING MAGIC LIB SYSTEM");
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
    public void onFinished() {
        //do damage to the ship.
        ship.setHitpoints(ship.getHitpoints() - (HPLossPerSecond));
        // cause the swarm (or what's left of it) to despawn
        if (ship.getHitpoints() <= 0) {
            ship.setSpawnDebris(false);
            Global.getCombatEngine().applyDamage(ship, ship.getLocation(), 100f, DamageType.ENERGY, 0f, true, false, null, false);
        }
    }
    @Override
    public float getBaseActiveDuration() {
        return interval;
    }

    @Override
    public float getBaseCooldownDuration() {
        return 0;
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
