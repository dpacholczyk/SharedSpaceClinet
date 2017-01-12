package threewe.arinterface.sharedspaceclient.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

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
            return "Error code: 74392763";
        } catch (IOException e) {
            return "Error code: 17474037";
        }

        return getResponse;
    }


}

