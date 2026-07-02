package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_ShipSkills;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_SkillBase;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_Skill_4;
import Abyssal_XO.data.scripts.threat.skills.interfaces.NanoThief_InterfaceBase;
import Abyssal_XO.data.scripts.threat.skills.interfaces.NanoThief_Interface_4;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.magiclib.subsystems.MagicSubsystemsManager;
import second_in_command.SCData;

public class NanoThief_4 extends Nano_Thief_Skill_Base {
    public static final String modifier = "AbyssalXO_Nano_Thief_Skill_4";
    public static double activeDamage = 500;
    public static double activePercent = 0.1f;
    public static double activeTime = 1;
    public static double resistance = 0.05f;
    public static double time = 5;
    public static double cooldown = 30;
    public static double activeCost = 10;
    public static double damagePerCost = 50;

    //public static final int minReclaimToActavate = 25;
    //private static final float hullMod = 0.95f;
    //private static final float armorMod = 0.95f;
    //private static final float shieldMod = 0.05f;
    //private static float hullChange = 0.95f;
    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        String activeDamage = (int) NanoThief_4.activeDamage +"";
        String activePercent = (int)(NanoThief_4.activePercent *100)+"%";
        String activeTime = (int) NanoThief_4.activeTime +"";
        String resistance = (int)(100*(1- NanoThief_4.resistance))+"%";
        String time = (int) NanoThief_4.time +"";
        String cooldown = (int) NanoThief_4.cooldown +"";
        String activeCost = (int) NanoThief_4.activeCost +"";
        String damagePerCost = (int) NanoThief_4.damagePerCost+"";

        tooltip.addPara("Whenever a ship takes %s damage, or %s of there hull in damage in a %s seconds:",0,Misc.getHighlightColor(), Misc.getHighlightColor(),activeDamage,activePercent,activeTime);
        tooltip.addPara("-reduce hull and armor damage taken by %s",0, Misc.getHighlightColor(), Misc.getHighlightColor(),resistance);
        tooltip.addPara("-lasts %s seconds",0, Misc.getHighlightColor(), Misc.getHighlightColor(),time);
        tooltip.addPara("-has %s second cooldown",0, Misc.getHighlightColor(), Misc.getHighlightColor(),cooldown);
        tooltip.addPara("-for every %s damage resisted this way, loess %s reclaim",0, Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),damagePerCost,"1");
        tooltip.addPara("-costs %s reclaim per activation",0, Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),activeCost);

        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"Some parts are simply irreplaceable. And sometimes, another shipment might be late. In cases like that, we gotta get something working. Make sure all the casing is just right, make sure there is something separating the core from any form of tampering. And if that means we take an old dampener field generator and weld it to the systems just to keep it structurally sound, then so be it. So stop your complaining and get to work!\"", Misc.getTextColor(), 0f);
        //LabelAPI label = tooltip.addPara("\"A few modifications here, a little striped armor there, Connecting the flux core with the ignition just right, and she will fly like a dream.\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();
        //MagicSubsystemsManager.addSubsystemToShip();
    }

    @Override
    public NanoThief_SkillBase createListiner(NanoThief_ShipSkills skills, ShipAPI ship) {
        return new NanoThief_Skill_4(skills,ship);
    }

    @Override
    public NanoThief_InterfaceBase createInterface() {
        return new NanoThief_Interface_4();
    }

    @Override
    public int getNanoThiefID() {
        return 4;
    }
    public static double weight = 1;
    public Float getNPCSpawnWeight(CampaignFleetAPI fleet) {
        return (float) weight;
    }
}
