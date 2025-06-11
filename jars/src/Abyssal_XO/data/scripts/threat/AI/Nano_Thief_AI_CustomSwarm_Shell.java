package Abyssal_XO.data.scripts.threat.AI;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.util.Misc;
import org.apache.log4j.Logger;
import org.lwjgl.util.vector.Vector2f;

public class Nano_Thief_AI_CustomSwarm_Shell implements ShipAIPlugin {
    private static Logger log = Global.getLogger(Nano_Thief_Stats.class);
    private Nano_Thief_Stats stats;
    private ShipAIPlugin combatAI;
    private ShipAIPlugin swarmAI;
    private ShipAIPlugin currAI;
    private ShipAPI currTarget;
    private float range;
    private float dotRange;
    private ShipAPI ship;
    public boolean isInCombat = false;
    //float HPLossPerSecond;

    public Nano_Thief_AI_CustomSwarm_Shell(ShipAPI ship, Nano_Thief_Stats stats){
        this.ship = ship;
        this.stats = stats;
        ShipAIPlugin combatAI = ship.getShipAI();
        this.combatAI = combatAI;
        this.swarmAI = new Nano_Thief_AI_CustomSwarm(ship,stats);
        this.currAI = swarmAI;
        ship.setShipAI(this);
        ship.getWing().setSourceShip(ship);//sets to ifself to prevent min ingagment rage from triggering. might remove if i build a custom AI for the ships.
        //combatTags = ship.getAIFlags();

        range = ship.getWing().getSpec().getAttackRunRange();
        //HPLossPerSecond = ship.getHitpoints() / ttl;
    }
    private void getWeaponStats(){
        /*LOOK. heres the deal: there might very well be a mush better way to do this.
        * for that reason, I will be ignoring this for now.
        * I spent all day on this. I need to god dam chill.*/
    }
    private float time;
    private static final float infinitival = 5;
    @Override
    public void advance(float amount) {
        try {
            currAI.advance(amount);
        }catch (Exception e){
            combatAI.advance(amount);
            log.info("ERROR, failed to get AI data of error: "+e);
        }
        time+=amount;
        if (time >= infinitival){
            //applyHpLossIfRequired(time);
            time = 0;
            if (currTarget != null && currTarget.isAlive() && isInEngagementRange(currTarget)) return;
            if (!isInCombat && shouldBeFighter()){//sets relevant data in class.
                return;
            }
            if (isInCombat && !shouldBeFighter()){
                disactavateCombatAI();
                return;
            }
        }
    }
    private boolean shouldBeFighter(){
        CombatEngineAPI engine = Global.getCombatEngine();
        int owner = ship.getOriginalOwner();

        for (ShipAPI curr : engine.getShips()) {
            if (curr == ship) continue;
            if (curr.isFighter()) continue;
            if (curr.isHulk() || curr.getOwner() == 100) continue;
            if (!isInEngagementRange(curr)) continue;
            if (curr.getOwner() != owner && engine.isAwareOf(owner, curr)) {
                /*float weight = getShipWeight(curr);
                WeightedRandomPicker<ShipAPI> picker = new WeightedRandomPicker<>();
                picker.add(curr, weight);*/
                activateCombatAI(curr);
                return true;
            }
        }
        return false;
    }
    /*public void applyHpLossIfRequired(float time){
        if (isInCombat){
            for (ShipAPI a : ship.getWing().getWingMembers()){
                a.setHitpoints(a.getHitpoints() - (HPLossPerSecond * time));
                // cause the swarm (or what's left of it) to despawn
                if (a.getHitpoints() <= 0) {
                    a.setSpawnDebris(false);
                    Global.getCombatEngine().applyDamage(a, a.getLocation(), 100f, DamageType.ENERGY, 0f, true, false, null, false);
                }
            }
        }
    }*/
    private void activateCombatAI(ShipAPI target){
        currTarget = target;
        currAI = combatAI;
        isInCombat = true;
    }
    private void disactavateCombatAI(){
        currAI = swarmAI;
        currTarget = null;
        isInCombat = false;
    }
    private boolean isInEngagementRange(ShipAPI ship){
        Vector2f pointA = this.ship.getLocation();
        Vector2f pointB = ship.getLocation();
        float c = Misc.getDistance(pointA,pointB);
        return c <= range;
    }
    @Override
    public void setDoNotFireDelay(float amount){
        currAI.setDoNotFireDelay(amount);
    }
    @Override
    public void forceCircumstanceEvaluation(){
        currAI.forceCircumstanceEvaluation();
    }
    @Override
    public boolean needsRefit(){
        if (currAI.needsRefit()){
            //make this ship start to rapidly decay if it requires refits.
        }
        return false;
    }

    @Override
    public ShipwideAIFlags getAIFlags(){
        return currAI.getAIFlags();
    }

    @Override
    public void cancelCurrentManeuver(){
        currAI.cancelCurrentManeuver();
    }

    @Override
    public ShipAIConfig getConfig(){
        return currAI.getConfig();
    }
    @Override
    public void setTargetOverride(ShipAPI target) {
        currAI.setTargetOverride(target);
    };
}
