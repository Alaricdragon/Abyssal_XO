package Abyssal_XO.data.scripts.threat.skills;

import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

public class NanoThief_3 extends Nano_Thief_Skill_Base {
    private static final String key = "AbyssalXO_Nano_Thief_Skill_3";
    private static final float hullMod = 0.1f;
    private static final float armorMod = 0.1f;
    private static final float shieldMod = 0.9f;
    private static final float timeMod = 1.5f;

    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        String hullmod = ((int)((hullMod)*100))+"%";
        String armormod = ((int)((armorMod)*100))+"%";
        String shieldmod = 100-(int)((shieldMod*100))+"%";
        String damagemod = ((int)((timeMod)*100))-100+"%";
        tooltip.addPara("gain %s hull",0, Misc.getHighlightColor(), Misc.getHighlightColor(),hullmod);
        tooltip.addPara("gain %s armor rating",0, Misc.getHighlightColor(), Misc.getHighlightColor(),armormod);
        tooltip.addPara("gain %s shield strength",0, Misc.getHighlightColor(), Misc.getHighlightColor(),shieldmod);
        tooltip.addPara("gain %s time to live",0, Misc.getHighlightColor(), Misc.getHighlightColor(),damagemod);

        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"Safety overridden, the armor poorly maintained. The shields rooted directly to the flux core, so if one goes so does the other. Someone needs to get fired for this.\"", Misc.getTextColor(), 0f);
        //LabelAPI label = tooltip.addPara("\"Sometimes, you just need to weld some armor to your hull. Just to make sure she makes it out alive, despite the cost\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();

    }
}
