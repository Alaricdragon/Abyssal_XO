package Abyssal_XO.data.scripts.lunaSettings;
import Abyssal_XO.data.scripts.threat.skills.*;
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
        luna_NA_ScrapworkFortifications();
        luna_NA_DescentralizedSwarms();
        luna_NA_DefensiveSwarms();
        luna_NA_CentralizedProduction();
        luna_NA_Desprate();
        luna_NA_Mastery();
    }
    private static void luna_NA_Base(){
        NanoThief_Base.reclaimOnStartPerDP = LunaSettings.getInt("Abyssal_XO","NA_BASE_reclaimOnStartPerDP");
        NanoThief_Base.reclaimFromHostilePerDP = LunaSettings.getInt("Abyssal_XO","NA_BASE_reclaimFromHostilePerDP");
        NanoThief_Base.reclaimMultiFromParent = LunaSettings.getDouble("Abyssal_XO","NA_BASE_reclaimMultiFromParent");
        NanoThief_Base.reclaimRecyclePercent = LunaSettings.getDouble("Abyssal_XO","NA_BASE_reclaimRecyclePercent");
    }
    private static void luna_NA_ScrapworkFortifications(){
        NanoThief_4.activeDamage = LunaSettings.getInt("Abyssal_XO","NA_SF_activeDamage");
        NanoThief_4.activePercent = LunaSettings.getDouble("Abyssal_XO","NA_SF_activePercent");
        NanoThief_4.activeTime = LunaSettings.getDouble("Abyssal_XO","NA_SF_activeTime");
        NanoThief_4.resistance = LunaSettings.getDouble("Abyssal_XO","NA_SF_resistance");
        NanoThief_4.time = LunaSettings.getDouble("Abyssal_XO","NA_SF_time");
        NanoThief_4.cooldown = LunaSettings.getDouble("Abyssal_XO","NA_SF_cooldown");
        NanoThief_4.activeCost = LunaSettings.getInt("Abyssal_XO","NA_SF_activeCost");
        NanoThief_4.damagePerCost = LunaSettings.getInt("Abyssal_XO","NA_SF_damagePerCost");

    }
    private static void luna_NA_DescentralizedSwarms(){
        NanoThief_6.CustomSwarm_COST_BASE = LunaSettings.getInt("Abyssal_XO","NA_DSP_CustomSwarm_COST_BASE");
        NanoThief_6.CustomSwarm_COST_PEROP = LunaSettings.getInt("Abyssal_XO","NA_DSP_CustomSwarm_COST_PEROP");
        NanoThief_6.CustomSwarm_BUILDTIME_PREREFIT = LunaSettings.getDouble("Abyssal_XO","NA_DSP_CustomSwarm_BUILDTIME_PREREFIT");
        NanoThief_6.CustomSwarm_RefundPercent = LunaSettings.getDouble("Abyssal_XO","NA_DSP_CustomSwarm_RefundPercent");
        NanoThief_6.CustomSwarm_RefundPercent_Bomber = LunaSettings.getDouble("Abyssal_XO","NA_DSP_CustomSwarm_RefundPercent_Bomber");

        NanoThief_6.dpPerFighters = LunaSettings.getInt("Abyssal_XO","NA_DSP_dpPerFighters");
        NanoThief_6.MINRANGEOFWING = LunaSettings.getInt("Abyssal_XO","NA_DSP_MINRANGEOFWING");
        NanoThief_6.speedPerSize[0] = LunaSettings.getDouble("Abyssal_XO","NA_DSP_speedPerSize_0");
        NanoThief_6.speedPerSize[1] = LunaSettings.getDouble("Abyssal_XO","NA_DSP_speedPerSize_1");
        NanoThief_6.speedPerSize[2] = LunaSettings.getDouble("Abyssal_XO","NA_DSP_speedPerSize_2");
        NanoThief_6.speedPerSize[3] = LunaSettings.getDouble("Abyssal_XO","NA_DSP_speedPerSize_3");
    }
    private static void luna_NA_DefensiveSwarms(){
        NanoThief_7.numPerSize[0] = LunaSettings.getInt("Abyssal_XO","NA_DS_numPerSize_0");
        NanoThief_7.numPerSize[1] = LunaSettings.getInt("Abyssal_XO","NA_DS_numPerSize_1");
        NanoThief_7.numPerSize[2] = LunaSettings.getInt("Abyssal_XO","NA_DS_numPerSize_2");
        NanoThief_7.numPerSize[3] = LunaSettings.getInt("Abyssal_XO","NA_DS_numPerSize_3");
        NanoThief_7.speedPerSize[0] = LunaSettings.getDouble("Abyssal_XO","NA_DS_speedPerSize_0");
        NanoThief_7.speedPerSize[1] = LunaSettings.getDouble("Abyssal_XO","NA_DS_speedPerSize_1");
        NanoThief_7.speedPerSize[2] = LunaSettings.getDouble("Abyssal_XO","NA_DS_speedPerSize_2");
        NanoThief_7.speedPerSize[3] = LunaSettings.getDouble("Abyssal_XO","NA_DS_speedPerSize_3");

    }
    private static void luna_NA_CentralizedProduction(){
        NanoThief_8.reclaimRaito = LunaSettings.getDouble("Abyssal_XO","NA_CR_reclaimRaito");
        NanoThief_8.sModBonus = LunaSettings.getDouble("Abyssal_XO","NA_CR_smod");
        NanoThief_8.reclaimPerSecondBase = LunaSettings.getInt("Abyssal_XO","NA_CR_reclaimPerSecondBase");
        NanoThief_8.reclaimPerSecondPerBost = LunaSettings.getInt("Abyssal_XO","NA_CR_reclaimPerSecondPerBost");
        NanoThief_8.reclaimPerSpeedBost = LunaSettings.getInt("Abyssal_XO","NA_CR_reclaimPerSpeedBost");
        NanoThief_8.speedMod = LunaSettings.getDouble("Abyssal_XO","NA_CR_speedMod");
        NanoThief_8.keeptReclaim = LunaSettings.getInt("Abyssal_XO","NA_CR_keeptReclaim");
        NanoThief_8.keeptReclaimAbility = LunaSettings.getDouble("Abyssal_XO","NA_CR_keeptReclaimAbility");
        NanoThief_8.baseReclaimEfficiencyMod = LunaSettings.getDouble("Abyssal_XO","NA_CR_baseReclaimEfficiencyMod");
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
