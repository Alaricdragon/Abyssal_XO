package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

public class NanoThief_8 extends Nano_Thief_SKill_Base{
    private static float costChange = 0.67f;
    private static int quality = 1;
    @Override
    public float costChange(float cost, ShipAPI target, Nano_Thief_Stats stats) {
        return cost * costChange;
    }
    @Override
    public float qualityChange(float reclaim, ShipAPI target, Nano_Thief_Stats stats) {
        return reclaim-quality;
    }

    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        int costReduction = (int) ((1 - costChange)*100);
        tooltip.addPara("Reduce reclaim cost by %s",0f,Misc.getHighlightColor(), Misc.getHighlightColor(),costReduction+"%");
        tooltip.addPara("Lose %s quality", 0f,Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(), ""+quality);

        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"I don't care what it takes, I don't even care if the craft explodes the moment we set foot on it. If we cant meet quotas, some safety concerns will be the least of our worry's!.\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();

    }
}
