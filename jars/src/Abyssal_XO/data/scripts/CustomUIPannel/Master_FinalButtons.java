package Abyssal_XO.data.scripts.CustomUIPannel;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.dialogPlugin.Nano_Thief_dialog;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.ButtonAPI;
import com.fs.starfarer.api.ui.CustomPanelAPI;
import com.fs.starfarer.api.ui.PositionAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import jdk.jfr.EventType;
import net.java.games.input.Component;
import net.java.games.input.Controller;
import org.lwjgl.input.Keyboard;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class Master_FinalButtons implements CustomUIPanelPlugin {
    public ButtonAPI finish;
    public Master_FinalButtons(){
    }
    public void createOptions(CustomPanelAPI panel,float width, float height){
        TooltipMakerAPI tooltip = panel.createUIElement(width,height,false);

        tooltip.getPosition().setLocation(0,0);
        ButtonAPI exit = tooltip.addButton("Cancel","exit",100,50,10);
        exit.setShortcut(Keyboard.KEY_ESCAPE,true);
        ButtonAPI finish = tooltip.addButton("Accept","finish",100,50,10);
        finish.setShortcut(Keyboard.KEY_RETURN,true);
        finish.getPosition().rightOfMid(exit,5);

        panel.addUIElement(tooltip);
        this.finish = finish;

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
        if (events.isEmpty()) return;
        //MasteryHolder.log.info("getting event....");
        for (InputEventAPI a : events){
            //a.getEventChar();
            //InputEvent.
            //a.getEventChar() == Component.Identifier.Key.ESCAPE;
            //if (a.isControlDownEvent(InputEvent.)){

            //}
        }
        //MasteryHolder.log.info("failed to get shifted.");
    }

    @Override
    public void buttonPressed(Object buttonId) {
        MasteryHolder.log.info("button pressed in: FinalButtons");
        switch ((String) buttonId){
            case "exit":
                MasteryHolder.log.info("running exit code");
                MasteryHolder.masteryHolder.returnToBaseDialog();
                //panel.removeComponent(tooltip);
                break;
            case "finish":
                MasteryHolder.log.info("running finished code");
                MasteryHolder.masteryHolder.returnToBaseDialog();
                ArrayList<Integer> numbers = new ArrayList<>();
                ArrayList<FleetMemberAPI> variants = new ArrayList<>();
                ArrayList<String> names = new ArrayList<>();
                for (Mastery_HeldShip_Single a : MasteryHolder.masteryHolder.heldShips.heldShips){
                    numbers.add(a.chance);
                    //a.ship;
                    FleetMemberAPI existingMember = a.ship;
                    //FleetMemberType.SHIP;
                    FleetMemberAPI memberCopy = Global.getSettings().createFleetMember(existingMember.getType(), existingMember.getVariant().clone());
                    variants.add(memberCopy);
                    names.add(a.shipNameForced.getText());
                }
                Global.getSector().getPlayerPerson().getMemory().set(Settings.NANO_THIEF_CUSTOM_MASTERY_NUMBERS_MEMORY_KEY,numbers);
                Global.getSector().getPlayerPerson().getMemory().set(Settings.NANO_THIEF_CUSTOM_MASTERY_MEMORY_KEY,variants);
                Global.getSector().getPlayerPerson().getMemory().set(Settings.NANO_THIEF_CUSTOM_MASTERY_NAMES_MEMORY_KEY,names);
                break;
        }
    }
}
