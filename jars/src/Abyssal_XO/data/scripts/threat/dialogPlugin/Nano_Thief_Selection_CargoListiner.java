package Abyssal_XO.data.scripts.threat.dialogPlugin;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoPickerListener;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.apache.log4j.Logger;

import java.util.List;

public class Nano_Thief_Selection_CargoListiner implements CargoPickerListener {
    private static Logger log = Global.getLogger(Nano_Thief_dialog.class);
    static CargoAPI playerFighters;
    static CargoAPI selectedFighters;
    private static String oldFighter;
    private static String currentFighter;
    private static CargoAPI backupCargo;
    public static CargoAPI prepareForSelection(){
        oldFighter = null;
        currentFighter = null;
        playerFighters = Global.getSector().getPlayerFleet().getCargo();
        playerFighters = playerFighters.createCopy();
        List<CargoAPI.CargoItemQuantity<String>> a = playerFighters.getFighters();
        playerFighters.clear();
        for (CargoAPI.CargoItemQuantity<String> b : a){
            FighterWingSpecAPI spec = Global.getSettings().getFighterWingSpec(b.getItem());
            if (spec.getRange() >= Settings.NANO_THIEF_MINRANGEOFWING) {
                playerFighters.addFighters(b.getItem(), 1);
            }
        }
        selectedFighters = null;
        backupCargo = Global.getSector().getPlayerFleet().getCargo().createCopy();
        return playerFighters;
    }
    private boolean hasFighterInStorge = true;
    public void prepareSelectedFighter(CargoAPI cargo){
        oldFighter = null;
        selectedFighters = cargo;
        String customFighter = Settings.NANO_THIEF_PALYER_BASEWING;//"talon_wing";
        if(Global.getSector().getPlayerPerson().getMemory().contains(Settings.NANO_THIEF_CUSTOM_WING_MEMORY_KEY)) {
            if (Global.getSector().getPlayerPerson().getMemory().getString(Settings.NANO_THIEF_CUSTOM_WING_MEMORY_KEY).equals("null")){

            }else {
                customFighter = Global.getSector().getPlayerPerson().getMemory().getString(Settings.NANO_THIEF_CUSTOM_WING_MEMORY_KEY);
                cargo.addFighters(customFighter, 1);
                oldFighter = customFighter;
                currentFighter = customFighter;
                playerFighters.removeFighters(currentFighter, 1);
                return;
            }
        }
        hasFighterInStorge = false;
    }
    public Nano_Thief_Selection_CargoListiner(){
    }
    @Override
    public void pickedCargo(CargoAPI cargo) {
        //log.info("this was a 'picked cargo'.");
        Nano_Thief_dialog.dialog.dismiss();
    }

    @Override
    public void cancelledCargoSelection() {
        //log.info("this was considered a canal");

        CargoAPI playerTemp = Global.getSector().getPlayerFleet().getCargo();
        playerTemp.clear();
        playerTemp.addAll(backupCargo);
        if (oldFighter != null) {
            Global.getSector().getPlayerPerson().getMemory().set(Settings.NANO_THIEF_CUSTOM_WING_MEMORY_KEY, oldFighter);//getString(Settings.NANO_THIEF_CUSTOM_WING_MEMORY_KEY);
        }else{
            Global.getSector().getPlayerPerson().getMemory().set(Settings.NANO_THIEF_CUSTOM_WING_MEMORY_KEY,"null");
            Global.getSector().getPlayerPerson().getMemory().expire(Settings.NANO_THIEF_CUSTOM_WING_MEMORY_KEY,-1);
            Global.getSector().getPlayerPerson().getMemory().unset(Settings.NANO_THIEF_CUSTOM_WING_MEMORY_KEY);//getString(Settings.NANO_THIEF_CUSTOM_WING_MEMORY_KEY);
        }
        Nano_Thief_dialog.dialog.dismiss();
    }
    @Override
    public void recreateTextPanel(TooltipMakerAPI panel, CargoAPI cargo, CargoStackAPI pickedUp, boolean pickedUpFromSource, CargoAPI combined) {
        if (selectedFighters == null) prepareSelectedFighter(cargo);
        if (cargo.getFighters().isEmpty() && hasFighterInStorge){
            hasFighterInStorge = false;
            setNewFighter(cargo,pickedUp,null,currentFighter);
        }
        if (!hasFighterInStorge && cargo.getFighters().size() == 1){
            hasFighterInStorge = true;
            setNewFighter(cargo,pickedUp,cargo.getFighters().get(0).getItem(),null);
        }
        if (cargo.getFighters().size() == 1 && !cargo.getFighters().get(0).getItem().equals(currentFighter)){
            setNewFighter(cargo,pickedUp,cargo.getFighters().get(0).getItem(),currentFighter);
        }
        /*if (pickedUp != null) {
            //pickedUp.getCargo().getFighters().clear();
            pickedUp.subtract(9999);
            pickedUp.setType(null);
            //pickedUp.setPickedUp(false);
        }*/
        /*if (pickedUp != null && !pickedUp.getCargo().getFighters().get(0).getItem().equals(currentFighter)){
            String temp = pickedUp.getCargo().getFighters().get(0).getItem();
            pickedUp.getCargo().clear();
            setNewFighter(cargo,pickedUp,temp,currentFighter);
            selectedFighters.addFighters(temp,1);
        }*/
        while (cargo.getFighters().size() > 1){
            for (CargoAPI.CargoItemQuantity<String> a : cargo.getFighters()) {
                if (!a.getItem().equals(currentFighter)) {
                    setNewFighter(cargo, pickedUp, a.getItem(), currentFighter);
                    break;
                }
            }

        }
        /*if (){
            if (cargo.getFighters().size() > 1) {
                cargo.removeFighters(currentFighter, 1);
                playerFighters.addFighters(currentFighter, 1);
            }
            currentFighter = cargo.getFighters().get(0).getItem();
            setNewFighter();
        }*/
        if (pickedUp != null){
            if (!pickedUpFromSource){
            }else{

            }
        }
        //cargo.addCommodity("crew",200);
        panel.addPara("the inserted fighter LCP is set as your Simulacrum Fighter Wing. the fighter LCP will not be available until you replace it with another one.",5);
        panel.addPara("if nothing is selected, talon wings will be selected for deployment",5);
        panel.addPara("The rules for base stats (before skill modifications) is as follows:",5);
        panel.addPara("Reclaim cost is %s per ordnance point + %s",5, Misc.getTextColor(), Misc.getHighlightColor(),""+(int)Settings.NANO_THIEF_CustomSwarm_COST_PEROP,""+(int)Settings.NANO_THIEF_CustomSwarm_COST_BASE);
        panel.addPara("Build time is wing size * replacement rate * %s",5,Misc.getTextColor(), Misc.getHighlightColor(),""+Settings.NANO_THIEF_CustomSwarm_BUILDTIME_PREREFIT);
        panel.addPara("Time to live is %s for bombers, and %s for everything else",5,Misc.getTextColor(), Misc.getHighlightColor(),""+(int)Settings.NANO_THIEF_CustomSwarm_Bomber_TTL,""+(int)Settings.NANO_THIEF_CustomSwarm_TTL);
        applyStatsPanel(panel);

        //dialog.dismiss();
    }
    private void applyStatsPanel(TooltipMakerAPI panel){
        String fighterID = currentFighter;
        if (currentFighter == null) fighterID = "talon_wing";
        String sprite = Global.getSettings().getFighterWingSpec(fighterID).getVariant().getHullSpec().getSpriteName();
        FighterWingSpecAPI fighterSpec = Global.getSettings().getFighterWingSpec(fighterID);
        //panel.addImageWithText(5);
        panel.addPara("",5);
        panel.addPara(fighterSpec.getWingName(),5);
        panel.addImage(sprite,5);
        Nano_Thief_Stats.displayStatsForFighterWithoutModification(panel,fighterSpec);
        //panel.addImage("very good stats.",5);
    }
    private void setNewFighter(CargoAPI cargo,CargoStackAPI pickedUp,String newFTemp,String oldFTemp){
        //if (pickedUp != null)pickedUp.getCargo().clear();
        if (oldFTemp != null) {
            cargo.removeFighters(oldFTemp, 9999999);
            if (!cargo.getFighters().isEmpty()) {

                /*boolean alreadyHas = false;
                for (CargoAPI.CargoItemQuantity<String> a : playerFighters.getFighters()){
                    if (a.getItem().equals(oldFTemp)){
                        alreadyHas = true;
                        break;
                    }
                }
                if (pickedUp != null && !alreadyHas){
                    for (CargoAPI.CargoItemQuantity<String> a : pickedUp.getCargo().getFighters()){
                        if (a.getItem().equals(oldFTemp)){
                            alreadyHas = true;
                            break;
                        }
                    }
                }*/
                //if (!alreadyHas) {
                    playerFighters.addFighters(oldFTemp, 1);
                    Global.getSector().getPlayerFleet().getCargo().addFighters(oldFTemp, 1);
                //}
            }
        }
        if (newFTemp != null) {
            Global.getSector().getPlayerFleet().getCargo().removeFighters(newFTemp,1);
            currentFighter = newFTemp;
            Global.getSector().getPlayerPerson().getMemory().set(Settings.NANO_THIEF_CUSTOM_WING_MEMORY_KEY, currentFighter);
        }else{
            currentFighter = "talon_wing";
            Global.getSector().getPlayerPerson().getMemory().set(Settings.NANO_THIEF_CUSTOM_WING_MEMORY_KEY, currentFighter);
        }
    }
}
