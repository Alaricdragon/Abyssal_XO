package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

public class NanoThief_5 extends Nano_Thief_SKill_Base{
    private static float timeChange = 3f;
    private static float costChange = 1.1f;
    private static int quality = 1;

    @Override
    public float manufactureTimeChange(float time, ShipAPI target, Nano_Thief_Stats stats) {
        return time * timeChange;
    }

    @Override
    public float costChange(float cost, ShipAPI target, Nano_Thief_Stats stats) {
        return cost * costChange;
    }
    @Override
    public float qualityChange(float reclaim, ShipAPI target, Nano_Thief_Stats stats) {
        return reclaim+quality;
    }

    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        int costReduction = (int) ((costChange-1)*100);
        int timeReduction = (int) ((1-(1/timeChange))*100);
        tooltip.addPara("Gain %s quality", 0f,Misc.getHighlightColor(), Misc.getHighlightColor(), ""+quality);
        tooltip.addPara("Increase reclaim cost by %s",0f,Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),costReduction+"%");
        tooltip.addPara("Decreases production speed by %s",0f,Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),timeReduction+"%");

        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"Oh sure, we can increase quality. No cost to weapons, no broken systems, no striped armor, nothing. But it will cost you. And it will take time.\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();

    }
}
