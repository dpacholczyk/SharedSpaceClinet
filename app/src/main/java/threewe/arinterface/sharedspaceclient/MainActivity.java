package threewe.arinterface.sharedspaceclient;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
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

import com.google.ar.core.Config;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.PlaneHitResult;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.CameraException;
import com.google.ar.core.exceptions.NotTrackingException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ArrayBlockingQueue;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import threewe.arinterface.sharedspaceclient.models.Structure;
import threewe.arinterface.sharedspaceclient.rendering.BackgroundRenderer;
import threewe.arinterface.sharedspaceclient.rendering.ObjectRenderer;
import threewe.arinterface.sharedspaceclient.rendering.PlaneAttachment;
import threewe.arinterface.sharedspaceclient.rendering.PlaneRenderer;
import threewe.arinterface.sharedspaceclient.rendering.PointCloudRenderer;
import threewe.arinterface.sharedspaceclient.utils.CameraPermissionHelper;
import threewe.arinterface.sharedspaceclient.utils.ColorPickerDialog;
import threewe.arinterface.sharedspaceclient.utils.State;
import threewe.arinterface.sharedspaceclient.utils.async.SyncTask;

/**
 * Created by Dawid Pacholczyk <dpacholczyk@outlook.com>
 */

public class MainActivity extends AppCompatActivity implements GLSurfaceView.Renderer {

    private static final String TAG = "MAIN_AR";

    private Dialog dialog;

    private int ColorAh= Color.BLACK;
    private HashMap<String, Object> rgb = null;
    private RelativeLayout layout;

    /**
     * ARCORE CHANGE START
     */
    private boolean message = true;

    private GLSurfaceView mSurfaceView;
    private Session mSession;
    private Config mConfig;
    private GestureDetector mGestureDetector;

    private BackgroundRenderer mBackgroundRenderer = new BackgroundRenderer();
    private ObjectRenderer mVirtualObject = new ObjectRenderer();
    private ObjectRenderer mVirtualObjectShadow = new ObjectRenderer();
    private PlaneRenderer mPlaneRenderer = new PlaneRenderer();
    private PointCloudRenderer mPointCloud = new PointCloudRenderer();

    // Temporary matrix allocated here to reduce number of allocations for each frame.
    private final float[] mAnchorMatrix = new float[16];

    // Tap handling and UI.
    private ArrayBlockingQueue<MotionEvent> mQueuedSingleTaps = new ArrayBlockingQueue<>(16);
    private ArrayList<PlaneAttachment> mTouches = new ArrayList<>();

    /**
     * ARCORE CHANGE END
     */

    private Button resetButton;

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





    private void saveToFile(String fileName, String data) {

        try {
            FileOutputStream fos = openFileOutput(fileName, Context.MODE_PRIVATE);
            fos.write(data.getBytes());
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSurfaceView = (GLSurfaceView) findViewById(R.id.surfaceview);
        mSession = new Session(this);
        mConfig = Config.createDefaultConfig();
        if (!mSession.isSupported(mConfig)) {
            Toast.makeText(this, "This device does not support AR", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                onSingleTap(e);
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
        });
        mSurfaceView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });

        mSurfaceView.setPreserveEGLContextOnPause(true);
        mSurfaceView.setEGLContextClientVersion(2);
        mSurfaceView.setEGLConfigChooser(8, 8, 8, 8, 16, 0); // Alpha used for plane blending.
        mSurfaceView.setRenderer(this);
        mSurfaceView.setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);

        Structure s = State.currentSession.structures.get(0);
        this.saveToFile("myobject.obj", s.definition);

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
//                State.selectedMode = ActivityType.Reset;
//                sendStateChangeRequest(null, null);
            }
        });
//
//        Button colorPickerButton = new Button(this);
//        colorPickerButton.setText(getResources().getString(R.string.color_picker));
//        colorPickerButton.setLayoutParams(params);
//        colorPickerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(State.selectedMode == null) {
//                    State.selectedMode = ActivityType.ColorPicker;
//                    layout = new RelativeLayout(MainActivity.this);
//                    ColorPickerDialog cpd=new ColorPickerDialog(MainActivity.this, listener, 0);
//                    cpd.show();
//                } else {
//                    State.selectedMode = null;
//                }
//            }
//        });
//
//        Button highlightButton = new Button(this);
//        highlightButton.setText(getResources().getString(R.string.highlight));
//        highlightButton.setLayoutParams(params);
//        highlightButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(State.selectedMode == null) {
////                    MainActivity.this.resetButton.setVisibility(View.VISIBLE);
//                    State.selectedMode = ActivityType.Highlight;
//                    Log.d("LISTVIEW", "highlight");
//                    prepareObjectList();
//                } else {
//                    State.selectedMode = null;
//                }
//            }
//        });
//
//        frame.addView(colorPickerButton);
//        frame.addView(highlightButton);
        frame.addView(resetButton);
        addContentView(frame, params);
    }

    private void prepareObjectList() {
        dialog = new Dialog(this);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.select_object));

        ListView oList = new ListView(this);
        String[] data = new String[State.currentSession.structures.size()];
        int i = 0;
        for(Structure structure : State.currentSession.structures) {
//            if(marker.getStructure().object.isVisible()) {
                Structure s = structure;
                data[i] = structure.name;
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] results) {
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            Toast.makeText(this,
                    "Camera permission is needed to run this application", Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void showLoadingMessage() {
        message = true;
    }

    private void hideLoadingMessage() {
        message = false;
        Log.d("TRACKING", "Surface found");
    }

    private void onSingleTap(MotionEvent e) {
        // Queue tap if there is space. Tap is lost if queue is full.
        mQueuedSingleTaps.offer(e);
    }

    @Override
    protected void onResume() {
        super.onResume();
        showLoadingMessage();
        mSession.resume(mConfig);
        mSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSurfaceView.onPause();
        mSession.pause();
    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        /**
         * @TODO
         * jaki ma to wpyw na scenę ?
         */
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f);
        mBackgroundRenderer.createOnGlThread(this);
        mSession.setCameraTextureName(mBackgroundRenderer.getTextureId());

        /**
         * @TODO
         * testy z innymi obiektami
         */
        // Prepare the other rendering objects.
        try {
            mVirtualObject.createOnGlThread(this, "myobject.obj", "papyrus.jpg");
//            mVirtualObject.createOnGlThread(this, "bulldozer.obj", "papyrus.jpg");
//            mVirtualObject.createOnGlThread(this, "andy.obj", "andy.png");
//            mVirtualObject.setMaterialProperties(0.0f, 3.5f, 1.0f, 6.0f);

//            mVirtualObjectShadow.createOnGlThread(this, "andy_shadow.obj", "andy_shadow.png");
//            mVirtualObjectShadow.setBlendMode(ObjectRenderer.BlendMode.Shadow);
//            mVirtualObjectShadow.setMaterialProperties(1.0f, 0.0f, 0.0f, 1.0f);
        } catch (IOException e) {
            Log.e(TAG, "Failed to read obj file");
        }

        try {
            mPlaneRenderer.createOnGlThread(this, "trigrid.png");
        } catch (IOException e) {
            Log.e(TAG, "Failed to read plane texture");
        }
        mPointCloud.createOnGlThread(this);
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        // Notify ARCore session that the view size changed so that the perspective matrix and
        // the video background can be properly adjusted.
        mSession.setDisplayGeometry(width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        // Clear screen to notify driver it should not load any pixels from previous frame.
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        try {
            if(message) {
                Log.d("TRACKING", "Searching for surfaces...");
            }

            Frame frame = mSession.update();

            // Handle taps. Handling only one tap per frame, as taps are usually low frequency
            // compared to frame rate.
            MotionEvent tap = mQueuedSingleTaps.poll();
            if (tap != null && frame.getTrackingState() == Frame.TrackingState.TRACKING) {
                for (HitResult hit : frame.hitTest(tap)) {
                    // Check if any plane was hit, and if it was hit inside the plane polygon.
                    if (hit instanceof PlaneHitResult && ((PlaneHitResult) hit).isHitInPolygon()) {
                        // Cap the number of objects created. This avoids overloading both the
                        // rendering system and ARCore.
                        if (mTouches.size() >= 16) {
                            mSession.removeAnchors(Arrays.asList(mTouches.get(0).getAnchor()));
                            mTouches.remove(0);
                        }
                        // Adding an Anchor tells ARCore that it should track this position in
                        // space. This anchor will be used in PlaneAttachment to place the 3d model
                        // in the correct position relative both to the world and to the plane.
                        mTouches.add(new PlaneAttachment(
                                ((PlaneHitResult) hit).getPlane(),
                                mSession.addAnchor(hit.getHitPose())));

                        // Hits are sorted by depth. Consider only closest hit on a plane.
                        break;
                    }
                }
            }

            /**
             * @TODO
             * pobawić się tym
             */
            mBackgroundRenderer.draw(frame);

            if (frame.getTrackingState() == Frame.TrackingState.NOT_TRACKING) {
                return;
            }

            // Get projection matrix.
            float[] projmtx = new float[16];
            mSession.getProjectionMatrix(projmtx, 0, 0.1f, 100.0f);

            // Get camera matrix and draw.
            float[] viewmtx = new float[16];
            frame.getViewMatrix(viewmtx, 0);

            // Compute lighting from average intensity of the image.
            final float lightIntensity = frame.getLightEstimate().getPixelIntensity();

            mPointCloud.update(frame.getPointCloud());
            mPointCloud.draw(frame.getPointCloudPose(), viewmtx, projmtx);

            // Check if we detected at least one plane. If so, hide the loading message.
            if (message) {
                for (Plane plane : mSession.getAllPlanes()) {
                    if (plane.getType() == com.google.ar.core.Plane.Type.HORIZONTAL_UPWARD_FACING &&
                            plane.getTrackingState() == Plane.TrackingState.TRACKING) {
                        hideLoadingMessage();
                        break;
                    }
                }
            }

            mPlaneRenderer.drawPlanes(mSession.getAllPlanes(), frame.getPose(), projmtx);

            // Visualize anchors created by touch.
            float scaleFactor = 0.01f;
            for (PlaneAttachment planeAttachment : mTouches) {
                if (!planeAttachment.isTracking()) {
                    continue;
                }
                // Get the current combined pose of an Anchor and Plane in world space. The Anchor
                // and Plane poses are updated during calls to session.update() as ARCore refines
                // its estimate of the world.
                planeAttachment.getPose().toMatrix(mAnchorMatrix, 0);

                // Update and draw the model and its shadow.
                mVirtualObject.updateModelMatrix(mAnchorMatrix, scaleFactor);
//                mVirtualObjectShadow.updateModelMatrix(mAnchorMatrix, scaleFactor);
                mVirtualObject.draw(viewmtx, projmtx, lightIntensity, true);
//                mVirtualObjectShadow.draw(viewmtx, projmtx, lightIntensity, false);
            }


        } catch (CameraException e) {
            e.printStackTrace();
        } catch(NotTrackingException e) {
            e.printStackTrace();
        }
    }
}
