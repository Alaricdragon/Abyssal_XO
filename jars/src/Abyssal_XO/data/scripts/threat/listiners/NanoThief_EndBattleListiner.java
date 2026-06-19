package Abyssal_XO.data.scripts.threat.listiners;

import Abyssal_XO.data.scripts.Utils;
import com.fs.starfarer.api.campaign.BaseCampaignEventListener;
import com.fs.starfarer.api.campaign.BattleAPI;
import com.fs.starfarer.api.campaign.CampaignFleetAPI;
import com.fs.starfarer.api.combat.EngagementResultAPI;
import org.lwjgl.openal.Util;

@Deprecated
public class NanoThief_EndBattleListiner extends BaseCampaignEventListener {
    public NanoThief_EndBattleListiner(boolean permaRegister) {
        super(permaRegister);
    }
    @Override
    public void reportBattleFinished(CampaignFleetAPI primaryWinner, BattleAPI battle) {
        super.reportBattleFinished(primaryWinner,battle);
        Utils.removeAllShipsIntoFleets();
    }

    @Override
    public void reportPlayerEngagement(EngagementResultAPI result) {
        super.reportPlayerEngagement(result);
        Utils.removeAllShipsIntoFleets();
    }
}
