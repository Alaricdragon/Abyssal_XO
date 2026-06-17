package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_Construction;
import Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_SawrmSpawner;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_MasteryShipStats;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatFleetManagerAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.combat.threat.SwarmLauncherEffect;
import com.fs.starfarer.api.loading.VariantSource;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

public class NanoThief_Skill_10 extends NanoThief_SkillBase{
    public NanoThief_Skill_10(NanoThief_ShipSkills skills, ShipAPI ship) {
        super(skills, ship);
        selectNextShip();
    }

    @Override
    public double getMaxCost() {
        int maxCost = 0;
        for (NanoThief_MasteryShipStats a : skills.stats.masteryShips) if (a.cost > maxCost) maxCost = (int) a.cost;
        return maxCost;
    }
    private float cooldown = 0;
    //private boolean onCooldown = false;
    //private boolean waiting = false;
    NanoThief_MasteryShipStats nextShip;
    @Override
    public void advance(float amount) {
        cooldown -= amount;
        if (cooldown > 0) return;
        if (!canBuildShip()) return;
        buildShip();
        selectNextShip();
    }
    @Override
    public void displayStats() {
        boolean overDP = false;
        if (cooldown <= 0){
            if (overDP){
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_10", "graphics/icons/hullsys/temporal_shell.png",
                        "Mastery Status", "Construction swarm for "+nextShip.name+" ready. Cannot launch do to limited dp", true);
                return;
            }
            if (skills.getTotalReclaim() >= nextShip.cost && ship.isPhased()){
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_10", "graphics/icons/hullsys/temporal_shell.png",
                        "Mastery Status", "Ready to construct "+nextShip.name+". cannot prepare construction while phased", true);
                return;
            }
            if (skills.getTotalReclaim() >= nextShip.cost){
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_10", "graphics/icons/hullsys/temporal_shell.png",
                        "Mastery Status", "Ready to construct "+nextShip.name+".", false);
            }else{
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_10", "graphics/icons/hullsys/temporal_shell.png",
                        "Mastery Status", "Swarm Prepared. Require at least "+(int)nextShip.cost+" reclaim to build "+nextShip.name+".", true);
            }
        }else{
            //50 - 40 = 10. 10 / 50 = 0.2 = 20%
            int percentDone = (int)(((nextShip.reloadTime-cooldown) / nextShip.reloadTime)*100);
            Settings.log.info("done eq: (int)((("+nextShip.reloadTime+" - "+cooldown+") / "+nextShip.reloadTime+") * "+100+") = "+percentDone);
            if (skills.getTotalReclaim() >= nextShip.cost){
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_10", "graphics/icons/hullsys/temporal_shell.png",
                        "Mastery Status", percentDone+"% ready to launch construction swarm...", false);
            }else{
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_10", "graphics/icons/hullsys/temporal_shell.png",
                        "Mastery Status", "Swarm Prepared. Require at least "+(int)nextShip.cost+" reclaim to build "+nextShip.name+".", true);
            }
        }
    }
    private void buildShip(){
        skills.useReclaim(nextShip.cost);

        ShipAPI primary = ship;//stats.getShip();
        CombatEngineAPI engine = Global.getCombatEngine();
        CombatFleetManagerAPI manager = engine.getFleetManager(primary.getOwner());
        manager.setSuppressDeploymentMessages(true);

        Vector2f loc = primary.getLocation();
        float facing = (float) Math.random() * 360f;
        ShipAPI fighter = null;
        FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP, SwarmLauncherEffect.CONSTRUCTION_SWARM_VARIANT);
        //Global.getFactory().createFleetMember
        //ShipVariantAPI OVERWRITER = member.getVariant();//Global.getSettings().getVariant("Abyssal_XO_ReclaimCore_Blank").clone();
        //OVERWRITER.setSource(VariantSource.REFIT);
        //OVERWRITER.setWingId(0,skills.stats.OF_fighterToBuild);
        //member.setOwner(primary.getOwner());
        //member.setVariant(OVERWRITER,false,true);

        fighter = manager.spawnFleetMember(member,loc, facing, 0f);
        fighter.setShipAI(new Nano_Thief_AI_Construction(fighter,nextShip,ship.getCurrentCR(),skills.stats));

        manager.removeDeployed(fighter,false);

        manager.setSuppressDeploymentMessages(false);
        Vector2f takeoffVel = Misc.getUnitVectorAtDegreeAngle(facing);
        takeoffVel.scale(fighter.getMaxSpeed() * 1f);



        //skills.stats.getOffinciveFighterCores().add(fighter);
        //return fighter;//note: not a fighter, but instead something very diffrent.
    }
    private boolean canBuildShip(){
        if (!skills.stats.hasSpareDP(nextShip.ship.getFleetPointCost(),ship.getOwner())) return false;
        if (ship.isPhased()) return false;
        if (skills.getTotalReclaim() < nextShip.cost) return false;
        return true;
    }
    private void selectNextShip(){
        int totalOdds = 0;
        for (NanoThief_MasteryShipStats a : skills.stats.masteryShips) totalOdds+=a.weight;
        int random = (int) (Math.random()*totalOdds);
        for (NanoThief_MasteryShipStats a : skills.stats.masteryShips){
            random-=a.weight;
            if (random <= 0){
                nextShip = a;
                cooldown = (float) nextShip.reloadTime;
                return;
            }
        }
        cooldown = (float) nextShip.reloadTime;
        Settings.log.warn("(mastery skill) failed to get ship from mastery. crying about it...");
        //nextShip;
    }
}
