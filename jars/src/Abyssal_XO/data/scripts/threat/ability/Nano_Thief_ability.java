package Abyssal_XO.data.scripts.threat.ability;

import Abyssal_XO.data.scripts.CustomUIPannel.MasteryHolder;
import Abyssal_XO.data.scripts.threat.dialogPlugin.Nano_Thief_dialog;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.econ.SubmarketAPI;
import com.fs.starfarer.api.impl.campaign.abilities.BaseDurationAbility;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;

import java.awt.*;

public class Nano_Thief_ability extends BaseDurationAbility {
    private String memoryKey = "$AbyssalXO_StorgeMemory";
    private SubmarketAPI cargo = null;
    @Override
    protected void activateImpl() {
        //Global.getSector().getCampaignUI().showInteractionDialog(Global.getSector().getEconomy().getMarketsCopy().get(0).getSubmarketsCopy().get(0).getCargo());
        //new MasteryHolder(null);
        Global.getSector().getCampaignUI().showInteractionDialog(new Nano_Thief_dialog(),Global.getSector().getPlayerFleet());
    }
    @Override
    protected void applyEffect(float amount, float level) {

    }

    @Override
    protected void deactivateImpl() {

    }

    @Override
    protected void cleanupImpl() {

    }
    @Override
    public void createTooltip(TooltipMakerAPI tooltip, boolean expanded) {
        //tooltip.addPara("",1,Misc.getHighlightColor(),"");
        tooltip.addPara("Lets you modify what fighters and ships nano-thief spawns.",1);
    }

    @Override
    public boolean hasTooltip() { return true; }

}
