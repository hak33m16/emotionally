package com.example.hakeem.myapplication;

import android.telecom.Call;
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

public class IBMToneAPI extends API{

	private ServiceCall call;
	private ToneAnalyzer server;
	private Pair<String,String> login;
	private IBMEnum mode;
	private JSONObject response = new JSONObject();

	private Boolean analysis_status = false;

	public IBMToneAPI(){
		server = new ToneAnalyzer("2017-09-21");
		login = Pair.create("a51ba1b9-ba49-4b5e-b197-edc37eecd571", "dHXyey1MmoWC");
		server.setUsernameAndPassword(login.first, login.second);
	}

    @Override
	public JSONObject analyze(String message){
	    ToneInput toneInput = new ToneInput.Builder().text(message).build();
	    ToneOptions options = new ToneOptions.Builder().toneInput(toneInput).build();
	    ToneAnalysis tone = server.tone(options).execute();

        analysis_status = true;

		Log.d("EMOTIONALLY", "Called analyze");
	    try {
	        Log.d("EMOTIONALLY", "Got Response");
	        return new JSONObject(tone.toString());
        } catch (JSONException e){
	        Log.e("EMOTIONALLY", "Bad JSONObject");
        }

        return new JSONObject();
    }

    @Override
	public Boolean message_analyzed() {
		return analysis_status;
	}

	/*public void analyze(String message){
		ToneInput toneInput = new ToneInput.Builder().text(message).build();
		ToneOptions options = new ToneOptions.Builder().toneInput(toneInput).build();
		call = server.tone(options);
		call.enqueue(new ServiceCallback<ToneAnalysis>(){
			@Override public void onResponse(ToneAnalysis tone) {
                try {
                    response = new JSONObject(tone.toString());
                } catch (JSONException ex){
                    Log.e("EMOTIONALLY", "Bad JSONObject");
                }
			}
			@Override public void onFailure(Exception e) {
                Log.e("EMOTIONALLY", "Bad request");
			}
		});
	}
	*/
}
