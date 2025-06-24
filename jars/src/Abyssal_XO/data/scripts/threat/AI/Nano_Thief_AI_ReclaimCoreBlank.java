package Abyssal_XO.data.scripts.threat.AI;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import org.lazywizard.lazylib.VectorUtils;

public class Nano_Thief_AI_ReclaimCoreBlank implements ShipAIPlugin {
    private ShipAPI ship;
    private ShipAPI motherShip;
    private CombatEngineAPI engine;
    public Nano_Thief_AI_ReclaimCoreBlank(ShipAPI ship,ShipAPI mothership){
        this.ship = ship;
        this.motherShip = mothership;
        ship.setPullBackFighters(false);
        ship.setParentStation(mothership);
        engine = Global.getCombatEngine();
    }
    @Override
    public void setDoNotFireDelay(float amount) {

    }

    @Override
    public void forceCircumstanceEvaluation() {

    }

    @Override
    public void advance(float amount) {
        float angle = VectorUtils.getAngle(ship.getLocation(),motherShip.getLocation());
        engine.headInDirectionWithoutTurning(ship,angle,999999);
        //ship.dis
        //engine.headInDirectionWithoutTurning();
        //ship.setFixedLocation(motherShip.getLocation());
    }

    @Override
    public boolean needsRefit() {
        return false;
    }

    @Override
    public ShipwideAIFlags getAIFlags() {
        return new ShipwideAIFlags();
    }

    @Override
    public void cancelCurrentManeuver() {

    }

    @Override
    public ShipAIConfig getConfig() {
        return new ShipAIConfig();
    }
}
