package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.listiners.NanoThief_LootListiner;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.util.DynamicStats;
import second_in_command.SCData;

public class NanoThief_3 extends Nano_Thief_Skill_Base {
    private static final String key = "AbyssalXO_Nano_Thief_Skill_3";
    public static final float reclaimPerSet = 1000f;
    public static final float suppliesPerSet = 5f;

    private static final float salvageMod = 0.20f;
    private static final float battleSalvageMod = 0.1f;
    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        //String hullmod = ((int)((hullMod)*100))+"%";

        String sMod = (int)(salvageMod*100)+"%";
        String sBMod = (int)(battleSalvageMod*100)+"%";

        String reclaim = (int)reclaimPerSet+"";
        String supplies = (int)suppliesPerSet+"";


        tooltip.addPara("+%s resources - but not rare items, such as blueprints - recovered from abandoned stations and derelicts", 0f, Misc.getHighlightColor(), Misc.getHighlightColor(),sMod);
        tooltip.addPara("+%s post battle salvage", 0f, Misc.getHighlightColor(), Misc.getHighlightColor(),sBMod);
        tooltip.addPara("At the end of combat, for every %s reclaim your ships hold:",0, Misc.getHighlightColor(), Misc.getHighlightColor(),reclaim);
        tooltip.addPara("-gain %s supplies",0, Misc.getHighlightColor(), Misc.getHighlightColor(),supplies);

        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"Every bit of scrap, even broken bulkhead, every lose screw, degraded missile, and flamed out fighter is just another chance for salvage. If you can manage it before the deadlines. \nAnd we can.\"", Misc.getTextColor(), 0f);
        //LabelAPI label = tooltip.addPara("\"Safety overridden, the armor poorly maintained. The shields rooted directly to the flux core, so if one goes so does the other. Someone needs to get fired for this.\"", Misc.getTextColor(), 0f);
        //LabelAPI label = tooltip.addPara("\"Sometimes, you just need to weld some armor to your hull. Just to make sure she makes it out alive, despite the cost\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();

    }

    @Override
    public void onActivation(SCData data) {
        super.onActivation(data);
        data.getFleet().getStats().getDynamic().getStat(Stats.SALVAGE_VALUE_MULT_FLEET_NOT_RARE).modifyFlat("AbyssalXO_NanoThief_Salvage",salvageMod);
        data.getFleet().getStats().getDynamic().getStat(Stats.BATTLE_SALVAGE_MULT_FLEET).modifyFlat("AbyssalXO_NanoThief_Salvage",battleSalvageMod);
        if (data.getCommander().equals(Global.getSector().getPlayerPerson())) {
            if (Global.getSector().getListenerManager().hasListenerOfClass(NanoThief_LootListiner.class)) return;
            Global.getSector().getListenerManager().addListener(new NanoThief_LootListiner());
        }
    }

    @Override
    public void onDeactivation(SCData data) {
        super.onDeactivation(data);
        data.getFleet().getStats().getDynamic().getStat(Stats.SALVAGE_VALUE_MULT_FLEET_NOT_RARE).unmodifyFlat("AbyssalXO_NanoThief_Salvage");
        data.getFleet().getStats().getDynamic().getStat(Stats.BATTLE_SALVAGE_MULT_FLEET).unmodifyFlat("AbyssalXO_NanoThief_Salvage");
        if (data.getCommander().equals(Global.getSector().getPlayerPerson())) {
            if (Global.getSector().getListenerManager().hasListenerOfClass(NanoThief_LootListiner.class)){
                Global.getSector().getListenerManager().removeListenerOfClass(NanoThief_LootListiner.class);
            }
        }
    }
}
