package Abyssal_XO.data.scripts.CustomUIPannel;

import Abyssal_XO.data.scripts.Settings;
import com.fs.starfarer.api.campaign.BaseCustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.CustomPanelAPI;
import com.fs.starfarer.api.ui.PositionAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.ui.UIComponentAPI;
import com.fs.starfarer.api.util.Misc;

import java.util.List;

public class MasteryInformationHolder2 implements CustomUIPanelPlugin {
    protected CustomPanelAPI root;
    UIComponentAPI panelThing;
    protected CustomPanelAPI panel;
    protected TooltipMakerAPI tooltip;

    public void createOptions(CustomPanelAPI panel,float width, float height){
        //to be ran the first time this is created.
        this.root = panel;
        panel = root.createCustomPanel(width,height,new BaseCustomUIPanelPlugin());
        panelThing = panel;
        root.addComponent(panel).setLocation(0,0);

        //createOptionsAfterRoot(panel,width,height);
    }
    private void createOptionsAfterRoot(CustomPanelAPI panel,float width, float height){
        //this.root = panel;
        //panel = root.createCustomPanel(width,height,new BaseCustomUIPanelPlugin());
        //panelThing = panel;
        //root.addComponent(panel).setLocation(0,0);
        TooltipMakerAPI tooltip = panel.createUIElement(width,height,true);
        tooltip.getPosition().setLocation(0,0);
        //tooltip.addPara("putting the ship here lol",5);
        //ShipVariantAPI ship = Global.getSettings().getVariant("legion_Escort");
        //Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy().get(0);
        this.tooltip = tooltip;
        try{
            MasteryHolder.log.info("doing display...");
            createDisplay();
        }catch (Exception e){
            MasteryHolder.log.info("doing error display....");
            createErrorDisplay(e);
        }
        panel.addUIElement(tooltip);
    }
    private void createDisplay(){
        int selected = MasteryHolder.masteryHolder.heldShips.getSelectedNumber();
        tooltip.addPara("%s / %s ships selected",5,selected > Settings.NANO_THIEF_MASTERY_maxShips ? Misc.getNegativeHighlightColor() : Misc.getHighlightColor(),""+selected,""+Settings.NANO_THIEF_MASTERY_maxShips);
        //flasher = tooltip.getPrev();
        if (selected > Settings.NANO_THIEF_MASTERY_maxShips) tooltip.addPara("cannot save ships, because you are over capacity.",5,Misc.getNegativeHighlightColor());
        tooltip.addPara("randomness: "+ (Math.random()*2555),5);
    }
    //private UIComponentAPI flasher;
    //public void flashMaxShips(){
    //    flasher;
    //}
    private void createErrorDisplay(Exception e){
        tooltip.addPara(""+e,5);
    }
    public void recreate_full(){
        //to be ran each time a ship is added.
        root.removeComponent(panel);

        panel = root.createCustomPanel(root.getPosition().getWidth(),root.getPosition().getHeight(),new BaseCustomUIPanelPlugin());
        panelThing = panel;
        root.addComponent(panel).setLocation(0,0);

        createOptionsAfterRoot(panel,root.getPosition().getWidth(),root.getPosition().getHeight());
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
    public MasteryInformationHolder2(){
    }
    /*
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

    }*/
}
