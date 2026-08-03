package Abyssal_XO.data.scripts.shroudDweller;

import Abyssal_XO.data.scripts.shroudDweller.activeSkills.ShroudDweller_ActiveSkillBase;
import Abyssal_XO.data.scripts.shroudDweller.skills.ShroudDweller_SkillBase;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.BaseEveryFrameCombatPlugin;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import lombok.Getter;
import second_in_command.SCData;
import second_in_command.SCUtils;
import second_in_command.specs.SCBaseSkillPlugin;
import second_in_command.specs.SCOfficer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ShroudDweller_Stats extends BaseEveryFrameCombatPlugin{
    //oh boy this is going to be fun. So how ill this work:
    public int[] skillMulti = new int[11];

    public SCData scData;
    public PersonAPI commander;
    public FleetDataAPI fleet;
    public int owner;
    public FactionAPI faction;

    private String commanderID;

    @Getter
    private ArrayList<ShroudDweller_ActiveSkillBase> skills = new ArrayList<>();
    private static boolean isAlly;

    public float totalEntropy = 0;

    public ShroudDweller_TargetUtils targetUtils;
    public ShroudDweller_Stats(PersonAPI commander, CampaignFleetAPI fleetAPI, FleetDataAPI fleet, String commanderID, boolean isAlly, int owner, FactionAPI faction, SCData scData) {
        this.commander = commander;
        this.fleet = fleet;
        this.commanderID = commanderID;
        //this.officer = officer;
        this.isAlly = isAlly;
        this.owner = owner;
        this.faction = faction;
        this.scData = scData;
        targetUtils = new ShroudDweller_TargetUtils(owner);
        for (SCOfficer c : SCUtils.getFleetData(fleetAPI).getActiveOfficers()) {
            //TODO: change the skillIds and the attribute gotten ID.
            if (c.getAptitudeId().equals("Abyssal_NanoThief")) {
                for (SCBaseSkillPlugin a : c.getActiveSkillPlugins()) {
                    ShroudDweller_SkillBase b = (ShroudDweller_SkillBase) a;
                    boolean abort = false;
                    switch (b.getId()) {
                        case "SiC_NanoThief_NanoThiefBase":
                            skillMulti[0]++;
                            if (!b.addMultiListeners() && skillMulti[0] > 1) abort = true;
                            break;
                        case "SiC_NanoThief_skill_1":
                            skillMulti[1]++;
                            if (!b.addMultiListeners() && skillMulti[1] > 1) abort = true;
                            break;
                        case "SiC_NanoThief_skill_2":
                            skillMulti[2]++;
                            if (!b.addMultiListeners() && skillMulti[2] > 1) abort = true;
                            break;
                        case "SiC_NanoThief_skill_3":
                            skillMulti[3]++;
                            if (!b.addMultiListeners() && skillMulti[3] > 1) abort = true;
                            break;
                        case "SiC_NanoThief_skill_4":
                            skillMulti[4]++;
                            if (!b.addMultiListeners() && skillMulti[4] > 1) abort = true;
                            break;
                        case "SiC_NanoThief_skill_5":
                            skillMulti[5]++;
                            if (!b.addMultiListeners() && skillMulti[5] > 1) abort = true;
                            break;
                        case "SiC_NanoThief_skill_6":
                            skillMulti[6]++;
                            if (!b.addMultiListeners() && skillMulti[6] > 1) abort = true;
                            break;
                        case "SiC_NanoThief_skill_7":
                            skillMulti[7]++;
                            if (!b.addMultiListeners() && skillMulti[7] > 1) abort = true;
                            break;
                        case "SiC_NanoThief_skill_8":
                            skillMulti[8]++;
                            if (!b.addMultiListeners() && skillMulti[8] > 1) abort = true;
                            break;
                        case "SiC_NanoThief_skill_9":
                            skillMulti[9]++;
                            if (!b.addMultiListeners() && skillMulti[9] > 1) abort = true;
                            break;
                        case "SiC_NanoThief_skill_10":
                            skillMulti[10]++;
                            if (!b.addMultiListeners() && skillMulti[10] > 1) abort = true;
                            break;
                    }
                    if (abort) continue;
                    if (!b.addListiner()) continue;
                    skills.add(b.getSkillCode());
                }
            }
        }
    }

    float time = 0;
    @Override
    public void advance(float amount, List<InputEventAPI> events) {
        time += amount;
        if (time < 1.5) return;
        if (targetUtils.hasFriendlyAlive()) for (ShroudDweller_ActiveSkillBase a : skills) a.advance(time);
        time = 0;
        if (storedEntropy != 0) applyEntropy();
    }
    public void addEntropy(float entropy){
        storedEntropy+=entropy;
        totalEntropy+=entropy;
    }
    float storedEntropy = 0;
    private void applyEntropy(){
        if (targetUtils.hasFriendlyAlive()) for (ShroudDweller_ActiveSkillBase a : skills) a.advance(storedEntropy);
        storedEntropy = 0;
    }
}
