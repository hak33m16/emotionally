package com.example.hakeem.myapplication;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private JSONObject AnalyzedText = new JSONObject();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        main();
    }

    public void main() {
        Settings specs = new Settings("IBMToneAPI");
        User uname = new User("a51ba1b9-ba49-4b5e-b197-edc37eecd571", "dHXyey1MmoWC", specs);
        ApplicationController apc = new ApplicationController(uname);
        apc.apiController.populateAnalysis(apc.apiController.adaptor.getAPI(specs.selectedAPI).analyze("Some great weather we are having today, what do you think bob?"));
        apc.apiController.printAnalysis();
    }

    private void startAPIThread(final IBMToneAPI a){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("EMOTIONALLY", "Calling analyze");
                AnalyzedText = a.analyze("Hello World, How are you today?");
            }
        });
        thread.start();
    }

}
