package Abyssal_XO.data.scripts.threat.skills;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_DP_Listener;
import Abyssal_XO.data.scripts.threat.listiners.NanoThief_ShipStats;
import com.fs.starfarer.api.combat.MutableShipStatsAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.ShipVariantAPI;
import com.fs.starfarer.api.ui.LabelAPI;
import com.fs.starfarer.api.ui.TooltipMakerAPI;
import com.fs.starfarer.api.util.Misc;
import second_in_command.SCData;

import java.util.List;

public class NanoThief_8 extends Nano_Thief_SKill_Base{
    public static final int maxTime = 10;
    private static final int reclaimPerMulti = 1000;
    private static final float hpPercent = 0.05f;
    private static final float hpMin = 200f;
    private static final float crPer = 0.02f;
    private static final float pptPer = 10f;
    private static final float fighterPercent = 0.10f;
    @Override
    public String getAffectsString() {
        return "all ships in fleet";
    }
    @Override
    public void ApplyChangeOnReclaim(ShipAPI ship, ShipAPI reclaim, float reclaimValue, Nano_Thief_Stats stats) {
        applythingToShipOrModule(ship, reclaim, reclaimValue, stats);
        for (ShipAPI a : ship.getChildModulesCopy()){
            applythingToShipOrModule(a, reclaim, reclaimValue, stats);
        }
    }
    private void applythingToShipOrModule(ShipAPI ship, ShipAPI reclaim, float reclaimValue, Nano_Thief_Stats stats){
        NanoThief_DP_Listener listiner = null;
        if (ship.hasListenerOfClass(NanoThief_DP_Listener.class)) {
            List<NanoThief_DP_Listener> a = ship.getListenerManager().getListeners(NanoThief_DP_Listener.class);
            listiner = a.get(0);
        }else{
            listiner = new NanoThief_DP_Listener(ship);
            ship.getListenerManager().addListener(listiner);
        }
        float multi = (reclaimValue / reclaimPerMulti) / maxTime;
        float hp = ship.getMaxHitpoints();
        hp = Math.max(hp*hpPercent,hpMin);
        log.info("applying restore to ship named: "+ship.getName()+" with stats of: HP: "+hp*multi+"CR"+crPer*multi+", ppr:"+pptPer*multi+", fighter rate"+fighterPercent*multi);
        listiner.addPower(hp*multi,crPer*multi,pptPer*multi,fighterPercent*multi);
    }

    @Override
    public void addTooltip(SCData scData, TooltipMakerAPI tooltip) {
        tooltip.addPara("For every %s reclaim gained, repair the following over the next %s seconds",0,Misc.getHighlightColor(),Misc.getHighlightColor(),reclaimPerMulti+"",maxTime+"");
        String stra = (int)(hpPercent*100)+"%";
        String stra2 = (int)hpMin+"";
        String strb = ""+(int)(crPer*100)+"%";
        String strc = ""+(int)pptPer;
        String strd = ""+(int)(fighterPercent*100)+"%";
        tooltip.addPara("   -gain %s or %s hull, whatever is higher",0f,Misc.getTextColor(), Misc.getHighlightColor(),stra,stra2);
        tooltip.addPara("   -%s combat readiness",0f,Misc.getTextColor(), Misc.getHighlightColor(),strb);
        tooltip.addPara("   -%s second peak performance time",0f,Misc.getTextColor(), Misc.getHighlightColor(),strc);
        tooltip.addPara("   -%s fighter replacement rate",0f,Misc.getTextColor(), Misc.getHighlightColor(),strd);
        //tooltip.addPara("Lose %s quality", 0f,Misc.getNegativeHighlightColor(), Misc.getNegativeHighlightColor(), ""+quality);

        tooltip.addSpacer(10f);

        LabelAPI label = tooltip.addPara("\"Our armor is striped to nothing, our weapons damaged past repair, and our hull is in a critical state. Our ship is at its limits, and I know we not the only ones in this situation. Caption, we cannot continue like this. If we don't do something desperate, none of us will return to command.\"", Misc.getTextColor(), 0f);
        tooltip.addPara(" - unknown", Misc.getTextColor(), 0f);

        label.italicize();
    }
}
