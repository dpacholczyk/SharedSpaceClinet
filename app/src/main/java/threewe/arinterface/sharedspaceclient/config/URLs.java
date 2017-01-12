package threewe.arinterface.sharedspaceclient.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import threewe.arinterface.sharedspaceclient.utils.URLUtils;

/**
 * Created by dpach on 29.12.2016.
 */

public class URLs {

    // markers start
    public final static String MARKERS_URL = "http://192.168.1.224:9000/markers";
    // markers end

    // structures start
    public final static String STRUCTURE_URL = "http://192.168.1.224:9000/structures/marker/";
    // structures end

    // sessions start
    public final static String CREATE_SESSION_URL = "http://192.168.1.224:9000/sessions/create";
    public final static String JOIN_SESSION_URL = "http://192.168.1.224:9000/sessions/join";
    public final static String GET_SESSION_URL = "http://192.168.1.224:9000/sessions/get";
    public final static String GET_SESSION_USERS_URL = "http://192.168.1.224:9000/sessions/users";
    // sessions end
}
