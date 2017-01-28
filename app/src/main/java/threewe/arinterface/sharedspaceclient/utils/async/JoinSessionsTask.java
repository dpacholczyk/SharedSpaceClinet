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
import threewe.arinterface.sharedspaceclient.config.URLs;
import threewe.arinterface.sharedspaceclient.models.Session;
import threewe.arinterface.sharedspaceclient.utils.State;
import threewe.arinterface.sharedspaceclient.utils.URLUtils;
import threewe.arinterface.sharedspaceclient.utils.translation.JsonTranslator;

/**
 * Created by Dawid Pacholczyk <dpacholczyk@outlook.com> on 14.01.2017.
 */

public class JoinSessionsTask extends AsyncTask<String, Void, String> {

    private Activity activity;
    private String sessionId;

    public JoinSessionsTask(Activity activity, String sessionId) {
        this.activity = activity;
        this.sessionId = sessionId;
    }

    protected String doInBackground(String... params) {
        try {
            List<String> sessionParams = new ArrayList<String>();
            sessionParams.add(this.sessionId);
            String getResponse = URLUtils.getRequest(URLs.GET_SESSION_URL, sessionParams);

            State.currentSession = JsonTranslator.getSessionFromJson(getResponse);
        } catch(Exception ex) {
            int test = 1;
        }

        return "";
    }

    protected void onPostExecute(String result) {
        activity.startActivity(new Intent(activity, MainActivity.class));
    }
}
