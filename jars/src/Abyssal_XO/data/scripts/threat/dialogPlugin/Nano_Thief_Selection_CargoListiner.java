package Abyssal_XO.data.scripts.threat.dialogPlugin;

import Abyssal_XO.data.scripts.Settings;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.CargoPickerListener;
import com.fs.starfarer.api.campaign.CargoStackAPI;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import org.apache.log4j.Logger;

import java.util.List;

public class Nano_Thief_Selection_CargoListiner implements CargoPickerListener {
    private static Logger log = Global.getLogger(Nano_Thief_dialog.class);
    static CargoAPI playerFighters;
    static CargoAPI selectedFighters;
    private static String oldFighter;
    private static String currentFighter;
    public static CargoAPI prepareForSelection(){
        playerFighters = Global.getSector().getPlayerFleet().getCargo();
        playerFighters = playerFighters.createCopy();
        List<CargoAPI.CargoItemQuantity<String>> a = playerFighters.getFighters();
        playerFighters.clear();
        for (CargoAPI.CargoItemQuantity<String> b : a){
            playerFighters.addFighters(b.getItem(),1);
        }
        selectedFighters = null;
        return playerFighters;
    }
    private boolean hasFighterInStorge = true;
    public void prepareSelectedFighter(CargoAPI cargo){
        oldCargo = cargo.createCopy();
        selectedFighters = cargo;
        String customFighter = "talon_wing";
        if(Global.getSector().getPlayerPerson().getMemory().contains(Settings.NANO_THIEF_CUSTOM_WING_MEMORY_KEY)) {
            customFighter = Global.getSector().getPlayerPerson().getMemory().getString(Settings.NANO_THIEF_CUSTOM_WING_MEMORY_KEY);
            cargo.addFighters(customFighter,1);
            oldFighter = customFighter;
            currentFighter = customFighter;
            playerFighters.removeFighters(currentFighter,1);
            return;
        }
        hasFighterInStorge = false;
    }
    public Nano_Thief_Selection_CargoListiner(){
    }
    @Override
    public void pickedCargo(CargoAPI cargo) {
    }

    @Override
    public void cancelledCargoSelection() {
        log.info("this was considered a canal");
        if (oldFighter != null) {
            Global.getSector().getPlayerPerson().getMemory().set(Settings.NANO_THIEF_CUSTOM_WING_MEMORY_KEY, oldFighter);//getString(Settings.NANO_THIEF_CUSTOM_WING_MEMORY_KEY);
        }else{
            Global.getSector().getPlayerPerson().getMemory().expire(Settings.NANO_THIEF_CUSTOM_WING_MEMORY_KEY,-1);
            Global.getSector().getPlayerPerson().getMemory().unset(Settings.NANO_THIEF_CUSTOM_WING_MEMORY_KEY);//getString(Settings.NANO_THIEF_CUSTOM_WING_MEMORY_KEY);

        }

    }
    private CargoAPI oldCargo;
    @Override
    public void recreateTextPanel(TooltipMakerAPI panel, CargoAPI cargo, CargoStackAPI pickedUp, boolean pickedUpFromSource, CargoAPI combined) {
        if (selectedFighters == null) prepareSelectedFighter(cargo);
        if (cargo.getFighters().isEmpty() && hasFighterInStorge){
            hasFighterInStorge = false;
            setNewFighter(cargo,null,currentFighter);
        }
        if (!hasFighterInStorge && cargo.getFighters().size() == 1){
            hasFighterInStorge = true;
            setNewFighter(cargo,cargo.getFighters().get(0).getItem(),null);
        }
        while (cargo.getFighters().size() > 1){
            setNewFighter(cargo,cargo.getFighters().get(1).getItem(),currentFighter);

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
        panel.addPara("the inserted fighter LCP is set as your replecent fighter. the fighter LCP will not be available untill you replace it with another one.",5);
        panel.addPara("if nothing is selected, talon wings will be selected for deployment",5);
        applyStatsPanel(panel);

        //dialog.dismiss();
    }
    private void applyStatsPanel(TooltipMakerAPI panel){
        String fighterID = currentFighter;
        if (currentFighter == null) fighterID = "talon_wing";
        String sprite = Global.getSettings().getFighterWingSpec(fighterID).getVariant().getHullSpec().getSpriteName();
        FighterWingSpecAPI fighterSpec = Global.getSettings().getFighterWingSpec(fighterID);
        //panel.addImageWithText(5);
        panel.addImage(sprite,5);
        //panel.addImage("very good stats.",5);
        panel.addPara(fighterSpec.getWingName(),5);
    }
    private void setNewFighter(CargoAPI cargo,String newFTemp,String oldFTemp){
        if (oldFTemp != null) {
            cargo.removeFighters(oldFTemp, 1);
            if (!cargo.getFighters().isEmpty()) {
                playerFighters.addFighters(oldFTemp, 1);
                Global.getSector().getPlayerFleet().getCargo().addFighters(oldFTemp, 1);
                //todo: when exiting dialog, find a way to change what fighters are in the players cargo to match what they were at the start of this.
                //because having both the 'add fighters' and 'remove fighters' work with this might be hard. maybe have a 'last removed fighter' data that is reset whenever 'set new fighter' is called?
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
