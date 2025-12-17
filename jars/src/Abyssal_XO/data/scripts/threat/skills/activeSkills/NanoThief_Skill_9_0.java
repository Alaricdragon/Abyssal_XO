package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_9;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;

public class NanoThief_Skill_9_0 extends NanoThief_SkillBase{
    public NanoThief_Skill_9_0(NanoThief_ShipSkills skills, ShipAPI ship) {
        super(skills, ship);
    }

    @Override
    public boolean alwaysAdvance() {
        return true;
    }
    float timeLeft = 1;
    @Override
    public void advance(float amount) {
        /*if (timeLeft >= 0){
            timeLeft-=amount;
            return;
        }
        timeLeft = 1;*/
        float hp = ship.getHitpoints() / ship.getMaxHitpoints(); // 10 / 5 = 2. 5 / 10 = 0.5
        hp = Math.min(hp,1);
        skills.costMods.put("9_0",(NanoThief_9.hpCost*((1-hp)*100))+1);
        skills.speedMods.put("9_0", ((float) (NanoThief_9.hpSpeed*((1-hp)*100)))+1);
    }
    @Override
    public void displayStats() {
        log.info("displaying stats for skill 9...");
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_9", "graphics/icons/hullsys/temporal_shell.png",
                "ReclaimSpeedAndCost", "cost multi: "+((int)(skills.costMulti*100))/100d+" speed multi: "+((int)(skills.timeflow*100))/100d, false);
    }
}
