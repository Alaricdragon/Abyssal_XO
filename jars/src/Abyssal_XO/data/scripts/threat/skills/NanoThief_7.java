package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

import java.util.Random;

public class NanoThief_7 extends Nano_Thief_SKill_Base{
    private static final float costChange = 1.25f;
    private static final int[] quality = {1,2,3,4};
    private static final float[] odds = {15,20,30,35};
    private static final float totalOdds = 100;
    Random ran = new Random();
    @Override
    public float costChange(float cost, ShipAPI target, Nano_Thief_Stats stats) {
        return cost * costChange;
    }
    @Override
    public float qualityChange(float reclaim, ShipAPI target, Nano_Thief_Stats stats) {
        float ran2 = ran.nextFloat()*totalOdds;
        for (int a = 0; a < odds.length; a++){
            if (ran2 < odds[a]) return reclaim+quality[a];
            ran2 -= odds[a];
        }
        return reclaim+quality[quality.length-1];
    }

    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        int costReduction = (int) ((costChange-1)*100);
        String stringThing = "";
        for (int a = 0; a < odds.length; a++){
            stringThing+=quality[a];
            stringThing+=" ("+(int)(odds[a])+"%)";
            if (a == odds.length-1) break;
            stringThing+=", ";
        }
        tooltip.addPara("gain %s quality, at random", 0f,Misc.getHighlightColor(), Misc.getHighlightColor(), stringThing);
        tooltip.addPara("Increase reclaim cost by %s",0f,Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),costReduction+"%");

        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"Not all martial is created equal. Some are more useful for advanced, complicated components. Some are better for other things. All we have to do is give up on making everything the same, Because we will never get the same piece of salvage twice.\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();

    }

}
