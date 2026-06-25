package Abyssal_XO.data.scripts.threat.skills.interfaces;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_ShipSkills;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_SkillBase;
import com.fs.starfarer.api.combat.ShipAPI;

import java.util.ArrayList;

public abstract class NanoThief_InterfaceBase {
    protected NanoThief_ShipSkills skills;
    public NanoThief_InterfaceBase(){
    }
    public void prepareData(NanoThief_ShipSkills skills,  ArrayList<NanoThief_SkillBase> listiners){
        this.skills = skills;
        prepareCustomData(listiners);
    }
    public abstract void prepareCustomData( ArrayList<NanoThief_SkillBase> listiners);
    public abstract boolean validListener(NanoThief_SkillBase a);
    public abstract void displayStats();
}
