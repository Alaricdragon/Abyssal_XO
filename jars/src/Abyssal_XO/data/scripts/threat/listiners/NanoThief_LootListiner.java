package Abyssal_XO.data.scripts.threat.listiners;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_3;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.InteractionDialogAPI;
import com.fs.starfarer.api.campaign.listeners.ShowLootListener;
import org.apache.log4j.Logger;

public class NanoThief_LootListiner implements ShowLootListener {
    private static Logger log = Global.getLogger(Nano_Thief_Stats.class);
    @Override
    public void reportAboutToShowLootToPlayer(CargoAPI loot, InteractionDialogAPI dialog) {
        NanoThief_3.calculatePlayerSuppliesGained();
        Settings.log.info("(loot adder): added supplies: "+NanoThief_3.reclaimToAdd);
        loot.addSupplies(NanoThief_3.reclaimToAdd);
        NanoThief_3.reclaimAdded+=NanoThief_3.reclaimToAdd;
        NanoThief_3.reclaimToAdd=0;
        NanoThief_3.alreadyLooted = true;
        NanoThief_3.playerHasSkill3 = false;//don't know if it exists anymore.
    }
}
