package Abyssal_XO.data.scripts.threat.listiners;

import Abyssal_XO.data.scripts.Settings;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import second_in_command.SCData;

import java.util.HashMap;
import java.util.List;

import static Abyssal_XO.data.scripts.threat.listiners.NanoThief_ShipSpawnedListener.*;

public class ShipSpawnedDelayLisinter extends BaseEveryFrameCombatPlugin {
    /*
    ok... so heres the plan:
        stage 1: make it so everything is added to a delayed listiner (however shot I can make it). this is for both the on spawn stage and after spawn stage.
        stage 2: revamp the 'part of my force' eq to include a 'time under player control' unless 'time under player control is
            -TEST: determine how 'time under player control' works.
            -note: make it so ships with a null commander just don't get SCData. This is because null commanders are not... not...
            ....
            I could just make it so fleets with null commanders don't get data? that -MIGHT- work....
            ....
            OK... so... new plan:
            FLEETS WITH NULL COMMANDERS DO NOT GET FLEET DATA.
            that might work. additional testing required.

            a.fleetCommander == Global.getCombatEngine().getFleetManager(a.originalOwner).fleetCommander

    */
    private ShipAPI a;
    public ShipSpawnedDelayLisinter(ShipAPI ship){
        this.a = ship;
    }
    private float amount = 0.1f;
    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        this.amount -= amount;
        //Settings.log.info("got amount of time left as: "+this.amount);
        if (this.amount > 0) return;
        //Settings.log.info("HERE: STARTED ON SHIP DATA STUFF");
        Global.getCombatEngine().removePlugin(this);//destroy this early to avoid major issues.
        //Settings.log.info("HERE: STARTED ON SHIP DATA STUFF PART 2:");
        //log?.info(" HERE: started with name, id, hull: "+a?.name+", "+a?.id+", "+a?.hullSpec?.hullId)
        if (a == null || !isShip(a)) return;
        //log?.info("     HERE: got valid ship")
        if (alreadyReady(a)){
            //log?.info("     HERE: got already has hullmod")
            //HashMap<Integer, SCData> map = (HashMap<Integer, SCData>) Global.getCombatEngine().getCustomData().get("SiC_SCDataMap");
            HashMap<String, SCData> map2 = (HashMap<String, SCData>) Global.getCombatEngine().getCustomData().get("SiC_SCDataMap_2");
            SCData data = getSCData(a);
            if (data == null) return;
            //map.put(a.getOriginalOwner(),data);
            if (a.getFleetCommander() != null) map2.put(a.getFleetCommander().getId(),data);
            //Global.getCombatEngine().getCustomData().put("SiC_SCDataMap",map);
            Global.getCombatEngine().getCustomData().put("SiC_SCDataMap_2",map2);
            addModules(a,data);
            //log?.info("     HERE: finished already has hullmod")
        }else if (isValidShipToConvert(a)){
            //log?.info("     HERE: got need to add hullmod")
            HashMap<Integer,SCData> map = (HashMap<Integer, SCData>) Global.getCombatEngine().getCustomData().get("SiC_SCDataMap");
            HashMap<String,SCData> map2 = (HashMap<String, SCData>) Global.getCombatEngine().getCustomData().get("SiC_SCDataMap_2");
            int force = a.getOriginalOwner();
            SCData data = null;
            if (convertWithFleetCommander(a)){
                if (a.getFleetCommander() != null && map2.containsKey(a.getFleetCommander().getId())) data = map2.get(a.getFleetCommander().getId());
                //log?.info("Attempting to convert as fleet commander data.")
            }else{
                //note: if this somehow does not work, the next best thing is to get the closest ship with fleet data.
                if (map.containsKey(force)) data = map.get(force);
                //log?.info("Attempting to convert without fleet commander data.")
            }
            if (data == null) return;
            refitShip(a,data);
            addModules(a,data);
            //log?.info("     HERE: finished need to add hullmod")
        }
        //log?.info(" isValidShipToConvert: is valid ship");
        //log?.info(" isValidShipToConvert: got data from other source?"+(data != null));
        //log?.info(" isValidShipToConvert: has item in map. is data null: "+(map.get(force)==null));
    }
    private boolean convertWithFleetCommander(ShipAPI a){
        if (a.getFleetMember() != null && a.getFleetMember().getFleetData() != null) return true;
        return false;
    }
}
