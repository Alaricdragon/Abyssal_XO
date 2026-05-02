package Abyssal_XO.data.scripts.CustomUIPannel;

import Abyssal_XO.data.scripts.Settings;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BaseCustomUIPanelPlugin;
import com.fs.starfarer.api.campaign.CustomUIPanelPlugin;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
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

    public int getSelectedNumber(){
        return heldShips.size();
    }

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


        if (MasteryHolder.masteryHolder.finalButtons != null) {
            if (heldShips.size() > Settings.NANO_THIEF_MASTERY_maxShips || heldShips.isEmpty()) {
                MasteryHolder.masteryHolder.finalButtons.finish.setEnabled(false);
                MasteryHolder.masteryHolder.finalButtons.finish.flash();
                //MasteryHolder.masteryHolder.finalButtons.finish.setText("To many ships to use. Max is "+Settings.MASTERY_maxShips);
            }else {
                MasteryHolder.masteryHolder.finalButtons.finish.setEnabled(true);
                //MasteryHolder.masteryHolder.finalButtons.finish.setText("Finish");
            }
        }
        UIComponentAPI last_a = null;//pTemp;
        UIComponentAPI last_b;
        UIComponentAPI last_c = null;
        int size = (int) (width / 100);
        int at = 0;
        for (Mastery_HeldShip_Single a : heldShips){
            //FleetMemberAPI ship = fleetList.get(a);
            //last_b = addSingleShip_asCompoment(ship,a);
            last_b = a.thisCompoment;
            if (at % size == 0){
                if (last_c != null) last_b.getPosition().belowMid(last_c,1);
                last_c = last_b;
            }else if (last_a != null) last_b.getPosition().rightOfMid(last_a,1);
            last_a = last_b;
            at++;
        }
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
    private boolean firstCreation = true;
    private ArrayList<Pair<FleetMemberAPI,Integer>> getMainShipList(){
        ArrayList<Pair<FleetMemberAPI,Integer>> output = new ArrayList<>();
        /*if (heldShips.isEmpty()){
            output.add(getBackupShip());
            MasteryHolder.log.info("returning a new with only the players flagship available.");
            return output;
        }*/
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
        toAdd = null;
        toRemove = new ArrayList<>();
        MasteryHolder.log.info("after reorganizing the main ship list, we have:");
        for (Pair<FleetMemberAPI, Integer> a : output){
            MasteryHolder.log.info("    ship of id, weight: "+a.one.getShipName()+", "+a.two);
        }
        if (output.isEmpty() && firstCreation) output.addAll(getSavedShips());
        firstCreation = false;
        return output;
    }
    private ArrayList<Pair<FleetMemberAPI,Integer>> getSavedShips(){
        ArrayList<Pair<FleetMemberAPI,Integer>> out = new ArrayList<>();
        if (!Global.getSector().getPlayerPerson().getMemory().contains(Settings.NANO_THIEF_CUSTOM_MASTERY_NUMBERS_MEMORY_KEY)){
            out.add(getBackupShip());
            return out;
        }
        ArrayList<FleetMemberAPI> variants = (ArrayList<FleetMemberAPI>) Global.getSector().getPlayerPerson().getMemory().get(Settings.NANO_THIEF_CUSTOM_MASTERY_MEMORY_KEY);
        ArrayList<Integer> numbers = (ArrayList<Integer>) Global.getSector().getPlayerPerson().getMemory().get(Settings.NANO_THIEF_CUSTOM_MASTERY_NUMBERS_MEMORY_KEY);
        for (int a = 0; a < variants.size(); a++){
            Pair<FleetMemberAPI,Integer> b = new Pair<>();
            b.one = variants.get(a);
            //double c = numbers.get(a);
            b.two = numbers.get(a);
            out.add(b);
        }
        if (out.isEmpty()) out.add(getBackupShip());
        return out;
    }
    private Pair<FleetMemberAPI,Integer> getBackupShip(){
        Pair<FleetMemberAPI,Integer> data = new Pair<>();
        data.one = Global.getFactory().createFleetMember(FleetMemberType.SHIP,"kite_pirates_Raider");
        //data.one = Global.getSector().getPlayerFleet().getFleetData().getMembersListCopy().get(0);
        data.two = 10;
        return data;
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
