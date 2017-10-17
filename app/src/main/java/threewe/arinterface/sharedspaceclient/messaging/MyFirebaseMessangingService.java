package threewe.arinterface.sharedspaceclient.messaging;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

import threewe.arinterface.sharedspaceclient.actions.ActionPicker;
import threewe.arinterface.sharedspaceclient.actions.Reset;
import threewe.arinterface.sharedspaceclient.config.ActionType;

import threewe.arinterface.sharedspaceclient.MenuActivity;
import threewe.arinterface.sharedspaceclient.config.ActivityType;
import threewe.arinterface.sharedspaceclient.models.Structure;
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
            String actionClass = remoteMessage.getData().get("action_name");
            String[] tmp = actionClass.split("\\.");
            String actionName = tmp[tmp.length-1];
            Long structureId = new Long(remoteMessage.getData().get("structure"));

            if(actionName.equals(ActivityType.Reset.toString())) {
                if(State.currentSession.structures != null && State.currentSession.structures.size() > 0) {
                    Reset reset = new Reset();
                    reset.run();
                    Log.d("ACTION_SYNC", "wykonuje reset");
                }
            } else {
                ActionPicker picker = new ActionPicker();
                if(actionType.equals(ActionType.ACTIVITY.toString())) {
                    if(State.currentSession.structures != null && State.currentSession.structures.size() > 0) {
                        for(Structure structure : State.currentSession.structures) {
                            if(structure.id.equals(structureId)) {
                                if(remoteMessage.getData().containsKey("color")) {
                                    String[] colors = remoteMessage.getData().get("color").split("\\|");
                                    HashMap<String, Object> colorParams = new HashMap<>();
                                    colorParams.put("red", colors[0]);
                                    colorParams.put("green", colors[1]);
                                    colorParams.put("blue", colors[2]);

                                    picker.rgb = colorParams;
                                }
                                picker.performAction(remoteMessage.getData().get("action_name"), structure);
                                Log.d("ACTION_SYNC", "synchronizuje akcje");
                            }
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
