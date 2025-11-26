package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_ShipSkills;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_SkillBase;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_Skill_6;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CharacterDataAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.apache.log4j.Logger;
import second_in_command.SCData;

public class NanoThief_6 extends Nano_Thief_Skill_Base {
    public static final float dpPerFighters = 10;
    public static final int MINRANGEOFWING = 2000;

    public static final int BASESWARM_COST = 175;
    public static final int BASESWARM_BUILDTIME = 10;
    public static final int BASESWARM_TTL = 120;//swrams get exstea base TTL because they already die from shoting wepons.


    public static final int ReclaimPerControl_BASE = 1000;
    public static final float CustomSwarm_COST_BASE = 20;//swarms cost 100 at 10 op, 200 at 20, 300 at 30.
    public static final float CustomSwarm_COST_PEROP = 10;//swarms cost 100 at 10 op, 200 at 20, 300 at 30.
    public static final float CustomSwarm_BUILDTIME_PREREFIT = 1f;//swarms nerfed to build at normal 100% refit rate.
    public static final float CustomSwarm_RefundPercent = 0.5f;
    public static final float CustomSwarm_RefundPercent_Bomber = 0.3f;
    public static final int CustomSwarm_TTL = 15;//swrams get exstea base TTL because they already die from shoting wepons.
    //@Deprecated
    //public static final int CustomSwarm_Bomber_TTL = 1;
    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {

        String line3a = ""+(int)CustomSwarm_TTL;
        //String line3b = ""+(int)CustomSwarm_Bomber_TTL;
        String line4a = ((int)(CustomSwarm_RefundPercent*100))+"%";
        String line4b = ((int)(CustomSwarm_RefundPercent_Bomber*100))+"%";

        String line6a = ""+(int)dpPerFighters;
        String line7a = ((int)(CustomSwarm_COST_BASE))+"";
        String line7b = ((int)(CustomSwarm_COST_PEROP))+"";
        String line9a = Settings.NANO_THIEF_ABILITY_NAME;
        tooltip.addPara("Construct Simulacrum Fighter Wings to assist your fleet in combat.",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        tooltip.addPara("Simulacrum Fighter Wings act as normal fighter wings with the following modifications:",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        tooltip.addPara("Simulacrum Fighter Wings can only be active for %s seconds before reutrning to the nearest fiendly ship",0,Misc.getHighlightColor(),Misc.getHighlightColor(),line3a);
        tooltip.addPara("Simulacrum Fighter Wings refund %s of there reclaim cost when returning to a firendly ship, or %s if a bomber",0,Misc.getHighlightColor(),Misc.getHighlightColor(),line4a,line4b);
        tooltip.addPara("Simulacrum Fighter Wings do not replace lost fighters in a wing.",0,Misc.getHighlightColor(),Misc.getHighlightColor());

        tooltip.addPara("only one Simulacrum Fighter Wing can be active in your fleet for every %s Deployment Ponits you have active",0,Misc.getHighlightColor(),Misc.getHighlightColor(),line6a);
        tooltip.addPara("cost %s + %s per op of the fighter wing",0,Misc.getNegativeHighlightColor(),Misc.getNegativeHighlightColor(),line7a,line7b);
        tooltip.addPara("has a cooldown equal to the combined replacement rate of every fighter in the wing",0,Misc.getNegativeHighlightColor(),Misc.getNegativeHighlightColor());

        tooltip.addPara("Simulacrum Fighter Wings build this way gain infinit engagment range",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        tooltip.addPara("gain the %s ability, that allows you to change your active Offincive Simulacrum Fighter Wing",0,Misc.getHighlightColor(),Misc.getHighlightColor(),line9a);
        tooltip.addPara("",0,Misc.getHighlightColor(),Misc.getHighlightColor());


        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"fighter craft require a dedecated ship to lanch from. This is the teaching of the Domain, the Hegmony, Tri-Tech, and many others. But if you just give up on little things like 'reliability', 'armor', and 'Dedecated Nanoforges', you can lanch fighter craft of basicly anything, from small rocks to capital class ships. They would tell you its impossable. Deadly, even.\nI tell them to watch me.\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();

    }

    @Override
    public NanoThief_SkillBase createListiner(NanoThief_ShipSkills skills, ShipAPI ship) {
        return new NanoThief_Skill_6(skills, ship);
    }

    @Override
    public void onActivation(SCData data) {
        if (data.getCommander().equals(Global.getSector().getPlayerPerson())){
            CharacterDataAPI character = Global.getSector().getCharacterData();
            if (character.getAbilities().contains(Settings.NANO_THIEF_ABILITY)) return;
            character.addAbility(Settings.NANO_THIEF_ABILITY);/**/
        }
    }
}
