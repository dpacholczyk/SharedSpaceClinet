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

    public final static String MARKERS_URL = "http://192.168.1.224:9000/markers";
    public final static String STRUCTURE_URL = "http://192.168.1.224:9000/structures/marker/";

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


        try {
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
            if(!OFFLINE) {
                try {
                    String result = "";
                    URL obj = new URL(urls[0]);
                    HttpURLConnection con = (HttpURLConnection)obj.openConnection();
                    String json = "";
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(obj.openStream(), "UTF-8"))) {
                        for (String line; (line = reader.readLine()) != null;) {
                            json += line;
                        }
                        JsonTranslator.setMarkersFromJson(json);
                        try {
                            for(Marker marker : State.availableMarkers) {
                                this.saveToFile(marker.getLocalFileName(), marker.pattern);

                                URL sObj = new URL(STRUCTURE_URL + marker.id);
                                HttpURLConnection sCon = (HttpURLConnection) sObj.openConnection();
                                String sjson = "";
                                try (BufferedReader sReader = new BufferedReader(new InputStreamReader(sObj.openStream(), "UTF-8"))) {
                                    for (String line; (line = sReader.readLine()) != null; ) {
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
            } else {
                String json = "[{\"id\":1,\"name\":\"android\",\"fileName\":\"android.patt\",\"pattern\":\" 223 223 224 224 224 224 220 224 224 222 224 224 224 224 224 224\\r\\n 224 224 224 224 224 194  40 157 214  52 139 224 224 224 224 224\\r\\n 224 224 224 224 224 175   4 114 188   7  94 224 224 224 224 224\\r\\n 224 223 224 224 221 167   3 112 183   4  93 212 224 224 224 224\\r\\n 224 224 224 224  87  11   0   7  18   1   7  18 215 224 224 224\\r\\n 224 224 197 221  65   0   0   0   0   0   1   2 180 204 223 224\\r\\n 224 208  30 112  65   0   0   0   0   0   0   1 134  22 141 224\\r\\n 224 188   9  79  62   0   0   0   0   0   0   1 120   9 120 224\\r\\n 224 191   9  82  68   0   0   0   0   0   0   1 121   8 120 224\\r\\n 223 194  10  80  66   0   0   0   1   0   1   7 122  11 123 224\\r\\n 224 210  21 106  70   4   4   5   5   4   7  17 143  17 133 224\\r\\n 224 224 195 218 166 146 146 146 148 145 147 146 210 198 223 224\\r\\n 224 224 224 224  92  10  32  18  17  32  29  30 221 224 224 224\\r\\n 224 224 224 224 188  34 170  10   6  96  71 139 224 224 224 224\\r\\n 224 224 224 224 224 193  72  32  26  65 143 224 224 224 224 224\\r\\n 224 224 224 224 224 201 214 220 220 223 188 224 224 224 224 224\\r\\n 208 208 208 208 208 208 202 208 208 205 207 208 208 208 208 208\\r\\n 208 208 208 208 208 179  13 138 198  27 117 208 208 208 208 208\\r\\n 208 208 208 208 208 164   0 112 176   0  87 208 208 208 208 208\\r\\n 208 208 208 208 204 158   0 108 168   0  83 192 208 208 208 208\\r\\n 208 208 208 208  65   3   0   2   7   0   4   4 199 208 208 208\\r\\n 208 208 175 203  54   0   0   0   0   0   0   0 171 182 206 208\\r\\n 208 191   7  93  56   0   0   0   0   0   0   0 132   0 121 208\\r\\n 208 172   0  71  56   0   0   0   0   0   0   0 120   0 111 208\\r\\n 208 175   0  72  56   0   0   0   0   0   0   0 118   0 103 209\\r\\n 208 179   0  71  56   0   0   0   0   0   0   0 117   0 112 210\\r\\n 208 195   6  98  60   4   4   4   5   4   4   5 136   9 123 209\\r\\n 208 208 187 205 154 136 136 136 138 134 136 136 195 189 208 208\\r\\n 208 208 208 208  80   1  22   6   4  21  15  17 205 208 208 208\\r\\n 208 208 208 208 175  20 161   2   0  86  59 125 208 208 208 210\\r\\n 208 208 208 208 208 182  63  28  24  61 128 208 208 211 213 214\\r\\n 208 208 208 208 208 184 201 208 208 209 176 209 214 210 211 208\\r\\n 208 208 208 208 208 208 200 206 208 203 207 208 208 208 208 208\\r\\n 208 208 208 208 208 178  13 138 198  25 114 208 208 208 208 208\\r\\n 208 208 208 208 208 164   0 112 176   0  86 208 208 208 208 208\\r\\n 208 208 208 208 202 155   0 107 165   0  82 188 208 208 208 208\\r\\n 208 208 208 208  64   3   0   4   7   0   4   3 198 208 208 208\\r\\n 208 208 173 203  55   0   0   0   0   0   0   0 171 181 204 208\\r\\n 208 190   7  93  56   0   0   0   0   0   0   0 132   1 122 208\\r\\n 208 172   0  71  56   0   0   0   0   0   0   0 120   0 111 208\\r\\n 208 174   0  72  56   0   0   0   0   0   0   0 118   0 106 208\\r\\n 208 178   0  71  56   0   0   0   0   0   0   0 117   0 112 208\\r\\n 208 195   8  99  61   4   4   4   4   4   4   6 136  10 124 208\\r\\n 208 208 186 205 151 134 135 133 135 131 132 137 195 189 208 208\\r\\n 208 208 208 208  79   1  20   6   4  21  15  16 205 210 211 208\\r\\n 208 208 208 208 175  24 158   2   0  85  61 126 208 208 209 208\\r\\n 208 208 208 208 208 182  64  28  23  61 130 208 208 210 210 211\\r\\n 208 208 208 208 208 185 201 208 208 209 176 208 210 208 209 209\\r\\n\\r\\n 224 224 224 224 224 224 224 224 224 224 224 224 224 224 224 224\\r\\n 224 224 224 224 224 223 141 120 120 123 133 223 224 224 224 224\\r\\n 224 224 224 224 224 204  22   9   8  11  17 198 224 224 224 224\\r\\n 224 224 224 224 215 180 134 120 121 122 143 210 221 224 224 224\\r\\n 224 224 224 212  18   2   1   1   1   7  17 146  30 139 224 224\\r\\n 224 139  94  93   7   1   0   0   0   1   7 147  29  71 143 188\\r\\n 222  52   7   4   1   0   0   0   0   0   4 145  32  96  65 223\\r\\n 224 214 188 183  18   0   0   0   0   1   5 148  17   6  26 220\\r\\n 224 157 114 112   7   0   0   0   0   0   5 146  18  10  32 220\\r\\n 220  40   4   3   0   0   0   0   0   0   4 146  32 170  72 214\\r\\n 224 194 175 167  11   0   0   0   0   0   4 146  10  34 193 201\\r\\n 224 224 224 221  87  65  65  62  68  66  70 166  92 188 224 224\\r\\n 224 224 224 224 224 221 112  79  82  80 106 218 224 224 224 224\\r\\n 224 224 224 224 224 197  30   9   9  10  21 195 224 224 224 224\\r\\n 223 224 224 223 224 224 208 188 191 194 210 224 224 224 224 224\\r\\n 223 224 224 224 224 224 224 224 224 223 224 224 224 224 224 224\\r\\n 208 208 208 208 208 208 208 208 209 210 209 208 208 210 214 208\\r\\n 208 208 208 208 208 206 121 111 103 112 123 208 208 208 213 211\\r\\n 208 208 208 208 208 182   0   0   0   0   9 189 208 208 211 210\\r\\n 208 208 208 208 199 171 132 120 118 117 136 195 205 208 208 214\\r\\n 208 208 208 192   4   0   0   0   0   0   5 136  17 125 208 209\\r\\n 207 117  87  83   4   0   0   0   0   0   4 136  15  59 128 176\\r\\n 205  27   0   0   0   0   0   0   0   0   4 134  21  86  61 209\\r\\n 208 198 176 168   7   0   0   0   0   0   5 138   4   0  24 208\\r\\n 208 138 112 108   2   0   0   0   0   0   4 136   6   2  28 208\\r\\n 202  13   0   0   0   0   0   0   0   0   4 136  22 161  63 201\\r\\n 208 179 164 158   3   0   0   0   0   0   4 136   1  20 182 184\\r\\n 208 208 208 204  65  54  56  56  56  56  60 154  80 175 208 208\\r\\n 208 208 208 208 208 203  93  71  72  71  98 205 208 208 208 208\\r\\n 208 208 208 208 208 175   7   0   0   0   6 187 208 208 208 208\\r\\n 208 208 208 208 208 208 191 172 175 179 195 208 208 208 208 208\\r\\n 208 208 208 208 208 208 208 208 208 208 208 208 208 208 208 208\\r\\n 208 208 208 208 208 208 208 208 208 208 208 208 208 208 211 209\\r\\n 208 208 208 208 208 204 122 111 106 112 124 208 211 209 210 209\\r\\n 208 208 208 208 208 181   1   0   0   0  10 189 210 208 210 208\\r\\n 208 208 208 208 198 171 132 120 118 117 136 195 205 208 208 210\\r\\n 208 208 208 188   3   0   0   0   0   0   6 137  16 126 208 208\\r\\n 207 114  86  82   4   0   0   0   0   0   4 132  15  61 130 176\\r\\n 203  25   0   0   0   0   0   0   0   0   4 131  21  85  61 209\\r\\n 208 198 176 165   7   0   0   0   0   0   4 135   4   0  23 208\\r\\n 206 138 112 107   4   0   0   0   0   0   4 133   6   2  28 208\\r\\n 200  13   0   0   0   0   0   0   0   0   4 135  20 158  64 201\\r\\n 208 178 164 155   3   0   0   0   0   0   4 134   1  24 182 185\\r\\n 208 208 208 202  64  55  56  56  56  56  61 151  79 175 208 208\\r\\n 208 208 208 208 208 203  93  71  72  71  99 205 208 208 208 208\\r\\n 208 208 208 208 208 173   7   0   0   0   8 186 208 208 208 208\\r\\n 208 208 208 208 208 208 190 172 174 178 195 208 208 208 208 208\\r\\n 208 208 208 208 208 208 208 208 208 208 208 208 208 208 208 208\\r\\n\\r\\n 224 224 224 224 224 188 223 220 220 214 201 224 224 224 224 224\\r\\n 224 224 224 224 224 143  65  26  32  72 193 224 224 224 224 224\\r\\n 224 224 224 224 139  71  96   6  10 170  34 188 224 224 224 224\\r\\n 224 224 224 221  30  29  32  17  18  32  10  92 224 224 224 224\\r\\n 224 223 198 210 146 147 145 148 146 146 146 166 218 195 224 224\\r\\n 224 133  17 143  17   7   4   5   5   4   4  70 106  21 210 224\\r\\n 224 123  11 122   7   1   0   1   0   0   0  66  80  10 194 223\\r\\n 224 120   8 121   1   0   0   0   0   0   0  68  82   9 191 224\\r\\n 224 120   9 120   1   0   0   0   0   0   0  62  79   9 188 224\\r\\n 224 141  22 134   1   0   0   0   0   0   0  65 112  30 208 224\\r\\n 224 223 204 180   2   1   0   0   0   0   0  65 221 197 224 224\\r\\n 224 224 224 215  18   7   1  18   7   0  11  87 224 224 224 224\\r\\n 224 224 224 224 212  93   4 183 112   3 167 221 224 224 223 224\\r\\n 224 224 224 224 224  94   7 188 114   4 175 224 224 224 224 224\\r\\n 224 224 224 224 224 139  52 214 157  40 194 224 224 224 224 224\\r\\n 224 224 224 224 224 224 222 224 224 220 224 224 224 224 223 223\\r\\n 208 211 210 214 209 176 209 208 208 201 184 208 208 208 208 208\\r\\n 214 213 211 208 208 128  61  24  28  63 182 208 208 208 208 208\\r\\n 210 208 208 208 125  59  86   0   2 161  20 175 208 208 208 208\\r\\n 208 208 208 205  17  15  21   4   6  22   1  80 208 208 208 208\\r\\n 208 208 189 195 136 136 134 138 136 136 136 154 205 187 208 208\\r\\n 209 123   9 136   5   4   4   5   4   4   4  60  98   6 195 208\\r\\n 210 112   0 117   0   0   0   0   0   0   0  56  71   0 179 208\\r\\n 209 103   0 118   0   0   0   0   0   0   0  56  72   0 175 208\\r\\n 208 111   0 120   0   0   0   0   0   0   0  56  71   0 172 208\\r\\n 208 121   0 132   0   0   0   0   0   0   0  56  93   7 191 208\\r\\n 208 206 182 171   0   0   0   0   0   0   0  54 203 175 208 208\\r\\n 208 208 208 199   4   4   0   7   2   0   3  65 208 208 208 208\\r\\n 208 208 208 208 192  83   0 168 108   0 158 204 208 208 208 208\\r\\n 208 208 208 208 208  87   0 176 112   0 164 208 208 208 208 208\\r\\n 208 208 208 208 208 117  27 198 138  13 179 208 208 208 208 208\\r\\n 208 208 208 208 208 207 205 208 208 202 208 208 208 208 208 208\\r\\n 209 209 208 210 208 176 209 208 208 201 185 208 208 208 208 208\\r\\n 211 210 210 208 208 130  61  23  28  64 182 208 208 208 208 208\\r\\n 208 209 208 208 126  61  85   0   2 158  24 175 208 208 208 208\\r\\n 208 211 210 205  16  15  21   4   6  20   1  79 208 208 208 208\\r\\n 208 208 189 195 137 132 131 135 133 135 134 151 205 186 208 208\\r\\n 208 124  10 136   6   4   4   4   4   4   4  61  99   8 195 208\\r\\n 208 112   0 117   0   0   0   0   0   0   0  56  71   0 178 208\\r\\n 208 106   0 118   0   0   0   0   0   0   0  56  72   0 174 208\\r\\n 208 111   0 120   0   0   0   0   0   0   0  56  71   0 172 208\\r\\n 208 122   1 132   0   0   0   0   0   0   0  56  93   7 190 208\\r\\n 208 204 181 171   0   0   0   0   0   0   0  55 203 173 208 208\\r\\n 208 208 208 198   3   4   0   7   4   0   3  64 208 208 208 208\\r\\n 208 208 208 208 188  82   0 165 107   0 155 202 208 208 208 208\\r\\n 208 208 208 208 208  86   0 176 112   0 164 208 208 208 208 208\\r\\n 208 208 208 208 208 114  25 198 138  13 178 208 208 208 208 208\\r\\n 208 208 208 208 208 207 203 208 206 200 208 208 208 208 208 208\\r\\n\\r\\n 224 224 224 224 224 224 223 224 224 224 224 224 224 224 224 223\\r\\n 224 224 224 224 224 210 194 191 188 208 224 224 223 224 224 223\\r\\n 224 224 224 224 195  21  10   9   9  30 197 224 224 224 224 224\\r\\n 224 224 224 224 218 106  80  82  79 112 221 224 224 224 224 224\\r\\n 224 224 188  92 166  70  66  68  62  65  65  87 221 224 224 224\\r\\n 201 193  34  10 146   4   0   0   0   0   0  11 167 175 194 224\\r\\n 214  72 170  32 146   4   0   0   0   0   0   0   3   4  40 220\\r\\n 220  32  10  18 146   5   0   0   0   0   0   7 112 114 157 224\\r\\n 220  26   6  17 148   5   1   0   0   0   0  18 183 188 214 224\\r\\n 223  65  96  32 145   4   0   0   0   0   0   1   4   7  52 222\\r\\n 188 143  71  29 147   7   1   0   0   0   1   7  93  94 139 224\\r\\n 224 224 139  30 146  17   7   1   1   1   2  18 212 224 224 224\\r\\n 224 224 224 221 210 143 122 121 120 134 180 215 224 224 224 224\\r\\n 224 224 224 224 198  17  11   8   9  22 204 224 224 224 224 224\\r\\n 224 224 224 224 223 133 123 120 120 141 223 224 224 224 224 224\\r\\n 224 224 224 224 224 224 224 224 224 224 224 224 224 224 224 224\\r\\n 208 208 208 208 208 208 208 208 208 208 208 208 208 208 208 208\\r\\n 208 208 208 208 208 195 179 175 172 191 208 208 208 208 208 208\\r\\n 208 208 208 208 187   6   0   0   0   7 175 208 208 208 208 208\\r\\n 208 208 208 208 205  98  71  72  71  93 203 208 208 208 208 208\\r\\n 208 208 175  80 154  60  56  56  56  56  54  65 204 208 208 208\\r\\n 184 182  20   1 136   4   0   0   0   0   0   3 158 164 179 208\\r\\n 201  63 161  22 136   4   0   0   0   0   0   0   0   0  13 202\\r\\n 208  28   2   6 136   4   0   0   0   0   0   2 108 112 138 208\\r\\n 208  24   0   4 138   5   0   0   0   0   0   7 168 176 198 208\\r\\n 209  61  86  21 134   4   0   0   0   0   0   0   0   0  27 205\\r\\n 176 128  59  15 136   4   0   0   0   0   0   4  83  87 117 207\\r\\n 209 208 125  17 136   5   0   0   0   0   0   4 192 208 208 208\\r\\n 214 208 208 205 195 136 117 118 120 132 171 199 208 208 208 208\\r\\n 210 211 208 208 189   9   0   0   0   0 182 208 208 208 208 208\\r\\n 211 213 208 208 208 123 112 103 111 121 206 208 208 208 208 208\\r\\n 208 214 210 208 208 209 210 209 208 208 208 208 208 208 208 208\\r\\n 208 208 208 208 208 208 208 208 208 208 208 208 208 208 208 208\\r\\n 208 208 208 208 208 195 178 174 172 190 208 208 208 208 208 208\\r\\n 208 208 208 208 186   8   0   0   0   7 173 208 208 208 208 208\\r\\n 208 208 208 208 205  99  71  72  71  93 203 208 208 208 208 208\\r\\n 208 208 175  79 151  61  56  56  56  56  55  64 202 208 208 208\\r\\n 185 182  24   1 134   4   0   0   0   0   0   3 155 164 178 208\\r\\n 201  64 158  20 135   4   0   0   0   0   0   0   0   0  13 200\\r\\n 208  28   2   6 133   4   0   0   0   0   0   4 107 112 138 206\\r\\n 208  23   0   4 135   4   0   0   0   0   0   7 165 176 198 208\\r\\n 209  61  85  21 131   4   0   0   0   0   0   0   0   0  25 203\\r\\n 176 130  61  15 132   4   0   0   0   0   0   4  82  86 114 207\\r\\n 208 208 126  16 137   6   0   0   0   0   0   3 188 208 208 208\\r\\n 210 208 208 205 195 136 117 118 120 132 171 198 208 208 208 208\\r\\n 208 210 208 210 189  10   0   0   0   1 181 208 208 208 208 208\\r\\n 209 210 209 211 208 124 112 106 111 122 204 208 208 208 208 208\\r\\n 209 211 208 208 208 208 208 208 208 208 208 208 208 208 208 208\\r\\n\\r\\n\"}]";
                String sjson = "{\"id\":1,\"name\":\"First\",\"colorR\":232.0,\"colorG\":100.0,\"colorB\":100.0,\"positionX\":0.0,\"positionY\":0.0,\"definition\":\"# Blender v2.78 (sub 0) OBJ File: ''\\n# www.blender.org\\nmtllib basic.mtl\\no Cube\\nv 1.000000 -1.000000 -1.000000\\nv 1.000000 -1.000000 1.000000\\nv -1.000000 -1.000000 1.000000\\nv -1.000000 -1.000000 -1.000000\\nv 1.000000 1.000000 -0.999999\\nv 0.999999 1.000000 1.000001\\nv -1.000000 1.000000 1.000000\\nv -1.000000 1.000000 -1.000000\\nvn 0.0000 -1.0000 0.0000\\nvn 0.0000 1.0000 0.0000\\nvn 1.0000 0.0000 0.0000\\nvn -0.0000 -0.0000 1.0000\\nvn -1.0000 -0.0000 -0.0000\\nvn 0.0000 0.0000 -1.0000\\nusemtl Material\\ns off\\nf 1//1 2//1 3//1 4//1\\nf 5//2 8//2 7//2 6//2\\nf 1//3 5//3 6//3 2//3\\nf 2//4 6//4 7//4 3//4\\nf 3//5 7//5 8//5 4//5\\nf 5//6 1//6 4//6 8//6\\n\"}";
                JsonTranslator.setMarkersFromJson(json);
                try {
                    for(Marker marker : State.availableMarkers) {
                        this.saveToFile(marker.getLocalFileName(), marker.pattern);

                        Structure s = JsonTranslator.getStructureFromJson(sjson);
                        marker.setStucture(s);

                        artoolkit = MainActivity.super.getArtoolkit();
                        someObject = new CustomObject(marker.name, marker.getLocalFileName(), 80.0, s);
                        artoolkit.registerARObject(someObject);
                    }
                } catch (AndARException ex){
                    Log.d("JSON", ex.getMessage());
                }
                startPreview();

                return "";
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
