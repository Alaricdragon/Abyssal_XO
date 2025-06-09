package Abyssal_XO.data.scripts.threat.subsystems;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.DamageType;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;
import org.magiclib.subsystems.MagicSubsystem;

public class DamageOverTime_System extends MagicSubsystem {
    public float HPLossPerSecond;
    public DamageOverTime_System(ShipAPI ship, int ttl) {
        super(ship);
        HPLossPerSecond = ship.getHitpoints() / ttl;
    }

    /*@Override
    protected float getRange() {
        ship.getAllWeapons().get(0);
    }*/

    @Override
    public void onFinished() {
        //do damage to the ship.
        ship.setHitpoints(ship.getHitpoints() - (HPLossPerSecond * 5));
        // cause the swarm (or what's left of it) to despawn
        if (ship.getHitpoints() <= 0) {
            ship.setSpawnDebris(false);
            Global.getCombatEngine().applyDamage(ship, ship.getLocation(), 100f, DamageType.ENERGY, 0f, true, false, null, false);
        }
    }
    @Override
    public float getBaseActiveDuration() {
        return 5;
    }

    @Override
    public float getBaseCooldownDuration() {
        return 0;
    }

    @Override
    public boolean shouldActivateAI(float amount) {
        return true;//ship.getAIFlags().hasFlag(ShipwideAIFlags.AIFlags.WING_NEAR_ENEMY);
    }

    @Override
    public String getDisplayText() {
        return "";
    }
}
