package Abyssal_XO.data.scripts.CustomUIPannel;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BaseCustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.input.InputEventAPI;
import com.fs.starfarer.api.ui.*;
import com.fs.starfarer.api.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class HeldShipsHolder implements CustomUIPanelPlugin {
    public HeldShipsHolder(){
        //createOptions(panel, dialog, tooltip);
    }
    protected ArrayList<Mastery_HeldShip_Single> heldShips = new ArrayList<>();
    protected ArrayList<Mastery_HeldShip_Single> toRemove = new ArrayList<>();
    protected Pair<FleetMemberAPI,Integer> toAdd = null;

    protected CustomPanelAPI root;
    UIComponentAPI panelThing;
    protected CustomPanelAPI panel;
    protected TooltipMakerAPI tooltip;
    public void createOptions(CustomPanelAPI panel,float width, float height){
        this.root = panel;
        panel = root.createCustomPanel(width,height,new BaseCustomUIPanelPlugin());
        panelThing = panel;
        root.addComponent(panel).setLocation(0,0);

        createOptionsAfterRoot(panel,width,height);
    }
    private void createOptionsAfterRoot(CustomPanelAPI panel,float width, float height){
        //this.root = panel;
        //panel = root.createCustomPanel(width,height,new BaseCustomUIPanelPlugin());
        //panelThing = panel;
        //root.addComponent(panel).setLocation(0,0);
        TooltipMakerAPI tooltip = panel.createUIElement(width,height,true);
        tooltip.getPosition().setLocation(0,0);
        //tooltip.addPara("putting the ship here lol",5);
        //ShipVariantAPI ship = Global.getSettings().getVariant("legion_Escort");
        //Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy().get(0);
        ArrayList<Pair<FleetMemberAPI, Integer>> list = getMainShipList();
        int totalOdds = 0;
        for (Pair<FleetMemberAPI, Integer> a : list) totalOdds+=a.two;
        heldShips = new ArrayList<>();
        for (Pair<FleetMemberAPI, Integer> a : list) heldShips.add(Mastery_HeldShip_Single.createItem(panel,tooltip,a.one,0,100,10,a.two,totalOdds));

        this.panel = panel;
        this.tooltip = tooltip;
        //Mastery_HeldShip_Single.createItem(panel,tooltip,Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy().get(0),0,100,10,1,1);
        panel.addUIElement(tooltip);
    }
    private void addShips(){
    }
    public void recreate_full(){
        root.removeComponent(panel);

        panel = root.createCustomPanel(root.getPosition().getWidth(),root.getPosition().getHeight(),new BaseCustomUIPanelPlugin());
        panelThing = panel;
        root.addComponent(panel).setLocation(0,0);

        createOptionsAfterRoot(panel,root.getPosition().getWidth(),root.getPosition().getHeight());
    }
    public void recreate(){
        recreate(panel,tooltip);
    }
    private ArrayList<Pair<FleetMemberAPI,Integer>> getMainShipList(){
        ArrayList<Pair<FleetMemberAPI,Integer>> output = new ArrayList<>();
        if (heldShips.isEmpty()){
            Pair<FleetMemberAPI,Integer> data = new Pair<>();
            data.one = Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy().get(0);
            data.two = 10;
            output.add(data);
            return output;
        }
        for (Mastery_HeldShip_Single a : heldShips){
            if (toRemove.contains(a)) continue;
            Pair<FleetMemberAPI,Integer> data = new Pair<>();
            data.one = a.ship;
            data.two = a.chance;
            output.add(data);
        }
        if (toAdd != null){
            output.add(toAdd);
        }

        return output;
    }
    public void recreate(CustomPanelAPI panel,TooltipMakerAPI tooltip){
        for (Mastery_HeldShip_Single a : toRemove){
            heldShips.remove(a);
            //a.clear();
            //tooltip.removeComponent(a.thisCompoment);
        }
        int totalOdds = 0;
        for (Mastery_HeldShip_Single a : heldShips) totalOdds+=a.getOdds();
        if (toAdd != null) totalOdds+=toAdd.two;
        //heldShips.addAll(toAdd);

        for (Mastery_HeldShip_Single a : heldShips) /*/a.recreate(totalOdds);/*/a.recreate(a.getOdds(),totalOdds);/**/
        if (toAdd != null) heldShips.add(Mastery_HeldShip_Single.createItem(panel,tooltip,toAdd.one,0,100,10,toAdd.two,totalOdds));
        toAdd = null;
        toRemove = new ArrayList<>();
    }
    @Override
    public void positionChanged(PositionAPI position) {

    }

    @Override
    public void renderBelow(float alphaMult) {

    }

    @Override
    public void render(float alphaMult) {

    }

    @Override
    public void advance(float amount) {

    }

    @Override
    public void processInput(List<InputEventAPI> events) {

    }

    @Override
    public void buttonPressed(Object buttonId) {
        MasteryHolder.log.info("button pressed in: HeldShipsHolder");
    }
}
