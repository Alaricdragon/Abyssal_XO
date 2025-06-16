package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_ShipStats;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

import java.util.List;
import java.util.Random;

public class NanoThief_7 extends Nano_Thief_SKill_Base{
    private static final int storgeChange = 1;
    private static final int storgeIncreasePerReclaim = 5000;

    @Override
    public String getAffectsString() {
        return "all ships in fleet";
    }

    @Override
    public float storedSwarmChange(float stored, ShipAPI target, Nano_Thief_Stats stats) {
        int pow = 0;
        if (target.hasListenerOfClass(NanoThief_ShipStats.class)) {
            NanoThief_ShipStats listiner;
            List<NanoThief_ShipStats> a = target.getListenerManager().getListeners(NanoThief_ShipStats.class);
            listiner = a.get(0);
            pow = (int) (listiner.getReclaim() / storgeIncreasePerReclaim);
        }
        return stored+storgeChange+pow;
    }

    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        String stra = ""+storgeChange;
        String strb = ""+storgeIncreasePerReclaim;
        String strc = "1";
        tooltip.addPara("Increase the max number of Simulacrum Fighter Wings that can be stored on the ship by %s",0, Misc.getHighlightColor(), Misc.getHighlightColor(),stra);
        tooltip.addPara("For every %s reclaim in ship, increase the max number of Simulacrum Fighter Wing that can be stored by an additional %s",0, Misc.getHighlightColor(), Misc.getHighlightColor(),strb,strc);
        tooltip.addSpacer(10f);
        tooltip.addPara("'stored' Simulacrum Fighter Wings can be produced even when a given ship has no available control. The stored Simulacrum Fighter Wings can be deployed very quickly when the size of your swarm is less then the number of Simulacrum Fighter Wing this ship can maintain.",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        //tooltip.addPara("gain %s quality, at random", 0f,Misc.getHighlightColor(), Misc.getHighlightColor(), stringThing);
        //tooltip.addPara("Increase reclaim cost by %s",0f,Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),costReduction+"%");

        tooltip.addSpacer(10f);

        //LabelAPI label = tooltip.addPara("\"Not all martial is created equal. Some are more useful for advanced, complicated components. Some are better for other things. All we have to do is give up on making everything the same, Because we will never get the same piece of salvage twice.\"", Misc.getTextColor(), 0f);
        LabelAPI label = tooltip.addPara("\"By creating additional storge areas for our products, we can manufacture in larger amounts, even when logistics is not up to the task of distribution\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();

    }

}
