package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.Settings;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

public class NanoThief_6 extends Nano_Thief_SKill_Base{
    public String getAffectsString() {
        return "every ship destroyed in combat";
    }
    String aaa = "Overrides the 'Central Fabricator' skill, forcing this ship to be the Central Fabricator. Will do nothing if the Central Fabricator has already been selected";
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
    }

    @Override
    public void onActivation(SCData data) {
        FactionAPI faction = Global.getSector().getPlayerFaction();
        if (!faction.getKnownHullMods().contains(Settings.HULLMOD_CENTRAL_FAB)) {
            faction.addKnownHullMod(Settings.HULLMOD_CENTRAL_FAB);
        }
    }

}
