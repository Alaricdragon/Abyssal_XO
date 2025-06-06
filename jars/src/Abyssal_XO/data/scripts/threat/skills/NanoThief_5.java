package Abyssal_XO.data.scripts.threat.skills;

import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

public class NanoThief_5 extends Nano_Thief_SKill_Base{
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
    }
}
