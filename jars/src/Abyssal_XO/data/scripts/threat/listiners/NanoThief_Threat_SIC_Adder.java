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
            if (a.hasTag(SHIP_UNDER_CONSTRUCTION) && !a.getVariant().getHullMods().contains(Settings.SIC_CONTROL_HULLMOD) && !a.getVariant().getHullMods().contains("sc_skill_controller")){
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
        SICSkillControllerBackup.addShipAfterShipSpawns(shipAPI, fleet);
    }
}
