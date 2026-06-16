package Abyssal_XO.data.scripts.CustomUIPannel;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.dialogPlugin.Nano_Thief_dialog;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.CustomVisualDialogDelegate;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.*;
import org.apache.log4j.Logger;

import java.util.List;

public class MasteryHolder implements CustomUIPanelPlugin, CustomVisualDialogDelegate {
    /*todo:
        I have forgotten what I am doing... this is a issue.
        so...
        issues:
            bottom ship display:
                -ships are slightly cut off (at the top)
                - the default ship is not displayed by default.
                    -issues: I assumed I would need a variant file, but I really need a fleetMemberAPI (I think)
                -'odds' value is offset slightly. possible fixes later...?
                -when loaded, normal ship list is not loaded.
            info display:
                -requires more information on the rules of selecting ships, as well as the stats of ships selected.
            button buttons:
                -the 'finished' button should set the relevant memory.
            moving items between displays:
                -works fine
        other requirements:
            right now, the display is very empty. it feels fake. I need to create the correct backgrounds. (like in normal gameplay).
                -see the cargo screen, or the fleet screen. every 'spot' sectioned off by lines, the background is not just back but in the player factions color. little things like that.
        -
        questions:
            1: how do I switch back to the normal dialog plugin from a visual dialog?
            2: how to I make the - # + have the + not be a tab away?
            3: my ships seems to be cut off a little bit. I think its from having two panels next to each other. (one on top of the other). any advice on fixing this mess?
            normal thing information:
                1: larger text box. about 2/5 of the screen?
                2: every 'item' has a box in player color around it. every single one, even the ones that are empty.
                3: every 'menu' (top and bottom) has a 'what this is' tab in the top left.
                4: every 'menu' has a scrollbar.
                5: the inter interface does not cover the inter screen (for some reason...?)
                6: the buttons (conform cancel) are in the bottom right. its also confirm - cancel
     */
    public static Logger log = Global.getLogger(Nano_Thief_Stats.class);
    public static MasteryHolder masteryHolder;
    private CustomPanelAPI panel;
    private InteractionDialogAPI dialog;
    private TooltipMakerAPI tooltip;


    public HeldShipsHolder heldShips;
    public ShipsInFleetHolder fleetShips;
    public MasteryInformationHolder2 infoHolder;
    public Master_FinalButtons finalButtons;
    public MasteryHolder(InteractionDialogAPI dialog){
        this.dialog = dialog;
        float height = Global.getSettings().getScreenHeightPixels();
        float width = Global.getSettings().getScreenWidthPixels();
        dialog.hideTextPanel();
        //dialog.getVisualPanel();
        dialog.showCustomVisualDialog(width,height,this);
        //createOptions(dialog);
    }

    public void createOptions_old(InteractionDialogAPI dialog){
        log.info("version 0.4");
        float height = Global.getSettings().getScreenHeightPixels();
        float width = Global.getSettings().getScreenWidthPixels();
        CustomPanelAPI THE_THING = dialog.getVisualPanel().showCustomPanel(width,height,this);
        TooltipMakerAPI tooltip = THE_THING.createUIElement(width, height, false);
        this.tooltip = tooltip;

        float infoWidth = 200;
        float buttonHeight = 50;
        /*MasterInformationHolder infoTemp2 = new MasterInformationHolder();
        CustomPanelAPI infoTemp = THE_THING.createCustomPanel(infoWidth,height,infoTemp2);
        infoTemp2.createOptions(infoTemp,infoWidth,height);
        UIComponentAPI info = tooltip.addCustom(infoTemp,10);*/
        //info.getPosition().setLocation(0,0);
        //note: need to create the internals here.

        ShipsInFleetHolder fleetTemp2 = new ShipsInFleetHolder();
        CustomPanelAPI fleetTemp = THE_THING.createCustomPanel(width-infoWidth,(height-buttonHeight)/2,fleetTemp2);
        fleetTemp2.createOptions(THE_THING,fleetTemp);
        UIComponentAPI fleet = tooltip.addCustom(fleetTemp,5);


        /*HeldShipsHolder selectedTemp2 = new HeldShipsHolder();
        CustomPanelAPI selectedTemp = THE_THING.createCustomPanel(width-infoWidth,(height-buttonHeight)/2,selectedTemp2);
        selectedTemp2.createOptions(selectedTemp);
        UIComponentAPI selected = tooltip.addCustom(selectedTemp,5);


        Master_FinalButtons bottomButtonsTemp2 = new Master_FinalButtons();
        CustomPanelAPI bottomButtonsTemp = THE_THING.createCustomPanel(width-infoWidth,buttonHeight,bottomButtonsTemp2);
        bottomButtonsTemp2.createOptions(bottomButtonsTemp);
        UIComponentAPI bottomButtons = tooltip.addCustom(bottomButtonsTemp,5);*/
        //fleet.getPosition().rightOfMid(info,5);
        //selected.getPosition().belowMid(fleet,5);
        //bottomButtons.getPosition().belowMid(selected,5);

        THE_THING.addUIElement(tooltip);
        panel.wrapTooltipWithBox(tooltip);

    }
    public void createOptions(CustomPanelAPI panel,TooltipMakerAPI tooltip){
        masteryHolder = this;
        log.info("version 0.5");
        float height = Global.getSettings().getScreenHeightPixels();
        float width = Global.getSettings().getScreenWidthPixels();

        float infoWidth = 200;
        float buttonHeight = 50;
        MasteryInformationHolder2 infoTemp2 = new MasteryInformationHolder2();
        infoHolder = infoTemp2;
        CustomPanelAPI infoTemp = panel.createCustomPanel(infoWidth,height-50,infoTemp2);
        infoTemp2.createOptions(infoTemp,infoWidth,height-50);
        UIComponentAPI info = tooltip.addCustom(infoTemp,10);
        //info.getPosition().setLocation(0,0);
        //note: need to create the internals here.

        ShipsInFleetHolder fleetTemp2 = new ShipsInFleetHolder();
        fleetShips = fleetTemp2;
        CustomPanelAPI fleetTemp = panel.createCustomPanel(width-infoWidth,(height-buttonHeight)/2,fleetTemp2);
        fleetTemp2.createOptions(panel,fleetTemp);
        UIComponentAPI fleet = tooltip.addCustom(fleetTemp,5);


        HeldShipsHolder selectedTemp2 = new HeldShipsHolder();
        heldShips = selectedTemp2;
        CustomPanelAPI selectedTemp = panel.createCustomPanel(width-infoWidth,(height-buttonHeight)/2,selectedTemp2);
        selectedTemp2.createOptions(selectedTemp,width-infoWidth,(height-buttonHeight)/2);
        UIComponentAPI selected = tooltip.addCustom(selectedTemp,5);


        Master_FinalButtons bottomButtonsTemp2 = new Master_FinalButtons();
        finalButtons = bottomButtonsTemp2;
        CustomPanelAPI bottomButtonsTemp = panel.createCustomPanel(width-infoWidth,buttonHeight,bottomButtonsTemp2);
        bottomButtonsTemp2.createOptions(bottomButtonsTemp,width-infoWidth,buttonHeight);
        UIComponentAPI bottomButtons = tooltip.addCustom(bottomButtonsTemp,5);

        fleet.getPosition().rightOfTop(info,5);
        selected.getPosition().belowMid(fleet,5);
        bottomButtons.getPosition().belowMid(selected,5);

        infoHolder.recreate_full();
        //THE_THING.addUIElement(tooltip);
        //panel.wrapTooltipWithBox(tooltip);
        //return THE_THING;
    }
    @Override
    public void positionChanged(PositionAPI position) {

    }

    @Override
    public void renderBelow(float alphaMult) {

    }

    @Override
    public void render(float alphaMult) {

    }
    public void returnToBaseDialog(){
        //dialog.showVisualPanel();
        dialog.showTextPanel();
        dialog.getVisualPanel();
        dialog.dismiss();
    }


    //actions for the delegate?
    DialogCallbacks callbacks;
    @Override
    public void init(CustomPanelAPI panel, DialogCallbacks callbacks) {
        this.panel = panel;
        float height = Global.getSettings().getScreenHeightPixels();
        float width = Global.getSettings().getScreenWidthPixels();
        TooltipMakerAPI tooltip = panel.createUIElement(width,height,false);
        tooltip.getPosition().inTL(0,0);
        createOptions(panel,tooltip);
        panel.addUIElement(tooltip);
        this.callbacks = callbacks;
    }

    @Override
    public CustomUIPanelPlugin getCustomPanelPlugin() {
        //WHAT IS THIS??!?!?!?!?!?
        return null;
    }

    @Override
    public float getNoiseAlpha() {
        return 0;
    }

    @Override
    public void advance(float amount) {

    }

    @Override
    public void reportDismissed(int option) {

    }

    @Override
    public void processInput(List<InputEventAPI> events) {

    }

    @Override
    public void buttonPressed(Object buttonId) {

    }
}
