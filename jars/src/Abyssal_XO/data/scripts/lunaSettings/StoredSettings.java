package Abyssal_XO.data.scripts.lunaSettings;
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
        //EscapeHullMod.maxJumps = LunaSettings.getInt("emergency_escape","maxShips");
    }
    /// not doing this because I am lazy. maybe later
    private static void getConfigSettings(){

        //EscapeHullMod.maxJumps = Global.getSettings().getInt("EmergencyEscape_MaxJumps");
    }
}
