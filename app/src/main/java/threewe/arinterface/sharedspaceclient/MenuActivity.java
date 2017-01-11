package threewe.arinterface.sharedspaceclient;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import threewe.arinterface.sharedspaceclient.config.URLs;
import threewe.arinterface.sharedspaceclient.utils.async.SessionsTask;

public class MenuActivity extends AppCompatActivity {

    private Button createButton;
    private Button joinButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        createButton = (Button) findViewById(R.id.create_session_button);
        joinButton = (Button) findViewById(R.id.join_session_button);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SessionsTask().execute();
            }
        });

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
