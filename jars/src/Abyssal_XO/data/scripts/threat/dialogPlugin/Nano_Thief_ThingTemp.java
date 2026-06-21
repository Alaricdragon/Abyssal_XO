package Abyssal_XO.data.scripts.threat.dialogPlugin;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BaseCustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.CustomPanelAPI;
import com.fs.starfarer.api.ui.PositionAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.List;

public class Nano_Thief_ThingTemp implements CustomUIPanelPlugin {
/*example of how this works:

baseSelfRefreshingPanel p1 = new baseSelfRefreshingPanel(){//new this
//here insert overrides for methods, like buildTooltip and such
};
p1.init();  //this creates the panel internally and makes it work, and ensures it already has the desired size _before_ adding it to parent
tooltip.addCustom(p1.getRoot());


rebuild() is intended to be used for when you need to actually change contents of the panel, in responce to some actions, usually button press, but not always.

the reason for all this is so you can refresh contents of a single panel without messing up anything up above in the hierarchy.
and of course you can nest as many of these as you want

then in buildTooltip you add your buttons (it already presents a ready made tooltip for you to fill)
and in buttonPressed you process those inputs, check button id and such



    */

    private static Logger log = Global.getLogger(Nano_Thief_ThingTemp.class);
    public Color buttonBaseColor(){
        return Global.getSettings().getBasePlayerColor();
    }
    public Color buttonBgColor(){
        return Global.getSettings().getDarkPlayerColor();
    }
    public float pad(){
        return 3f;
    }

    public float width(){
        return 0f;
    }
    public float height(){
        return 0f;
    }


    @Override
    public void positionChanged(PositionAPI position) {
        //what the fuck does this do?
    }
    public void setRoot(CustomPanelAPI panel) {
        this.root = panel;
        rebuild();
    }

    public CustomPanelAPI getRoot() {
        return root;
    }

    /*public Nano_Thief_ThingTemp init() {
        log.info("running init on button thing?");
        CustomPanelAPI p1 = Global.getSettings().createCustom(0,0, this);
        setRoot(p1);
        skip();
        root.getPosition().setSize(width(), height());
        return this;
    }*/

    public void skip(){
        //this self advances.
        for(int i=0; i<3; i++){
            root.advance(1f);
        }
    }
    public CustomPanelAPI root;
    public CustomPanelAPI panel;
    public TooltipMakerAPI tooltip;
    private boolean rebuild = false;

    public void rebuild() {
        rebuild = true;
    }

    @Override
    public void renderBelow(float alphaMult) {
        //??????
    }

    @Override
    public void render(float alphaMult) {
        //render? is this what displays things?
    }

    public void buildTooltip(CustomPanelAPI panel, TooltipMakerAPI tooltip) {
        //what does this do???? am confused.....
        //maybe this is for some type of tooltip? what does that even mean?!!?
        //tooltip;
    }

    @Override
    final public void advance(float amount) {
        log.info("Advance");
        if (root == null) return;
        if (rebuild) {
            try {
                root.removeComponent(panel);
                panel = null;
            } catch (Exception ignore) {
            }
            rebuild = false;
            panel = root.createCustomPanel(1, 1, new BaseCustomUIPanelPlugin() {
                @Override
                public void advance(float amount) {
                    advancePostCreation(amount);
                }
                @Override
                public void processInput(List<InputEventAPI> events) {
                    root.getPlugin().processInput(events);
                }
                @Override
                public void buttonPressed(Object buttonId) {
                    root.getPlugin().buttonPressed(buttonId);
                }
            });
            root.addComponent(panel).inTL(0,0);
            tooltip = panel.createUIElement(width(), height(), false);
            panel.addUIElement(tooltip).inTL(0,0);
            buildTooltip(panel, tooltip);
        }

    }

    public void advancePostCreation(float amount) {
        //I think this is the 'advance' after I run the 'rebuild' function.
    }

    @Override
    public void processInput(List<InputEventAPI> events) {
        //looking at context, I think this is responcable for processing if this is pressed, but... how...?
        //oh fuck me its a full 'button click' data. this is fucked up in the exstream.
        //how the hell do I deal with that?!?!?
    }

    @Override
    public void buttonPressed(Object buttonId) {
        //were is the object detection for buttons being pressed?
        log.info("I PRESSED THE BITTPM");
    }


    public void send(Object... param){

    }

}