package com.example.hakeem.myapplication;

enum IBMEnum {sentences, document};

public class IBMToneAPI extends API {

	private ServiceCall call;
	private ToneAnalyzer server;
	private Pair<String,String> login;
	private IBMEnum mode;

}