package Abyssal_XO.data.scripts.threat.dialogPlugin;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.impl.items.ShroudedSubstratePlugin;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.combat.ShipHullSpecAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.RuleBasedInteractionDialogPluginImpl;
import com.fs.starfarer.api.impl.campaign.ids.Items;
import com.fs.starfarer.api.impl.campaign.rulecmd.AddRemoveAnyItem;
import com.fs.starfarer.api.impl.campaign.rulecmd.AddRemoveCommodity;
import com.fs.starfarer.api.impl.campaign.rulecmd.FireBest;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;
import com.fs.starfarer.api.util.Misc;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class NF_dialog_CustomProduction {
    public NF_dialog_CustomProduction(InteractionDialogAPI dialog){
        showWeaponPicker(dialog);
    }
    protected void showWeaponPicker(InteractionDialogAPI dialog){//, Map<String, MemoryAPI> memoryMap) {

        int reclaim = getReclaim();//Global.getSector().getPlayerMemoryWithoutUpdate().getInt(ShroudedSubstratePlugin.SHROUDED_SUBSTRATE_AVAILABLE);

        Set<String> weapons = new LinkedHashSet<>();
        for (String spec : Global.getSector().getPlayerFaction().getKnownWeapons()) {
            int cost = getCost(Global.getSettings().getWeaponSpec(spec));
            if (!canBuild(Global.getSettings().getWeaponSpec(spec))) continue;
            weapons.add(spec);
        }
        Set<String> ships = new LinkedHashSet<>();
        for (String spec : Global.getSector().getPlayerFaction().getKnownShips()) {
            int cost = getCost(Global.getSettings().getHullSpec(spec));
            if (!canBuild(Global.getSettings().getHullSpec(spec))) continue;
            ships.add(spec);
        }
        Set<String> fighters = new LinkedHashSet<>();
        for (String spec : Global.getSector().getPlayerFaction().getKnownFighters()) {
            int cost = getCost(Global.getSettings().getFighterWingSpec(spec));
            if (!canBuild(Global.getSettings().getFighterWingSpec(spec))) continue;
            fighters.add(spec);
        }

        dialog.showCustomProductionPicker(new BaseCustomProductionPickerDelegateImpl() {
            @Override
            public Set<String> getAvailableFighters() {
                return fighters;
            }
            @Override
            public Set<String> getAvailableShipHulls() {
                return ships;
            }
            @Override
            public Set<String> getAvailableWeapons() {
                return weapons;
            }
            @Override
            public float getCostMult() {
                return 1f;
            }
            @Override
            public float getMaximumValue() {
                return getReclaim();
            }

            @Override
            public String getWeaponColumnNameOverride() {
                return "Weapon";
            }

            @Override
            public String getNoMatchingBlueprintsLabelOverride() {
                return "No matching weapons";
            }

            @Override
            public String getMaximumOrderValueLabelOverride() {
                return "Reclaim available";
            }

            @Override
            public String getCurrentOrderValueLabelOverride() {
                return "Reclaim required";
            }
            @Override
            public String getItemGoesOverMaxValueStringOverride() {
                return "Not enough Reclaim";
            }
            @Override
            public String getCustomOrderLabelOverride() {
                return "Reclaim reconfiguration";
            }
            @Override
            public String getNoProductionOrdersLabelOverride() {
                return "No reconfiguration orders";
            }
            @Override
            public boolean withQuantityLimits() {
                return false;
            }
            @Override
            public boolean isUseCreditSign() {
                return false;
            }

            @Override
            public int getCostOverride(Object item) {
                if (item instanceof WeaponSpecAPI) {
                    return getCost((WeaponSpecAPI) item);
                }
                if(item instanceof FighterWingSpecAPI){
                    return getCost((FighterWingSpecAPI) item);
                }
                if (item instanceof ShipHullSpecAPI){
                    return getCost((ShipHullSpecAPI) item);
                }
                return -1;
            }

            @Override
            public void notifyProductionSelected(FactionProductionAPI production) {
                /*
                if (!(dialog.getPlugin() instanceof RuleBasedInteractionDialogPluginImpl)) return;
                RuleBasedInteractionDialogPluginImpl plugin = (RuleBasedInteractionDialogPluginImpl) dialog.getPlugin();
                if (!(plugin.getCustom1() instanceof SpecialItemPlugin.RightClickActionHelper)) return;
                SpecialItemPlugin.RightClickActionHelper helper = (SpecialItemPlugin.RightClickActionHelper) plugin.getCustom1();
                */

                int cost = production.getTotalCurrentCost();
                //helper.removeFromClickedStackFirst(cost);
                spendReclaim(cost);
                //int reclaim = getReclaim();

                //Global.getSector().getPlayerMemoryWithoutUpdate().set(ShroudedSubstratePlugin.SHROUDED_SUBSTRATE_AVAILABLE, substrate);


                Nano_Thief_dialog.reset();

                for (FactionProductionAPI.ItemInProductionAPI item : production.getCurrent()) {
                    if (item.getType() == FactionProductionAPI.ProductionItemType.WEAPON) {
                        /*helper.addItems(CargoAPI.CargoItemType.WEAPONS, item.getSpecId(), item.getQuantity());
                        AddRemoveCommodity.addWeaponGainText(item.getSpecId(), item.getQuantity(), dialog.getTextPanel());*/
                        Global.getSector().getPlayerFleet().getCargo().addWeapons(item.getSpecId(),item.getQuantity());
                        AddRemoveCommodity.addWeaponGainText(item.getSpecId(),item.getQuantity(),dialog.getTextPanel());
                        continue;
                    }
                    if (item.getType() == FactionProductionAPI.ProductionItemType.FIGHTER){
                        Global.getSector().getPlayerFleet().getCargo().addFighters(item.getSpecId(),item.getQuantity());
                        AddRemoveCommodity.addFighterGainText(item.getSpecId(),item.getQuantity(),dialog.getTextPanel());
                        continue;
                    }
                    if (item.getType() == FactionProductionAPI.ProductionItemType.SHIP){
                        dialog.getTextPanel().addPara("Gained "+item.getQuantity()+" "+Global.getSettings().getHullSpec(item.getSpecId()).getHullName(), Misc.getTextColor(),Misc.getHighlightColor(),""+item.getQuantity(),Global.getSettings().getHullSpec(item.getSpecId()).getHullName());
                        for(int a = 0; a < item.getQuantity(); a++){
                            //Global.getSettings().getVariant();
                            //Global.getSettings().getHullSpec(item.getSpecId());
                            //Misc;
                            //Global.getFactory().createFleetMember();
                            //Global.getFactory()
                            FleetMemberAPI member = Global.getFactory().createFleetMember(FleetMemberType.SHIP,item.getSpecId()+"_Hull");
                            Global.getSector().getPlayerFleet().getFleetData().addFleetMember(member);
                        }
                        //AddRemoveCommodity.addFleetMemberGainText(dialog.getTextPanel());
                    }
                }
                //FireBest.fire(null, dialog, memoryMap, "SubstrateWeaponsPicked");

                Global.getSoundPlayer().playUISound("ui_cargo_machinery_drop", 1f, 1f);
            }
        });
    }
    protected int getReclaim(){
        return 50000000;
    }
    protected boolean canBuild(WeaponSpecAPI a){
        /*if (cost > 0 && cost <= reclaim) {
            fighters.add(spec);
        }*/
        return true;
    }
    protected boolean canBuild(FighterWingSpecAPI a){
        return true;
    }
    protected boolean canBuild(ShipHullSpecAPI a){
        return true;
    }
    protected int getCost(WeaponSpecAPI a){
        return modifyCost((int) a.getBaseValue());
    }
    protected int getCost(FighterWingSpecAPI a){
        return modifyCost((int) a.getBaseValue());
    }
    protected int getCost(ShipHullSpecAPI a){
        return modifyCost((int) a.getBaseValue());
    }
    protected int modifyCost(int baseCost){
        return baseCost / 10;
    }

    protected void spendReclaim(int reclaim){

    }
}
