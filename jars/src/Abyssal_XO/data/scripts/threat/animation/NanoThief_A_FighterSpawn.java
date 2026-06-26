package Abyssal_XO.data.scripts.threat.animation;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_SawrmSpawner;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.EveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ViewportAPI;
import com.fs.starfarer.api.impl.combat.threat.FragmentSwarmHullmod;
import com.fs.starfarer.api.impl.combat.threat.RoilingSwarmEffect;
import com.fs.starfarer.api.input.InputEventAPI;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.List;

public class NanoThief_A_FighterSpawn implements EveryFrameCombatPlugin {
    private ShipAPI ship;
    private static final float maxTime = 3;
    private float time = 0;

    private int initalMembers;
    private int members;
    RoilingSwarmEffect swarm;
    private int cost;
    public NanoThief_A_FighterSpawn(ShipAPI ship, Nano_Thief_AI_SawrmSpawner spawner){
        this.ship = ship;
        cost = (int) spawner.cost_per_fighter;
        initalMembers = (cost / 10) + 20;
        //members = initalMembers;
        RoilingSwarmEffect existing = RoilingSwarmEffect.getSwarmFor(ship);
        if (existing != null){
            time = maxTime;
            return;
        }
        RoilingSwarmEffect swarm = FragmentSwarmHullmod.createSwarmFor(ship);
        RoilingSwarmEffect.getFlockingMap().remove(swarm.getParams().flockingClass, swarm);
        swarm.getParams().flockingClass = FragmentSwarmHullmod.RECLAMATION_SWARM_FLOCKING_CLASS;
        RoilingSwarmEffect.getFlockingMap().add(swarm.getParams().flockingClass, swarm);
        swarm.getParams().memberExchangeClass = FragmentSwarmHullmod.RECLAMATION_SWARM_EXCHANGE_CLASS;
        swarm.getParams().removeMembersAboveMaintainLevel = true;
        swarm.getParams().maxOffset = ship.getHullSpec().getCollisionRadius() / 2;
        swarm.getParams().maxDespawnTime = 0.1f;
        //swarm.getParams().

        this.swarm = swarm;
        setSwarmSize(initalMembers);
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
    private void setSwarmSize(int size){
        swarm.getParams().initialMembers = size;
        swarm.getParams().baseMembersToMaintain = size;
        swarm.getParams().maxNumMembersToAlwaysRemoveAbove = size;
        members = size;
    }

    private static final Color jitterColor = new Color(130,155,145,55);
    private static final Color jitterUnderColor = new Color(130,155,145,155);
    private void animate(ShipAPI ship,float intensity){
        Logger log = Global.getLogger(Nano_Thief_Stats.class);
        log.info("applying fighter animation at "+intensity+" intensity");
        if (members != (int)(intensity * initalMembers)) setSwarmSize((int) (intensity * initalMembers));
        //ship.setAlphaMult((1-intensity));
        /*ship.setJitter(Settings.DISPLAYID_NANOTHIEF+"_skill_4", jitterColor, 1, (int)Math.max(2*intensity,1), 0f, 5);
        ship.setJitterUnder(Settings.DISPLAYID_NANOTHIEF+"_skill_4", jitterUnderColor, 1, (int)Math.max(25*intensity,1), 0f, 7);
        for (ShipAPI b : ship.getChildModulesCopy()){
            animate(b,intensity);
        }*/
    }
    private void endAnimation(ShipAPI ship){
        //ship.setAlphaMult(1);
        swarm.getParams().baseMembersToMaintain = 0;
        swarm.setForceDespawn(true);
        /*for (ShipAPI b : ship.getChildModulesCopy()){
            endAnimation(b);
        }*/
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
