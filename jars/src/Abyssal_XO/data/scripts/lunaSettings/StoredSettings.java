package Abyssal_XO.data.scripts.lunaSettings;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_10;
import com.fs.starfarer.api.Global;
import lunalib.lunaSettings.LunaSettings;

public class StoredSettings {
    public static void getSettings(){
        if (Global.getSettings().getModManager().isModEnabled("lunalib")){
            getLunaSettings();
        }else {
            getConfigSettings();
        }
    }
    public static void attemptEnableLunalib(){
        if (!Global.getSettings().getModManager().isModEnabled("lunalib")) return;
        LunaSettings.addSettingsListener(new ApplySettingsOnChange());
    }
    private static void getLunaSettings(){
        NanoThief_10.maxShips = LunaSettings.getInt("Abyssal_XO","NA_Mastery_maxShips");
        NanoThief_10.maxNumberForNPC = NanoThief_10.maxShips;
        NanoThief_10.canFrigate = LunaSettings.getBoolean("Abyssal_XO","NA_Mastery_canFrigate");
        NanoThief_10.canDestroyer = LunaSettings.getBoolean("Abyssal_XO","NA_Mastery_canDestroyer");
        NanoThief_10.canCruiser = LunaSettings.getBoolean("Abyssal_XO","NA_Mastery_canCruiser");
        NanoThief_10.canCapital = LunaSettings.getBoolean("Abyssal_XO","NA_Mastery_canCapital");
        NanoThief_10.allowedSizesForNPC = new boolean[]{NanoThief_10.canFrigate,NanoThief_10.canDestroyer, NanoThief_10.canCruiser, NanoThief_10.canCapital};
        NanoThief_10.allowNPCFabricators = LunaSettings.getBoolean("Abyssal_XO","NA_Mastery_canFabricator");
        int temp = LunaSettings.getInt("Abyssal_XO","NA_Mastery_oddsVariance");
        NanoThief_10.maxOddsNPC = temp+1;
        NanoThief_10.sModCost = LunaSettings.getDouble("Abyssal_XO","NA_Mastery_sModCost");
        NanoThief_10.dModDiscount = LunaSettings.getDouble("Abyssal_XO","NA_Mastery_dModDiscount");
        NanoThief_10.dModmin = LunaSettings.getDouble("Abyssal_XO","NA_Mastery_dModmin");
        NanoThief_10.costPerDP = LunaSettings.getInt("Abyssal_XO","NA_Mastery_costPerDP");
        NanoThief_10.rechargeTimePerDP = LunaSettings.getDouble("Abyssal_XO","NA_Mastery_rechargeTimePerDP");
        NanoThief_10.buildTimePerDP = LunaSettings.getDouble("Abyssal_XO","NA_Mastery_buildTimePerDP");
        NanoThief_10.minCR = LunaSettings.getDouble("Abyssal_XO","NA_Mastery_minCR");
        NanoThief_10.peakCRDuration = LunaSettings.getDouble("Abyssal_XO","NA_Mastery_peakCRDuration");
        NanoThief_10.maxReclaimPercent = LunaSettings.getDouble("Abyssal_XO","NA_Mastery_maxReclaimPercent");
    }
    /// not doing this because I am lazy. maybe later
    private static void getConfigSettings(){

        //EscapeHullMod.maxJumps = Global.getSettings().getInt("EmergencyEscape_MaxJumps");
    }
}
