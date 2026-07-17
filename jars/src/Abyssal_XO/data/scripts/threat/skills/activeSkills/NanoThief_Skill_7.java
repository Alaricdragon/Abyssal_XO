package Abyssal_XO.data.scripts.threat.skills.activeSkills;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.Utils;
import Abyssal_XO.data.scripts.hullmods.SICSkillControllerBackup;
import Abyssal_XO.data.scripts.threat.AI.Nano_Thief_AI_SawrmSpawner;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_6;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_7;
import Abyssal_XO.data.scripts.threat.skills.interfaces.NanoThief_Interface_7;
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
import lombok.Getter;
import lombok.Setter;
import org.lwjgl.util.vector.Vector2f;

import static Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_Skill_6.getFighterWingSize;

public class NanoThief_Skill_7 extends NanoThief_SkillBase{
    public void addRechargeTime(double percent){
        cooldown -= recharge;
        if (cooldown < 0) cooldown = 0;
    }
    public float cooldown = 0;
    //public boolean onCooldown = false;
    public boolean waiting = false;
    public float recharge;
    @Setter
    @Getter
    private NanoThief_Interface_7 interface7;
    public boolean WaitingOnReclaim = false;
    public NanoThief_Skill_7(NanoThief_ShipSkills skills, ShipAPI ship) {
        super(skills, ship);
        float buildTimeMulti;
        //double numShips;
        switch (ship.getHullSize()){
            case CAPITAL_SHIP:
                //numShips = NanoThief_7.numPerSize[3];
                buildTimeMulti = NanoThief_7.speedPerSize[3] == 0 ? 0 : (float) (1 / NanoThief_7.speedPerSize[3]);
                //buildTimeMulti = (float) (1 + (1-NanoThief_7.speedPerSize[3]));//0.5+1 = 1.5 // 1 / 0.5 = 2.
                break;
            case CRUISER:
                //numShips = NanoThief_7.numPerSize[2];
                buildTimeMulti = NanoThief_7.speedPerSize[2] == 0 ? 0 : (float) (1 / NanoThief_7.speedPerSize[2]);
                //buildTimeMulti = (float) (1 + (1-NanoThief_7.speedPerSize[2]));//0.25 + 1  = 1.25 // 1 / 0.75 =
                break;
            case DESTROYER:
                //numShips = NanoThief_7.numPerSize[1];
                buildTimeMulti = NanoThief_7.speedPerSize[1] == 0 ? 0 : (float) (1 / NanoThief_7.speedPerSize[1]);
                //buildTimeMulti = (float) (1 + (1-NanoThief_7.speedPerSize[1]));//
                break;
            default:
                //numShips = NanoThief_7.numPerSize[0];
                buildTimeMulti = NanoThief_7.speedPerSize[0] == 0 ? 0 : (float) (1 / NanoThief_7.speedPerSize[0]);
                //buildTimeMulti = (float) (1 + (1-NanoThief_7.speedPerSize[0]));//-0.25 +1 = 0.75 // 3: 1+(1-3) = 1 + -2 = -1.
        }
        recharge = skills.stats.DF_productionTime*buildTimeMulti;
        cooldown = recharge;
        //maxFighters = (int) numShips;
        /*for (ShipAPI mods : ship.getChildModulesCopy()){
            switch (ship.getHullSize()){
                case CAPITAL_SHIP:
                    speedMult += NanoThief_7.speedPerModule[3];
                    numShips += NanoThief_7.numbPerModule[3];
                    break;
                case CRUISER:
                    speedMult += NanoThief_7.speedPerModule[2];
                    numShips += NanoThief_7.numbPerModule[2];
                case DESTROYER:
                    speedMult += NanoThief_7.speedPerModule[1];
                    numShips += NanoThief_7.numbPerModule[1];
                default:
                    speedMult += NanoThief_7.speedPerModule[0];
                    numShips += NanoThief_7.numbPerModule[0];
            }
        }*/
    }
    public static void getStats(Nano_Thief_Stats spec, FighterWingSpecAPI a){
        spec.DF_fighterHullSpec = a.getVariant().getHullSpec();
        spec.DF_wingSize = getFighterWingSize(spec, a);
        spec.DF_fighterToBuild = a.getId();
        if (a.getId().equals(Settings.NANO_THIEF_BASEWING)){
            spec.DF_swarmCost = NanoThief_6.BASESWARM_COST;
            spec.DF_productionTime = NanoThief_6.BASESWARM_BUILDTIME;
            spec.DF_ttl = NanoThief_6.BASESWARM_TTL;
            spec.DF_recyclePerFighter = (float) ((spec.DF_swarmCost / Math.max(spec.DF_wingSize,1))*NanoThief_6.CustomSwarm_RefundPercent);
            logStats(spec);
            return;
        }
        spec.DF_swarmCost = (a.getOpCost(a.getVariant().getStatsForOpCosts())*NanoThief_6.CustomSwarm_COST_PEROP)+NanoThief_6.CustomSwarm_COST_BASE;
        spec.DF_productionTime = (float) ((a.getNumFighters() * a.getRefitTime() * NanoThief_6.CustomSwarm_BUILDTIME_PREREFIT) + NanoThief_6.CustomSwarm_BUILDTIME_BASE);
        spec.DF_recyclePerFighter = (spec.DF_swarmCost / Math.max(spec.DF_wingSize,1));
        spec.DF_ttl = NanoThief_6.CustomSwarm_TTL;
        if (a.getRole().equals(WingRole.BOMBER)){
            //spec.DF_ttl = NanoThief_6.CustomSwarm_Bomber_TTL;
            spec.DF_recyclePerFighter *=NanoThief_6.CustomSwarm_RefundPercent_Bomber;
        }else {
            spec.DF_recyclePerFighter *= NanoThief_6.CustomSwarm_RefundPercent;
        }
        logStats(spec);
    }
    private static void logStats(Nano_Thief_Stats spec){
        log.info("got swarm of ID: "+spec.OF_fighterToBuild +" stats as: cost: "+spec.OF_swarmCost +", productionTime: "+spec.OF_productionTime +", time to live"+spec.OF_ttl +", and refund per fighter: "+spec.OF_recyclePerFighter);
    }

    @Override
    public void advance(float amount) {
        //note: NanoThief_ShipStats handles both the spawning and despawinging of swarm cores. This will require change.
        cooldown -= amount;
        if (cooldown > 0) return;
        //maxFighters = getMaxFighters();
        double cost = skills.getModifiedCost(skills.stats.DF_swarmCost);
        /*if (skills.getTotalReclaim() < cost && !waiting){// || currentFighters() >= maxFighters){
            //if (skills.stats.getReadSavedDP() <= 0) skills.stats.getDeployedPonits();
            cooldown = 1;//skills.stats.OF_productionTime;
            onCooldown = false;
            waiting = false;
            return;
        }*/
        /*if (!onCooldown){
            onCooldown = true;
            cooldown = recharge;
            return;
        }*/
        //create a combat swarm
        WaitingOnReclaim = false;
        if (skills.getTotalReclaim() < cost){
            waiting = true;
            cooldown = 1;
            WaitingOnReclaim = true;
            return;
        }
        if (currentFighters() >= interface7.getMaxFighters() || ship.isPhased()){
            waiting = true;
            cooldown = 1;
            return;
        }
        waiting = false;
        //onCooldown = true;
        cooldown = recharge;
        skills.useReclaim(cost);
        createCombatSwarmCore();
    }

    @Override
    public void displayStats() {
    }
    /*private int getMaxFighters(){
        return maxFighters;
    }*/
    private int currentFighters(){
        return interface7.currentFighters();// + skills.stats.getOffinciveFighterWings().size();
    }
    public ShipAPI createCombatSwarmCore(){//ShipAPI primary){
        //skills.stats.getDeployedPonits();
        ShipAPI primary = ship;//stats.getShip();
        String wingId = skills.stats.DF_fighterToBuild;//Settings.NANO_THIEF_BASEWING;//SwarmLauncherEffect.RECLAMATION_SWARM_WING;//"attack_swarm_wing"
        CombatEngineAPI engine = Global.getCombatEngine();
        //CombatFleetManagerAPI manager = engine.getFleetManager(FleetSide.ENEMY);//engine.getFleetManager(primary.getOwner());
        CombatFleetManagerAPI manager = engine.getFleetManager(primary.getOwner());
        manager.setSuppressDeploymentMessages(true);

        Vector2f loc = primary.getLocation();
        float facing = (float) Math.random() * 360f;
        //log.info("attempting to create a attack swarm at "+loc.x+", "+loc.y+" at ship of "+primary.getName()+" who's location is "+primary.getLocation().x+", "+primary.getLocation().y);
        ShipAPI fighter = null;
        //Global.getSettings().getVariant("");


        FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP,"Abyssal_XO_ReclaimCore_Blank_2");
        //Global.getFactory().createFleetMember
        ShipVariantAPI OVERWRITER = member.getVariant();//Global.getSettings().getVariant("Abyssal_XO_ReclaimCore_Blank").clone();
        OVERWRITER.setSource(VariantSource.REFIT);
        OVERWRITER.setWingId(0,skills.stats.DF_fighterToBuild);
        /*if (!Utils.isCurrectSiCVersion()) {
            if (primary.getFleetMember() != null && primary.getFleetMember().getFleetData() != null && primary.getFleetMember().getFleetData().getFleet() != null) {
                //SICSkillControllerBackup.fleet_global = primary.getFleetMember().getFleetData().getFleet();
                SICSkillControllerBackup.member_map.put(member, primary.getFleetMember().getFleetData().getFleet());
                OVERWRITER.addMod(Settings.SIC_CONTROL_HULLMOD);
            }
        }*/
        member.setOwner(primary.getOwner());
        member.setVariant(OVERWRITER,false,true);

        fighter = manager.spawnFleetMember(member,loc, facing, 0f);
        fighter.setShipAI(new Nano_Thief_AI_SawrmSpawner(fighter,primary,skills.stats.DF_fighterToBuild,skills.stats,false,this));
        manager.removeDeployed(fighter,false);

        manager.setSuppressDeploymentMessages(false);
        Vector2f takeoffVel = Misc.getUnitVectorAtDegreeAngle(facing);
        takeoffVel.scale(fighter.getMaxSpeed() * 1f);

        this.interface7.defenders.add(fighter);

        return fighter;//note: not a fighter, but instead something very diffrent.
    }
    @Override
    public double getMaxCost() {
        return skills.stats.DF_swarmCost;
    }
}
