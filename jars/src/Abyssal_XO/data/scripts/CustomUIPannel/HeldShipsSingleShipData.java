package Abyssal_XO.data.scripts.CustomUIPannel;

import com.fs.starfarer.api.fleet.FleetMemberAPI;

public class HeldShipsSingleShipData {
    public FleetMemberAPI ship;
    public int odds;
    public String name;
    public String variant;
    public HeldShipsSingleShipData(Mastery_HeldShip_Single single){
        ship = single.ship;
        odds = single.chance;
        name = single.shipNameForced.getText();//single.ship.getShipName();
        variant = single.ship.getVariant().getDisplayName();
    }
    public HeldShipsSingleShipData(FleetMemberAPI ship, int odds, String name, String variant){
        this.ship = ship;
        this.odds = odds;
        this.name = name;
        this.variant = variant;
    }
}
