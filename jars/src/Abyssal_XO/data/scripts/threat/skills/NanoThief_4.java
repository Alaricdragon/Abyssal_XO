package Abyssal_XO.data.scripts.threat.skills;

import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

public class NanoThief_4 extends Nano_Thief_Skill_Base {
    private static final String key = "AbyssalXO_Nano_Thief_Skill_4";
    private static final float speedChangeMulti = 0.50f;
    private static final int statisSpeedGain = 40;
    private static final float enginDurbilityMulti = 0.9f;
    //private static final float hullMod = 0.95f;
    //private static final float armorMod = 0.95f;
    //private static final float shieldMod = 0.05f;
    //private static float hullChange = 0.95f;
    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {

        String speedMod = (int)(((speedChangeMulti)*100))+"%";

        String hullmod = 100-((int)((enginDurbilityMulti)*100))+"%";
        //String armormod = 100-((int)((armorMod)*100))+"%";
        //String shieldmod = "5%";//(int)(((1+shieldMod)*100)-100)+"%";

        tooltip.addPara("Increase movement speed by %s",0, Misc.getHighlightColor(), Misc.getHighlightColor(),statisSpeedGain+" su");
        tooltip.addPara("Increase movement speed by an additional %s when outside of combat",0, Misc.getHighlightColor(), Misc.getHighlightColor(),speedMod);
        tooltip.addPara("Lose %s engin durability",0, Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),hullmod);

        //tooltip.addPara("Lose %s hull",0, Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),hullmod);
        //tooltip.addPara("Lose %s armor rating",0, Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),armormod);
        //tooltip.addPara("Lose %s shield strength",0, Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),shieldmod);

        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"A few modifications here, a little striped armor there, Connecting the flux core with the ignition just right, and she will fly like a dream.\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();

    }
}
