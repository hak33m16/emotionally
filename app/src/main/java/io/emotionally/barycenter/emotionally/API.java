package io.emotionally.barycenter.emotionally;

import org.json.JSONObject;

public class API {

	protected String API_ID;

	public API(){API_ID = "";}

	public API(String a){
        API_ID = a;
    }

    /*public JSONObject analyze(String message){
        return new JSONObject();
    }*/

    public String analyze(String message){
        return new String();
    }

    //public Boolean message_analyzed() { return false; }

}
