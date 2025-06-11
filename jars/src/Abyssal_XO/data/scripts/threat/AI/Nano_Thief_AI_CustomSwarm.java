package Abyssal_XO.data.scripts.threat.AI;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.*;
import com.fs.starfarer.api.impl.combat.threat.ThreatSwarmAI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

public class Nano_Thief_AI_CustomSwarm extends Nano_Thief_AIManager {
    public Nano_Thief_AI_CustomSwarm(ShipAPI ship, Nano_Thief_Stats stats) {
        super(ship);
        attackSwarm = true;
        constructionSwarm = false;
        reclamationSwarm = false;
    }




    //notes: fighters do there main work here.
}
