package io.emotionally.barycenter.emotionally;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //ApplicationController   apc;
    //ProgressBar             analysisStatus;
    //EditText                mainActivityTextBox;

    ////////////////////////////////////////////////////////
    //
    // Overridden Methods from Parent
    //
    ///////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mainToolbar = (Toolbar) findViewById(R.id.settings_bar);
        //TODO set colors in layout and not in backend
        /*mainToolbar.setBackground(
                new ColorDrawable(Color.RED )
        );*/
        //mainToolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mainToolbar);

        // Hide loading bar by default
        //analysisStatus = findViewById(R.id.analysis_progress_bar);
        //analysisStatus.setVisibility( View.INVISIBLE );

        //mainActivityTextBox = findViewById(R.id.analysis_input_main_activity);

        //apc = new ApplicationController();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.android_settings:
                Intent settings = new Intent(this, SettingsActivity.class);
                startActivity(settings);
                break;
            case R.id.keyboard_options:
                Intent options = new Intent(this, OptionsActivity.class);
                startActivity(options);
                break;
            case R.id.help:
                Intent help = new Intent(this, HelpActivity.class);
                startActivity(help);
                break;
            default:
                //error
        }
        return super.onOptionsItemSelected(item);
    }

    ////////////////////////////////////////////////////////////
    //
    // VIEW MANIPULATION FUNCTIONS
    //
    //////////////////////////////////

    /* Screen Switching */
    public void options_screen(View view) {
        setContentView(R.layout.options_view);
    }

    /*public void analyzeButton(View view) {

        // Display loading bar in main UI
        //analysisStatus.setVisibility( View.VISIBLE );

        // Act as a buffer between view hierarchies
        //final String currentUserMessage = mainActivityTextBox.getText().toString();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //final String response = "Let's not waste API calls right now.";
                final String response = apc.getAnalysisApiController().getApiAdaptor().getAPI("IBMToneAPI").analyze( currentUserMessage);
                Log.d("EMOTIONALLY", response);

                //launchMainService(response);

            }
        };

        new Thread(runnable).start();

    }*/

    /*
    public void closeAnalysisService(View view) {

        //Intent svc = new Intent(this, AnalysisOverlayService.class);
        //finish();
        // Close activity if it is already open
        //stopService(serviceIntent);
        //finish();

    }*/

    /*
    private void launchMainService( String text ) {

        // Analysis is done if we've reached this point
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                analysisStatus.setVisibility( View.INVISIBLE );
            }
        });

        Intent svc = new Intent(this, AnalysisOverlayService.class);
        svc.putExtra("io.emotionally.barycenter.emotionally.ANALYSIS", text);

        // Close activity if it is already open
        stopService(svc);
        startService(svc);
        finish();

    }*/

    public final static int REQUEST_CODE = 10101;

    ///////////////////////////////////////////////////////////
    //
    // Permission-Based Functions
    //
    //////////////////////////////////

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
                //launchMainService("this should not have happened");
            }
            else {
                Toast.makeText(this, "Sorry. Can't draw overlays without permission...", Toast.LENGTH_SHORT).show();
            }
        }
    }

}

