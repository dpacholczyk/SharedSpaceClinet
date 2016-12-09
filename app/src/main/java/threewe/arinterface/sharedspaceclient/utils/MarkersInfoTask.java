package threewe.arinterface.sharedspaceclient.utils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

class MarkersInfoTask extends AsyncTask<String, Void, String> {

    private static final String MARKERS_URL = "http://localhost:9000/markers";

    private Exception exception;

    protected String doInBackground(String... urls) {
        try {
            String result = "";
            URL obj = new URL(MARKERS_URL);
            HttpURLConnection con = (HttpURLConnection)obj.openConnection();
            Log.d("JSON", MARKERS_URL);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(obj.openStream(), "UTF-8"))) {
                Log.d("JSON", "try");
                for (String line; (line = reader.readLine()) != null;) {
                    Log.d("JSON", line);
                }
            } catch(Exception ex) {
                Log.d("JSON", ex.getMessage());
            }

            return result;
        } catch (Exception e) {
            this.exception = e;

            return null;
        }
    }

    protected void onPostExecute(String result) {
        // TODO: check this.exception
        // TODO: do something with the feed
    }
}
