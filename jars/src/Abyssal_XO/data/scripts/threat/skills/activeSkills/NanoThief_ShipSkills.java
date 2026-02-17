package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_8;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_9;
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
    private HashMap<ShipAPI,reclaim> incomingReclaim = new HashMap<>();
    protected Nano_Thief_Stats stats;
    @Getter
    private ArrayList<NanoThief_SkillBase> skills = new ArrayList<>();
    @Getter
    private ArrayList<NanoThief_SkillBase> alwaysSkills = new ArrayList<>();
    protected ShipAPI ship;
    protected float timeflow=1f;
    protected double costMulti=1;
    protected double reclaim = 0;
    private HashMap<String,Double> costMods = new HashMap<>();
    private HashMap<String,Float> speedMods = new HashMap<>();
    boolean activeWellOverloaded = false;

    @Getter
    private ArrayList<ShipAPI> childShips = new ArrayList<>();
    //@Getter
    //protected double refinedReclaim = 0;
    public NanoThief_ShipSkills(Nano_Thief_Stats stats, ShipAPI ship){
        this.stats = stats;
        this.ship = ship;
        //set skills here.
        //add in a switch statment to determin data about this ship.
        stats.getAvailableShips().add(ship);
        for (Nano_Thief_Skill_Base a : stats.getSkills()){
            if (a instanceof NanoThief_9) activeWellOverloaded = true;
            NanoThief_SkillBase listener = a.createListiner(this,this.ship);
            if (listener != null) {
                addListener(listener, ship);
            }

            NanoThief_SkillBase[] listeners = a.createListiners(this,this.ship);
            if (listeners == null) continue;
            addListeners(listeners,ship);
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
        addChildShips(ship);
    }
    private void addChildShips(ShipAPI ship){
        for (ShipAPI a : ship.getChildModulesCopy()){
            if (!childShips.contains(a)){
                childShips.add(a);
                addChildShips(a);
            }
        }
    }
    private ArrayList<NanoThief_SkillBase> removedSkills = new ArrayList<>();
    @Deprecated
    public void suppressListener(NanoThief_SkillBase listiner){
        removedSkills.add(listiner);
    }
    public void addListener(NanoThief_SkillBase listiner, ShipAPI ship){
        if (listiner == null || !listiner.shouldUse(ship)){
            log.info("cannot add this listiner to this ship..."+listiner.getClass().getCanonicalName());
            return;
        }
        if (listiner.alwaysAdvance()){
            log.info("adding skill to 'always' of class: "+listiner.getClass().getCanonicalName());
            alwaysSkills.add(listiner);
        }else{
            log.info("adding skill to 'only when reclaim'"+listiner.getClass().getCanonicalName());
            skills.add(listiner);
        }
    }
    public void addListeners(NanoThief_SkillBase[] listiner, ShipAPI ship){
        log.info("attempting to add a list of listiners of size: "+listiner.length);
        for (NanoThief_SkillBase a : listiner) addListener(a,ship);
    }
    public void resetReclaim(){
        reclaim=0;
        //refinedReclaim=0;
    }
    public double getIncomingReclaimValue(){
        //todo: modify this for a missing central fabricator as well.
        int incoming = 0;
        ArrayList<ShipAPI> toRemove = new ArrayList<>();
        for (ShipAPI a : incomingReclaim.keySet()) if (a.isHulk() || !a.isAlive()) toRemove.add(a);
        for (ShipAPI a : toRemove) incomingReclaim.remove(a);
        for (ShipAPI a : incomingReclaim.keySet()){
            double mod = 1;
            if (stats.getCentralFab() != null) {
                //Object ai = a.getAI();
                //Nano_Thief_AI_Reclaim ai2;
                //log.info("getting AI of: "+ai.getClass().getName() +", "+ai.getClass().getCanonicalName());
                //if (ai instanceof Nano_Thief_AI_ReclaimCombat) {
                //    ai2 = ((Nano_Thief_AI_ReclaimCombat) ai).getOldAI();
                //} else {
                //    ai2 = (Nano_Thief_AI_Reclaim) ai;
                //}
                if (ship.equals(stats.getCentralFab())) if (!incomingReclaim.get(a).isRefined) mod = NanoThief_8.reclaimRaito;
                else if (!incomingReclaim.get(a).isRefined) mod = NanoThief_8.baseReclaimEfficiencyMod;
            }
            incoming += (int) (incomingReclaim.get(a).value*mod);
        }
        return incoming;
    }
    public double getModifiedCost(double cost){
        return cost * costMulti;
    }
    public double getCostMulti(){
        double mult = 1;
        for (double a : costMods.values()) mult += a;
        costMulti = mult;
        return mult;
    }
    public float getTimeMulti(){
        float mult = 1;
        for (float a : speedMods.values()) mult += a;
        timeflow = mult;
        return mult;
    }
    public void addSpeedMod(String id, float value){
        speedMods.put(id,value);
        getTimeMulti();
    }
    public void removeSpeedMod(String id){
        speedMods.remove(id);
        getTimeMulti();
    }
    public void addCostMod(String id, double value){
        costMods.put(id,value);
        getCostMulti();
    }
    public void removeCostMod(String id){
        costMods.remove(id);
        getCostMulti();
    }
    public double getTotalReclaim(){
        return reclaim;//+refinedReclaim;
    }
    public double getTotalReclaimIncludingIncomeing(){
        return getTotalReclaim()+ getIncomingReclaimValue();
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
        //getCostMulti();
        //getTimeMulti();
        attemptToDisplayStats();
        if (ship.isHulk()) return;
        if (!activeWellOverloaded && (ship.getFluxTracker().isOverloaded() || ship.getFluxTracker().isVenting())) return;
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


        for (NanoThief_SkillBase a : alwaysSkills){
            a.displayStats();
        }
        for (NanoThief_SkillBase a : skills){
            a.displayStats();
        }
    }
    public void addIncomingReclaim(ShipAPI reclaim, int value, boolean isRefined){
        incomingReclaim.put(reclaim,new reclaim(value, isRefined));
    }

    public ArrayList<ShipAPI> getChildShips() {
        for (int a = childShips.size()-1; a >= 0; a--) if (!childShips.get(a).isAlive() || childShips.get(a).isHulk()) childShips.remove(a);
        return childShips;
    }
}

class reclaim{
    public int value;
    public boolean isRefined;
    reclaim(int value, boolean isRefined){
        this.value = value;
        this.isRefined = isRefined;
    }
}
