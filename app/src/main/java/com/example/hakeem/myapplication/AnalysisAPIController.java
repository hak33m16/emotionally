package com.example.hakeem.myapplication;

/**
 * Created by Hakeem on 2/18/2018.
 */

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class AnalysisAPIController {

    private Analysis analysis;
    public APIAdaptor adaptor;

    public void AnalysisAPIController(Settings settings){
        adaptor.getAPI(settings.selectedAPI);
    }

    public void populateAnalysis(JSONObject analyzedResponse){
        analysis.message = analyzedResponse.toString();
    }

    public void printAnalysis(){
        Log.d("EMOTIONALLY", analysis.message);
    }

}