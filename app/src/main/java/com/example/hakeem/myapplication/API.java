package com.example.hakeem.myapplication;

import org.json.JSONObject;

public class API {

	protected String API_ID;

	public API(){API_ID = "";}

	public API(String a){
        API_ID = a;
    }

    public JSONObject analyze(String message){
        return new JSONObject();
    }

}
