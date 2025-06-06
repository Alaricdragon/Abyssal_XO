package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import org.apache.log4j.Logger;
import second_in_command.SCData;
import second_in_command.specs.SCBaseSkillPlugin;

public class Nano_Thief_SKill_Base extends SCBaseSkillPlugin {
    protected static Logger log = Global.getLogger(Nano_Thief_SKill_Base.class);
    public float swarmCostMulti = 1;
    public float swarmCostAdd = 0;

    public float qualityMulti = 1;
    public float qualityAdd = 0;

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
