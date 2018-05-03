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
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.util.Pair;
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
import com.github.mikephil.charting.highlight.Range;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

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
    private APIAdaptor apiAdaptor;

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

        apiAdaptor = new APIAdaptor();
        String action = intent.getAction();

        if ( action == null ) return;
        Log.d("EMOTIONALLY", "The action has been read as: " + action);

        switch (action) {
            case "io.emotionally.barycenter.emotionally.ACTION_ANALYZE":
                // Can't find out how to stop this exception. Odd.
                addOverlayView(
                        (String) intent.getExtras().get("io.emotionally.barycenter.emotionally.ANALYSIS_TEXT"),
                        (String) intent.getExtras().get("io.emotionally.barycenter.emotionally.ANALYSIS_API_ID")
                );
                break;
            case "io.emotionally.barycenter.emotionally.CLOSE":
                onDestroy();
                break;
            default:
                break;
        }

    }

    private void addOverlayView(String analysis, String API_ID) {

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
        //scrollableTextBox.setText(analysis);

        List<SpannableString> sentences = coloredStringBuilder(analysis, API_ID);
        for ( SpannableString sentence : sentences ) {
            scrollableTextBox.append( sentence );
        }

        RadarChart analysisChart = (RadarChart) floatingView.findViewById(R.id.float_analysis_radarchart);
        analysisChart = graphGenerator(analysisChart, analysis, API_ID);

        windowManager.addView(floatingView, params);
    }

    public RadarChart graphGenerator(RadarChart analysisChart, String analysis, String API_ID) {

        API currentAPI = apiAdaptor.getAPI(API_ID);
        final List<String> labels = currentAPI.getAnalysisLabels();
        JSONObject analysisJSON, documentJSON, sentenceJSON;

        Boolean sentencesExist = true;
        JSONArray sentences = null;

        List< Pair<String, Float> > documentAnalysisData = new LinkedList< Pair<String, Float> >();

        for ( String label : labels ) {
            documentAnalysisData.add( Pair.create(label, 0.05f) );
        }

        try {
            analysisJSON = new JSONObject(analysis);
            documentJSON = new JSONObject( analysisJSON.getJSONObject("document_tone").toString() );

            JSONArray tonesArray = (JSONArray) documentJSON.get("tones");
            //JSONObject tones = new JSONObject( tonesArray.get(0).toString() );

            for (int i = 0; i < tonesArray.length(); ++ i) {
                JSONObject tone = new JSONObject( tonesArray.get(i).toString() );

                String toneName = tone.get("tone_name").toString();
                Float toneScore = Float.parseFloat( tone.get("score").toString() );

                //documentAnalysisData.put(toneName, toneScore);
                for (int j = 0; j < documentAnalysisData.size(); ++ j) {
                    // If we find the tone with our name, replace the 0 in it
                    // with an actual value.
                    if ( documentAnalysisData.get(j).first.equals(toneName) ) {
                        documentAnalysisData.set(j, Pair.create(toneName, toneScore));
                    }

                }

            }

        } catch ( org.json.JSONException e ) {
            Log.d("EMOTIONALLY", "Bad JSON detected in graphGenerator()");
            return null;
        }

        Iterator<?> keys = documentJSON.keys();
        while ( keys.hasNext() ) {
            String key = (String) keys.next();
            Log.d("EMOTIONALLY", "Key: " + key);
        }

        ArrayList<RadarEntry> entries = new ArrayList<>();

        for ( Pair<String, Float> entry : documentAnalysisData ) {
            entries.add( new RadarEntry(entry.second, entry.first) );
        }

        RadarDataSet radarDataSet = new RadarDataSet(entries, "Score (0 - 1)");
        radarDataSet.setFillColor(Color.CYAN);
        radarDataSet.setFillAlpha(100); // Out of 255
        radarDataSet.setDrawFilled(true);
        radarDataSet.setDrawValues(false);

        XAxis xAxis = analysisChart.getXAxis();
        xAxis.setValueFormatter(new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return labels.get( (int) value );
            }

        });
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(1);

        RadarData radarData = new RadarData(radarDataSet);

        analysisChart.setData(radarData);
        analysisChart.setWebColor(Color.BLACK);
        analysisChart.setBackgroundColor(Color.WHITE);
        analysisChart.setWebColorInner(Color.GRAY);

        Description graphDescription = new Description();
        graphDescription.setText("Document Tone Summary");
        graphDescription.setTextAlign(Paint.Align.RIGHT);

        analysisChart.setDescription( graphDescription );

        return analysisChart;
    }

    public List<SpannableString> coloredStringBuilder(String analysis, String API_ID) {

        API currentAPI = apiAdaptor.getAPI(API_ID);
        Map<String, Integer> colorMap = currentAPI.getLabelColors();
        JSONObject analysisJSON = null;
        JSONArray sentencesJSON = null;

        List<SpannableString> coloredSentences = new LinkedList<SpannableString>();

        Log.d("EMOTIONALLY", "Made it to the colored string builder.");

        // Here we will take each color, and generate
        // a key for the user.
        List<String> labels = currentAPI.getAnalysisLabels();
        for ( int i = 0; i < labels.size(); ++ i ) {
            SpannableString keyColoredString = new SpannableString( labels.get(i) + "\n" );
            keyColoredString.setSpan( new BackgroundColorSpan(colorMap.get(labels.get(i))), 0, keyColoredString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            coloredSentences.add(keyColoredString);
        }

        // This is where we'll take the content out
        // of the JSON and color correlate it.
        try {
            // Parse each sentence from JSON
            //List<JSONArray> sentences = new LinkedList<JSONArray>();

            analysisJSON = new JSONObject(analysis);
            sentencesJSON = new JSONArray ( analysisJSON.get("sentences_tone").toString() );

            Log.d("EMOTIONALLY", sentencesJSON.toString() );

            // Iterate through the sentences if they exist
            //JSONArray sentenceArray = (JSONArray) analysisJSON.get("sentence_tones");
            for (int i = 0; i < sentencesJSON.length(); ++ i) {

                JSONObject sentence = new JSONObject( sentencesJSON.get(i).toString() );
                String sentenceContent = sentence.get("text").toString();

                Log.d("EMOTIONALLY", "Sentence Content: " + sentenceContent);

                // Take the array of 'tones' and grab the first toneObject from it,
                // as this will be our highest score in tone.
                JSONArray toneObjectArray = new JSONArray( sentence.get("tones").toString() );
                JSONObject toneObject = new JSONObject( toneObjectArray.get(0).toString() );

                String toneName = toneObject.get("tone_name").toString();
                Float toneScore = Float.parseFloat( toneObject.get("score").toString() );

                // Now that we've pulled all of our data, create an object.
                SpannableString coloredString = new SpannableString(sentenceContent);
                coloredString.setSpan( new BackgroundColorSpan(colorMap.get(toneName)), 0, coloredString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                // Let's put it into our list
                coloredSentences.add(coloredString);

            }

        } catch ( JSONException e ) {
            e.printStackTrace();
        }

        return coloredSentences;

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