package Abyssal_XO.data.scripts.threat.skills.interfaces;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_7;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_SkillBase;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_Skill_6;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_Skill_7;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import lombok.Getter;

import java.util.ArrayList;

public class NanoThief_Interface_6 extends NanoThief_InterfaceBase {
    ArrayList<NanoThief_Skill_6> listiners = new ArrayList<>();
    @Override
    public void prepareCustomData(ArrayList<NanoThief_SkillBase> listiners) {
        for (NanoThief_SkillBase a : listiners){
            NanoThief_Skill_6 b = (NanoThief_Skill_6) a;
            this.listiners.add(b);
        }
    }
    @Override
    public boolean validListener(NanoThief_SkillBase a) {
        return a instanceof NanoThief_Skill_6;
    }

    @Override
    public void displayStats() {
        if (listiners.size() == 1) displaySingleStat(listiners.get(0));
        else displayMultiStat();
    }
    private void displaySingleStat(NanoThief_Skill_6 listiner){
        //maxFighters = getMaxFighters();
        int max = listiner.getMaxFighters();
        int cur = listiner.currentFighters();
        /*if (max <= cur) {
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_6", "graphics/icons/hullsys/temporal_shell.png",
                    "Offencive Fighter Construction Status", cur+" / "+max, true);
            return;
        }*/
        if (skills.getTotalReclaim() >= skills.getModifiedCost(skills.stats.OF_swarmCost)){
            if (!listiner.onCooldown && !listiner.waiting){
                listiner.onCooldown = true;
                listiner.cooldown = listiner.recharge;
            }
            if (listiner.waiting){
                if (skills.ship.isPhased()) {
                    Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_6", "graphics/icons/hullsys/temporal_shell.png",
                            "Offencive Fighter Construction Status", cur + " / " + max + ", cannot create fighter while phased", true);
                    return;
                }
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_6", "graphics/icons/hullsys/temporal_shell.png",
                        "Offencive Fighter Construction Status", cur+" / "+max+", cannot control additional fighters", true);
                return;
            }
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_6", "graphics/icons/hullsys/temporal_shell.png",
                    "Offencive Fighter Construction Status", cur+" / "+max+", "+(int)(((listiner.recharge-listiner.cooldown) / listiner.recharge)*100)+"% prepared to create wing...", false);
            return;
        }
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_6", "graphics/icons/hullsys/temporal_shell.png",
                "Offencive Fighter Construction Status", cur+" / "+max+", cannot build wing do to limited reclaim", true);

    }
    private void displayMultiStat(){
        int max = listiners.get(0).getMaxFighters();
        int cur = listiners.get(0).currentFighters();
        if (skills.getTotalReclaim() < skills.getModifiedCost(skills.stats.OF_swarmCost)){
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_6", "graphics/icons/hullsys/temporal_shell.png",
                    "Offencive Fighter Construction Status", cur+" / "+max+", cannot build wings do to limited reclaim", true);
            return;
        }
        String display = cur + " / "+max+": ";
        String displayTemp = "";
        boolean isWaiting = false;
        boolean isDebuff = false;
        for (int b = 0; b < listiners.size(); b++) {
            NanoThief_Skill_6 a = listiners.get(b);
            displayTemp += "("+getSingleListinerString(a)+")";
            if (a.waiting) isWaiting = true;
        }
        if (skills.ship.isPhased() && isWaiting && max < cur){
            display+=" cannot create fighters when phased. ";
            isDebuff = true;
        }
        if (max >= cur){
            display+= " cannot control additional fighters. ";
            isDebuff = true;
        }
        display+=displayTemp;
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_6", "graphics/icons/hullsys/temporal_shell.png",
                "Offencive Fighter Construction Status", display, isDebuff);
    }
    private String getSingleListinerString(NanoThief_Skill_6 a){
        if (!a.onCooldown && !a.waiting){
            a.onCooldown = true;
            a.cooldown = a.recharge;
        }
        return (int)(((a.recharge-a.cooldown) / a.recharge)*100)+"%";
    }
}
