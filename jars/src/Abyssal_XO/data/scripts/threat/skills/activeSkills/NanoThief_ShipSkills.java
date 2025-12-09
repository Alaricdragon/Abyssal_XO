package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.skills.Nano_Thief_Skill_Base;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import lombok.Getter;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;

public class NanoThief_ShipSkills implements AdvanceableListener {
    private static Logger log = Global.getLogger(Nano_Thief_Stats.class);
    @Getter
    private HashMap<ShipAPI,Integer> incomingReclaim = new HashMap<>();
    protected Nano_Thief_Stats stats;
    @Getter
    private ArrayList<NanoThief_SkillBase> skills = new ArrayList<>();
    @Getter
    private ArrayList<NanoThief_SkillBase> alwaysSkills = new ArrayList<>();
    protected ShipAPI ship;
    protected float timeflow=1f;
    protected double reclaim = 0;
    //@Getter
    //protected double refinedReclaim = 0;
    public NanoThief_ShipSkills(Nano_Thief_Stats stats, ShipAPI ship){
        this.stats = stats;
        this.ship = ship;
        //set skills here.
        //add in a switch statment to determin data about this ship.
        stats.getAvailableShips().add(ship);
        for (Nano_Thief_Skill_Base a : stats.getSkills()){
            NanoThief_SkillBase listener = a.createListiner(this,this.ship);
            addListener(listener,ship);
            //if (listener == null) continue;
            //skills.add(listener);
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
    @Deprecated
    public void suppressListener(NanoThief_SkillBase listiner){
        removedSkills.add(listiner);
    }
    public void addListener(NanoThief_SkillBase listiner, ShipAPI ship){
        if (listiner == null || !listiner.shouldUse(ship)){
            log.info("cannot add this listiner to this ship...");
            return;
        }
        if (listiner.alwaysAdvance()){
            log.info("adding skill to 'always'");
            alwaysSkills.add(listiner);
        }else{
            log.info("adding skill to 'only when reclaim'");
            skills.add(listiner);
        }
    }
    public void resetReclaim(){
        reclaim=0;
        //refinedReclaim=0;
    }
    public double getTotalReclaim(){
        return reclaim;//+refinedReclaim;
    }
    public double getTotalReclaimIncludingIncomeing(){
        int incoming = 0;
        ArrayList<ShipAPI> toRemove = new ArrayList<>();
        for (ShipAPI a : incomingReclaim.keySet()) if (a.isHulk() || !a.isAlive()) toRemove.add(a);
        for (ShipAPI a : toRemove) incomingReclaim.remove(a);
        for (ShipAPI a : incomingReclaim.keySet()) incoming += incomingReclaim.get(a);
        return getTotalReclaim()+incoming;
    }
    public void useReclaim(double reclaim){
        //if (refinedReclaim > 0){
        //    refinedReclaim -= reclaim;
        //    return;
        //}
        this.reclaim -= reclaim;
    }
    public void addReclaim(double reclaim){
        //boolean a = stats.centralFabAlive();
        this.reclaim+=reclaim;
        //addReclaim(reclaim,a);
    }
    /*public void addReclaim(double reclaim, boolean refined){
        if (refined){
            this.refinedReclaim+=reclaim;
            return;
        }
        this.reclaim+=reclaim;
    }*/
    public double getMaxUse(){
        double max = 0;
        for (NanoThief_SkillBase a : skills) if (a.getMaxCost() > max) max = a.getMaxCost();
        for (NanoThief_SkillBase a : alwaysSkills) if (a.getMaxCost() > max) max = a.getMaxCost();
        return max;
    }
    @Override
    public void advance(float amount) {
        attemptToDisplayStats();
        for (NanoThief_SkillBase a : alwaysSkills) a.advance(amount);
        if (getTotalReclaim() == 0) return;
        amount *= timeflow;
        for (NanoThief_SkillBase a : skills) a.advance(amount);
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
            int basicReclaim = 0;
            for (NanoThief_SkillBase a : getAlwaysSkills()){
                if (a instanceof NanoThief_Skill_8){
                    basicReclaim = (int) ((NanoThief_Skill_8) a).fakeReclaim;
                    break;
                }
            }
            int trueReclaim = (int) reclaim;
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_1", "graphics/icons/hullsys/temporal_shell.png",
                    "Reclaim Processing", basicReclaim +" Reclaim / "+trueReclaim+ " Refined Reclaim", false);
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
