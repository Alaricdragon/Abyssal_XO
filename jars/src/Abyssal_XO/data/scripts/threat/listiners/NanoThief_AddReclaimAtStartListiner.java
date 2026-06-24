package Abyssal_XO.data.scripts.threat.listiners;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_ShipSkills;
import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;

/// this is used because things like the construction script are not added immanently on ship creation, to my frustration
public class NanoThief_AddReclaimAtStartListiner implements AdvanceableListener {
    int reclaim;
    Nano_Thief_Stats stats;
    NanoThief_ShipSkills shipSkills;
    ShipAPI ship;
    float timer = 1f;
    public NanoThief_AddReclaimAtStartListiner(ShipAPI ship, int reclaim, Nano_Thief_Stats stats, NanoThief_ShipSkills shipSkills){
        this.reclaim = reclaim;
        this.stats = stats;
        this.shipSkills = shipSkills;
        this.ship = ship;
    }
    @Override
    public void advance(float amount) {
        if (Global.getCurrentState().equals(GameState.CAMPAIGN) ||
                Global.getCurrentState().equals(GameState.TITLE) ||
                Global.getCombatEngine().isCombatOver() ||
                !Global.getCombatEngine().isInEngine(ship)
        ){
            Settings.log.info("failed to add starting reclaim because something went wrong...?");
            ship.removeListener(this);
            return;
        }
        timer -= amount;
        Settings.log.info("got timer to get reclaim as: "+timer);
        if (!stats.isValidReclaimTarget(ship)){
            ship.removeListener(this);
            Settings.log.info("ship of name: "+ship.getName()+" is invalid reclaim target. not adding starting reclaim");
        }
        if (timer <= 0){
            Settings.log.info("ship of name "+ship.getName()+" got "+reclaim+" inital reclaim");
            shipSkills.addReclaim(reclaim);
            ship.removeListener(this);
        }
    }
}
