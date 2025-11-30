package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_ShipSkills;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_SkillBase;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import org.apache.log4j.Logger;
import second_in_command.SCData;
import second_in_command.specs.SCBaseSkillPlugin;

public class Nano_Thief_Skill_Base extends SCBaseSkillPlugin {
    protected static Logger log = Global.getLogger(Nano_Thief_Skill_Base.class);
    public void initStats(Nano_Thief_Stats stats){

    }
    public NanoThief_SkillBase createListiner(NanoThief_ShipSkills skills, ShipAPI ship){
        return null;
    }
    @Override
    public String getAffectsString() {
        return "All Ships In Fleet";//"Simulacrum Fighter Wings produced by your fleet";
    }
    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltipMakerAPI) {

    }
}
