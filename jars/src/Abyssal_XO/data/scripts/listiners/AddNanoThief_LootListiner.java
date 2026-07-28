package Abyssal_XO.data.scripts.listiners;

import Abyssal_XO.data.scripts.Settings;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.campaign.listeners.ShowLootListener;

import static Abyssal_XO.data.scripts.Settings.MEMKEY_NANOTHIEF_BLACK_BOX_QUEST_STAGE;

public class AddNanoThief_LootListiner implements ShowLootListener {
    @Override
    public void reportAboutToShowLootToPlayer(CargoAPI loot, InteractionDialogAPI dialog) {
        //if (!(Global.getSector().getMemory().contains(MEMKEY_NANOTHIEF_BLACK_BOX_QUEST_STAGE) && Global.getSector().getMemory().getInt(MEMKEY_NANOTHIEF_BLACK_BOX_QUEST_STAGE) == -3)) return;
        //if (!(dialog.getInteractionTarget() instanceof CampaignFleetAPI)) return;
        //CampaignFleetAPI a  = (CampaignFleetAPI) dialog.getInteractionTarget();
        //if (!a.hasTag(TAG_NANOTHIEF_BOSS)) return;
        Settings.log.info("adding the black box to loot.");
        //loot.addSupplies(600000);
        loot.addSpecial(new SpecialItemData("Abyssal_XO_black_box",null),1);//would this create a new specal item?
        Global.getSector().getMemory().set(MEMKEY_NANOTHIEF_BLACK_BOX_QUEST_STAGE,4);//sets this quest to finally be over.
        Global.getSector().getListenerManager().removeListener(this);
        Global.getSector().getListenerManager().removeListenerOfClass(ThreatBossCreater.class);
        //the remove listiners can be checked just by going in game and entering / exiting the system over and over.
    }
}
