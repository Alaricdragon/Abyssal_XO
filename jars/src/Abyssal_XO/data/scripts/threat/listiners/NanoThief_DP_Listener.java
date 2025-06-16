package Abyssal_XO.data.scripts.threat.listiners;

import Abyssal_XO.data.scripts.threat.Nano_Thief_Stats;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_8;
import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.combat.FighterLaunchBayAPI;
import com.fs.starfarer.api.combat.ShipAPI;
import com.fs.starfarer.api.combat.listeners.AdvanceableListener;
import org.apache.log4j.Logger;

public class NanoThief_DP_Listener implements AdvanceableListener {
    private static Logger log = Global.getLogger(NanoThief_DP_Listener.class);
    private ShipAPI ship;
    private float time=0;
    private float hullPerSecond;
    private float CRPerSecond;
    private float PPTPerSecond;
    private float fighterPerSecond;

    private final float maxCR;
    public NanoThief_DP_Listener(ShipAPI ship){
        this.ship = ship;
        maxCR = Math.max(1f,ship.getCRAtDeployment());

    }
    public void addPower(float hullPerSecond, float CRPerSecond, float PPTPerSecond, float fighterPerSecond){
        float multi = 0;
        if (this.time >= 0.1) multi = this.time / NanoThief_8.maxTime;
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
        log.info("got multi as: "+mutli);
        float hp = Math.min(ship.getMaxHitpoints(), ship.getHitpoints() + (this.hullPerSecond*mutli));
        log.info("  got hp as: "+hp);
        ship.setHitpoints(hp);
        float cr = Math.min(maxCR, ship.getCurrentCR() + (this.CRPerSecond*mutli));
        log.info("  got cr as: "+cr);
        ship.setCurrentCR(cr);

        float ppt = ship.getFullTimeDeployed() - (this.PPTPerSecond*mutli);
        log.info("  got ppt as: "+ppt);
        //float ppt = Math.min(ship.getTimeDeployedForCRReduction(), ship.getPeakTimeRemaining() + (this.PPTPerSecond*mutli));
        ship.setTimeDeployed(ppt);
        for (FighterLaunchBayAPI a : ship.getLaunchBaysCopy()){
            float fighter = Math.min(1, a.getCurrRate() + (this.fighterPerSecond*mutli));
            a.setCurrRate(fighter);
        }
        //ship.setFighterTimeBeforeRefit(cr);
        //ship.getChildModulesCopy();//note: this type of stat will be applied in the thing itself (so each modal will get its own listener.)
    }
    public void displayIfPlayer(){

    }
}
