package Abyssal_XO.data.scripts.utility;

import com.fs.starfarer.api.Global;
import com.fs.starfarer.api.campaign.CampaignEventListener;

import java.util.ArrayList;

public abstract class CleanListTemp {
    public CleanListTemp() {
        start();
    }
    /// If this object should be removed or not.
    public abstract boolean shouldClean(Object a);
    /// should run a list, were 'process' is called on each item you check
    public abstract void runList();
    /// should remove the giving item from the list.
    public abstract void remove(Object a);
    private ArrayList<Object> toRemove = new ArrayList<>();
    public void process(Object a){
        if (shouldClean(a)) toRemove.add(a);
    }
    private void start(){
        runList();
        for (Object a : toRemove) remove(a);
    }
}
