package threewe.arinterface.sharedspaceclient;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import threewe.arinterface.sharedspaceclient.config.URLs;
import threewe.arinterface.sharedspaceclient.utils.State;
import threewe.arinterface.sharedspaceclient.utils.URLUtils;
import threewe.arinterface.sharedspaceclient.utils.async.JoinSessionsTask;
import threewe.arinterface.sharedspaceclient.utils.async.SessionsTask;
import threewe.arinterface.sharedspaceclient.utils.async.TokenTask;

/**
 * Created by Dawid Pacholczyk <dpacholczyk@outlook.com>
 */

public class MenuActivity extends AppCompatActivity {

    private Button createButton;
    private Button joinButton;
    private TextView yourDeviceId;

    private String sessionId;

    public static String manufacturer;
    public static String deviceId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // special server configuration START
        URLs.setProtocol("http");
        URLs.setPort("9000");
        URLs.setAdress("192.168.1.224");
        URLs.setAdresses();
        // special server configuration STOP

        manufacturer = Build.MANUFACTURER + ": " + Build.MODEL;

//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        deviceId = Build.ID;
//        Log.d("WIADOMOSCI", "Device id: " + deviceId + " : Refreshed tokenn: " + refreshedToken );
//        new TokenTask(deviceId, refreshedToken).execute();


//        createButton = (Button) findViewById(R.id.create_session_button);
        joinButton = (Button) findViewById(R.id.join_session_button);
        yourDeviceId = (TextView) findViewById(R.id.your_device_label);

        yourDeviceId.setText(Build.ID);

//        createButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                new SessionsTask().execute();
//            }
//        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
                builder.setTitle(getResources().getString(R.string.join_session_title));
                final EditText input = new EditText(MenuActivity.this);
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                input.setText("1");
                builder.setView(input);

                builder.setPositiveButton(getResources().getString(R.string.join_session_short), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sessionId = input.getText().toString();
                        new JoinSessionsTask(MenuActivity.this, sessionId).execute();
                    }
                });
                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
            }
        });
    }
}
