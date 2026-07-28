package Abyssal_XO.data.scripts.backgrounds;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.FactionSpecAPI;
import com.fs.starfarer.api.characters.FullName;
import com.fs.starfarer.api.characters.PersonAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import exerelin.campaign.backgrounds.BaseCharacterBackground;
import exerelin.utilities.NexFactionConfig;
import lunalib.lunaUtil.LunaCommons;
import second_in_command.SCUtils;
import second_in_command.specs.SCOfficer;

import static Abyssal_XO.data.scripts.Settings.MEMKEY_NANOTHIEF_BACKGROUND;

public class BlackBox extends BaseCharacterBackground {
    public static boolean isStartUnlocked(){
        //LunaCommons.set("Abyssal_XO","BlackBoxStartUnlock",false);
        return Boolean.TRUE.equals(LunaCommons.getBoolean("Abyssal_XO", "BlackBoxStartUnlock"));
    }
    public boolean canUse(){
        //to set run:
        //LunaCommons.set("Abyssal_XO","BlackBoxStartUnlock",true);
        //if (true) return true;
        return isStartUnlocked();
    }
    @Override
    public String getTitle(FactionSpecAPI factionSpec, NexFactionConfig factionConfig) {
        if (canUse()) return super.getTitle(factionSpec, factionConfig);
        return "??? [Locked]";
    }

    @Override
    public boolean shouldShowInSelection(FactionSpecAPI factionSpec, NexFactionConfig factionConfig) {
        return super.shouldShowInSelection(factionSpec, factionConfig);
    }

    @Override
    public boolean canBeSelected(FactionSpecAPI factionSpec, NexFactionConfig factionConfig) {
        return canUse();
    }

    @Override
    public String getShortDescription(FactionSpecAPI factionSpec, NexFactionConfig factionConfig) {
        if (!canUse()) return "Make a choice regarding a strange black box to unlock this background.";
        return super.getShortDescription(factionSpec, factionConfig);
    }

    @Override
    public String getLongDescription(FactionSpecAPI factionSpec, NexFactionConfig factionConfig) {
        if (!canUse()) return "";
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

        //String a = "The Family h";
        PersonAPI person = Global.getFactory().createPerson();
        person.setName(new FullName("Black","Box", FullName.Gender.ANY));
        person.setPortraitSprite("graphics/icons/AbyssalXO_BlackBox.png");
        SCOfficer officer = new SCOfficer(person, "Abyssal_NanoThief");
        SCUtils.getPlayerData().addOfficerToFleet(officer);

        Global.getSector().getMemory().set(MEMKEY_NANOTHIEF_BACKGROUND,true);
    }

    @Override
    public void addTooltipForSelection(TooltipMakerAPI tooltip, FactionSpecAPI factionSpec, NexFactionConfig factionConfig, Boolean expanded) {
        super.addTooltipForSelection(tooltip, factionSpec, factionConfig, expanded);
        if (!canUse()) return;
        tooltip.addSpacer(10f);
        tooltip.addPara("Start the game with the Nano-Thief Attribute.",0f, Misc.getTextColor(),Misc.getHighlightColor(),"Nano-Thief Attribute");
    }

    @Override
    public float getOrder() {
        if (!canUse()) return Integer.MAX_VALUE - spec.order;
        return super.getOrder();
    }
}
