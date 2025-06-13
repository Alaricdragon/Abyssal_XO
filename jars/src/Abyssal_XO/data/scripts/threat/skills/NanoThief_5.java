package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

public class NanoThief_5 extends Nano_Thief_SKill_Base{
    private static float timeChange = 3f;
    private static float costChange = 1.1f;
    private static int quality = 1;
    private static final String key = "AbyssalXO_Nano_Thief_Skill_5";
    private static final float hullMod = 0.95f;
    private static final float armorMod = 0.95f;
    private static final float shieldMod = 0.05f;//note: the shield mod is bugged at this level. so ya.
    private static final float damageMod = 0.95f;

    private static final float costMod = 0.8f;
    private static final float buildTimeMod = 0.7f;
    private static final float controlMod = 0.9f;

    @Override
    public float manufactureTimeChange(float time, ShipAPI target, Nano_Thief_Stats stats) {
        return time * timeChange;
    }

    @Override
    public float costChange(float cost, ShipAPI target, Nano_Thief_Stats stats) {
        return cost * costChange;
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
        gain 10% flux dissipation*/
        String hullmod = 100-((int)((hullMod)*100))+"%";
        String armormod = 100-((int)((armorMod)*100))+"%";
        String shieldmod = (int)(((1+shieldMod)*100)-100)+"%";
        //String damagemod = 100-((int)((ttlMod)*100))+"%";
        //tooltip.addPara("Gain the 'Overcharged' sub system, wish increased precived time flow by %s for %s seconds with a very long cooldown.",0, Misc.getHighlightColor(), Misc.getHighlightColor(),timeflowmod,timeflowdur);
        tooltip.addPara("Lose %s hull",0, Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),hullmod);
        tooltip.addPara("Lose %s armor rating",0, Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),armormod);
        tooltip.addPara("Lose %s shield strength",0, Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),shieldmod);
        //tooltip.addPara("Lose %s time to live",0, Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),damagemod);


        int costReduction = (int) ((costChange-1)*100);
        int timeReduction = (int) ((1-(1/timeChange))*100);
        tooltip.addPara("Gain %s quality", 0f,Misc.getHighlightColor(), Misc.getHighlightColor(), ""+quality);
        tooltip.addPara("Increase reclaim cost by %s",0f,Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),costReduction+"%");
        tooltip.addPara("Decreases production speed by %s",0f,Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),timeReduction+"%");

        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"Oh sure, we can increase quality. No cost to weapons, no broken systems, no striped armor, nothing. But it will cost you. And it will take time.\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();

    }
}
