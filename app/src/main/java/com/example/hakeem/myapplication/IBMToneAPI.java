package com.example.hakeem.myapplication;

import android.util.Log;
import android.util.Pair;

import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.ToneAnalyzer;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneAnalysis;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneInput;
import com.ibm.watson.developer_cloud.tone_analyzer.v3.model.ToneOptions;

import org.json.JSONException;
import org.json.JSONObject;

enum IBMEnum {sentences, document};

public class IBMToneAPI extends API {

	private ServiceCall call;
	private ToneAnalyzer server;
	private Pair<String,String> login;
	private IBMEnum mode;

	public IBMToneAPI(){
		server = new ToneAnalyzer("2017-09-21");
		login = Pair.create("a51ba1b9-ba49-4b5e-b197-edc37eecd571", "dHXyey1MmoWC");
		server.setUsernameAndPassword(login.first, login.second);
	}

	public JSONObject analyze(String message){
		String jMsg = "{\"text\":}" + '\"' + message + "\"}";
		JSONObject rtn = new JSONObject();
		try {
			JSONObject jObject = new JSONObject(jMsg);
			ToneInput toneInput = new ToneInput.Builder().text(jObject.get("text").toString()).build();
			ToneOptions options = new ToneOptions.Builder().toneInput(toneInput).build();
			ToneAnalysis tone = server.tone(options).execute();
			return rtn.getJSONObject(tone.toString());
		} catch (JSONException e){
			Log.e("JSONERROR", "Invalid JSON");
		}
		return rtn;
	}

}