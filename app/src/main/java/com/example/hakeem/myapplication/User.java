package com.example.hakeem.myapplication;

public class User {

	public String username;
	public String password;
	public int id;
	public Settings settings;

    public User(){
        username = null;
        password = null;
        id = 0;
        settings = null;
    }

    public User(String user, String pass, Settings setting){
        username = user;
        password = pass;
        settings = setting;
    }

}
