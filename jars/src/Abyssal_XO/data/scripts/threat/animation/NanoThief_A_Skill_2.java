package Abyssal_XO.data.scripts.threat.animation;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_2;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.combat.WeaponAPI;
import com.fs.starfarer.api.input.InputEventAPI;

import java.awt.*;
import java.util.List;

public class NanoThief_A_Skill_2 implements EveryFrameCombatPlugin {
    private WeaponAPI weapon;
    private float time;
    private float maxTime;
    //private static final Color color = new Color(130,155,145,255);
    private static final Color color = new Color(65,75,70,255);
    public NanoThief_A_Skill_2(WeaponAPI b, float time){
        this.weapon = b;
        this.time = time;
        this.maxTime = time;
        Global.getCombatEngine().addPlugin(this);
    }
    @Override
    public void processInputPreCoreControls(float amount, List<InputEventAPI> events) {

    }

    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        time-=amount;
        animate();
        if (time <= 0 || weapon.getShip() == null || weapon.getShip().isHulk() || !weapon.getShip().isAlive()) endAnimation();
    }
    public void animate(){
        //Settings.log.info("("+weapon.getSpec().getWeaponId()+") got glow amount as: "+((time/maxTime)* NanoThief_2.animationIntensity)+" from time/max, intensity: "+time+"/"+maxTime+", "+NanoThief_2.animationIntensity);
        //weapon.
        weapon.setGlowAmount((time/maxTime)* NanoThief_2.animationIntensity,color);
        //0.75 / 10 = 0.75
        //0.
    }
    public void endAnimation(){
        weapon.setGlowAmount(0,color);
        Global.getCombatEngine().removePlugin(this);
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
