package Abyssal_XO.data.scripts.threat.listiners;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_8;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.FighterLaunchBayAPI;
import com.fs.starfarer.api.combat.MutableStat;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import org.apache.log4j.Logger;

public class NanoThief_DP_Listener implements AdvanceableListener {
    private static Logger log = Global.getLogger(NanoThief_DP_Listener.class);
    private static final String key = "AbyssalXO_NanoThief_DP_Listener";
    private ShipAPI ship;
    private float time=0;
    private float hullPerSecond;
    private float CRPerSecond;
    private float PPTPerSecond;
    private float fighterPerSecond;

    private final float maxCR;
    public NanoThief_DP_Listener(ShipAPI ship){
        this.ship = ship;
        maxCR = Math.max(0.7f,ship.getCRAtDeployment());
        //maxCR = Math.min(1f,ship.getMutableStats().getMaxCombatReadiness().getModifiedValue());
        log.info("got max CR as: "+maxCR);

    }
    public void addPower(float hullPerSecond, float CRPerSecond, float PPTPerSecond, float fighterPerSecond){
        float multi = 0;
        if (this.time >= 0.1) multi = 1-(this.time / NanoThief_8.maxTime);//1 / 10 = 0.1 = 0.9. // 5/10 = 0.5 = 0.5. // 9/10 = 0.9 = 0.1.
        multi = Math.max(multi,0);
        this.hullPerSecond = (this.hullPerSecond*multi)+hullPerSecond;
        this.CRPerSecond = (this.CRPerSecond*multi)+CRPerSecond;
        this.PPTPerSecond = (this.PPTPerSecond*multi)+PPTPerSecond;
        this.fighterPerSecond = (this.fighterPerSecond*multi)+fighterPerSecond;
        this.time = 0;
    }
    @Override
    public void advance(float amount) {
        displayIfPlayer();
        time+=amount;
        float mutli = amount;
        if (time >= NanoThief_8.maxTime){
            mutli = NanoThief_8.maxTime-(time-amount);
            if (mutli <= 0){
                ship.getListenerManager().removeListener(this);
                return;
            }
        }
        //log.info("got multi as: "+mutli);
        float hp = Math.min(ship.getMaxHitpoints(), ship.getHitpoints() + (this.hullPerSecond*mutli));
        //log.info("  got hp as: "+hp);
        ship.setHitpoints(hp);
        float cr = Math.min(maxCR, ship.getCurrentCR() + (this.CRPerSecond*mutli));
        //log.info("  got cr as: "+cr);
        ship.setCurrentCR(cr);

        float ppt = ship.getTimeDeployedForCRReduction() - (this.PPTPerSecond*mutli);
        ppt = Math.max(0,ppt);
        //log.info("  got ppt as: "+ppt);
        //log.info("  got time lest as: "+ship.getPeakTimeRemaining());
        //log.info("  got max time as: "+ship.getTimeDeployedForCRReduction());
        //log.info("  got time deployed as: "+ship.getTimeDeployedUnderPlayerControl());
        ship.setTimeDeployed(ppt);//ship.getTimeDeployedForCRReduction() - ppt);
        //log.info("  after modification, got time left as: "+ship.getPeakTimeRemaining());
        for (FighterLaunchBayAPI a : ship.getLaunchBaysCopy()){
            float fighter = Math.min(1, a.getCurrRate() + (this.fighterPerSecond*mutli));
            a.setCurrRate(fighter);
        }
    }
    public void displayIfPlayer(){

    }
}
