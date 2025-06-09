package Abyssal_XO.data.scripts.threat.AI;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.impl.combat.threat.*;
import com.fs.starfarer.api.util.IntervalUtil;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.WeightedRandomPicker;
import org.apache.log4j.Logger;
import org.lwjgl.util.vector.Vector2f;

import java.util.ArrayList;
import java.util.List;
public class Nano_Thief_AI_Reclaim implements ShipAIPlugin {
    //notes:
    //this is copied from 'ThreatSwarmAI'. this required a lot of removing things (construction swarms, and I require defense swarms.)
	public static float ATTRACTOR_RANGE_MAX_SAME_WING = 1000000f;
	public static float ATTRACTOR_RANGE_MAX = 500f;
	public static float COHESION_RANGE_MIN = 150f;
	public static float COHESION_RANGE_MAX = 300f;
	public static float REPEL_RANGE_MIN = 0f;
	public static float REPEL_RANGE_MAX = 150f;

	public static float MAX_TARGET_RANGE = 3000f;

	public static boolean isReclamationSwarm(ShipAPI ship) {
		return ship != null && ship.getVariant().getHullVariantId().equals(SwarmLauncherEffect.RECLAMATION_SWARM_VARIANT);
	}

	public static class SharedSwarmWingData {
		public ShipAPI target = null;
	}

	public static class FlockingData {
		public Vector2f loc;
		public Vector2f vel;
		public float minA;
		public float maxA;
		public float minR;
		public float maxR;
		public float repelAtAngleDist;
		public float minC;
		public float maxC;
		public float attractWeight;
		public float repelWeight;
		public float cohesionWeight;
		public float facing;
	}

	public static float PROB_ENABLE_OTHER_GROUP = 0.5f;

	protected ShipwideAIFlags flags = new ShipwideAIFlags();
	protected ShipAPI ship;

	protected IntervalUtil updateInterval = new IntervalUtil(0.5f, 1.5f);
	protected IntervalUtil headingInterval = new IntervalUtil(0.5f, 1.5f);
	protected IntervalUtil attackRangeMultInterval = new IntervalUtil(0.2f, 1.8f);
	protected IntervalUtil reclamationReturnInterval = new IntervalUtil(0.2f, 1.8f);

	protected float sinceTurnedOffFlash = 0f;
	protected ShipAPI fabricator = null;

	protected List<FlockingData> flockingData = new ArrayList<>();
	protected float desiredHeading = 0f;
	protected float headingChangeRate = 0f;
	protected float elapsedSincePrevHeadingUpdate = 0f;
	protected float attackRangeMult = 1f;

	protected IntervalUtil enableOtherWeaponInterval = new IntervalUtil(5f, 15f);
	protected IntervalUtil priorityTargetPickerInterval = new IntervalUtil(1f, 3f);
	protected float enableOtherWeaponDuration = 0f;
	protected float elapsed = 0f;

	protected boolean startedConstruction = false;
	private ConstructionSwarmSystemScript.SwarmConstructionData constructionData;
	private ThreatShipConstructionScript constructionScript;

	//protected boolean attackSwarm = false;
	//protected boolean constructionSwarm = false;
	protected boolean reclamationSwarm = true;

	private Nano_Thief_Stats stats;
	private int reclaimValue;

	private static Logger log = Global.getLogger(Nano_Thief_AI_Reclaim.class);
	public Nano_Thief_AI_Reclaim(ShipAPI ship, Nano_Thief_Stats stats,int reclam) {
		this.ship = ship;
		this.stats = stats;
		this.reclaimValue = reclam;

		isReclamationSwarm(ship);

		doInitialSetup();

		updateInterval.forceIntervalElapsed();
		headingInterval.forceIntervalElapsed();
		attackRangeMultInterval.forceIntervalElapsed();
		priorityTargetPickerInterval.forceIntervalElapsed();
	}

	public SharedSwarmWingData getShared() {
		if (ship.getWing() == null) return new SharedSwarmWingData();

		String key = "SharedSwarmWingData";
		SharedSwarmWingData data = (SharedSwarmWingData) ship.getWing().getCustomData().get(key);
		if (data == null) {
			data = new SharedSwarmWingData();
			ship.getWing().getCustomData().put(key, data);
		}
		return data;
	}

	protected void doInitialSetup() {
		/*if (attackSwarm) {
			// 0: voltaic
			// 1: unstable
			// 2: kinetic
			// 3: seeker
			// 4: defabrication

			toggleOn(0);
			toggleOn(1);
			toggleOff(2);
			toggleOff(3);
			toggleOff(4);

			ship.giveCommand(ShipCommand.SELECT_GROUP, null, 6);
		}*/
	}

	protected void toggleOn(int groupNum) {
		List<WeaponGroupAPI> groups = ship.getWeaponGroupsCopy();
		if (groups.size() <= groupNum) return;
		groups.get(groupNum).toggleOn();
	}
	protected void toggleOff(int groupNum) {
		List<WeaponGroupAPI> groups = ship.getWeaponGroupsCopy();
		if (groups.size() <= groupNum) return;
		groups.get(groupNum).toggleOff();
	}


	protected void advanceForSpecificSwarmType(float amount) {
		/*if (attackSwarm) {
			if (ship.isWingLeader()) {
				priorityTargetPickerInterval.advance(amount);
				if (priorityTargetPickerInterval.intervalElapsed()) {
					pickPriorityTarget();
				}
			}

			attackRangeMultInterval.advance(amount * 0.1f);
			if (attackRangeMultInterval.intervalElapsed()) {
				updateAttackRangeMult();
			}

			// 0: voltaic, always on
			// 1: unstable, always on
			// 2: kinetic
			// 3: seeker
			// 4: unused (was defab at some point)
			if (enableOtherWeaponDuration > 0) {
				enableOtherWeaponDuration -= amount;
				if (enableOtherWeaponDuration <= 0) {
					toggleOff(2);
					toggleOff(3);
					toggleOff(4);
				}
			} else {
				//amount *= 10f;
				boolean phaseMode = VoltaicDischargeOnFireEffect.isSwarmPhaseMode(ship);


				enableOtherWeaponInterval.advance(amount * 5f);
				if (enableOtherWeaponInterval.intervalElapsed()) {
					if ((float) Math.random() < PROB_ENABLE_OTHER_GROUP) {
						toggleOff(2);
						toggleOff(3);
						toggleOff(4);

						ShipAPI target = (ShipAPI) flags.getCustom(ShipwideAIFlags.AIFlags.MANEUVER_TARGET);

//						boolean targetShieldsFacingUs = false;
//						if (target != null) {
//							ShieldAPI targetShield = target.getShield();
//							targetShieldsFacingUs = targetShield != null &&
//										targetShield.isOn() &&
//										Misc.isInArc(targetShield.getFacing(), Math.max(30f, targetShield.getActiveArc()),
//												target.getLocation(), ship.getLocation());
//						}
//						if (targetShieldsFacingUs) {
//							toggleOn(2);
//						} else {
//							toggleOn(3);
//						}


						// use Seeker only when it will be destroyed by using seeker, as a "final attack"
						boolean useSeeker = ship.getHullLevel() < 0.22f;
						boolean useKinetic = true;
						if (target == null || target.isFighter()) {
							useSeeker = false;
							useKinetic = false;
						}

						if (useSeeker) {
							toggleOn(3);
						} else if (useKinetic) {
							toggleOn(2);
						}

						enableOtherWeaponDuration = 0.5f + 0.5f * (float) Math.random();
					}
				}
			}
		}

		if (constructionSwarm) {
			if (constructionData == null) {
				RoilingSwarmEffect swarm = RoilingSwarmEffect.getSwarmFor(ship);
				if (swarm != null) {
					constructionData = (ConstructionSwarmSystemScript.SwarmConstructionData) swarm.custom1;
				}
			}
			if (constructionData != null) {
				if (elapsed > constructionData.preConstructionTravelTime && !startedConstruction) {
					startedConstruction = true;
					RoilingSwarmEffect swarm = RoilingSwarmEffect.getSwarmFor(ship);
					if (swarm != null) {
						constructionScript = new ThreatShipConstructionScript(
											constructionData.variantId, ship, 0f, constructionData.constructionTime);
						Global.getCombatEngine().addPlugin(constructionScript);
					}
				}
			}
		}
        */
		if (reclamationSwarm) {
			//log.info("advancing swarm AI for the swarm!");
			if (fabricator == null || !fabricator.isAlive()) {
				reclamationReturnInterval.advance(amount);
				if (reclamationReturnInterval.intervalElapsed()) {
					CombatEngineAPI engine = Global.getCombatEngine();
					fabricator = stats.getTargetForReclaim(ship,engine);
					if (fabricator == null){
						ship.setShipAI(new ThreatSwarmAI(ship));
						//todo: change to combat AI.
					}
				}
			} else if(canGather()){
				sinceTurnedOffFlash += amount;
				if (sinceTurnedOffFlash > 3f) {
					CombatEngineAPI engine = Global.getCombatEngine();
					if (fabricator.isAlive()) {
						//todo: HERE. this is were I apply effects to ships! I have no fucking clue how it works.
						//fabricator.setCurrentCR(Math.min(1f, fabricator.getCurrentCR() + 0.01f * ship.getHullLevel()));
						stats.applyEffectsWhenAbsorbed(fabricator,ship,reclaimValue);
						RoilingSwarmEffect swarm = RoilingSwarmEffect.getSwarmFor(ship);
						RoilingSwarmEffect swarmFabricator = RoilingSwarmEffect.getSwarmFor(fabricator);
						if (swarm != null && swarmFabricator != null) {
							swarm.transferMembersTo(swarmFabricator, swarm.getNumActiveMembers());
						}
					}
					ship.setHitpoints(0f);
					ship.setSpawnDebris(false);
					engine.applyDamage(ship, ship.getLocation(), 100f, DamageType.ENERGY, 0f, true, false, ship, false);
				}
			}
		}
	}

	protected void pickPriorityTarget() {
		SharedSwarmWingData data = getShared();
		if (data.target != null && data.target.isAlive()) {
			return;
		}

		WeightedRandomPicker<ShipAPI> picker = new WeightedRandomPicker<>();
		CombatEngineAPI engine = Global.getCombatEngine();
		int owner = ship.getOriginalOwner();

		for (ShipAPI curr : engine.getShips()) {
			if (curr == ship) continue;
			if (curr.isFighter()) continue;
			if (curr.isHulk() || curr.getOwner() == 100) continue;

			if (curr.getOwner() != owner && engine.isAwareOf(owner, curr)) {
				float weight = getShipWeight(curr);
				if (curr.isFrigate()) {
					weight *= 0.0001f;
				}
				picker.add(curr, weight);
			}
		}

		data.target = picker.pick();
	}
	protected void updateAttackRangeMult() {
		//attackRangeMult = 0.75f + 0.5f * (float) Math.random();
		attackRangeMult = 0.5f + 1f * (float) Math.random();
	}

	@Override
	public void advance(float amount) {
		//log.info("running basic advance...!");
		//if (true) return;

		elapsed += amount;
		advanceForSpecificSwarmType(amount);

		updateInterval.advance(amount);
		if (updateInterval.intervalElapsed()) {
			updateFlockingData();
		}

		headingInterval.advance(amount * 5f);
		if (headingInterval.intervalElapsed()) {
			computeDesiredHeading();
			elapsedSincePrevHeadingUpdate = 0f;
		}

		giveMovementCommands();

		elapsedSincePrevHeadingUpdate += amount;
	}

	protected void giveMovementCommands() {
		if (constructionScript != null && constructionScript.getShip() != null) {
			ship.giveCommand(ShipCommand.DECELERATE, null, 0);
			return;
		}

		String source = "swarm_wingman_catch_up_speed_bonus";
		MutableShipStatsAPI stats = ship.getMutableStats();
		if (ship.isWingLeader() || ship.getWingLeader() == null) {
			stats.getMaxSpeed().unmodifyMult(source);
			stats.getAcceleration().unmodifyMult(source);
			stats.getDeceleration().unmodifyMult(source);
		} else {
			ShipAPI leader = ship.getWingLeader();
			float dist = Misc.getDistance(ship.getLocation(), leader.getLocation());
			float mult = (dist - COHESION_RANGE_MAX * 0.5f -
					ship.getCollisionRadius() * 0.5f - leader.getCollisionRadius() * 0.5f) / COHESION_RANGE_MAX;
			if (mult < 0f) mult = 0f;
			if (mult > 1f) mult = 1f;
			stats.getMaxSpeed().modifyMult(source, 1f + .25f * mult);
			stats.getAcceleration().modifyMult(source, 1f + 0.5f * mult);
			stats.getDeceleration().modifyMult(source, 1f + 0.5f * mult);
		}

		float useHeading = desiredHeading;
		//useHeading += headingChangeRate * elapsedSincePrevHeadingUpdate;

		CombatEngineAPI engine = Global.getCombatEngine();
		engine.headInDirectionWithoutTurning(ship, useHeading, 10000);
		Misc.turnTowardsFacingV2(ship, useHeading, 0f);
	}

	protected void computeDesiredHeading() {

		Vector2f loc = ship.getLocation();
		Vector2f vel = ship.getVelocity();
		float facing = ship.getFacing();

		Vector2f total = new Vector2f();

		for (FlockingData curr : flockingData) {
			float dist = Misc.getDistance(curr.loc, loc);
			if (curr.maxR > 0 && dist < curr.maxR) {
				float repelWeight = curr.repelWeight;
				if (dist > curr.minR && curr.maxR > curr.minR) {
					repelWeight = (dist - curr.minR)  / (curr.maxR - curr.minR);
					if (repelWeight > 1f) repelWeight = 1f;
					repelWeight = 1f - repelWeight;
					repelWeight *= curr.repelWeight;
				}

				Vector2f dir = Misc.getUnitVector(curr.loc, loc);

				float distIntoRepel = curr.maxR - dist;
				float repelAdjustmentAngle = 0f;
				if (distIntoRepel < curr.repelAtAngleDist && curr.repelAtAngleDist > 0) {
					float repelMult = (1f - distIntoRepel / curr.repelAtAngleDist);
					repelAdjustmentAngle = 90f * repelMult;
					repelWeight *= (1f - repelMult);

					float repelAngle = Misc.getAngleInDegrees(dir);
					float turnDir = Misc.getClosestTurnDirection(dir, vel);
					repelAdjustmentAngle *= turnDir;
					dir = Misc.getUnitVectorAtDegreeAngle(repelAngle + repelAdjustmentAngle);
				}

				dir.scale(repelWeight);
				Vector2f.add(total, dir, total);
			}

			if (curr.maxA > 0 && dist < curr.maxA) {
				float attractWeight = curr.attractWeight;
				if (dist > curr.minA && curr.maxA > curr.minA) {
					attractWeight = (dist - curr.minA)  / (curr.maxA - curr.minA);
					if (attractWeight > 1f) attractWeight = 1f;
					attractWeight = 1f - attractWeight;
					attractWeight *= curr.attractWeight;
				}

				Vector2f dir = Misc.getUnitVector(loc, curr.loc);
				dir.scale(attractWeight);
				Vector2f.add(total, dir, total);
			}

			if (curr.maxC > 0 && dist < curr.maxC) {
				float cohesionWeight = curr.cohesionWeight;
				if (dist > curr.minC && curr.maxC > curr.minC) {
					cohesionWeight = (dist - curr.minC)  / (curr.maxC - curr.minC);
					if (cohesionWeight > 1f) cohesionWeight = 1f;
					cohesionWeight = 1f - cohesionWeight;
					cohesionWeight *= curr.cohesionWeight;
				}

				Vector2f dir = new Vector2f(curr.vel);
				Misc.normalise(dir);
				dir.scale(cohesionWeight);
				Vector2f.add(total, dir, total);
			}
		}

		if (total.length() <= 0) {
			desiredHeading = ship.getFacing();
			headingChangeRate = ship.getAngularVelocity() * 0.5f;
		} else {
//			Vector2f currDir = new Vector2f(vel);
//			Misc.normalise(currDir);
//			currDir.scale(total.length() * 0.25f);
//			Vector2f.add(total, currDir, total);

			float prev = desiredHeading;
			desiredHeading = Misc.getAngleInDegrees(total);
			if (elapsedSincePrevHeadingUpdate > 0) {
				headingChangeRate = Misc.getAngleDiff(prev, desiredHeading) / elapsedSincePrevHeadingUpdate;
			} else {
				headingChangeRate = ship.getAngularVelocity() * 0.5f;
			}
		}
	}


	protected void updateFlockingData() {
		flockingData.clear();
		CombatEngineAPI engine = Global.getCombatEngine();

		int owner = ship.getOriginalOwner();
		FighterWingAPI wing = ship.getWing();
		if (wing == null) return;

		Vector2f loc = ship.getLocation();
		boolean wingLeader = ship.isWingLeader();

		float radius = ship.getCollisionRadius() * 0.5f;

		RoilingSwarmEffect swarm = RoilingSwarmEffect.getSwarmFor(ship);
		if (swarm != null) { // && (constructionSwarm) {
			radius = swarm.getParams().maxOffset;
		}

		ShipAPI target = null;

		ArrayList<FlockingData> flockingData_s0 = new ArrayList<>();
		ArrayList<FlockingData> flockingData_s1 = new ArrayList<>();
		ArrayList<FlockingData> flockingData_s2 = new ArrayList<>();
		ArrayList<FlockingData> flockingData_s3 = new ArrayList<>();
		ArrayList<ArrayList<FlockingData>> allData = new ArrayList<>();
		allData.add(flockingData_s3);
		allData.add(flockingData_s2);
		allData.add(flockingData_s1);
		allData.add(flockingData_s0);
		while(true){//please dont ask why.
			if (fabricator == null) break;
			if (fabricator == ship) break;
			if (fabricator.isFighter() && (!fabricator.isWingLeader() || fabricator.getOwner() == owner)) break;

			if (fabricator.isHulk() || fabricator.getOwner() == 100) break;

			// return to Fabricator Units, ignore other ships
			//NOTE: here is were I can filter what possible targets I want.
			if (fabricator.getOwner() != owner) break;
			if (!stats.isValidReclaimTarget(fabricator)) break;
			if (!fabricator.equals(fabricator)) break;
			float fabricatorRadius = fabricator.getCollisionRadius() * 0.5f;
			FlockingData data = new FlockingData();
			data.facing = fabricator.getFacing();
			data.loc = fabricator.getLocation();
			data.vel = fabricator.getVelocity();
			data.attractWeight = getShipWeight(fabricator) * stats.getReclaimTargetPriority(ship);
			data.repelWeight = data.attractWeight * 10f;
			data.minA = radius + fabricatorRadius;
			data.maxA = 1000000f;
			data.minR = radius + fabricatorRadius;
			data.maxR = radius + fabricatorRadius + 100f;
			data.repelAtAngleDist = (data.maxR - data.minR) * 0.5f;
			flockingData.add(data);
			break;
		}
		if (target != null) {
			flags.setFlag(ShipwideAIFlags.AIFlags.MANEUVER_TARGET, 3f, target);
		} else {
			flags.unsetFlag(ShipwideAIFlags.AIFlags.MANEUVER_TARGET);
		}

		if (flockingData.isEmpty()) {
			FlockingData data = new FlockingData();
			data.facing = 0f;
			data.loc = new Vector2f();
			data.vel = new Vector2f();
			data.attractWeight = 5f;
			data.repelWeight = data.attractWeight * 10f;
			data.minA = 1000f;
			data.maxA = 1000000f;
			data.minR = 1000f;
			data.maxR = 3000f;
			data.repelAtAngleDist = 1000f;
			flockingData.add(data);
		}

		if (swarm != null && swarm.getParams().flockingClass != null && swarm.getAttachedTo() != null) {
			for (RoilingSwarmEffect curr : RoilingSwarmEffect.getFlockingMap().getList(swarm.getParams().flockingClass)) {
				if (curr == swarm) continue;
				if (curr.getAttachedTo() == ship || curr.getAttachedTo() == null ||
						curr.getAttachedTo().getOwner() != owner) {
					continue;
				}

				if (swarm.getParams().flockingClass.equals(curr.getParams().flockingClass)) {
					// avoid other construction swarms - looking for a clear area
					boolean sameWing = wing == ((ShipAPI) curr.getAttachedTo()).getWing();
					boolean otherWingLeader = ((ShipAPI) curr.getAttachedTo()).isWingLeader();

					// actually - make the leader wait a bit, otherwise they never catch up
					// or not
					if (wingLeader && sameWing) continue; // others catch up/line up on leader

					if (!sameWing) {
						float dist = Misc.getDistance(loc, curr.getAttachedTo().getLocation());
						if (dist > ATTRACTOR_RANGE_MAX + 500f) continue;
					}


					float currRadius = curr.getAttachedTo().getCollisionRadius() * 0.5f;
					FlockingData data = new FlockingData();
					data.facing = curr.getAttachedTo().getFacing();
					data.loc = curr.getAttachedTo().getLocation();
					data.vel = curr.getAttachedTo().getVelocity();
					data.attractWeight = 1f;
					data.repelWeight = 10f;
					data.cohesionWeight = 1f;
					if (sameWing) {
						if (wingLeader) {
							data.attractWeight = 0.1f;
						} else {
							data.attractWeight = 3f;
						}
						data.minA = 0f + radius + currRadius;
						data.maxA = ATTRACTOR_RANGE_MAX_SAME_WING + radius + currRadius;
					} else {
						data.minA = 0f + radius + currRadius;
						data.maxA = ATTRACTOR_RANGE_MAX + radius + currRadius;
					}
					data.minR = REPEL_RANGE_MIN + radius + currRadius;
					data.maxR = REPEL_RANGE_MAX + radius + currRadius;
					if (wingLeader && otherWingLeader) {
						data.maxR = ATTRACTOR_RANGE_MAX + radius + currRadius;
					}
					data.minC = COHESION_RANGE_MIN + radius + currRadius;
					data.maxC = COHESION_RANGE_MAX + radius + currRadius;
					if (reclamationSwarm) {
						data.minR *= 0.33f;
						data.maxR *= 0.33f;
					}
					flockingData.add(data);
				}
			}
		}
	}

	protected boolean canGather(){
		if (fabricator.isPhased()) return false;
		Vector2f pointA = fabricator.getLocation();
		Vector2f pointB = ship.getLocation();
		float c = Misc.getDistance(pointA,pointB);
		float size = fabricator.getCollisionRadius();
		if (c < (size*1.5)+100) return true;
		return false;
	}





	@Override
	public ShipwideAIFlags getAIFlags() {
		return flags;
	}

	public static float getShipWeight(ShipAPI ship) {
		return getShipWeight(ship, true);
	}
	public static float getShipWeight(ShipAPI ship, boolean adjustForNonCombat) {
		boolean nonCombat = ship.isNonCombat(false);
		float weight = 0;
		switch (ship.getHullSize()) {
		case CAPITAL_SHIP: weight += 8; break;
		case CRUISER: weight += 4; break;
		case DESTROYER: weight += 2; break;
		case FRIGATE: weight += 1; break;
		case FIGHTER: weight += 1; break;
		}
		if (nonCombat && adjustForNonCombat) weight *= 0.25f;
		if (ship.isDrone()) weight *= 0.1f;
		return weight;
	}

	public void setDoNotFireDelay(float amount) {}
	public void forceCircumstanceEvaluation() {}
	public boolean needsRefit() { return false; }
	public void cancelCurrentManeuver() {}
	public ShipAIConfig getConfig() { return null; }
}
