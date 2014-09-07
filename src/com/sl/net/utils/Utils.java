/*
 *  Copyright 2014 Stephen Lee
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.sl.net.utils;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @ClassName {@link Utils}
 * @Description Utils
 * @author Stephen Lee
 * @date 2014-9-70-22:19:23
 */
public class Utils {
	private static final String CHARSETNAME = "UTF-8";

	/**
	 * InputStream to String by BufferedReader
	 * 
	 * @param inputStream
	 *            inputStream
	 * 
	 * @param charsetName
	 *            encode type
	 * 
	 * @return String result String
	 */
	public static String Inputstr2String_Reader(InputStream inputStream, String charsetName) {

		String resultStr = "";

		try {
			if (charsetName == null || charsetName.equals("")) {

				// default encode is utf-8
				charsetName = CHARSETNAME;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, charsetName));

			StringBuffer sb = new StringBuffer();

			while ((resultStr = reader.readLine()) != null) {
				sb.append(resultStr).append("\n");
			}

			return sb.toString();
		} catch (UnsupportedEncodingException unsupportedEncodingException) {
			unsupportedEncodingException.printStackTrace();
		} catch (IOException iOException) {
			iOException.printStackTrace();
		}

		return resultStr;
	}

	/**
	 * InputStream to String by ByteArrayOutputStream
	 * 
	 * @param inputStream
	 *            inputStream
	 * 
	 * @param charsetName
	 *            encode type
	 * 
	 * @return result String
	 */
	public static String InputStream2String_ByteArray(InputStream inputStream, String charsetName) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] b = new byte[1024];
		int len = 0;
		try {
			if (charsetName == null || charsetName.equals("")) {
				// default encode is utf-8
				charsetName = CHARSETNAME;
			}
			while ((len = inputStream.read(b)) > 0) {
				out.write(b, 0, len);
			}
			return out.toString(charsetName);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return "";
	}

	/**
	 * String to InputStream by ByteArrayInputStream
	 * 
	 * @param targetString
	 *            String
	 * 
	 * @param charsetName
	 *            encode type
	 * 
	 * @return result InputStream
	 */
	public static InputStream Str2Inputstr(String targetString, String charsetName) {
		if (charsetName == null || charsetName.equals("")) {

			// default encode is utf-8
			charsetName = CHARSETNAME;
		}

		try {
			return new ByteArrayInputStream(targetString.getBytes(charsetName));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}
	
	/**
	 * check network available
	 * 
	 * @return
	 */
	public static boolean checkNetWorkAvailable(Context context)
	  {
		ConnectivityManager cm = (ConnectivityManager) context   
                .getSystemService(Context.CONNECTIVITY_SERVICE);   
        if (cm == null) {   
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();   
            if (info != null) {   
                for (int i = 0; i < info.length; i++) {   
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {   
                        return true;   
                    }   
                }   
            }   
        }   
        return false;   
	  }
	  
	  public static int checkNetWorkStatus(Context context)
	  {
	    if (checkNetWorkAvailable(context))
	    {
	      ConnectivityManager mConenctivity = (ConnectivityManager)context.getApplicationContext().getSystemService("connectivity");
	      NetworkInfo info = mConenctivity.getActiveNetworkInfo();
	      if (info != null) {
	        if (info.getType() == 0)
	        {
	          String netType = info.getExtraInfo();
	          if (netType.contains("wap"))
	          {
	            if (netType.contains("uniwap")) {
	              return 1;
	            }
	            if (netType.contains("ctwap")) {
	              return 2;
	            }
	            return 3;
	          }
	        }
	        else if (info.getType() == 1)
	        {
	          return 4;
	        }
	      }
	      return -1;
	    }
	    return -1;
	  }
}
