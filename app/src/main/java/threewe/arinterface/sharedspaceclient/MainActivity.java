package threewe.arinterface.sharedspaceclient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
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
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.sql.Struct;
import java.util.HashMap;
import java.util.Map;

import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.AndARActivity;
import edu.dhbw.andar.exceptions.AndARException;
import threewe.arinterface.sharedspaceclient.actions.Highlight;
import threewe.arinterface.sharedspaceclient.config.ActivityType;
import threewe.arinterface.sharedspaceclient.models.Marker;
import threewe.arinterface.sharedspaceclient.models.Structure;
import threewe.arinterface.sharedspaceclient.objects.CustomObject;
import threewe.arinterface.sharedspaceclient.renderer.CustomRenderer;
import threewe.arinterface.sharedspaceclient.utils.ColorPickerDialog;
import threewe.arinterface.sharedspaceclient.utils.State;
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

    private Button resetButton;
    private Dialog dialog;

    private int ColorAh= Color.BLACK;
    private HashMap<String, Object> rgb = null;
    private RelativeLayout layout;

    private ColorPickerDialog.OnColorChangedListener listener = new ColorPickerDialog.OnColorChangedListener() {
        @Override
        public void colorChanged(int color) {
            Log.d("KOLOR", ""+color);
            ColorAh=color;
            layout.setBackgroundColor(ColorAh);
            int red = Color.red(ColorAh);
            int green = Color.green(ColorAh);
            int blue = Color.blue(ColorAh);
            MainActivity.this.rgb = new HashMap<>();
            rgb.put("red", red);
            rgb.put("green", green);
            rgb.put("blue", blue);

            prepareObjectList();
        }
    };

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

        resetButton = new Button(this);
        resetButton.setText(getResources().getText(R.string.reset));
        resetButton.setLayoutParams(params);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                State.selectedMode = ActivityType.Reset;
                sendStateChangeRequest(null, null);
            }
        });

        Button colorPickerButton = new Button(this);
        colorPickerButton.setText(getResources().getString(R.string.color_picker));
        colorPickerButton.setLayoutParams(params);
        colorPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(State.selectedMode == null) {
                    State.selectedMode = ActivityType.ColorPicker;
                    layout = new RelativeLayout(MainActivity.this);
                    ColorPickerDialog cpd=new ColorPickerDialog(MainActivity.this, listener, 0);
                    cpd.show();
                } else {
                    State.selectedMode = null;
                }
            }
        });

        Button highlightButton = new Button(this);
        highlightButton.setText(getResources().getString(R.string.highlight));
        highlightButton.setLayoutParams(params);
        highlightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(State.selectedMode == null) {
//                    MainActivity.this.resetButton.setVisibility(View.VISIBLE);
                    State.selectedMode = ActivityType.Highlight;
                    Log.d("LISTVIEW", "highlight");
                    prepareObjectList();
                } else {
                    State.selectedMode = null;
                }
            }
        });

        frame.addView(colorPickerButton);
        frame.addView(highlightButton);
        frame.addView(resetButton);
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
        dialog = new Dialog(this);
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

                MainActivity.this.sendStateChangeRequest(structure, MainActivity.this.rgb);
//                MainActivity.this.resetButton.setVisibility(View.VISIBLE);
                MainActivity.this.dialog.dismiss();
            }
        });

        builder.setView(oList);
        dialog = builder.create();

        dialog.show();
    }

//    private void prepare

    private void sendStateChangeRequest(Structure structure, HashMap<String, Object> rgb) {
        HashMap<String, Object> params = new HashMap<>();

        params.put("activity", State.ACTION_PACKAGE + State.selectedMode.toString());
        if(structure == null) {
            params.put("structure", 0);
        } else {
            params.put("structure", structure.id);
        }
        if(rgb != null) {
            params.put("color", rgb.get("red") + "|" + rgb.get("green") + "|" + rgb.get("blue"));
        }
        params.put("session", State.currentSession.id);
        params.put("sender", State.getCurrentId());

        new SyncTask(params).execute();
    }
}
