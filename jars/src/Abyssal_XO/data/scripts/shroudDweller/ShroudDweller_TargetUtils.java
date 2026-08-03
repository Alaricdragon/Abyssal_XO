package Abyssal_XO.data.scripts.shroudDweller;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.DeployedFleetMemberAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.FleetMemberDeploymentListener;
import lombok.Getter;

import java.util.ArrayList;

public class ShroudDweller_TargetUtils implements FleetMemberDeploymentListener {
    /*todo: so why is this here, and how does it work?
        This is here because two things:
            1: true random targeting can cause the targets to be the same every fucking time
            2: getting every ship every time I want to target anything is not cost effective.
        How will this work?
            1: this will be a 'on deployed' listiner, getting whenever a none fighter ship is deployed.
            2: this will always target the 'first' ship in order.
            3: when targeted, the 'first' ship will have its 'priority' changed. Effectively moving down the map
                -note: I did some consideration, and well... it might be better just to move the 'used' ship to the back of the map?
                ...
                issues over issues.... I... fucking despise that arg....
                so like... no.
                This requires randomization. Place the old ship in a random position does not work. this does not work at all....
                ....
                I have to do a more complecated EQ. this could be costly, but hopefully not mush....
                -note: only targets that your fleet is aware of should be targeted.


    */
    @Getter
    private ArrayList<ShipAPI> friendlyShips =new ArrayList<>();
    private ArrayList<Float> friendlyWeight =new ArrayList<>();
    @Getter
    private ArrayList<ShipAPI> hostileShips =new ArrayList<>();
    private ArrayList<Float> hostileWeight =new ArrayList<>();

    private int owner;
    public ShroudDweller_TargetUtils(int owner){
        this.owner = owner;
    }
    public boolean hasFriendlyAlive(){
        return !friendlyShips.isEmpty();
    }
    public ShipAPI fineFriendlyTarget(){
        return getTarget(friendlyShips,friendlyWeight);
    }
    public ShipAPI findHostileTarget(){
        return getTarget(hostileShips,hostileWeight);
    }
    private boolean isValid(ShipAPI a){
        if (!Global.getCombatEngine().isAwareOf(owner,a)) return false;
        return true;
    }
    private boolean shouldBeRemoved(ShipAPI a){
        if (a.isHulk()) return true;
        if (!a.isAlive()) return true;
        return false;
    }
    private ShipAPI getTarget(ArrayList<ShipAPI> ships, ArrayList<Float> weights){
        float totalWeight = 0;
        for (int a = 0; a < ships.size(); a++){
            ShipAPI ship = ships.get(a);
            if (shouldBeRemoved(ship)){
                //this works because I am not adding things here, or comparing them. the 'continue' makes this effectively isolated.
                ships.remove(a);
                weights.remove(a);
                continue;
            }
            if (!isValid(ship)) continue;
            totalWeight+=weights.get(a);
        }
        totalWeight = (float) (Math.random()*totalWeight);
        for (int a = 0; a < ships.size(); a++){
            ShipAPI ship = ships.get(a);
            if (!isValid(ship)) continue;
            totalWeight-=weights.get(a);
            if (totalWeight <= 0){
                return ship;
            }
        }
        return ships.isEmpty() ? null : ships.get(ships.size()-1);
    }

    @Override
    public void reportFleetMemberDeployed(DeployedFleetMemberAPI member) {
        /*todo:
            this is kinda improtant.
            how it will work:
            1: all ships deployed from this XO will get a tag. this tag is used to determin valid friendly targets (based on SCData).
            basically, the tag is going to be named after something in the SCData and applied on spawn.
                -notice: this is going to have to be applyed shortly after spawn do to the time it takes flags to activate.
            2: hostile ships will just be hostile ships.
            they will all be added to arrays here.
            in cases were no friendly ships exssit, targeting utils will

         */
    }
}
