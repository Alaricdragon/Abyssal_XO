package Abyssal_XO.data.scripts.CustomUIPannel;

import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.CustomPanelAPI;
import com.fs.starfarer.api.ui.PositionAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.util.List;

public class MasterInformationHolder implements CustomUIPanelPlugin {
    private int maxShips = 4;
    public MasterInformationHolder(){

    }
    public void createOptions(CustomPanelAPI panel,float width, float height){
        this.panel = panel;
        //recalculateDisplay(panel,null);
        //not yet....
        /*TooltipMakerAPI tooltip = panel.createUIElement(panel.getPosition().getWidth(),panel.getPosition().getHeight(),true);
        tooltip.getPosition().setLocation(0,0);
        tooltip.addPara("Information about mastery here",5, Misc.getTextColor(),Misc.getHighlightColor());
        tooltip.addPara("cost equal some eq of %s * by %s",5, Misc.getTextColor(),Misc.getHighlightColor(),"200","5");
        tooltip.addPara("smods get more time of something of %s",5, Misc.getTextColor(),Misc.getHighlightColor(),"15");
        panel.addUIElement(tooltip);*/
    }
    private CustomPanelAPI panel;
    private TooltipMakerAPI tooltip;
    public void recalculateDisplay(){
        recalculateDisplay(panel,tooltip);
    }
    public void recalculateDisplay(CustomPanelAPI panel,TooltipMakerAPI tooltip){
        //todo: instead of removing the component, I really need to just remove the contents. this is not working....
        if (tooltip != null) panel.removeComponent(tooltip);
        tooltip = panel.createUIElement(panel.getPosition().getWidth(),panel.getPosition().getHeight(),true);
        this.tooltip = tooltip;
        tooltip.getPosition().setLocation(0,0);

        int currentShipCount = MasteryHolder.masteryHolder.heldShips.getSelectedNumber();
        tooltip.addPara("currently have %s out of %s selected ships",5,currentShipCount > maxShips ? Misc.getNegativeHighlightColor() : Misc.getTextColor(),Misc.getHighlightColor(),""+currentShipCount,""+maxShips);
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
