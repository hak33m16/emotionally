package io.emotionally.barycenter.emotionally;

/**
 * Created by Hakeem on 2/18/2018.
 */

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class AnalysisAPIController {

    private Analysis analysis;
    private APIAdaptor adaptor;

    public void AnalysisAPIController(User u){};
    public JSONObject populateAnalysis(){
        String jStr = "{\"key\":\"value\"}";
        try {
            return new JSONObject(jStr);
        } catch (JSONException e) {
            Log.d("MYAPP", "unexpected JSON Exception");
        }
        return new JSONObject();
    }

}
