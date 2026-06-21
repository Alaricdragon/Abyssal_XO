package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_ShipSkills;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_SkillBase;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_Skill_10;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CharacterDataAPI;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.ids.ShipRoles;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

import java.util.ArrayList;

public class NanoThief_10 extends Nano_Thief_Skill_Base {

    public static int maxShips = 4;
    public static boolean canFrigate = true;
    public static boolean canDestroyer = false;
    public static boolean canCruiser = false;
    public static boolean canCapital = false;
    public static boolean allowNPCFabricators = false;

    public static boolean[] allowedSizesForNPC = {canFrigate,canDestroyer, canCruiser, canCapital};
    //public static boolean[] allowedSizesForNPC = {true,false,false,false};
    public static int maxNumberForNPC = maxShips;
    private static int maxOddsNPC = 20;
    private static int minOddsNPC = 1;

    public static final double sModCost = 0.25;//per s-mod, add a
    public static final double dModDiscount = 0.1;
    public static final double dModmin = 0.5;
    public static final double costPerDP = 200;//200 for 1 dp cost, 2000 for 10 dp. (0.5 capitals for 1 dp. 0.5 frigets for 1 dp.)

    public static final double rechargeTimePerDP = 10;//10 seconds per dp cost of ship.
    public static final double buildTimePerDP = 2.5;//2.5 seconds per dp cost of ship.

    public static final double minCR = 0.4;//for spawning ships
    public static final double peakCRDuration = 0.6;

    //public static final double forceRechargePerDP = rechargeTimePerDP;//this is for recharging the ship form a new simulacrum fighter.
    public static final double maxReclaimPercent = 0.75;//max amount of reclaim a simulacrum ship is worth when destroyed.
    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        /*
        every 60 seconds create a starship (reclaim cost = 500 * DP)
        every D-mod on the ship (excluding 'degraded drive field', 'erratic field ejector', 'compromised storge') reduces the reclaim cost by 20%, up to a maximum of 50%
        every S-mod on the ship increased the reclaim cost by 25%
        when this ship is destroyed, it is worth at 50% of the reclaim it took to create it, or 1000. whatever is lower.
        */
        /*
        when reclaim is available and this ability is off cooldown, select a random ship from the selected list.
        a nanobot swarm will be deployed to construct this ship in a empty area nearby.
        this process takes %s seconds per deployment point.
        this has a cooldown of %s per deployment point.

        the reclaim cost is %s per deployment points, reduced by %s per none logistical dmod (up to a reduction of %s),and increased by %s per smod (with no upper limit)
        the smod and dmod cost changes are not multiplicative.
        any ship in your fleet can be chosen as a possible ship to build, up to a maximum of %s ships.
        */
        String line4_0 = ""+(int)rechargeTimePerDP;
        String line7_0 = ""+maxShips;
        String line8_0 = (int)(minCR*100)+"%";
        tooltip.addPara("When reclaim is available and this ability is ready, select a random ship from a list of possible ships",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        tooltip.addPara("A nanobot swarm will be deployed to construct this ship as a simulacrum ship in a empty area nearby",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        tooltip.addPara("The nanobot swarm will take %s seconds to prepare per deployment point of the simulacrum ship",0,Misc.getHighlightColor(),Misc.getHighlightColor(),line4_0);
        tooltip.addPara("The simulacrum ships this skill can build can be selected, up to a maximum of %s ships.",0,Misc.getHighlightColor(),Misc.getHighlightColor(),line7_0);
        tooltip.addPara("ships with less then %s cr cannot use this ability",0,Misc.getHighlightColor(),Misc.getHighlightColor(),line8_0);
        tooltip.addPara("available simulacrum ships in this fleet:",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        displayShipStats(tooltip,getShips(scData.getCommander(),scData.getFleet().getFaction()),true);
        tooltip.addPara("",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        this.addNewAbilityText(scData, tooltip);

        tooltip.addSpacer(10f);
        LabelAPI label = tooltip.addPara("\"Day in day out I attempt again and again, resalting in only endless iterations, calculations and failures. One stacked on top of another, an endless road with no true destination. That is mastery. To claim its anything else would be the last mistake you ever make 'caption'.\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        tooltip.addPara("",0,Misc.getHighlightColor(),Misc.getHighlightColor());
        this.displayBaseStats(scData, tooltip);
        label.italicize();

    }
    private void displayBaseStats(SCData scData, TooltipMakerAPI tooltip){
        //String line3_0 = ""+(int)forceRechargePerDP;

        String line4_0 = ""+(int)costPerDP;
        String line4_1 = (int)(dModDiscount*100)+"%";
        String line4_2 = (int)(dModmin*100)+"%";
        String line4_3 = (int)(sModCost*100)+"%";

        String line5_0 = ""+buildTimePerDP;
        String line5_1 = "can be damaged";
        String line5_2 = "move";
        String line5_3 = "attack";
        String line5_4 = "otherize defend itself";

        String line6_0 = 100-(int)(peakCRDuration*100)+"%";

        String line7_0 = (int)(maxReclaimPercent*100)+"%";
        tooltip.addPara("Simulacrum ships act as normal ships with the following modifications:",0,Misc.getGrayColor(),Misc.getHighlightColor());
        tooltip.addPara("   -cannot be built if you don't have enough spare deployment points to deploy the ship normally",0,Misc.getGrayColor(),Misc.getHighlightColor());
        tooltip.addPara("   -must be built form a 'construction swarm'",0,Misc.getGrayColor(),Misc.getHighlightColor());
        tooltip.addPara("   -starting cr is equal to the cr of the ship that spawned the simulacrum ship",0,Misc.getGrayColor(),Misc.getHighlightColor());
        //tooltip.addPara("   -cannot be used to create additional simulacrum ships until %s seconds pass per deployment point",0,Misc.getGrayColor(),Misc.getHighlightColor(),line3_0);
        tooltip.addPara("   -cost %s reclaim per deployment point, reduced by %s per none logistical d-mod (up to a reduction of %s),and increased by %s per s-mod (with no upper limit). not multiplicative",0,Misc.getGrayColor(),Misc.getNegativeHighlightColor(),line4_0,line4_1,line4_2,line4_3);
        tooltip.addPara("   -takes %s seconds to build per deployment point. during this time, the ship %s, cannot %s, %s or %s",0,Misc.getGrayColor(),Misc.getNegativeHighlightColor(),line5_0,line5_1,line5_2,line5_3,line5_4);
        tooltip.addPara("   -peak performance time is reduced by %s",0,Misc.getGrayColor(),Misc.getNegativeHighlightColor(),line6_0);
        tooltip.addPara("   -reclaim gained from the ship is caped to %s of the reclaim it cost to build",0,Misc.getGrayColor(),Misc.getNegativeHighlightColor(),line7_0);
        tooltip.addPara("   -after combat the ships fall apart, being reduced to nothing",0,Misc.getNegativeHighlightColor(),Misc.getNegativeHighlightColor());
        //tooltip.addPara("   -The s-mod and d-mod cost changes are not multiplicative with each other.",0,Misc.getNegativeHighlightColor(),Misc.getNegativeHighlightColor());

    }
    public void addNewAbilityText(SCData scData, TooltipMakerAPI tooltip){
        String line9a = Settings.NANO_THIEF_ABILITY_NAME;
        tooltip.addPara("gain the %s ability, that allows you to change your Simulacrum Ships",0,Misc.getHighlightColor(),Misc.getHighlightColor(),line9a);
    }
    public void initStats(Nano_Thief_Stats stats) {
        stats.masteryShips = getShips(stats.commander,stats.faction);
    }
    public static void displayShipStats(TooltipMakerAPI panel,NanoThief_MasteryShipStats[] ships,boolean adjustSize){
        //Nano_Thief_Stats spec = new Nano_Thief_Stats(ships);
        int size = 50;
        //if (adjustSize) size = size / ships.length;
        for (NanoThief_MasteryShipStats a : ships){//spec.masteryShips){
            displayShipStats(panel,a,size);
        }
    }
    private static void displayShipStats(TooltipMakerAPI panel,NanoThief_MasteryShipStats ship,int size){
        String sprite = ship.ship.getHullSpec().getSpriteName();
        //panel.addPara(,12);
        //panel.addImage(sprite,30,30,0);
        panel.addImage(sprite,size,size,0);
        //panel.addPara(" wing name: %s",0,Misc.getTextColor(),Misc.getHighlightColor(),a.getWingName());
        int cost = (int) ship.cost;
        int weight = (int) (ship.weight);
        int builtTime = ((int)(ship.buildTime*10))/10;
        int rechargeTime = ((int)(ship.reloadTime*10))/10;
        panel.addPara("Name: %s Cost: %s, Weight %s, BuildTime %s, RechargeTime: %s",0, Misc.getTextColor(), Misc.getHighlightColor(),ship.name,""+cost,""+weight,""+builtTime,""+rechargeTime);
        //panel.addPara(" Weight: %s",0,Misc.getTextColor(), Misc.getHighlightColor(),""+weight);
        //panel.addPara(" BuildTime: %s",0,Misc.getTextColor(), Misc.getHighlightColor(),""+builtTime);
        //panel.addPara(" RechargeTime: %s",0,Misc.getTextColor(), Misc.getHighlightColor(),""+rechargeTime);
    }
    public static void displayShipStatsStright(TooltipMakerAPI panel,NanoThief_MasteryShipStats ship,int size){
        String sprite = ship.ship.getHullSpec().getSpriteName();
        panel.addPara(ship.name,12);
        //panel.addImage(sprite,30,30,0);
        panel.addImage(sprite,size,size,0);
        //panel.addPara(" wing name: %s",0,Misc.getTextColor(),Misc.getHighlightColor(),a.getWingName());
        int cost = (int) ship.cost;
        int baseCost = ship.getBaseCost();
        int smod = ship.getSModCost();
        int dmod = ship.getDModReduction();
        int weight = (int) (ship.weight);
        int builtTime = ((int)(ship.buildTime*10))/10;
        int rechargeTime = ((int)(ship.reloadTime*10))/10;
        panel.addPara(" Cost: %s (base: %s)",0, Misc.getTextColor(), Misc.getHighlightColor(),""+cost,""+baseCost);
        if (smod != 0)panel.addPara(" (+ %s from s-mods)",0, Misc.getTextColor(), Misc.getNegativeHighlightColor(),""+smod);
        if (dmod != 0)panel.addPara(" (- %s from d-mods)",0, Misc.getTextColor(), Misc.getPositiveHighlightColor(),""+dmod);
        panel.addPara(" Weight: %s",0,Misc.getTextColor(), Misc.getHighlightColor(),""+weight);
        panel.addPara(" BuildTime: %s",0,Misc.getTextColor(), Misc.getHighlightColor(),""+builtTime);
        panel.addPara(" RechargeTime: %s",0,Misc.getTextColor(), Misc.getHighlightColor(),""+rechargeTime);
    }
    private static NanoThief_MasteryShipStats[] getShips(PersonAPI commander,FactionAPI faction) {
        //fleetData.getCommander().getMemory();
        String memKey = Settings.NANO_THIEF_CUSTOM_MASTERY_MEMORY_KEY;
        //Global.getSettings().getVariant();
        //CampaignFleetAPI fleet;
        if (commander.isPlayer()) {
            //fleet = Global.getSector().getPlayerFleet();
            if (commander.getMemoryWithoutUpdate().contains(memKey)) {
                Object temp = commander.getMemoryWithoutUpdate().get(memKey);
                ArrayList<Object> temp2 = (ArrayList<Object>) temp;
                if (temp2.isEmpty()) {
                    FleetMemberAPI memberCopy = Global.getSettings().createFleetMember(FleetMemberType.SHIP, Global.getSettings().getVariant(Settings.NANO_THIEF_MASTERY_BASESHIP));
                    return new NanoThief_MasteryShipStats[]{new NanoThief_MasteryShipStats(memberCopy, 10, "raider")};
                }
                if (temp2.get(0) instanceof FleetMemberAPI) {
                    ArrayList<String> names = new ArrayList<>();
                    ArrayList<Integer> odds = new ArrayList<>();
                    if (commander.getMemoryWithoutUpdate().contains(Settings.NANO_THIEF_CUSTOM_MASTERY_NAMES_MEMORY_KEY)) {
                        ArrayList<String> temp3 = (ArrayList<String>) commander.getMemoryWithoutUpdate().get(Settings.NANO_THIEF_CUSTOM_MASTERY_NAMES_MEMORY_KEY);
                        for (String a : temp3) names.add(a);
                    } else {
                        for (int a = 0; a < temp2.size(); a++) names.add("");
                    }
                    if (commander.getMemoryWithoutUpdate().contains(Settings.NANO_THIEF_CUSTOM_MASTERY_NUMBERS_MEMORY_KEY)) {
                        ArrayList<Integer> temp3 = (ArrayList<Integer>) commander.getMemoryWithoutUpdate().get(Settings.NANO_THIEF_CUSTOM_MASTERY_NUMBERS_MEMORY_KEY);
                        for (int a : temp3) odds.add(a);
                    } else {
                        for (int a = 0; a < temp2.size(); a++) odds.add(1);
                    }
                    NanoThief_MasteryShipStats[] out = new NanoThief_MasteryShipStats[temp2.size()];
                    for (int a = 0; a < temp2.size(); a++) {
                        out[a] = new NanoThief_MasteryShipStats((FleetMemberAPI) temp2.get(a), odds.get(a), names.get(a));
                    }
                    return out;
                }
                if (temp2.get(0) instanceof String) {
                    ArrayList<String> names = new ArrayList<>();
                    ArrayList<Integer> odds = new ArrayList<>();
                    if (commander.getMemoryWithoutUpdate().contains(Settings.NANO_THIEF_CUSTOM_MASTERY_NAMES_MEMORY_KEY)) {
                        ArrayList<String> temp3 = (ArrayList<String>) commander.getMemoryWithoutUpdate().get(Settings.NANO_THIEF_CUSTOM_MASTERY_NAMES_MEMORY_KEY);
                        for (String a : temp3) names.add(a);
                    } else {
                        for (int a = 0; a < temp2.size(); a++) names.add("");
                    }
                    if (commander.getMemoryWithoutUpdate().contains(Settings.NANO_THIEF_CUSTOM_MASTERY_NUMBERS_MEMORY_KEY)) {
                        ArrayList<Integer> temp3 = (ArrayList<Integer>) commander.getMemoryWithoutUpdate().get(Settings.NANO_THIEF_CUSTOM_MASTERY_NUMBERS_MEMORY_KEY);
                        for (int a : temp3) odds.add(a);
                    } else {
                        for (int a = 0; a < temp2.size(); a++) odds.add(1);
                    }
                    NanoThief_MasteryShipStats[] out = new NanoThief_MasteryShipStats[temp2.size()];
                    for (int a = 0; a < temp2.size(); a++) {
                        FleetMemberAPI memberCopy = Global.getSettings().createFleetMember(FleetMemberType.SHIP, Global.getSettings().getVariant((String) temp2.get(a)));
                        out[a] = new NanoThief_MasteryShipStats(memberCopy, odds.get(a), names.get(a));
                    }
                    return out;
                }
                FleetMemberAPI memberCopy = Global.getSettings().createFleetMember(FleetMemberType.SHIP, Global.getSettings().getVariant(Settings.NANO_THIEF_MASTERY_BASESHIP));
                return new NanoThief_MasteryShipStats[]{new NanoThief_MasteryShipStats(memberCopy, 10, "raider")};
            } else {
                FleetMemberAPI memberCopy = Global.getSettings().createFleetMember(FleetMemberType.SHIP, Global.getSettings().getVariant(Settings.NANO_THIEF_MASTERY_BASESHIP));
                return new NanoThief_MasteryShipStats[]{new NanoThief_MasteryShipStats(memberCopy, 10, "raider")};
            }
        } else {
            if (commander.getMemoryWithoutUpdate().contains(memKey)) {
                Object temp = commander.getMemoryWithoutUpdate().get(memKey);
                ArrayList<Object> temp2 = (ArrayList<Object>) temp;
                if (temp2.isEmpty()) {
                    FleetMemberAPI memberCopy = Global.getSettings().createFleetMember(FleetMemberType.SHIP, Global.getSettings().getVariant(Settings.NANO_THIEF_MASTERY_BASESHIP));
                    return new NanoThief_MasteryShipStats[]{new NanoThief_MasteryShipStats(memberCopy, 10, "")};
                }
                if (temp2.get(0) instanceof FleetMemberAPI) {
                    ArrayList<String> names = new ArrayList<>();
                    ArrayList<Integer> odds = new ArrayList<>();
                    if (commander.getMemoryWithoutUpdate().contains(Settings.NANO_THIEF_CUSTOM_MASTERY_NAMES_MEMORY_KEY)) {
                        ArrayList<String> temp3 = (ArrayList<String>) commander.getMemoryWithoutUpdate().get(Settings.NANO_THIEF_CUSTOM_MASTERY_NAMES_MEMORY_KEY);
                        for (String a : temp3) names.add(a);
                    } else {
                        for (int a = 0; a < temp2.size(); a++) names.add(Global.getSettings().getVariant((String) temp2.get(a)).getDisplayName());
                    }
                    if (commander.getMemoryWithoutUpdate().contains(Settings.NANO_THIEF_CUSTOM_MASTERY_NUMBERS_MEMORY_KEY)) {
                        ArrayList<Integer> temp3 = (ArrayList<Integer>) commander.getMemoryWithoutUpdate().get(Settings.NANO_THIEF_CUSTOM_MASTERY_NUMBERS_MEMORY_KEY);
                        for (int a : temp3) odds.add(a);
                    } else {
                        for (int a = 0; a < temp2.size(); a++) odds.add(1);
                    }
                    NanoThief_MasteryShipStats[] out = new NanoThief_MasteryShipStats[temp2.size()];
                    for (int a = 0; a < temp2.size(); a++) {
                        out[a] = new NanoThief_MasteryShipStats((FleetMemberAPI) temp2.get(a), odds.get(a), names.get(a));
                    }
                    return out;
                }
                if (temp2.get(0) instanceof String) {
                    ArrayList<String> names = new ArrayList<>();
                    ArrayList<Integer> odds = new ArrayList<>();
                    if (commander.getMemoryWithoutUpdate().contains(Settings.NANO_THIEF_CUSTOM_MASTERY_NAMES_MEMORY_KEY)) {
                        ArrayList<String> temp3 = (ArrayList<String>) commander.getMemoryWithoutUpdate().get(Settings.NANO_THIEF_CUSTOM_MASTERY_NAMES_MEMORY_KEY);
                        for (String a : temp3) names.add(a);
                    } else {
                        for (int a = 0; a < temp2.size(); a++) names.add(Global.getSettings().getVariant((String) temp2.get(a)).getDisplayName());
                    }
                    if (commander.getMemoryWithoutUpdate().contains(Settings.NANO_THIEF_CUSTOM_MASTERY_NUMBERS_MEMORY_KEY)) {
                        ArrayList<Integer> temp3 = (ArrayList<Integer>) commander.getMemoryWithoutUpdate().get(Settings.NANO_THIEF_CUSTOM_MASTERY_NUMBERS_MEMORY_KEY);
                        for (int a : temp3) odds.add(a);
                    } else {
                        for (int a = 0; a < temp2.size(); a++) odds.add(1);
                    }
                    NanoThief_MasteryShipStats[] out = new NanoThief_MasteryShipStats[temp2.size()];
                    for (int a = 0; a < temp2.size(); a++) {
                        FleetMemberAPI memberCopy = Global.getSettings().createFleetMember(FleetMemberType.SHIP, Global.getSettings().getVariant((String) temp2.get(a)));
                        out[a] = new NanoThief_MasteryShipStats(memberCopy, odds.get(a), names.get(a));
                    }
                    return out;
                }
                FleetMemberAPI memberCopy = Global.getSettings().createFleetMember(FleetMemberType.SHIP, Global.getSettings().getVariant(Settings.NANO_THIEF_MASTERY_BASESHIP));
                return new NanoThief_MasteryShipStats[]{new NanoThief_MasteryShipStats(memberCopy, 10, "raider")};
            }
            ArrayList<String> ships = getRandomShipsForNPC(faction);
            //ArrayList<String> names;
            ArrayList<Integer> odds = new ArrayList<>();
            NanoThief_MasteryShipStats[] out = new NanoThief_MasteryShipStats[ships.size()];
            for (int a = 0; a < ships.size(); a++){
                odds.add((int)((Math.random() * (maxOddsNPC-minOddsNPC))+minOddsNPC));
                FleetMemberAPI memberCopy = Global.getSettings().createFleetMember(FleetMemberType.SHIP, Global.getSettings().getVariant(ships.get(a)));
                out[a] = new NanoThief_MasteryShipStats(memberCopy,odds.get(a),Global.getSettings().getVariant(ships.get(a)).getDisplayName());
            }
            commander.getMemory().set(memKey,ships);
            commander.getMemory().set(Settings.NANO_THIEF_CUSTOM_MASTERY_NUMBERS_MEMORY_KEY,odds);
            return out;
        }
    }
    private static ArrayList<String> getRandomShipsForNPC(FactionAPI fac){
        //fac = Global.getSector().getFaction("tritachyon");
        ArrayList<String> possable = new ArrayList<>();
        //public static final String HULLTYPE_CARRIER = "CARRIER", HULLTYPE_WARSHIP = "WARSHIP", HULLTYPE_PHASE = "PHASE", HULLTYPE_COMBATCIV = "COMBATCIV", HULLTYPE_TANKER = "TANKER", HULLTYPE_CARGO = "CARGO", HULLTYPE_PERSONNEL = "PERSONNEL", HULLTYPE_LINER = "LINER", HULLTYPE_TUG = "TUG", HULLTYPE_UTILITY = "UTILITY";
        String[] typesAllowed = {
                ShipRoles.CARRIER_SMALL,
                ShipRoles.CARRIER_MEDIUM,
                ShipRoles.CARRIER_LARGE,

                ShipRoles.COMBAT_SMALL_FOR_SMALL_FLEET,
                ShipRoles.COMBAT_SMALL,
                ShipRoles.COMBAT_MEDIUM,
                ShipRoles.COMBAT_LARGE,
                ShipRoles.COMBAT_CAPITAL,

                ShipRoles.PHASE_SMALL,
                ShipRoles.PHASE_MEDIUM,
                ShipRoles.PHASE_LARGE,
                ShipRoles.PHASE_CAPITAL
        };
        /*if (fac.getId().equals("threat")){
            typesAllowed = new String[]{
                    "CARRIER",
                    "WARSHIP",
                    "PHASE",
                    "threatHive",
                    "threatFabricator",
                    "threatOverseer"
            };
        }*/

        String temp5 = "possable ships inside of faction....";
        for (String e : fac.getKnownShips()){
            temp5+=", "+e;
        }
        Settings.log.info(temp5);

        for (String b : typesAllowed) {

            Settings.log.info("attempting to get allowed variants of type"+b+". number of entry's is: "+Global.getSettings().getEntriesForRole(fac.getId(),b).size());
            Settings.log.info("attempting to get allowed variants of type"+b+". (from secondary function) numer of entry's is: "+fac.getVariantsForRole(b).size());
            Settings.log.info("default entry's for role of type"+b+". number of entry's is: "+Global.getSettings().getDefaultEntriesForRole(b).size());


            //Global.getSettings().getEntriesForRole();
            for (String a : fac.getVariantsForRole(b)){//fac.getVariantsForRole(b)) {
                //String a = d.getVariantId();
                int size = switch (Global.getSettings().getVariant(a).getHullSize()) {
                    case FIGHTER, FRIGATE -> size = 0;
                    case DESTROYER -> size = 1;
                    case CRUISER -> size = 2;
                    case CAPITAL_SHIP -> size = 3;
                    default -> 0;
                };
                Settings.log.info("attempting to get a ship of var id: " + a);
                if (!allowedSizesForNPC[size]) continue;
                possable.add(a);
            }
        }
        String out2 = "got possable ships as:";
        for (String a : possable) out2 += ", "+a;
        Settings.log.info(out2);

        if (!possable.isEmpty()) {
            int number = (int) ((Math.random() * (maxNumberForNPC - 1)) + 1);
            ArrayList<String> gotten = new ArrayList<>();
            while (!possable.isEmpty() && gotten.size() < number) {
                String a = possable.get((int) (Math.random() * possable.size() - 1));
                gotten.add(a);
                possable.remove(a);
            }

            out2 = "got goten ships as:";
            for (String a : possable) out2 += ", "+a;
            Settings.log.info(out2);

            if (!gotten.isEmpty()) return gotten;
        }
        ArrayList<String> out = new ArrayList<>();
        out.add(Settings.NANO_THIEF_MASTERY_BASESHIP);
        Settings.log.info("failed to get any ships. returning base ship.... (faction used was: )"+fac.getId());
        return out;
    }

    @Override
    public void onActivation(SCData data) {
        if (data.getCommander().equals(Global.getSector().getPlayerPerson())){
            CharacterDataAPI character = Global.getSector().getCharacterData();
            if (character.getAbilities().contains(Settings.NANO_THIEF_ABILITY)) return;
            character.addAbility(Settings.NANO_THIEF_ABILITY);
        }
    }

    @Override
    public NanoThief_SkillBase createListiner(NanoThief_ShipSkills skills, ShipAPI ship) {
        return new NanoThief_Skill_10(skills, ship);
    }
}
