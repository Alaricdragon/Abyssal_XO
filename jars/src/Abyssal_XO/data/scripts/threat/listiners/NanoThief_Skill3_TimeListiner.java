package Abyssal_XO.data.scripts.threat.listiners;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_3;
import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.Global;

public class NanoThief_Skill3_TimeListiner implements EveryFrameScript {
    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public boolean runWhilePaused() {
        return false;
    }
    float maxTime = 1;
    boolean done = false;
    @Override
    public void advance(float amount) {
        //Settings.log.info("(time listener) running an instance of time lisinter...");
        maxTime-=amount;
        if (maxTime <= 0){
            //Settings.log.info("(time listener) activating the intel message!...");
            NanoThief_3.sendIntelMessage();
            Global.getSector().getListenerManager().removeListener(this);
            done = true;
        }
    }
}
