package com.zikesjan.planespotting.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.zikesjan.planespotting.model.Flight;

public class ApiConnector {

	private static final String apiURL = "http://www.flightradar24.com/PlaneFeed.json";

	/**
	 * method for getting the informations about the planes up in the air
	 * 
	 * @param latitude
	 * @param longtitude
	 * @return
	 */
	public static List<Flight> getInfo() {
		String url = apiURL;
		HttpClient httpclient = new DefaultHttpClient();
		try {

			HttpGet method = new HttpGet(new URI(url));
			HttpResponse response = httpclient.execute(method);
			StatusLine statusLine = response.getStatusLine();

			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity entity = response.getEntity();

				// a simple JSON response read
				InputStream instream = entity.getContent();
				String result = convertStreamToString(instream);

				// a simple JSONObject creation
				JSONObject json = null;
				try {
					json = new JSONObject(result);
				} catch (JSONException e) {
					e.printStackTrace();
				}

				// closing the input stream will trigger connection release
				instream.close();

				Iterator<?> keys = json.keys();
				List<Flight> l = new LinkedList<Flight>();
				while (keys.hasNext()) {
					String key = (String) keys.next();
					try {
						if (json.get(key) instanceof JSONArray) {
							//loading the data from JSONArray to objects
							JSONArray current = (JSONArray) json.get(key);
							l.add(new Flight(key, current.getDouble(1), current.getDouble(2), current.getString(8), current.getString(9), current.getString(11), current.getString(12), current.getString(4)));
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				return l;
			} else if (statusLine.getStatusCode() == HttpStatus.SC_BAD_REQUEST) {
				return null;
			}

		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e1) {
			e1.printStackTrace();
		}

		return null;
	}

	/**
	 * method for converting stream to string
	 * 
	 * @param is
	 * @return
	 */
	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
}
