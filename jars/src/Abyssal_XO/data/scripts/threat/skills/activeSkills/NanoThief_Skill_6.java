package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.hullmods.ReclaimCore;
import Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_SawrmSpawner;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_ShipStats;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_6;
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
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;
import second_in_command.SCUtils;
import second_in_command.specs.SCOfficer;

public class NanoThief_Skill_6 extends NanoThief_SkillBase{
    //todo: hold a list of all sim fighters in the Stats.
    //      get fighters to spawn again.
    //      make it so I can detect when a fighter lands somwhow, (and gain reclaim)
    //      make it so I can instantly relocate any 'swarm spawner' to any other ship instantly.
    //      make it so I can force the fighters to land whenever I want them to.
    //      make it so if they refuse they take massive damage and die instantly.
    //      note: I don't need to worry about holding more then one wing in one swarm yet. let them TP at will.
    public NanoThief_Skill_6(NanoThief_ShipSkills skills, ShipAPI ship) {
        super(skills, ship);
    }
    public static void getStats(Nano_Thief_Stats spec, FighterWingSpecAPI a){
        spec.OF_fighterHullSpec = a.getVariant().getHullSpec();
        float range=0;
        for (String b : a.getVariant().getFittedWeaponSlots()){
            if (b == null) continue;
            float c = a.getVariant().getWeaponSpec(b).getMaxRange();
            if (c > range) range = c;
        }
        //spec.OF_range = Math.max(1000,range+200);
        spec.OF_wingSize = a.getNumFighters();
        if (a.getId().equals(Settings.NANO_THIEF_BASEWING)){
            spec.OF_swarmCost = NanoThief_6.BASESWARM_COST;
            spec.OF_productionTime = NanoThief_6.BASESWARM_BUILDTIME;
            spec.OF_ttl = NanoThief_6.BASESWARM_TTL;
            spec.OF_recyclePerFighter = (spec.OF_swarmCost / spec.OF_wingSize)*NanoThief_6.CustomSwarm_RefundPercent;
            logStats(spec);
            return;
        }
        spec.OF_recyclePerFighter = spec.OF_swarmCost / spec.OF_wingSize;
        spec.OF_productionTime = a.getNumFighters() * a.getRefitTime() * NanoThief_6.CustomSwarm_BUILDTIME_PREREFIT;
        spec.OF_recyclePerFighter = (spec.OF_swarmCost / spec.OF_wingSize);
        if (a.getRole().equals(WingRole.BOMBER)){
            spec.OF_ttl = NanoThief_6.CustomSwarm_Bomber_TTL;
            spec.OF_recyclePerFighter *=NanoThief_6.CustomSwarm_RefundPercent_Bomber;
        }else {
            spec.OF_ttl = NanoThief_6.CustomSwarm_TTL;
            spec.OF_recyclePerFighter *= NanoThief_6.CustomSwarm_RefundPercent;
        }
        spec.OF_swarmCost = (a.getOpCost(a.getVariant().getStatsForOpCosts())*NanoThief_6.CustomSwarm_COST_PEROP)+NanoThief_6.CustomSwarm_COST_BASE;
        logStats(spec);
    }
    private static void logStats(Nano_Thief_Stats spec){
        log.info("got swarm of ID: "+spec.OF_fighterToBuild +" stats as: cost: "+spec.OF_swarmCost +", productionTime: "+spec.OF_productionTime +", time to live"+spec.OF_ttl +", and refund per fighter: "+spec.OF_recyclePerFighter);

    }
    public static void displayStats(TooltipMakerAPI panel, FighterWingSpecAPI a){
        Nano_Thief_Stats spec = null;//new Nano_Thief_Stats(a.getId());
        for (SCOfficer b : SCUtils.getFleetData(Global.getSector().getPlayerFleet()).getActiveOfficers()){
            //log.info("      checking SiC officer of atrubuteID: "+b.getAptitudeId());
            if (!b.getAptitudeId().equals("Abyssal_NanoThief")) continue;
            //log.info("      added Sic officer from fleet "+a.getId()+" to list of commanders....");
            spec = new Nano_Thief_Stats(a.getId(),true,b,a.getId());
            break;
        }
        if (spec == null){
            spec = new Nano_Thief_Stats(a.getId());
        }
        getStats(spec,a);
        spec.OF_ttl = spec.getModifedTTL(null);
        spec.OF_productionTime = spec.getModifedProductionTime(null);
        spec.OF_swarmCost = spec.getModifiedCost(null);
        panel.addPara("Simulacrum Fighter Wing stats for fleet: ",5);
        panel.addPara("Time to live: %s",5, Misc.getTextColor(), Misc.getHighlightColor(),""+(int)spec.OF_ttl);
        panel.addPara("Production time: %s",5,Misc.getTextColor(), Misc.getHighlightColor(),""+(int)spec.OF_productionTime);
        panel.addPara("Reclaim cost: %s",5,Misc.getTextColor(), Misc.getHighlightColor(),""+(int)spec.OF_swarmCost);
        panel.addPara("Reclaim gained when a fighter docks: %s",5,Misc.getTextColor(), Misc.getHighlightColor(),""+(int)spec.OF_recyclePerFighter);
    }
    private float cooldown = 0;
    private boolean onCooldown = false;
    private int maxFighters = 0;
    @Override
    public void advance(float amount) {
        //note: NanoThief_ShipStats handles both the spawning and despawinging of swarm cores. This will require change.
        cooldown -= amount;
        if (cooldown > 0) return;
        maxFighters = getMaxFighters();
        if (skills.getTotalReclaim() < skills.stats.OF_swarmCost || currentFighters() >= maxFighters){
            //if (skills.stats.getReadSavedDP() <= 0) skills.stats.getDeployedPonits();
            cooldown = 1;//skills.stats.OF_productionTime;
            onCooldown = false;
            return;
        }
        if (!onCooldown){
            onCooldown = true;
            cooldown = skills.stats.OF_productionTime;
            return;
        }
        //create a combat swarm
        onCooldown = true;
        cooldown = skills.stats.OF_productionTime;
        skills.useReclaim(skills.stats.OF_swarmCost);
        /*todo: I need to apply the following to the combat cores:
                I need to make the core its fighters to the 'stats'. (note: I dont need to save fighters. only cores. because the cores dont despawn untill the fighters are all dead now.)
                I need to find a way to make the fighters return to the fucking carrier when its time to go and rest.

         */
        createCombatSwarmCore();
    }

    @Override
    public void displayStats() {
        maxFighters = getMaxFighters();
        int max = maxFighters;
        int cur = currentFighters();
        if (max <= cur) {
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_6", "graphics/icons/hullsys/temporal_shell.png",
                    "Offencive Fighter Construction Status", cur+" / "+max, true);
            return;
        }
        if (skills.getTotalReclaim() >= skills.stats.OF_swarmCost){
            if (!onCooldown){
                onCooldown = true;
                cooldown = skills.stats.OF_productionTime;
            }
            if (cooldown <= 0 && ship.isPhased()){
                Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_6", "graphics/icons/hullsys/temporal_shell.png",
                        "Offencive Fighter Construction Status", cur+" / "+max+", cannot create fighter in phase", true);
            }
            Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_6", "graphics/icons/hullsys/temporal_shell.png",
                    "Offencive Fighter Construction Status", cur+" / "+max+", "+(int)(((skills.stats.OF_productionTime-cooldown) / skills.stats.OF_productionTime)*100)+"% completed new wing...", false);
            return;
        }
        Global.getCombatEngine().maintainStatusForPlayerShip(Settings.DISPLAYID_NANOTHIEF + "_skill_6", "graphics/icons/hullsys/temporal_shell.png",
                "Offencive Fighter Construction Status", cur+" / "+max+", cannot build wing do to limited reclaim", true);
    }
    private int getMaxFighters(){
        return (int)Math.floor((skills.stats.getDeployedPonits() / NanoThief_6.dpPerFighters)+0.5);
    }
    private int currentFighters(){
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
        //OVERWRITER.setWingId(0,Settings.NANO_THIEF_BASEWING);//'broadsword_wing' from settings causes strange fragment swarm to spawn??? WTF?
        //OVERWRITER.setWingId(0,Settings.NANO_THIEF_PALYER_BASEWING);//'broadsword_wing' from settings causes strange fragment swarm to spawn??? WTF?
        OVERWRITER.setWingId(0,skills.stats.OF_fighterToBuild);
        //OVERWRITER.getWing(0).addTag("independent_of_carrier");
        //OVERWRITER.getWing(0).addTag("auto_fighter");
        //OVERWRITER.setWingId(1,stats.getFighterToBuild());
        //OVERWRITER.setWingId(2,stats.getFighterToBuild());
        member.setOwner(primary.getOwner());
        member.setVariant(OVERWRITER,false,true);

        fighter = manager.spawnFleetMember(member,loc, facing, 0f);
        //engine.ship
        //fighter.setOwner(primary.getOwner());
        //fighter = manager.spawnShipOrWing(member,loc,facing,0f);

        //if (!isAlly) fighter.setAlly(false);
        //log.info("spawning spawner with a wing of: "+this.fighterToBuild);
        //log.info("the fighters ID was given as: "+OVERWRITER.getWing(0).getId());
        //log.info("temp thing: "+fighter.getWing().getSpec());//no wing...? //maybe wing only exsists a short time after creation?
        //log.info("temp thing 2:"+fighter.getLaunchBaysCopy().get(0).getTimeUntilNextReplacement());
        //log.info("got the true ID of the wing as: "+fighter.getLaunchBaysCopy().get(0).getWing().getSpec().getId());
        fighter.setShipAI(new Nano_Thief_AI_SawrmSpawner(fighter,primary,skills.stats.OF_fighterToBuild,skills.stats,true));
        //note: this is usefull for making the guys follow your primary ship. not yet compleated.
        /*if (stats.getReclaimCore() == null){
            ShipAPI core = manager.spawnShipOrWing("Abyssal_XO_ReclaimCore_Blank",loc, facing, 0f,null);
            core.setShipAI(new Nano_Thief_AI_ReclaimCoreBlank(core,primary));
            //core.setAlphaMult(0);
            stats.setReclaimCore(core);
        }*/
        fighter.setCustomData(ReclaimCore.IDOfData,this);

        manager.removeDeployed(fighter,false);



        //fighter = manager.spawnShipOrWing(Settings.NANO_THIEF_CREATER_SHIP, loc, facing, 0f, null);
        //fighter.getWing().setSourceShip(primary);//sets to ifself to prevent min ingagment rage from triggering. might remove if i build a custom AI for the ships.
        manager.setSuppressDeploymentMessages(false);
        Vector2f takeoffVel = Misc.getUnitVectorAtDegreeAngle(facing);
        takeoffVel.scale(fighter.getMaxSpeed() * 1f);

        //Vector2f.add(fighter.getVelocity(), takeoffVel, fighter.getVelocity());

        skills.stats.getOffinciveFighterCores().add(fighter);
        return fighter;//note: not a fighter, but instead something very diffrent.
    }
}
