package com.example.hakeem.myapplication;

public class APIAdaptor {

    private API api;

    public APIAdaptor(){
        api = null;
    }

    public APIAdaptor(String API_ID){
        api = new API(API_ID);
    }

	public API getAPI(String API_ID){
		return new API(API_ID);
	}

}
