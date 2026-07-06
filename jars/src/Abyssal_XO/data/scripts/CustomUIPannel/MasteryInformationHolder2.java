package Abyssal_XO.data.scripts.CustomUIPannel;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_10;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_6;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_MasteryShipStats;
import com.fs.starfarer.api.campaign.BaseCustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.CustomPanelAPI;
import com.fs.starfarer.api.ui.PositionAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.ui.UIComponentAPI;
import com.fs.starfarer.api.util.Misc;

import java.util.List;

import static Abyssal_XO.data.scripts.threat.skills.NanoThief_10.maxShips;

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
            //MasteryHolder.log.info("doing display...");
            createDisplay();
        }catch (Exception e){
            MasteryHolder.log.info("doing error display....");
            createErrorDisplay(e);
        }
        panel.addUIElement(tooltip);
    }
    private void createDisplay(){
        //insental data (number of ships over capacity and simaler.
        int selected = MasteryHolder.masteryHolder.heldShips.getSelectedNumber();
        tooltip.addPara("%s / %s ships selected",5,selected > maxShips ? Misc.getNegativeHighlightColor() : Misc.getHighlightColor(),""+selected,""+maxShips);
        if (selected > maxShips) tooltip.addPara("cannot save ships, because you are over capacity.",5,Misc.getNegativeHighlightColor());
        //tooltip.addPara("randomness: "+ (Math.random()*2555),5);

        //description:
        //copy the basic description here?
        tooltip.addPara("select your Simulacrum Ships here",5);
        tooltip.addPara("if nothing is selected, a pirate kite raider will be selected for deployment",5);
        tooltip.addPara("the rules for simulacrum ships are as follows:",5);
        tooltip.addPara("Reclaim cost is %s + %s per deployment point + (base cost * s-mods * %s) - (base cost * d-mods * %s)"+NanoThief_10.addDOHSIfRequired(NanoThief_10.baseCosts,NanoThief_10.costPerDPs),5, Misc.getTextColor(), Misc.getHighlightColor(),NanoThief_10.sizeStringSingleAsInts(NanoThief_10.baseCosts,"/",""),NanoThief_10.sizeStringSingleAsInts(NanoThief_10.costPerDPs,"/",""),""+NanoThief_10.sModCost,""+NanoThief_10.dModDiscount);
        tooltip.addPara("Were d-mods only includes combat effecting d-mods, and is caped to %s of the base reclaim cost",5, Misc.getTextColor(), Misc.getHighlightColor(),((int) (NanoThief_10.dModmin*100)) + "%");
        tooltip.addPara("Build time is %s + %s per deployment point"+NanoThief_10.addDOHSIfRequired(NanoThief_10.builtTimeBases,NanoThief_10.buildTimePerDPs),5,Misc.getTextColor(), Misc.getHighlightColor(),NanoThief_10.sizeStringSingleAsDoubles(NanoThief_10.builtTimeBases,"/",""),NanoThief_10.sizeStringSingleAsDoubles(NanoThief_10.buildTimePerDPs,"/",""));
        tooltip.addPara("Time to recharge construction is %s + %s per deployment point"+NanoThief_10.addDOHSIfRequired(NanoThief_10.rechargeTimeBases,NanoThief_10.rechargeTimePerDPs),5,Misc.getTextColor(), Misc.getHighlightColor(),NanoThief_10.sizeStringSingleAsInts(NanoThief_10.rechargeTimeBases,"/",""),NanoThief_10.sizeStringSingleAsInts(NanoThief_10.rechargeTimePerDPs,"/",""));
        tooltip.addPara("Loes %s peak performance time",5,Misc.getTextColor(), Misc.getHighlightColor(),(100-(int)(NanoThief_10.peakCRDuration*100)) + "%");
        //basic rules here (reclaim cost, build time, max numbers of ships selected.)

        //ship data:
        tooltip.addPara("selected ship stats:",5,Misc.getHighlightColor());
        for (Mastery_HeldShip_Single a : MasteryHolder.masteryHolder.heldShips.heldShips){
            NanoThief_MasteryShipStats b = new NanoThief_MasteryShipStats(a.ship,a.chance,a.shipNameForced.getText());
            NanoThief_10.displayShipStatsStright(tooltip,b,50);
        }
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
