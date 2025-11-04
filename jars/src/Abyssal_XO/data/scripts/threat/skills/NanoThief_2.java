package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_ShipSkills;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_SkillBase;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_Skill_2;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

public class NanoThief_2 extends Nano_Thief_Skill_Base {
    private static final String key = "AbyssalXO_Nano_Thief_Skill_2";

    public static final float timeSmall = 15;
    public static final float timeMid = 30;
    public static final float timeLarge = 45;

    public static final float costSmall = 5;
    public static final float costMid = 10;
    public static final float costLarge = 15;
    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        String tSmall = ""+timeSmall;
        String tMid = ""+timeMid;
        String tLarge = ""+timeLarge;

        String cSmall = ""+costSmall;
        String cMid = ""+costMid;
        String cLarge = ""+costLarge;

        tooltip.addPara("Every %s/%s/%s seconds, fully refill a single empty 'limited ammo' weapons ammo",0,Misc.getHighlightColor(),Misc.getHighlightColor(),tSmall,tMid,tLarge);
        tooltip.addPara("Costs %s/%s/%s reclaim per op depending on weapon size",0,Misc.getHighlightColor(),Misc.getHighlightColor(),cSmall,cMid,cLarge);
        tooltip.addPara("Will refill smaller weapons first",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        tooltip.addPara("All attached modules share a cooldown with the main ship",0,Misc.getHighlightColor(),Misc.getHighlightColor());

        tooltip.addSpacer(10f);

        //LabelAPI label = tooltip.addPara("\"I don't care what it takes, I don't even care if the craft explodes the moment we set foot on it. If we cant meet quotas, some safety concerns will be the least of our worry's!.\"", Misc.getTextColor(), 0f);
        LabelAPI label = tooltip.addPara("\"Using the highest quality materials and proper production cycles is oftentimes unrequired. Especially when there is a deadline to meet.\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();
    }

    @Override
    public NanoThief_SkillBase createListiner(NanoThief_ShipSkills skills, ShipAPI ship) {
        return new NanoThief_Skill_2(skills,ship);
    }
}
