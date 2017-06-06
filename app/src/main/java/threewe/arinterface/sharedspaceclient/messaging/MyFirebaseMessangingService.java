package threewe.arinterface.sharedspaceclient.messaging;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

import threewe.arinterface.sharedspaceclient.actions.Action;
import threewe.arinterface.sharedspaceclient.actions.ActionPicker;
import threewe.arinterface.sharedspaceclient.config.ActionType;

import threewe.arinterface.sharedspaceclient.MenuActivity;
import threewe.arinterface.sharedspaceclient.models.Marker;
import threewe.arinterface.sharedspaceclient.utils.State;

/**
 * Created by dpach on 26.02.2017.
 */

public class MyFirebaseMessangingService extends FirebaseMessagingService {
    public static String TAG = "WIADOMOSCI";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(State.currentSession != null) {
            Log.d(TAG, "From: " + remoteMessage.getFrom());

            if (remoteMessage.getData().size() > 0) {
                Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            }
            Map<String, String> test2 = remoteMessage.getData();
            String actionType = remoteMessage.getData().get("action");
            Long structureId = new Long(remoteMessage.getData().get("structure"));

            if(actionType.equals(ActionType.ACTIVITY.toString())) {
                if(State.currentSession.markers != null && State.currentSession.markers.size() > 0) {
                    for(Marker marker : State.currentSession.markers) {
                        if(marker.getStructure().id.equals(structureId)) {
                            ActionPicker.performAction(remoteMessage.getData().get("action_name"), marker.getStructure());
                            Log.d("ACTION_SYNC", "synchronizuje akcje");
                        }
                    }
                }
            }

            if (remoteMessage.getNotification() != null) {
                Log.d(TAG, "( " + MenuActivity.manufacturer + " ) " + "Message Notification Body: " + remoteMessage.getNotification().getBody());
            }
        }


    }
}
