package Abyssal_XO.data.scripts.threat;

import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import second_in_command.SCData;
import second_in_command.specs.SCBaseSkillPlugin;

public class Nano_Thief_SKill_Base extends SCBaseSkillPlugin {
    float swarmCostMulti = 1;
    float swarmCostAdd = 0;

    float qualityMulti = 1;
    float qualityAdd = 0;

    public float qualityChange(float reclaim,ShipAPI target){
        return reclaim;
    }
    public void ApplyChangeOnReclaim(ShipAPI ship,ShipAPI reclaim, float reclaimValue){
        //runs code whenever reclaim is collected.
    }
    public void changeReclaimStats(ShipAPI ship,int quality){
        //changes the stats of the reclaim swarm
    }
    public void changeCombatSwarmStats(ShipAPI ship,int quality){
        //changes the stats of the combat swarm
    }
    public void changeDefenderSwarmStats(ShipAPI ship,int quality){
        //changes the stats of the defense swarm.
    }

    @Override
    public String getAffectsString() {
        return "";
    }

    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltipMakerAPI) {

    }
}
