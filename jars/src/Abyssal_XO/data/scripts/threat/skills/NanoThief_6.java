package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.Settings;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.apache.log4j.Logger;
import second_in_command.SCData;

public class NanoThief_6 extends Nano_Thief_Skill_Base {
    private static Logger log = Global.getLogger(NanoThief_6.class);
    private static final String key = "AbyssalXO_Nano_Thief_Skill_6";
    private static final float fabCostMod = 0.7f;
    private static final float fabSpeedMod = 0.8f;
    private static final float fabSpeedPer1000Mod = 0.95f;
    private static final float fabControlMod = 0.8f;
    private static final float centralFabReclaimMultiHullmod = 0.9f;

    private static final float fabHullMod = 0.1f;
    private static final float fabArmorMod = 0.1f;
    private static final float fabShieldMod = 0.9f;

    private static final float fabTTLMod = 1.2f;


    private static final float costMod = 1.5f;
    private static final float speedMod = 1.25f;
    private static final float tTlMod = 0.8f;

    private static final float hullMod = 0.9f;
    private static final float armorMod = 0.9f;
    private static final float shieldMod = 0.1f;
    private static final float damageMod = 0.95f;
    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        /*6) Centralized Logistics: when the first reclaim package is created, the largest, highest mass ship in your fleet is marked as the 'Central Fabricator'. Reclaim Packages will always attempt to move to the Central Fabricator, provided it exists.
        for every 1000 Reclaim the Central Fabricator has:
        produce Simulacrum Fighter Wings 10% faster.
        Simulacrum Fighter Wings produced by the Central Fabricator:
            Take 20% less time to build
            for every 1000 recalim stored in the Central Fabricator, take 10% less time to build
            cost 33% less.
            cost 20% less control
            gain 20% time to live
            gain 5% max hp
            shields take 5% less damage.
        Simulacrum Fighter Wings produced by any ship that is NOT the Central Fabricator:
            cost 50% more
            take 25% more time to build
            lose 20% max hp
            shields take 20% more damage.

            lose 20% time to live
            lose 5% damage
        After the Central Fabricator is assigned, it cannot be changed for the inter combat, even if it is destroyed or retreats.
        gain the 'Central Fabricator' hullmod, allowing you to chose your Central Fabricator*/
        /*tooltip.addPara("When the first reclaim package is created, the largest, highest mass ship in your fleet is marked as the Central Fabricator. Reclaim Packages will always attempt to move to the Central Fabricator, provided it exists. \nSwarms produced by the Central Fabricator have the following changes:",0f,Misc.getHighlightColor(), Misc.getHighlightColor());

        int display = (int) ((1 - fabCostChange)*100);
        tooltip.addPara("Take %s less time to build",0,Misc.getHighlightColor(),Misc.getHighlightColor(),buildmod);
        tooltip.addPara("   -Reduce reclaim cost by %s",0f,Misc.getTextColor(), Misc.getHighlightColor(),display+"%");
        tooltip.addPara("   -Gain %s quality",0f,Misc.getTextColor(), Misc.getHighlightColor(),fabQuality+"");
        tooltip.addPara("   -Increases the number of swarms that can be deployed at once by %s",0f,Misc.getTextColor(), Misc.getHighlightColor(),fabControlStr);
        display = (int) (((1/fabProductionSpeed)-1)*100);
        tooltip.addPara("increases production speed by %s",0f,Misc.getTextColor(), Misc.getHighlightColor(),display+"%");


        tooltip.addPara("If the Central Fabricator is destroyed or retreats Reclaim Packages will attempt to move to the nearest capital ship, provided one exists. \nSwarms produced by the Capital Ships that are not the Central Fabricator have the following changes:",0f,Misc.getHighlightColor(), Misc.getHighlightColor());
        //tooltip.addPara("   -Gain %s quality",0f,Misc.getTextColor(), Misc.getHighlightColor(),capitalQuality+"");*/
        tooltip.addPara("When the first reclaim package is created, the largest, highest mass ship in your fleet is marked as the Central Fabricator. Reclaim Packages will always chose to be processed at the Central Fabricator, provided it exists.",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        tooltip.addPara("Simulacrum Fighter Wings produced by the Central Fabricator:",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        String cost = 100-((int)((fabCostMod)*100))+"%";
        String control = 100-((int)((fabControlMod)*100))+"%";
        String time = 100-((int)((fabSpeedMod)*100))+"%";
        String per1000time = 100-((int)((fabSpeedPer1000Mod)*100))+"%";
        String ttl = ((int)(fabTTLMod*100))-100+"%";
        String hp = (int)(fabHullMod*100)+"%";
        String armor = (int)(fabArmorMod*100)+"%";
        String shields = 100-(int)((fabShieldMod*100))+"%";

        tooltip.addPara("   -Cost %s less",0,Misc.getTextColor(),Misc.getHighlightColor(),cost);
        tooltip.addPara("   -Use %s less control",0,Misc.getTextColor(),Misc.getHighlightColor(),control);
        tooltip.addPara("   -Take %s less time to build",0,Misc.getTextColor(),Misc.getHighlightColor(),time);
        tooltip.addPara("   -For every %s reclaim in the Central Fabricator, take an additional %s less time to build",0,Misc.getTextColor(),Misc.getHighlightColor(),"1000",per1000time);
        tooltip.addPara("   -Gain %s time to live",0,Misc.getTextColor(),Misc.getHighlightColor(),ttl);
        tooltip.addPara("   -Gain %s hull",0,Misc.getTextColor(),Misc.getHighlightColor(),hp);
        tooltip.addPara("   -Gain %s armor rating",0,Misc.getTextColor(),Misc.getHighlightColor(),armor);
        tooltip.addPara("   -Gain %s shield strength",0,Misc.getTextColor(),Misc.getHighlightColor(),shields);

         cost = ((int)((costMod)*100))-100+"%";
         time = ((int)((speedMod)*100))-100+"%";
         ttl = 100-((int)(tTlMod*100))+"%";
         hp = 100-((int)((hullMod)*100))+"%";
         armor = 100-((int)((armorMod)*100))+"%";
         shields = (int)((shieldMod*100))+"%";
         String damage = 100-((int)((damageMod)*100))+"%";

        tooltip.addPara("Simulacrum Fighter Wings produced by all other ships in your fleet:",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        tooltip.addPara("   -Cost %s more",0,Misc.getTextColor(),Misc.getNegativeHighlightColor(),cost);
        tooltip.addPara("   -Take %s more time to build",0,Misc.getTextColor(),Misc.getNegativeHighlightColor(),time);
        tooltip.addPara("   -Lose %s time to live",0,Misc.getTextColor(),Misc.getNegativeHighlightColor(),ttl);
        tooltip.addPara("   -Lose %s hull",0,Misc.getTextColor(),Misc.getNegativeHighlightColor(),hp);
        tooltip.addPara("   -Lose %s armor rating",0,Misc.getTextColor(),Misc.getNegativeHighlightColor(),armor);
        tooltip.addPara("   -Lose %s shield strength",0,Misc.getTextColor(),Misc.getNegativeHighlightColor(),shields);
        tooltip.addPara("   -Lose %s damage",0,Misc.getTextColor(),Misc.getNegativeHighlightColor(),damage);

        tooltip.addPara("If the Central Fabracator is destroyed, disabled, or retreates, a new Central Fabractor will not be sellected untill the battle is over",0,Misc.getNegativeHighlightColor(),Misc.getNegativeHighlightColor());
        tooltip.addPara("Gain the %s hullmod, allowing you to chose your Central Fabracator",0,Misc.getHighlightColor(),Misc.getHighlightColor(),"Central Fabricator");

        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"By having a single, centralized factory we can streamline important production tasks, dramatically improving output and quality and reducing cost. \nNow we just need to worry about the logistics\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();

    }
    @Override
    public void onActivation(SCData data) {
        if (data.getCommander().equals(Global.getSector().getPlayerPerson())) {
            FactionAPI faction = Global.getSector().getPlayerFaction();
            if (!faction.getKnownHullMods().contains(Settings.HULLMOD_CENTRAL_FAB)) {
                faction.addKnownHullMod(Settings.HULLMOD_CENTRAL_FAB);
            }
        }
    }
}
