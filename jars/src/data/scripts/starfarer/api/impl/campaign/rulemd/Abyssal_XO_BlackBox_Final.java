package data.scripts.starfarer.api.impl.campaign.rulemd;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.backgrounds.BlackBox;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.TextPanelAPI;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.impl.campaign.rulecmd.BaseCommandPlugin;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import lunalib.lunaUtil.LunaCommons;
import second_in_command.SCUtils;
import second_in_command.specs.SCOfficer;

import java.util.List;
import java.util.Map;

public class Abyssal_XO_BlackBox_Final extends BaseCommandPlugin {
    @Override
    public boolean execute(String ruleId, InteractionDialogAPI dialog, List<Misc.Token> params, Map<String, MemoryAPI> memoryMap) {
        String command = params.get(0).getString(memoryMap);
        if (command == null) return false;
        if (command.equals("accept")){
            accept(dialog);
            //Global.getSector().getPlayerFleet().getCargo();
            //Global.getSector().getPlayerFleet().getCargo().addSpecial(new SpecialItemData("Abyssal_XO_black_box",null),1);
            Global.getSector().getPlayerFleet().getCargo().removeItems(CargoAPI.CargoItemType.SPECIAL,new SpecialItemData("Abyssal_XO_black_box",null),1);
            return true;
        }
        if (command.equals("refuse")){
            refuse(dialog);
            Global.getSector().getPlayerFleet().getCargo().removeItems(CargoAPI.CargoItemType.SPECIAL,new SpecialItemData("Abyssal_XO_black_box",null),1);
            return true;
        }
        return true;
    }
    public void accept(InteractionDialogAPI dialog){
        TextPanelAPI text = dialog.getTextPanel();
        PersonAPI person = Global.getFactory().createPerson();
        person.setName(new FullName("Black","Box", FullName.Gender.ANY));
        person.setPortraitSprite("graphics/icons/AbyssalXO_BlackBox.png");
        SCOfficer officer = new SCOfficer(person, "Abyssal_NanoThief");
        SCUtils.getPlayerData().addOfficerToFleet(officer);
        String s0 = "Nano-Thief attribute";
        String s1 = "The "+person.getNameString();
        text.addPara("Acquired the "+s0+" '"+s1+"'",Misc.getTextColor(),Misc.getHighlightColor(),s0,s1);

        if (!BlackBox.isStartUnlocked()){
            LunaCommons.set("Abyssal_XO","BlackBoxStartUnlock",true);
            text.addPara("");
            String name = "Black Box";
            text.addPara("Unlocked the '"+name+"' background",Misc.getTextColor(),Misc.getHighlightColor(),name);
        }
        Global.getSector().getMemory().set(Settings.MEMKEY_NANOTHIEF_BLACK_BOX_QUEST_STAGE,5);
    }
    public void refuse(InteractionDialogAPI dialog){
        TextPanelAPI text = dialog.getTextPanel();
        Global.getSector().getPlayerStats().addStoryPoints(8, text, false);
        Global.getSector().getMemory().set(Settings.MEMKEY_NANOTHIEF_BLACK_BOX_QUEST_STAGE,6);
    }
}
