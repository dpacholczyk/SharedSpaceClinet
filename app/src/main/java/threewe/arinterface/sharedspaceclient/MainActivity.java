package threewe.arinterface.sharedspaceclient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.sql.Struct;
import java.util.HashMap;
import java.util.Map;

import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.ActivityType;
import edu.dhbw.andar.AndARActivity;
import edu.dhbw.andar.exceptions.AndARException;
import threewe.arinterface.sharedspaceclient.actions.Highlight;
import threewe.arinterface.sharedspaceclient.config.TmpColorsSolution;
import threewe.arinterface.sharedspaceclient.config.URLs;
import threewe.arinterface.sharedspaceclient.models.Marker;
import threewe.arinterface.sharedspaceclient.models.Session;
import threewe.arinterface.sharedspaceclient.models.Structure;
import threewe.arinterface.sharedspaceclient.objects.CustomObject;
import threewe.arinterface.sharedspaceclient.renderer.CustomRenderer;
import threewe.arinterface.sharedspaceclient.utils.State;
import threewe.arinterface.sharedspaceclient.utils.URLUtils;
import threewe.arinterface.sharedspaceclient.utils.async.SendNotificationTask;
import threewe.arinterface.sharedspaceclient.utils.async.SyncTask;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CustomRenderer renderer = new CustomRenderer();
        super.setNonARRenderer(renderer);
        for(Marker marker : State.currentSession.markers) {
            this.saveToFile(marker.getLocalFileName(), marker.pattern);

            artoolkit = MainActivity.super.getArtoolkit();
            someObject = new CustomObject(marker, 80.0, marker.getStructure(), this);
            try {
                artoolkit.registerARObject(someObject);
            } catch (AndARException e) {
                e.printStackTrace();
            }
        }


        LinearLayout frame = new LinearLayout(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
                Gravity.TOP|Gravity.RIGHT);

        Button pointerButton = new Button(this);
        pointerButton.setText(getResources().getString(R.string.pointer));
        pointerButton.setLayoutParams(params);
        pointerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentMode == null) {
                    currentMode = ActivityType.Pointer;
                    Log.d("LISTVIEW", "pointer");
                    prepareObjectList();
                } else {
                    currentMode = null;
                }
            }
        });

        Button highlightButton = new Button(this);
        highlightButton.setText(getResources().getString(R.string.highlight));
        highlightButton.setLayoutParams(params);
        highlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentMode == null) {
                    currentMode = ActivityType.Highlight;
                    Log.d("LISTVIEW", "highlight");
                    prepareObjectList();
                } else {
                    currentMode = null;
                }
            }
        });

        frame.addView(pointerButton);
        frame.addView(highlightButton);
        addContentView(frame, params);
    }

    private void saveToFile(String fileName, String pattern) {
        try {
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(pattern.getBytes());
            fos.close();

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
                    touchX = event.getX();
                    touchY = event.getY();
                    String color = this.makeScreenshot((int)touchX, (int)touchY);


                    break;
                case MotionEvent.ACTION_DOWN:

                    touchX = event.getX();
                    touchY = event.getY();
                    Log.d("POINTER", "x: " + touchX + " | y: " + touchY );

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

    private void prepareObjectList() {
        Dialog dialog = new Dialog(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.select_object));

        ListView oList = new ListView(this);
        String[] data = new String[State.currentSession.markers.size()];
        int i = 0;
        for(Marker marker : State.currentSession.markers) {
//            if(marker.getStructure().object.isVisible()) {
                Structure s = marker.getStructure();
                data[i] = marker.getStructure().name;
                i++;
//            }
        }
        ArrayAdapter<String> oAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, data);
        oList.setAdapter(oAdapter);
        oList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // name of the structure
                String selectedItem = (String) adapterView.getItemAtPosition(i);
                Structure structure = Structure.findStructureByName(selectedItem);

                HashMap<String, Object> params = new HashMap<>();
                params.put("activity", Highlight.class.getCanonicalName());
                params.put("structure", structure.id);
                params.put("session", 1);
                params.put("sender", "Dawid");

                new SyncTask(params).execute();
            }
        });

        builder.setView(oList);
        dialog = builder.create();

        dialog.show();
    }
}
