package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_ShipSkills;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_SkillBase;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_Skill_6;
import Abyssal_XO.data.scripts.threat.skills.interfaces.NanoThief_InterfaceBase;
import Abyssal_XO.data.scripts.threat.skills.interfaces.NanoThief_Interface_6;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CharacterDataAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

import java.util.ArrayList;

public class NanoThief_6 extends Nano_Thief_Skill_Base {
    /*todo:
    *       for 'thraet'
    *           1) create at least 3 fighters for use:
    *               1) some type of bomber
    *               2) some type of defender
    *               3) some type of swarmer
    *           2) make it so basic attack swarms no longer spawn as Sim Fighter Wings
    * */
    //public static float dpPerFighters = 10;
    public static int MINRANGEOFWING = 2000;

    public static double baseDpPerFighter = 8;//?
    public static double dpPerOpPerFighter = 0.4f;//how mush dp is required per op.
    //so... 240 battle size for all eqs.
    //talan: 6 + .4? = 240/6.4 = 37 ish. (from 24)
    //15: 6+3.2 = 240/9.2 = //

    // 2: 8+0.4 = 240/8.4 = 30 ish...
    // 15: 8+3 = 240/11 = 21.?
    // 20: 8+4 = 240/12 = 20.
    // 40: = 8 + ()
    // ? = 40
    // 20 = 8+(40*a) -> -8 -> 12 = 40*a -> /40 -> 12/40 = 0.4

    // 2: 8+0.8 = 240/8.8 = 9*2.66 = 18+6= 26 fighter wings.
    // 15:8+4+2 = 240/14 = 15ish?

    //public static final int ReclaimPerControl_BASE = 1000;
    public static float CustomSwarm_COST_BASE = 40;//swarms cost 100 at 10 op, 200 at 20, 300 at 30.
    public static float CustomSwarm_COST_PEROP = 10;//swarms cost 100 at 10 op, 200 at 20, 300 at 30.
    // 10,20 = : 2dp:30rc, 5dp:70rc, 10dp:120rc
    // 10,40 = : 2dp:50rc, 5dp:90rc, 10dp:140rc
    public static double CustomSwarm_BUILDTIME_PREREFIT = 1f;//swarms nerfed to build at normal 100% refit rate.
    public static double CustomSwarm_BUILDTIME_BASE = 10;
    public static double CustomSwarm_RefundPercent = 0.5f;
    public static double CustomSwarm_RefundPercent_Bomber = CustomSwarm_RefundPercent;
    public static int CustomSwarm_TTL = 60;//swrams get exstea base TTL because they already die from shoting wepons.
    public static final int BASESWARM_COST = 100;
    public static final int BASESWARM_BUILDTIME = 10;
    public static final int BASESWARM_TTL = CustomSwarm_TTL;//swrams get exstea base TTL because they already die from shoting wepons.


    public static double[] speedPerSize = {0.5f,0.75f,1f,1.25f};
    public static void displayStats(TooltipMakerAPI panel, FighterWingSpecAPI a,boolean offincive){
        Nano_Thief_Stats spec = new Nano_Thief_Stats(a.getId(),offincive);//new Nano_Thief_Stats(a.getId());
        //NanoThief_6.getFighterID();
        //NanoThief_Skill_6.getStats(spec,a);
        //spec.OF_ttl = spec.getModifedTTL(null);
        //spec.OF_productionTime = spec.getModifedProductionTime(null);
        //spec.OF_swarmCost = spec.getModifiedCost(null);
        //panel.addPara("",0);
        String sprite = a.getVariant().getHullSpec().getSpriteName();
        //panel.addImageWithText(5);
        //panel.addPara("",5);
        panel.addPara(a.getWingName(),0);
        //panel.addImage(sprite,30,30,0);
        panel.addImage(sprite,0);
        //panel.addPara(" wing name: %s",0,Misc.getTextColor(),Misc.getHighlightColor(),a.getWingName());
        int ttl;
        int pt;
        int sc;
        int rpf;
        if (offincive){
            ttl=(int)spec.OF_ttl;
            pt = (int)spec.OF_productionTime;
            sc = (int)spec.OF_swarmCost;
            rpf = (int)spec.OF_recyclePerFighter;
            float dppf = ((int)(spec.OF_DpPerFighter*100))/100f;
            panel.addPara(" Dp per fighter: %s",0, Misc.getTextColor(), Misc.getHighlightColor(),""+dppf);
        }else{
            ttl=(int)spec.DF_ttl;
            pt = (int)spec.DF_productionTime;
            sc = (int)spec.DF_swarmCost;
            rpf = (int)spec.DF_recyclePerFighter;
        }
        panel.addPara(" Time to live: %s",0, Misc.getTextColor(), Misc.getHighlightColor(),""+ttl);
        panel.addPara(" Production time: %s",0,Misc.getTextColor(), Misc.getHighlightColor(),""+pt);
        panel.addPara(" Reclaim cost: %s",0,Misc.getTextColor(), Misc.getHighlightColor(),""+sc);
        panel.addPara(" Reclaim gained when a fighter docks: %s",0,Misc.getTextColor(), Misc.getHighlightColor(),""+rpf);
    }
    public static String getFighterID(PersonAPI commander,FactionAPI getFaction,boolean isOffincive){
        //fleetData.getCommander().getMemory();
        String memKey;
        if (isOffincive){
            memKey = Settings.NANO_THIEF_CUSTOM_WING_ATK_MEMORY_KEY;
        }else{
            memKey = Settings.NANO_THIEF_CUSTOM_WING_DEF_MEMORY_KEY;
        }
        String customFighter = "";
        //CampaignFleetAPI fleet;
        if (commander.isPlayer()){
            //fleet = Global.getSector().getPlayerFleet();
            if(commander.getMemoryWithoutUpdate().contains(memKey)) {
                customFighter = commander.getMemoryWithoutUpdate().getString(memKey);
            }else{
                customFighter = Settings.NANO_THIEF_PALYER_BASEWING;
            }
        }else{
            if(commander.getMemoryWithoutUpdate().contains(memKey)) {
                customFighter = commander.getMemoryWithoutUpdate().getString(memKey);
            }
        }
        if (customFighter.isBlank()){
            //get fighter here.
            FactionAPI fac = getFaction;//commander.getFleet().getFaction();
            ArrayList<String> possable = new ArrayList<>();
            if (fac.getId().equals("threat")){
                possable.add("broadsword_wing");
                possable.add("warthog_wing");
                possable.add("piranha_wing");
                possable.add("perdition_wing");
            }
            for (String a : fac.getKnownFighters()){
                FighterWingSpecAPI spc = Global.getSettings().getFighterWingSpec(a);
                if ((spc.isRegularFighter() || spc.isAssault() || spc.isBomber() || spc.isInterceptor())
                && isOffincive ? spc.getRange() >= MINRANGEOFWING : true) {//spc.getOpCost(spc.getVariant().getStatsForOpCosts()) ) {
                    possable.add(a);
                }
            }
            if (!possable.isEmpty())customFighter = possable.get((int) (Math.random()*possable.size()-1));
            if (customFighter.isBlank()) customFighter = Settings.NANO_THIEF_PALYER_BASEWING;
            commander.getMemory().set(memKey,customFighter);
        }
        log.info("got fighter wign ID as: "+customFighter);
        return customFighter;
    }
    @Override
    public void initStats(Nano_Thief_Stats stats) {
        //... what the fuck do you do!?!?!?
        //.... oh wait I remember now.
        //log.info("getting 'init stats'");
        NanoThief_Skill_6.getStats(stats,Global.getSettings().getFighterWingSpec(getFighterID(stats.commander,stats.faction,true)));
    }

    public void addSimWinFactorsToTooltip(SCData scData, TooltipMakerAPI tooltip,boolean offincive){
        String line3a = ""+(int)CustomSwarm_TTL;
        String line4a = ((int)(CustomSwarm_RefundPercent*100))+"%";
        //String line4b = ((int)(CustomSwarm_RefundPercent_Bomber*100))+"%";
        String line7a = ((int)(CustomSwarm_COST_BASE))+"";
        String line7b = ((int)(CustomSwarm_COST_PEROP))+"";

        String line8a = CustomSwarm_BUILDTIME_BASE+"";
        String line8b = ((int)(CustomSwarm_BUILDTIME_PREREFIT*100)/100)+"";
        tooltip.addPara("Simulacrum Fighter Wings act as normal fighter wings with the following modifications:",0,Misc.getGrayColor(),Misc.getHighlightColor());
        tooltip.addPara("   -can only be active for %s seconds before reutrning to the nearest fiendly ship",0,Misc.getGrayColor(),Misc.getHighlightColor(),line3a);
        tooltip.addPara("   -will refund %s of there reclaim cost when returning to a firendly ship",0,Misc.getGrayColor(),Misc.getHighlightColor(),line4a);
        tooltip.addPara("   -do not replace lost fighters in a wing.",0,Misc.getGrayColor(),Misc.getHighlightColor());
        tooltip.addPara("   -cost %s + %s per op of the fighter wing",0,Misc.getGrayColor(),Misc.getNegativeHighlightColor(),line7a,line7b);
        tooltip.addPara("   -buildtime is %s + %s multiplied by combined replacement rate of every fighter in the wing",0,Misc.getGrayColor(),Misc.getNegativeHighlightColor(),line8a,line8b);
    }
    public void displayBuildingFighter(SCData scData, TooltipMakerAPI tooltip,boolean offincive){
        tooltip.addPara("",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        String fighter = getFighterID(scData.getCommander(),scData.getFleet().getFaction(),offincive);
        tooltip.addPara("Simulacrum Fighter Wing stats:",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        Nano_Thief_Stats.displayStatsForFighterWithoutModification(tooltip,Global.getSettings().getFighterWingSpec(fighter),offincive);
    }
    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {

        //String line3b = ""+(int)CustomSwarm_Bomber_TTL;
        String line3a = (int)(speedPerSize[0]*100)+"%";
        String line3b = (int)(speedPerSize[1]*100)+"%";
        String line3c = (int)(speedPerSize[2]*100)+"%";
        String line3d = (int)(speedPerSize[3]*100)+"%";

        //String line6a = ""+(int)dpPerFighters;
        String line6a = ""+(int)baseDpPerFighter;
        String line6b = ""+((int)(dpPerOpPerFighter*100))/100f;
        tooltip.addPara("Construct Offencive Simulacrum Fighter Wings to assist your fleet in combat.",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        tooltip.addPara("Every Offencive Simulacrum Fighter requires %s + %s per op active deployment points to be maintained",0,Misc.getTextColor(),Misc.getHighlightColor(),line6a,line6b);
        //tooltip.addPara("only one Offencive Simulacrum Fighter Wing can be active in your fleet for every %s Deployment Ponits you have active",0,Misc.getHighlightColor(),Misc.getHighlightColor(),line6a);
        tooltip.addPara("Each ship in your fleet builds Officensive Simulacrum Fighter Wings at %s/%s/%s/%s speed depending on hullsize",0,Misc.getHighlightColor(),Misc.getHighlightColor(),line3a,line3b,line3c,line3d);

        tooltip.addPara("Offencive Simulacrum Fighter Wings build this way gain infinit engagment range",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        displayBuildingFighter(scData, tooltip,true);
        tooltip.addPara("",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        this.addNewAbilityText(scData, tooltip);

        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"fighter craft require a dedicated ship to launch from. This is the teaching of the Domain, the Hegemony, Tri-Tech, and many others. But if you just give up on little things like 'reliability', 'armor', and 'Dedicated Nanoforges', you can launch fighter craft of basically anything, from small rocks to capital class ships. They would tell you its impossible. Deadly, even.\nI tell them to watch me.\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        tooltip.addPara("",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        this.addSimWinFactorsToTooltip(scData, tooltip,false);
        label.italicize();
    }
    public void addNewAbilityText(SCData scData, TooltipMakerAPI tooltip){
        String line9a = Settings.NANO_THIEF_ABILITY_NAME;
        tooltip.addPara("gain the %s ability, that allows you to change your active Offincive Simulacrum Fighter Wing",0,Misc.getHighlightColor(),Misc.getHighlightColor(),line9a);
    }
    @Override
    public NanoThief_SkillBase createListiner(NanoThief_ShipSkills skills, ShipAPI ship) {
        return new NanoThief_Skill_6(skills, ship);
    }

    @Override
    public NanoThief_InterfaceBase createInterface() {
        return new NanoThief_Interface_6();
    }

    @Override
    public int getNanoThiefID() {
        return 6;
    }

    @Override
    public void onActivation(SCData data) {
        if (data.getCommander().equals(Global.getSector().getPlayerPerson())){
            CharacterDataAPI character = Global.getSector().getCharacterData();
            if (character.getAbilities().contains(Settings.NANO_THIEF_ABILITY)) return;
            character.addAbility(Settings.NANO_THIEF_ABILITY);
        }
    }

    public static double weight = 1;
    public Float getNPCSpawnWeight(CampaignFleetAPI fleet) {
        return (float) weight;
    }
}
