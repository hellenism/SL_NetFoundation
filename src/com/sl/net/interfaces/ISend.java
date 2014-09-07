package com.sl.net.interfaces;

import java.io.InputStream;
import java.util.HashMap;

public interface ISend {
	InputStream getInputStream(String urlString,HashMap<String, String> queryParams);
	String getResponseString(String urlString,HashMap<String, String> queryParams);
}
