package Abyssal_XO.data.scripts.threat.dialogPlugin;

import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.InteractionDialogPlugin;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.combat.EngagementResultAPI;

import java.util.Map;

public class Nano_Thief_SelectMastery implements InteractionDialogPlugin {
    @Override
    public void init(InteractionDialogAPI dialog) {
        dialog.getVisualPanel();
        /*
        --new questions--
        so, I want to create a interface with two parts. At the top, a list of ships the player has, with a button next to each that I use to add that ship to memory.
        at the bottom, a list of ships gotten from memory, with a number beside it that I can increase / decrease with buttons.
        the issue is: I have no idea how to start. I have attempted to look at mods that do things like this, but have found that the interfaces are very completed and attempting to understand them has been difficult.
        does anyone have any advice on were to start with this mess?
        */
    }

    @Override
    public void optionSelected(String optionText, Object optionData) {

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
    //so... how the fuck am I going to do this?
    /*
        option a:




    */
}
