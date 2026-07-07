package Abyssal_XO.data.scripts.threat.skills.interfaces;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_10;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_ShipSkills;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_SkillBase;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_Skill_10;
import com.fs.starfarer.api.Global;

import java.util.ArrayList;

public class NanoThief_Interface_10 extends NanoThief_InterfaceBase {
    private ArrayList<NanoThief_Skill_10> listiners = new ArrayList<>();
    private boolean isSingle;
    @Override
    public void prepareCustomData(ArrayList<NanoThief_SkillBase> listiners) {
        for (NanoThief_SkillBase a : listiners){
            this.listiners.add((NanoThief_Skill_10) a);
        }
        isSingle = listiners.size() == 1;
    }

    @Override
    public boolean validListener(NanoThief_SkillBase a) {
        return a instanceof NanoThief_Skill_10;
    }

    @Override
    public void displayStats() {
        //for (NanoThief_Skill_10 a : listiners){
        //    displayStatsSingle(a);
        //}
        if (isSingle) displayStatsSingle(listiners.get(0));
        else displayMultiStats();
    }
    public void displayMultiStats(){
        //String[] stats = new String[listiners.size()];
        if (!listiners.get(0).hasEnouthCR()){
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_10", "graphics/icons/hullsys/temporal_shell.png",
                    "Mastery Status", "At least "+(int)(NanoThief_10.minCR*100)+"% cr is required to create construction swarms", true);
            return;
        }
        String out = "";
        for (int b = 0; b < listiners.size(); b++){
            NanoThief_Skill_10 a = listiners.get(b);
            a.waitTime = 0.1f;
            out += "("+getSingleString(a)+")";
            if (b != listiners.size() - 1) out += ", ";
        }
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_10", "graphics/icons/hullsys/temporal_shell.png",
                "Mastery Status", out, false);
    }
    private String getSingleString(NanoThief_Skill_10 single){
        if (single.cooldown <= 0){
            single.cooldown = 0;
            if (!single.hasEnouthDP()){
                return single.nextShip.name+": 100%. require "+single.nextShip.ship.getFleetPointCost()+"dp";
            }
            if (single.hasEnouthReclaim() && single.inStateOfPrevention()){
                return single.nextShip.name+": 100%. cant deploy in phase";
            }
            if (single.hasEnouthReclaim()){
                return single.nextShip.name+": 100%";
            }else{
                return single.nextShip.name+": costs "+(int)single.nextShip.cost+" reclaim";
            }
        }else{
            //50 - 40 = 10. 10 / 50 = 0.2 = 20%
            int percentDone = (int)(((single.nextShip.reloadTime-single.cooldown) / single.nextShip.reloadTime)*100);
            //Settings.log.info("done eq: (int)((("+nextShip.reloadTime+" - "+cooldown+") / "+nextShip.reloadTime+") * "+100+") = "+percentDone);

            if (single.hasEnouthReclaim()){
                return single.nextShip.name+": "+percentDone+"%";
            }else{
                return single.nextShip.name+": costs "+(int)single.nextShip.cost+" reclaim";
            }
        }
    }
    public void displayStatsSingle(NanoThief_Skill_10 single) {
        single.waitTime = 0.1f;//for player ship waittime. for a faster, smover, interface.
        if (!single.hasEnouthCR()){
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_10", "graphics/icons/hullsys/temporal_shell.png",
                    "Mastery Status", "At least "+(int)(NanoThief_10.minCR*100)+"% cr is required to create construction swarms", true);
            return;
        }
        //cooldown-=waiting;//player gets a faster interface.
        //waiting = 0;
        if (single.cooldown <= 0){
            single.cooldown = 0;
            if (!single.hasEnouthDP()){
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_10", "graphics/icons/hullsys/temporal_shell.png",
                        "Mastery Status", "Cannot build "+single.nextShip.name+". Requires "+single.nextShip.ship.getDeploymentPointsCost()+" dp.", true);
                return;
            }
            if (single.hasEnouthReclaim() && single.inStateOfPrevention()){
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_10", "graphics/icons/hullsys/temporal_shell.png",
                        "Mastery Status", "Cannot build "+single.nextShip.name+" while phased", true);
                return;
            }
            if (single.hasEnouthReclaim()){
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_10", "graphics/icons/hullsys/temporal_shell.png",
                        "Mastery Status", "Ready to construct "+single.nextShip.name+".", false);
            }else{
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_10", "graphics/icons/hullsys/temporal_shell.png",
                        "Mastery Status", "Cannot build "+single.nextShip.name+". requires "+(int)single.nextShip.cost+" reclaim", true);
            }
        }else{
            //50 - 40 = 10. 10 / 50 = 0.2 = 20%
            int percentDone = single.onCooldown ? (int)(((single.nextShip.reloadTime-single.cooldown) / single.nextShip.reloadTime)*100) : 100;
            //Settings.log.info("done eq: (int)((("+nextShip.reloadTime+" - "+cooldown+") / "+nextShip.reloadTime+") * "+100+") = "+percentDone);
            if (single.hasEnouthReclaim()){
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_10", "graphics/icons/hullsys/temporal_shell.png",
                        "Mastery Status", percentDone+"% ready to build "+single.nextShip.name+". Requires "+(int)single.nextShip.cost+" reclaim", false);
            }else{
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_10", "graphics/icons/hullsys/temporal_shell.png",
                        "Mastery Status", percentDone+"% ready to build "+single.nextShip.name+". Requires "+(int)single.nextShip.cost+" reclaim", true);
            }
        }
    }
}
