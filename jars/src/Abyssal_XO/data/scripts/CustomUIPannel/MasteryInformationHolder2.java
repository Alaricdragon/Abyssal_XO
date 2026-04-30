package Abyssal_XO.data.scripts.CustomUIPannel;

import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.CustomPanelAPI;
import com.fs.starfarer.api.ui.PositionAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.ui.UIComponentAPI;

import java.util.List;

public class MasteryInformationHolder2 implements CustomUIPanelPlugin {
    public MasteryInformationHolder2(){
    }
    public CustomPanelAPI create(CustomPanelAPI panel, TooltipMakerAPI tooltip, float width, float height){
        //MasteryInformationHolder2 infoTemp2 = new MasteryInformationHolder2();
        CustomPanelAPI infoTemp = panel.createCustomPanel(width,height,this);
        createOptions(infoTemp,width,height);
        UIComponentAPI info = tooltip.addCustom(infoTemp,0);//info == this. but not really required?
        this.panel = infoTemp;
        //complement = info;
        createOptions(this.panel,width,height);
        return infoTemp;
    }
    //UIComponentAPI complement;
    CustomPanelAPI panel;
    public void createOptions(CustomPanelAPI panel, float width, float height){
        TooltipMakerAPI tooltip = panel.createUIElement(width,height,true);
        tooltip.getPosition().setLocation(0,0);

        tooltip.addPara("aaaaaaaa"+((int)(Math.random()*2222)),5);

        panel.addUIElement(tooltip);
        //recalculateDisplay(panel,null);
        //not yet....
        /*TooltipMakerAPI tooltip = panel.createUIElement(panel.getPosition().getWidth(),panel.getPosition().getHeight(),true);
        tooltip.getPosition().setLocation(0,0);
        tooltip.addPara("Information about mastery here",5, Misc.getTextColor(),Misc.getHighlightColor());
        tooltip.addPara("cost equal some eq of %s * by %s",5, Misc.getTextColor(),Misc.getHighlightColor(),"200","5");
        tooltip.addPara("smods get more time of something of %s",5, Misc.getTextColor(),Misc.getHighlightColor(),"15");
        panel.addUIElement(tooltip);*/
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
