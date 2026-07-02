package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.Settings;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

public class NanoThief_8 extends Nano_Thief_Skill_Base {
    //this needs to change.
    //so... what do I need to do here?
    public static double sModBonus = 1.05;

    public static double reclaimRaito = 1.2;
    public static double reclaimPerSecondBase = 100; // 10 seconds for 1000 reclaim
    public static double reclaimPerSecondPerBost = 50; // 50 = 20 seconds. 1000 = 150 = 7.5 seconds. 2000 = 200 = 5 seconds. 3000 = 250 = 4 seconds. 4000 = 300 = 3.3~ seconds. 5000 = 350 = 2.4?

    //public static double reclaimRefined = 60;
    //public static double reclaimCost = 50;
    public static double speedMod = 2;
    public static double reclaimPerSpeedBost = 1000;
    //public static double speedPerBost = 0.2;//????

    public static double keeptReclaim = 2000;
    public static double keeptReclaimAbility = 2;

    public static double minReclaimToTarget = 0.5;
    public static double baseReclaimEfficiencyMod = 0.8;


    //public static double reclaimRaito = reclaimRefined / reclaimCost;

    @Override
    public int getNanoThiefID() {
        return 8;
    }

    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        //String line1a = ""+(int)reclaimRefined;
        //String line1b = ""+(int)reclaimCost;
        String line1a = ""+(int)reclaimPerSecondBase;
        String line1b = ""+(int)reclaimPerSecondPerBost;
        String line1c = ""+(int)reclaimPerSpeedBost;

        //String line2a = ""+(int)reclaimPerSpeedBost;
        //String line2b = (int)(speedPerBost*100)+"%";
        String line2a = (int)((reclaimRaito*100))-100 + "%";

        String line3a = (int)keeptReclaim+"";
        String line3b = (int)keeptReclaimAbility+"";
        String line3c = (int)(minReclaimToTarget*100)+"%";

        String line4a = (int)(100*(speedMod-1))+"%";
        String line5a = (int)(100*(1-baseReclaimEfficiencyMod))+"%";
        tooltip.addPara("When the first reclaim package is created, the largest, highest mass ship in your fleet is marked as the Central Fabricator. Reclaim Packages will always chose to be processed at the Central Fabricator, provided it exists.",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        tooltip.addPara("The Central Fabricator will have the following changes:",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        //tooltip.addPara("   -Every second it will produce %s refined reclaim from %s reclaim",0,Misc.getTextColor(),Misc.getHighlightColor(),line1a,line1b);
        tooltip.addPara("   -Every second it will refine %s reclaim. Will refine an additional %s reclaim per second for every %s reclaim in storge",0,Misc.getTextColor(),Misc.getHighlightColor(),line1a,line1b,line1c);
        //tooltip.addPara("   -For every %s reclaim held, produce refined reclaim %s faster",0,Misc.getTextColor(),Misc.getHighlightColor(),line2a,line2b);
        tooltip.addPara("   -Refined reclaim will have its value increased by %s",0,Misc.getTextColor(),Misc.getHighlightColor(),line2a);
        tooltip.addPara("   -will hold onto %s refined reclaim, or %s times the cost of its most costly ability, whatever is higher",0,Misc.getTextColor(),Misc.getHighlightColor(),line3a,line3b);
        tooltip.addPara("   -Will send refined reclaim packages to friendly ships, prioritizing ships with less reclaim",0,Misc.getTextColor(),Misc.getHighlightColor());
        tooltip.addPara("   -Only one Central Fabricator can be selected per battle.",0,Misc.getNegativeHighlightColor(),Misc.getNegativeHighlightColor());
        //tooltip.addPara("Every second the Central Fabricator will produce %s refined reclaim from %s reclaim",0,Misc.getHighlightColor(),Misc.getHighlightColor(),line1a,line1b);
        //tooltip.addPara("For every %s reclaim the Central Fabricator has %s more reclaim will be refined each second",0,Misc.getHighlightColor(),Misc.getHighlightColor(),line2a,line2b);
        //tooltip.addPara("When Central Fabricator has %s reclaim, or %s times is most costly ability (what ever is higher) the Central Fabricator will create a refined reclaim package send it to the ship with the least reclaim.",0,Misc.getHighlightColor(),Misc.getHighlightColor(),line3a,line3b);
        //tooltip.addPara("   -only ships with %s less reclaim then the Central Fabricator holds are eligible to receive a Refined Reclaim Package (this number includes incoming Refined Reclaim Packages).",0,Misc.getHighlightColor(),Misc.getHighlightColor(),line3c);
        //tooltip.addPara("",0);
        tooltip.addPara("Refined Reclaim Packages move %s faster then normal reclaim packages, but otherwise act the same as normal Reclaim Packages",0,Misc.getHighlightColor(),Misc.getHighlightColor(),line4a);
        tooltip.addPara("Ships other then the central fabricator receive %s less reclaim from normal reclaim packages",0,Misc.getNegativeHighlightColor(),Misc.getNegativeHighlightColor(),line5a);
        tooltip.addPara("",0);
        tooltip.addPara("Gain the %s hullmod, allowing you to chose your Central Fabricator",0,Misc.getHighlightColor(),Misc.getHighlightColor(),"Central Fabricator");


        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"By having a single, centralized factory we can streamline important production tasks, dramatically improving output and quality well reducing cost. \nNow we just need to worry about the logistics\"", Misc.getTextColor(), 0f);
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

    public static double weight = 1;
    public Float getNPCSpawnWeight(CampaignFleetAPI fleet) {
        return (float) weight;
    }
}
