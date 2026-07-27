package Abyssal_XO.data.scripts;
import Abyssal_XO.data.scripts.listiners.AddNanoThief_LootListiner;
import Abyssal_XO.data.scripts.listiners.EndBattleListiner;
import Abyssal_XO.data.scripts.listiners.ThreatBossCreater;
import Abyssal_XO.data.scripts.lunaSettings.StoredSettings;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import org.json.JSONException;

public class SiC_NanoThief_Plugin extends BaseModPlugin {
    @Override
    public void onApplicationLoad() {
    }

    @Override
    public void onGameLoad(boolean newGame) {
        super.onGameLoad(newGame);
        try {
            Settings.init();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        StoredSettings.attemptEnableLunalib();
        StoredSettings.getSettings();
        addListinersAsRequired();
    }
    @Override
    public void beforeGameSave() {
        super.beforeGameSave();
    }
    private void addListinersAsRequired(){
         addKillLisinter();
         addThreatBossCreate();
    }
    private void addKillLisinter(){
        //todo: add checks to make sure this is still required.
        Settings.log.info("HERE: ADDING LISTINER I AM ONLY SUPPOSE TO HAVE ONE OF: do I already have one?: "+Global.getSector().getListenerManager().getListeners(EndBattleListiner.class).size());
        if (!Global.getSector().getListenerManager().hasListenerOfClass(EndBattleListiner.class)) Global.getSector().addListener(new EndBattleListiner(false));
    }
    public static void addThreatBossCreate(){
        //todo: add checks to add this only if required.
        //      note: for now its always active, just to see how its wokring in log.
        if (!StoredSettings.tempEnableBoss || Global.getSector().getMemory().getInt(Settings.MEMKEY_NANOTHIEF_STATUS) != -1) return;//prevent boss activation its not ready yet.
        if (!Global.getSector().getListenerManager().hasListenerOfClass(ThreatBossCreater.class))Global.getSector().getListenerManager().addListener(new ThreatBossCreater(false));
        if (!Global.getSector().getListenerManager().hasListenerOfClass(AddNanoThief_LootListiner.class))Global.getSector().getListenerManager().addListener(new AddNanoThief_LootListiner());
    }

    /*@Override
    public PluginPick<ShipAIPlugin> pickShipAI(FleetMemberAPI member, ShipAPI ship) {
        if (ship.isFighter()) {
            if (ship.getHullSpec().hasTag(Tags.THREAT_SWARM_AI)) {
                return new PluginPick<ShipAIPlugin>(new Nano_Thief_AI_Reclaim(ship), CampaignPlugin.PickPriority.HIGHEST);//CampaignPlugin.PickPriority.CORE_SPECIFIC);
            }
            return null;
        }
    }*/
}