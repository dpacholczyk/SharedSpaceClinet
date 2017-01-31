package edu.dhbw.andar.util;

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

    static {
        availableColors = new ArrayList<>();
        availableColors.add("Green");
        availableColors.add("Blue");
        availableColors.add("Ted");
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
            method = cls.getDeclaredMethod("methodName", Integer.class);
            boolean answer = (boolean)method.invoke(obj, red, green, blue);
            if(answer) {
                return color.toLowerCase();
            }
        }

        return "none";
    }

    public static boolean isGreen(int red, int green, int blue) {
        if(green >= 100 && red < 100 && blue < 100) {
            return true;
        }

        return false;
    }

    public static boolean isBlue(int red, int green, int blue) {
        if(blue >= 100 && red < 100 && green < 100) {
            return true;
        }

        return false;
    }

    public static boolean isRed(int red, int green, int blue) {
        if(red >= 100 && green < 100 && blue < 100) {
            return true;
        }

        return false;
    }

    public static boolean isPurple(int red, int green, int blue) {
        if(red >= 100 && blue >= 100 && green < 100) {
            return true;
        }

        return false;
    }

    public static boolean isTeal(int red, int green, int blue) {
        if(green >= 100 && blue >= 100 && red < 100) {
            return true;
        }

        return false;
    }

    public static boolean isYellow(int red, int green, int blue) {
        if(red >= 100 && blue >= 100 && green < 100) {
            return true;
        }

        return false;
    }

}
