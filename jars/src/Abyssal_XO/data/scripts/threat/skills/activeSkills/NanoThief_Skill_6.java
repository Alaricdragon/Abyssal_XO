package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.hullmods.SICSkillControllerBackup;
import Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_SawrmSpawner;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_6;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_7;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.CombatEngineAPI;
import com.fs.starfarer.api.combat.CombatFleetManagerAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import com.fs.starfarer.api.loading.VariantSource;
import com.fs.starfarer.api.loading.WingRole;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

public class NanoThief_Skill_6 extends NanoThief_SkillBase{
    public NanoThief_Skill_6(NanoThief_ShipSkills skills, ShipAPI ship) {
        super(skills, ship);
        float buildTimeMulti;
        switch (ship.getHullSize()){
            case CAPITAL_SHIP:
                buildTimeMulti = NanoThief_6.speedPerSize[3] == 0 ? 0 : (float) (1 / NanoThief_6.speedPerSize[3]);
                //buildTimeMulti = (float) (1 + (1-NanoThief_6.speedPerSize[3]));
                break;
            case CRUISER:
                buildTimeMulti = NanoThief_6.speedPerSize[2] == 0 ? 0 : (float) (1 / NanoThief_6.speedPerSize[2]);
                //buildTimeMulti = (float) (1 + (1-NanoThief_6.speedPerSize[2]));
                break;
            case DESTROYER:
                buildTimeMulti = NanoThief_6.speedPerSize[1] == 0 ? 0 : (float) (1 / NanoThief_6.speedPerSize[1]);
                //buildTimeMulti = (float) (1 + (1-NanoThief_6.speedPerSize[1]));
                break;
            default:
                buildTimeMulti = NanoThief_6.speedPerSize[0] == 0 ? 0 : (float) (1 / NanoThief_6.speedPerSize[0]);
                //buildTimeMulti = (float) (1 + (1-NanoThief_6.speedPerSize[0]));
        }
        recharge = skills.stats.OF_productionTime*buildTimeMulti;
    }
    public static void getStats(Nano_Thief_Stats spec, FighterWingSpecAPI a){
        spec.OF_fighterHullSpec = a.getVariant().getHullSpec();
        spec.OF_wingSize = a.getNumFighters();
        spec.OF_fighterToBuild = a.getId();
        if (a.getId().equals(Settings.NANO_THIEF_BASEWING)){
            spec.OF_swarmCost = NanoThief_6.BASESWARM_COST;
            spec.OF_productionTime = NanoThief_6.BASESWARM_BUILDTIME;
            spec.OF_ttl = NanoThief_6.BASESWARM_TTL;
            spec.OF_recyclePerFighter = (float) ((spec.OF_swarmCost / Math.max(spec.OF_wingSize,1))*NanoThief_6.CustomSwarm_RefundPercent);
            logStats(spec);
            return;
        }
        spec.OF_swarmCost = (a.getOpCost(a.getVariant().getStatsForOpCosts())*NanoThief_6.CustomSwarm_COST_PEROP)+NanoThief_6.CustomSwarm_COST_BASE;
        spec.OF_productionTime = (float) (a.getNumFighters() * a.getRefitTime() * NanoThief_6.CustomSwarm_BUILDTIME_PREREFIT);
        spec.OF_recyclePerFighter = (spec.OF_swarmCost / Math.max(spec.OF_wingSize,1));
        spec.OF_ttl = NanoThief_6.CustomSwarm_TTL;
        if (a.getRole().equals(WingRole.BOMBER)){
            //spec.OF_ttl = NanoThief_6.CustomSwarm_Bomber_TTL;
            spec.OF_recyclePerFighter *=NanoThief_6.CustomSwarm_RefundPercent_Bomber;
        }else {
            spec.OF_recyclePerFighter *= NanoThief_6.CustomSwarm_RefundPercent;
        }
        logStats(spec);
    }
    private static void logStats(Nano_Thief_Stats spec){
        log.info("got swarm of ID: "+spec.OF_fighterToBuild +" stats as: cost: "+spec.OF_swarmCost +", productionTime: "+spec.OF_productionTime +", time to live"+spec.OF_ttl +", and refund per fighter: "+spec.OF_recyclePerFighter);
    }
    public float cooldown = 0;
    public boolean onCooldown = false;
    public boolean waiting = false;
    public int maxFighters = 0;
    public float recharge;
    @Override
    public void advance(float amount) {
        //note: NanoThief_ShipStats handles both the spawning and despawinging of swarm cores. This will require change.
        cooldown -= amount;
        if (cooldown > 0) return;
        maxFighters = getMaxFighters();
        double cost = skills.getModifiedCost(skills.stats.OF_swarmCost);
        if (skills.getTotalReclaim() < cost && !waiting){// || currentFighters() >= maxFighters){
            //if (skills.stats.getReadSavedDP() <= 0) skills.stats.getDeployedPonits();
            cooldown = 1;//skills.stats.OF_productionTime;
            onCooldown = false;
            waiting = false;
            return;
        }
        if (!onCooldown){
            onCooldown = true;
            cooldown = recharge;
            return;
        }
        //create a combat swarm
        if (currentFighters() >= maxFighters || ship.isPhased()){
            waiting = true;
            cooldown = 1;
            return;
        }
        waiting = false;
        onCooldown = true;
        cooldown = recharge;
        skills.useReclaim(cost);
        createCombatSwarmCore();
    }

    /*@Override
    public void displayStats() {
        maxFighters = getMaxFighters();
        int max = maxFighters;
        int cur = currentFighters();
        if (skills.getTotalReclaim() >= skills.getModifiedCost(skills.stats.OF_swarmCost)){
            if (!onCooldown && !waiting){
                onCooldown = true;
                cooldown = recharge;//skills.stats.OF_productionTime*recharge;
                //cooldown = skills.stats.OF_productionTime*recharge;
            }
            if (waiting){
                if (ship.isPhased()) {
                    Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_6", "graphics/icons/hullsys/temporal_shell.png",
                            "Offencive Fighter Construction Status", cur + " / " + max + ", cannot create fighter while phased", true);
                    return;
                }
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_6", "graphics/icons/hullsys/temporal_shell.png",
                        "Offencive Fighter Construction Status", cur+" / "+max+", cannot control additional fighters", true);
                return;
            }
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_6", "graphics/icons/hullsys/temporal_shell.png",
                    "Offencive Fighter Construction Status", cur+" / "+max+", "+(int)(((recharge-cooldown) / recharge)*100)+"% prepared to create wing...", false);
            return;
        }
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_6", "graphics/icons/hullsys/temporal_shell.png",
                "Offencive Fighter Construction Status", cur+" / "+max+", cannot build wing do to limited reclaim", true);
    }*/
    public int getMaxFighters(){
        return Math.max((int)Math.floor((skills.stats.getDeployedPonits() / NanoThief_6.dpPerFighters)+0.5) * skills.stats.skillMulti[6],1);
    }
    public int currentFighters(){
        return skills.stats.getOffinciveFighterCores().size();// + skills.stats.getOffinciveFighterWings().size();
    }


    public ShipAPI createCombatSwarmCore(){//ShipAPI primary){
        skills.stats.getDeployedPonits();
        ShipAPI primary = ship;//stats.getShip();
        String wingId = skills.stats.OF_fighterToBuild;//Settings.NANO_THIEF_BASEWING;//SwarmLauncherEffect.RECLAMATION_SWARM_WING;//"attack_swarm_wing"
        CombatEngineAPI engine = Global.getCombatEngine();
        //CombatFleetManagerAPI manager = engine.getFleetManager(FleetSide.ENEMY);//engine.getFleetManager(primary.getOwner());
        CombatFleetManagerAPI manager = engine.getFleetManager(primary.getOwner());
        manager.setSuppressDeploymentMessages(true);

        Vector2f loc = primary.getLocation();
        float facing = (float) Math.random() * 360f;
        //log.info("attempting to create a attack swarm at "+loc.x+", "+loc.y+" at ship of "+primary.getName()+" who's location is "+primary.getLocation().x+", "+primary.getLocation().y);
        ShipAPI fighter = null;
        //Global.getSettings().getVariant("");


        FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP,"Abyssal_XO_ReclaimCore_Blank");
        //Global.getFactory().createFleetMember
        ShipVariantAPI OVERWRITER = member.getVariant();//Global.getSettings().getVariant("Abyssal_XO_ReclaimCore_Blank").clone();
        OVERWRITER.setSource(VariantSource.REFIT);
        OVERWRITER.setWingId(0,skills.stats.OF_fighterToBuild);
        if (primary.getFleetMember() != null && primary.getFleetMember().getFleetData() != null && primary.getFleetMember().getFleetData().getFleet() != null) {
            SICSkillControllerBackup.fleet_global = primary.getFleetMember().getFleetData().getFleet();
            OVERWRITER.addMod(Settings.SIC_CONTROL_HULLMOD);
        }
        member.setOwner(primary.getOwner());
        member.setVariant(OVERWRITER,false,true);

        fighter = manager.spawnFleetMember(member,loc, facing, 0f);
        fighter.setShipAI(new Nano_Thief_AI_SawrmSpawner(fighter,primary,skills.stats.OF_fighterToBuild,skills.stats,true,this));

        manager.removeDeployed(fighter,false);

        manager.setSuppressDeploymentMessages(false);
        Vector2f takeoffVel = Misc.getUnitVectorAtDegreeAngle(facing);
        takeoffVel.scale(fighter.getMaxSpeed() * 1f);

        skills.stats.getOffinciveFighterCores().add(fighter);
        return fighter;//note: not a fighter, but instead something very diffrent.
    }

    @Override
    public double getMaxCost() {
        return skills.stats.OF_swarmCost;
    }
}
