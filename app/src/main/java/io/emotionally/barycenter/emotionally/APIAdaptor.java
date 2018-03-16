package io.emotionally.barycenter.emotionally;

public class APIAdaptor {

    public API getAPI(String API_ID){
        switch(API_ID){
            case "IBMToneAPI":
                return new IBMToneAPI();
            default:
                return new IBMToneAPI();
        }
    }

	/*public API getAPI(String API_ID){
		return new API(API_ID);
	}*/

}
