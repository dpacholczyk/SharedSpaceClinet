package edu.dhbw.andar.util;

import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawid Pacholczyk <dpacholczyk@outlook.com>< on 31.01.2017.
 * Temporary set of methods for color picking. It is not 100% color picking but only a substitute
 */

public class ColorTools {
    protected static List<String> availableColors;

    public static final String FOR_ACTIVATION = "green";

    static {
        availableColors = new ArrayList<>();
        availableColors.add("Green");
        availableColors.add("Blue");
        availableColors.add("Red");
        availableColors.add("Purple");
        availableColors.add("Teal");
        availableColors.add("Yellow");
    }

    public static String getColor(int red, int green, int blue) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method;
        Object obj = new ColorTools();
        Class cls = Class.forName("edu.dhbw.andar.util.ColorTools");
        for(String color : availableColors) {
            String methodName = "is" + color;
            Log.d("SCREENSHOT", "method: " + methodName);
            method = cls.getDeclaredMethod(methodName, Integer.class, Integer.class, Integer.class);
            boolean answer = (boolean)method.invoke(obj, new Integer(red), new Integer(green), new Integer(blue));
            Log.d("SCREENSHOT", "answer: " + answer);
            if(answer) {
                return color.toLowerCase();
            }
        }

        return "none";
    }

    public static boolean isGreen(Integer red, Integer green, Integer blue) {
        if(green >= 100 && red < 100 && blue < 100) {
            return true;
        }

        return false;
    }

    public static boolean isBlue(Integer red, Integer green, Integer blue) {
        if(blue >= 100 && red < 100 && green < 100) {
            return true;
        }

        return false;
    }

    public static boolean isRed(Integer red, Integer green, Integer blue) {
        if(red >= 100 && green < 100 && blue < 100) {
            return true;
        }

        return false;
    }

    public static boolean isPurple(Integer red, Integer green, Integer blue) {
        if(red >= 100 && blue >= 100 && green < 100) {
            return true;
        }

        return false;
    }

    public static boolean isTeal(Integer red, Integer green, Integer blue) {
        if(green >= 100 && blue >= 100 && red < 100) {
            return true;
        }

        return false;
    }

    public static boolean isYellow(Integer red, Integer green, Integer blue) {
        if(red >= 100 && blue >= 100 && green < 100) {
            return true;
        }

        return false;
    }

}
