package Abyssal_XO.data.scripts.listiners;

import com.fs.starfarer.api.EveryFrameScript;
import com.fs.starfarer.api.util.Misc;

public abstract class DelayedScript implements EveryFrameScript {
    private float maxTime=9999;
    private boolean done = false;
    public DelayedScript(float minDays, float maxDays){
        maxTime = (float) ((Math.random() * (maxDays - minDays)) + minDays);
    }
    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public boolean runWhilePaused() {
        return false;
    }

    @Override
    public void advance(float amount) {
        maxTime-=Misc.getDays(amount);
        if (maxTime <= 0){
            ActivateCode();
            done = true;
        }
    }
    public abstract void ActivateCode();
}
