package threewe.arinterface.sharedspaceclient.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import threewe.arinterface.sharedspaceclient.config.URLs;
import threewe.arinterface.sharedspaceclient.models.Session;
import threewe.arinterface.sharedspaceclient.utils.translation.JsonTranslator;

/**
 * Created by dpach on 11.01.2017.
 */

public class URLUtils
{

    public static String addParameter(String URL, String value)
    {
        int qpos = URL.indexOf('?');
        int hpos = URL.indexOf('#');
//        char sep = qpos == -1 ? '?' : '&';
        String sep = "/";
        String seg = sep + encodeUrl(value);
        return hpos == -1 ? URL + seg : URL.substring(0, hpos) + seg
                + URL.substring(hpos);
    }

    public static String encodeUrl(String url)
    {
        try
        {
            return URLEncoder.encode(url, "UTF-8");
        }
        catch (UnsupportedEncodingException uee)
        {
            throw new IllegalArgumentException(uee);
        }
    }

    public static String getRequest(String GET_URL) {
        String getResponse = "";
        URL getUrl = null;
        try {
            getUrl = new URL(GET_URL);
            Log.d("WIADOMOSCI", GET_URL);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(getUrl.openStream(), "UTF-8"))) {
                for (String line; (line = reader.readLine()) != null;) {
                    getResponse += line;
                }
            }
        } catch (MalformedURLException e) {
            return "Error code: 74392763";
        } catch (IOException e) {
            return "Error code: 17474037";
        }

        return getResponse;
    }

    public static String getRequest(String GET_URL, List<String> params) {
        String getResponse = "";
        for(String param : params) {
            GET_URL = URLUtils.addParameter(GET_URL, param);
        }
        URL getUrl = null;
        try {
            getUrl = new URL(GET_URL);
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(getUrl.openStream(), "UTF-8"))) {
                for (String line; (line = reader.readLine()) != null;) {
                    getResponse += line;
                }
            }
        } catch (MalformedURLException e) {
            Log.e("URLUtils", "74392763: " + e.getMessage());
            return "Error code: 74392763";
        } catch (IOException e) {
            Log.e("URLUtils", "17474037: " + e.getMessage());
            return "Error code: 17474037";
        }

        return getResponse;
    }

    public static void postRequest(String POST_URL, Map<String, Object> params) {
        try {
            URL url = new URL(POST_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");

//            String urlParameters = "SessionName=test&DeviceId=" + State.getCurrentId();
            String urlParameters = "";
            Iterator it = params.entrySet().iterator();
            int m = 0;
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                System.out.println(pair.getKey() + " = " + pair.getValue());
                if(m == 0) {
                    urlParameters = pair.getKey() + "=" + pair.getValue();
                } else {
                    urlParameters += "&" + pair.getKey() + "=" + pair.getValue();
                }
                m++;
            }
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

        } catch(Exception ex) {
            Log.d("POLACZENIE", ex.getMessage() + "");
        }
    }


}

