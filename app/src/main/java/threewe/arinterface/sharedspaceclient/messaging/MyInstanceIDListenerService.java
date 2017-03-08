package threewe.arinterface.sharedspaceclient.messaging;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import threewe.arinterface.sharedspaceclient.R;
import threewe.arinterface.sharedspaceclient.config.URLs;
import threewe.arinterface.sharedspaceclient.utils.URLUtils;

/**
 * Created by dpach on 26.02.2017.
 */

public class MyInstanceIDListenerService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("WIADOMOSCI", "Refreshed token: " + refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    public void sendRegistrationToServer(String refreshedToken) {
        Log.d("WIADOMOSCI", "wysylam");
        URLUtils.getRequest(URLs.SAVE_TOKEN_URL + refreshedToken);
    }
}
