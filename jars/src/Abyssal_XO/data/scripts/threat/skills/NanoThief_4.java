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
    private static final String key = "AbyssalXO_Nano_Thief_Skill_4";
    private static float speedChangeMulti = 0.75f;
    private static final float hullMod = 0.95f;
    private static final float armorMod = 0.95f;
    private static final float shieldMod = 0.05f;
    //private static float hullChange = 0.95f;
    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {

        String speedMod = (int)(((speedChangeMulti)*100))+"%";

        String hullmod = 100-((int)((hullMod)*100))+"%";
        String armormod = 100-((int)((armorMod)*100))+"%";
        String shieldmod = "5%";//(int)(((1+shieldMod)*100)-100)+"%";
        tooltip.addPara("Increase movement speed by %s when outside of combat",0, Misc.getHighlightColor(), Misc.getHighlightColor(),speedMod);
        tooltip.addPara("Lose %s hull",0, Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),hullmod);
        tooltip.addPara("Lose %s armor rating",0, Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),armormod);
        tooltip.addPara("Lose %s shield strength",0, Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),shieldmod);

        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"A few modifications here, a little striped armor there, Connecting the flux core with the ignition just right, and she will fly like a dream.\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();

    }
    @Override
    public void changeCombatSwarmStats(ShipAPI ship, Nano_Thief_Stats stats) {
        /*log.info("getting a single ships speed stats...");
        for (String a : ship.getMutableStats().getMaxSpeed().getFlatMods().keySet()){
            log.info("  flat mod as: "+ship.getMutableStats().getMaxSpeed().getFlatMods().get(a).value);
        }
        for (String a : ship.getMutableStats().getMaxSpeed().getMultMods().keySet()){
            log.info("  multi mod as: "+ship.getMutableStats().getMaxSpeed().getMultMods().get(a).value);
        }*/


        //ship.getMutableStats().getZeroFluxSpeedBoost().modifyFlat("aaaaaaaadsadsad",1000);
        MagicSubsystemsManager.addSubsystemToShip(ship, new Cruise_System(ship,stats.getRange(),stats.getFighterHullSpec().getEngineSpec().getMaxSpeed() * speedChangeMulti));
        ship.getMutableStats().getHullBonus().modifyMult(key,hullMod);
        ship.getMutableStats().getArmorBonus().modifyMult(key,armorMod);
        if (stats.getFighterHullSpec().getShieldSpec() == null) return;
        ship.getMutableStats().getShieldDamageTakenMult().modifyFlat(key,stats.getFighterHullSpec().getShieldSpec().getFluxPerDamageAbsorbed()*shieldMod);
    }
}
