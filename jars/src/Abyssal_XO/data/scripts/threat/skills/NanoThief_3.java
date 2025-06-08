package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

public class NanoThief_3 extends Nano_Thief_SKill_Base{
    private static final String sourceKey = "NanoThief_3";
    private static final float hullChange = 0.50f;
    private static final int quality = 1;
    @Override
    public float qualityChange(float reclaim, ShipAPI target, Nano_Thief_Stats stats) {
        return reclaim-quality;
    }

    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        int costReduction = (int) ((hullChange)*100);
        tooltip.addPara("Increase hull by %s",0f,Misc.getHighlightColor(), Misc.getHighlightColor(),costReduction+"%");
        tooltip.addPara("Lose %s quality", 0f,Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(), ""+quality);

        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"Sometimes, you just need to weld some armor to your hull. Just to make sure she makes it out alive, despite the cost\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();

    }
    @Override
    public void changeCombatSwarmStats(ShipAPI ship, int quality, Nano_Thief_Stats stats) {
        float multi = 1 + ((float)hullChange);
        ship.getMutableStats().getHullBonus().modifyMult(sourceKey,multi);
    }
}
