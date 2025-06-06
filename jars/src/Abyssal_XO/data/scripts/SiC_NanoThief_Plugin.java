package Abyssal_XO.data.scripts;
import com.fs.starfarer.api.BaseModPlugin;
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
    }

    @Override
    public void beforeGameSave() {
        super.beforeGameSave();
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