package Abyssal_XO.data.scripts.threat.listiners;

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
        Nano_Thief_Stats.setPlayerExstraReclaimIfRequired();
        float reclaim = Nano_Thief_Stats.getPlayerExstraReclaim();
        log.info("attempting to create more loot from something or other =)");
        reclaim /= NanoThief_3.reclaimPerSet;
        log.info("got sets as:");
        log.info("  total sets: "+reclaim);
        log.info("  supplies: "+reclaim*NanoThief_3.suppliesPerSet);
        loot.addCommodity("supplies",reclaim*NanoThief_3.suppliesPerSet);
    }
}
