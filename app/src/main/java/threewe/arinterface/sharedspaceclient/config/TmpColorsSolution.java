package threewe.arinterface.sharedspaceclient.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dawid Pacholczyk <dpacholczyk@outlook.com> on 28.01.2017.
 * This is a temporary solution for list of colors that can be used for color picking
 */

public class TmpColorsSolution {
    public static List<int[]> allowedColors;

    public TmpColorsSolution() {
        allowedColors = new ArrayList<int[]>();
        int[] color = new int[3];
        color[0] = 255;
        color[1] = 0;
        color[2] = 0;
        allowedColors.add(color);

        color = new int[3];
        color[0] = 0;
        color[1] = 255;
        color[2] = 0;
        allowedColors.add(color);

        color = new int[3];
        color[0] = 0;
        color[1] = 0;
        color[2] = 255;
        allowedColors.add(color);
    }
}
