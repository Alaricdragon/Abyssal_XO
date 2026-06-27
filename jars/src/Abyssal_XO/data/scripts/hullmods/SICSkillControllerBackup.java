package Abyssal_XO.data.scripts.hullmods;

import Abyssal_XO.data.scripts.Settings;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import second_in_command.SCData;
import second_in_command.SCUtils;
import second_in_command.specs.SCBaseSkillPlugin;

import java.util.HashMap;
import java.util.List;

public class SICSkillControllerBackup extends BaseHullMod {
    //todo: NOTICE: there is effectively only one instance of this mod. so HWAT THE FUCK WHY??
    private CampaignFleetAPI fleet = null;
    public static CampaignFleetAPI fleet_global = null;
    public static HashMap<FleetMemberAPI,CampaignFleetAPI> member_map = new HashMap<>();
    public static HashMap<ShipAPI,CampaignFleetAPI> ship_map = new HashMap<>();
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
        SCData data = getData(stats.getFleetMember());
        if (data == null) return;
        for (SCBaseSkillPlugin skill : data.getAllActiveSkillsPlugins()) {
            skill.applyEffectsBeforeShipCreation(data, stats, stats.getVariant(), hullSize, id+"_"+skill.getId());
        }
    }

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        super.applyEffectsAfterShipCreation(ship, id);
        //if (tryToSetFleet(ship.getFleetMember())) return;
        SCData data = getData(ship);
        if (data == null) return;
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
        if (data == null) return;
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
        if (data == null) return;
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
    private SCData getData(ShipAPI shipAPI){
        return getData(shipAPI.getFleetMember());//SCUtils.getFleetData(ship_map.get(shipAPI));
    }
    private SCData getData(FleetMemberAPI fleetMemberAPI){
        if (fleetMemberAPI == null){
            Settings.log.info("failed to get fleet member for a ship....");
            return null;
        }
        return SCUtils.getFleetData(member_map.get(fleetMemberAPI));
    }
    /*private List<SCBaseSkillPlugin> getSkills(){
        return SCUtils.getFleetData(getFleet()).getAllActiveSkillsPlugins();
    }*/
}
