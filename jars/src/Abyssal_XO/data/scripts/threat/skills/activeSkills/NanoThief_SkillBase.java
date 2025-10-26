package Abyssal_XO.data.scripts.threat.skills.activeSkills;

public class NanoThief_SkillBase {
    protected NanoThief_ShipSkills skills;
    public NanoThief_SkillBase(NanoThief_ShipSkills skills){
        this.skills = skills;//only the skills are needed. everything else can be gotten form it.
    }
    public void prepareData(){

    }
    public void advance(float amount){
    }
    public void displayStats(){}
}
