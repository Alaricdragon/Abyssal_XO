package Abyssal_XO.data.scripts;

import com.fs.starfarer.api.Global;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Settings {
    public static final String SIC_CONTROL_HULLMOD = "Abussal_XO_SIC_controler";//"sc_skill_controller";
    public static final String MEMKEY_NANOTHIEF_STATUS = "$Abyssal_XO_GOT_NANOTHIEF";
    public static final String MEMKEY_NANOTHIEF_BOSSFLEET = "$Abyssal_XO_NF_BOSS";
    public static final String MEMKEY_NANOTHIEF_BOSSSCRIPT = "$Abyssal_XO_NF_BOSS_SCRIPT";

    public static Logger log = Global.getLogger(Settings.class);
    public static final int[] NANO_THIEF_RECLAIM_GAIN = {
            200,
            400,
            600,
            800
    };
    @Deprecated
    public static final int NANO_THIEF_MINRANGEOFWING = 2000;

    @Deprecated
    public static final int NANO_THIEF_BASESWARM_COST = 175;
    @Deprecated
    public static final int NANO_THIEF_BASESWARM_BUILDTIME = 10;
    @Deprecated
    public static final int NANO_THIEF_BASESWARM_TTL = 120;//swrams get exstea base TTL because they already die from shoting wepons.


    @Deprecated
    public static final int NANO_THIEF_ReclaimPerControl_BASE = 1000;
    @Deprecated
    public static final float NANO_THIEF_CustomSwarm_COST_BASE = 20;//swarms cost 100 at 10 op, 200 at 20, 300 at 30.
    @Deprecated
    public static final float NANO_THIEF_CustomSwarm_COST_PEROP = 10;//swarms cost 100 at 10 op, 200 at 20, 300 at 30.
    @Deprecated
    public static final float NANO_THIEF_CustomSwarm_BUILDTIME_PREREFIT = 0.5f;//swarms build 3 times faster then they should
    @Deprecated
    public static final float NANO_THEEF_CustomSwarm_RefundPercent = 0.5f;
    @Deprecated
    public static final float NANO_THEEF_CustomSwarm_RefundPercent_Bomber = 0.3f;
    @Deprecated
    public static final int NANO_THIEF_CustomSwarm_TTL = 60;//swrams get exstea base TTL because they already die from shoting wepons.
    @Deprecated
    public static final int NANO_THIEF_CustomSwarm_Bomber_TTL = 30;

    public static final double NANO_THIEF_RECLAIM_GATHER_TIME = 3;

    public static final String NANO_THIEF_ABILITY = "Abyssal_XO_NanoThief_setFighters";
    public static final String NANO_THIEF_ABILITY_NAME = "Adjust NanoThief Settings";
    public static final String NANO_THIEF_CUSTOM_WING_ATK_MEMORY_KEY = "$Abyssal_XO_NANO_THIEF_CUSTOMWING";
    public static final String NANO_THIEF_CUSTOM_WING_DEF_MEMORY_KEY = "$Abyssal_XO_NANO_THIEF_CUSTOMWING_DEF";
    public static final String NANO_THIEF_CUSTOM_MASTERY_MEMORY_KEY = "$Abyssal_XO_NANO_MASTERY_SHIP";
    public static final String NANO_THIEF_CUSTOM_MASTERY_NUMBERS_MEMORY_KEY = "$Abyssal_XO_NANO_MASTERY_NUMBERS";
    public static final String NANO_THIEF_CUSTOM_MASTERY_NAMES_MEMORY_KEY = "$Abyssal_XO_NANO_MASTERY_NAMES";
   // public static String NANO_THIEF_CUSTOM_MASTERY_RECLAIM_MEMERY_KEY = "$Nano_Thief_ReclaimOverride";
    public static final String NANO_THIEF_SKILL_4_ACTIVE_KEY = "Abyssal_XO_Skill_4_Active";
    public static final String NANO_THIEF_RECLAIM_TARGET_KEY = "Abyssal_XO_Nanothief_ReclaimTarget_";
    public static final String NANO_THIEF_SIC_HULLMOD_DATA_KEY = "Abyssal_XO_dataMemoryKey";
    public static final String NANO_THIEF_MASTERY_BASESHIP = "kite_pirates_Raider";
    public static final String NANO_THIEF_BASEWING = "attack_swarm_wing";//"attack_swarm_wing";
    public static final String NANO_THIEF_PALYER_BASEWING = "warthog_wing";//"broadsword_wing";
    public static final String DISPLAYID_NANOTHIEF = "Abyssal_XO_DisplayKey";
    public static final String HULLMOD_CENTRAL_FAB = "Abyssal_XO_CF";
    public static final String TAG_HASRECLAMED = "Abyssal_XO_NanoThief_HasReclamed";
    public static final String NANO_THIEF_CREATER_SHIP = "Abyssal_XO_ReclaimCore_Blank";
    public static ArrayList<String> NanoThief_Users = new ArrayList<>();
    public static ArrayList<String> NanoThief_Banned = new ArrayList<>();

    public static final String TAG_NO_RECLAIM_ON_BUILD = "Abyssal_XO_NoReclaimOnBuild";

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
