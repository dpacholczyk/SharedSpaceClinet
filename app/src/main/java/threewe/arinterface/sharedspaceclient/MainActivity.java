package threewe.arinterface.sharedspaceclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.microedition.khronos.opengles.GL;

import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.AndARActivity;
import edu.dhbw.andar.exceptions.AndARException;
import threewe.arinterface.sharedspaceclient.activities.SharedSpaceActivity;
import threewe.arinterface.sharedspaceclient.models.Marker;
import threewe.arinterface.sharedspaceclient.models.Structure;
import threewe.arinterface.sharedspaceclient.objects.CustomObject;
import threewe.arinterface.sharedspaceclient.renderer.CustomRenderer;
import threewe.arinterface.sharedspaceclient.utils.MatrixTrackingGL;
import threewe.arinterface.sharedspaceclient.utils.State;
import threewe.arinterface.sharedspaceclient.utils.toolkit.SharedSpaceToolkit;
import threewe.arinterface.sharedspaceclient.utils.translation.JsonTranslator;
import threewe.arinterface.sharedspaceclient.views.MyView;

public class MainActivity extends AndARActivity {

    public final static String MARKERS_URL = "http://192.168.1.224:8081/markers";
    public final static String STRUCTURE_URL = "http://192.168.1.224:8081/structures/marker/";

    private CustomObject someObject;
//    private SharedSpaceToolkit artoolkit;
    private ARToolkit artoolkit;


    private float touchX = 0;
    private float touchY = 0;

    private boolean wentUp = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomRenderer renderer = new CustomRenderer();
        super.setNonARRenderer(renderer);


        try {
            Log.d("JSON", "start");
            new MarkersInfoTask().execute(MARKERS_URL);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean chaneStatus = false;
        if(!wentUp) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_UP:
                    chaneStatus = true;
                    break;
            }
        }


        return true;
    }

    private void checkColor() {

    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e("AndAR EXCEPTION", ex.getMessage());
        finish();
    }

    class MarkersInfoTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        protected String doInBackground(String... urls) {
            try {
                String result = "";
                URL obj = new URL(urls[0]);
                HttpURLConnection con = (HttpURLConnection)obj.openConnection();
                Log.d("JSON", urls[0]);
                String json = "";
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(obj.openStream(), "UTF-8"))) {
                    Log.d("JSON", "try");
                    for (String line; (line = reader.readLine()) != null;) {
                        Log.d("JSON", line);
                        json += line;
                    }
                    Log.d("JSON", "manual: " + json);
                    JsonTranslator.setMarkersFromJson(json);
                    try {
                        for(Marker marker : State.availableMarkers) {
                            Log.d("JSON", marker.pattern);
                            this.saveToFile(marker.getLocalFileName(), marker.pattern);

                            URL sObj = new URL(STRUCTURE_URL + marker.id);
                            HttpURLConnection sCon = (HttpURLConnection) sObj.openConnection();
                            String sjson = "";
                            try (BufferedReader sReader = new BufferedReader(new InputStreamReader(sObj.openStream(), "UTF-8"))) {
                                Log.d("JSON", "try");
                                for (String line; (line = sReader.readLine()) != null; ) {
                                    Log.d("JSON", line);
                                    sjson += line;
                                }
                                Structure s = JsonTranslator.getStructureFromJson(sjson);
                                marker.setStucture(s);
                            } catch(Exception ex) {
                                Log.d("JSON", "structure exception: " + ex.getMessage());
                            }

                            artoolkit = MainActivity.super.getArtoolkit();
                            someObject = new CustomObject(marker.name, marker.getLocalFileName(), 80.0, marker.getStructure());
                            artoolkit.registerARObject(someObject);
                        }
                    } catch (AndARException ex){
                        System.out.println(ex.getMessage());
                        Log.d("JSON", ex.getMessage());
                    }
                    startPreview();
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

        private void saveToFile(String fileName, String pattern) {
            try {
                FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
                fos.write(pattern.getBytes());
                fos.close();

                for(int i = 0; i < getFilesDir().listFiles().length; i++) {
                    Log.d("GENIUSZ", getFilesDir().listFiles()[i].toString());
                }

                FileInputStream in = openFileInput(fileName);
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

                Log.d("GENIUSZ", sb.toString());
            } catch(Exception ex) {
                Log.d("SHARED_SPACE", ex.getMessage());
            }
        }
    }
}
