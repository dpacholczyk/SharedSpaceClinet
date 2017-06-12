package threewe.arinterface.sharedspaceclient.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import threewe.arinterface.sharedspaceclient.config.ActivityType;
import threewe.arinterface.sharedspaceclient.config.URLs;
import threewe.arinterface.sharedspaceclient.models.Marker;
import threewe.arinterface.sharedspaceclient.models.Session;
import threewe.arinterface.sharedspaceclient.models.Structure;
import threewe.arinterface.sharedspaceclient.models.User;

/**
 * Created by Dawid Pacholczyk <dpacholczyk@outlook.com> on 05.11.2016.
 */

public class State {
    // list of available markers in current session
    public static List<Marker> availableMarkers = new ArrayList<Marker>();

    public static Session currentSession = null;

    public static User currentUser = null;

    public static ActivityType selectedMode = null;

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

    public static void shareState(ActivityType activityType, Structure structure) {
        Map<String, Object> activity = new HashMap<>();
        activity.put("type", activityType);
        activity.put("structure", structure.id);
        activity.put("session", State.currentSession.id);

        URLUtils.postRequest(URLs.STATE_SYNC_URL, activity);
    }

}
