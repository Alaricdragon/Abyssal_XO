package Abyssal_XO.data.scripts.CustomUIPannel;

import Abyssal_XO.data.scripts.threat.dialogPlugin.fuckYou;
import com.fs.graphics.Sprite;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.graphics.SpriteAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.PositionAPI;
import com.fs.starfarer.api.ui.UIComponentAPI;
import lombok.Getter;
import org.apache.log4j.Logger;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;

public class ShipSprite implements UIComponentAPI {
    private static Logger log = Global.getLogger(Abyssal_XO.data.scripts.threat.dialogPlugin.fuckYou.class);
    public PositionAPI p;
    private SpriteAPI sprite;

    private float mouseX, mouseY;

    public ShipSprite(String sprite,PositionAPI p) {
        /*this crashes because the input sprite is wrong. going to just iggnore this for now.*/
        this.sprite = Global.getSettings().getSprite(sprite);
        this.p = p;
    }

    /*public void positionChanged(PositionAPI position) {
        log.info("position changed");
        p = position;
        mouseX = p.getX() + p.getWidth() / 2f;
        mouseY = p.getY() + p.getHeight() / 2f;
    }*/

    public void advance(float amount) {
        if (p == null) return;

    }

    @Override
    public void setOpacity(float opacity) {

    }

    @Override
    public float getOpacity() {
        return 0;
    }

    @Override
        public void processInput(List<InputEventAPI> events) {
            /*log.info("processInput");
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
            }*/
        }

    @Override
    public PositionAPI getPosition() {
        return p;
    }

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
}
