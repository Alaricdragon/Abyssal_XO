package Abyssal_XO.data.scripts.CustomUIPannel;

import Abyssal_XO.data.scripts.threat.dialogPlugin.Nano_Thief_dialog;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.Misc;

import java.util.ArrayList;
import java.util.List;

public class ShipsInFleetHolder implements CustomUIPanelPlugin {
    /*notes:
        1: getX and getCenterX are diffrent.
            getX is relevant position.
            getCenterX is the true position
        2: when useing a 'PositionAPI' releitive position set functions keep in mind that if you want to unset them, you cant just unset one item if it has any child items. You need to unset them all. one by fucking one.
        3: to add buttons the most striaghtforward way starting out is to call createTooltip on CustomPanelAPI and call addButton on it from there
            -how the hell do I do that????




    */
    /*I am getting nowhere.
    * so: here is what I know
    * 1) this fjnkbasdijlsbv nmfcjwmknbv mas,vbn fc as,fbnvc
    * 2) I -can- render items. I can also get the 'position' of a givin item next to another item.
    * 3) maybe... all I need to do is just... get the relevent position */
    public void note(){
        /*position.belowMid(new ButtonAPI() {},5);
        * is that usefull??A?!?!?!?!?*/

    }
    private SpriteAPI sprite;
    boolean setOptions = false;
    public CustomPanelAPI panel;
    private InteractionDialogAPI dialog;
    private TooltipMakerAPI tooltip;
    public void createOptions(CustomPanelAPI panel,InteractionDialogAPI dialog){
        MasteryHolder.log.info("    runing create options...");
        this.panel = panel;
        this.dialog = dialog;
        this.tooltip = panel.createUIElement(panel.getPosition().getWidth(),panel.getPosition().getHeight(),true);
        MasteryHolder.log.info("    created UI element...");
        List<FleetMemberAPI> fleetList = Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy();

        ButtonAPI pTemp = tooltip.addButton("","",0,100,2);
        UIComponentAPI last_a = pTemp;
        UIComponentAPI last_b;

        MasteryHolder.log.info("    added button...");


        for (int a = 0; a < fleetList.size(); a++){
            FleetMemberAPI ship = fleetList.get(a);
            last_b = addSingleShip_Working(ship,a);
            if (last_a != null) last_b.getPosition().rightOfMid(last_a,1);
            last_a = last_b;
        }
        MasteryHolder.log.info("    finished addding ships...");
        //tooltip.addTable("",0,1);
        //tooltip.beginTable(Misc.getBasePlayerColor(),Misc.getDarkPlayerColor(),Misc.getBrightPlayerColor(),100,true,false,);
        //tooltip.addImage(ship.getSpriteOverride(),100,100,5);
        //tooltip;
        //tooltip;
        //tooltip.addImageWithText(1);

        panel.addUIElement(tooltip);
        MasteryHolder.log.info("    tooltip added...");
    }
    private UIComponentAPI addSingleShip_Working(FleetMemberAPI ship, int idInFleet){
        ArrayList<FleetMemberAPI> ships = new ArrayList<>();
        ships.add(ship);
        tooltip.addShipList(1,1,100, Misc.getBasePlayerColor(),ships,10);
        UIComponentAPI labal = tooltip.getPrev();


        ButtonAPI but = tooltip.addButton("Select Ship","add:"+idInFleet,100,50,1);
        labal.getPosition().aboveMid(but,1);

        //tooltip.addComponent(panel.createCustomPanel(100,100,new DisplayShip()));

        return labal;

    }
    /*private UIComponentAPI addSingleShipAshLibAttempt(FleetMemberAPI ship,int idInFleet){
        //tooltip.addComponent(new ShipSprite(sprite,p));
        //FleetMemberAPI.getSpriteOverride()
        //ButtonAPI labal = tooltip.addButton(ship.getShipName(),"",100,50,1);

        //TooltipMakerAPI shipHolder = panel.createUIElement(100,100,false);
        //LabelAPI labal = ShipInfoGenerator.processShipData(ship.getHullSpec(),shipHolder,false);//how big is this generated
        //panel.addUIElement(shipHolder);
        UIComponentAPI labal = DisplayShip.createShip(panel,dialog,tooltip,ship);
        //so... what does this not work?
        //the best theory I can think of is simple: I need to create a new 'CustomUIPlugin' for this holder? maybe?

        //float width= 990f;
        //CustomPanelAPI panelAPIs = ShipInfoGenerator.getShipImage(ship.getHullSpec(), 100, null).one;
        //ShipInfoGenerator.generate(tooltip, AshMisc.getFleetMemberFromSpec(ship.getHullSpec()),null,panelAPIs,width);


        //shipHolder.
        //HorizontalTooltipMaker a = new HorizontalTooltipMaker();
        ButtonAPI but = tooltip.addButton("Select Ship","add:"+idInFleet,100,50,1);
        //shipHolder.getPosition().aboveMid(but,1);
        //but.getPosition().aboveMid(labal,1);
        labal.getPosition().aboveMid(but,1);

        tooltip.addComponent(labal);
        return labal;

    }*/
    private UIComponentAPI addSingleShip_old(FleetMemberAPI ship, int idInFleet){
        ButtonAPI labal = tooltip.addButton(ship.getShipName(),"",100,50,1);

        ButtonAPI but = tooltip.addButton("Select Ship","add:"+idInFleet,100,50,1);

        labal.getPosition().aboveMid(but,1);
        return labal;
    }
    public ShipsInFleetHolder(){
        //sprite = Global.getSettings().getSprite("graphics/ships/wolf/wolf_base.png");

    }
    public void init(){

    }
    public PositionAPI p;
    //private SpriteAPI sprite;

    //private float mouseX, mouseY;

    //public fuckYou() {
    //    sprite = Global.getSettings().getSprite("graphics/ships/wolf/wolf_base.png");
    //}

    public void positionChanged(PositionAPI position) {
        //log.info("position changed");
        p = position;
        //mouseX = p.getX() + p.getWidth() / 2f;
        //mouseY = p.getY() + p.getHeight() / 2f;
        mouseX = p.getX() + p.getWidth() / 2f;
        mouseY = p.getY() + p.getHeight() / 2f;
    }
    float mouseX;
    float mouseY;

    @Override
    public void renderBelow(float alphaMult) {

    }

    @Override
    public void render(float alphaMult) {
        if (p == null || true) return;
        sprite.setAlphaMult(alphaMult);
        sprite.renderAtCenter(mouseX, mouseY);
        if (!setOptions){

            setOptions = true;
        }
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
/*

//fuck ash lib. I dont like it >=(
//for real, I dont get how this is suppose to be easyer? I am clearly doing something fucking wrong.
class DisplayShip implements CustomUIPanelPlugin {
    public static CustomPanelAPI createShip(CustomPanelAPI panel, InteractionDialogAPI dialog, TooltipMakerAPI tooltip,FleetMemberAPI ship){
        CustomPanelAPI newPannel = panel.createCustomPanel(100,100,new DisplayShip());

        //TooltipMakerAPI shipHolder = newPannel.createUIElement(100,100,false);
        //LabelAPI labal = ShipInfoGenerator.processShipData(ship.getHullSpec(),shipHolder,false);//how big is this generated
        //newPannel.addUIElement(shipHolder);

        TooltipMakerAPI shipHolder = newPannel.createUIElement(100,100,false);
        float width= 990f;
        CustomPanelAPI panelAPIs = ShipInfoGenerator.getShipImage(ship.getHullSpec(), 100, null).one;
        ShipInfoGenerator.generate(shipHolder, AshMisc.getFleetMemberFromSpec(ship.getHullSpec()),null,panelAPIs,width);

        newPannel.addUIElement(shipHolder);

        tooltip.addCustom(newPannel,0);


        return panelAPIs;
    }
    public static CustomPanelAPI createShip_working(CustomPanelAPI panel, InteractionDialogAPI dialog, TooltipMakerAPI tooltip,FleetMemberAPI ship){
        CustomPanelAPI newPannel = panel.createCustomPanel(100,100,new DisplayShip());

        TooltipMakerAPI shipHolder = newPannel.createUIElement(100,100,false);
        LabelAPI labal = ShipInfoGenerator.processShipData(ship.getHullSpec(),shipHolder,false);//how big is this generated
        newPannel.addUIElement(shipHolder);

        tooltip.addCustom(newPannel,0);



        //float width= 990f;
        //CustomPanelAPI panelAPIs = ShipInfoGenerator.getShipImage(ship.getHullSpec(), 100, null).one;
        //ShipInfoGenerator.generate(tooltip, AshMisc.getFleetMemberFromSpec(ship.getHullSpec()),null,panelAPIs,width);


        return newPannel;
    }
    public DisplayShip(){
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
}*/
