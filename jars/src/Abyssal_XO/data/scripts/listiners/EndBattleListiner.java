package Abyssal_XO.data.scripts.listiners;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.lunaSettings.StoredSettings;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_Skill3_TimeListiner;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_3;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.*;
import com.fs.starfarer.api.combat.EngagementResultAPI;
import com.fs.starfarer.api.fleet.FleetMemberAPI;
import com.fs.starfarer.api.impl.campaign.RuleBasedInteractionDialogPluginImpl;

import static Abyssal_XO.data.scripts.Settings.TAG_NANOTHIEF_BOSS;
import static Abyssal_XO.data.scripts.SiC_NanoThief_Plugin.addThreatBossCreate;

public class EndBattleListiner extends BaseCampaignEventListener {
    //todo: stages of threat quest:
    // 1: get 'marked' by the transceiver. Maybe create a dialog for this.
    // 2: when in threat space, a special threat fleet will spawn. It will go directly to the player. If the player enters a new system and this fleet is created, the old one is discarded.
    //   - this fleet will have the 3 nano-thief XO officers I wanted to test.
    //   - this fleet will have a few more ships then normal maybe? or just normal threat strong?
    // 4: when this fleet is destroyed, it will drop the black box. right clicking on it will add nano-thief, and add the 'black box' start.
    public static int requiredFabsForThreat = 6;
    public EndBattleListiner(boolean permaRegister) {
        super(permaRegister);
        Settings.log.info("HERE: adding a listiner of id: "+this);
    }
    @Override
    public void reportPlayerEngagement(EngagementResultAPI result) {
        processNT_Skill3();
        processThreat(result);
    }
    private void processNT_Skill3(){
        //Settings.log.info("(end battle listiner) runing...");
        if (!NanoThief_3.playerHasSkill3) return;
        //Settings.log.info("(end battle listiner) player has skill...");
        NanoThief_3.calculatePlayerSuppliesGained();
        if (!Global.getSector().getPlayerFleet().hasScriptOfClass(NanoThief_Skill3_TimeListiner.class)){
            //Settings.log.info("(end battle listiner) added true listener...");
            Global.getSector().getPlayerFleet().addScript(new NanoThief_Skill3_TimeListiner());
        }
    }
    private void processThreat(EngagementResultAPI result){
        if (!result.didPlayerWin()) return;
        if (Global.getSector().getMemory().contains(Settings.MEMKEY_NANOTHIEF_BLACK_BOX_QUEST_STAGE) && Global.getSector().getMemory().getInt(Settings.MEMKEY_NANOTHIEF_BLACK_BOX_QUEST_STAGE) > 0){
            switch (Global.getSector().getMemory().getInt(Settings.MEMKEY_NANOTHIEF_BLACK_BOX_QUEST_STAGE)){
                //note: any 'after threat killed' things should be added here.
                case 2:
                    Settings.log.info("attempting to add the loot listiner...");
                    for (CampaignFleetAPI loser : result.getBattle().getOtherSideFor(Global.getSector().getPlayerFleet())) {
                        if (loser.hasTag(TAG_NANOTHIEF_BOSS)){
                            Settings.log.info("Adding the loot listener.");
                            Global.getSector().getListenerManager().addListener(new AddNanoThief_LootListiner());
                            Global.getSector().getMemory().set(Settings.MEMKEY_NANOTHIEF_BLACK_BOX_QUEST_STAGE,3);
                            break;
                        }
                    }
                    break;
            }
            return;
        }
        int size = 0;
        for (FleetMemberAPI a : result.getLoserResult().getDestroyed()){
            if (a.getVariant().getHullSpec().getHullId().equals("fabricator_unit")) size++;
        }
        for (FleetMemberAPI a : result.getLoserResult().getDisabled()){
            if (a.getVariant().getHullSpec().getHullId().equals("fabricator_unit")) size++;
        }
        size = !Global.getSector().getMemory().contains(Settings.MEMKEY_NANOTHIEF_KILLED_FABS) ? size : size + Global.getSector().getMemory().getInt(Settings.MEMKEY_NANOTHIEF_KILLED_FABS);
        Global.getSector().getMemory().set(Settings.MEMKEY_NANOTHIEF_KILLED_FABS,size);
        Settings.log.info("this class is: "+this);
        Settings.log.info("HERE: ADDING LISTINER I AM ONLY SUPPOSE TO HAVE ONE OF: do I already have one?: "+Global.getSector().getListenerManager().getListeners(EndBattleListiner.class).size());
        Settings.log.info("HERE: already have lsitiner? "+Global.getSector().getListenerManager().hasListenerOfClass(EndBattleListiner.class));
        int count_a = 0;
        int count_a2 = 0;
        int count_b = 0;
        int count_b2 = 0;
        for (CampaignEventListener a : Global.getSector().getAllListeners()){
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
        }
        Settings.log.info("got counts of lstiners as: "+count_a+", "+count_b);
        Settings.log.info("got things that re not listiners as: "+count_a2+", "+count_b2);
        Settings.log.info("Updating killed fab memory to: "+size);

        if (size >= requiredFabsForThreat && StoredSettings.tempEnableBoss){
            //todo: find out if I am adding more then one of this listiner into combat by mistake or not.
            //todo: make it so this is a 1 - 5 day long listiner instead. A perma regesterd delay basicly.
            Settings.log.info("Preparing pre-boss event");
            //Global.getSector().getListenerManager().addListener(new AddNanoThief_LootListiner());
            Global.getSector().getMemory().set(Settings.MEMKEY_NANOTHIEF_BLACK_BOX_QUEST_STAGE,1);//NOTE: this prevents this code from triggering again.
            Global.getSector().addScript(new DelayedScript(5f, 20f) {
                @Override
                public void ActivateCode() {
                    Settings.log.info("pre-boss event done. ready for bossfight >=)");
                    RuleBasedInteractionDialogPluginImpl plugin = new RuleBasedInteractionDialogPluginImpl("abyssal_XO_UnknownSenserStart");
                    //plugin.setCustom1(helper);
                    Global.getSector().getCampaignUI().showInteractionDialogFromCargo(plugin, Global.getSector().getPlayerFleet(), new CampaignUIAPI.DismissDialogDelegate() {
                        @Override
                        public void dialogDismissed() {
                            Global.getSector().getMemory().set(Settings.MEMKEY_NANOTHIEF_BLACK_BOX_QUEST_STAGE,2);
                            addThreatBossCreate();//adds both lisitiners here.
                        }
                    });
                }
            });
        }

    }
    @Override
    public void reportEncounterLootGenerated(FleetEncounterContextPlugin plugin, CargoAPI loot) {
        //so... does this -only- look at look, or can this add to it as well? more information required.
    }
}
