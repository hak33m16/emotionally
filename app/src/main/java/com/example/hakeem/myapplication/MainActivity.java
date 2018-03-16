package com.example.hakeem.myapplication;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    Settings specs = new Settings("IBMToneAPI");
    User uname = new User("a51ba1b9-ba49-4b5e-b197-edc37eecd571", "dHXyey1MmoWC", specs);
    ApplicationController apc = new ApplicationController();

    String testMessage = "Some great weather we are having today, what do you think bob? I fucked your wife.";

    TextView analysisTextField = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        analysisTextField = (TextView) findViewById(R.id.analysis_response_field);
        apc = new ApplicationController();

    }

    public void analyzeButton(View view) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                final String response = apc.getAnalysisApiController().getApiAdaptor().getAPI("IBMToneAPI").analyze(testMessage);

                // Must run on UI thread, modifies UI
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainViewModifier(response);
                    }
                });

            }
        };

        new Thread(runnable).start();

    }

    public void mainViewModifier(String msg) {

        analysisTextField.setText(msg);

    }

}
