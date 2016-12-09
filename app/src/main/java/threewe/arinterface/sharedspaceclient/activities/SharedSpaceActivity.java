package threewe.arinterface.sharedspaceclient.activities;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import java.io.IOException;

import edu.dhbw.andar.ARToolkit;
import edu.dhbw.andar.AndARActivity;
import edu.dhbw.andar.AndARRenderer;
import edu.dhbw.andar.CameraHolder;
import edu.dhbw.andar.CameraParameters;
import edu.dhbw.andar.CameraPreviewHandler;
import edu.dhbw.andar.CameraStatus;
import edu.dhbw.andar.exceptions.AndARRuntimeException;
import edu.dhbw.andar.util.IO;
import threewe.arinterface.sharedspaceclient.utils.toolkit.SharedSpaceToolkit;

/**
 * Created by dpach on 06.11.2016.
 */

public abstract class SharedSpaceActivity extends AndARActivity {
    private SharedSpaceToolkit artoolkit;
    private GLSurfaceView glSurfaceView;
    private Camera camera;
    private AndARRenderer renderer;
    private Resources res;
    private CameraPreviewHandler cameraHandler;
    private boolean mPausing = false;
    private CameraStatus camStatus = new CameraStatus();
    private boolean surfaceCreated = false;
    private SurfaceHolder mSurfaceHolder = null;
    private SharedSpaceActivity.Preview previewSurface;
    private boolean startPreviewRightAway;
    private boolean previewing = false;

    public SharedSpaceActivity() {
        super();
    }

    public SharedSpaceActivity(boolean startPreviewRightAway) {
        super(startPreviewRightAway);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.currentThread().setUncaughtExceptionHandler(this);
        this.res = this.getResources();
        this.artoolkit = new SharedSpaceToolkit(this.res, this.getFilesDir());
        this.setFullscreen();
        this.disableScreenTurnOff();

        try {
            IO.transferFilesToPrivateFS(this.getFilesDir(), this.res);
        } catch (IOException var3) {
            var3.printStackTrace();
            throw new AndARRuntimeException(var3.getMessage());
        }

        FrameLayout frame = new FrameLayout(this);
        this.previewSurface = new SharedSpaceActivity.Preview(this);
        this.glSurfaceView = new GLSurfaceView(this);
        this.renderer = new AndARRenderer(this.res, this.artoolkit, this);
        this.cameraHandler = new CameraPreviewHandler(this.glSurfaceView, this.renderer, this.res, this.artoolkit, this.camStatus);
        this.glSurfaceView.setRenderer(this.renderer);
        this.glSurfaceView.setRenderMode(0);
        this.glSurfaceView.getHolder().addCallback(this);
        frame.addView(this.glSurfaceView);
        frame.addView(this.previewSurface);
        this.setContentView(frame);
    }

    public void disableScreenTurnOff() {
        this.getWindow().setFlags(128, 128);
    }

    public void setOrientation() {
//        this.setRequestedOrientation(0);
    }

    public void setFullscreen() {
        this.requestWindowFeature(1);
        this.getWindow().setFlags(1024, 1024);
    }

    public void setNoTitle() {
        this.requestWindowFeature(1);
    }

    protected void onPause() {
        this.mPausing = true;
        this.glSurfaceView.onPause();
        super.onPause();
        this.finish();
        if(this.cameraHandler != null) {
            this.cameraHandler.stopThreads();
        }

    }

    protected void onDestroy() {
        super.onDestroy();
        System.runFinalization();
    }

    protected void onResume() {
        this.mPausing = false;
        this.glSurfaceView.onResume();
        super.onResume();
    }

    protected void onStop() {
        super.onStop();
    }

    private void openCamera() {
        if(this.camera == null) {
            this.camera = CameraHolder.instance().open();

            try {
                this.camera.setPreviewDisplay(this.mSurfaceHolder);
            } catch (IOException var3) {
                var3.printStackTrace();
            }

            CameraParameters.setCameraParameters(this.camera, this.previewSurface.getWidth(), this.previewSurface.getHeight());

//            try {
//                this.cameraHandler.init(this.camera);
//            } catch (Exception var2) {
//                var2.printStackTrace();
//            }
        }

    }

    private void closeCamera() {
        if(this.camera != null) {
            CameraHolder.instance().keep();
            CameraHolder.instance().release();
            this.camera = null;
            this.previewing = false;
        }

    }

    public void startPreview() {
        if(this.surfaceCreated) {
            if(!this.mPausing && !this.isFinishing()) {
                if(this.previewing) {
                    this.stopPreview();
                }

                this.openCamera();
                this.camera.startPreview();
                this.previewing = true;
            }
        }
    }

    private void stopPreview() {
        if(this.camera != null && this.previewing) {
            this.previewing = false;
            this.camera.stopPreview();
        }

    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceCreated(SurfaceHolder holder) {
        this.surfaceCreated = true;
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    public SharedSpaceToolkit getArtoolkit() {
        return this.artoolkit;
    }

    public Bitmap takeScreenshot() {
        return this.renderer.takeScreenshot();
    }

    public SurfaceView getSurfaceView() {
        return this.glSurfaceView;
    }


    class Preview extends SurfaceView implements SurfaceHolder.Callback {
        SurfaceHolder mHolder = this.getHolder();
        Camera mCamera;
        private int w;
        private int h;

        Preview(Context context) {
            super(context);
            this.mHolder.addCallback(this);
            this.mHolder.setType(3);
        }

        public void surfaceCreated(SurfaceHolder holder) {
        }

        public void surfaceDestroyed(SurfaceHolder holder) {
            SharedSpaceActivity.this.stopPreview();
//            SharedSpaceActivity.this.closeCamera();
            SharedSpaceActivity.this.mSurfaceHolder = null;
        }

        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            this.w = w;
            this.h = h;
            SharedSpaceActivity.this.mSurfaceHolder = holder;
            if(SharedSpaceActivity.this.startPreviewRightAway) {
                SharedSpaceActivity.this.startPreview();
            }

        }

        public int getScreenWidth() {
            return this.w;
        }

        public int getScreenHeight() {
            return this.h;
        }
    }
}
