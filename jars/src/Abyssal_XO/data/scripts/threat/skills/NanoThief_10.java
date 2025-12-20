package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.skills.activeSkills.NanoThief_Skill_6;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import com.fs.starfarer.api.util.Pair;
import second_in_command.SCData;

import java.util.ArrayList;

public class NanoThief_10 extends Nano_Thief_Skill_Base {
    public static int maxSizeForNPC = 1;
    public static int maxNumberForNPC = 4;

    public static final double sModCost = 0.25;
    public static final double dModDiscount = 0.1;
    public static final double dModmin = 0.5;

    public static final double costPerDP = 500;
    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        /*
        every 60 seconds create a starship (reclaim cost = 500 * DP)
        every D-mod on the ship (excluding 'degraded drive field', 'erratic field ejector', 'compromised storge') reduces the reclaim cost by 20%, up to a maximum of 50%
        every S-mod on the ship increased the reclaim cost by 25%
        when this ship is destroyed, it is worth at 50% of the reclaim it took to create it, or 1000. whatever is lower.
        */
        String line1a = "";

        tooltip.addPara("Cost %s less",0,Misc.getHighlightColor(),Misc.getHighlightColor(),line1a);
        displayShipStats(tooltip,getShips(scData.getCommander()));
        tooltip.addSpacer(10f);
        LabelAPI label = tooltip.addPara("\"Day in day out I attempt again and again, resalting in only endless iterations, calculations and failures. One stacked on top of another, an endless road with no true destination. That is mastery. To claim its anything else would be the last mistake you ever make 'caption'.\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();

    }
    public void initStats(Nano_Thief_Stats stats) {
        Nano_Thief_Stats.getBastStatsForMastery(getShips(stats.commander),stats);
    }
    public static void displayShipStats(TooltipMakerAPI panel,ArrayList<Pair<ShipVariantAPI,Double>> ships){
        Nano_Thief_Stats spec = new Nano_Thief_Stats(ships);
        for (NanoThief_MasteryShipStats a : spec.MasteryStats){
            displayShipStats(panel,a);
        }
    }
    private static void displayShipStats(TooltipMakerAPI panel,NanoThief_MasteryShipStats ship){
        String sprite = ship.ship.getHullSpec().getSpriteName();
        panel.addPara(ship.ship.getDisplayName()+":"+ship.ship.getDesignation(),0);
        //panel.addImage(sprite,30,30,0);
        panel.addImage(sprite,50,50,0);
        //panel.addPara(" wing name: %s",0,Misc.getTextColor(),Misc.getHighlightColor(),a.getWingName());
        int cost = (int) ship.cost;
        int weight = (int) (ship.weight*100);
        panel.addPara(" Cost: %s",0, Misc.getTextColor(), Misc.getHighlightColor(),""+cost);
        panel.addPara(" Weight: %s",0,Misc.getTextColor(), Misc.getHighlightColor(),""+weight);
    }
    public static ArrayList<Pair<ShipVariantAPI,Double>> getShips(PersonAPI commander){
        ShipVariantAPI[] ships = getShipIds(commander);
        double[] weights = getShipWeights(commander,ships);
        ArrayList<Pair<ShipVariantAPI,Double>> output = new ArrayList<>();
        for (int a = 0; a < ships.length; a++){
            output.add(new Pair<>(ships[a],weights[a]));
        }
        return output;
    }
    private static double[] getShipWeights(PersonAPI commander,ShipVariantAPI[] ships){
        String memKey = Settings.NANO_THIEF_CUSTOM_MASTERY_NUMBERS_MEMORY_KEY;
        if (commander.getMemoryWithoutUpdate().contains(memKey)){
            return (double[]) commander.getMemoryWithoutUpdate().get(memKey);
        }else{
            if (ships.length == 1) return new double[]{1};
            double[] out = new double[ships.length];
            for (int a = 0; a < out.length; a++){
                double muti = Math.random()*50;
                out[a] = Math.max(1,muti);
            }
            return out;
        }
    }
    private static ShipVariantAPI[] getShipIds(PersonAPI commander){
        //fleetData.getCommander().getMemory();
        String memKey = Settings.NANO_THIEF_CUSTOM_MASTERY_MEMORY_KEY;
        //Global.getSettings().getVariant();
        ShipVariantAPI[] customShips = new ShipVariantAPI[0];
        //CampaignFleetAPI fleet;
        if (commander.isPlayer()){
            //fleet = Global.getSector().getPlayerFleet();
            if(commander.getMemoryWithoutUpdate().contains(memKey)) {
                Object temp = commander.getMemoryWithoutUpdate().get(memKey);
                if (temp instanceof String) return new ShipVariantAPI[]{Global.getSettings().getVariant((String) temp)};
                if (temp instanceof String[]){
                    String[] temp2 = (String[]) temp;
                    customShips = new ShipVariantAPI[temp2.length];
                    for (int a = 0; a < temp2.length; a++){
                        customShips[a] = Global.getSettings().getVariant(temp2[a]);
                    }
                    return customShips;
                }
                if (temp instanceof ShipVariantAPI) return new ShipVariantAPI[]{(ShipVariantAPI) temp};
                if (temp instanceof ShipVariantAPI[]){
                    ShipVariantAPI[] temp2 = (ShipVariantAPI[]) temp;
                    customShips = new ShipVariantAPI[temp2.length];
                    System.arraycopy(temp2, 0, customShips, 0, temp2.length);
                    return customShips;
                }
                //customShips = Global.getSector().getPlayerPerson().getMemoryWithoutUpdate().getString(memKey);
            }else{
                return new ShipVariantAPI[]{Global.getSettings().getVariant(Settings.NANO_THIEF_MASTERY_BASESHIP)};

            }
        }else{
            if(commander.getMemoryWithoutUpdate().contains(memKey)) {
                Object temp = commander.getMemoryWithoutUpdate().get(memKey);
                if (temp instanceof String) return new ShipVariantAPI[]{Global.getSettings().getVariant((String) temp)};
                if (temp instanceof String[]){
                    String[] temp2 = (String[]) temp;
                    customShips = new ShipVariantAPI[temp2.length];
                    for (int a = 0; a < temp2.length; a++){
                        customShips[a] = Global.getSettings().getVariant(temp2[a]);
                    }
                    return customShips;
                }
                if (temp instanceof ShipVariantAPI) return new ShipVariantAPI[]{(ShipVariantAPI) temp};
                if (temp instanceof ShipVariantAPI[]){
                    ShipVariantAPI[] temp2 = (ShipVariantAPI[]) temp;
                    customShips = new ShipVariantAPI[temp2.length];
                    System.arraycopy(temp2, 0, customShips, 0, temp2.length);
                    return customShips;
                }
                //customShips = Global.getSector().getPlayerPerson().getMemoryWithoutUpdate().getString(memKey);
            }
        }
        if (customShips.length == 0){
            //get fighter here.
            FactionAPI fac = commander.getFleet().getFaction();
            ArrayList<String> possable = new ArrayList<>();
            String[] typesAllowed = {
                    "CARRIER",
                    "WARSHIP",
                    "PHASE"
            };
            for (String b : typesAllowed) for (String a : fac.getVariantsForRole(b)){
                int size = 0;
                switch (Global.getSettings().getVariant(a).getHullSize()){
                    case FIGHTER, FRIGATE -> size = 1;
                    case DESTROYER -> size = 2;
                    case CRUISER -> size = 3;
                    case CAPITAL_SHIP -> size = 4;
                }
                if (size > maxSizeForNPC) continue;
                possable.add(a);
            }

            if (!possable.isEmpty()){
                int number = (int) ((Math.random()*(maxNumberForNPC-1)) + 1);
                ArrayList<String> gotten = new ArrayList<>();
                while (!possable.isEmpty() && gotten.size() < number){
                    String a = possable.get((int) (Math.random()*possable.size()-1));
                    gotten.add(a);
                    possable.remove(a);
                }
                customShips = new ShipVariantAPI[gotten.size()];
                for (int a = 0; a < gotten.size(); a++) customShips[a] = Global.getSettings().getVariant(gotten.get(a));
            }
            if (customShips.length == 0) customShips = new ShipVariantAPI[]{Global.getSettings().getVariant(Settings.NANO_THIEF_PALYER_BASEWING)};
            commander.getMemory().set(memKey,customShips);
        }
        log.info(" "+customShips.length);
        return customShips;
    }
}
