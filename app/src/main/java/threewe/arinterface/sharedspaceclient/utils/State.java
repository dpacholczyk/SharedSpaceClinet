package threewe.arinterface.sharedspaceclient.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import threewe.arinterface.sharedspaceclient.models.Marker;
import threewe.arinterface.sharedspaceclient.models.Session;

/**
 * Created by dpach on 05.11.2016.
 */

public class State {
    // list of available markers in current session
    public static List<Marker> availableMarkers = new ArrayList<Marker>();

    public static Session currentSession = null;

    private static String currentId = "DAWID";

    public static String getCurrentId() {
        Log.d("POLACZENIE", currentId);
        if(currentId == null) {
            Long tsLong = System.currentTimeMillis()/1000;
            String ts = tsLong.toString();
            currentId = ts;
        }

        return currentId;
    }
}
