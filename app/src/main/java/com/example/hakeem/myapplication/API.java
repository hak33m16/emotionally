package com.example.hakeem.myapplication;

import org.json.JSONObject;

public class API {

	protected String API_ID;

	public API(){API_ID = null;}
	public API(String a){
        API_ID = a;
    }

    public JSONObject analyze(String message){
        switch (API_ID){
            case "IBMToneAPI":{
                IBMToneAPI ita = new IBMToneAPI();
                return ita.analyze(message);
            }
            default:{
                IBMToneAPI ita = new IBMToneAPI();
                return ita.analyze(message);
            }
        }
    }

}
