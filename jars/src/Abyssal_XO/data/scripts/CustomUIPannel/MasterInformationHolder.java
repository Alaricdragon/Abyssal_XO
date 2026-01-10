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
    public MasterInformationHolder(){

    }
    public void createOptions(CustomPanelAPI panel,float width, float height){
        TooltipMakerAPI tooltip = panel.createUIElement(panel.getPosition().getWidth(),panel.getPosition().getHeight(),true);
        tooltip.getPosition().setLocation(0,0);
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
