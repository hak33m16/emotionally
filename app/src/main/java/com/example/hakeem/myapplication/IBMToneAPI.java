package com.example.hakeem.myapplication;

import android.util.Log;
import android.util.Pair;

import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.http.ServiceCallback;
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
		String jMsg = "{\"text\":" + '\"' + message + "\"}";
		Log.d("EMOTIONALLY", "Message To Analyze: " + jMsg);
		JSONObject rtn = new JSONObject();
		try {
			JSONObject jObject = new JSONObject(jMsg);
			Log.d("EMOTIONALLY", "JSONObject: " + jObject.toString());
			Log.d("EMOTIONALLY", jObject.get("text").toString());
			ToneInput toneInput = new ToneInput.Builder().text(jObject.get("text").toString()).build();
			Log.d("EMOTIONALLY", toneInput.text());
			ToneOptions options = new ToneOptions.Builder().toneInput(toneInput).build();
			call = server.tone(options);
			call.enqueue(new ServiceCallback<ToneAnalysis>(){
				@Override public void onResponse(ToneAnalysis tone) {
					Log.d("EMOTIONALLY", "MADE ASYNCRONOUS CALL");
					Log.d("EMOTIONALLY", "Success: " + tone.toString());
				}
				@Override public void onFailure(Exception e) {
					Log.e("EMOTIONALLY", "RESPONSE FAILED!");
				}
				public void execute(){};
			});
		} catch (JSONException e){
			try {
				rtn.put("Text", "Fail");
			}catch (JSONException ex){
				Log.e("EMOTIONALLY", "FAILED TO POPULATE ERROR OBJECT");
				throw new RuntimeException("BAD FAILURE JSON STRING");
			}
		}
		Log.d("EMOTIONALLY", "RTN+STR: " + rtn.toString());
		return rtn;
	}
}
