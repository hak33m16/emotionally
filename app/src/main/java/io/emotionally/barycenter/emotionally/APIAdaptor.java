package io.emotionally.barycenter.emotionally;

public class APIAdaptor {

	public API getAPI(String API_ID){
		return new API(API_ID);
	}

}