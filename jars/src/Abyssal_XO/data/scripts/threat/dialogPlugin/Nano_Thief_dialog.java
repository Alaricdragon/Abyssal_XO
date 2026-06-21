package Abyssal_XO.data.scripts.threat.dialogPlugin;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.InteractionDialogPlugin;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.combat.EngagementResultAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipHullSpecAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.codex.CodexDataV2;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.ui.UIPanelAPI;
import org.apache.log4j.Logger;
import second_in_command.SCUtils;
import second_in_command.specs.SCBaseSkillPlugin;
import second_in_command.specs.SCOfficer;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Nano_Thief_dialog implements InteractionDialogPlugin {
    private static Logger log = Global.getLogger(Nano_Thief_dialog.class);
    public void init(){
        //Global.getSector().getCampaignUI().
    }
    public static InteractionDialogAPI dialog;
    private static Nano_Thief_dialog me;
    private static boolean exitOnReset;
    //todo: 1: add a way to find out what skills I, the play, have.
    //      2: add dialogs. one opens the 'get attack cargo', one the 'get defensive wing', and one the 'get ships I can build' dialog.
    @Override
    public void init(InteractionDialogAPI dialog) {
        dialog.getOptionPanel().clearOptions();
        this.dialog = dialog;
        me = this;
        //attemptShowCargoAttack(dialog);
        boolean hasSimAtk=false, hasSimDef=false, hasMastery=false;
        int size = 0;
        for (SCOfficer b : SCUtils.getFleetData(Global.getSector().getPlayerFleet()).getActiveOfficers()){
            if (!b.getAptitudeId().equals("Abyssal_NanoThief")) continue;
            for (SCBaseSkillPlugin a : b.getActiveSkillPlugins()){
                if (a.getId().equals("SiC_NanoThief_skill_6")){
                    size++;
                    hasSimAtk=true;
                }
                if (a.getId().equals("SiC_NanoThief_skill_7")){
                    size++;
                    hasSimDef=true;
                }
                if (a.getId().equals("SiC_NanoThief_skill_10")){
                    size++;
                    hasMastery=true;
                }
            }
            break;
        }
        if (size == 1){
            if (hasSimAtk) attemptShowCargoAttack(dialog);
            if (hasSimDef) attemptShowCargoDef(dialog);
            if (hasMastery) attemptShowMastery(dialog);
            exitOnReset = true;
            return;
        }
        exitOnReset = false;
        if (hasSimAtk) dialog.getOptionPanel().addOption("attack","attack");
        if (hasSimDef) dialog.getOptionPanel().addOption("defense","defense");
        if (hasMastery) dialog.getOptionPanel().addOption("mastery","mastery");
        dialog.getOptionPanel().addOption("exit (escape)","exit");
        dialog.setOptionOnEscape("exit","exit");

        //dialog.get
    }
    public void attemptShowCargoAttack(InteractionDialogAPI dialog){
        dialog.showCargoPickerDialog("cargo","Conferm", "Cancal",true,400, Nano_Thief_Selection_Sfw_Attack_CargoListiner.prepareForSelection(true),new Nano_Thief_Selection_Sfw_Attack_CargoListiner());
    }
    public void attemptShowCargoDef(InteractionDialogAPI dialog){
        dialog.showCargoPickerDialog("cargo","Conferm", "Cancal",true,400, Nano_Thief_Selection_Sfw_Attack_CargoListiner.prepareForSelection(false),new Nano_Thief_Selection_Sfw_Def_CargoListiner());
    }
    public void attemptShowMastery(InteractionDialogAPI dialog){
        log.info("trying to set up mastery dialog plugin...");
        Nano_Thief_SelectMastery a = new Nano_Thief_SelectMastery();
        dialog.setPlugin(a);
        a.init(dialog);
    }
    public static void reset(){
        if (exitOnReset){
            dialog.dismiss();
            return;
        }
        me.init(dialog);
    }
    @Override
    public void optionSelected(String optionText, Object optionData) {
        switch ((String) optionData){
            case "start":
                init(dialog);
            case "exit":
            case "Cancal":
                dialog.dismiss();
                break;
            case "attack":
                attemptShowCargoAttack(dialog);
                break;
            case "defense":
                attemptShowCargoDef(dialog);
                break;
            case "mastery":
                attemptShowMastery(dialog);
                break;
        }
    }

    @Override
    public void optionMousedOver(String optionText, Object optionData) {

    }

    @Override
    public void advance(float amount) {

    }

    @Override
    public void backFromEngagement(EngagementResultAPI battleResult) {

    }

    @Override
    public Object getContext() {
        return null;
    }

    @Override
    public Map<String, MemoryAPI> getMemoryMap() {
        return Map.of();
    }
}
