package com.demos.activities;

import java.net.HttpURLConnection;

import com.example.sl_netfoundation.R;
import com.sl.net.core.NetAction;
import com.sl.net.interfaces.IHttpListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Demo 2 : if your activity have many http request and you want to dispose
 * callback on same methods , your activity implements IHttpListener
 * 
 * @author Stephen
 */
public class SecondActivity extends Activity implements IHttpListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button button1 = (Button) findViewById(R.id.button1);
		button1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				NetAction netAction = new NetAction(SecondActivity.this, SecondActivity.this);
				String urlString = "http://mlife.cmbchina.com/Merchant/movie_ticket/ticket/v4/film/coming_films_list.json";
				netAction.execute(urlString, null);
			}
		});

		Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.second, menu);
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

	@Override
	public void httpSuccess(String responseString, HttpURLConnection connection) {
		Toast.makeText(this, responseString, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void httpFail(HttpURLConnection connection, Throwable error) {
	}
}
