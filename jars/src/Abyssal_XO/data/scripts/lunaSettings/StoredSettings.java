package Abyssal_XO.data.scripts.lunaSettings;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_10;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_9;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_Base;
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
        luna_NA_Base();
        //-
        luna_NA_Desprate();
        luna_NA_Mastery();
    }
    private static void luna_NA_Base(){
        NanoThief_Base.reclaimOnStartPerDP = LunaSettings.getInt("Abyssal_XO","NA_BASE_reclaimOnStartPerDP");
        NanoThief_Base.reclaimFromHostilePerDP = LunaSettings.getInt("Abyssal_XO","NA_BASE_reclaimFromHostilePerDP");
        NanoThief_Base.reclaimMultiFromParent = LunaSettings.getDouble("Abyssal_XO","NA_BASE_reclaimMultiFromParent");
        NanoThief_Base.reclaimRecyclePercent = LunaSettings.getDouble("Abyssal_XO","NA_BASE_reclaimRecyclePercent");
    }

    private static void luna_NA_Desprate(){
        NanoThief_9.crStart = LunaSettings.getDouble("Abyssal_XO","NA_DM_crStart");
        NanoThief_9.crReginSpeed = LunaSettings.getDouble("Abyssal_XO","NA_DM_crReginSpeed");
        NanoThief_9.crSkillSpeed = LunaSettings.getDouble("Abyssal_XO","NA_DM_crSkillSpeed");
        NanoThief_9.crSkillCost = LunaSettings.getDouble("Abyssal_XO","NA_DM_crSkillCost");
        NanoThief_9.reclaimCostPerCR = LunaSettings.getInt("Abyssal_XO","NA_DM_reclaimCostPerCR") * 100;//its completed. please don't ask....
        NanoThief_9.hpSpeed = LunaSettings.getDouble("Abyssal_XO","NA_DM_hpSpeed");
        NanoThief_9.hpCost = LunaSettings.getDouble("Abyssal_XO","NA_DM_hpCost");
        NanoThief_9.overloadMinTime = LunaSettings.getDouble("Abyssal_XO","NA_DM_overloadMinTime");
        NanoThief_9.overloadRemoved = LunaSettings.getDouble("Abyssal_XO","NA_DM_overloadRemoved");
        NanoThief_9.overloadCostPerSecond = LunaSettings.getInt("Abyssal_XO","NA_DM_overloadCostPerSecond");
        NanoThief_9.overloadEffectTime = LunaSettings.getInt("Abyssal_XO","NA_DM_overloadEffectTime");
        NanoThief_9.overloadSkillCost = LunaSettings.getDouble("Abyssal_XO","NA_DM_overloadSkillCost");
        NanoThief_9.overloadSkillSpeed = LunaSettings.getDouble("Abyssal_XO","NA_DM_overloadSkillSpeed");

    }
    private static void luna_NA_Mastery(){
        NanoThief_10.maxShips = LunaSettings.getInt("Abyssal_XO","NA_Mastery_maxShips");
        NanoThief_10.maxNumberForNPC = NanoThief_10.maxShips;
        NanoThief_10.canFrigate = LunaSettings.getBoolean("Abyssal_XO","NA_Mastery_canFrigate");
        NanoThief_10.canDestroyer = LunaSettings.getBoolean("Abyssal_XO","NA_Mastery_canDestroyer");
        NanoThief_10.canCruiser = LunaSettings.getBoolean("Abyssal_XO","NA_Mastery_canCruiser");
        NanoThief_10.canCapital = LunaSettings.getBoolean("Abyssal_XO","NA_Mastery_canCapital");
        NanoThief_10.allowedSizesForNPC = new boolean[]{NanoThief_10.canFrigate,NanoThief_10.canDestroyer, NanoThief_10.canCruiser, NanoThief_10.canCapital};
        NanoThief_10.allowNPCFabricators = LunaSettings.getBoolean("Abyssal_XO","NA_Mastery_canFabricator");
        NanoThief_10.fabricatorDPOverride = LunaSettings.getInt("Abyssal_XO","NA_Mastery_fabricatorDPOverride");
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
