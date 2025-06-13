package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.subsystems.Cruise_System;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.magiclib.subsystems.MagicSubsystemsManager;
import second_in_command.SCData;

public class NanoThief_4 extends Nano_Thief_SKill_Base{
    private static final String sourceKey = "NanoThief_4";
    private static float speedChangeMulti = 10f;
    private static float hullChange = 0.9f;
    private static int quality = 1;
    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        int costReduction = (int) (100*(1-hullChange));
        tooltip.addPara("Gain %s quality", 0f,Misc.getHighlightColor(), Misc.getHighlightColor(), ""+quality);
        tooltip.addPara("Reduce hull by %s",0f,Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),costReduction+"%");

        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"Look, its simple. Use some of the budget for that new 'advanced' armor and put it into the internal systems. We will meet there spec requirements, and they wont ever know the difference\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();

    }
    @Override
    public void changeCombatSwarmStats(ShipAPI ship, Nano_Thief_Stats stats) {
        MagicSubsystemsManager.addSubsystemToShip(ship, new Cruise_System(ship,stats.getRange(),stats.getFighterHullSpec().getEngineSpec().getMaxSpeed() * speedChangeMulti));
        ship.getMutableStats().getHullBonus().modifyMult(sourceKey,hullChange);
    }
}
