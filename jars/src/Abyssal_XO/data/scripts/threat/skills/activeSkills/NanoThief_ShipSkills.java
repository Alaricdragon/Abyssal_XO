package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.skills.Nano_Thief_Skill_Base;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import lombok.Getter;

import java.util.ArrayList;

public class NanoThief_ShipSkills implements AdvanceableListener {
    protected Nano_Thief_Stats stats;
    private ArrayList<NanoThief_SkillBase> skills = new ArrayList<>();
    protected ShipAPI ship;
    protected float timeflow=1f;
    @Getter
    protected double reclaim = 0;
    @Getter
    protected double refinedReclaim = 0;
    public NanoThief_ShipSkills(Nano_Thief_Stats stats, ShipAPI ship){
        this.stats = stats;
        this.ship = ship;
        //set skills here.
        //add in a switch statment to determin data about this ship.
        stats.getAvailableShips().put(ship.getId(),ship);
        for (Nano_Thief_Skill_Base a : stats.getSkills()){
            NanoThief_SkillBase listener = a.createListiner(this,this.ship);
            if (listener == null) continue;
            skills.add(listener);
            /*if (listener.applyToModules()){
                for (ShipAPI b : ship.getChildModulesCopy()){
                    listener = a.createListiner(this,b);
                    if (listener == null) continue;
                    skills.add(listener);
                }
            }*/
        }

        for (NanoThief_SkillBase a : removedSkills){
            skills.remove(a);
        }
    }
    private ArrayList<NanoThief_SkillBase> removedSkills = new ArrayList<>();
    public void suppressListener(NanoThief_SkillBase listiner){
        removedSkills.add(listiner);
    }
    public void resetReclaim(){
        reclaim=0;
        refinedReclaim=0;
    }
    public double getTotalReclaim(){
        return reclaim+refinedReclaim;
    }
    public void useReclaim(double reclaim){
        if (refinedReclaim > 0){
            refinedReclaim -= reclaim;
            return;
        }
        this.reclaim -= reclaim;
    }
    public void addReclaim(double reclaim, boolean refined){
        if (refined){
            this.refinedReclaim+=reclaim;
            return;
        }
        this.reclaim+=reclaim;
    }
    @Override
    public void advance(float amount) {
        attemptToDisplayStats();
        if (getReclaim() == 0) return;
        amount = amount*=timeflow;
        for (NanoThief_SkillBase a : skills){
            a.advance(amount);
            //note: desperate messures will undo its own timeflow here.
        }
    }
    private void attemptToDisplayStats(){
        if (!ship.equals(Global.getCombatEngine().getPlayerShip())) return;
        /*if (control > swarms.size() || maxStorge > stored) {
            if (progress >= creationTime) {
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_2", "graphics/icons/hullsys/temporal_shell.png",
                        "Production Status", "Ready to launch Simulacrum Fighter Wing", false);
            } else {
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_2", "graphics/icons/hullsys/temporal_shell.png",
                        "Production Status", "Simulacrum Fighter Wing "+(int)((progress / creationTime) * 100)+"% complete", false);
            }
        }*/
        if (ship.equals(stats.getCentralFab())) {
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_0", "graphics/icons/hullsys/temporal_shell.png",
                    "Central Fabricator", "The center of all Refined Reclaim production in your fleet", false);
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_1", "graphics/icons/hullsys/temporal_shell.png",
                    "Reclaim Processing", (int) reclaim +" / "+refinedReclaim+ " Reclaim / Refined Reclaim", false);
        }else {
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_1", "graphics/icons/hullsys/temporal_shell.png",
                    "Stored Reclaim", (int) getTotalReclaim() + " Reclaim available", false);
        }
        /*int control = controlAmount();
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF, "graphics/icons/hullsys/temporal_shell.png",
                "Deployed Simulacrum Fighter Wing", (swarms.size()+reclaimCores.size())+"/"+control, false);
        if (stored == 0) return;
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF+"_4", "graphics/icons/hullsys/temporal_shell.png",
                "Prepared Simulacrum Fighter Wing", stored+"/"+maxStorge, false);*/


        for (NanoThief_SkillBase a : skills){
            a.displayStats();
        }
    }
}
