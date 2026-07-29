package Abyssal_XO.data.scripts.threat.dialogPlugin;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BaseCustomProductionPickerDelegateImpl;
import com.fs.starfarer.api.campaign.FactionProductionAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.combat.ShipHullSpecAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.fleet.FleetMemberType;
import com.fs.starfarer.api.impl.campaign.rulecmd.AddRemoveCommodity;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;
import com.fs.starfarer.api.loading.WeaponSpecAPI;
import com.fs.starfarer.api.util.Misc;

import java.util.LinkedHashSet;
import java.util.Set;

public class NF_dialog_CustomProduction_res extends NF_dialog_CustomProduction{
    //.... ok so... like... this wont work... I cant make something create supplies here without more work.
    public NF_dialog_CustomProduction_res(InteractionDialogAPI dialog){
        super(dialog);
        //showWeaponPicker(dialog);
    }
    @Override
    protected void showWeaponPicker(InteractionDialogAPI dialog){//, Map<String, MemoryAPI> memoryMap) {
        //!!!!!THIS WILL NOT WORK!!!!!!!!//
        //The issue is simple: this is built to handle weapons, ships, and hulls. So unless I create a weapon that is just....
        //.... Fuck you all. For real. I am going to have to do it.//
        //EDITS: This will work! HOW? simple: All I need to do is simply... add this to weapons.
        /// ......///
        //todo: move this into the base production. Add it under weapons, and change weapons to be weapons and resorses. make the 'weapons that are resorses' have there own tab, everything should be fineeeeeee.
        int reclaim = getReclaim();//Global.getSector().getPlayerMemoryWithoutUpdate().getInt(ShroudedSubstratePlugin.SHROUDED_SUBSTRATE_AVAILABLE);

        Set<String> resorces = new LinkedHashSet<>();
        //note: this really needs to be changed.
        for (String spec : Global.getSector().getPlayerFaction().getKnownShips()) {
            if (!canBuild(Global.getSettings().getHullSpec(spec))) continue;
            resorces.add(spec);
        }

        dialog.showCustomProductionPicker(new BaseCustomProductionPickerDelegateImpl() {
            @Override
            public Set<String> getAvailableFighters() {
                return new LinkedHashSet<>();
            }
            @Override
            public Set<String> getAvailableShipHulls() {
                return new LinkedHashSet<>();
            }
            @Override
            public Set<String> getAvailableWeapons() {
                return resorces;
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
                return "Resources";
            }


            @Override
            public String getNoMatchingBlueprintsLabelOverride() {
                return "No viable resources to forge";
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
                        Global.getSector().getPlayerFleet().getCargo().addCommodity(convertIdToItemID(item.getSpecId()),item.getQuantity());
                        dialog.getTextPanel().addPara("Gained "+item.getQuantity()+" "+Global.getSettings().getCommoditySpec(convertIdToItemID(item.getSpecId())).getName(), Misc.getTextColor(),Misc.getHighlightColor(),""+item.getQuantity(),Global.getSettings().getCommoditySpec(convertIdToItemID(item.getSpecId())).getName());
                    }
                }
                //FireBest.fire(null, dialog, memoryMap, "SubstrateWeaponsPicked");

                Global.getSoundPlayer().playUISound("ui_cargo_machinery_drop", 1f, 1f);
            }
        });
    }
    @Override
    protected boolean canBuild(WeaponSpecAPI a){
        /*if (cost > 0 && cost <= reclaim) {
            fighters.add(spec);
        }*/
        return true;
    }
    protected String convertIdToItemID(String input){
        String out = switch (input) {
            //just an example, for now.
            case "abyssal_XO_fake_supplies" -> "supplies";
            default -> "";
        };
        return out;
    }
    @Override
    protected int getCost(WeaponSpecAPI a){
        int base = (int) Global.getSettings().getCommoditySpec(convertIdToItemID(a.getWeaponId())).getBasePrice();
        return modifyCost(base);
    }
    @Override
    protected int modifyCost(int baseCost){
        return baseCost / 10;
    }
}
