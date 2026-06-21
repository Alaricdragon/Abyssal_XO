package Abyssal_XO.data.scripts.threat.dialogPlugin;

import Abyssal_XO.data.scripts.Settings;
import Abyssal_XO.data.scripts.threat.skills.NanoThief_7;
import com.fs.starfarer.api.loading.FighterWingSpecAPI;

public class Nano_Thief_Selection_Sfw_Def_CargoListiner extends Nano_Thief_Selection_Sfw_Attack_CargoListiner {
    @Override
    protected String getMemoryKey() {
        return Settings.NANO_THIEF_CUSTOM_WING_DEF_MEMORY_KEY;
    }
    public static boolean canUseFighter(FighterWingSpecAPI spec){
        for (String a : NanoThief_7.bannedTags){
            if (spec.hasTag(a)) return false;
        }
        return true;
    }
}
