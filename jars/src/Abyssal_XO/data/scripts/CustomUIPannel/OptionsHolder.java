package Abyssal_XO.data.scripts.CustomUIPannel;

import Abyssal_XO.data.scripts.threat.dialogPlugin.Nano_Thief_dialog;
import ashlib.data.plugins.info.FighterInfoGenerator;
import ashlib.data.plugins.info.ShipInfoGenerator;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.Misc;

import java.util.List;

public class OptionsHolder implements CustomUIPanelPlugin {
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

    private CustomPanelAPI panel;
    private InteractionDialogAPI dialog;
    private TooltipMakerAPI tooltip;
    public void createOptions(CustomPanelAPI panel, InteractionDialogAPI dialog, TooltipMakerAPI tooltip){
        this.panel = panel;
        this.dialog = dialog;
        this.tooltip = tooltip;
        ButtonAPI pTemp = tooltip.addButton("return","exit",100,50,1);
        List<FleetMemberAPI> fleetList = Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy();
        UIComponentAPI last_a = pTemp;
        UIComponentAPI last_b;

        for (int a = 0; a < 1; a++){//fleetList.size(); a++){
            FleetMemberAPI ship = fleetList.get(a);
            last_b = addSingleShip(ship,a);
            last_b.getPosition().rightOfMid(last_a,1);
            last_a = last_b;
        }
        //tooltip.addTable("",0,1);
        //tooltip.beginTable(Misc.getBasePlayerColor(),Misc.getDarkPlayerColor(),Misc.getBrightPlayerColor(),100,true,false,);
        //tooltip.addImage(ship.getSpriteOverride(),100,100,5);
        //tooltip;
        //tooltip;
        //tooltip.addImageWithText(1);
    }
    private UIComponentAPI addSingleShip(FleetMemberAPI ship,int idInFleet){
        /*tooltip.addCustomDoNotSetPosition(new UIComponentAPI() {
            PositionAPI p;
            @Override
            public PositionAPI getPosition() {
                return p;
            }

            @Override
            public void render(float alphaMult) {

            }

            @Override
            public void processInput(List<InputEventAPI> events) {

            }

            @Override
            public void advance(float amount) {

            }

            @Override
            public void setOpacity(float opacity) {

            }

            @Override
            public float getOpacity() {
                return 0;
            }
        });*/
        //tooltip.addComponent(new ShipSprite(sprite,p));
        //FleetMemberAPI.getSpriteOverride()
        TooltipMakerAPI shipHolder = panel.createUIElement(100,100,false);
        //ButtonAPI labal = tooltip.addButton(ship.getShipName(),"",100,50,1);
        LabelAPI labal = ShipInfoGenerator.processShipData(ship.getHullSpec(),shipHolder,false);//how big is this generated
        panel.addUIElement(shipHolder);
        shipHolder.
        //HorizontalTooltipMaker a = new HorizontalTooltipMaker();
        ButtonAPI but = tooltip.addButton("Select Ship","add:"+idInFleet,100,50,1);
        //but.getPosition().belowMid(labal,1);
        labal.getPosition().aboveMid(but,1);
        return shipHolder;

    }
    public OptionsHolder(){
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
        switch ((String) buttonId){
            case "exit":
                panel.removeComponent(tooltip);
                Nano_Thief_dialog.reset();
                break;
        }
    }
}
