package threewe.arinterface.sharedspaceclient.utils.async;

import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import threewe.arinterface.sharedspaceclient.config.URLs;
import threewe.arinterface.sharedspaceclient.utils.State;
import threewe.arinterface.sharedspaceclient.utils.URLUtils;

/**
 * Created by dpach on 01.01.2017.
 */

public class SessionsTask extends AsyncTask<String, Void, String> {

    private Exception exception;

    protected String doInBackground(String... params) {
        String resultTodisplay = "";
        try {
            URL url = new URL(URLs.CREATE_SESSION_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");

            String urlParameters = "DeviceId=" + State.getCurrentId();
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();

            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            Log.d("POLACZENIE", response.toString());

            List<String> sessionParams = new ArrayList<String>();
            sessionParams.add(response.toString());
            String getResponse = URLUtils.getRequest(URLs.GET_SESSION_URL, sessionParams);
            Log.d("POLACZENIE", getResponse);
            int t = 1;




        } catch(Exception ex) {
            Log.d("POLACZENIE", ex.getMessage() + "");
        }

        return "";
    }

    protected void onPostExecute(String result) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}
