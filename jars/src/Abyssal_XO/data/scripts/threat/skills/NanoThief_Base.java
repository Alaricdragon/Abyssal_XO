package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.dialogPlugin.Nano_Thief_dialog;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_BattleListener;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CharacterDataAPI;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

public class NanoThief_Base extends Nano_Thief_SKill_Base {
    private static final String key = "AbyssalXO_Nano_Thief_Skill_0";
    private static final float hullMod = 0.9f;
    private static final float armorMod = 0.9f;
    private static final float shieldMod = 0.1f;
    private static final float damageMod = 0.95f;

    @Override
    public String getAffectsString() {
        return "every ship destroyed in combat";
    }

    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        /*
    When any ship is destroyed in combat, harvest a Reclaim Package worth 1000/2000/3000/4000 reclaim, depending on hullsize. reclaim packages will then go to the nearest ship in the fleet. Any  Reclaim Packages that reaches there target will be converted into reclaim.
    for every 1000 reclaim in a ship, gain 1 control, rounded up.
    for every control, gain the ability to control one more Simulacrum Fighter Wings.
    Each Simulacrum Fighter Wing costs OP cost * ?? reclaim to produce, and can takes refit time * wing size * ?? seconds to produce.
    Simulacrum Fighters dont benefit from fighter modifiers, and rapidly decay, only being able to stay in combat for 60 seconds before being destroyed.
    Simulacrum Fighters have infinite engagement range.
    Simulacrum Fighters have -20% hull, -20% shield efficiency, and -10% damage
        *
        * */
        String hullmod = 100-((int)((hullMod)*100))+"%";
        String armormod = 100-((int)((armorMod)*100))+"%";
        String shieldmod = (int)(((1+shieldMod)*100)-100)+"%";
        String damagemod = 100-((int)((damageMod)*100))+"%";
        tooltip.addPara("When any ship is destroyed in combat, harvest a Reclaim Package worth %s/%s/%s/%s reclaim, depending on hullsize. reclaim packages will then go to the nearest ship in the fleet. Any  Reclaim Packages that reaches there target will be converted into reclaim.",0f,Misc.getHighlightColor(), Misc.getHighlightColor(),""+Settings.NANO_THIEF_RECLAIM_GAIN[0],""+Settings.NANO_THIEF_RECLAIM_GAIN[1],""+Settings.NANO_THIEF_RECLAIM_GAIN[2],""+Settings.NANO_THIEF_RECLAIM_GAIN[3]);
        tooltip.addPara("For every %s reclaim in a ship, it gains %s control, rounded up.",0f,Misc.getHighlightColor(), Misc.getHighlightColor(),""+(int)Settings.NANO_THIEF_ReclaimPerControl_BASE,""+1);
        tooltip.addPara("For every control a ship has, it gains the ability to maintain %s Simulacrum Fighter Wings",0f,Misc.getHighlightColor(), Misc.getHighlightColor(),""+1);
        tooltip.addPara("Every Simulacrum Fighter Wings Produced by your fleet:",0, Misc.getHighlightColor(), Misc.getHighlightColor());
        tooltip.addPara("   -Reclaim cost is %s per ordnance point + %s",0, Misc.getTextColor(), Misc.getHighlightColor(),""+(int)Settings.NANO_THIEF_CustomSwarm_COST_PEROP,""+(int)Settings.NANO_THIEF_CustomSwarm_COST_BASE);
        tooltip.addPara("   -Build time is wing size x replacement rate x %s",0,Misc.getTextColor(), Misc.getHighlightColor(),""+Settings.NANO_THIEF_CustomSwarm_BUILDTIME_PREREFIT);
        tooltip.addPara("   -Time to live is %s for bombers, and %s for none bombers",0,Misc.getTextColor(), Misc.getHighlightColor(),""+(int)Settings.NANO_THIEF_CustomSwarm_Bomber_TTL,""+(int)Settings.NANO_THIEF_CustomSwarm_TTL);
        tooltip.addPara("   -Gain infinite engagement range",0, Misc.getTextColor(), Misc.getHighlightColor());
        tooltip.addPara("   -Lose %s hull",0, Misc.getTextColor(), Misc.getNegativeHighlightColor(),hullmod);
        tooltip.addPara("   -Lose %s armor rating",0, Misc.getTextColor(), Misc.getNegativeHighlightColor(),armormod);
        tooltip.addPara("   -Lose %s shield strength",0, Misc.getTextColor(), Misc.getNegativeHighlightColor(),shieldmod);
        tooltip.addPara("   -Lose %s damage",0, Misc.getTextColor(), Misc.getNegativeHighlightColor(),damagemod);
        tooltip.addSpacer(10f);
        LabelAPI label = tooltip.addPara("\"Its an art you know. Salvaging ships on the battlefield, well under fire. There are legends of rebels harvesting whole fleets on the battlefields, sending the patchwork wreckage to attack there oppressors. Its an wonderful thing to watch. \n Makes me want to cry tears of joy. And envy.\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();


    }
    @Override
    public void applyEffectsAfterShipCreation(SCData data, ShipAPI ship, ShipVariantAPI variant, String id) {
    }

    @Override
    public void applyEffectsBeforeShipCreation(SCData data, MutableShipStatsAPI stats, ShipVariantAPI variant, ShipAPI.HullSize hullSize, String id) {
    }
    @Override
    public void advanceInCombat(SCData data, ShipAPI ship, Float amount) {
        if (!Global.getCombatEngine().hasPluginOfClass(NanoThief_BattleListener.class)) {
            Global.getCombatEngine().addPlugin(new NanoThief_BattleListener());
            //Global.getCombatEngine().addPlugin(new NanoThief_CustomSwarmHPController());
        }

        /*IntervalUtil interval = (IntervalUtil) Global.getCombatEngine().getCustomData().get("Sic_NanoFief_NanoFief_Waiter");
        if (interval == null) {
            interval = new IntervalUtil(0.25f, 0.33f);
            Global.getCombatEngine().getCustomData().put("Sic_NanoFief_NanoFief_Waiter", interval);
        }
        interval.advance(amount);*/

        /*if (interval.intervalElapsed()) {
            for (ShipAPI a : Global.getCombatEngine().getShips()) {
                if (a.hasListenerOfClass()){
                    ship.addListener();
                }
                if (!toApply.hasListenerOfClass(WitchcraftKillingBlowHandler::class.java)) {
                    toApply.addListener(WitchcraftKillingBlowHandler())
                }
            }
        }*/
    }

    @Override
    public void onActivation(SCData data) {
        if (data.getCommander().equals(Global.getSector().getPlayerPerson())){
            //Global.getSector().getCampaignUI().showInteractionDialog(new Nano_Thief_dialog(),Global.getSector().getPlayerFleet());
            /**/CharacterDataAPI character = Global.getSector().getCharacterData();
            if (character.getAbilities().contains(Settings.NANO_THIEF_ABILITY)) return;
            character.addAbility(Settings.NANO_THIEF_ABILITY);/**/
        }
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
