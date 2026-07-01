package Abyssal_XO.data.scripts.listiners;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.Utils;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_LootListiner;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.BaseCampaignEventListener;
import com.fs.starfarer.api.campaign.CargoAPI;
import com.fs.starfarer.api.campaign.FleetEncounterContextPlugin;
import com.fs.starfarer.api.combat.EngagementResultAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;

public class EndBattleListiner extends BaseCampaignEventListener {
    //todo: stages of threat quest:
    // 1: get 'marked' by the transceiver. Maybe create a dialog for this.
    // 2: when in threat space, a special threat fleet will spawn. It will go directly to the player. If the player enters a new system and this fleet is created, the old one is discarded.
    //   - this fleet will have the 3 nano-thief XO officers I wanted to test.
    //   - this fleet will have a few more ships then normal maybe? or just normal threat strong?
    // 4: when this fleet is destroyed, it will drop the black box. right clicking on it will add nano-thief, and add the 'black box' start.
    public static int requiredFabsForThreat = 8;
    public EndBattleListiner(boolean permaRegister) {
        super(permaRegister);
    }
    @Override
    public void reportPlayerEngagement(EngagementResultAPI result) {
        if (!result.didPlayerWin()) return;
        processThreat(result);
    }
    private void processThreat(EngagementResultAPI result){
        if (Global.getSector().getMemory().contains(Settings.MEMKEY_NANOTHIEF_STATUS) && Global.getSector().getMemory().getInt(Settings.MEMKEY_NANOTHIEF_STATUS) == -1) return;
        int size = 0;
        for (FleetMemberAPI a : result.getWinnerResult().getDestroyed()){
            if (a.getVariant().getHullSpec().getHullId().equals("fabricator_unit")) size++;
        }
        for (FleetMemberAPI a : result.getWinnerResult().getDisabled()){
            if (a.getVariant().getHullSpec().getHullId().equals("fabricator_unit")) size++;
        }
        size = Global.getSector().getMemory().contains(Settings.MEMKEY_NANOTHIEF_STATUS) ? size : size + Global.getSector().getMemory().getInt(Settings.MEMKEY_NANOTHIEF_STATUS);
        Global.getSector().getMemory().set(Settings.MEMKEY_NANOTHIEF_STATUS,size);

        //Global.getFactory().createFleet

        //todo: note: AM NOT DOING THIS DO TO THE -ISSUES- WITH THIS. (The issues are simple: Someone might not chose to take the hullmod then be locked out of content.)
        //String[] requiredMods = new String[]{
        //    "fragment_swarm",
        //    "secondary_fabricator",
        //    "fragment_coordinator"
        //};
        //if ()for (String a : requiredMods) if (!Global.getSector().getPlayerFaction().getKnownHullMods().contains(a)) return;
        if (size >= requiredFabsForThreat){
            Global.getSector().getListenerManager().addListener(new AddNanoThief_LootListiner());
            Global.getSector().getMemory().set(Settings.MEMKEY_NANOTHIEF_STATUS,-1);
        }
    }
    @Override
    public void reportEncounterLootGenerated(FleetEncounterContextPlugin plugin, CargoAPI loot) {
        //so... does this -only- look at look, or can this add to it as well? more information required.
    }
}
