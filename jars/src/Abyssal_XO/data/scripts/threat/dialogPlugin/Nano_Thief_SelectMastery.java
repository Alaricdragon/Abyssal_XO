package Abyssal_XO.data.scripts.threat.dialogPlugin;

import Abyssal_XO.data.scripts.CustomUIPannel.MasteryHolder;
import Abyssal_XO.data.scripts.CustomUIPannel.ShipsInFleetHolder;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.InteractionDialogPlugin;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.combat.EngagementResultAPI;
import com.fs.starfarer.api.ui.CustomPanelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import org.apache.log4j.Logger;

import java.util.Map;

public class Nano_Thief_SelectMastery implements InteractionDialogPlugin {
    private static Logger log = Global.getLogger(Nano_Thief_Stats.class);
    @Override
    public void init(InteractionDialogAPI dialog) {
        log.info("attempting to inti dialog.... help.");
        //dialog.getVisualPanel();

        //dialog.getOptionPanel().clearOptions();
        //dialog.getTextPanel().clear();
        //dialog.setPromptText("");

        //dialog.getTextPanel().addPara("did it work?");
        /*
        --new questions--
        so, I want to create a interface with two parts. At the top, a list of ships the player has, with a button next to each that I use to add that ship to memory.
        at the bottom, a list of ships gotten from memory, with a number beside it that I can increase / decrease with buttons.
        the issue is: I have no idea how to start. I have attempted to look at mods that do things like this, but have found that the interfaces are very completed and attempting to understand them has been difficult.
        does anyone have any advice on were to start with this mess?
        */
        /*so my plan:
        * 1: remember that I can force a givin item to be next to another item by milipulating its position stat.
        * -!and you define what logic each button fires in your plugin's buttonPressed method (the param there is the data param of addButton)!-
        *
        * 2: read the text in 'Nano_Thief_ThingTemp' to understand that system.
        * */


        //basics on how to get a my interface.
        try {
            log.info("attempting to add mastery dialog");
            new MasteryHolder(dialog);
            log.info("added mastery dialog");
        }catch (Exception e){
            log.info("ERROR: failed to get mastery dialog! PLEASE REPORT TO ALARIC DRAGON. error of: \n"+e);
            MasteryHolder.masteryHolder.returnToBaseDialog();
        }

        //ShipsInFleetHolder optionsHolder = new ShipsInFleetHolder();
        //optionsHolder(optionsHolder,dialog,0.2,0.2,0.8,0.5);
        //applying my interface

        log.info("in theory, done initing dialog");



        /*Nano_Thief_ThingTemp panel = new Nano_Thief_ThingTemp(){

        };
        panel.init();
        dialog.getVisualPanel().showCustomPanel(0,0,panel.getRoot());*/
    }
    /*public void optionsHolder(InteractionDialogAPI dialog, double px, double py, double sx, double sy){
        CustomPanelAPI THE_THING = dialog.getVisualPanel().showCustomPanel(1000,1000,optionsHolder);
        //setting the position of my data to the top left.
        THE_THING.getPosition().inTL(0,0);
        //getting a tooltip from my interface (this is were I get buttons. I have used tooltips before. this could fucking work lets goooo)
        TooltipMakerAPI tooltip = THE_THING.createUIElement(optionsHolder.p.getWidth(), optionsHolder.p.getHeight(), false);
        //adding a button, and getting the ability to manipulate its position!
        //tooltip.addButton("AAAAAA","ButtonData",5,5,1).getPosition();
        //tooltip.addCustom(THE_THING,1);
        ShipsInFleetHolder optionsHolder = new ShipsInFleetHolder();
        optionsHolder.createOptions(THE_THING,dialog,tooltip);
        //THE_THING.addUIElement(tooltip);
    }*/

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
