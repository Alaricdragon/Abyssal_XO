package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_ShipSkills;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_SkillBase;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_Skill_6;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_Skill_7;
import Abyssal_XO.data.scripts.threat.skills.interfaces.NanoThief_InterfaceBase;
import Abyssal_XO.data.scripts.threat.skills.interfaces.NanoThief_Interface_7;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.CharacterDataAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

public class NanoThief_7 extends NanoThief_6 {
    public static String[] bannedTags = {"independent_of_carrier"};
    public static int[] numPerSize = {1,1,2,3};
    //public static final float[] speedPerSize = {0.35f,0.5f,0.75f,1f};
    public static double[] speedPerSize = {0.5f,0.75f,1f,1.25f};
    //public static final double[] numbPerModule = {0,0.5,0.75,1};
    //public static final double[] speedPerModule = {0.1,0.2,0.3,0.4};
    @Override
    public void initStats(Nano_Thief_Stats stats) {
        NanoThief_Skill_7.getStats(stats, Global.getSettings().getFighterWingSpec(getFighterID(stats.commander,stats.faction,false)));
    }

    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        String line3a = (int)(speedPerSize[0]*100)+"%";
        String line3b = (int)(speedPerSize[1]*100)+"%";
        String line3c = (int)(speedPerSize[2]*100)+"%";
        String line3d = (int)(speedPerSize[3]*100)+"%";
        tooltip.addPara("Construct Defensive Simulacrum Fighter Wings to assist your fleet in combat.",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        tooltip.addPara("Each ship in your fleet can support up to %s/%s/%s/%s Simulacrum Fighter Wings depending on hullsize",0,Misc.getHighlightColor(),Misc.getHighlightColor(),numPerSize[0]+"",numPerSize[1]+"",numPerSize[2]+"",numPerSize[3]+"");
        tooltip.addPara("Each ship in your fleet builds Defensive Simulacrum Fighter Wings at %s/%s/%s/%s speed depending on hullsize",0,Misc.getHighlightColor(),Misc.getHighlightColor(),line3a,line3b,line3c,line3d);

        tooltip.addPara("Defensive Simulacrum Fighter Wings build this cannot stray from the ship that created them",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        displayBuildingFighter(scData, tooltip,false);
        tooltip.addPara("",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        this.addNewAbilityText(scData, tooltip);

        tooltip.addSpacer(10f);

//        LabelAPI label = tooltip.addPara("\"fighter craft require a dedicated ship to launch from. This is the teaching of the Domain, the Hegemony, Tri-Tech, and many others. But if you just give up on little things like 'reliability', 'armor', and 'Dedicated Nanoforges', you can launch fighter craft of basically anything, from small rocks to capital class ships. They would tell you its impossible. Deadly, even.\nI tell them to watch me.\"", Misc.getTextColor(), 0f);
        LabelAPI label = tooltip.addPara("\"In theory, its a terrible idea. The fighters have no real support, no way to replenish, no sensers outside of there motherships range. No dedicated nanoforges ether, so we cant replenish wings with any form of ease. But so long as we are fed constant supplies of materials, we can support this. It could work.\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        tooltip.addPara("",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        this.addSimWinFactorsToTooltip(scData, tooltip,false);
        label.italicize();
    }

    @Override
    public NanoThief_SkillBase createListiner(NanoThief_ShipSkills skills, ShipAPI ship) {
        return new NanoThief_Skill_7(skills, ship);
    }

    @Override
    public NanoThief_InterfaceBase createInterface() {
        return new NanoThief_Interface_7();
    }

    @Override
    public void onActivation(SCData data) {
        if (data.getCommander().equals(Global.getSector().getPlayerPerson())){
            CharacterDataAPI character = Global.getSector().getCharacterData();
            if (character.getAbilities().contains(Settings.NANO_THIEF_ABILITY)) return;
            character.addAbility(Settings.NANO_THIEF_ABILITY);
        }
    }
    @Override
    public int getNanoThiefID() {
        return 7;
    }

    public static double weight = 1;
    public Float getNPCSpawnWeight(CampaignFleetAPI fleet) {
        return (float) weight;
    }
}
