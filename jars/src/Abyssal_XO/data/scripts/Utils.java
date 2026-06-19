package Abyssal_XO.data.scripts;

import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.campaign.FleetDataAPI;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;
import second_in_command.SCUtils;
import second_in_command.specs.SCOfficer;

import java.util.HashMap;

public class Utils {

    public static float getDistance(float x, float y, Vector2f loc2){
        return Misc.getDistance(new Vector2f(x,y),loc2);
    }
    public static double getExpenseValue(double value,double maxValue,double increments,double speedPerValue){
        //double cr2 = (cr*100);
        //System.out.println("    cr: "+cr2);
        double a = ((maxValue+increments)-value) /increments;//so max cr is 1, -10 is 2, -20 is 4, -30 is 8.
        //System.out.println("    a: "+a);
        return speedPerValue *(Math.pow(2,a)/2);
    }
    @Deprecated
    public static void applySICSkillsToShip(ShipAPI ship, CampaignFleetAPI fleet){
        //member.variant.addPermaMod("sc_skill_controller")
        //data here is a just in case bit of data.
        for (SCOfficer b : SCUtils.getFleetData(fleet).getActiveOfficers()){
            //b.getSkillPlugins().get(0).
        }
    }
    @Deprecated
    private static HashMap<FleetMemberAPI, FleetDataAPI> hiddingShips = new HashMap<>();
    @Deprecated
    public static void applyShipIntoFleet(FleetDataAPI fleet, FleetMemberAPI ship){
        ship.setFleetCommanderForStats(fleet.getCommander(),fleet);
        fleet.addFleetMember(ship);
        hiddingShips.put(ship,fleet);
    }
    @Deprecated
    public static void removeAllShipsIntoFleets(){
        for (FleetMemberAPI a : hiddingShips.keySet()){
            hiddingShips.get(a).removeFleetMember(a);
        }
        hiddingShips = new HashMap<>();
    }
    /*public static float getDistance(Vector2f loc,Vector2f loc2){
        //float angle = VectorUtils.getAngle(loc, loc2);
        float x2 = loc.x - loc2.x;
        x2 = Math.max(x2, -x2);
        float y2 = loc.y - loc2.y;
        y2 = Math.max(y2, -y2);
        float d = x2+y2;
        return d;
    }*/
}
