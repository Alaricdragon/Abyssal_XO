package Abyssal_XO.data.scripts.threat.AI;

import Abyssal_XO.data.scripts.threat_old.Nano_Thief_Stats;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.impl.combat.threat.ThreatSwarmAI;

@Deprecated
public class Nano_Thief_AI_CustomSwarm extends ThreatSwarmAI {
    public Nano_Thief_AI_CustomSwarm(ShipAPI ship, Nano_Thief_Stats stats) {
        super(ship);
        attackSwarm = true;
        constructionSwarm = false;
        reclamationSwarm = false;
    }




    //notes: fighters do there main work here.
}
