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
            HashMap<Integer, SCData> map = (HashMap<Integer, SCData>) Global.getCombatEngine().getCustomData().get("SiC_SCDataMap");
            SCData data = getSCData(a);
            if (data == null) return;
            map.put(a.getOriginalOwner(),data);
            Global.getCombatEngine().getCustomData().put("SiC_SCDataMap",map);
            addModules(a,data);
            //log?.info("     HERE: finished already has hullmod")
        }else if (isValidShipToConvert(a)){
            //log?.info("     HERE: got need to add hullmod")
            HashMap<Integer,SCData> map = (HashMap<Integer, SCData>) Global.getCombatEngine().getCustomData().get("SiC_SCDataMap");
            int force = a.getOriginalOwner();
            SCData data = null;
            if (map.containsKey(force)) data = map.get(force);
            if (data == null) return;
            refitShip(a,data);
            addModules(a,data);
            //log?.info("     HERE: finished need to add hullmod")
        }
        //log?.info(" isValidShipToConvert: is valid ship");
        //log?.info(" isValidShipToConvert: got data from other source?"+(data != null));
        //log?.info(" isValidShipToConvert: has item in map. is data null: "+(map.get(force)==null));
    }
}
