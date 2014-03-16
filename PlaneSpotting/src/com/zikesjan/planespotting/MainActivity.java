package com.zikesjan.planespotting;

import java.util.List;

import com.zikesjan.planespotting.model.Flight;
import com.zikesjan.planespotting.util.ApiConnector;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;

public class MainActivity extends Activity {

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		AsyncTask<Void, Void, List<Flight>> task = new DownloadJsonAsyncTask();
		task.execute();
		
		return true;
	}

	
	/**
	 * Async task that is calling API that gets initial informations about the
	 * district
	 * 
	 * @author zikesjan
	 * 
	 */
	private class DownloadJsonAsyncTask extends AsyncTask<Void, Void, List<Flight>> {

		private ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() {

			// show progress dialog
			dialog = ProgressDialog.show(MainActivity.this, "", getResources()
					.getString(R.string.info_dialog_text), true);
			dialog.setCancelable(true);

			super.onPreExecute();
		}

		@Override
		protected List<Flight> doInBackground(Void... params) {
			return ApiConnector.getInfo();
		}
		
		protected void onPostExecute(List<Flight> result) {
			Log.v(result.size()+"", "");
			StringBuilder sb = new StringBuilder();
			for(Flight f : result){
				sb.append(f.id+", "+f.from+", "+f.to+", "+f.type+"\n");
				Log.v(f.id+", "+f.from+", "+f.to+", "+f.type, "");
			}
			TextView t = (TextView)findViewById(R.id.text);
			t.setText(sb.toString());
			dialog.cancel();
			super.onPostExecute(result);
		}

	}
}
