package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

public class NanoThief_6 extends Nano_Thief_SKill_Base{
    private static final float fabCostChange = 0.67f;
    private static final int fabQuality = 3;
    private static final float fabControl = 0.66f;//50% more control.
    private static final float fabProductionSpeed = 0.25f;
    private static final String fabControlStr = "50%";//please dont ask.

    private static final int capitalQuality = 1;
    @Override
    public float costChange(float cost, ShipAPI target, Nano_Thief_Stats stats) {
        if (target.equals(stats.getCentralFab())) return cost * fabCostChange;
        return cost;
    }
    @Override
    public float reclaimPerControlChange(float reclaim, ShipAPI target, Nano_Thief_Stats stats) {
        if (target.equals(stats.getCentralFab())) return reclaim * fabControl;
        return reclaim;
    }

    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        tooltip.addPara("When the first reclaim package is created, the largest, highest mass ship in your fleet is marked as the Central Fabricator. Reclaim Packages will always attempt to move to the Central Fabricator, provided it exists. \nSwarms produced by the Central Fabricator have the following changes:",0f,Misc.getHighlightColor(), Misc.getHighlightColor());

        int display = (int) ((1 - fabCostChange)*100);
        tooltip.addPara("   -Reduce reclaim cost by %s",0f,Misc.getTextColor(), Misc.getHighlightColor(),display+"%");
        tooltip.addPara("   -Gain %s quality",0f,Misc.getTextColor(), Misc.getHighlightColor(),fabQuality+"");
        tooltip.addPara("   -Increases the number of swarms that can be deployed at once by %s",0f,Misc.getTextColor(), Misc.getHighlightColor(),fabControlStr);
        display = (int) (((1/fabProductionSpeed)-1)*100);
        tooltip.addPara("increases production speed by %s",0f,Misc.getTextColor(), Misc.getHighlightColor(),display+"%");


        tooltip.addPara("If the Central Fabricator is destroyed or retreats Reclaim Packages will attempt to move to the nearest capital ship, provided one exists. \nSwarms produced by the Capital Ships that are not the Central Fabricator have the following changes:",0f,Misc.getHighlightColor(), Misc.getHighlightColor());
        tooltip.addPara("   -Gain %s quality",0f,Misc.getTextColor(), Misc.getHighlightColor(),capitalQuality+"");

        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"By having a single, centralized factory we can streamline important production tasks, Dramatically improving output and quality, for no increase in cost. \nNow we just need to worry about the logistics\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();

    }
}
