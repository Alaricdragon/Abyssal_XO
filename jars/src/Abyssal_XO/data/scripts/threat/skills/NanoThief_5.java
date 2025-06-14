package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

public class NanoThief_5 extends Nano_Thief_SKill_Base{
    private static final String key = "AbyssalXO_Nano_Thief_Skill_5";

    private static final float timeMod = 1.1f;

    private static final float hullMod = 0.1f;
    private static final float armorMod = 0.1f;
    private static final float shieldMod = 0.9f;

    private static final float damageMod = 0.05f;
    private static final float fireMod = 0.1f;
    private static final float rechargeMod = 10f;//please dont ask
    private static final float fluxMod = 0.1f;


    private static final float costMod = 1.3f;
    private static final float buildTimeMod = 1.3f;

    @Override
    public float manufactureTimeChange(float time, ShipAPI target, Nano_Thief_Stats stats) {
        return time * buildTimeMod;
    }

    @Override
    public float costChange(float cost, ShipAPI target, Nano_Thief_Stats stats) {
        return cost * costMod;
    }

    @Override
    public float timeToLiveChange(float time, ShipAPI target, Nano_Thief_Stats stats) {
        return time * timeMod;
    }

    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        /*
        cost 30% more
        take 10% more time to build
        gains 10% Time To Live
        gains 10% max hp
        shields take 10% less damage
        gains 10% higher fire rate for all weapons
        gain 10% recharge rate for all weapons
        gain 10% flux dissipation
        gain 5% damage*/
        String timemod = ((int)((timeMod)*100))-100+"%";

        String hullmod = ((int)((hullMod)*100))+"%";
        String armormod = ((int)((armorMod)*100))+"%";
        String shieldmod = 100-(int)((shieldMod*100))+"%";
        String firerate = (int)(fireMod*100)+"%";
        String rechare = (int)(rechargeMod)+"%";
        String fluxmod = (int)(fluxMod*100)+"%";
        String damagemod = (int)(damageMod*100)+"%";

        //String damagemod = 100-((int)((ttlMod)*100))+"%";
        //tooltip.addPara("Gain the 'Overcharged' sub system, wish increased precived time flow by %s for %s seconds with a very long cooldown.",0, Misc.getHighlightColor(), Misc.getHighlightColor(),timeflowmod,timeflowdur);
        tooltip.addPara("Gain %s time to live",0, Misc.getHighlightColor(), Misc.getHighlightColor(),timemod);
        tooltip.addPara("Gain %s hull",0, Misc.getHighlightColor(), Misc.getHighlightColor(),hullmod);
        tooltip.addPara("Gain %s armor rating",0, Misc.getHighlightColor(), Misc.getHighlightColor(),armormod);
        tooltip.addPara("Gain %s shield strength",0, Misc.getHighlightColor(), Misc.getHighlightColor(),shieldmod);
        tooltip.addPara("Gain %s fire rate for all weapons",0, Misc.getHighlightColor(), Misc.getHighlightColor(),firerate);
        tooltip.addPara("Gain %s faster charge gain all weapons",0, Misc.getHighlightColor(), Misc.getHighlightColor(),rechare);
        tooltip.addPara("Gain %s flux dissipation",0, Misc.getHighlightColor(), Misc.getHighlightColor(),fluxmod);
        tooltip.addPara("Gain %s damage",0, Misc.getHighlightColor(), Misc.getHighlightColor(),damagemod);
        //tooltip.addPara("Lose %s time to live",0, Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),damagemod);


        String reclaim = (int)((costMod*100))-100+"%";
        String buildmod = (int)((buildTimeMod*100))-100+"%";
        tooltip.addPara("Increase reclaim cost by %s",0f,Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),reclaim);
        tooltip.addPara("Take %s more time to build",0,Misc.getNegativeHighlightColor(),Misc.getNegativeHighlightColor(),buildmod);

        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"Oh sure, we can increase quality. No cost to weapons, no broken systems, no striped armor, nothing. But it will cost you. And it will take time.\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();

    }

    @Override
    public void changeCombatSwarmStats(ShipAPI ship,ShipAPI fabricator, Nano_Thief_Stats stats) {
        ship.getMutableStats().getBeamWeaponDamageMult().modifyMult(key,damageMod);
        ship.getMutableStats().getMissileWeaponDamageMult().modifyMult(key,damageMod);
        ship.getMutableStats().getEnergyWeaponDamageMult().modifyMult(key,damageMod);
        ship.getMutableStats().getBallisticWeaponDamageMult().modifyMult(key,damageMod);

        ship.getMutableStats().getBallisticAmmoRegenMult().modifyPercent(key, fireMod);
        ship.getMutableStats().getEnergyAmmoRegenMult().modifyPercent(key, fireMod);
        ship.getMutableStats().getMissileAmmoRegenMult().modifyPercent(key, fireMod);

        ship.getMutableStats().getFluxDissipation().modifyFlat(key,stats.getFighterHullSpec().getFluxCapacity()*fluxMod);


        ship.getMutableStats().getHullBonus().modifyFlat(key,hullMod*stats.getFighterHullSpec().getHitpoints());
        ship.getMutableStats().getArmorBonus().modifyFlat(key,armorMod*stats.getFighterHullSpec().getArmorRating());
        if (stats.getFighterHullSpec().getShieldSpec() == null) return;
        ship.getMutableStats().getShieldDamageTakenMult().modifyMult(key,shieldMod);//stats.getFighterHullSpec().getShieldSpec().getFluxPerDamageAbsorbed());
    }
}
