package io.emotionally.barycenter.emotionally;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.Console;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    /* Screen Switching */

    public void options_screen(View view) {
        setContentView(R.layout.options_view);
    }

    /*
    //private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;
    // https://developer.android.com/reference/android/view/WindowManager.LayoutParams.html#TYPE_APPLICATION_OVERLAY
    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2038;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    public void analyzeMessage(View view) {

        IBMToneAPI ita = new IBMToneAPI();

        EditText textbox = findViewById(R.id.editText);
        JSONObject analyzedJson = ita.analyze(textbox.getText().toString()); //JSONObject analyzedJson = ita.analyze("Hello, World! I am doing so good today, I probably got my app working :)");

        String jString = analyzedJson.toString();

        TextView textview = findViewById(R.id.textView);
        textview.setText(jString);

    }

    public void analyzeHead(View view) {
        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(MainActivity.this, AnalyzeService.class));
                finish();
            }
        });
    }*/

    public void buttonViewLaunch(View v){
        if (Settings.canDrawOverlays(this)) {
            // Launch service right away - the user has already previously granted permission
            launchMainService();
        }
        else {
            // Check that the user has granted permission, and prompt them if not
            checkDrawOverlayPermission();
        }
        //launchMainService();
    }

    private void launchMainService() {
        Intent svc = new Intent(this, MainService.class);
        stopService(svc);
        startService(svc);
        finish();
    }

    public final static int REQUEST_CODE = 10101;

    public void checkDrawOverlayPermission() {

        // Checks if app already has permission to draw overlays
        if (!Settings.canDrawOverlays(this)) {
            // If not, form up an Intent to launch the permission request
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
            // Launch Intent, with the supplied request code
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {

        // Check if a request code is received that matches that which we provided for the overlay draw request
        if (requestCode == REQUEST_CODE) {
            // Double-check that the user granted it, and didn't just dismiss the request
            if (Settings.canDrawOverlays(this)) {
                // Launch the service
                launchMainService();
            }
            else {
                Toast.makeText(this, "Sorry. Can't draw overlays without permission...", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        }

        startService( new Intent(MainActivity.this, HUD.class) );*/

// else {
//    initializeView();
//}

        //analyzeView();
        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API<23. But for API > 23
        //you have to ask for the permission in runtime.
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

            //If the draw over permission is not available open the settings screen
            //to grant the permission.
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, CODE_DRAW_OVER_OTHER_APP_PERMISSION);
        } else {

            Button analyze = (Button) findViewById(R.id.button);
            analyze.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    startService(new Intent(MainActivity.this, AnalyzeService.class));
                    Log.d("KeyboardTag", "Analysis service should have started.");
                    finish();
                }

            });

            analyzeView();
        }*/

    //}



    /*public void analyzeView() {

        Button analyze = (Button) findViewById(R.id.analyze_key);
        if (analyze != null && !analyze.equals(null)) {
            analyze.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                    startService(new Intent(MainActivity.this, AnalyzeService.class));
                    finish();
                }

            });
        }

    }*/


    /*
    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_DRAW_OVER_OTHER_APP_PERMISSION) {

            //Check if the permission is granted or not.
            // Settings activity never returns proper value so instead check with following method
            if (Settings.canDrawOverlays(this)) {

                Button analyze = (Button) findViewById(R.id.button);
                analyze.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        startService(new Intent(MainActivity.this, AnalyzeService.class));
                        finish();
                    }

                });

                analyzeView();
            } else { //Permission is not available
                Toast.makeText(this,
                        "Draw over other app permission not available. Closing the application",
                        Toast.LENGTH_SHORT).show();

                finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }*/

//}
