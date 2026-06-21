package Abyssal_XO.data.scripts.threat.animation;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import com.fs.starfarer.api.input.InputEventAPI;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.List;

public class NanoThief_A_FighterSpawn implements EveryFrameCombatPlugin {
    private ShipAPI ship;
    private static final float maxTime = 6;
    private float time = 0;
    public NanoThief_A_FighterSpawn(ShipAPI ship){
        this.ship = ship;
    }
    /*@Override
    public void advance(float amount) {
        time+=amount;
        float intensity = maxTime - time;
        if (intensity <= 0){
            endAnimation(ship);
            ship.removeListener(this);
        }
        animate(ship,(maxTime - time)/maxTime);
    }*/
    //130,155,145,255
    private static final Color jitterColor = new Color(130,155,145,55);
    private static final Color jitterUnderColor = new Color(130,155,145,155);
    private void animate(ShipAPI ship,float intensity){
        Logger log = Global.getLogger(Nano_Thief_Stats.class);
        log.info("applying fighter animation at "+intensity+" intensity");
        //ship.setAlphaMult((1-intensity));
        ship.setJitter(Settings.DISPLAYID_NANOTHIEF+"_skill_4", jitterColor, 1, (int)Math.max(2*intensity,1), 0f, 5);
        ship.setJitterUnder(Settings.DISPLAYID_NANOTHIEF+"_skill_4", jitterUnderColor, 1, (int)Math.max(25*intensity,1), 0f, 7);
        for (ShipAPI b : ship.getChildModulesCopy()){
            animate(b,intensity);
        }
    }
    private void endAnimation(ShipAPI ship){
        //ship.setAlphaMult(1);
        for (ShipAPI b : ship.getChildModulesCopy()){
            endAnimation(b);
        }
    }

    @Override
    public void processInputPreCoreControls(float amount, List<InputEventAPI> events) {

    }

    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        time+=amount;
        float intensity = maxTime - time;
        if (intensity <= 0){
            endAnimation(ship);
            //ship.removeListener(this);
            Global.getCombatEngine().removePlugin(this);
        }
        animate(ship,1);//(maxTime - time)/maxTime);
    }

    @Override
    public void renderInWorldCoords(ViewportAPI viewport) {

    }

    @Override
    public void renderInUICoords(ViewportAPI viewport) {

    }

    @Override
    public void init(CombatEngineAPI engine) {

    }
}
