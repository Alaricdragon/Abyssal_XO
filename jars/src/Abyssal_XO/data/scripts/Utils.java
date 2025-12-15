package Abyssal_XO.data.scripts;

import com.fs.starfarer.api.util.Misc;
import org.lwjgl.util.vector.Vector2f;

public class Utils {

    public static float getDistance(float x, float y, Vector2f loc2){
        return Misc.getDistance(new Vector2f(x,y),loc2);
    }
    public static double getExpenseValue(double value,double maxValue,double increments,double speedPerValue){
        //double cr2 = (cr*100);
        //System.out.println("    cr: "+cr2);
        double a = ((maxValue+increments)-value) /increments;//so max cr is 1, -10 is 2, -20 is 4, -30 is 8.
        //System.out.println("    a: "+a);
        return speedPerValue *(Math.pow(2,a)/2);
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
