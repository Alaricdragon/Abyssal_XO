package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_ShipSkillsAdder;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_BattleListener;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CharacterDataAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.apache.log4j.Logger;
import second_in_command.SCData;
import second_in_command.specs.SCBaseSkillPlugin;

import java.util.List;

import static Abyssal_XO.data.scripts.Settings.NANO_THIEF_RECLAIM_RECYCLE_PERCENT;

public class NanoThief_Base extends Nano_Thief_Skill_Base {
    private static final String key = "AbyssalXO_Nano_Thief_Skill_0";
    @Override
    public String getAffectsString() {
        return "every ship destroyed in combat";
    }

    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        /*
    When any ship is destroyed in combat, harvest a Reclaim Package worth 1000/2000/3000/4000 reclaim, depending on hullsize. reclaim packages will then go to the nearest ship in the fleet. Any  Reclaim Packages that reaches there target will be converted into reclaim.
    for every 1000 reclaim in a ship, gain 1 control, rounded up.
    for every control, gain the ability to control one more Simulacrum Fighter Wings.
    Each Simulacrum Fighter Wing costs OP cost * ?? reclaim to produce, and can takes refit time * wing size * ?? seconds to produce.
    Simulacrum Fighters dont benefit from fighter modifiers, and rapidly decay, only being able to stay in combat for 60 seconds before being destroyed.
    Simulacrum Fighters have infinite engagement range.
    Simulacrum Fighters have -20% hull, -20% shield efficiency, and -10% damage
        *
        * */
        tooltip.addPara("When any ship is destroyed in combat, harvest a Reclaim Package worth %s/%s/%s/%s reclaim multiplied by the number of skills in this attribute other then this one, depending on hullsize. reclaim packages will then go to the nearest ship in the fleet. Any  Reclaim Packages that reaches there target will be converted into reclaim.",0f,Misc.getHighlightColor(), Misc.getHighlightColor(),""+Settings.NANO_THIEF_RECLAIM_GAIN[0],""+Settings.NANO_THIEF_RECLAIM_GAIN[1],""+Settings.NANO_THIEF_RECLAIM_GAIN[2],""+Settings.NANO_THIEF_RECLAIM_GAIN[3]);
        String percent = NANO_THIEF_RECLAIM_RECYCLE_PERCENT *100 +"%";
        tooltip.addPara("When a ship holding any amount of Reclaim is destroyed, add %s of the held Reclaim to the Reclaim Package",0f,Misc.getHighlightColor(), Misc.getHighlightColor(),percent);
        tooltip.addSpacer(10f);
        LabelAPI label = tooltip.addPara("\"Its an art you know. Salvaging ships on the battlefield, well under fire. There are legends of rebels harvesting whole fleets on the battlefields, sending the patchwork wreckage to attack there oppressors. Its an wonderful thing to watch. \n Makes me want to cry tears of joy. And envy.\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();


    }
    @Override
    public void applyEffectsAfterShipCreation(SCData data, ShipAPI ship, ShipVariantAPI variant, String id) {
        if (ship.getParentStation() != null || ship.getParentPieceId() != null){
            //log.info("NOT ADDING LISTENER FOR A SINGLE SHIP, BECAUSE IT IS A MODULE");
            return;
        }
        if (ship.hasListenerOfClass(NanoThief_ShipSkillsAdder.class)) return;
        try{
            if (ship.getHullSpec().getHullId().equals("Abyssal_XO_ReclaimCore")){
                //Settings.log.info("running logger for fighter calculations =)");
                return;
            }
        }catch (Exception e){

        }
            //List<NanoThief_ShipSkillsAdder> a = ship.getListenerManager().getListeners(NanoThief_ShipSkillsAdder.class);
            //listiner = a.get(0);
        ship.addListener(new NanoThief_ShipSkillsAdder(ship,data));
        /*/

        if (!Global.getCurrentState().equals(GameState.COMBAT)) return;
        if (!Global.getCombatEngine().hasPluginOfClass(NanoThief_BattleListener.class)) {
            Global.getCombatEngine().addPlugin(new NanoThief_BattleListener());
        }
        //data.getCommander();
        Nano_Thief_Stats stats = NanoThief_BattleListener.getStatsForShip(ship,data);
        if (stats == null){
            return;
        }
        stats.getAvailableShips().put(ship.getId(),ship);
        //stats.deployedDP+=ship;
        ship.addListener(new NanoThief_ShipSkills(stats,ship));
        //to do: add this ship into the master list of all ships part of this force. this will be useful =).
        //NanoThief_BattleListener;
        /**/
        //log.info("THIS IS BEFORE SHIP CREATION");
    }
    private static Logger log = Global.getLogger(NanoThief_Base.class);

    @Override
    public void applyEffectsBeforeShipCreation(SCData data, MutableShipStatsAPI stats, ShipVariantAPI variant, ShipAPI.HullSize hullSize, String id) {
        //if (!Global.getCurrentState().equals(GameState.COMBAT)) return;

        //log.info("THIS IS AFTER SHIP CREATION");
    }
    @Override
    public void advanceInCombat(SCData data, ShipAPI ship, Float amount) {
        /*I need to look up and find some type of 'deployment listener' to determine what ships are deployed.
        * I could also simply just.... not do that. and instead, just have 'Centralized Production' handle it, every second or two, adding in new ships (and removing old ones whenever it pleases.)*/
        if (!Global.getCombatEngine().hasPluginOfClass(NanoThief_BattleListener.class)) {
            Global.getCombatEngine().addPlugin(new NanoThief_BattleListener());
        }
        //Settings.log.info("calculating active ship with this skill: "+ship.getName());
    }

    @Override
    public void onActivation(SCData data) {
    }
}
