package Abyssal_XO.data.scripts.hullmods;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.Utils;
import Abyssal_XO.data.scripts.combatability.CombatabilityUtility;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.loading.VariantSource;
import second_in_command.SCData;
import second_in_command.SCUtils;
import second_in_command.specs.SCBaseSkillPlugin;

import java.util.HashMap;

import static Abyssal_XO.data.scripts.Settings.NANO_THIEF_SIC_HULLMOD_DATA_KEY;

public class SICSkillControllerBackup extends BaseHullMod {
    //todo: NOTICE: there is effectively only one instance of this mod. so HWAT THE FUCK WHY??
    //private CampaignFleetAPI fleet = null;
    //public static CampaignFleetAPI fleet_global = null;
    //public static HashMap<FleetMemberAPI,CampaignFleetAPI> member_map = new HashMap<>();
    //public static HashMap<ShipAPI,CampaignFleetAPI> ship_map = new HashMap<>();

    public static void addHullmodAndData_Chose(ShipAPI ship, SCData data){
        if (Utils.isCurrectSiCVersion()) CombatabilityUtility.addSiC_MidFight(ship,data);
        addShipAfterShipSpawns(ship,data);
    }

    /*
    /// adds the fleet to this hullmod -before- the ship gets is added to the combat engin.
    public static void addShipBeforeShipSpawns(FleetMemberAPI ship, CampaignFleetAPI fleet){
        if (Utils.isCurrectSiCVersion()) return;
        ShipVariantAPI OVERWRITER = ship.getVariant();//Global.getSettings().getVariant("Abyssal_XO_ReclaimCore_Blank").clone();
        OVERWRITER.setSource(VariantSource.REFIT);
        SICSkillControllerBackup.member_map.put(ship,fleet);
        OVERWRITER.addMod(Settings.SIC_CONTROL_HULLMOD);
        ship.setVariant(OVERWRITER,false,true);
    }*/
    /// adds the ship to this hullmod after the ship is added to the combat engine
    public static void addShipAfterShipSpawns(ShipAPI ship, SCData data){
        if (Utils.isCurrectSiCVersion()) return;
        Settings.log.info("attempting to add hullmods to a single ship of name: "+ship.getName()+", id: "+ship.getFleetMember().getId()+", hull id: "+ship.getHullSpec().getHullId());
        Settings.log.info(" ships fleet starting as: "+(ship.getFleetMember() != null && ship.getFleetMember().getFleetData() != null && ship.getFleetMember().getFleetData().getFleet() != null ? ship.getFleetMember().getFleetData().getFleet().getId() : "N/A"));
        //SICSkillControllerBackup.member_map.put(ship.getFleetMember(),fleet);
        //ship.getFleetMember().setCustomData(NANO_THIEF_SIC_HULLMOD_FLEET_KEY,fleet);
        ShipVariantAPI OVERWRITER = ship.getVariant();//Global.getSettings().getVariant("Abyssal_XO_ReclaimCore_Blank").clone();
        OVERWRITER.setSource(VariantSource.REFIT);
        //OVERWRITER.setWingId(0,skills.stats.OF_fighterToBuild);
        OVERWRITER.addMod(Settings.SIC_CONTROL_HULLMOD);
        //ship.getVariant().getHullMods();
        ship.getFleetMember().setVariant(OVERWRITER,false,true);//setVariant(OVERWRITER,false,true);
        Settings.log.info(" does have hullmod: "+ship.getVariant().hasHullMod(Settings.SIC_CONTROL_HULLMOD));

        ship.applyEffectsAfterShipAddedToCombatEngine();

        ship.setCustomData(NANO_THIEF_SIC_HULLMOD_DATA_KEY,data);
        //SCData data = getData(ship.getFleetMember());
        if (data == null) return;

        for (SCBaseSkillPlugin skill : data.getAllActiveSkillsPlugins()) {
            skill.applyEffectsBeforeShipCreation(data, ship.getFleetMember().getStats(), ship.getVariant(), ship.getHullSize(), Settings.SIC_CONTROL_HULLMOD+"_"+skill.getId());
        }
        for (SCBaseSkillPlugin skill : data.getAllActiveSkillsPlugins()) {
            skill.applyEffectsAfterShipCreation(data, ship, ship.getVariant(), Settings.SIC_CONTROL_HULLMOD+"_"+skill.getId());
        }
    }
    /*public void setFleet(FleetMemberAPI memberAPI){//might need some more things here???
        if (this.fleet != null){
            Settings.log.info( "(SICSkillControllerBackup)"+(memberAPI != null ?memberAPI.getId():"N/A")+" already has fleet of id"+fleet.getId());
            return;
        }
        if (memberAPI != null && member_map.containsKey(memberAPI)){
            this.fleet = member_map.get(memberAPI);
            //map.remove(memberAPI);
            Settings.log.info("(SICSkillControllerBackup)"+memberAPI.getId()+" getting fleet from map");
            return;
        }
        if (fleet_global != null){
            this.fleet = fleet_global;
            fleet_global = null;
            Settings.log.info("(SICSkillControllerBackup)"+memberAPI.getId()+" getting fleet static fleet temp");
            return;
        }
        Settings.log.warn("(SICSkillControllerBackup)"+memberAPI.getId()+" failed to get fleet. something is very wrong.....");
        //else{
            //Settings.log.info("got fleet. something is right.");
        //}
        //this.fleet = null;
    }*/
    //id: Abussal_XO_SIC_controler
    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        super.applyEffectsBeforeShipCreation(hullSize, stats, id);

       // if (tryToSetFleet(stats.getFleetMember())) return;
        //this never actavates in this context.
        /*SCData data = getData(stats.getFleetMember());
        if (data == null){
            Settings.log.info("failed to get data (a)"+(stats.getFleetMember() != null ? stats.getFleetMember().getId() : "N/A"));
            return;
        }
        for (SCBaseSkillPlugin skill : data.getAllActiveSkillsPlugins()) {
            skill.applyEffectsBeforeShipCreation(data, stats, stats.getVariant(), hullSize, id+"_"+skill.getId());
        }*/
    }

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        super.applyEffectsAfterShipCreation(ship, id);
        //if (tryToSetFleet(ship.getFleetMember())) return;
        SCData data = getData(ship);
        if (data == null){
            Settings.log.info("failed to get data (b)"+(ship.getFleetMember() != null ? ship.getFleetMember().getId() : "N/A"));
            //if (ship.getMutableStats().getFleetMember() != null)Settings.log.info(" -"+ship.getMutableStats().getFleetMember().getId());
            return;
        }
        for (SCBaseSkillPlugin skill : data.getAllActiveSkillsPlugins()) {
            skill.applyEffectsAfterShipCreation(data, ship, ship.getVariant(), id+"_"+skill.getId());
            //skill.applyEffectsBeforeShipCreation(data, stats, stats.getVariant(), hullSize, "${id}_${skill.getId()}")
        }
    }

    @Override
    public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
        super.applyEffectsToFighterSpawnedByShip(fighter, ship, id);
        //if (tryToSetFleet(ship.getFleetMember())) return;
        SCData data = getData(ship);
        if (data == null){
            Settings.log.info("failed to get data (c)"+(ship.getFleetMember() != null ? ship.getFleetMember().getId() : "N/A"));
            return;
        }
        for (SCBaseSkillPlugin skill : data.getAllActiveSkillsPlugins()) {
            //skill.applyEffectsToFighterSpawnedByShip(data, ship, ship.getVariant(), id+"_"+skill.getId());
            skill.applyEffectsToFighterSpawnedByShip(data, fighter, ship, id+"_"+skill.getId());
            //skill.applyEffectsBeforeShipCreation(data, stats, stats.getVariant(), hullSize, "${id}_${skill.getId()}")
        }
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        super.advanceInCombat(ship, amount);
        //if (tryToSetFleet(ship.getFleetMember())) return;
        SCData data = getData(ship);
        if (data == null){
            Settings.log.info("failed to get data (d)"+(ship.getFleetMember() != null ? ship.getFleetMember().getId() : "N/A"));
            Settings.log.info("-name, size, hull id:"+ship.getName()+", "+ship.getHullSpec().getHullSize()+", "+ship.getHullSpec().getHullId());
            return;
        }
        for (SCBaseSkillPlugin skill : data.getAllActiveSkillsPlugins()) {
            //skill.applyEffectsToFighterSpawnedByShip(data, ship, ship.getVariant(), id+"_"+skill.getId());
            skill.advanceInCombat(data,  ship, amount);
            //skill.applyEffectsBeforeShipCreation(data, stats, stats.getVariant(), hullSize, "${id}_${skill.getId()}")
        }
    }

    /*private boolean tryToSetFleet(FleetMemberAPI fleetMemberAPI){
        setFleet(fleetMemberAPI);
        return fleet == null;
    }*/
    /*private CampaignFleetAPI getFleet(){
        return this.fleet;
    }*/
    private static SCData getData(ShipAPI shipAPI){
        if (shipAPI.getCustomData().containsKey(NANO_THIEF_SIC_HULLMOD_DATA_KEY)){
            //Settings.log.info("got saved data for ship of: "+shipAPI.getId());
            return (SCData) shipAPI.getCustomData().get(NANO_THIEF_SIC_HULLMOD_DATA_KEY);
        }
        return null;
        //modified to only return the saved data, or nothing at all...
        /*ShipAPI looking = shipAPI;
        while (shipAPI.getParentStation() != null) {
            shipAPI = shipAPI.getParentStation();//FORCE item to use parent stations memory.
            Settings.log.info(" -Forcing parent station of id: "+shipAPI.getId()+", name: "+shipAPI.getName());
        }
        Settings.log.info("failed to get saved data for ship of: "+looking.getId()+", Attempting to set data...");
        SCData data = getData(shipAPI.getFleetMember() != null ? shipAPI.getFleetMember() : shipAPI.getMutableStats().getFleetMember());//SCUtils.getFleetData(ship_map.get(shipAPI));
        looking.setCustomData(NANO_THIEF_SIC_HULLMOD_DATA_KEY,data);
        return data;*/
    }
    /*private static SCData getData(FleetMemberAPI fleetMemberAPI){
        if (fleetMemberAPI == null){
            Settings.log.info("(SIC_Controller_Backup) failed to get fleet member for a ship...");
            return null;
        }
        if (!member_map.containsKey(fleetMemberAPI)){
            Settings.log.warn("(SIC_Controller_Backup) failed to get fleet in fleetmember - fleet map.  fleetMember id, name of: "+fleetMemberAPI.getId()+", "+fleetMemberAPI.getShipName());
            Settings.log.info("     got some stats as: hull id: "+fleetMemberAPI.getHullSpec().getHullId()+", size: "+fleetMemberAPI.getHullSpec().getHullSize());
            return null;
        }
        Settings.log.info("got base data of member id: "+fleetMemberAPI.getId()+" as "+member_map.get(fleetMemberAPI).getId());
        return SCUtils.getFleetData(member_map.get(fleetMemberAPI));
    }*/
    /*private List<SCBaseSkillPlugin> getSkills(){
        return SCUtils.getFleetData(getFleet()).getAllActiveSkillsPlugins();
    }*/
}
