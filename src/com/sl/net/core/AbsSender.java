package com.sl.net.core;

import java.io.InputStream;
import java.util.HashMap;

import com.sl.net.interfaces.ISend;

public abstract class AbsSender implements ISend{
	
	// ------ Interface ------
	@Override
	public InputStream getInputStream(String urlString, HashMap<String, String> queryParams) {
		return null;
	}

	@Override
	public String getResponseString(String urlString, HashMap<String, String> queryParams) {
		// TODO Auto-generated method stub
		return null;
	}

	// ------
	public abstract void send();
}
