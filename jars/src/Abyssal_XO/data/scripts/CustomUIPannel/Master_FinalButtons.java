package Abyssal_XO.data.scripts.CustomUIPannel;

import Abyssal_XO.data.scripts.threat.dialogPlugin.Nano_Thief_dialog;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.ButtonAPI;
import com.fs.starfarer.api.ui.CustomPanelAPI;
import com.fs.starfarer.api.ui.PositionAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;

import java.util.List;

public class Master_FinalButtons implements CustomUIPanelPlugin {
    public Master_FinalButtons(){
    }
    public void createOptions(CustomPanelAPI panel){
        //ButtonAPI finish = tooltip.addButton("Accept","finish",100,50,10);
        //ButtonAPI exit = tooltip.addButton("Cancel","exit",100,50,10);

        //exit.getPosition().leftOfMid(finish,5);

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
            case "exit":
                //panel.removeComponent(tooltip);
                //Nano_Thief_dialog.reset();
                break;
        }
    }
}
