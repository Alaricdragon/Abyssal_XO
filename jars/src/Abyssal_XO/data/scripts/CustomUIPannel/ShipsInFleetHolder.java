package Abyssal_XO.data.scripts.CustomUIPannel;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static Abyssal_XO.data.scripts.threat.skills.NanoThief_10.*;

public class ShipsInFleetHolder implements CustomUIPanelPlugin {
    public void note(){
        /*position.belowMid(new ButtonAPI() {},5);
        * is that usefull??A?!?!?!?!?*/

    }
    private SpriteAPI sprite;
    boolean setOptions = false;
    public CustomPanelAPI panel;
    public CustomPanelAPI master;
    private TooltipMakerAPI tooltip;
    public void createOptions(CustomPanelAPI master,CustomPanelAPI panel){
        MasteryHolder.log.info("    runing create options...");
        this.panel = panel;
        this.master = master;
        this.tooltip = panel.createUIElement(panel.getPosition().getWidth(),panel.getPosition().getHeight(),true);
        tooltip.getPosition().setLocation(0,0);
        MasteryHolder.log.info("    created UI element...");

        tooltip.createLabel("HEADER",Misc.getTextColor());
        //todo: I can do a lot with this header. so thats cool.

        //ButtonAPI pTemp = tooltip.addButton("","",0,150,2);
        UIComponentAPI last_a = null;//tooltip.getPrev();//pTemp;
        UIComponentAPI last_b;
        UIComponentAPI last_c = null;

        MasteryHolder.log.info("    added button...");

        int size = (int) (panel.getPosition().getWidth() / 100);
        int at = 0;
        for (int a = 0; a < activeMembers.size(); a++){
            FleetMemberAPI ship = activeMembers.get(a);
            //last_b = addSingleShip_asCompoment(ship,a);
            Pair<UIComponentAPI, UIComponentAPI> z = addSingleShip_Working(ship,a);
            last_b = z.one;
            //if (at == 0) {
            //    last_b.getPosition().belowMid(last_a,1);
            //    last_c = z.two;
            //}else
            if (at % size == 0){
                if (last_c != null) last_b.getPosition().belowMid(last_c,1);
                last_c = z.two;
            }else if (last_a != null) last_b.getPosition().rightOfMid(last_a,1);
            last_a = last_b;
            at++;
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
    private boolean isApplicable(FleetMemberAPI ship){
        return  switch (ship.getHullSpec().getHullSize()){
            case DEFAULT, FIGHTER, FRIGATE -> canFrigate;
            case DESTROYER -> canDestroyer;
            case CRUISER -> canCruiser;
            case CAPITAL_SHIP -> canCapital;
        };
    }
    /*private UIComponentAPI addSingleShip_asCompoment(FleetMemberAPI ship, int idInFleet){
        return Mastery_HeldShip_Single.createItem(master,tooltip,ship,idInFleet,100,50);

    }*/
    private Pair<UIComponentAPI,UIComponentAPI> addSingleShip_Working(FleetMemberAPI ship, int idInFleet){
        ArrayList<FleetMemberAPI> ships = new ArrayList<>();
        UIComponentAPI labal0;
        UIComponentAPI labal1;
        UIComponentAPI labal2;
        ships.add(ship);
        tooltip.addShipList(1,1,100, Misc.getBasePlayerColor(),ships,10);
        labal0 = tooltip.getPrev();
        labal1 = labal0;

        tooltip.addPara(ship.getShipName(),1);
        labal2 = tooltip.getPrev();
        labal2.getPosition().setSize(100,26);
        labal2.getPosition().belowMid(labal1,2);
        labal1 = labal2;

        tooltip.addPara(ship.getVariant().getDisplayName(),1);
        labal2 = tooltip.getPrev();
        labal2.getPosition().setSize(100,26);
        labal2.getPosition().belowMid(labal1,5);
        labal1 = labal2;

        ButtonAPI but = tooltip.addButton("Select Ship","add:"+idInFleet,100,50,1);
        but.getPosition().belowMid(labal1,5);

        //tooltip.addComponent(panel.createCustomPanel(100,100,new DisplayShip()));

        return new Pair<>(labal0,but);

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
    private ArrayList<FleetMemberAPI> activeMembers;
    public ShipsInFleetHolder(){
        //sprite = Global.getSettings().getSprite("graphics/ships/wolf/wolf_base.png");
        activeMembers = new ArrayList<>();
        List<FleetMemberAPI> fleetList = Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy();
        for (int a = 0; a < fleetList.size(); a++) {
            FleetMemberAPI ship = fleetList.get(a);
            if (isApplicable(ship)) activeMembers.add(ship);
        }
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
        String[] data = ((String) buttonId).split(":");
        FleetMemberAPI fleetmember = activeMembers.get(Integer.parseInt(data[1]));
        MasteryHolder.masteryHolder.heldShips.toAdd = new HeldShipsSingleShipData(fleetmember,10,fleetmember.getShipName(),fleetmember.getVariant().getDisplayName());
        MasteryHolder.masteryHolder.heldShips.recreate_full();
        MasteryHolder.masteryHolder.infoHolder.recreate_full();
        MasteryHolder.log.info("button pressed in: ShipsInFleetHolder");
    }
}