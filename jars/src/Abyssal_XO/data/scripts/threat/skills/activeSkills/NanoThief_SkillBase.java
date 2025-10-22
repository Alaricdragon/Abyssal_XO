package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;

public class NanoThief_SkillBase {
    NanoThief_ShipSkills skills;
    public NanoThief_SkillBase(NanoThief_ShipSkills skills){
        this.skills = skills;//only the skills are needed. everything else can be gotten form it.
    }
    public void advance(float amount){
    }
}
