package threewe.arinterface.sharedspaceclient;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.AndARActivity;
import edu.dhbw.andar.exceptions.AndARException;
import threewe.arinterface.sharedspaceclient.models.Marker;
import threewe.arinterface.sharedspaceclient.models.Session;
import threewe.arinterface.sharedspaceclient.objects.CustomObject;
import threewe.arinterface.sharedspaceclient.renderer.CustomRenderer;
import threewe.arinterface.sharedspaceclient.utils.State;

/**
 * Created by Dawid Pacholczyk <dpacholczyk@outlook.com>
 */

public class MainActivity extends AndARActivity {

    private CustomObject someObject;
//    private SharedSpaceToolkit artoolkit;
    private ARToolkit artoolkit;


    private float touchX = 0;
    private float touchY = 0;

    private boolean wentUp = false;
    private static final boolean OFFLINE = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomRenderer renderer = new CustomRenderer();
        super.setNonARRenderer(renderer);
        Session test = State.currentSession;
        for(Marker marker : State.currentSession.markers) {
            this.saveToFile(marker.getLocalFileName(), marker.pattern);

            artoolkit = MainActivity.super.getArtoolkit();
            someObject = new CustomObject(marker.name, marker.getLocalFileName(), 80.0, marker.getStructure(), this);
            try {
                artoolkit.registerARObject(someObject);
            } catch (AndARException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveToFile(String fileName, String pattern) {
        try {
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(pattern.getBytes());
            fos.close();

//            for(int i = 0; i < getFilesDir().listFiles().length; i++) {
//                Log.d("GENIUSZ", getFilesDir().listFiles()[i].toString());
//            }

            FileInputStream in = openFileInput(fileName);
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
        } catch(Exception ex) {

        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!wentUp) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_UP:
                    this.makeScreenshot((int)event.getX(), (int)event.getY());
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

}
