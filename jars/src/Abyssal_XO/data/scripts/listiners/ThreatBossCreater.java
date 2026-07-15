package Abyssal_XO.data.scripts.listiners;

import Abyssal_XO.data.scripts.AI.ThreatBossController;
import Abyssal_XO.data.scripts.Settings;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.impl.campaign.ids.Tags;
import com.fs.starfarer.api.impl.combat.threat.DisposableThreatFleetManager;
import com.fs.starfarer.api.impl.combat.threat.ThreatFleetBehaviorScript;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;
import second_in_command.SCData;
import second_in_command.SCUtils;
import second_in_command.misc.SCSettings;
import second_in_command.specs.SCOfficer;

import java.util.Random;

import static Abyssal_XO.data.scripts.Settings.MEMKEY_NANOTHIEF_BOSSFLEET;
import static Abyssal_XO.data.scripts.Settings.MEMKEY_NANOTHIEF_BOSSSCRIPT;

public class ThreatBossCreater extends BaseCampaignEventListener {
    /*todo:
        1: test and make sure the hostile is removed on new system enter
        2: make sure the boss is not removed on same system enter.
        3: make sure the AI switches back to patroling on system leave.
        4: make a 'boss difficulty' slider for options.

     */

    private CampaignFleetAPI fleet;
    //private EveryFrameScript script = null;
    public ThreatBossCreater(boolean permaRegister) {
        super(permaRegister);
        Settings.log.info("NF_BOSS_CREATE: It is active.");
        //DO NOT perma register.
        rememberFleet();
    }
    /*
    //to hyperspace from system.
302484 [Thread-2] INFO  Abyssal_XO.data.scripts.Settings  - NF_BOSS_CREATE: reported player jumped to: unknown location
302484 [Thread-2] INFO  Abyssal_XO.data.scripts.Settings  - NF_BOSS_CREATE: (player location:) fleet, star system, is hyperspace: false, true, true
302484 [Thread-2] INFO  Abyssal_XO.data.scripts.Settings  - NF_BOSS_CREATE: (from) name, star system, is hyperspace: Player Fleet, true, true
302484 [Thread-2] INFO  Abyssal_XO.data.scripts.Settings  - NF_BOSS_CREATE: (to) name, star system, is hyperspace: unknown location, true, true

    //hyperspace to system:
    384349 [Thread-2] INFO  Abyssal_XO.data.scripts.Settings  - NF_BOSS_CREATE: reported player jumped to: unknown location
384351 [Thread-2] INFO  Abyssal_XO.data.scripts.Settings  - NF_BOSS_CREATE: (player location:) fleet, star system, is hyperspace: false, false, false
384351 [Thread-2] INFO  Abyssal_XO.data.scripts.Settings  - NF_BOSS_CREATE: (from) name, star system, is hyperspace: Player Fleet, false, false
384351 [Thread-2] INFO  Abyssal_XO.data.scripts.Settings  - NF_BOSS_CREATE: (to) name, star system, is hyperspace: unknown location, false, false
384351 [Thread-2] INFO  Abyssal_XO.data.scripts.Settings  - NF_BOSS_CREATE: fleet, star system, is hyperspace: false, false, false
384351 [Thread-2] INFO  Abyssal_XO.data.scripts.Settings  - NF_BOSS_CREATE: trying to spawn fleet...
384351 [Thread-2] INFO  Abyssal_XO.data.scripts.Settings  - NF_BOSS_CREATE: attempting to despawn fleet.
384351 [Thread-2] INFO  Abyssal_XO.data.scripts.Settings  - NF_BOSS_CREATE: -fleet despawned or not does not exsist...
384352 [Thread-2] INFO  Abyssal_XO.data.scripts.Settings  - NF_BOSS_CREATE: -destination is threat. spawning fleet.
384352 [Thread-2] INFO  Abyssal_XO.data.scripts.Settings  - NF_BOSS_CREATE: SPAWNED FLEET IN SYSTEM NAMED: Deep Space
384352 [Thread-2] INFO  Abyssal_XO.data.scripts.Settings  - NF_BOSS_CREATE: -finished spawning fleet.
384352 [Thread-2] INFO  Abyssal_XO.data.scripts.Settings  - NF_BOSS_CREATE: saved fleet as id: N/A

     */
    public void reportFleetJumped(CampaignFleetAPI fleet, SectorEntityToken from, JumpPointAPI.JumpDestination to) {
        if (!fleet.isPlayerFleet()) return;
        Settings.log.info("NF_BOSS_CREATE: reported player jumped to: "+to.getDestination().getName());
        CampaignFleetAPI pf = Global.getSector().getPlayerFleet();
        //Settings.log.info("NF_BOSS_CREATE: (player location:) fleet, star system, is hyperspace: "+(pf==null)+", "+(pf.getStarSystem()==null)+", "+(pf.isInHyperspace()));
        //Settings.log.info("NF_BOSS_CREATE: (from) name, star system, is hyperspace: "+(from.getName())+", "+(from.getStarSystem()==null)+", "+(from.isInHyperspace()));
        //Settings.log.info("NF_BOSS_CREATE: (to) name, star system, is hyperspace: "+(to.getDestination().getName())+", "+(to.getDestination().getStarSystem()==null)+", "+(to.getDestination().isInHyperspace()));
        tryToSpawnFleet();
    }
    private void rememberFleet(){
        Settings.log.info("NF_BOSS_CREATE: gathering memory of fleet");
        if (Global.getSector().getMemory().contains(MEMKEY_NANOTHIEF_BOSSFLEET)){
            Settings.log.info("NF_BOSS_CREATE: -got fleet");
            fleet = (CampaignFleetAPI) Global.getSector().getMemory().get(MEMKEY_NANOTHIEF_BOSSFLEET);
            //script = (EveryFrameScript) Global.getSector().getMemory().get(MEMKEY_NANOTHIEF_BOSSSCRIPT);
        }
    }
    public void saveFleet(){
        Settings.log.info("NF_BOSS_CREATE: saved fleet as id: "+(fleet == null ? "N/A":fleet.getId()));
        Global.getSector().getMemory().set(MEMKEY_NANOTHIEF_BOSSFLEET,fleet);
        //Global.getSector().getMemory().set(MEMKEY_NANOTHIEF_BOSSSCRIPT,script);
    }
    private void tryToSpawnFleet() {
        CampaignFleetAPI pf = Global.getSector().getPlayerFleet();
        Settings.log.info("NF_BOSS_CREATE: fleet, star system, is hyperspace: "+(pf==null)+", "+(pf.getStarSystem()==null)+", "+(pf.isInHyperspace()));
        if (pf == null || pf.getStarSystem() == null || pf.isInHyperspace()) return;
        StarSystemAPI to = pf.getStarSystem();
        Settings.log.info("NF_BOSS_CREATE: trying to spawn fleet...");
        if (!despawnFleet(to) && fleet != null) return;
        Settings.log.info("NF_BOSS_CREATE: -fleet despawned or not does not exsist...");
        //if (to.getDestination() == null) return;
        //Settings.log.info("NF_BOSS_CREATE: -destination not null");
        //if (to.getDestination().isInHyperspace()) return;
        //Settings.log.info("NF_BOSS_CREATE: -destination not hyperspace");
        //if (to.getDestination().getStarSystem() == null) return;
        //Settings.log.info("NF_BOSS_CREATE: -destination is star system");
        if (to.hasTag(Tags.SYSTEM_CAN_SPAWN_THREAT)){
            Settings.log.info("NF_BOSS_CREATE: -destination is threat. spawning fleet.");
            fleet = spawnFleet(to);
        }else{
            Settings.log.info("NF_BOSS_CREATE: -destination not threat. refused to spawn.");
            fleet = null;
        }
        Settings.log.info("NF_BOSS_CREATE: -finished spawning fleet.");
        saveFleet();
    }
    public CampaignFleetAPI spawnFleet(StarSystemAPI systemAPI){
        Settings.log.info("NF_BOSS_CREATE: SPAWNED FLEET IN SYSTEM NAMED: "+systemAPI.getName());
        CampaignFleetAPI fleetTemp = DisposableThreatFleetManager.createThreatFleet(3,0,0, DisposableThreatFleetManager.FabricatorEscortStrength.MAXIMUM,null);

        systemAPI.addEntity(fleetTemp);

        float radius = 4000f + 2000f * (float) Math.random();
        Vector2f loc = Misc.getPointAtRadius(new Vector2f(), radius);
        fleetTemp.setLocation(loc.x, loc.y);
        ThreatBossController script = new ThreatBossController(fleetTemp);
        fleetTemp.addScript(script);

        SCData scData = SCUtils.getFleetData(fleetTemp);
        for (SCOfficer a : scData.getActiveOfficers()) scData.removeOfficerFromFleet(a);
        //SCSettings.Companion.getAdditionalSlotForNPCFleets();
        //SCSettings.Companion.getAdditionalLevel();
        for (int a = 0; a < (SCSettings.Companion.getAdditionalSlotForNPCFleets() ? 4 : 3); a++){
            //triple threat = )
            SCOfficer officer = new SCOfficer(Global.getSector().getFaction("threat").createRandomPerson(), "Abyssal_NanoThief");
            officer.setSkillPoints(SCSettings.Companion.getAdditionalLevel() ? 6 : 5);
            officer.levelUpIfNeeded();
            scData.addOfficerToFleet(officer);
            scData.setOfficerInSlot(a,officer);
            //todo: level up officers.
        }
        Settings.log.info("NF_BOSS_CREATE. added SiC officers. now has: "+scData.getActiveOfficers().size()+" Officers.");
        return fleetTemp;
    }
    private boolean despawnFleet(StarSystemAPI to){
        Settings.log.info("NF_BOSS_CREATE: attempting to despawn fleet.");
        if (fleet == null || fleet.getStarSystem() == null) return false;
        Settings.log.info("NF_BOSS_CREATE: -fleet and system exsist.");
        if (to == null) return false;
        Settings.log.info("NF_BOSS_CREATE: -to not null");
        if (fleet.getStarSystem().equals(to)) return false;
        Settings.log.info("NF_BOSS_CREATE: -fleet is not in same system. despawning.");
        fleet.despawn();
        //if (script != null) fleet.removeScript(script); //script is bound to the fleet. fleet is despawned. does this script need to be removed?!?!
        fleet = null;
        //script = null;
        saveFleet();//save fleet if required. a just in case for when I destroy the fleet.
        return true;
    }
}
