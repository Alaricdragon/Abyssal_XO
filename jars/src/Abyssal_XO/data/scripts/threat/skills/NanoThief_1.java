package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

public class NanoThief_1 extends Nano_Thief_SKill_Base{
    private static final String sourceKey = "NanoThief_1";
    private static final int quality = 1;
    private static final int damageIncrease = 20;
    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        tooltip.addPara("Gain %s increased damage",0f,Misc.getHighlightColor(), Misc.getHighlightColor(),damageIncrease+"%");
        tooltip.addPara("Lose %s quality", 0f,Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(), ""+quality);

        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"Yes, its unstable. Hell, its practicly destroys itself when it fires! but dam if its not fun well it lasts\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();

    }
    @Override
    public void changeCombatSwarmStats(ShipAPI ship, Nano_Thief_Stats stats) {
        float multi = 1 + ((float)damageIncrease / 100);
        ship.getMutableStats().getEnergyWeaponDamageMult().modifyMult(sourceKey,multi);
        ship.getMutableStats().getMissileWeaponDamageMult().modifyMult(sourceKey,multi);
        ship.getMutableStats().getBeamWeaponDamageMult().modifyMult(sourceKey,multi);
        ship.getMutableStats().getBallisticWeaponDamageMult().modifyMult(sourceKey,multi);
    }
}
