package Abyssal_XO.data.scripts.backgrounds;

import com.fs.starfarer.api.campaign.FactionSpecAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import exerelin.campaign.backgrounds.BaseCharacterBackground;
import exerelin.utilities.NexFactionConfig;
import lunalib.lunaUtil.LunaCommons;

public class BlackBox extends BaseCharacterBackground {
    private boolean isUnlocked(){
        //to set run:
        //LunaCommons.set("Abyssal_XO","BlackBoxStartUnlock",true);
        //if (true) return true;
        return Boolean.TRUE.equals(LunaCommons.getBoolean("Abyssal_XO", "BlackBoxStartUnlock"));
    }
    @Override
    public String getTitle(FactionSpecAPI factionSpec, NexFactionConfig factionConfig) {
        if (isUnlocked()) return super.getTitle(factionSpec, factionConfig);
        return "??? [Locked]";
    }

    @Override
    public boolean shouldShowInSelection(FactionSpecAPI factionSpec, NexFactionConfig factionConfig) {
        return super.shouldShowInSelection(factionSpec, factionConfig);
    }

    @Override
    public boolean canBeSelected(FactionSpecAPI factionSpec, NexFactionConfig factionConfig) {
        return isUnlocked();
    }

    @Override
    public String getShortDescription(FactionSpecAPI factionSpec, NexFactionConfig factionConfig) {
        if (!isUnlocked()) return "Make a choice regarding a strange black box to unlock this background.";
        return super.getShortDescription(factionSpec, factionConfig);
    }

    @Override
    public String getLongDescription(FactionSpecAPI factionSpec, NexFactionConfig factionConfig) {
        if (!isUnlocked()) return "";
        return super.getLongDescription(factionSpec, factionConfig);
    }

    @Override
    public void addTooltipForIntel(TooltipMakerAPI tooltip, FactionSpecAPI factionSpec, NexFactionConfig factionConfig) {
        super.addTooltipForIntel(tooltip, factionSpec, factionConfig);
    }

    @Override
    public void onNewGameAfterTimePass(FactionSpecAPI factionSpec, NexFactionConfig factionConfig) {
        //note: other 'on new game' code exsists
        //todo: add the XO here.
        super.onNewGameAfterTimePass(factionSpec, factionConfig);
    }

    @Override
    public void addTooltipForSelection(TooltipMakerAPI tooltip, FactionSpecAPI factionSpec, NexFactionConfig factionConfig, Boolean expanded) {
        super.addTooltipForSelection(tooltip, factionSpec, factionConfig, expanded);
        if (!isUnlocked()) return;
        tooltip.addSpacer(10f);
        tooltip.addPara("Start the game with the Nano-Thief Attribute.",0f, Misc.getTextColor(),Misc.getHighlightColor(),"Nano-Thief Attribute");
    }

    @Override
    public float getOrder() {
        if (!isUnlocked()) return Integer.MAX_VALUE - spec.order;
        return super.getOrder();
    }
}
