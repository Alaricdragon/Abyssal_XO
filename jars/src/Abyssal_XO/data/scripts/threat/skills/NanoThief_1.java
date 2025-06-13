package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.subsystems.DamageOverTime_System;
import Abyssal_XO.data.scripts.threat.subsystems.Overcharge_System;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import lombok.Getter;
import org.magiclib.subsystems.MagicSubsystemsManager;
import second_in_command.SCData;

public class NanoThief_1 extends Nano_Thief_SKill_Base{
    private static final String key = "AbyssalXO_Nano_Thief_Skill_1";
    private static final float hullMod = 0.8f;
    private static final float armorMod = 0.8f;
    private static final float shieldMod = 0.2f;
    private static final float ttlMod = 0.5f;

    @Getter
    private static final float timeFlowMod = 1f;
    @Getter
    private static final float timeFlowDur = 20;//10 seconds precived.
    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        /*
            Gain the 'Overcharged' sub system, wish increases precived time flow by 200% for 30 seconds with a very long cooldown.
        lose 50% time to live,
        50% hp,
        lost 50% armor
        50% .

         */
        String timeflowmod = (int)(100*(timeFlowMod))+"%";
        String timeflowdur = (int)(timeFlowDur/timeFlowMod)+"";

        String hullmod = 100-((int)((hullMod)*100))+"%";
        String armormod = 100-((int)((armorMod)*100))+"%";
        String shieldmod = (int)(((1+shieldMod)*100)-100)+"%";
        String damagemod = 100-((int)((ttlMod)*100))+"%";
        tooltip.addPara("Gain the 'Overcharged' sub system, wish increased precived time flow by %s for %s seconds with a very long cooldown.",0, Misc.getHighlightColor(), Misc.getHighlightColor(),timeflowmod,timeflowdur);
        tooltip.addPara("Lose %s hull",0, Misc.getTextColor(), Misc.getNegativeHighlightColor(),hullmod);
        tooltip.addPara("Lose %s armor rating",0, Misc.getTextColor(), Misc.getNegativeHighlightColor(),armormod);
        tooltip.addPara("Lose %s shield strength",0, Misc.getTextColor(), Misc.getNegativeHighlightColor(),shieldmod);
        tooltip.addPara("Lose %s time to live",0, Misc.getTextColor(), Misc.getNegativeHighlightColor(),damagemod);

        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"Yes, its unstable. Hell, its practicly destroys itself when it fires! but dam if its not fun well it lasts\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();

    }

    @Override
    public float timeToLiveChange(float time, ShipAPI target, Nano_Thief_Stats stats) {
        return time * ttlMod;
    }

    @Override
    public void changeCombatSwarmStats(ShipAPI ship, Nano_Thief_Stats stats) {
        MagicSubsystemsManager.addSubsystemToShip(ship, new Overcharge_System(ship,stats.getRange()));
        ship.getMutableStats().getHullBonus().modifyMult(key,hullMod);
        ship.getMutableStats().getArmorBonus().modifyMult(key,armorMod);
        if (stats.getFighterHullSpec().getShieldSpec() == null) return;
        ship.getMutableStats().getShieldDamageTakenMult().modifyFlat(key,stats.getFighterHullSpec().getShieldSpec().getFluxPerDamageAbsorbed()*shieldMod);
    }
}
