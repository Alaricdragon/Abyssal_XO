package Abyssal_XO.data.scripts.threat.dialogPlugin;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.combat.EngagementResultAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipHullSpecAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.codex.CodexDataV2;
import com.fs.starfarer.api.ui.Alignment;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.ui.UIPanelAPI;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

public class Nano_Thief_dialog implements InteractionDialogPlugin {
    private static Logger log = Global.getLogger(Nano_Thief_dialog.class);
    public void init(){
        //Global.getSector().getCampaignUI().
    }
    public static InteractionDialogAPI dialog;
    @Override
    public void init(InteractionDialogAPI dialog) {
        this.dialog = dialog;
        attemptShowCargo(dialog);
        //dialog.getOptionPanel().addOption("exit","exit");

        //dialog.get
    }
    public void attemptShowCargo(InteractionDialogAPI dialog){
        dialog.showCargoPickerDialog("cargo","Conferm", "Cancal",true,400,Nano_Thief_Selection_CargoListiner.prepareForSelection(),new Nano_Thief_Selection_CargoListiner());
        /*dialog.showCargoPickerDialog("cargo", "confermSelection", "cancalSelection", true, 400, Global.getSector().getPlayerFleet().getCargo(), new CargoPickerListener() {
            @Override
            public void pickedCargo(CargoAPI cargo) {
            }

            @Override
            public void cancelledCargoSelection() {
            }

            @Override
            public void recreateTextPanel(TooltipMakerAPI panel, CargoAPI cargo, CargoStackAPI pickedUp, boolean pickedUpFromSource, CargoAPI combined) {
                dialog.dismiss();
            }
        });
        */
        //info.showCargo();
    }
    public void attemptThing2(InteractionDialogAPI dialog){
        log.info("attempting to add images of number: "+Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy().size());
        TooltipMakerAPI info = dialog.getTextPanel().beginTooltip();
        for (FleetMemberAPI a : Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy()) {
            log.info("looking at ship of name: "+a.getHullSpec().getHullName());
            //info.addButton("selectShip","HAHAHAHAHA",20,12,5);
            info.beginImageWithText(a.getHullSpec().getSpriteName(), 5, 200, true);
            //info.addPara(a.getShipName(),5);
            //info.addImageWithText(5);
            dialog.getTextPanel().addImage(a.getHullSpec().getSpriteName());
        }
        dialog.getTextPanel().addTooltip();

    }
    public void attemptShipThing(InteractionDialogAPI dialog){
        int pad = 10;
        TooltipMakerAPI info = dialog.getTextPanel().beginTooltip();
        info.beginImageWithText("",5,200,true);
        info.addImageWithText(pad);

        Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy().get(0).getHullSpec();
        ArrayList<ShipHullSpecAPI> hullList = new ArrayList<>();
        for (FleetMemberAPI a : Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy()){
            hullList.add(a.getHullSpec());
        }
        UIPanelAPI firstItem = null;
        UIPanelAPI prevItem = null;
        UIPanelAPI newItem = null;
        Map<ShipHullSpecAPI,Integer> hullIdQtyMap = new LinkedHashMap<>();
        for (ShipHullSpecAPI ship : hullList) {
            TooltipMakerAPI image = info.beginImageWithText(ship.getSpriteName(),40,40,false);
            if (firstItem == null) {
                firstItem = info.addImageWithText(pad);;
                prevItem = firstItem;
            }
            else
            {
                newItem = info.addImageWithText(pad);
                newItem.getPosition().rightOfTop(prevItem, pad);
                prevItem = newItem;;
            }

           // String tooltipStr = (int) Math.ceil((hullIdQtyMap.get(ship) / totalChance) * 30f)  + " x " + ship.getNameWithDesignationWithDashClass();
            //info.addTooltipToPrevious( new ToolTip(200, ship.getHullName(), Color.WHITE, CodexDataV2.getShipEntryId(ship.getHullId())), TooltipMakerAPI.TooltipLocation.LEFT);

            info.addPara(ship.getHullName(),pad);
            info.setCodexEntryId(CodexDataV2.getShipEntryId(ship.getHullId()));
        }
        info.beginImageWithText("",5,200,true);
        newItem = info.addImageWithText(pad);
        newItem.getPosition().belowLeft(firstItem, pad);
    }
    public void addOptionsForAllShips(){
        TooltipMakerAPI tooltip = dialog.getTextPanel().beginTooltip();
        tooltip.addSectionHeading("the things", Alignment.MID,10);
        //tooltip.addIconGroup();
    }
    public void addOptionForFighter(ShipAPI shipAPI){

    }
    @Override
    public void optionSelected(String optionText, Object optionData) {
        if (optionData.equals("exit") || optionData.equals("Cancal")){
            dialog.dismiss();
        }
    }

    @Override
    public void optionMousedOver(String optionText, Object optionData) {

    }

    @Override
    public void advance(float amount) {

    }

    @Override
    public void backFromEngagement(EngagementResultAPI battleResult) {

    }

    @Override
    public Object getContext() {
        return null;
    }

    @Override
    public Map<String, MemoryAPI> getMemoryMap() {
        return Map.of();
    }
}
