package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;

import java.util.ArrayList;

public class NanoThief_ShipSkills implements AdvanceableListener {
    protected Nano_Thief_Stats stats;
    private ArrayList<NanoThief_SkillBase> skills = new ArrayList<>();
    protected ShipAPI ship;
    protected float timeflow=1f;
    public NanoThief_ShipSkills(Nano_Thief_Stats stats, ShipAPI ship){
        this.stats = stats;
        this.ship = ship;
        //set skills here.
        //add in a switch statment to determin data about this ship.
    }

    @Override
    public void advance(float amount) {
        amount = amount*=timeflow;
        for (NanoThief_SkillBase a : skills){
            a.advance(amount);
            //note: desperate messures will undo its own timeflow here.
        }
    }
}
