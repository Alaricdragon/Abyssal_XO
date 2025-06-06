package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_BattleListener;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_ShipStats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.apache.log4j.Logger;
import second_in_command.SCData;

public class NanoThief_Base extends Nano_Thief_SKill_Base {
    /*NOTE:
    * this is active, however it only effects destroyed friendly ships. I need something more...(also spawns infinit ships lol)*/
    @Override
    public String getAffectsString() {
        return "every ship destroyed in combat";
    }

    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        //this is effectivly template data. for now.
        tooltip.addPara("Halves the speed at which combat readiness degrades after peak performance time runs out", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
        tooltip.addPara("Powers up imprints from the \"Dance between Realms\" skill", 0f, Misc.getHighlightColor(), Misc.getHighlightColor());
        tooltip.addPara("   - Turns Helmsmanship, Field Modulation and Systems Expertise Elite", 0f, Misc.getTextColor(), Misc.getHighlightColor(), "Helmsmanship", "Field Modulation", "Systems Expertise");
        tooltip.addPara("   - Provides imprints with non-elite Energy Weapon Mastery and Gunnery Implants", 0f, Misc.getTextColor(), Misc.getHighlightColor(), "Energy Weapon Mastery", "Gunnery Implants");

        tooltip.addSpacer(10f);

        var label = tooltip.addPara("\"Me's a crowd.\"", Misc.getHighlightColor(), 0f);
        label.italicize();

    }
    @Override
    public void applyEffectsAfterShipCreation(SCData data, ShipAPI ship, ShipVariantAPI variant, String id) {
    }

    @Override
    public void applyEffectsBeforeShipCreation(SCData data, MutableShipStatsAPI stats, ShipVariantAPI variant, ShipAPI.HullSize hullSize, String id) {
    }
    @Override
    public void advanceInCombat(SCData data, ShipAPI ship, Float amount) {
        if (!Global.getCombatEngine().hasPluginOfClass(NanoThief_BattleListener.class)) {
            Global.getCombatEngine().addPlugin(new NanoThief_BattleListener());
        }

        /*IntervalUtil interval = (IntervalUtil) Global.getCombatEngine().getCustomData().get("Sic_NanoFief_NanoFief_Waiter");
        if (interval == null) {
            interval = new IntervalUtil(0.25f, 0.33f);
            Global.getCombatEngine().getCustomData().put("Sic_NanoFief_NanoFief_Waiter", interval);
        }
        interval.advance(amount);*/

        /*if (interval.intervalElapsed()) {
            for (ShipAPI a : Global.getCombatEngine().getShips()) {
                if (a.hasListenerOfClass()){
                    ship.addListener();
                }
                if (!toApply.hasListenerOfClass(WitchcraftKillingBlowHandler::class.java)) {
                    toApply.addListener(WitchcraftKillingBlowHandler())
                }
            }
        }*/
    }


    public void changeReclaimStats(ShipAPI ship,int quality){
        //changes the stats of the reclaim swarm
    }
    public void changeCombatSwarmStats(ShipAPI ship,int quality){
        applyBaseQualityChange(ship, quality);
        //changes the stats of the combat swarm
    }
    public void changeDefenderSwarmStats(ShipAPI ship,int quality){
        applyBaseQualityChange(ship, quality);
        //changes the stats of the defense swarm.
    }
    private static String source = "Abyssal_XO_Nano_Theif_Stats";
    private void applyBaseQualityChange(ShipAPI ship,int quality){
        //quality = 10;
        log.info("attempting to apply base quality upgrade with a quality of "+quality);
        if (quality < 0){
            log.info("quality is in fact, decreasing...");
            float multi = 1;
            while (quality < 0) {
                multi *= 0.9;
                quality++;
            }
            log.info("got total quality modifier as: "+multi);

            ship.getMutableStats().getHullBonus().modifyMult(source,multi);

            ship.getMutableStats().getMaxSpeed().modifyMult(source,multi);
            ship.getMutableStats().getAcceleration().modifyMult(source,multi);

            ship.getMutableStats().getEnergyAmmoRegenMult().modifyMult(source,multi);
            ship.getMutableStats().getBallisticAmmoRegenMult().modifyMult(source,multi);
            ship.getMutableStats().getMissileAmmoRegenMult().modifyMult(source,multi);

            ship.getMutableStats().getBallisticRoFMult().modifyMult(source,multi);
            ship.getMutableStats().getEnergyRoFMult().modifyMult(source,multi);
            ship.getMutableStats().getMissileRoFMult().modifyMult(source,multi);

        }else if (quality > 0){
            log.info("quality is in fact, increasing...");
            float multi = (quality*0.1f) + 1;
            log.info("got total quality modifier as: "+multi);
            //ship.getMutableStats().getTimeMult();
            ship.getMutableStats().getHullBonus().modifyMult(source,multi);

            ship.getMutableStats().getMaxSpeed().modifyMult(source,multi);
            ship.getMutableStats().getAcceleration().modifyMult(source,multi);

            ship.getMutableStats().getEnergyAmmoRegenMult().modifyMult(source,multi);
            ship.getMutableStats().getBallisticAmmoRegenMult().modifyMult(source,multi);
            ship.getMutableStats().getMissileAmmoRegenMult().modifyMult(source,multi);

            ship.getMutableStats().getBallisticRoFMult().modifyMult(source,multi);
            ship.getMutableStats().getEnergyRoFMult().modifyMult(source,multi);
            ship.getMutableStats().getMissileRoFMult().modifyMult(source,multi);
        }
    }












        /*
        if (Global.getCombatEngine().hasPluginOfClass(NanoThief_BattleListener.class)) return;
        Global.getCombatEngine().addPlugin(new NanoThief_BattleListener());
        //note: this might only effect ships with the commander? will need to check...
        super.advanceInCombat(data, ship, amount);
        if (!ship.isHulk() || ship.hasTag(TAG_HASRECLAMED)) return;
        if (ThreatCombatStrategyAI.isFabricator(ship)) return;

        float elapsedAsHulk = 0f;
        String key = "SiC_NanoThief_elapsedAsHulkKey";
        if (ship.getCustomData().containsKey(key)) {
            elapsedAsHulk = (float) ship.getCustomData().get(key);
        }
        elapsedAsHulk += amount;
        ship.setCustomData(key, elapsedAsHulk);
        if (elapsedAsHulk > 1f) {
            CombatEngineAPI engine = Global.getCombatEngine();
            int owner = ship.getOriginalOwner();
            boolean found = false;
            for (ShipAPI curr : engine.getShips()) {
                if (curr == ship || curr.getOwner() != owner) continue;
                if (curr.isHulk() || curr.getOwner() == 100) continue;
                if (!Nano_Thief_Utils.canAcceptReclaim(curr)) continue;
                //if (curr.getCurrentCR() >= 1f) continue;
                found = true;
                break;
            }
            if (found) {
                Global.getCombatEngine().addPlugin(new ThreatShipReclamationScript(ship, 3f));
            } else {
                ship.setCustomData(key, 0f);
            }
        }
    }*/
}
//note: I was looking at 'SotF.TendToOurGarden' for this. it might be useful for defensive reasons.
/*class NanoThief_SwarmCreater implements AdvanceableListener {
    @Override
    public void advance(float amount) {

    }
}*/
