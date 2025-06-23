package Abyssal_XO.data.scripts.threat.AI;

import Abyssal_XO.data.scripts.threat.listiners.NanoThief_ShipStats;
import com.fs.starfarer.api.combat.ShipAIConfig;
import com.fs.starfarer.api.combat.ShipAIPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipwideAIFlags;

public class Nano_Thief_AI_SawrmSpawner implements ShipAIPlugin {
    private ShipAPI ship;
    private ShipAPI motherShip;
    private NanoThief_ShipStats stats;
    public Nano_Thief_AI_SawrmSpawner(ShipAPI ship,ShipAPI motherShip, String wing, NanoThief_ShipStats stats){
        this.ship = ship;
        this.motherShip = motherShip;
        this.stats = stats;
    }
    @Override
    public void setDoNotFireDelay(float amount) {

    }

    @Override
    public void forceCircumstanceEvaluation() {

    }

    @Override
    public void advance(float amount) {
        ship.setFixedLocation(motherShip.getLocation());
    }

    @Override
    public boolean needsRefit() {
        return false;
    }

    @Override
    public ShipwideAIFlags getAIFlags() {
        return null;
    }

    @Override
    public void cancelCurrentManeuver() {

    }

    @Override
    public ShipAIConfig getConfig() {
        return null;
    }
}
