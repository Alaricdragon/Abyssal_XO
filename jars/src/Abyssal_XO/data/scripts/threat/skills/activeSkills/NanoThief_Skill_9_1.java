package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.threat.skills.NanoThief_9;
import com.fs.starfarer.api.combat.ShipAPI;

public class NanoThief_Skill_9_1 extends NanoThief_SkillBase{
    public NanoThief_Skill_9_1(NanoThief_ShipSkills skills, ShipAPI ship) {
        super(skills, ship);
    }
    @Override
    public boolean alwaysAdvance() {
        //not always advance, because effects only happen when reclaim is available.
        return false;
    }
    float time = 1;
    float crRegen = 0;
    @Override
    public void advance(float amount) {
        advanceCrRepair();
        if (time >= 0){
            time -= amount;
            return;
        }
        time = 1;
        if (ship.getCurrentCR() >= NanoThief_9.crStart){
            skills.speedMods.remove("9_1");
            skills.costMods.remove("9_1");
            return;
        }
        advanceCrRepair();
        skills.speedMods.put("9_1", (float) NanoThief_9.crSkillSpeed(ship.getCurrentCR()));
        skills.costMods.put("9_1",NanoThief_9.crSkillCost(ship.getCurrentCR()));
    }
    public void adjustCRSpeed(){
        if (ship.getCurrentCR() >= NanoThief_9.crStart){
            crRegen = 0;
            return;
        }
        crRegen = (float) NanoThief_9.crRegenSpeed(ship.getCurrentCR()) / 100f;

    }
    public void advanceCrRepair(){
        /*todo:
            right now my current CR regen thing should be giving me a CR every second.
            I dont know what is up with it, but it is way to fucking fast...
            omg...
            I forgot to multiply the advance by the current 'amount' so its going like, 500X faster.
            opps lol.

        */
        adjustCRSpeed();
        if (crRegen <= 0) return;
        double cost = skills.getModifiedCost(crRegen*NanoThief_9.reclaimCostPerCR);
        cost = Math.min(cost,skills.getTotalReclaim());
        //double newCR = NanoThief_9.reclaimCostPerCR*(cost - ((skills.costMulti-1)*cost));
        double newCR = cost / skills.getModifiedCost(NanoThief_9.reclaimCostPerCR);
        ship.setCurrentCR((float) (newCR+ship.getCurrentCR()));
        skills.useReclaim(cost);
        log.info("recovering "+newCR+" cr at the cost of "+cost);
    }
}
