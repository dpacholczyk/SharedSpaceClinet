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
    
    private static String protocol = "http";
    private static String adress = "192.168.1.224";
    private static String port = "9000";
    
    public static void setProtocol(String p) {
        protocol = p;
    }
    
    public static void setAdress(String a) {
        adress = a;
    }
    
    public static void setPort(String p) {
        port = p;
    }

    // markers start
    public static String MARKERS_URL = protocol + "://" + adress + ":" + port + "/markers";
    // markers end

    // structures start
    public static String STRUCTURE_URL = protocol + "://" + adress + ":" + port + "/marker/";
    // structures end

    // sessions start
    public static String CREATE_SESSION_URL = protocol + "://" + adress + ":" + port + "/sessions/create";
    public static String JOIN_SESSION_URL = protocol + "://" + adress + ":" + port + "/sessions/join";
    public static String GET_SESSION_URL = protocol + "://" + adress + ":" + port + "/sessions/get";
    public static String GET_SESSION_USERS_URL = protocol + "://" + adress + ":" + port + "/sessions/users";
    // sessions end

    // notifications start
    public static String SAVE_TOKEN_URL = protocol + "://" + adress + ":" + port + "/notifications/token/save/";
    public static String SEND_NOTIFICACTION_URL = protocol + "://" + adress + ":" + port + "/notifications/receive";
    // notifications end

    // users start
    public static String GET_USER_URL = protocol + "://" + adress + ":" + port + "/users/get";
    // users end

    public static void setAdresses() {
        MARKERS_URL = protocol + "://" + adress + ":" + port + "/markers";
        STRUCTURE_URL = protocol + "://" + adress + ":" + port + "/marker/";
        CREATE_SESSION_URL = protocol + "://" + adress + ":" + port + "/sessions/create";
        JOIN_SESSION_URL = protocol + "://" + adress + ":" + port + "/sessions/join";
        GET_SESSION_URL = protocol + "://" + adress + ":" + port + "/sessions/get";
        GET_SESSION_USERS_URL = protocol + "://" + adress + ":" + port + "/sessions/users";
        SAVE_TOKEN_URL = protocol + "://" + adress + ":" + port + "/notifications/token/save/";
        SEND_NOTIFICACTION_URL = protocol + "://" + adress + ":" + port + "/notifications/receive";
        GET_USER_URL = protocol + "://" + adress + ":" + port + "/users/get";
    }
}
