package Abyssal_XO.data.scripts.CustomUIPannel;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.*;
import org.apache.log4j.Logger;

import java.util.List;

public class MasteryHolder implements CustomUIPanelPlugin {
    public static Logger log = Global.getLogger(Nano_Thief_Stats.class);
    public static MasteryHolder masteryHolder;
    private CustomPanelAPI panel;
    private InteractionDialogAPI dialog;
    private TooltipMakerAPI tooltip;
    public MasteryHolder(InteractionDialogAPI dialog){
        createOptions(dialog);
    }
    public void createOptions(InteractionDialogAPI dialog){
        log.info("version 0.4");
        float height = Global.getSettings().getScreenHeightPixels();
        float width = Global.getSettings().getScreenWidthPixels();
        this.dialog = dialog;
        CustomPanelAPI THE_THING = dialog.getVisualPanel().showCustomPanel(width,height,this);
        //THE_THING.getPosition().setLocation(0,0);
        //setting the position of my data to the top left.
        //THE_THING.getPosition().inTL(0,50);
        //getting a tooltip from my interface (this is were I get buttons. I have used tooltips before. this could fucking work lets goooo)
        TooltipMakerAPI tooltip = THE_THING.createUIElement(width, height, false);
        this.panel = THE_THING;
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
    @Override
    public void positionChanged(PositionAPI position) {

    }

    @Override
    public void renderBelow(float alphaMult) {

    }

    @Override
    public void render(float alphaMult) {

    }

    @Override
    public void advance(float amount) {

    }

    @Override
    public void processInput(List<InputEventAPI> events) {

    }

    @Override
    public void buttonPressed(Object buttonId) {

    }
}
