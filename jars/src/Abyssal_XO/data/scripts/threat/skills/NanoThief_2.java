package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import lombok.Getter;
import second_in_command.SCData;

public class NanoThief_2 extends Nano_Thief_SKill_Base{
    private static final String key = "AbyssalXO_Nano_Thief_Skill_2";
    private static final float hullMod = 0.95f;
    private static final float armorMod = 0.95f;
    private static final float shieldMod = 0.05f;//note: the shield mod is bugged at this level. so ya.
    private static final float damageMod = 0.95f;

    private static final float costMod = 0.8f;
    private static final float buildTimeMod = 0.7f;
    private static final float controlMod = 0.9f;

    @Override
    public float costChange(float cost, ShipAPI target, Nano_Thief_Stats stats) {
        return cost * costMod;
    }

    @Override
    public float manufactureTimeChange(float time, ShipAPI target, Nano_Thief_Stats stats) {
        return time * buildTimeMod;
    }

    @Override
    public float reclaimPerControlChange(float reclaim, ShipAPI target, Nano_Thief_Stats stats) {
        return reclaim * controlMod;
    }

    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        String costmod = 100-((int)((costMod)*100))+"%";
        String buildmod = 100-((int)((buildTimeMod)*100))+"%";
        String controlmod = 100-((int)((controlMod)*100))+"%";

        String hullmod = 100-((int)((hullMod)*100))+"%";
        String armormod = 100-((int)((armorMod)*100))+"%";
        String shieldmod = "5%";//(int)((shieldMod*100)-100)+"%";
        String damagemod = 100-((int)((damageMod)*100))+"%";
        tooltip.addPara("Cost %s less",0,Misc.getHighlightColor(),Misc.getHighlightColor(),costmod);
        tooltip.addPara("Take %s less time to build",0,Misc.getHighlightColor(),Misc.getHighlightColor(),buildmod);
        tooltip.addPara("Use %s less control",0,Misc.getHighlightColor(),Misc.getHighlightColor(),controlmod);
        tooltip.addPara("Lose %s hull",0, Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),hullmod);
        tooltip.addPara("Lose %s armor rating",0, Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),armormod);
        tooltip.addPara("Lose %s shield strength",0, Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),shieldmod);
        tooltip.addPara("Lose %s damage",0, Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),damagemod);
        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"I don't care what it takes, I don't even care if the craft explodes the moment we set foot on it. If we cant meet quotas, some safety concerns will be the least of our worry's!.\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();
    }
    @Override
    public void changeCombatSwarmStats(ShipAPI ship,ShipAPI fabricator, Nano_Thief_Stats stats) {
        ship.getMutableStats().getHullBonus().modifyMult(key,hullMod);
        ship.getMutableStats().getArmorBonus().modifyMult(key,armorMod);

        ship.getMutableStats().getBeamWeaponDamageMult().modifyMult(key,damageMod);
        ship.getMutableStats().getMissileWeaponDamageMult().modifyMult(key,damageMod);
        ship.getMutableStats().getEnergyWeaponDamageMult().modifyMult(key,damageMod);
        ship.getMutableStats().getBallisticWeaponDamageMult().modifyMult(key,damageMod);

        if (stats.getFighterHullSpec().getShieldSpec() == null) return;
        ship.getMutableStats().getShieldDamageTakenMult().modifyFlat(key,stats.getFighterHullSpec().getShieldSpec().getFluxPerDamageAbsorbed()*shieldMod);
    }
}
