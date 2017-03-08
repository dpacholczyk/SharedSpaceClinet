package threewe.arinterface.sharedspaceclient.utils.async;

import android.os.AsyncTask;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

import threewe.arinterface.sharedspaceclient.config.URLs;
import threewe.arinterface.sharedspaceclient.utils.URLUtils;

/**
 * Created by Dawid Pacholczyk <dpacholczyk@outlook.com> on 01.01.2017.
 */

public class SendNotificationTask extends AsyncTask<String, Void, String> {

    private Exception exception;
    private String message;

    public SendNotificationTask(String message) {
        this.message = message;
    }

    protected String doInBackground(String... params) {
//        URLUtils.getRequest(URLs.SEND_NOTIFICACTION_URL + this.message);
//        Log.d("NOTIFICATIONS", URLs.SEND_NOTIFICACTION_URL + this.message);

        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("message", this.message);
        URLUtils.postRequest(URLs.SEND_NOTIFICACTION_URL, urlParams);

        return "";
    }
}
