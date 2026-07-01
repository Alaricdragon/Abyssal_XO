package Abyssal_XO.data.scripts.listiners;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.SpecialItemData;
import com.fs.starfarer.api.campaign.listeners.ShowLootListener;

public class AddNanoThief_LootListiner implements ShowLootListener {
    @Override
    public void reportAboutToShowLootToPlayer(CargoAPI loot, InteractionDialogAPI dialog) {
        //Global.getCombatEngine().isMission();
        //Global.getCombatEngine().isSimulation();
        //Global.getCombatEngine().isInMissionSim();
        //Global.getCombatEngine().isInCampaignSim();
        dialog.getInteractionTarget();//maybe use this to determine what fleet I was fighting?
        loot.addSpecial(new SpecialItemData("",""),1);//would this create a new specal item?
        Global.getSector().getListenerManager().removeListener(this);
    }
}
