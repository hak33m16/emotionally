package io.emotionally.barycenter.emotionally;

public class Settings {

	public int id;
	public double longPressTime;
	public double backspaceSpeed;
	public String selectedAPI;

    public Settings(){
        id = 0;
        longPressTime = 0;
        backspaceSpeed = 0;
        selectedAPI = null;
    }

    public Settings(String api){
        id = 0;
        longPressTime = 0;
        backspaceSpeed = 0;
        selectedAPI = api;
    }

}
