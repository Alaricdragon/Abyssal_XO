package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_ShipSkills;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_SkillBase;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_Skill_2;
import Abyssal_XO.data.scripts.threat.skills.interfaces.NanoThief_InterfaceBase;
import Abyssal_XO.data.scripts.threat.skills.interfaces.NanoThief_Interface_2;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

public class NanoThief_2 extends Nano_Thief_Skill_Base {
    private static final String key = "AbyssalXO_Nano_Thief_Skill_2";

    public static float timeSmall = 15;
    public static float timeMid = 30;
    public static float timeLarge = 45;

    public static float costSmall = 10;
    public static float costMid = 20;
    public static float costLarge = 30;
    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        String tSmall = ""+(int)timeSmall;
        String tMid = ""+(int)timeMid;
        String tLarge = ""+(int)timeLarge;

        String cSmall = ""+(int)costSmall;
        String cMid = ""+(int)costMid;
        String cLarge = ""+(int)costLarge;

        tooltip.addPara("Every %s/%s/%s seconds, fully refill a single empty 'limited ammo' weapons ammo",0,Misc.getHighlightColor(),Misc.getHighlightColor(),tSmall,tMid,tLarge);
        tooltip.addPara("Will refill larger weapons first",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        tooltip.addPara("All attached modules share a cooldown with the main ship. Will always refill the main ship first",0,Misc.getHighlightColor(),Misc.getHighlightColor());

        tooltip.addPara("Costs %s/%s/%s reclaim per OP depending on weapon size",0,Misc.getNegativeHighlightColor(),Misc.getNegativeHighlightColor(),cSmall,cMid,cLarge);
        tooltip.addPara("Effects that increase max ammo increase cost proportionally",0,Misc.getNegativeHighlightColor(),Misc.getNegativeHighlightColor());
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

    @Override
    public NanoThief_InterfaceBase createInterface() {
        return new NanoThief_Interface_2();
    }

    @Override
    public int getNanoThiefID() {
        return 2;
    }
}
