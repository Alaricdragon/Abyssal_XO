package Abyssal_XO.data.scripts.CustomUIPannel;

import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.Misc;

import java.util.ArrayList;
import java.util.List;

public class Mastery_FleetShip_Single implements CustomUIPanelPlugin {
    private Mastery_FleetShip_Single(){

    }
    public static UIComponentAPI createItem(CustomPanelAPI masterPanel,CustomPanelAPI holderPanel, TooltipMakerAPI holderTooltip, FleetMemberAPI ship, int idInFleet, float shipSize, float buttonHieght){
        Mastery_FleetShip_Single fleetTemp2 = new Mastery_FleetShip_Single();
        CustomPanelAPI fleetTemp = masterPanel.createCustomPanel(shipSize,shipSize+buttonHieght,fleetTemp2);

        TooltipMakerAPI tooltip = fleetTemp.createUIElement(shipSize,shipSize+buttonHieght,false);
        fleetTemp2.createOptions(tooltip,ship,idInFleet);
        fleetTemp.addUIElement(tooltip);

        UIComponentAPI fleet = holderTooltip.addCustom(fleetTemp,5);
        return fleet;
    }
    public void createOptions(TooltipMakerAPI tooltip,FleetMemberAPI ship, int idInFleet){
        //panel.createUIElement(0,0,false);
        ArrayList<FleetMemberAPI> ships = new ArrayList<>();
        ships.add(ship);
        tooltip.addShipList(1,1,100, Misc.getBasePlayerColor(),ships,10);
        UIComponentAPI labal = tooltip.getPrev();

        ButtonAPI but = tooltip.addButton("Select Ship","add:"+idInFleet,100,50,1);
        //labal.getPosition().aboveMid(but,1);
        but.getPosition().belowMid(labal,1);

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
