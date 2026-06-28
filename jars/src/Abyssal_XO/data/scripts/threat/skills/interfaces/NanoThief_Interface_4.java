package Abyssal_XO.data.scripts.threat.skills.interfaces;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_4;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_SkillBase;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_Skill_4;
import com.fs.starfarer.api.Global;

import java.util.ArrayList;

public class NanoThief_Interface_4 extends NanoThief_InterfaceBase{
    private ArrayList<NanoThief_Skill_4> listiners = new ArrayList<>();
    @Override
    public void prepareCustomData(ArrayList<NanoThief_SkillBase> listiners) {
        for (NanoThief_SkillBase a : listiners) this.listiners.add((NanoThief_Skill_4) a);
    }

    @Override
    public boolean validListener(NanoThief_SkillBase a) {
        return a instanceof NanoThief_Skill_4;
    }

    @Override
    public void displayStats() {
        if (listiners.size() == 1) displaySingle(listiners.get(0));
        else displayMulti();
    }
    private void displaySingle(NanoThief_Skill_4 single){
        if (single.isActive){
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_4", "graphics/icons/hullsys/temporal_shell.png",
                    "Scrapy Fortification", "Active for "+(int)(NanoThief_4.time-single.timeActive)+" seconds",false);
            return;
        }
        if (skills.getTotalReclaim() < skills.getModifiedCost(NanoThief_4.activeCost)){
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_4", "graphics/icons/hullsys/temporal_shell.png",
                    "Scrapy Fortification", "Cannot activate well under "+NanoThief_4.activeCost+" reclaim",true);
            return;
        }
        if (single.ready){
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_4", "graphics/icons/hullsys/temporal_shell.png",
                    "Scrapy Fortification", "Ready to activate",false);
            return;
        }
        // 1 / 10 = 0.1
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_4", "graphics/icons/hullsys/temporal_shell.png",
                "Scrapy Fortification", (int)(100-((single.cooldown / NanoThief_4.cooldown)*100))+"% ready",true);

    }
    private void displayMulti(){
        String out = "";
        boolean debuff = false;
        for (NanoThief_Skill_4 a : listiners) if (a.isActive){
            out += "Active for "+(int)(NanoThief_4.time-a.timeActive)+" seconds. ";
        }else if (skills.getTotalReclaim() < skills.getModifiedCost(NanoThief_4.activeCost)){
            out += "Cannot activate well under "+NanoThief_4.activeCost+" reclaim. ";
            debuff = true;
        }
        int ready = getReadyInstances();
        if (ready != 0) out += ready+" charges ready. ";
        ArrayList<String> singles = new ArrayList<>();
        for (NanoThief_Skill_4 a : listiners){
            String b = getSingleString(a);
            if (b != null) singles.add(b);
        }
        for (int a = 0 ; a < singles.size(); a++){
            out+= singles.get(a);
            if (a != singles.size() - 1) out +=", ";
        }
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_4", "graphics/icons/hullsys/temporal_shell.png",
                "Scrapy Fortification", out,debuff);
    }
    private int getReadyInstances(){
        int ready = 0;
        for (NanoThief_Skill_4 a : listiners) if (a.ready) ready++;
        return ready;
    }
    private String getSingleString(NanoThief_Skill_4 a){
        if (a.ready || a.isActive) return null;
        return "("+(int)(100-((a.cooldown / NanoThief_4.cooldown)*100))+"%"+")";
    }
}
