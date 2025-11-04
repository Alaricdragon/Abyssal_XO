package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import org.apache.log4j.Logger;

public class NanoThief_SkillBase {
    protected NanoThief_ShipSkills skills;
    protected ShipAPI ship;
    protected static Logger log = Global.getLogger(NanoThief_SkillBase.class);
    @Deprecated
    public boolean applyToModules(){
        return false;
    }
    public NanoThief_SkillBase(NanoThief_ShipSkills skills,ShipAPI ship){
        this.skills = skills;//only the skills are needed. everything else can be gotten form it.
        this.ship = ship;
    }
    public void prepareData(){

    }
    public void advance(float amount){
    }
    public void displayStats(){}
}
