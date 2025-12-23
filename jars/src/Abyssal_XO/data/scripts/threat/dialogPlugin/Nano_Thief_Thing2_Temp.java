package Abyssal_XO.data.scripts.threat.dialogPlugin;

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

public abstract class Nano_Thief_Thing2_Temp implements CustomUIPanelPlugin {
     //this was build by someone named 'not Me' on discord. thanks 'not Me'.

    /*
    so things to note:
        1: there is a 'button pressed' function. in fact, this intier function seems to be a button?
           but how does it work? there is no object detection. nothing. How does it work!?!??


    */
    private static Logger log = Global.getLogger(Abyssal_XO.data.scripts.threat.dialogPlugin.Nano_Thief_ThingTemp.class);
    public Color buttonBaseColor(){
        return Global.getSettings().getBasePlayerColor();
    }
    public Color buttonBgColor(){
        return Global.getSettings().getDarkPlayerColor();
    }
    public abstract float pad();

    public abstract float width();
    public abstract float height();


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

    public Nano_Thief_Thing2_Temp init() {
        log.info("running init on button thing?");
        CustomPanelAPI p1 = Global.getSettings().createCustom(0,0, this);
        setRoot(p1);
        skip();
        root.getPosition().setSize(width(), height());
        return this;
    }

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
    public abstract void renderBelow(float alphaMult);

    @Override
    public abstract void render(float alphaMult);

    public abstract void buildTooltip(CustomPanelAPI panel, TooltipMakerAPI tooltip);

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
                public void processInput(java.util.List<InputEventAPI> events) {
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

    public abstract void advancePostCreation(float amount);

    @Override
    public void processInput(List<InputEventAPI> events){

    }

    @Override
    public abstract void buttonPressed(Object buttonId);


    public abstract void send(Object... param);

}