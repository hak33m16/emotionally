package io.emotionally.barycenter.emotionally;

/**
 * Created by Hakeem on 3/10/2018.
 */


import android.annotation.SuppressLint;
import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.RadarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.RadarData;
import com.github.mikephil.charting.data.RadarDataSet;
import com.github.mikephil.charting.data.RadarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 *
 * Created by matt on 08/08/2016.
 *
 * Extended by: Abdel-Hakeem Badran
 * Date: 04/19/2018
 *
 */

public class AnalysisOverlayService extends IntentService implements View.OnTouchListener, View.OnGenericMotionListener {

    private static final String TAG = "EMOTIONALLY";
    private WindowManager windowManager;
    private View floatingView;
    private String displayText;

    public class CloseButtonOnClickListener implements View.OnClickListener {

        private Context appContext;
        public CloseButtonOnClickListener(Context context) {
            appContext = context;
        }

        @Override
        public void onClick(View view) {
            // Call function in owning class.
            onDestroy();
        }

    }

    public AnalysisOverlayService() {
        super("AnalysisOverlayService");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // Prevent code duplication and pass intent to
        // our intent handler. :)
        onHandleIntent(intent);

        return START_NOT_STICKY;

    }

    @Override
    public IBinder onBind(Intent intent) {

        // Prevent code duplication and pass intent to
        // our intent handler. :)
        onHandleIntent(intent);

        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();
        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String action = intent.getAction();

        if ( action == null ) return;
        Log.d("EMOTIONALLY", "The action has been read as: " + action);

        switch (action) {
            case "io.emotionally.barycenter.emotionally.ACTION_ANALYZE":
                // Can't find out how to stop this exception. Odd.
                addOverlayView((String) intent.getExtras().get("io.emotionally.barycenter.emotionally.ANALYSIS_TEXT"));
                break;
            case "io.emotionally.barycenter.emotionally.CLOSE":
                onDestroy();
                break;
            default:
                break;
        }

    }

    private void addOverlayView(String analysis) {

        //JsonParser parser = new JsonParser();
        //JSONObject analysisJSON = new JSONObject( (JSONObject) analysis);

        ////////////////////////////////////////////////////////////////
        //
        // Use access to display the system overlay via WindowManager
        //

        final WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        0,
                        PixelFormat.TRANSLUCENT);

        //params.gravity = Gravity.CENTER | Gravity.START;
        params.x = 0;
        params.y = 0;

        FrameLayout interceptorLayout = new FrameLayout(this) {

            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {

                // Only fire on the ACTION_DOWN event, or you'll get two events (one for _DOWN, one for _UP)
                if (event.getAction() == KeyEvent.ACTION_DOWN) {

                    // Check if the HOME button is pressed
                    if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {

                        Log.v(TAG, "BACK Button Pressed");
                        onDestroy();
                        // As we've taken action, we'll return true to prevent other apps from consuming the event as well
                        return true;
                    }
                }

                // Otherwise don't intercept the event
                return super.dispatchKeyEvent(event);
            }
        };

        // "Inflate" the view utilizing our WindowManager
        floatingView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.floating_analysis_alert, interceptorLayout);

        floatingView.setOnTouchListener(this);

        ImageButton exitButton = (ImageButton) floatingView.findViewById(R.id.float_analysis_exit_button);
        exitButton.setOnClickListener( new CloseButtonOnClickListener(this) );

        TextView scrollableTextBox = floatingView.findViewById(R.id.float_analysis_text);
        scrollableTextBox.setText(analysis);

        RadarChart analysisChart = (RadarChart) floatingView.findViewById(R.id.float_analysis_radarchart);

        // Data population for chart //

        ArrayList<RadarEntry> entries = new ArrayList<>();
        entries.add(new RadarEntry(4f, "Anger"));
        entries.add(new RadarEntry(5f, "Fear"));
        entries.add(new RadarEntry(2f, "Joy"));
        entries.add(new RadarEntry(3f, "Sadness"));
        entries.add(new RadarEntry(1f, "Analytical"));
        entries.add(new RadarEntry(5f, "Confident"));
        entries.add(new RadarEntry(3f, "Tentative"));

        RadarDataSet radarDataSet = new RadarDataSet(entries, "Score (0 - 1)");
        radarDataSet.setFillColor(Color.CYAN);
        radarDataSet.setFillAlpha(100); // Out of 255
        radarDataSet.setDrawFilled(true);
        radarDataSet.setDrawValues(false);

        XAxis xAxis = analysisChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {

                String label;
                switch((int) value) {
                    case 0: label = "Anger";
                        break;
                    case 1: label = "Fear";
                        break;
                    case 2: label = "Joy";
                        break;
                    case 3: label = "Sadness";
                        break;
                    case 4: label = "Analytical";
                        break;
                    case 5: label = "Confident";
                        break;
                    case 6: label = "Tentative";
                        break;
                    default: label = "! ERROR !";
                        break;
                }

                return label;

            }

        });

        RadarData radarData = new RadarData(radarDataSet);

        analysisChart.setData(radarData);
        analysisChart.setWebColor(Color.BLACK);
        analysisChart.setBackgroundColor(Color.WHITE);
        analysisChart.setWebColorInner(Color.GRAY);

        Description graphDescription = new Description();
        graphDescription.setText("Document Tone Summary");
        graphDescription.setTextAlign(Paint.Align.RIGHT);
        //graphDescription.setPosition( graphDescription.getPosition().x + 50, graphDescription.getPosition().y + 50 );

        analysisChart.setDescription( graphDescription );

        // -- //

        windowManager.addView(floatingView, params);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        if ( floatingView != null ) {
            windowManager.removeView(floatingView);
            floatingView = null;
        }

        //unregisterReceiver( receiver );

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        Log.d("EMOTIONALLY", "onTouch...");

        //view.performClick();

        //motionEvent.getAction();


        //closeButton.setPressed(true);

        //Log.d("EMOTIONALLY", "Image Button Status: " + closeButton.isPressed() );
        //if ( closeButtonPressed() ) {
            // Kills the analysis screen.
        //    onDestroy();
        //}

        return true;
    }



    @Override
    public boolean onGenericMotion(View view, MotionEvent motionEvent) {

        Log.d("EMOTIONALLY", "Generic Button State: " + motionEvent.getButtonState() );

        return true;
    }

    //////////////////////////////////////////////////
    //
    // Functions Dependent on Receiving Intent
    //
    //////////////////


}