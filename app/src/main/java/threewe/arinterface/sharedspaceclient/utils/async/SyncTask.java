package threewe.arinterface.sharedspaceclient.utils.async;

import android.os.AsyncTask;

import java.util.HashMap;
import java.util.Map;

import threewe.arinterface.sharedspaceclient.config.URLs;
import threewe.arinterface.sharedspaceclient.utils.URLUtils;

/**
 * Created by Dawid Pacholczyk <dpacholczyk@outlook.com> on 01.01.2017.
 */

public class SyncTask extends AsyncTask<String, Void, String> {

    private Exception exception;
    private HashMap<String, Object> params;

    public SyncTask(HashMap<String, Object> params) {
        this.params = params;
    }

    protected String doInBackground(String... p) {
        URLUtils.postRequest(URLs.STATE_SYNC_URL, this.params);

        return "";
    }
}
