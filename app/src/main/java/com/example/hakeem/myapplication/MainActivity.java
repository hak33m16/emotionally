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

    private JSONObject AnalyzedText = new JSONObject();
    //AnalysisAPIController anaApiCon = null;
    Settings specs = new Settings("IBMToneAPI");
    User uname = new User("a51ba1b9-ba49-4b5e-b197-edc37eecd571", "dHXyey1MmoWC", specs);
    ApplicationController apc = new ApplicationController();

    TextView analysisTextField = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        analysisTextField = (TextView) findViewById(R.id.analysis_response_field);
        apc = new ApplicationController();

        main();
    }

    public void main() {
        //apc.getAnalysisApiController().populateAnalysis(apc.getAnalysisApiController().adaptor.getAPI("IBMToneAPI").analyze("Some great weather we are having today, what do you think bob?"));
        //API currentAPI = apc.getAnalysisApiController();//getApiAdaptor().getAPI("IBMToneAPI");
        //anaApiCon.populateAnalysis(anaApiCon.adaptor.getAPI("IBMToneAPI").analyze("Some great weather we are having today, what do you think bob?"));

        String testMessage = "Some great weather we are having today, what do you think bob? I fucked your wife.";
        //APICall(apc.getAnalysisApiController().getApiAdaptor().getAPI("IBMToneAPI"), testMessage);

        //APICall(apc, testMessage);

        //AnalysisAPIController anaApiCon = apc.getAnalysisApiController();

        /*Log.d("EMOTIONALLY", "0");
        Settings specs = new Settings("IBMToneAPI");
        Log.d("EMOTIONALLY", "1");
        User uname = new User("a51ba1b9-ba49-4b5e-b197-edc37eecd571", "dHXyey1MmoWC", specs);
        Log.d("EMOTIONALLY", "2");
        ApplicationController apc = new ApplicationController(uname);
        Log.d("EMOTIONALLY", "3");
        apc.apiController.populateAnalysis(apc.apiController.adaptor.getAPI(specs.selectedAPI).analyze("Some great weather we are having today, what do you think bob?"));
        Log.d("EMOTIONALLY", "4");
        apc.apiController.printAnalysis();
        Log.d("EMOTIONALLY", "5");*/
    }

    public void analyzeButton(View view) {

        /*Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });*/

        String testMessage = "Some great weather we are having today, what do you think bob? I fucked your wife.";
        APICall(apc, testMessage);

        //setContentView(R.layout.activity_main);
        //TextView analysisTextField = (TextView) findViewById(R.id.analysis_response_field);



        //analysisTextField.setText( apc.getAnalysisApiController().getAnalysis().toString() );

        //TextView analysis_field = (TextView) findViewById(R.layout.activity_main.)

    }

    public void changeText(String text) {
        analysisTextField.setText( text );
    }


    /*class APIThread extends Thread {

        public Handler apiHandler;

        @Override
        public void run() {

            Looper.prepare();

            apiHandler = new Handler() {

                @Override
                public void handleMessage(Message msg) {

                }

            }

        }

    }*/

    private void APICall(final ApplicationController apc, final String message) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("EMOTIONALLY", "Calling analyze");
                apc.getAnalysisApiController().populateAnalysis(apc.getAnalysisApiController().getApiAdaptor().getAPI("IBMToneAPI").analyze(message));
                //apc.getAnalysisApiController().printAnalysis();

                JSONObject hopefully = apc.getAnalysisApiController().getAnalysis();
                Log.d("EMOTIONALLY", hopefully.toString());

                //changeText( hopefully.toString() );

                //analysisTextField.setText( apc.getAnalysisApiController().getAnalysis().toString() );
                //analysisTextField.setText( hopefully.toString() );
                /*if (api.message_analyzed()) {

                    //return response;
                }*/

            }
        });

        thread.start();

    }

    /*private void startAPIThread(final IBMToneAPI a){
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("EMOTIONALLY", "Calling analyze");
                AnalyzedText = a.analyze("Hello World, How are you today?");
            }
        });
        thread.start();
    }*/

}
