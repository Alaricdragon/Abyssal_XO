package Abyssal_XO.data.scripts.threat.AI;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.combat.threat.ThreatSwarmAI;
import lombok.Getter;

public class Nano_Thief_AI_ReclaimCombat extends ThreatSwarmAI {
    private Nano_Thief_Stats stats;
    @Getter
    private Nano_Thief_AI_Reclaim oldAI;
    public Nano_Thief_AI_ReclaimCombat(ShipAPI ship, Nano_Thief_Stats stats,Nano_Thief_AI_Reclaim oldAI) {
        super(ship);
        this.stats = stats;
        this.oldAI = oldAI;
    }

    @Override
    public void advance(float amount) {
        super.advance(amount);
        if (!stats.getAvailableShips().isEmpty()) ship.setShipAI(oldAI);
    }
}
