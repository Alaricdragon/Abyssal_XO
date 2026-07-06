package Abyssal_XO.data.scripts.threat.listiners;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.hullmods.SICSkillControllerBackup;
import Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_Construction;
import Abyssal_XO.data.scripts.threat.AI.Nano_Thief_MasteryConstructionScript;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_MasteryShipStats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.combat.threat.RoilingSwarmEffect;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.loading.VariantSource;

import java.util.List;

public class NanoThief_Mastery_TransitionData extends BaseEveryFrameCombatPlugin {
    ShipAPI ship;
    Nano_Thief_Stats stats;
    NanoThief_MasteryShipStats constructionDatas;
    //Nano_Thief_MasteryConstructionScript constructionScript;
    float cr;
    Nano_Thief_AI_Construction linkedSwarmAI;
    public NanoThief_Mastery_TransitionData(ShipAPI ship, Nano_Thief_Stats stats, NanoThief_MasteryShipStats constructionDatas, Nano_Thief_MasteryConstructionScript constructionScript, float cr, Nano_Thief_AI_Construction linkedSwarmAI){
        this.ship = ship;
        this.stats = stats;
        this.constructionDatas = constructionDatas;
        //this.constructionScript = constructionScript;
        this.cr = cr;
        this.linkedSwarmAI = linkedSwarmAI;
    }
    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        Global.getCombatEngine().removePlugin(this);
        actavate();
    }
    private void actavate(){
        RoilingSwarmEffect swarm = RoilingSwarmEffect.getSwarmFor(ship);
        if (swarm != null) {
            //FleetMemberAPI temp = Global.getSettings().createFleetMember(FleetMemberType.SHIP, constructionDatas.ship.getVariant().clone());
            //ShipAPI ship2 = Global.getCombatEngine().getFleetManager(ship.getOriginalOwner()).spawnFleetMember(temp,ship.getCopyLocation(),ship.getFacing(),0f);
            //if (true) return;
            //SICSkillControllerBackup.fleet_global = stats.fleet.getFleet();
            FleetMemberAPI memberCopy = Global.getSettings().createFleetMember(FleetMemberType.SHIP, constructionDatas.ship.getVariant().clone());
            //FleetMemberAPI memberCopy = Global.getSettings().createFleetMember(FleetMemberType.SHIP,constructionDatas.ship.getVariant().clone());
            //FleetMemberAPI memberCopy = Global.getSettings().createFleetMember(FleetMemberType.SHIP,Global.getSettings().getVariant("onslaught_mk1_Ancient") );//constructionDatas.ship.getVariant().clone());
            SICSkillControllerBackup.member_map.put(memberCopy,stats.fleet.getFleet());
            //Settings.log.info("GOT MEMBER ID AS (a): "+memberCopy.getId());
            //FleetMemberAPI memberCopy = Global.getSettings().createFleetMember(FleetMemberType.SHIP, Global.getSettings().getVariant(Settings.NANO_THIEF_MASTERY_BASESHIP));
            //memberCopy.setOwner(ship.getOwner());
            memberCopy.setOwner(ship.getOriginalOwner());
            memberCopy.setFleetCommanderForStats(stats.commander,stats.fleet);
            memberCopy.setShipName(constructionDatas.name);
            //Utils.applyShipIntoFleet(stats.fleet,memberCopy);
            //stats.fleet.addFleetMember(memberCopy);
            //stats.fleet.removeFleetMember(memberCopy);
            ShipVariantAPI OVERWRITER = memberCopy.getVariant();//Global.getSettings().getVariant("Abyssal_XO_ReclaimCore_Blank").clone();
            OVERWRITER.setSource(VariantSource.REFIT);
            OVERWRITER.addMod("Abyssal_XO_DC");
            OVERWRITER.addMod(Settings.SIC_CONTROL_HULLMOD);
            memberCopy.setOwner(ship.getOwner());
            memberCopy.setVariant(OVERWRITER,false,true);
            memberCopy.getStats().getMinCrewMod().modifyMult("Abyssal_XO",0);
            //Settings.log.info("GOT MEMBER ID AS (b): "+memberCopy.getId());
            /*Settings.log.info("HERE: GETTING DATA: "+memberCopy.isMothballed()); //get if ship is mothballed?
            //memberCopy.getFleetData().getFleet();
            String out = "";
            //out += "got fleet data name as: "+memberCopy.getFleetData().getFleet().getName()+"";
            out += ",-got all hull mods on new ship as: ";
            for (String a : memberCopy.getVariant().getHullMods()){
                out+=a+", ";
            }
            Settings.log.info(out);*/
            //memberCopy.getStats().getDynamic().getStat()
            //memberCopy.getStats().
            //FleetMemberAPI memberCopy = constructionDatas.ship;

            //memberCopy.getStats().getMaxCombatReadiness().setBaseValue(9999);
            //memberCopy;
            //memberCopy.getStats().getMaxCombatReadiness().modifyFlat();
            //memberCopy;
            linkedSwarmAI.constructionScript = new Nano_Thief_MasteryConstructionScript(
                    memberCopy, ship, 1f, (float) constructionDatas.buildTime,cr,constructionDatas.cost,stats);
            Global.getCombatEngine().addPlugin(linkedSwarmAI.constructionScript);
        }
    }
}
