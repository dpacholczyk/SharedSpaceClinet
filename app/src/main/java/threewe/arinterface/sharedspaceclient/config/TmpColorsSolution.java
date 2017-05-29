package threewe.arinterface.sharedspaceclient.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Dawid Pacholczyk <dpacholczyk@outlook.com> on 28.01.2017.
 * This is a temporary solution for list of colors that can be used for color picking
 */

public class TmpColorsSolution {
    public static Map<String, int[]> allowedColors;

    public TmpColorsSolution() {
        allowedColors = new TreeMap<>();
        int[] color = new int[3];
        color[0] = 255;
        color[1] = 0;
        color[2] = 0;
        allowedColors.put("red", color);

        color = new int[3];
        color[0] = 0;
        color[1] = 255;
        color[2] = 0;
        allowedColors.put("green", color);

        color = new int[3];
        color[0] = 0;
        color[1] = 0;
        color[2] = 255;
        allowedColors.put("blue", color);
    }

    public static boolean isColorPicked(String key) {
        return allowedColors.containsKey(key);
    }
}
