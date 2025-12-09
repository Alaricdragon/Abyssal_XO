package Abyssal_XO.data.scripts.threat.skills;

import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

public class NanoThief_9 extends Nano_Thief_Skill_Base {
    private static float costChange = 0.8f;
    private static float controlChange = 0.5f;
    private static float buildTimeChange = 0.5f;
    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        /*when you take more then 10% hull damage in 5 seconds, or when hull is below 20%
        if overloaded:
        reduce overload time by 50%. costs 50 reclaim per second reduced.

        for 10 seconds, for every 1% of missing hull:
        increase speed of all nano-thief skills by 2% (excluding desperate measures)
        increase the cost of all nano-thief skills by 1% (excluding desperate measures)

        for 10 seconds, well your CR is below 20%:
        regenerate 1% CR a second. costs 50 reclaim per CR regenerated
        can only trigger once every 10 seconds*/
        /*todo:
            1) decide on the different bonus macanics present here
                -)CR bonuses
                    1) when below 40% CR
                        regenerate 1 CR every 10 - 0.5 seconds for 50 reclaim.
                        increase skill speed by 0.25% - 1% per missing CR, up to a maximum of 20%.
                        this skill becomes mush more powerful the closer to 0 CR your ship is.
                -) HP bonuses:
                    1) for every 1% of missing hull:
                        - increase speed of all nano-thief by 2%
                        - increase the cost of all nano-thief skills by 1%
                -) if overloaded:
                    reduce overload by 50%. costs 50 reclaim per second reduced.
                    for 5 seconds after a overload:
                        - increase the speed of all nano-thief skills by 2% per second reduced
                        - increase cost of all nano-thief sills by 1% per second reduced.
            2) create descriptions for this
            3) make Nano_Thief_Skill for this skill.
                1) make it so NanoThief_ShipSkills can have a list of skills as input.
                2) make multible skills. Have each skill effect themselfs.
                3) keep in mind that skills that reduce skill cooldown should not be effected by themselves (so they unmodified themself.)
        */
        String costmod = 100-((int)((costChange)*100))+"%";
        String buildmod = 100-((int)((buildTimeChange)*100))+"%";
        String controlmod = 100-((int)((controlChange)*100))+"%";

        tooltip.addPara("Cost %s less",0,Misc.getHighlightColor(),Misc.getHighlightColor(),costmod);
        tooltip.addPara("Take %s less time to build",0,Misc.getHighlightColor(),Misc.getHighlightColor(),buildmod);
        tooltip.addPara("Use %s less control",0,Misc.getHighlightColor(),Misc.getHighlightColor(),controlmod);

        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"Efficiency is not something mastered in a day. Sometimes, it can take many cycles to get everything working just right. To obtain... Perfection.\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();

    }
}
