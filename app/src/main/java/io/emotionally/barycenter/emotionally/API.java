package io.emotionally.barycenter.emotionally;

import com.github.mikephil.charting.charts.RadarChart;

import org.json.JSONObject;

///////////////////////////////////
//
// This is our abstract API class.
// 'public' functions in Java are
// by default 'virtual' as in C++
//

public class API {

	protected String API_ID;

	public API(){
	    API_ID = "";
	}

	public API(String a){
	    API_ID = a;
    }

    // Static method that will be utilized
	// by each concrete API class.
    public static RadarChart generateGraph(JSONObject analysisJSON) {
		RadarChart radarChart;
        return  null;
	}

    /*public JSONObject analyze(String message){
        return new JSONObject();
    }*/

    public String analyze(String message){
        return "";
    }

}
