package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_ShipSkills;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_SkillBase;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_Skill_2;
import Abyssal_XO.data.scripts.threat.skills.interfaces.NanoThief_InterfaceBase;
import Abyssal_XO.data.scripts.threat.skills.interfaces.NanoThief_Interface_2;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

public class NanoThief_2 extends Nano_Thief_Skill_Base {
    /*
        todo:
            changes:
                 make this a continues reload. So it reloads one volley of ammo
                    -adjust eq for this.
                    -adjust cooldown based on percent of weapon reloaded.
                    -select weapon to reload at random.
                 make every op incur a cooldown? [maybe?]

     */
    private static final String key = "AbyssalXO_Nano_Thief_Skill_2";

    public static float timeSmall = 30;
    public static float timeMid = 60;
    public static float timeLarge = 90;

    public static float costSmall = 30;
    public static float costMid = 40;
    public static float costLarge = 50;

    public static float baseCostSmall = 50;
    public static float baseCostMid = 70;
    public static float baseCostLarge = 90;

    public static float animationTimeMult = 1;
    public static float animationIntensity = 1.25f;
    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        String tSmall = ""+(int)timeSmall;
        String tMid = ""+(int)timeMid;
        String tLarge = ""+(int)timeLarge;

        String cSmall = ""+(int)costSmall;
        String cBaseSmall = ""+(int)baseCostSmall;
        String cMid = ""+(int)costMid;
        String cBaseMid = ""+(int)baseCostMid;
        String cLarge = ""+(int)costLarge;
        String cBaseLarge = ""+(int)baseCostLarge;

        tooltip.addPara("Every %s/%s/%s seconds, fully refill the ammo of a random 'limited ammo' weapon",0,Misc.getHighlightColor(),Misc.getHighlightColor(),tSmall,tMid,tLarge);
        //tooltip.addPara("Will refill a ",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        tooltip.addPara("All attached modules share a cooldown with the main ship.",0,Misc.getHighlightColor(),Misc.getHighlightColor());

        tooltip.addPara("Costs %s/%s/%s + %s/%s/%s reclaim per OP depending on weapon size",0,Misc.getNegativeHighlightColor(),Misc.getNegativeHighlightColor(),cBaseSmall,cBaseMid,cBaseLarge,cSmall,cMid,cLarge);
        tooltip.addPara("Effects that increase max ammo increase cost proportionally",0,Misc.getNegativeHighlightColor(),Misc.getNegativeHighlightColor());
        tooltip.addPara("When a weapon reloads, the reclaim cost and cooldown is proportional to the amount of ammo reloaded compaired to the maximum amount of ammo that weapon can hold",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        tooltip.addPara("Does not work on built in weapons",0,Misc.getHighlightColor(),Misc.getHighlightColor());
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
    public static double weight = 1;
    public Float getNPCSpawnWeight(CampaignFleetAPI fleet) {
        return (float) weight;
    }
}
