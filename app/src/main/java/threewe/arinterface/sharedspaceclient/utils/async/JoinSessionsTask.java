package threewe.arinterface.sharedspaceclient.utils.async;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import threewe.arinterface.sharedspaceclient.MainActivity;
import threewe.arinterface.sharedspaceclient.MenuActivity;
import threewe.arinterface.sharedspaceclient.config.URLs;
import threewe.arinterface.sharedspaceclient.models.Session;
import threewe.arinterface.sharedspaceclient.utils.State;
import threewe.arinterface.sharedspaceclient.utils.URLUtils;
import threewe.arinterface.sharedspaceclient.utils.translation.JsonTranslator;

/**
 * Created by Dawid Pacholczyk <dpacholczyk@outlook.com> on 14.01.2017.
 */

public class JoinSessionsTask extends AsyncTask<String, Void, String> {

    private static final String TAG = "SharedSpace_JoinSession";

    private Activity activity;
    private String sessionId;

    public JoinSessionsTask(Activity activity, String sessionId) {
        this.activity = activity;
        this.sessionId = sessionId;
        int q = 1;
    }

    protected String doInBackground(String... params) {
        int t = 1;
        try {
            List<String> sessionParams = new ArrayList<String>();
            sessionParams.add(this.sessionId);
            String getResponse = URLUtils.getRequest(URLs.GET_SESSION_URL, sessionParams);

            State.currentSession = JsonTranslator.getSessionFromJson(getResponse);



            List<String> joinParams = new ArrayList<String>();
            joinParams.add(MenuActivity.deviceId);
            joinParams.add(State.currentSession.id.toString());
            URLUtils.getRequest(URLs.JOIN_SESSION_URL, joinParams);
        } catch(Exception ex) {
            int test = 1;
            Log.e(TAG, ex.getMessage());
        }

        return "";
    }

    protected void onPostExecute(String result) {
        activity.startActivity(new Intent(activity, MainActivity.class));
    }
}
