package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

public class NanoThief_9 extends Nano_Thief_SKill_Base{
    private static float costChange = 0.8f;
    private static float controlChange = 0.5f;
    private static float buildTimeChange = 0.5f;
    @Override
    public float costChange(float cost, ShipAPI target, Nano_Thief_Stats stats) {
        return cost * costChange;
    }

    @Override
    public float manufactureTimeChange(float time, ShipAPI target, Nano_Thief_Stats stats) {
        return time*buildTimeChange;
    }

    @Override
    public float reclaimPerControlChange(float reclaim, ShipAPI target, Nano_Thief_Stats stats) {
        return reclaim*controlChange;
    }

    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        String costmod = 100-((int)((costChange)*100))+"%";
        String buildmod = 100-((int)((buildTimeChange)*100))+"%";
        String controlmod = 100-((int)((controlChange)*100))+"%";

        tooltip.addPara("Cost %s less",0,Misc.getHighlightColor(),Misc.getHighlightColor(),costmod);
        tooltip.addPara("Take %s less time to build",0,Misc.getHighlightColor(),Misc.getHighlightColor(),buildmod);
        tooltip.addPara("Use %s less control",0,Misc.getHighlightColor(),Misc.getHighlightColor(),controlmod);

        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"Efficiency is not something mastered in a day. Sometimes, it can take many cycles to get everything working just right. To obtain... Perfection.\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();

    }
}
