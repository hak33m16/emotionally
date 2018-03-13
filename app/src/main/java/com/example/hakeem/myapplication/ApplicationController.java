package com.example.hakeem.myapplication;

import org.json.JSONObject;

public class ApplicationController{

	public AnalysisAPIController apiController;
	public User user;

    public ApplicationController(){
        apiController = null;
        user = null;
    }

    public ApplicationController(User u){
        user = u;
        apiController = null;
    }

    public ApplicationController(AnalysisAPIController apc){
        apiController = apc;
        user = null;
    }

    public ApplicationController(User u, AnalysisAPIController apc){
        user = u;
        apiController = apc;
    }

}
