package Abyssal_XO.data.scripts;
import Abyssal_XO.data.scripts.listiners.AddNanoThief_LootListiner;
import Abyssal_XO.data.scripts.listiners.EndBattleListiner;
import Abyssal_XO.data.scripts.listiners.ThreatBossCreater;
import Abyssal_XO.data.scripts.lunaSettings.StoredSettings;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_LootListiner;
import Abyssal_XO.data.scripts.utility.CleanListTemp;
import com.fs.starfarer.api.BaseModPlugin;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignEventListener;
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
        removeUnreqiredListiners();
        int count_a = 0;
        int count_a2 = 0;
        int count_b = 0;
        int count_b2 = 0;
        int count_c = 0;
        int count_c2 = 0;
        int count_d = 0;
        int count_d2 = 0;
        for (CampaignEventListener a : Global.getSector().getAllListeners()){
            //Settings.log.info("got cn as: "+a.getClass().getCanonicalName());//<-
            /*
            todo: find the two people who made the scripts here, and make them pay. I mean uh... dont make them pay.
                  Tell them that there scrips are being added every time the game loads. So there is a lot of them.
            195195 [Thread-2] INFO  Abyssal_XO.data.scripts.Settings  - got cn as: org.widehorizons.ui.scripts.WHDiscoveryEvents
            195195 [Thread-2] INFO  Abyssal_XO.data.scripts.Settings  - got cn as: data.scripts.vice.listeners.PruneBantengMarketListener
             */
            if (a instanceof EndBattleListiner){
                count_a++;
            }else{
                count_a2++;
            }
            if (a instanceof ThreatBossCreater){
                count_b++;
            }else{
                count_b2++;
            }
            if (a instanceof NanoThief_LootListiner){
                count_c++;
            }else{
                count_c2++;
            }
            if (a instanceof AddNanoThief_LootListiner){
                count_d++;
            }else{
                count_d2++;
            }
        }
        Settings.log.info("got counts of lstiners as: "+count_a+", "+count_b+", "+count_c+", "+count_d);
        Settings.log.info("got things that re not listiners as: "+count_a2+", "+count_b2+", "+count_c2+", "+count_d2);
         addKillLisinter();
         addThreatBossCreate();
    }
    private void addKillLisinter(){
        //todo: add checks to make sure this is still required.
        Settings.log.info("HERE: ADDING LISTINER I AM ONLY SUPPOSE TO HAVE ONE OF: do I already have one?: "+Global.getSector().getListenerManager().getListeners(EndBattleListiner.class).size());
        Settings.log.info("HERE: already have lsitiner? "+Global.getSector().getListenerManager().hasListenerOfClass(EndBattleListiner.class));
        if (!Global.getSector().getListenerManager().hasListenerOfClass(EndBattleListiner.class)) Global.getSector().addTransientListener(new EndBattleListiner(false));
    }
    public static void addThreatBossCreate(){
        //todo: add checks to add this only if required.
        //      note: for now its always active, just to see how its wokring in log.
        if (!StoredSettings.tempEnableBoss || Global.getSector().getMemory().getInt(Settings.MEMKEY_NANOTHIEF_BLACK_BOX_QUEST_STAGE) != 2) return;//prevent boss activation its not ready yet.
        if (!Global.getSector().getListenerManager().hasListenerOfClass(ThreatBossCreater.class)) Global.getSector().addTransientListener(new ThreatBossCreater(false));
        //if (!Global.getSector().getListenerManager().hasListenerOfClass(AddNanoThief_LootListiner.class)) Global.getSector().getListenerManager().addListener(new AddNanoThief_LootListiner());
    }

    private static void removeUnreqiredListiners(){
        Settings.log.info("triming down scripts. Sorry about this...");
        new CleanListTemp() {
            @Override
            public boolean shouldClean(Object a) {
                return a instanceof EndBattleListiner;
            }

            @Override
            public void runList() {
                for (CampaignEventListener a : Global.getSector().getAllListeners()) process(a);
            }

            @Override
            public void remove(Object a) {
                Settings.log.info("a as: "+a.getClass().getCanonicalName());
                Global.getSector().removeListener((CampaignEventListener) a);//we will see if that works lol
            }
        };
        new CleanListTemp() {
            @Override
            public boolean shouldClean(Object a) {
                return a instanceof ThreatBossCreater;
            }

            @Override
            public void runList() {
                for (CampaignEventListener a : Global.getSector().getAllListeners()) process(a);
            }

            @Override
            public void remove(Object a) {
                Settings.log.info("b as: "+a.getClass().getCanonicalName());
                Global.getSector().removeListener((CampaignEventListener) a);//we will see if that works lol
            }
        };
        Settings.log.info("finished triming down scripts. Let us never talk about this again.");
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