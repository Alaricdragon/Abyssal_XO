package Abyssal_XO.data.scripts;

import com.fs.starfarer.api.Global;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class Settings {
    public static final String HULLMOD_CENTRAL_FAB = "";
    public static final String TAG_HASRECLAMED = "Sic_NanoThief_HasReclamed";
    public static ArrayList<String> NanoThief_Users = new ArrayList<>();
    public static void init() throws JSONException {
        JSONArray array = Global.getSettings().getJSONArray("NanoThief_Users");
        NanoThief_Users = new ArrayList<>();
        for (int a  = 0; a < array.length(); a++){
            NanoThief_Users.add(array.getString(a));
        }
    }
}
