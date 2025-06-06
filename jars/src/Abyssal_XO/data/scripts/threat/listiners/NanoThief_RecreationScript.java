package Abyssal_XO.data.scripts.threat.listiners;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.impl.combat.RiftLanceEffect;
import com.fs.starfarer.api.impl.combat.threat.*;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.util.*;
import org.lwjgl.util.vector.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static Abyssal_XO.data.scripts.Settings.TAG_HASRECLAMED;

public class NanoThief_RecreationScript extends BaseEveryFrameCombatPlugin {

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
	public NanoThief_RecreationScript(Pair<Nano_Thief_Stats,Integer> data, ShipAPI ship, float delay) {
		status = data.one;
		forceID = data.two;


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

	public List<ShipAPI> getPieces() {
		return pieces;
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

	protected void spawnSwarms(float amount) {
		if (!spawnedSwarms) {
			int numSwarms = 1;

			for (ConstructionSwarmSystemScript.SwarmConstructableVariant curr : ConstructionSwarmSystemScript.CONSTRUCTABLE) {
				if (curr.variantId.equals(primary.getVariant().getHullVariantId())) {
					numSwarms = (int) Math.round(curr.cr * 100f);
					break;
				}
			}

			for (int i = 0; i < numSwarms; i++) {
				ShipAPI curr = launchSwarm();
				swarms.add(curr);
			}
			spawnedSwarms = true;
		}

		interval2.advance(amount * 2f);
		if (interval2.intervalElapsed()) {
			WeightedRandomPicker<ShipAPI> picker = new WeightedRandomPicker<>();
			picker.addAll(pieces);

			for (ShipAPI curr : swarms) {
				RoilingSwarmEffect swarm = RoilingSwarmEffect.getSwarmFor(curr);
				if (swarm == null) continue;
				if (swarm.getNumActiveMembers() > swarm.getParams().baseMembersToMaintain) continue;

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
		return status.createReclaim(primary,forceID);
	}



/*
	public static RoilingSwarmEffect createSwarmFor(ShipAPI ship) {
		RoilingSwarmEffect existing = RoilingSwarmEffect.getSwarmFor(ship);
		if (existing != null) return existing;

//		if (true) {
//			return SwarmLauncherEffect.createTestDwellerSwarmFor(ship);
//		}

		RoilingSwarmEffect.RoilingSwarmParams params = new RoilingSwarmEffect.RoilingSwarmParams();
		if (ship.isFighter()) {
			float radius = 20f;
			int numMembers = 50;

			String wingId = ship.getWing() == null ? null : ship.getWing().getWingId();
			if (SwarmLauncherEffect.SWARM_RADIUS.containsKey(wingId)) {
				radius = SwarmLauncherEffect.SWARM_RADIUS.get(wingId);
			}
			if (SwarmLauncherEffect.FRAGMENT_NUM.containsKey(wingId)) {
				numMembers = SwarmLauncherEffect.FRAGMENT_NUM.get(wingId);
			}

			params.memberExchangeClass = STANDARD_SWARM_EXCHANGE_CLASS;
			params.flockingClass = FragmentSwarmHullmod.STANDARD_SWARM_FLOCKING_CLASS;
			params.maxSpeed = ship.getMaxSpeedWithoutBoost() +
					Math.max(ship.getMaxSpeedWithoutBoost() * 0.25f + 50f, 100f);

			params.flashRateMult = 0.25f;
			params.flashCoreRadiusMult = 0f;
			params.flashRadius = 120f;
			params.flashFringeColor = new Color(255,0,0,40);
			params.flashCoreColor = new Color(255,255,255,127);

			// if this is set to true and the swarm is glowing, missile-fragments pop over the glow and it looks bad
			//params.renderFlashOnSameLayer = true;

			params.maxOffset = radius;
			params.initialMembers = numMembers;
			params.baseMembersToMaintain = params.initialMembers;
		} else {
			params.memberExchangeClass = STANDARD_SWARM_EXCHANGE_CLASS;
			params.maxSpeed = ship.getMaxSpeedWithoutBoost() +
					Math.max(ship.getMaxSpeedWithoutBoost() * 0.25f + 50f, 100f) +
					ship.getMutableStats().getZeroFluxSpeedBoost().getModifiedValue();

			params.flashRateMult = 0.25f;
			params.flashCoreRadiusMult = 0f;
			params.flashRadius = 120f;
			params.flashFringeColor = new Color(255,0,0,40);
			params.flashCoreColor = new Color(255,255,255,127);

			// if this is set to true and the swarm is glowing, missile-fragments pop over the glow and it looks bad
			//params.renderFlashOnSameLayer = true;

			params.minOffset = 0f;
			params.maxOffset = Math.min(100f, ship.getCollisionRadius() * 0.5f);
			params.generateOffsetAroundAttachedEntityOval = true;
			params.despawnSound = null; // ship explosion does the job instead
			params.spawnOffsetMult = 0.33f;
			params.spawnOffsetMultForInitialSpawn = 1f;

			params.baseMembersToMaintain = getBaseSwarmSize(ship.getHullSize());
			params.memberRespawnRate = getBaseSwarmRespawnRateMult(ship.getHullSize());
			params.maxNumMembersToAlwaysRemoveAbove = params.baseMembersToMaintain * 2;

			//params.offsetRerollFractionOnMemberRespawn = 0.05f;

			params.initialMembers = 0;
			params.initialMembers = params.baseMembersToMaintain;
			params.removeMembersAboveMaintainLevel = false;
		}

		List<WeaponAPI> glowWeapons = new ArrayList<>();
		for (WeaponAPI w : ship.getAllWeapons()) {
			if (w.usesAmmo() && w.getSpec().hasTag(Tags.FRAGMENT_GLOW)) {
				glowWeapons.add(w);
			}
			if (w.getSpec().hasTag(Tags.OVERSEER_CHARGE) ||
					(ship.isFighter() && w.getSpec().hasTag(Tags.OVERSEER_CHARGE_FIGHTER))) {
				w.setAmmo(0);
			}
		}

//		if (ship.hasTag(Tags.FRAGMENT_SWARM_START_WITH_ZERO_FRAGMENTS)) {
//			params.initialMembers = 0;
//		}

		return new RoilingSwarmEffect(ship, params) {
			protected ColorShifterUtil glowColorShifter = new ColorShifterUtil(new Color(0, 0, 0, 0));
			protected boolean resetFlash = false;

			@Override
			public int getNumMembersToMaintain() {
				if (ship.isFighter()) {
					return (int)Math.round(((0.2f + 0.8f * ship.getHullLevel()) * super.getNumMembersToMaintain()));
				}
				return super.getNumMembersToMaintain();
			}

			@Override
			public void advance(float amount) {
				super.advance(amount);

				glowColorShifter.advance(amount);

				// this is actually QUITE performance-intensive on the rendering, at least doubles the cost per swarm
				// (comment was from when flashFrequency was *10 with a shorter flashRateMult; *2 is pretty ok -am
				if (VoltaicDischargeOnFireEffect.isSwarmPhaseMode(ship)) {
					params.flashFrequency = 4f;
					params.flashProbability = 1f;
					resetFlash = true;
				} else {
					if (!glowWeapons.isEmpty()) {
						float ammoFractionTotal = 0f;
						float totalOP = 0f;
						for (WeaponAPI w : glowWeapons) {
							float f = w.getAmmo() / Math.max(1f, w.getMaxAmmo());
							Color glowColor = w.getSpec().getGlowColor();
							//						if (f > 0) {
							//							glowColorShifter.shift(w, glowColor, 0.5f, 0.5f, 1f);
							//						}
							glowColorShifter.shift(w, glowColor, 0.5f, 0.5f, 1f);
							float weight = w.getSpec().getOrdnancePointCost(null);
							ammoFractionTotal += f * weight;
							totalOP += weight;
						}

						float ammoFraction = ammoFractionTotal / Math.max(1f, totalOP);
						params.flashFrequency = (1f + ammoFraction) * 2f;
						params.flashFrequency *= Math.max(1f, Math.min(2f, params.baseMembersToMaintain / 50f));
						params.flashProbability = 1f;
						if (ammoFraction <= 0f) {
							params.flashProbability = 0f;
						}
						//params.flashFringeColor = new Color(255,0,0,(int)(30f + 30f * ammoFraction));
						//float glowAlphaBase = 50f;
						float glowAlphaBase = 30f;
						if (ship.isFighter()) {
							glowAlphaBase = 18f;
						}

						float extraGlow = (totalOP - 10f) / 90f;
						if (extraGlow < 0) extraGlow = 0;
						if (extraGlow > 1f) extraGlow = 1f;

						int glowAlpha = (int)(glowAlphaBase + glowAlphaBase * (ammoFraction + extraGlow * 0.5f));
						if (glowAlpha > 255) glowAlpha = 255;
						//params.flashFringeColor = Misc.setAlpha(glowColorShifter.getCurr(), glowAlpha);
						params.flashFringeColor = Misc.setBrightness(glowColorShifter.getCurr(), 255);
						params.flashFringeColor = Misc.setAlpha(params.flashFringeColor, glowAlpha);

						resetFlash = true;
					} else {
						//if (ThreatSwarmAI.isAttackSwarm(ship)) {
						if (resetFlash) {
							params.flashProbability = 0f;
							resetFlash = false;
						}
					}
				}

//				int flashing = 0;
//				for (SwarmMember p : members) {
//					if (p.flash != null) {
//						flashing++;
//					}
//				}
//				System.out.println("Flashing: " + flashing + ", total: " + members.size());
			}

		};
	}
	public static int getBaseSwarmSize(ShipAPI.HullSize size) {
		switch (size) {
			case CAPITAL_SHIP: return 100;
			case CRUISER: return 60;
			case DESTROYER: return 40;
			case FRIGATE: return 20;
			case FIGHTER: return 50;
			case DEFAULT: return 20;
			default: return 20;
		}
	}

	public static float getBaseSwarmRespawnRateMult(ShipAPI.HullSize size) {
		switch (size) {
			case CAPITAL_SHIP: return 5f;
			case CRUISER: return 3f;
			case DESTROYER: return 2f;
			case FRIGATE: return 1f;
			case FIGHTER: return 0f;
			case DEFAULT: return 0f;
			default: return 0f;
		}
	}*/
}
