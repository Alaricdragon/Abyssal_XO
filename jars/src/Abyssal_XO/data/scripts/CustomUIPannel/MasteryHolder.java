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
        float height = Global.getSettings().getScreenHeightPixels();
        float width = Global.getSettings().getScreenWidthPixels();
        this.dialog = dialog;
        CustomPanelAPI THE_THING = dialog.getVisualPanel().showCustomPanel(width,height,this);
        //setting the position of my data to the top left.
        THE_THING.getPosition().inTL(0,0);
        //getting a tooltip from my interface (this is were I get buttons. I have used tooltips before. this could fucking work lets goooo)
        TooltipMakerAPI tooltip = THE_THING.createUIElement(width, height, false);
        //adding a button, and getting the ability to manipulate its position!
        //tooltip.addButton("AAAAAA","ButtonData",5,5,1).getPosition();
        //tooltip.addCustom(THE_THING,1);
        this.panel = THE_THING;
        this.tooltip = tooltip;

        float infoWidth = 200;
        float buttonHeight = 50;
        //UIComponentAPI info = tooltip.addCustom(THE_THING.createCustomPanel(height,infoWidth,new MasterInformationHolder(THE_THING,dialog,tooltip)),5);
        log.info("stage 1");
        ShipsInFleetHolder fleetTemp2 = new ShipsInFleetHolder();
        log.info("stage 2");
        CustomPanelAPI fleetTemp = THE_THING.createCustomPanel(width-infoWidth,(height-buttonHeight)/2,fleetTemp2);
        log.info("stage 3");
        fleetTemp2.createOptions(fleetTemp,dialog);
        log.info("stage 4");

        UIComponentAPI fleet = tooltip.addCustom(fleetTemp,5);
        log.info("compleat");

        /*
        UIComponentAPI selected = tooltip.addCustom(THE_THING.createCustomPanel(width-infoWidth,(height-buttonHeight)/2,new HeldShipsHolder(THE_THING,dialog,tooltip)),5);

        UIComponentAPI bottomButtons = tooltip.addCustom(THE_THING.createCustomPanel(width-infoWidth,buttonHeight,new Master_FinalButtons(THE_THING,dialog,tooltip)),5);

        fleet.getPosition().rightOfTop(info,5);
        selected.getPosition().belowMid(fleet,5);
        bottomButtons.getPosition().belowMid(selected,5);*/

        THE_THING.addUIElement(tooltip);

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
