package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_LootListiner;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.impl.campaign.ids.Stats;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.util.DynamicStats;
import second_in_command.SCData;
import second_in_command.SCUtils;
import second_in_command.specs.SCOfficer;

public class NanoThief_3 extends Nano_Thief_Skill_Base {
    /*
        todo:
            1: make it so 'reclaim' gained is not reset when a new battle starts, only when
            2: make it so the amount of 'reclaim' is not added if the player is in a sim.
            3: make it so if the player does not claim reclaim, a intil screen pops up with the supplies added to the players fleet.

     */
    private static final String key = "AbyssalXO_Nano_Thief_Skill_3";
    public static float reclaimPerSet = 1000f;
    public static float suppliesPerSet = 5f;

    public static double salvageMod = 0.20f;
    public static double battleSalvageMod = 0.1f;

    @Override
    public int getNanoThiefID() {
        return 3;
    }

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
    public static boolean attemptSettingsUpdateIfRequired(){
        if (Global.getSector() == null || Global.getSector().getPlayerFleet() == null || SCUtils.getFleetData(Global.getSector().getPlayerFleet()) == null) return false;
        SCData data = SCUtils.getFleetData(Global.getSector().getPlayerFleet());
        for (SCOfficer a : data.getActiveOfficers()) if (a.getAptitudeId().equals("Abyssal_NanoThief")){
            Nano_Thief_Stats b = new Nano_Thief_Stats(Global.getSector().getPlayerPerson(),Global.getSector().getPlayerFleet(),Global.getSector().getPlayerFleet().getFleetData(),"",true,0,Global.getSector().getPlayerFaction());
            data.getFleet().getStats().getDynamic().getStat(Stats.SALVAGE_VALUE_MULT_FLEET_NOT_RARE).modifyFlat("AbyssalXO_NanoThief_Salvage", (float) salvageMod*b.skillMulti[3]);
            data.getFleet().getStats().getDynamic().getStat(Stats.BATTLE_SALVAGE_MULT_FLEET).modifyFlat("AbyssalXO_NanoThief_Salvage", (float) battleSalvageMod*b.skillMulti[3]);
            return true;
        }
        return false;
    }
    @Override
    public void onActivation(SCData data) {
        super.onActivation(data);
        if (data.isPlayer() && attemptSettingsUpdateIfRequired()){

        }else {
            data.getFleet().getStats().getDynamic().getStat(Stats.SALVAGE_VALUE_MULT_FLEET_NOT_RARE).modifyFlat("AbyssalXO_NanoThief_Salvage", (float) salvageMod);
            data.getFleet().getStats().getDynamic().getStat(Stats.BATTLE_SALVAGE_MULT_FLEET).modifyFlat("AbyssalXO_NanoThief_Salvage", (float) battleSalvageMod);
        }
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
