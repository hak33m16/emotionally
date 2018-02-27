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
import android.widget.Toast;

import java.io.Console;

public class MainActivity extends AppCompatActivity {

    private static final int CODE_DRAW_OVER_OTHER_APP_PERMISSION = 2084;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //analyzeView();
        setContentView(R.layout.activity_main);

        //Check if the application has draw over other apps permission or not?
        //This permission is by default available for API<23. But for API > 23
        //you have to ask for the permission in runtime.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {

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
        }



    }

    public void analyzeView() {

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

    }


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

}
