package io.emotionally.barycenter.emotionally;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Hakeem on 2/18/2018.
 */

public class Analysis {

    public ArrayList<ArrayList<String>> response;
    public String message;
    public int id;


    public Analysis(){
        response = new ArrayList<ArrayList<String>>();
        message = new String("");
        id = 0;
    }

    public void populateResponse() {
        //
    }

}
