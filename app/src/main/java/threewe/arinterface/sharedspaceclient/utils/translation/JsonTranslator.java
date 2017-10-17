package threewe.arinterface.sharedspaceclient.utils.translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import threewe.arinterface.sharedspaceclient.models.Session;
import threewe.arinterface.sharedspaceclient.models.SessionUser;
import threewe.arinterface.sharedspaceclient.models.Structure;
import threewe.arinterface.sharedspaceclient.models.User;
import threewe.arinterface.sharedspaceclient.utils.State;

/**
 * Created by dpach on 05.11.2016.
 */

public class JsonTranslator {

    public static Structure getStructureFromJson(String json) {
        Structure s = null;
        try {
//            JSONArray jArray = new JSONArray(json);
//            for(int i = 0; i < jArray.length(); i++) {
//                JSONObject jObject = jArray.getJSONObject(i);
                JSONObject jObject = new JSONObject(json);
                s = new Structure(jObject.getLong("id"), jObject.getString("name"), jObject.getString("definition"));
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return s;
    }

    public static User getUserFromJson(String json) {
        User user = null;

        try {
            JSONObject jObject = new JSONObject(json);

            Long id = jObject.getLong("id");
            String name = jObject.getString("name");
            String deviceId = jObject.getString("deviceId");

            user.id = id;
            user.name = name;
            user.deviceId = deviceId;

        } catch(JSONException e) {
            e.printStackTrace();
        }

        return user;
    }

    /**
     * @TODO
     * wyciąć kwestie związane z markerami
     *
     * @param json
     * @return
     */
    public static Session getSessionFromJson(String json) {
        Session s = null;
        try {
            JSONObject jObject = new JSONObject(json);

            Long sessionId = jObject.getLong("id");
            JSONArray jUsers = jObject.getJSONArray("users");
            JSONArray jStructures = jObject.getJSONArray("structures");

            List<SessionUser> users = new ArrayList<>();
            s = new Session();
            for(int i = 0; i < jUsers.length(); i++) {
                JSONObject jSessionUser = jUsers.getJSONObject(i);
                JSONObject jUser = jSessionUser.getJSONObject("user");

                SessionUser su = new SessionUser();
                User u = new User();

                u.id = jUser.getLong("id");
                u.name = jUser.getString("name");

                su.id = jSessionUser.getLong("id");
                su.user = u;
                su.isHost = jSessionUser.getBoolean("isHost");

                users.add(su);
            }

            for(int i = 0; i < jStructures.length(); i++) {
                JSONObject jStructure = jStructures.getJSONObject(i);

                Structure structure = new Structure();
                structure.id = jStructure.getLong("id");
                structure.name = jStructure.getString("name");
                structure.definition = jStructure.getString("definition").replace("\r", "");

                s.structures.add(structure);
            }

            s.id = sessionId;
            s.users = users;

        } catch(JSONException e) {
            e.printStackTrace();
        }

        return s;
    }
}
