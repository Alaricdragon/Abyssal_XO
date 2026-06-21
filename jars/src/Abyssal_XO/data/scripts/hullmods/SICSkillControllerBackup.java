package Abyssal_XO.data.scripts.hullmods;

import Abyssal_XO.data.scripts.Settings;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.combat.BaseHullMod;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import second_in_command.SCData;
import second_in_command.SCUtils;
import second_in_command.specs.SCBaseSkillPlugin;

import java.util.List;

public class SICSkillControllerBackup extends BaseHullMod {
    private CampaignFleetAPI fleet = null;
    public static CampaignFleetAPI fleet_global = null;
    public void setFleet(){//might need some more things here???
        if (this.fleet == null && fleet_global != null){
            this.fleet = fleet_global;
            fleet_global = null;
        }else if (this.fleet == null){
            Settings.log.warn("failed to get fleet. something is very wrong.....");
        }//else{
            //Settings.log.info("got fleet. something is right.");
        //}
        //this.fleet = null;
    }
    //id: Abussal_XO_SIC_controler
    @Override
    public void applyEffectsBeforeShipCreation(ShipAPI.HullSize hullSize, MutableShipStatsAPI stats, String id) {
        super.applyEffectsBeforeShipCreation(hullSize, stats, id);
        if (tryToSetFleet()) return;
        SCData data = getData();
        List<SCBaseSkillPlugin> skills = getSkills();
        for (SCBaseSkillPlugin skill : skills) {
            skill.applyEffectsBeforeShipCreation(data, stats, stats.getVariant(), hullSize, id+"_"+skill.getId());
        }
    }

    @Override
    public void applyEffectsAfterShipCreation(ShipAPI ship, String id) {
        super.applyEffectsAfterShipCreation(ship, id);
        if (tryToSetFleet()) return;
        SCData data = getData();
        List<SCBaseSkillPlugin> skills = getSkills();
        for (SCBaseSkillPlugin skill : skills) {
            skill.applyEffectsAfterShipCreation(data, ship, ship.getVariant(), id+"_"+skill.getId());
            //skill.applyEffectsBeforeShipCreation(data, stats, stats.getVariant(), hullSize, "${id}_${skill.getId()}")
        }
    }

    @Override
    public void applyEffectsToFighterSpawnedByShip(ShipAPI fighter, ShipAPI ship, String id) {
        super.applyEffectsToFighterSpawnedByShip(fighter, ship, id);
        if (tryToSetFleet()) return;
        SCData data = getData();
        List<SCBaseSkillPlugin> skills = getSkills();
        for (SCBaseSkillPlugin skill : skills) {
            //skill.applyEffectsToFighterSpawnedByShip(data, ship, ship.getVariant(), id+"_"+skill.getId());
            skill.applyEffectsToFighterSpawnedByShip(data, fighter, ship, id+"_"+skill.getId());
            //skill.applyEffectsBeforeShipCreation(data, stats, stats.getVariant(), hullSize, "${id}_${skill.getId()}")
        }
    }

    @Override
    public void advanceInCombat(ShipAPI ship, float amount) {
        super.advanceInCombat(ship, amount);
        if (tryToSetFleet()) return;
        SCData data = getData();
        List<SCBaseSkillPlugin> skills = getSkills();
        for (SCBaseSkillPlugin skill : skills) {
            //skill.applyEffectsToFighterSpawnedByShip(data, ship, ship.getVariant(), id+"_"+skill.getId());
            skill.advanceInCombat(data,  ship, amount);
            //skill.applyEffectsBeforeShipCreation(data, stats, stats.getVariant(), hullSize, "${id}_${skill.getId()}")
        }
    }

    private boolean tryToSetFleet(){
        setFleet();
        return fleet == null;
    }
    private CampaignFleetAPI getFleet(){
        return this.fleet;
    }
    private SCData getData(){
        return SCUtils.getFleetData(getFleet());
    }
    private List<SCBaseSkillPlugin> getSkills(){
        return SCUtils.getFleetData(getFleet()).getAllActiveSkillsPlugins();
    }
}
