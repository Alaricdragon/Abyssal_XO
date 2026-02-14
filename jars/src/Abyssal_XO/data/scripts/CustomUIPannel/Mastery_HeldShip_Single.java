package Abyssal_XO.data.scripts.CustomUIPannel;

import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.Misc;

import java.util.ArrayList;
import java.util.List;

public class Mastery_HeldShip_Single implements CustomUIPanelPlugin {
    /*so, the plan here:
    * 1: I need to 'remember' both the 'number text' and the '+ button'.
    * 2: when + or - button is pressed, I need to restructure this to update the odds below the ship? maybe?
    *   -altunitive: have the odds displayed in the info tab and simply restructure that each time.
    *
    * bugs:
    *   1: right now, the ship is not displaying at the 'right' position. fixes required.*/
    private boolean changed = false;
    public int chance;
    private int totalChance;

    private TooltipMakerAPI tooltip;
    public FleetMemberAPI ship;
    private float shipSize;
    private float buttonHeight;

    private LabelAPI odds;
    private UIComponentAPI addB;
    private UIComponentAPI reduceB;

    public UIComponentAPI thisCompoment;

    public Mastery_HeldShip_Single(int chance, int totalChance){
        this.chance = chance;
        this.totalChance = totalChance;
    }
    public static Mastery_HeldShip_Single createItem(CustomPanelAPI masterPanel, TooltipMakerAPI holderTooltip, FleetMemberAPI ship, int idInFleet, float shipSize, float buttonHieght, int odds, int totalOdds){
        Mastery_HeldShip_Single fleetTemp2 = new Mastery_HeldShip_Single(odds,totalOdds);
        CustomPanelAPI fleetTemp = masterPanel.createCustomPanel(shipSize,shipSize+(buttonHieght*2),fleetTemp2);

        TooltipMakerAPI tooltip = fleetTemp.createUIElement(shipSize,shipSize+(buttonHieght*2),false);
        fleetTemp2.tooltip = tooltip;
        fleetTemp2.buttonHeight = buttonHieght;
        fleetTemp2.shipSize = shipSize;
        fleetTemp2.ship = ship;

        tooltip.getPosition().setLocation(0,0);
        fleetTemp2.createOptions(tooltip,ship,shipSize,buttonHieght);
        fleetTemp.addUIElement(tooltip);

        UIComponentAPI fleet = holderTooltip.addCustom(fleetTemp,5);
        fleetTemp2.thisCompoment = fleet;
        return fleetTemp2;
    }
    public void createOptions(TooltipMakerAPI tooltip,FleetMemberAPI ship, float shipSize, float buttonSize){
        //panel.createUIElement(0,0,false);

        ArrayList<FleetMemberAPI> ships = new ArrayList<>();
        ships.add(ship);
        tooltip.addShipList(1,1,shipSize, Misc.getBasePlayerColor(),ships,0);
        UIComponentAPI labal = tooltip.getPrev();

        ButtonAPI but = tooltip.addButton("remove Ship","remove",shipSize,buttonSize,1);
        //labal.getPosition().aboveMid(but,1);
        ButtonAPI butMin = tooltip.addButton("-","decrease",shipSize/3,buttonSize,1);
        odds = tooltip.addPara(chance+"/"+totalChance,1);//value of this ship / total number of ships.
        //maybe have this as a percentage chance?
        //or maybe just have the odds here...
        UIComponentAPI textLabel = tooltip.getPrev();
        //tooltip.removeComponent(textLabel);
        ButtonAPI butAdd = tooltip.addButton("+","increase",shipSize/3,buttonSize,1);

        but.getPosition().belowMid(labal,1);
        butMin.getPosition().belowLeft(but,1);
        textLabel.getPosition().rightOfMid(butMin,1);
        butAdd.getPosition().rightOfMid(textLabel,1);

        //odds = textLabel;
        reduceB = butMin;
        addB = butAdd;

    }
    public void recreate(int chance, int totalChance){
        MasteryHolder.log.info("checking if change required... (chance, old chance, totalchance, oldtotal, force change):"+chance+", "+this.chance+", "+totalChance+", "+this.totalChance+", "+changed);
        if (chance != this.chance || totalChance != this.totalChance || changed){

            recreateChanceText(chance, totalChance);
            //clear();
            this.chance = chance;
            this.totalChance = totalChance;
            this.changed = false;
            //createOptions(tooltip,ship,shipSize,buttonHeight);
        }
    }
    public void recreateChanceText(int chance, int totalChance){
        odds.setText(chance+"/"+totalChance);
    }
    public int getOdds(){
        return this.chance;
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
        switch ((String) buttonId){
            case "remove":
                MasteryHolder.log.info("running button single ship -> remove");
                MasteryHolder.masteryHolder.heldShips.toRemove.add(this);
                MasteryHolder.masteryHolder.heldShips.recreate_full();
                break;
            case "decrease":
                MasteryHolder.log.info("running button single ship -> decrease");
                if (chance <= 1) break;
                chance--;
                changed = true;
                MasteryHolder.masteryHolder.heldShips.recreate();
                break;
            case "increase":
                MasteryHolder.log.info("running button single ship -> increase");
                if (chance >= 20) break;
                chance++;
                changed = true;
                MasteryHolder.masteryHolder.heldShips.recreate();
                break;
        }
    }
}
