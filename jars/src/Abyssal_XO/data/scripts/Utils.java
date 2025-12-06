package Abyssal_XO.data.scripts;

import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

public class Utils {

    public static float getDistance(float x, float y, Vector2f loc2){
        return Misc.getDistance(new Vector2f(x,y),loc2);
    }
    /*public static float getDistance(Vector2f loc,Vector2f loc2){
        //float angle = VectorUtils.getAngle(loc, loc2);
        float x2 = loc.x - loc2.x;
        x2 = Math.max(x2, -x2);
        float y2 = loc.y - loc2.y;
        y2 = Math.max(y2, -y2);
        float d = x2+y2;
        return d;
    }*/
}
