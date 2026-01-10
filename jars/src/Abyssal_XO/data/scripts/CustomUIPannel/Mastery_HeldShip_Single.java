package Abyssal_XO.data.scripts.CustomUIPannel;

import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.Misc;

import java.util.ArrayList;
import java.util.List;

public class Mastery_HeldShip_Single implements CustomUIPanelPlugin {
    private Mastery_HeldShip_Single(){

    }
    public static UIComponentAPI createItem(CustomPanelAPI masterPanel, TooltipMakerAPI holderTooltip, FleetMemberAPI ship, int idInFleet, float shipSize, float buttonHieght){
        Mastery_HeldShip_Single fleetTemp2 = new Mastery_HeldShip_Single();
        CustomPanelAPI fleetTemp = masterPanel.createCustomPanel(shipSize,shipSize+(buttonHieght*2),fleetTemp2);

        TooltipMakerAPI tooltip = fleetTemp.createUIElement(shipSize,shipSize+(buttonHieght*2),false);
        tooltip.getPosition().setLocation(0,0);
        fleetTemp2.createOptions(tooltip,ship,shipSize,buttonHieght);
        fleetTemp.addUIElement(tooltip);

        UIComponentAPI fleet = holderTooltip.addCustom(fleetTemp,5);
        return fleet;
    }
    public void createOptions(TooltipMakerAPI tooltip,FleetMemberAPI ship, float shipSize, float buttonSize){
        //panel.createUIElement(0,0,false);
        ArrayList<FleetMemberAPI> ships = new ArrayList<>();
        ships.add(ship);
        tooltip.addShipList(1,1,shipSize, Misc.getBasePlayerColor(),ships,0);
        UIComponentAPI labal = tooltip.getPrev();

        ButtonAPI but = tooltip.addButton("remove Ship","remove",shipSize,buttonSize,1);
        //labal.getPosition().aboveMid(but,1);
        ButtonAPI butMin = tooltip.addButton("-","decrease:",shipSize/3,buttonSize,1);
        /*LabelAPI text = */tooltip.addPara("odds",1);
        UIComponentAPI textLabel = tooltip.getPrev();
        ButtonAPI butAdd = tooltip.addButton("+","increase:",shipSize/3,buttonSize,1);
        but.getPosition().belowMid(labal,1);
        butMin.getPosition().belowLeft(but,1);
        textLabel.getPosition().rightOfMid(butMin,1);
        butAdd.getPosition().rightOfMid(textLabel,1);

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
        MasteryHolder.log.info("button pressed in: singleShipHolder");
    }
}
