package Abyssal_XO.data.scripts.threat.animation;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.combat.RiftLanceEffect;
import com.fs.starfarer.api.impl.combat.threat.RoilingSwarmEffect;
import com.fs.starfarer.api.impl.combat.threat.VoltaicDischargeOnFireEffect;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import org.apache.log4j.Logger;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static Abyssal_XO.data.scripts.Settings.TAG_HASRECLAMED;

public class NanoThief_A_ReclaimSpawn extends BaseEveryFrameCombatPlugin {
    public static float CR_PER_RECLAMATION_SWARM = 0.02f;

    public static float RECLAMATION_SWARM_SPEED_MULT = 0.67f;
    public static float RECLAMATION_SWARM_COLLISION_MULT = 1.5f;
    public static float RECLAMATION_SWARM_RADIUS_MULT = 2f;
    public static float RECLAMATION_SWARM_HP_MULT = 2f;
    public static float RECLAMATION_SWARM_FRAGMENT_SIZE_MULT = 0.67f;



    protected float elapsed = 0f;
    protected ShipAPI primary = null;
    protected List<ShipAPI> pieces = new ArrayList<>();
    protected List<ShipAPI> swarms = new ArrayList<>();
    protected float delay;
    protected float fadeOutTime;
    protected float origMaxSpeed = 500f;

    protected IntervalUtil interval = new IntervalUtil(0.075f, 0.125f);
    protected IntervalUtil interval2 = new IntervalUtil(0.075f, 0.125f);
    protected boolean spawnedSwarms = false;

    protected Nano_Thief_Stats status;
    protected int forceID;
    protected int reclaim;
    protected boolean isShip;
    protected boolean isRefined;
    protected ShipAPI targetOverride;
    protected ShipAPI primaryShip;
    public NanoThief_A_ReclaimSpawn(Pair<Nano_Thief_Stats,Integer> data, ShipAPI ship, float delay,int reclaim,boolean isRefined,boolean isShip,ShipAPI targetOverride) {
        status = data.one;
        forceID = data.two;
        this.reclaim = reclaim;
        this.isShip = isShip;
        this.isRefined = isRefined;
        this.targetOverride = targetOverride;
        this.primaryShip = ship;

        this.delay = delay;

        this.primary = ship;
        for (ShipAPI curr : Global.getCombatEngine().getShips()) {
            if (curr.getFleetMember() == ship.getFleetMember()) {
                pieces.add(curr);
            }
        }

        switch (ship.getHullSize()) {
            case CAPITAL_SHIP:
                this.fadeOutTime = 15f;
                break;
            case CRUISER:
                this.fadeOutTime = 12f;
                break;
            case DESTROYER:
                this.fadeOutTime = 9f;
                break;
            case FRIGATE:
                this.fadeOutTime = 7f;
                break;
            default:
                this.fadeOutTime = 7f;
                break;
        }

        this.fadeOutTime += 3f;

        interval.forceIntervalElapsed();

        for (ShipAPI curr : pieces) {
            curr.addTag(TAG_HASRECLAMED);
        }
    }

    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        if (Global.getCombatEngine().isPaused()) return;

        elapsed += amount;
        if (elapsed < delay) return;


        CombatEngineAPI engine = Global.getCombatEngine();

        float progress = (elapsed - delay) / fadeOutTime;
        if (progress < 0f) progress = 0f;
        if (progress > 1f) progress = 1f;


        float remaining = fadeOutTime - (elapsed - delay);


        if (elapsed > delay + 2f && remaining > 4f) {
            spawnSwarms(amount);
        }

        if (isShip)advanceForShip(amount,progress,engine,remaining);
        else advanceForPieces(amount,progress,engine,remaining);

    }
    private static Logger log = Global.getLogger(Nano_Thief_Stats.class);
    private void advanceForShip(float amount,float progress, CombatEngineAPI engine, float remaining){
        //log.info("advanceForShip");
        boolean first = true;
        boolean anyInEngine = false;
        ShipAPI ship = this.primaryShip;
        if (engine.isInEngine(ship)) anyInEngine = true;

        /*Vector2f vel = ship.getVelocity();
        Vector2f acc = new Vector2f(vel);
        if (acc.length() != 0) {
            acc.normalise();
            acc.scale(-1f);
            acc.scale(amount * ship.getDeceleration());
            Vector2f.add(vel, acc, vel);
            float speed = vel.length();
            if (speed <= 1 || speed < acc.length()) {
                vel.set(0, 0);
            }
        }

        float alpha = 1f;
        if (progress > 0.5f) {
            alpha = (1f - progress) * 2f;
        }

        ship.setAlphaMult(alpha);

        if (first) {
            Global.getSoundPlayer().playLoop("reclamation_loop", ship, 1f, 1f, ship.getLocation(), ship.getVelocity());
            first = false;
        }

        if (false) {
            float jitterLevel = 1f - progress;
            if (fadeOutTime <= 4f) {
                if (jitterLevel < 0.5f) {
                    jitterLevel *= 2f;
                } else {
                    jitterLevel = (1f - jitterLevel) * 2f;
                }
            } else {
                if (jitterLevel < 0.5f) {
                    jitterLevel *= 2f;
                } else if (remaining <= 2f) {
                    jitterLevel = remaining / 2f;
                } else {
                    jitterLevel = 1f;
                }
            }
            jitterLevel = (float) Math.sqrt(jitterLevel);

            //float jitterRange = 1f - progress;
            float jitterRange = 1f;
            if (remaining < 2f) {
                jitterRange = remaining / 2f;
            } else {
                jitterRange = (elapsed - delay) / Math.max(1f, fadeOutTime - 2f);
            }
            float maxRangeBonus = 25f;
            float jitterRangeBonus = jitterRange * maxRangeBonus;

            Color c = VoltaicDischargeOnFireEffect.EMP_FRINGE_COLOR;
            c = Misc.setAlpha(c, 127);

            ship.setJitter(this, c, jitterLevel, 3, 0f, jitterRangeBonus);
        }*/

        //spawnParticles(ship, amount, progress);

        if (elapsed > fadeOutTime + delay || !anyInEngine) {
            //log.info("removing plugin (ship)");
            engine.removePlugin(this);
        }

    }
    private void advanceForPieces(float amount,float progress, CombatEngineAPI engine, float remaining){
        //log.info("advanceForPeaces");
        boolean first = true;
        boolean anyInEngine = false;
        for (ShipAPI ship : pieces) {
            if (!engine.isInEngine(ship)) continue;
            anyInEngine = true;

            Vector2f vel = ship.getVelocity();
            Vector2f acc = new Vector2f(vel);
            if (acc.length() != 0) {
                acc.normalise();
                acc.scale(-1f);
                acc.scale(amount * ship.getDeceleration());
                Vector2f.add(vel, acc, vel);
                float speed = vel.length();
                if (speed <= 1 || speed < acc.length()) {
                    vel.set(0, 0);
                }
            }

            float alpha = 1f;
            if (progress > 0.5f) {
                alpha = (1f - progress) * 2f;
            }

            ship.setAlphaMult(alpha);

            if (first) {
                Global.getSoundPlayer().playLoop("reclamation_loop", ship, 1f, 1f, ship.getLocation(), ship.getVelocity());
                first = false;
            }

            if (false) {
                float jitterLevel = 1f - progress;
                if (fadeOutTime <= 4f) {
                    if (jitterLevel < 0.5f) {
                        jitterLevel *= 2f;
                    } else {
                        jitterLevel = (1f - jitterLevel) * 2f;
                    }
                } else {
                    if (jitterLevel < 0.5f) {
                        jitterLevel *= 2f;
                    } else if (remaining <= 2f) {
                        jitterLevel = remaining / 2f;
                    } else {
                        jitterLevel = 1f;
                    }
                }
                jitterLevel = (float) Math.sqrt(jitterLevel);

                //float jitterRange = 1f - progress;
                float jitterRange = 1f;
                if (remaining < 2f) {
                    jitterRange = remaining / 2f;
                } else {
                    jitterRange = (elapsed - delay) / Math.max(1f, fadeOutTime - 2f);
                }
                float maxRangeBonus = 25f;
                float jitterRangeBonus = jitterRange * maxRangeBonus;

                Color c = VoltaicDischargeOnFireEffect.EMP_FRINGE_COLOR;
                c = Misc.setAlpha(c, 127);

                ship.setJitter(this, c, jitterLevel, 3, 0f, jitterRangeBonus);
            }

            spawnParticles(ship, amount, progress);
        }

        if (elapsed > fadeOutTime + delay || !anyInEngine) {
            for (ShipAPI ship : pieces) {
                engine.removeEntity(ship);
                ship.setAlphaMult(0f);
            }
            engine.removePlugin(this);
            //log.info("removing plugin (peaces)");
        }

    }
    protected void spawnParticles(ShipAPI ship, float amount, float progress) {
        if (ship == null) return;

        float remaining = fadeOutTime - (elapsed - delay);

        interval.advance(amount);
        if (interval.intervalElapsed()) {
            CombatEngineAPI engine = Global.getCombatEngine();

            Color c = RiftLanceEffect.getColorForDarkening(VoltaicDischargeOnFireEffect.EMP_FRINGE_COLOR);
            c = Misc.setAlpha(c, 50);
            float baseDuration = 2f;
            Vector2f vel = new Vector2f(ship.getVelocity());
            //float size = ship.getCollisionRadius() * 0.35f;
            float size = ship.getCollisionRadius() * 0.33f;

            float extraDur = 0f;
            if (remaining < 1f) extraDur = 1f;

            //for (int i = 0; i < 3; i++) {
            for (int i = 0; i < 11; i++) {
                Vector2f point = new Vector2f(ship.getLocation());
                point = Misc.getPointWithinRadiusUniform(point, ship.getCollisionRadius() * 0.75f, Misc.random);
                float dur = baseDuration + baseDuration * (float) Math.random();
                dur += extraDur;
                float nSize = size;
                Vector2f pt = Misc.getPointWithinRadius(point, nSize * 0.5f);
                Vector2f v = Misc.getUnitVectorAtDegreeAngle((float) Math.random() * 360f);
                v.scale(nSize + nSize * (float) Math.random() * 0.5f);
                v.scale(0.2f);
                Vector2f.add(vel, v, v);

                float maxSpeed = nSize * 1.5f * 0.2f;
                float minSpeed = nSize * 1f * 0.2f;
                float overMin = v.length() - minSpeed;
                if (overMin > 0) {
                    float durMult = 1f - overMin / (maxSpeed - minSpeed);
                    if (durMult < 0.1f) durMult = 0.1f;
                    dur *= 0.5f + 0.5f * durMult;
                }
                engine.addNegativeNebulaParticle(pt, v, nSize * 1f, 2f,
                        0.5f / dur, 0f, dur, c);
            }
        }
    }

    private float speedMulti = 2;
    protected void spawnSwarms(float amount) {
        if (!spawnedSwarms) {
            int numSwarms = 1;

			/*for (ConstructionSwarmSystemScript.SwarmConstructableVariant curr : ConstructionSwarmSystemScript.CONSTRUCTABLE) {
				if (curr.variantId.equals(primary.getVariant().getHullVariantId())) {
					numSwarms = (int) Math.round(curr.cr * 100f);
					break;
				}
			}*/

            for (int i = 0; i < numSwarms; i++) {
                ShipAPI curr = launchSwarm();
                if (curr != null) swarms.add(curr);
            }
            spawnedSwarms = true;
        }
        interval2.advance(amount * speedMulti);
        if (interval2.intervalElapsed()) {
            WeightedRandomPicker<ShipAPI> picker = new WeightedRandomPicker<>();
            picker.addAll(pieces);

            for (ShipAPI curr : swarms) {
                RoilingSwarmEffect swarm = RoilingSwarmEffect.getSwarmFor(curr);
                if (swarm == null) continue;
                if (swarm.getNumActiveMembers() >= swarm.getParams().baseMembersToMaintain) continue;
                speedMulti = swarm.getParams().baseMembersToMaintain / 20f; //every 20 members, increase build speed by 1.
                RoilingSwarmEffect.SwarmMember p = swarm.addMember();

                ShipAPI piece = picker.pick();
                if (piece == null) continue;

                Vector2f loc = Misc.getPointWithinRadius(piece.getLocation(), piece.getCollisionRadius() * 0.5f);
                p.loc.set(loc);
                p.fader.setDurationIn(0.3f);
            }

        }
    }

    protected ShipAPI launchSwarm() {
        return status.createReclaim(primary,forceID,reclaim,isRefined,targetOverride);
    }

}
