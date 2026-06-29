package Abyssal_XO.data.scripts.threat.skills.interfaces;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_SkillBase;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_Skill_2;
import com.fs.starfarer.api.Global;

import java.util.ArrayList;

public class NanoThief_Interface_2 extends NanoThief_InterfaceBase{
    private ArrayList<NanoThief_Skill_2> listiners = new ArrayList<>();
    @Override
    public void prepareCustomData(ArrayList<NanoThief_SkillBase> listiners) {
        for (NanoThief_SkillBase a : listiners) this.listiners.add((NanoThief_Skill_2) a);
    }

    @Override
    public boolean validListener(NanoThief_SkillBase a) {
        return a instanceof NanoThief_Skill_2;
    }

    @Override
    public void displayStats() {
        if (listiners.size() == 1) displaySingle(listiners.get(0));
        else displayMulti();
    }
    private void displaySingle(NanoThief_Skill_2 single){
        single.secondsPerReceack = 0.25f;
        if (single.onCooldown){
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_2", "graphics/icons/hullsys/temporal_shell.png",
                    "Scrapwork Microforge", ""+(int)(100-((single.cooldown/single.coolDownInstance)*100))+"% Ready",true);
            // 10 / 5 = 2. 5 / 10 = 0.5
            return;
        }
        //Settings.log.info("get cost, totalreclaim: "+skills.getModifiedCost(single.lowestCurrentCost)+", "+skills.getTotalReclaim());
        if (skills.getModifiedCost(single.lowestCurrentCost) > skills.getTotalReclaim()){
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_2", "graphics/icons/hullsys/temporal_shell.png",
                    "Scrapwork Microforge", "Require at least "+skills.getModifiedCost(single.lowestCurrentCost)+" reclaim to forge missiles",true);
            return;
        }
        if (single.lowestCurrentCost != 0){
            single.cooldown = 0;
        }
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_2", "graphics/icons/hullsys/temporal_shell.png",
                "Scrapwork Microforge", "Ready to activate",false);
    }
    private void displayMulti(){
        String out = "";
        boolean debuff = false;
        if (skills.getModifiedCost(listiners.get(0).lowestCurrentCost) > skills.getTotalReclaim()){
            out += "Require "+skills.getModifiedCost(listiners.get(0).lowestCurrentCost)+" reclaim. ";
            debuff = true;
        }
        // if (listiners.get(0).lowestCurrentCost != 0){
        //    listiners.get(0).cooldown = 0;
        //}
        int ready = 0;
        String temp = "";
        for (int b = 0; b < listiners.size(); b++){
            NanoThief_Skill_2 a = listiners.get(b);
            a.secondsPerReceack = 0.25f;
            if (!a.onCooldown){
                ready++;
                continue;
            }
            temp += getSingleString(a);
            if (b != listiners.size() - 1) temp+=", ";
        }
        if (ready == 0) debuff = true;
        else out += "charges: ("+ready+") ";
        out+=temp;
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_2", "graphics/icons/hullsys/temporal_shell.png",
                "Scrapwork Microforge", out,debuff);
    }
    private String getSingleString(NanoThief_Skill_2 a){
        //note: only rus when not on cooldown, so this is fine.
        if (a.lowestCurrentCost != 0){
            a.cooldown = 0;
            a.advance(0);//because this can trigger --NOW--. or not...?
        }
        return "("+(int)(100-((a.cooldown/a.coolDownInstance)*100))+"%)";
    }
}
