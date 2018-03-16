package io.emotionally.barycenter.emotionally;

/**
 * Created by Hakeem on 2/18/2018.
 */

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class AnalysisAPIController {

    private Analysis analysis;
    public APIAdaptor adaptor;

    public AnalysisAPIController(){
        analysis = new Analysis();
        adaptor = new APIAdaptor();
    }

    public APIAdaptor getApiAdaptor(){
        return adaptor;
    }

    public AnalysisAPIController(Settings settings){
        adaptor.getAPI(settings.selectedAPI);
    }

    public void populateAnalysis(JSONObject analyzedResponse){
        analysis.message = analyzedResponse.toString();
    }

    public void printAnalysis(){
        Log.d("EMOTIONALLY", analysis.message);
    }

    public JSONObject getAnalysis(){
        try{
            JSONObject jObj = new JSONObject(analysis.message);
            return jObj;
        } catch (JSONException e){
            Log.e("EMOTIONALLY", "BAD JSON");
        }
        return new JSONObject();
    }
}
