package threewe.arinterface.sharedspaceclient.messaging;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import threewe.arinterface.sharedspaceclient.MenuActivity;

/**
 * Created by dpach on 26.02.2017.
 */

public class MyFirebaseMessangingService extends FirebaseMessagingService {
    public static String TAG = "WIADOMOSCI";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "( " + MenuActivity.manufacturer + " ) " + "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

    }
}
