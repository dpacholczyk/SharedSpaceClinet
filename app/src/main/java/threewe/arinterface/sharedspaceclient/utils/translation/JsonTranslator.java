package threewe.arinterface.sharedspaceclient.utils.translation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import threewe.arinterface.sharedspaceclient.models.Marker;
import threewe.arinterface.sharedspaceclient.models.Session;
import threewe.arinterface.sharedspaceclient.models.Structure;
import threewe.arinterface.sharedspaceclient.utils.State;

/**
 * Created by dpach on 05.11.2016.
 */

public class JsonTranslator {
    public static void setMarkersFromJson(String json) {
        try {
            JSONArray jArray = new JSONArray(json);
            for(int i = 0; i < jArray.length(); i++) {
                JSONObject jObject = jArray.getJSONObject(i);
                Marker m = new Marker(jObject.getLong("id"), jObject.getString("name"), jObject.getString("fileName"), jObject.getString("pattern"));
                State.availableMarkers.add(m);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Structure getStructureFromJson(String json) {
        Structure s = null;
        try {
//            JSONArray jArray = new JSONArray(json);
//            for(int i = 0; i < jArray.length(); i++) {
//                JSONObject jObject = jArray.getJSONObject(i);
                JSONObject jObject = new JSONObject(json);
                s = new Structure(jObject.getLong("id"), jObject.getString("name"),
                        (float)jObject.getDouble("colorR"), (float)jObject.getDouble("colorG"), (float)jObject.getDouble("colorB"),
                        jObject.getDouble("positionX"), jObject.getDouble("positionY"), jObject.getString("definition"));
//            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return s;
    }

    public static Session getSessionFromJson(String json) {
        Session s = null;
        try {
            JSONObject jObject = new JSONObject(json);
            s = new Session();

        } catch(JSONException e) {
            e.printStackTrace();
        }

        return s;
    }
}
