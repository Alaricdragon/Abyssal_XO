package Abyssal_XO.data.scripts.threat.listiners;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_ShipSkills;
import com.fs.starfarer.api.GameState;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import second_in_command.SCData;

public class NanoThief_ShipSkillsAdder implements AdvanceableListener {
    private ShipAPI ship;
    private SCData data;
    public NanoThief_ShipSkillsAdder(ShipAPI ship, SCData data){
        this.ship = ship;
        this.data = data;
    }
    @Override
    public void advance(float amount) {
        if (Global.getCurrentState().equals(GameState.CAMPAIGN) || Global.getCurrentState().equals(GameState.TITLE)) return;
        if (Global.getCombatEngine().isCombatOver()) return;
        if (!Global.getCombatEngine().isInEngine(ship)) return;
        if (!(Global.getCombatEngine().hasPluginOfClass(NanoThief_BattleListener.class))) return;
        if (NanoThief_BattleListener.getHostileCaptions().isEmpty() && NanoThief_BattleListener.getFriendlyCaptions().isEmpty()) return;
        if (ship.hasListenerOfClass(NanoThief_ShipSkills.class)) return; //this should never happen.
        Nano_Thief_Stats stats = NanoThief_BattleListener.getStatsForShip(ship,data);
        if (stats == null){
            return;
        }
        stats.getAvailableShips().put(ship.getId(),ship);
        //stats.deployedDP+=ship;
        ship.addListener(new NanoThief_ShipSkills(stats,ship));
        ship.removeListener(this);
    }
}
