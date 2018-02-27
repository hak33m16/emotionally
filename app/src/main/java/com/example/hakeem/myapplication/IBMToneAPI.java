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
		String jMsg = "{\"text\":}" + '\"' + message + "\"}";
		final JSONObject rtn = new JSONObject();
		try {
			JSONObject jObject = new JSONObject(jMsg);
			ToneInput toneInput = new ToneInput.Builder().text(jObject.get("text").toString()).build();
			ToneOptions options = new ToneOptions.Builder().toneInput(toneInput).build();
			call = server.tone(options);
			call.enqueue(new ServiceCallback<ToneAnalysis>(){
				@Override public void onResponse(ToneAnalysis tone) {
					Log.d("RESPONE", "MADE ASYCHRONOUSE CALL");
					try {
						rtn.getJSONObject(tone.toString());
					}catch(JSONException e){
						throw new RuntimeException("BAD TONE OBJECT");
					}
				}
				@Override public void onFailure(Exception e) {
					try {
						rtn.getJSONObject("{\"text\":\"fail\"}");
					}catch (JSONException ec){
						throw new RuntimeException("BAD REQUEST FAIL STRING");
					}
				}
				public void execute(){};
			});
			return rtn;
		} catch (JSONException e){
			try {
				rtn.getJSONObject("{\"text\":\"fail\"}");
			}catch (JSONException ex){
				throw new RuntimeException("BAD FAILURE JSON STRING");
			}
		}
		return rtn;
	}

}