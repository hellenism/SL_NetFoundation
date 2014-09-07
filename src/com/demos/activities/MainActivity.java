package com.demos.activities;

import java.net.HttpURLConnection;

import com.example.sl_netfoundation.R;
import com.sl.net.core.HttpResponseHandler;
import com.sl.net.core.NetAction;
import com.sl.net.exception.ExceptionConstants;
import com.sl.net.exception.NetException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * Demo 1: create and send http request by NetAction 
 * 
 * @author Stephen
 *
 */
public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		NetAction.setDebugModel(true);
		NetAction netAction = new NetAction(MainActivity.this, new HttpResponseHandler() {

			@Override
			public void httpSuccess(String responseString, HttpURLConnection connection) {
				Toast.makeText(MainActivity.this, responseString, Toast.LENGTH_SHORT).show();
			}

			@Override
			public void httpFail(HttpURLConnection connection, Throwable error) {
				NetException e = (NetException) error;
				if (e.getmExceptionCode() == ExceptionConstants.NETWORKDISABLE) {
					Toast.makeText(MainActivity.this, "���粻����", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MainActivity.this, "������:" + error.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}

		});

		String urlString = "http://mlife.cmbchina.com/Merchant/movie_ticket/ticket/v4/film/coming_films_list.json";
		netAction.execute(urlString, null);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
