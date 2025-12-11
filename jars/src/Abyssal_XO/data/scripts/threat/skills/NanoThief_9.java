package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_ShipSkills;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_SkillBase;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.Sys;
import second_in_command.SCData;

public class NanoThief_9 extends Nano_Thief_Skill_Base {

    public static double crStart = 0.4;
    private static double crStartForEq = crStart*100;
    public static double crReginSpeed = 0.001;//0.25%. speed at min value. is increased by eq.
    public static double crSkillSpeed = 0.01;//2.5%. speed at min value. is increase by eq
    public static double crSkillCost = 0.005;//2.5%. speed at min value. is increase by eq

    public static double hpSpeed = 0.02;//2% speed per missing hull.
    public static double hpCost = 0.01;

    //ship.getFluxTracker().getOverloadTimeRemaining();
    //ship.getFluxTracker().setOverloadDuration();
    public static double overloadMinTime = 1;
    public static double overloadRemoved = 0.5;
    public static double overloadCostPerSecond = 50;
    public static double overloadEffectTime = 5;
    public static double overloadSkillCost = 0.05;
    public static double overloadSkillSpeed = 0.1;
    public static void main(String[] args){
        double cr = 0.0;
        System.out.println("cr: "+cr*100);
        System.out.println("cr gain speed: "+crRegenSpeed(cr)*100);
        System.out.println("skill speed: "+crSkillSpeed(cr));
        System.out.println("skill cost: "+crSkillCost(cr));
    }
    public static double crRegenSpeed(double cr){
        double cr2 = (cr*100);
        //System.out.println("    cr: "+cr2);
        double a = ((crStartForEq+10)-cr2) /10;//so max cr is 1, -10 is 2, -20 is 4, -30 is 8.
        //System.out.println("    a: "+a);
        return crReginSpeed *(Math.pow(2,a)/2);
    }
    public static double crSkillSpeed(double cr){
        double cr2 = (cr*100);
        double a = ((crStartForEq+10)-cr2) /10;//so max cr is 1, -10 is 2, -20 is 4, -30 is 8.
        return crSkillSpeed *(Math.pow(2,a)/2);//so at max cr it is bonus*1, -10 it is bonus*2, -20 it is bonus*4, -30 it is bonus*8
    }
    public static double crSkillCost(double cr){
        double cr2 = (cr*100);
        double a = ((crStartForEq+10)-cr2) /10;//so max cr is 1, -10 is 2, -20 is 4, -30 is 8.
        return crSkillCost *(Math.pow(2,a)/2);//so at max cr it is bonus*1, -10 it is bonus*2, -20 it is bonus*4, -30 it is bonus*8
    }
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
        /*so... how should I manage this?
            for an eq, I could simple, have:
            //so at 100 and 0.01, it would be: 100 * 100 = 10000.
            lost HP * (bonus * lost HP).

            so for CR recovery:
            min = 0.025%
            max = 0.01
            40% - 0%.

            devide it as follows:
            below 40%:
                gain 10 second cr regen.
                gain 0.25% per missing cr skill speed.
            below 30%
                gain 5 second cr regen
                gain 0.5% per missing cr skill speed.
            below 20%:
                gain 2.5 second cr regen
                gain 0.75% per missing cr skill speed.
            below 10%:
                gain 1.25 second cr regen
                gain 1% per missing cr skill speed.

            eq:
                bonus   = 1 / 2 / 4 / 8 division.
                bonus2  = 1 / 2 / 3 / 4 multi
                eq for 1:
                    a = (50-cr) / 10.     (1,2,3,4)
                    divide = (2 pow a)/2. (1,2,4,8)


                eq for 2: multi = (40 / 10)
        */
        /*todo:
            1) decide on the different bonus macanics present here
                -)CR bonuses
                    1) when below 40% CR
                        regenerate 1 CR every 10 - 1.25 seconds for 50 reclaim.
                        increase skill speed by 1% - 8%.
                        increase skill cost by 0.5% - 4%.
                        this skill becomes mush more powerful the closer to 0 CR your ship is.
                -) HP bonuses:
                    1) for every 1% of missing hull:
                        - increase speed of all nano-thief by 2%
                        - increase the cost of all nano-thief skills by 1%
                -) if overloaded:
                    reduce overload duration past 1 second 50%. costs 50 reclaim per second reduced.
                    for 5 seconds after a overload:
                        - increase the speed of all nano-thief skills by 10%
                        - increase cost of all nano-thief sills by 5%
                -) Nano Thief skills are no longer disabled well overloaded or venting.
            2) create descriptions for this
            3) make Nano_Thief_Skill for this skill.
                1) make it so NanoThief_ShipSkills can have a list of skills as input.
                2) make multible skills. Have each skill effect themselfs.
                3) keep in mind that skills that reduce skill cooldown should not be effected by themselves (so they unmodified themself.)
        */
        String line2a = (int)(hpSpeed*100)+"%";
        String line3a = (int)(hpCost*100)+"%";

        String line4a = (int)(crStart*100)+"%";
        String line5a = ((int)(crRegenSpeed(crStart)*10000))/100f+"%";
        String line5b = (((int)(1/(crRegenSpeed(0))*10000))/100f)+"%";
        String line6a = ((int)(1/(crSkillSpeed(crStart))*10000))/100f+"%";
        String line6b = ((int)(crSkillSpeed(0)*10000))/100f+"%";
        String line7a = ((int)(crSkillCost(crStart)*10000))/100f+"%";
        String line7b = ((int)(crSkillCost(0)*10000))/100f+"%";

        String line8a = (int)overloadMinTime+"";
        String line8b = (int)(overloadRemoved*100)+"%";
        String line8c = (int)(overloadCostPerSecond)+"";
        String line9a = (int)(overloadEffectTime)+"";
        String line10a = (int)(overloadSkillSpeed * 100)+"%";
        String line11a = (int)(overloadSkillCost*100)+"%";
        tooltip.addPara("For every %s missing hull:",0,Misc.getHighlightColor(),Misc.getHighlightColor(),"1%");
        tooltip.addPara("   -increase the speed of all Nano Thief skills by %s",0,Misc.getTextColor(),Misc.getHighlightColor(),line2a);
        tooltip.addPara("   -increase the cost of all Nano Thief skills by %s",0,Misc.getTextColor(),Misc.getNegativeHighlightColor(),line3a);
        tooltip.addPara("When below %s Combat Readyness:",0,Misc.getHighlightColor(),Misc.getHighlightColor(),line4a);
        tooltip.addPara("   -regenerate 1 cr every %s - %s second",0,Misc.getTextColor(),Misc.getHighlightColor(),line5a,line5b);
        tooltip.addPara("   -increase the speed of all Nano Thief skills by %s - %s",0,Misc.getTextColor(),Misc.getHighlightColor(),line6a,line6b);
        tooltip.addPara("   -increase the cost of all Nano Thief skills by %s - %s",0,Misc.getTextColor(),Misc.getNegativeHighlightColor(),line7a,line7b);
        tooltip.addPara("   -this effect becomes mush more powerful as your cr reaches 0",0,Misc.getTextColor(),Misc.getHighlightColor());
        tooltip.addPara("If overloaded:",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        tooltip.addPara("   -reduce overload duration past %s second %s",0,Misc.getTextColor(),Misc.getHighlightColor(),line8a,line8b);
        tooltip.addPara("   -costs %s reclaim per second reduced",0,Misc.getTextColor(),Misc.getNegativeHighlightColor(),line8c);
        tooltip.addPara("For %s seconds after overloading:",0,Misc.getHighlightColor(),Misc.getHighlightColor(),line9a);
        tooltip.addPara("   -increase the speed of all Nano Thief skills by %s",0,Misc.getTextColor(),Misc.getHighlightColor(),line10a);
        tooltip.addPara("   -increase the cost of all Nano Thief skills by %s",0,Misc.getTextColor(),Misc.getNegativeHighlightColor(),line11a);
        tooltip.addPara("All Nano Thief skill now work well overloading and venting",0,Misc.getHighlightColor(),Misc.getHighlightColor());

        tooltip.addSpacer(10f);
        LabelAPI label = tooltip.addPara("\"Our armor is striped to nothing, our weapons damaged past repair, and our hull is in a critical state. Our ship is at its limits, and I know we not the only ones in this situation. Caption, we cannot continue like this. If we don't do something desperate, none of us will return to command.\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();
    }

    @Override
    public NanoThief_SkillBase createListiner(NanoThief_ShipSkills skills, ShipAPI ship) {
        return super.createListiner(skills, ship);
    }
}
