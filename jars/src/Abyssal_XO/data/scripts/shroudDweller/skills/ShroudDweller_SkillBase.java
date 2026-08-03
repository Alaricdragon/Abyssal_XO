package Abyssal_XO.data.scripts.shroudDweller.skills;

import Abyssal_XO.data.scripts.shroudDweller.activeSkills.ShroudDweller_ActiveSkillBase;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;
import second_in_command.specs.SCBaseSkillPlugin;
import second_in_command.specs.SCOfficer;

public abstract class ShroudDweller_SkillBase extends SCBaseSkillPlugin {
    //note: this is a lot easier then nano-thief. Because everything is going to be controled by a 'central thing' that will handle all skill updates and useage.
    /*public void initStats(Nano_Thief_Stats stats){

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
    }*/
    public abstract boolean addListiner();
    public abstract boolean addMultiListeners();
    public abstract ShroudDweller_ActiveSkillBase getSkillCode();
    @Override
    public String getAffectsString() {
        return "The Battlefield";//"Simulacrum Fighter Wings produced by your fleet";
    }
    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltipMakerAPI) {

    }
    public void addMultiSkillText(SCData scData, TooltipMakerAPI tooltipMakerAPI){
        int a = getNumberThisSkills(scData);
        if (a <= 1) return;
        applyMultiSkillString(scData,tooltipMakerAPI, getMultiString(scData,a),a);
    }
    public void applyMultiSkillString(SCData scData, TooltipMakerAPI tooltipMakerAPI,String input,int number){
        tooltipMakerAPI.addPara("",0);
        tooltipMakerAPI.addPara("Synergy bonus between "+number+" copys of same skill:",0,Misc.getStoryOptionColor(),Misc.getStoryOptionColor());
        tooltipMakerAPI.addPara(input,0, Misc.getStoryOptionColor(),Misc.getStoryOptionColor());
    }
    private int getNumberThisSkills(SCData scData){
        int number = 0;
        for (SCOfficer a : scData.getActiveOfficers()) for (String b : a.getActiveSkillIDs()){
            if (b.equals(getId())){
                number++;
                break;//no more then one skill in skill.
            }
        }
        return number;
    }
    public String getMultiString(SCData scData, int number){return "";}
}
