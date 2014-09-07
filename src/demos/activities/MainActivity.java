package demos.activities;

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
 * Demo . 通过创建匿名回调对象进行使用
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
					Toast.makeText(MainActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(MainActivity.this, "出错了:" + error.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}

		});

		String urlString = "http://mlife.cmbchina.com/Merchant/movie_ticket/ticket/v4/film/coming_films_list.json";
		netAction.execute(urlString, null);
		
		/*Button button = (Button) findViewById(R.id.button1);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				NetAction netAction = new NetAction(MainActivity.this, new HttpResponseHandler() {

					@Override
					public void httpSuccess(String responseString, HttpURLConnection connection) {
						Toast.makeText(MainActivity.this, responseString, Toast.LENGTH_SHORT).show();
					}

					@Override
					public void httpFail(HttpURLConnection connection, Throwable error) {
						NetException e = (NetException) error;
						if (e.getmExceptionCode() == ExceptionConstants.NETWORKDISABLE) {
							Toast.makeText(MainActivity.this, "网络不可用", Toast.LENGTH_SHORT).show();
						} else {
							Toast.makeText(MainActivity.this, "出错了:" + error.getMessage(), Toast.LENGTH_SHORT).show();
						}
					}

				});

				String urlString = "http://mlife.cmbchina.com/Merchant/movie_ticket/ticket/v4/film/coming_films_list.json";
				netAction.setConnectTimeout(1);
				netAction.execute(urlString, null);
			}
		});

		Button button2 = (Button) findViewById(R.id.button2);
		button2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				NetAction netAction = new NetAction(MainActivity.this, new HttpResponseHandler() {

					@Override
					public void httpSuccess(String responseString, HttpURLConnection connection) {
						Toast.makeText(MainActivity.this, responseString, Toast.LENGTH_SHORT).show();
					}

					@Override
					public void httpFail(HttpURLConnection connection, Throwable error) {
						
					}
				});
				
				netAction.setHttpMethod("POST");
				String urlString = "http://mlife.cmbchina.com/Merchant/movie_ticket/ticket/v4/film/coming_films_list.json";
				netAction.execute(urlString, null);
			}
		});
		
		Button button3 = (Button) findViewById(R.id.button3);
		button3.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,SecondActivity.class);
				MainActivity.this.startActivity(intent);
			}
		});*/
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
