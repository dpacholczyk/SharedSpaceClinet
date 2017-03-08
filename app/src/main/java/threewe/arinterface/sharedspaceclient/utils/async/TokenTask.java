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
 * Created by Dawid Pacholczyk <dpacholczyk@outlook.com> on 01.01.2017.
 */

public class TokenTask extends AsyncTask<String, Void, String> {

    private Exception exception;
    private String token;

    public TokenTask(String token) {
        this.token = token;
    }

    protected String doInBackground(String... params) {
        URLUtils.getRequest(URLs.SAVE_TOKEN_URL + this.token);

        return "";
    }
}
