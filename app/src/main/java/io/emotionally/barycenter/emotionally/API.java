package io.emotionally.barycenter.emotionally;

import android.graphics.Color;
import android.util.Pair;

import com.github.mikephil.charting.charts.RadarChart;
import org.json.JSONObject;
import java.util.List;
import java.util.Map;

///////////////////////////////////
//
// This is our abstract API class.
// 'public' functions in Java are
// by default 'virtual' as in C++
//

public class API {

    // Static method that will be utilized
	// by each concrete API class in future.
    public static RadarChart generateGraph(JSONObject analysisJSON) {
		RadarChart radarChart;
        return  null;
	}

	public List<String> getAnalysisLabels() {
		return null;
	}
    public Map<String, Integer> getLabelColors() { return null; }

    public String analyze(String message){
        return "";
    }

}
