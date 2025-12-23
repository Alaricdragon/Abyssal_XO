package Abyssal_XO.data.scripts.CustomUIPannel;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.ButtonAPI;
import com.fs.starfarer.api.ui.PositionAPI;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

public abstract class Button implements CustomUIPanelPlugin {
    private float mouseX, mouseY;
    //int x,y;
    //final int width,height;
    private PositionAPI p;
    /*public Button(int width,int height){
        //this.width = width;
        //this.height = height;
    }*/

    public Color buttonBaseColor(){
        return Global.getSettings().getBasePlayerColor();
    }
    public Color buttonBgColor(){
        return Global.getSettings().getDarkPlayerColor();
    }
    @Override
    public void positionChanged(PositionAPI position) {
        p = position;
        mouseX = p.getX() + p.getWidth() / 2f;
        mouseY = p.getY() + p.getHeight() / 2f;

    }

    @Override
    public void renderBelow(float alphaMult) {
        float x = p.getX();
        float y = p.getY();
        float w = p.getWidth();
        float h = p.getHeight();

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        Color color = buttonBaseColor();

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
    }

    @Override
    public void render(float alphaMult) {

    }

    @Override
    public void advance(float amount) {

    }

    @Override
    public void processInput(List<InputEventAPI> events) {
        if (p == null) return;
        for (InputEventAPI event : events) {
            if (event.isConsumed()) continue;
            if (event.isMouseDownEvent()) {
                if (p.containsEvent(event)) {
                    //log.info("pressed button????");
                    mouseX = event.getX();
                    mouseY = event.getY();
                    //System.out.println("x,y: " + x + "," + y);
                }
            }
        }
    }
    public abstract void pressed();
    @Override
    public void buttonPressed(Object buttonId) {
    }
}
