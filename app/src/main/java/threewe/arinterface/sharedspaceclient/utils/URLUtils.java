package threewe.arinterface.sharedspaceclient.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

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

}

