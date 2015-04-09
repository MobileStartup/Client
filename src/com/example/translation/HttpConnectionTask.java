package com.example.translation;

import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;

public class HttpConnectionTask extends AsyncTask<String, Void, String> {

	@Override
	protected String doInBackground(String... params) {
		String input = params[0];
		String output = "";
		try {
			String url = "http://10.0.2.2:5000/?input="+URLEncoder.encode(input, HTTP.UTF_8).replace("+", "%20");
			HttpClient http_client = new DefaultHttpClient();
			HttpGet request = new HttpGet(url);
			request.setHeader("toke", "sodfijo2323");
			HttpResponse response = http_client.execute(request);
			response.getHeaders("token");
			HttpEntity entity = response.getEntity();
			output = EntityUtils.toString(entity, HTTP.UTF_8);
		} catch (Exception e) {
			System.out.println("Exception:" + e.getMessage());
		}
		return output;
	}

}
