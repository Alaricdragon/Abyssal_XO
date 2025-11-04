package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_ShipSkills;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_SkillBase;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_Skill_1;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import lombok.Getter;
import second_in_command.SCData;

public class NanoThief_1 extends Nano_Thief_Skill_Base {
    /*balance notes:
    * cost:
    * remember: cost effectivly scailes with ship HP. something very important.
    * speed:
    * speed is effectivly, a percent of 'max'. so however large max speed is, the faster the speed will go, even at higher values.
    * */
    private static final String key = "AbyssalXO_Nano_Thief_Skill_1";
    @Getter
    private static final float hullMin = 0.05f;
    @Getter
    private static final float hullMax = 0.9f;
    @Getter
    private static final double speedMax = 0.02d;//2X combat inderance
    @Getter
    private static final double speedMin = 0.0025d;//0.25X combat indurance //1

    @Getter
    private static final int hullPerReclaim = 50;

    @Getter
    private static final double hullRange = hullMax-hullMin;//0.01d
    @Getter
    private static final double hullRepairThing = (speedMax-speedMin);//0.01d
    @Override
    public NanoThief_SkillBase createListiner(NanoThief_ShipSkills skills, ShipAPI ship){
        return new NanoThief_Skill_1(skills,ship);
    }
    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        /*
    every second, recover between 5% and 0.5% hull. This skill is fastest when below 5% hull, and slowest past 90% hull.
    for every 100 hull restored, costs 1 reclaim
         */

        String hullMaxS = (hullMax*100)+"%";
        String hullMinS = (hullMin*100)+"%";
        String speedMaxS = (speedMax*100)+"%";
        String speedMinS = (speedMin*100)+"%";
        String one = "1";
        String reclaimPerHullS = (hullPerReclaim)+"";


        tooltip.addPara("Every second recover a small percentage of hull. This effect becomes faster the more missing hull a ship has, restoring %s hull per second at %s hull, and restoring %s hull per second at %s hull",0, Misc.getHighlightColor(), Misc.getHighlightColor(),speedMaxS,hullMaxS,speedMinS,hullMinS);
        tooltip.addPara("Costs %s reclaim per %s hull repaired",0, Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(),one,reclaimPerHullS);

        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"Look, I don't know what you want me to tell you. Maybe that it will all be fine if we just cut a little more of our funds, but the fact of the matter is simple: You want your fancy devices to work, you pay for repairs.\"", Misc.getTextColor(), 0f);
        //LabelAPI label = tooltip.addPara("\"Yes, its unstable. Hell, its practicly destroys itself when it fires! but dam if its not fun well it lasts\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();

    }
}
