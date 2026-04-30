package Abyssal_XO.data.scripts.CustomUIPannel;

import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.Sys;

import java.util.List;

public class MasterInformationHolder implements CustomUIPanelPlugin {
    private int maxShips = 4;
    public MasterInformationHolder(){

    }
    public void createOptions(CustomPanelAPI panel,float width, float height){
        this.panel = panel;
        //recalculateDisplay(panel,null);
        //not yet....
        TooltipMakerAPI tooltip = panel.createUIElement(panel.getPosition().getWidth(),panel.getPosition().getHeight(),true);
        tooltip.getPosition().setLocation(0,0);
        this.tooltip = tooltip;
        /*
        tooltip.addPara("Information about mastery here",5, Misc.getTextColor(),Misc.getHighlightColor());
        tooltip.addPara("cost equal some eq of %s * by %s",5, Misc.getTextColor(),Misc.getHighlightColor(),"200","5");
        tooltip.addPara("smods get more time of something of %s",5, Misc.getTextColor(),Misc.getHighlightColor(),"15");
        */
        panel.addUIElement(tooltip);
    }
    private CustomPanelAPI panel;
    private TooltipMakerAPI tooltip;
    public void recalculateDisplay(){
        recalculateDisplay(panel,tooltip);
    }
    public void recalculateDisplay(CustomPanelAPI panel,TooltipMakerAPI tooltip){
        /* todo:
            my plan has the following steps:
                1: copy the way the HeldShipsHolder works were it can 'recalculate' itself by having a 'root' panel, and a secondary panel that actualy does things.
                ....
                WHY IT NOT WORK?!?!?

         */
        if (compoment != null) panel.removeComponent(compoment);
        //tooltip = panel.createUIElement(panel.getPosition().getWidth(),panel.getPosition().getHeight(),true);
        //this.tooltip = tooltip;
        //tooltip.getPosition().setLocation(0,0);

        MasteryInformationHolder2 thing = new MasteryInformationHolder2();
        compoment = thing.create(panel,tooltip,panel.getPosition().getWidth(),panel.getPosition().getHeight());

        panel.addUIElement(tooltip);
    }
    UIComponentAPI compoment = null;
    public void recalculateDisplay_OLD(CustomPanelAPI panel,TooltipMakerAPI tooltip){
        //todo: instead of everything I am doing right now, instead just like.... maybe have a sub system inside of the MasterInformationHolder, that holds its own panel with its own text panel?
        if (tooltip != null) {
            /*UIComponentAPI a = tooltip.getPrev();
            while(a != null){
                MasteryHolder.log.info("removing another item from tooltip (or trying to): "+a.toString());
                tooltip.removeComponent(a);
                a = tooltip.getPrev();
            }*/
            panel.removeComponent(tooltip);
        }
        tooltip = panel.createUIElement(panel.getPosition().getWidth(),panel.getPosition().getHeight(),true);
        this.tooltip = tooltip;
        tooltip.getPosition().setLocation(0,0);

        int currentShipCount = MasteryHolder.masteryHolder.heldShips.getSelectedNumber();
        LabelAPI thing = tooltip.addPara("currently have %s out of %s selected ships",5,currentShipCount > maxShips ? Misc.getNegativeHighlightColor() : Misc.getTextColor(),Misc.getHighlightColor(),""+currentShipCount,""+maxShips);
        /*thing.setText("fuck you areareaerrea");
        thing.setHighlightColor(Misc.getHighlightColor());
        thing.highlightFirst("you");
        thing.setHighlightColor(Misc.getNegativeHighlightColor());
        thing.highlightFirst("fuck");*/
        tooltip.addPara("Information about mastery here",5, Misc.getTextColor(),Misc.getHighlightColor());
        tooltip.addPara("cost equal some eq of %s * by %s",5, Misc.getTextColor(),Misc.getHighlightColor(),"200","5");
        tooltip.addPara("smods get more time of something of %s",5, Misc.getTextColor(),Misc.getHighlightColor(),"15");
        panel.addUIElement(tooltip);
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
        MasteryHolder.log.info("button pressed in: MasteryInformationHolder");
    }
}
