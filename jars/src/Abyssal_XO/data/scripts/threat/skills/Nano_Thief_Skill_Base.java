package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_ShipSkills;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_SkillBase;
import Abyssal_XO.data.scripts.threat.skills.interfaces.NanoThief_InterfaceBase;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.apache.log4j.Logger;
import second_in_command.SCData;
import second_in_command.specs.SCBaseSkillPlugin;
import second_in_command.specs.SCOfficer;

public abstract class Nano_Thief_Skill_Base extends SCBaseSkillPlugin {
    protected static Logger log = Global.getLogger(Nano_Thief_Skill_Base.class);
    public void initStats(Nano_Thief_Stats stats){

    }
    public NanoThief_SkillBase createListiner(NanoThief_ShipSkills skills, ShipAPI ship){
        return null;
    }
    public NanoThief_SkillBase[] createListiners(NanoThief_ShipSkills skills, ShipAPI ship){
        return null;
    }
    public boolean canMulitAddListiners(){
        return true;
    }
    /// This is used for creating an interface when multiple of the same listeners are active.
    public NanoThief_InterfaceBase createInterface(){
        return null;
    }
    @Override
    public String getAffectsString() {
        return "All Ships In Fleet";//"Simulacrum Fighter Wings produced by your fleet";
    }
    /// for some internal bits of data.
    public abstract int getNanoThiefID();
    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltipMakerAPI) {

    }
    public void addMultiSkillText(SCData scData, TooltipMakerAPI tooltipMakerAPI){
        int a = getNumberThisSkills(scData);
        if (a <= 1) return;
        applyMultiSkillString(scData,tooltipMakerAPI, getMultiString(scData,a));
    }
    public void applyMultiSkillString(SCData scData, TooltipMakerAPI tooltipMakerAPI,String input){
        tooltipMakerAPI.addPara("",0);
        tooltipMakerAPI.addPara(input,0, Misc.getStoryOptionColor());
    }
    private int getNumberThisSkills(SCData scData){
        int number = 0;
        for (SCOfficer a : scData.getActiveOfficers()) for (String b : a.getActiveSkillIDs()){
            if (a.equals(getId())){
                number++;
                break;//no more then one skill in skill.
            }
        }
        return number;
    }
    public String getMultiString(SCData scData, int number){return "";}
}
