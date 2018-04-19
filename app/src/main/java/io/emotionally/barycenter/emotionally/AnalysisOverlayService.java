package io.emotionally.barycenter.emotionally;

/**
 * Created by Hakeem on 3/10/2018.
 */


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 *
 * Created by matt on 08/08/2016.
 *
 * Extended by: Abdel-Hakeem Badran
 * Date: 04/19/2018
 *
 */

public class AnalysisOverlayService extends Service implements View.OnTouchListener, View.OnGenericMotionListener {

    private static final String TAG = AnalysisOverlayService.class.getSimpleName();

    private WindowManager windowManager;

    private View floatyView;

    private String displayText = "empty";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("EMOTIONALLY", "We've made it to the onStartCommand function in AnalysisOverlayService.java");

        Bundle extras = intent.getExtras();
        if (extras == null) {
            Log.d("EMOTIONALLY", "The analysis didn't make it.");
        } else {
            displayText = (String) extras.get("ANALYSIS");
        }

        addOverlayView();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {

        Log.d("EMOTIONALLY", "We've made it to the IBinder function in AnalysisOverlayService.java");

        Bundle extras = intent.getExtras();
        if (extras == null) {
            Log.d("EMOTIONALLY", "The analysis didn't make it.");
        } else {
            displayText = (String) extras.get("ANALYSIS");
        }

        return null;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        //addOverlayView();
    }

    private void addOverlayView() {

        final WindowManager.LayoutParams params =
                new WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT,
                        WindowManager.LayoutParams.TYPE_PHONE,
                        0,
                        PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER | Gravity.START;
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

                        // As we've taken action, we'll return true to prevent other apps from consuming the event as well
                        return true;
                    }
                }

                // Otherwise don't intercept the event
                return super.dispatchKeyEvent(event);
            }
        };

        Log.d("EMOTIONALLY", "But did we do this AFTER making it to the onStart?");
        floatyView = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.floating_alert, interceptorLayout);

        floatyView.setOnTouchListener(this);

        TextView testModify = floatyView.findViewById(R.id.float_analysis_text);
        testModify.setText(displayText);

        windowManager.addView(floatyView, params);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        if (floatyView != null) {

            windowManager.removeView(floatyView);

            floatyView = null;
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        Log.v(TAG, "onTouch...");

        // Kill service
        onDestroy();

        return true;
    }

    @Override
    public boolean onGenericMotion(View view, MotionEvent motionEvent) {

        //motionEvent.getButtonState();

        return true;
    }

}