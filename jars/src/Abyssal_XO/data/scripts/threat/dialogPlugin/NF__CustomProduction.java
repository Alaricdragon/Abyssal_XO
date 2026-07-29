package Abyssal_XO.data.scripts.threat.dialogPlugin;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.impl.items.ShroudedSubstratePlugin;
import com.fs.starfarer.api.campaign.rules.MemoryAPI;
import com.fs.starfarer.api.combat.FighterWingAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipHullSpecAPI;
import com.fs.starfarer.api.impl.campaign.RuleBasedInteractionDialogPluginImpl;
import com.fs.starfarer.api.impl.campaign.ids.Items;
import com.fs.starfarer.api.impl.campaign.rulecmd.AddRemoveCommodity;
import com.fs.starfarer.api.impl.campaign.rulecmd.FireBest;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import com.fs.starfarer.api.loading.HullModSpecAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

public class NF__CustomProduction {
    protected void showWeaponPicker(InteractionDialogAPI dialog, Map<String, MemoryAPI> memoryMap) {

        int reclaim = getReclaim();//Global.getSector().getPlayerMemoryWithoutUpdate().getInt(ShroudedSubstratePlugin.SHROUDED_SUBSTRATE_AVAILABLE);

        Set<String> weapons = new LinkedHashSet<>();
        for (String spec : Global.getSector().getPlayerFaction().getKnownWeapons()) {
            int cost = getCost(Global.getSettings().getWeaponSpec(spec));
            if (!canBuild(Global.getSettings().getWeaponSpec(spec))) continue;
            if (cost > 0 && cost <= reclaim) {
                weapons.add(spec);
            }
        }
        Set<String> ships = new LinkedHashSet<>();
        for (String spec : Global.getSector().getPlayerFaction().getKnownShips()) {
            int cost = getCost(Global.getSettings().getHullSpec(spec));
            if (!canBuild(Global.getSettings().getHullSpec(spec))) continue;
            if (cost > 0 && cost <= reclaim) {
                ships.add(spec);
            }
        }
        Set<String> fighters = new LinkedHashSet<>();
        for (String spec : Global.getSector().getPlayerFaction().getKnownFighters()) {
            int cost = getCost(Global.getSettings().getFighterWingSpec(spec));
            if (!canBuild(Global.getSettings().getFighterWingSpec(spec))) continue;
            if (cost > 0 && cost <= reclaim) {
                fighters.add(spec);
            }
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
                return "Shrouded Substrate available";
            }

            @Override
            public String getCurrentOrderValueLabelOverride() {
                return "Shrouded Substrate required";
            }
            @Override
            public String getItemGoesOverMaxValueStringOverride() {
                return "Not enough Shrouded Substrate";
            }
            @Override
            public String getCustomOrderLabelOverride() {
                return "Weapon assembly";
            }
            @Override
            public String getNoProductionOrdersLabelOverride() {
                return "No assembly orders";
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
                if (!(dialog.getPlugin() instanceof RuleBasedInteractionDialogPluginImpl)) return;
                RuleBasedInteractionDialogPluginImpl plugin = (RuleBasedInteractionDialogPluginImpl) dialog.getPlugin();
                if (!(plugin.getCustom1() instanceof SpecialItemPlugin.RightClickActionHelper)) return;
                SpecialItemPlugin.RightClickActionHelper helper = (SpecialItemPlugin.RightClickActionHelper) plugin.getCustom1();

                int cost = production.getTotalCurrentCost();
                helper.removeFromClickedStackFirst(cost);
                int substrate = (int) helper.getNumItems(CargoAPI.CargoItemType.SPECIAL, new SpecialItemData(Items.SHROUDED_SUBSTRATE, null));
                Global.getSector().getPlayerMemoryWithoutUpdate().set(ShroudedSubstratePlugin.SHROUDED_SUBSTRATE_AVAILABLE, substrate);

                for (FactionProductionAPI.ItemInProductionAPI item : production.getCurrent()) {
                    if (item.getType() == FactionProductionAPI.ProductionItemType.WEAPON) {
                        helper.addItems(CargoAPI.CargoItemType.WEAPONS, item.getSpecId(), item.getQuantity());
                        AddRemoveCommodity.addWeaponGainText(item.getSpecId(), item.getQuantity(), dialog.getTextPanel());
                    }
                }

                FireBest.fire(null, dialog, memoryMap, "SubstrateWeaponsPicked");

                Global.getSoundPlayer().playUISound("ui_cargo_machinery_drop", 1f, 1f);
            }
        });
    }
    private int getReclaim(){
        return 0;
    }
    private boolean canBuild(WeaponSpecAPI a){
        return true;
    }
    private boolean canBuild(FighterWingSpecAPI a){
        return true;
    }
    private boolean canBuild(ShipHullSpecAPI a){
        return true;
    }
    private int getCost(WeaponSpecAPI a){
        return 5;
    }
    private int getCost(FighterWingSpecAPI a){
        return 5;
    }
    private int getCost(ShipHullSpecAPI a){
        return 5;
    }
}
