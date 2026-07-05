package Abyssal_XO.data.scripts.threat.skills.interfaces;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_7;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_SkillBase;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_Skill_7;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import lombok.Getter;

import java.util.ArrayList;

public class NanoThief_Interface_7 extends NanoThief_InterfaceBase {
    ArrayList<NanoThief_Skill_7> listiners = new ArrayList<>();
    @Override
    public void prepareCustomData(ArrayList<NanoThief_SkillBase> listiners) {
        double numShips;
        switch (skills.ship.getHullSize()){
            case CAPITAL_SHIP:
                numShips = NanoThief_7.numPerSize[3];
                break;
            case CRUISER:
                numShips = NanoThief_7.numPerSize[2];
                break;
            case DESTROYER:
                numShips = NanoThief_7.numPerSize[1];
                break;
            default:
                numShips = NanoThief_7.numPerSize[0];
        }
        maxFighters = (int) (numShips * skills.stats.skillMulti[7]);
        for (NanoThief_SkillBase a : listiners){
            NanoThief_Skill_7 b = (NanoThief_Skill_7) a;
            b.setInterface7(this);
            this.listiners.add(b);
        }
    }
    @Override
    public boolean validListener(NanoThief_SkillBase a) {
        return a instanceof NanoThief_Skill_7;
    }

    @Override
    public void displayStats() {
        if (listiners.size() == 1) displaySingleStat(listiners.get(0));
        else displayMultiStat();
    }
    private void displaySingleStat(NanoThief_Skill_7 listiner){
        //maxFighters = getMaxFighters();
        int max = maxFighters;
        int cur = currentFighters();
        /*if (max <= cur) {
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_6", "graphics/icons/hullsys/temporal_shell.png",
                    "Offencive Fighter Construction Status", cur+" / "+max, true);
            return;
        }*/
        if (listiner.waiting){
            listiner.cooldown = 0;
            if (skills.ship.isPhased()) {
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_7", "graphics/icons/hullsys/temporal_shell.png",
                        "Defencive Fighter Construction Status", cur + " / " + max + ", cannot create fighter while phased", true);
                return;
            }
            if (skills.getTotalReclaim() < skills.getModifiedCost(skills.stats.DF_swarmCost)) {
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_7", "graphics/icons/hullsys/temporal_shell.png",
                        "Defencive Fighter Construction Status", cur+" / "+max+", requires "+skills.getModifiedCost(skills.stats.OF_swarmCost)+" reclaim. "+100+"% ready", true);
                return;
            }
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_7", "graphics/icons/hullsys/temporal_shell.png",
                    "Defencive Fighter Construction Status", cur+" / "+max+", cannot control additional fighters", true);
            return;
        }
        if (skills.getTotalReclaim() < skills.getModifiedCost(skills.stats.DF_swarmCost)) {
            int charge = (int)(((listiner.recharge-listiner.cooldown) / listiner.recharge)*100);
            charge = Math.min(charge,100);
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_7", "graphics/icons/hullsys/temporal_shell.png",
                    "Defencive Fighter Construction Status", cur+" / "+max+", requires "+skills.getModifiedCost(skills.stats.DF_swarmCost)+" reclaim. "+charge+"% ready", true);
            return;
        }
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_7", "graphics/icons/hullsys/temporal_shell.png",
                "Defencive Fighter Construction Status", cur+" / "+max+", "+(int)(((listiner.recharge-listiner.cooldown) / listiner.recharge)*100)+"% prepared to create wing...", false);
        return;

    }
    private void displayMultiStat(){
        int max = maxFighters;
        int cur = currentFighters();
        String display = cur + " / "+max+": ";
        String displayTemp = "";
        boolean isWaiting = false;
        boolean isDebuff = false;
        for (int b = 0; b < listiners.size(); b++) {
            NanoThief_Skill_7 a = listiners.get(b);
            displayTemp += "("+getSingleListinerString(a)+")";
            if (a.waiting) isWaiting = true;
        }
        if (skills.getTotalReclaim() < skills.getModifiedCost(skills.stats.DF_swarmCost)){
            display+=" requires "+skills.getModifiedCost(skills.stats.DF_swarmCost)+" reclaim. ";
            isDebuff = true;
        }else if (skills.ship.isPhased() && isWaiting && canCreateFighter()){
            display+=" cannot create fighters when phased. ";
            isDebuff = true;
        }else if (!canCreateFighter()){
            display+= " cannot control additional fighters. ";
            isDebuff = true;
        }
        display+=displayTemp;
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_7", "graphics/icons/hullsys/temporal_shell.png",
                "Defensive Swarms", display, isDebuff);
    }
    private String getSingleListinerString(NanoThief_Skill_7 a){
        /*if (!a.onCooldown && !a.waiting){
            a.onCooldown = true;
            a.cooldown = a.recharge;
        }*/
        if (a.waiting) a.cooldown = 0;
        return a.WaitingOnReclaim ? "100%" : (int)(((a.recharge-a.cooldown) / a.recharge)*100)+"%";
    }
    @Getter
    private int maxFighters;
    public ArrayList<ShipAPI> defenders = new ArrayList<>();
    public boolean canCreateFighter(){
        return defenders.size() < maxFighters;
    }
    public int currentFighters(){
        return defenders.size();
    }
    /*public void addFighter(ShipAPI swarm){
        defenders.add(swarm);
    }
    public void removeFighter(ShipAPI swarm){
        defenders.remove(swarm);
    }*/
}
