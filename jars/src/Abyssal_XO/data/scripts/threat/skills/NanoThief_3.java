package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_LootListiner;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_ShipSkills;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_SkillBase;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.combat.ShipAPI;
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
            -
            notes on stuff and things:
            1: add a intel thing that fades after a few days, but shows how mush reclaim you got in your last battle.
            2: add a second intel thing that triggers if you run from a fight, (AKA didnt get the loot) that adds the loot to the player.
     */
    /*
    * */

    private static final String key = "AbyssalXO_Nano_Thief_Skill_3";
    public static float reclaimPerSet = 1000f;
    public static float suppliesPerSet = 5f;

    public static double salvageMod = 0.20f;
    public static double battleSalvageMod = 0.1f;

    public static boolean showSupplyMessageLooted = false;
    public static boolean showSupplyMessageNotLooted = true;
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
    public static double weight = 1;
    public Float getNPCSpawnWeight(CampaignFleetAPI fleet) {
        return (float) weight;
    }
    public static boolean playerHasSkill3;
    public static boolean alreadyLooted;
    public static float reclaimToAdd;
    public static float reclaimAdded;
    public static void sendIntelMessage(){
        if (alreadyLooted) if (showSupplyMessageLooted)Global.getSector().getCampaignUI().addMessage("Gained "+(int)(reclaimAdded+reclaimToAdd)+" supplies from previous battle (Already looted)",Global.getSector().getPlayerFaction().getBaseUIColor());
        else{
            if (showSupplyMessageNotLooted) Global.getSector().getCampaignUI().addMessage("Gained "+(int)(reclaimAdded+reclaimToAdd)+" supplies from previous battle",Global.getSector().getPlayerFaction().getBaseUIColor());
            Global.getSector().getPlayerFleet().getCargo().addSupplies(reclaimToAdd);
        }
        reclaimAdded = 0;
        reclaimToAdd = 0;
    }
    public static void calculatePlayerSuppliesGained(){
        Nano_Thief_Stats.setPlayerExstraReclaimIfRequired();
        float reclaim = Nano_Thief_Stats.getPlayerExstraReclaim();
        log.info("attempting to create more loot from something or other =)");
        if (NanoThief_3.reclaimPerSet != 0)reclaim /= NanoThief_3.reclaimPerSet;
        else reclaim = 0;
        log.info("got sets as:");
        log.info("  total sets: "+reclaim);
        log.info("  supplies: "+reclaim*NanoThief_3.suppliesPerSet);
        float supplies = reclaim*NanoThief_3.suppliesPerSet;
        reclaimToAdd+=supplies;

        playerHasSkill3 = false;
        alreadyLooted = false;
    }
    @Override
    public NanoThief_SkillBase createListiner(NanoThief_ShipSkills skills, ShipAPI ship) {
        if (!Global.getCombatEngine().isSimulation() && skills.stats.fleet != null && skills.stats.fleet.getFleet() != null && skills.stats.fleet.getFleet().isPlayerFleet()){
            playerHasSkill3 = true;
            //alreadyLooted = false;
        }
        return null;
    }
}
