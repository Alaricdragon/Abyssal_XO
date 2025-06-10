package Abyssal_XO.data.scripts.threat.ability;

import Abyssal_XO.data.scripts.threat.dialogPlugin.Nano_Thief_dialog;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.MarketAPI;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.impl.campaign.abilities.BaseDurationAbility;
import org.lwjgl.opengl.GL11;

public class Nano_Thief_ability extends BaseDurationAbility {
    private String memoryKey = "$AbyssalXO_StorgeMemory";
    private SubmarketAPI cargo = null;
    @Override
    protected void activateImpl() {
        //Global.getSector().getCampaignUI().showInteractionDialog(Global.getSector().getEconomy().getMarketsCopy().get(0).getSubmarketsCopy().get(0).getCargo());
        Global.getSector().getCampaignUI().showInteractionDialog(new Nano_Thief_dialog(),Global.getSector().getPlayerFleet());
        //Global.getFactory().createMarket(memoryKey,"null",1);
        //Global.getSector().getEconomy();
        //CargoAPI.
        //Global;
    }
    /*private SubmarketAPI initIfRequired(){
        //Global.getSector().getCampaignUI().show
        //Global.getFactory().create
        if (!Global.getSector().getMemory().contains(memoryKey)){
            MarketAPI market = Global.getSector().getEconomy().getMarketsCopy().get(0);
            market.addSubmarket("Abyssal_NanoThief_Storge");
            SubmarketAPI subMarket = market.getSubmarket("Abyssal_NanoThief_Storge");
            market.removeSubmarket(subMarket.getSpecId());
            subMarket.getCargoNullOk();


            Global.getSector().getCampaignUI().showInteractionDialog(market.getPrimaryEntity());
            //subMarket
            //subMarket.getCargoNullOk();
            //Global.getSector().getEconomy().getMarketsCopy().get(0).
        }
    }*/
    @Override
    protected void applyEffect(float amount, float level) {

    }

    @Override
    protected void deactivateImpl() {

    }

    @Override
    protected void cleanupImpl() {

    }
}
