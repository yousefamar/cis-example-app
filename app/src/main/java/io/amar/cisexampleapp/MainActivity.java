package io.amar.cisexampleapp;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.sensingkit.sensingkitlib.*;
import org.sensingkit.sensingkitlib.data.SKLightData;
import org.sensingkit.sensingkitlib.data.SKSensorData;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;
    private final AsyncHttpClient client = new AsyncHttpClient();
    private SensingKitLibInterface mSensingKitLib;

    private String group;
    private String type;

    private void sendData(float value) {
        RequestParams params = new RequestParams();
        params.put("group", group);
        params.put("type", type);
        params.put("value", value);
        client.post("https://cis.amar.io/write", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mActionBarToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);
        getSupportActionBar().setTitle("CIS Challenge Example App");

        Button button = (Button) findViewById(R.id.button);
        try {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        group = ((EditText) findViewById(R.id.groupName)).getText().toString();
                        type = ((EditText) findViewById(R.id.sensorType)).getText().toString();
                        mSensingKitLib.startContinuousSensingWithSensor(SKSensorModuleType.LIGHT);
                        ((TextView) findViewById(R.id.textView)).setText("Your phone is now streaming light sensor data to https://cis.amar.io/" + group + "/" + type);
                    } catch (SKException e) {
                    } finally {
                        v.setEnabled(false);
                    }
                }
            });
        } catch (NullPointerException e) {
        }

        try {
            mSensingKitLib = SensingKitLib.getSensingKitLib(this);
            mSensingKitLib.registerSensorModule(SKSensorModuleType.LIGHT);
            mSensingKitLib.subscribeSensorDataListener(SKSensorModuleType.LIGHT, new SKSensorDataListener() {
                @Override
                public void onDataReceived(final SKSensorModuleType moduleType, final SKSensorData sensorData) {
                    sendData(((SKLightData) sensorData).getLight());
                }
            });
        } catch (SKException e) {
        }

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://io.amar.cisexampleapp/http/host/path")
        );
        AppIndex.AppIndexApi.start(client2, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://io.amar.cisexampleapp/http/host/path")
        );
        AppIndex.AppIndexApi.end(client2, viewAction);
        client2.disconnect();
    }
}
