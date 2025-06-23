package Abyssal_XO.data.scripts;

import com.fs.starfarer.api.Global;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Settings {
    public static final int[] NANO_THIEF_RECLAIM_GAIN = {
            1000,
            2000,
            3000,
            4000
    };
    public static final int NANO_THIEF_MINRANGEOFWING = 2000;

    public static final int NANO_THIEF_BASESWARM_COST = 175;
    public static final int NANO_THIEF_BASESWARM_BUILDTIME = 5;
    public static final int NANO_THIEF_BASESWARM_TTL = 120;//swrams get exstea base TTL because they already die from shoting wepons.


    public static final int NANO_THIEF_ReclaimPerControl_BASE = 1000;
    public static final float NANO_THIEF_CustomSwarm_COST_BASE = 20;//swarms cost 100 at 10 op, 200 at 20, 300 at 30.
    public static final float NANO_THIEF_CustomSwarm_COST_PEROP = 10;//swarms cost 100 at 10 op, 200 at 20, 300 at 30.
    public static final float NANO_THIEF_CustomSwarm_BUILDTIME_PREREFIT = 0.5f;//swarms build 3 times faster then they should
    public static final int NANO_THIEF_CustomSwarm_TTL = 60;//swrams get exstea base TTL because they already die from shoting wepons.
    public static final int NANO_THIEF_CustomSwarm_Bomber_TTL = 30;

    public static final String NANO_THIEF_ABILITY = "Abyssal_XO_NanoThief_setFighters";
    public static final String NANO_THIEF_CUSTOM_WING_MEMORY_KEY = "$Abyssal_XO_NANO_THIEF_CUSTOMWING";
    public static final String NANO_THIEF_BASEWING = "attack_swarm_wing";//"broadsword_wing";
    public static final String NANO_THIEF_PALYER_BASEWING = "talon_wing";//"broadsword_wing";
    public static final String DISPLAYID_NANOTHIEF = "Abyssal_XO_DisplayKey";
    public static final String HULLMOD_CENTRAL_FAB = "Abyssal_XO_CF";
    public static final String TAG_HASRECLAMED = "Abyssal_XO_NanoThief_HasReclamed";
    public static final String NANO_THIEF_CREATER_SHIP = "Abyssal_XO_ReclaimCore_Blank";
    public static ArrayList<String> NanoThief_Users = new ArrayList<>();
    public static ArrayList<String> NanoThief_Banned = new ArrayList<>();
    public static void init() throws JSONException {
        JSONArray array = Global.getSettings().getJSONArray("NanoThief_Users");
        NanoThief_Users = new ArrayList<>();
        for (int a  = 0; a < array.length(); a++){
            NanoThief_Users.add(array.getString(a));
        }

        array = Global.getSettings().getJSONArray("NanoThief_BanndedHullmodsFromAcceptingReclaim");
        NanoThief_Banned = new ArrayList<>();
        for (int a  = 0; a < array.length(); a++){
            NanoThief_Banned.add(array.getString(a));
        }
    }
}
