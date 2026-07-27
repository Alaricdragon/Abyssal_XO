package Abyssal_XO.data.scripts.listiners;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.listeners.ShowLootListener;

import static Abyssal_XO.data.scripts.Settings.MEMKEY_NANOTHIEF_STATUS;
import static Abyssal_XO.data.scripts.Settings.TAG_NANOTHIEF_BOSS;

public class AddNanoThief_LootListiner implements ShowLootListener {
    @Override
    public void reportAboutToShowLootToPlayer(CargoAPI loot, InteractionDialogAPI dialog) {
        if (!loot.getFleetData().getFleet().hasTag(TAG_NANOTHIEF_BOSS)) return;
        loot.addSpecial(new SpecialItemData("Abyssal_XO_black_box",null),1);//would this create a new specal item?
        Global.getSector().getMemory().set(MEMKEY_NANOTHIEF_STATUS,-2);//sets this quest to finally be over.
        Global.getSector().getListenerManager().removeListener(this);
        Global.getSector().getListenerManager().removeListenerOfClass(ThreatBossCreater.class);
    }
}
