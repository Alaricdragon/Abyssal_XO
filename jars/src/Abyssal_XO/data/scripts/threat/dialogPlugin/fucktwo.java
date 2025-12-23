package Abyssal_XO.data.scripts.threat.dialogPlugin;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.*;
import org.apache.log4j.Logger;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

public class fucktwo implements CustomPanelAPI, CustomUIPanelPlugin{
    private static Logger log = Global.getLogger(fuckYou.class);
    public PositionAPI p;
    private SpriteAPI sprite;

    private float mouseX, mouseY;

    public fucktwo() {
        sprite = Global.getSettings().getSprite("graphics/ships/wolf/wolf_base.png");
    }

    public void positionChanged(PositionAPI position) {
        log.info("position changed");
        p = position;
        mouseX = p.getX() + p.getWidth() / 2f;
        mouseY = p.getY() + p.getHeight() / 2f;
    }

    public void advance(float amount) {
        if (p == null) return;
    }

    @Override
    public void processInput(List<InputEventAPI> events) {
        log.info("processInput");
        if (p == null) return;
        for (InputEventAPI event : events) {
            if (event.isConsumed()) continue;

            if (event.isMouseMoveEvent()) {
                if (p.containsEvent(event)) {
                    log.info("pressed button????");
                    mouseX = event.getX();
                    mouseY = event.getY();
                    //System.out.println("x,y: " + x + "," + y);
                } else {
                    mouseX = p.getX() + p.getWidth() / 2f;
                    mouseY = p.getY() + p.getHeight() / 2f;
                }
            }
        }
    }

    @Override
    public void buttonPressed(Object buttonId) {

    }

    @Override
    public void render(float alphaMult) {
        if (p == null) return;
        log.info("render");
        float x = p.getX();
        float y = p.getY();
        float w = p.getWidth();
        float h = p.getHeight();

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Color color = Color.cyan;

        GL11.glColor4ub((byte)color.getRed(),
                (byte)color.getGreen(),
                (byte)color.getBlue(),
                (byte)(color.getAlpha() * alphaMult * 0.25f));

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex2f(x, y);
            GL11.glVertex2f(x, y + h);
            GL11.glVertex2f(x + w, y + h);
            GL11.glVertex2f(x + w, y);
        }
        GL11.glEnd();

//		mouseX = Mouse.getX();
//		mouseY = Mouse.getY();
        sprite.setAlphaMult(alphaMult);
        sprite.renderAtCenter(mouseX, mouseY);

    }

    public void renderBelow(float alphaMult) {

    }

    @Override
    public CustomPanelAPI createCustomPanel(float width, float height, CustomUIPanelPlugin plugin) {
        return null;
    }

    @Override
    public TooltipMakerAPI createUIElement(float width, float height, boolean withScroller) {
        return null;
    }

    @Override
    public PositionAPI addUIElement(TooltipMakerAPI element) {
        return null;
    }

    @Override
    public CustomUIPanelPlugin getPlugin() {
        return null;
    }

    @Override
    public IntelUIAPI getIntelUI() {
        return null;
    }

    @Override
    public UIPanelAPI wrapTooltipWithBox(TooltipMakerAPI tooltip) {
        return null;
    }

    @Override
    public UIPanelAPI wrapTooltipWithBox(TooltipMakerAPI tooltip, Color color) {
        return null;
    }

    @Override
    public UIPanelAPI wrapTooltipWithBox(TooltipMakerAPI tooltip, float padLeft, float padRight, float padBelow, float padAbove, Color color) {
        return null;
    }

    @Override
    public void updateUIElementSizeAndMakeItProcessInput(TooltipMakerAPI element) {

    }

    @Override
    public PositionAPI addComponent(UIComponentAPI custom) {
        return null;
    }

    @Override
    public void removeComponent(UIComponentAPI component) {

    }

    @Override
    public void bringComponentToTop(UIComponentAPI c) {

    }

    @Override
    public void sendToBottom(UIComponentAPI c) {

    }

    @Override
    public PositionAPI getPosition() {
        return null;
    }

    @Override
    public void setOpacity(float opacity) {

    }

    @Override
    public float getOpacity() {
        return 0;
    }
}
