package Abyssal_XO.data.scripts.threat.listiners;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.hullmods.SICSkillControllerBackup;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.loading.VariantSource;

import java.util.HashMap;
import java.util.List;

    import static com.fs.starfarer.api.impl.combat.threat.ThreatShipConstructionScript.SHIP_UNDER_CONSTRUCTION;

public class NanoThief_Threat_SIC_Adder extends BaseEveryFrameCombatPlugin {
    public NanoThief_Threat_SIC_Adder(){
        SICSkillControllerBackup.member_map = new HashMap<>();
    }
    float cooldown = 2;
    float time = cooldown;
    public void advance(float amount, List<InputEventAPI> events) {
        time -= amount;
        if (time > 0) return;
        time = cooldown;
        //for ()
        //events.get(0).getEventType().equals(CombatEvent);
        CombatEngineAPI engine = Global.getCombatEngine();
        for (ShipAPI a : engine.getShips()) {
            if (a.isHulk()) continue;
            if (a.getHullSize() == ShipAPI.HullSize.FIGHTER) continue;
            if (a.hasTag(SHIP_UNDER_CONSTRUCTION) && !a.getVariant().getHullMods().contains("Abussal_XO_SIC_controler") && !a.getVariant().getHullMods().contains("sc_skill_controller")){
                int force = a.getOriginalOwner();
                for (ShipAPI b : engine.getShips()){
                    if (b.getOriginalOwner() != force) continue;
                    if (b.getFleetMember() != null && b.getFleetMember().getFleetData() != null && b.getFleetMember().getFleetData().getFleet() != null){
                        refitShip(a,b.getFleetMember().getFleetData().getFleet());
                        //time = -1;//break this, then run it again immanently.
                        //return;
                        break;
                    }
                }
            }
        }
            //events.get(0).getEventClass().equals()
    }
    private void refitShip(ShipAPI shipAPI, CampaignFleetAPI fleet){
        /*todo:
            1: try seeing if said ships already have a fleet by defalt (if so, filter for fleets that have SC data)
            2: run checks in hullmod to see when a giving bit of code fails. so I can have some fucking logs about what is going on
            3: run tests to try and determine if simulacrum fighters and simulacrum ships still work.
            4: make sure nano-thief is being added at the right time with my code...? (its not. on added to combat it runs.)
            5: try 'shipAPI.applyEffectsAfterShipAddedToCombatEngine();' to see if that helps

         */
        Settings.log.info("attempting to add hullmods to a single ship of name, id: "+shipAPI.getName()+" id: "+shipAPI.getFleetMember().getId());
        SICSkillControllerBackup.member_map.put(shipAPI.getFleetMember(),fleet);
        //shipAPI.getFleetMember().setCustomData(NANO_THIEF_SIC_HULLMOD_FLEET_KEY,fleet);
        ShipVariantAPI OVERWRITER = shipAPI.getVariant();//Global.getSettings().getVariant("Abyssal_XO_ReclaimCore_Blank").clone();
        OVERWRITER.setSource(VariantSource.REFIT);
        //OVERWRITER.setWingId(0,skills.stats.OF_fighterToBuild);
        OVERWRITER.addMod("Abussal_XO_SIC_controler");
        //shipAPI.getVariant().getHullMods();
        shipAPI.getFleetMember().setVariant(OVERWRITER,false,true);//setVariant(OVERWRITER,false,true);
        Settings.log.info("does have hullmod: "+shipAPI.getVariant().hasHullMod("Abussal_XO_SIC_controler"));
    }
}
