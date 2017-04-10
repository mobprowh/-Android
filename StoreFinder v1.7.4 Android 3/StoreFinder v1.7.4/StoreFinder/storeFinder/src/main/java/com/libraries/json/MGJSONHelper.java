package com.libraries.json;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.libraries.ssl.MGHTTPClient;

public class MGJSONHelper {

	public static String getJSONFromUrl(String url) {
		
		try {
//			HttpClient httpClient = new DefaultHttpClient();
			
			DefaultHttpClient httpClient = MGHTTPClient.getNewHttpClient();
            HttpPost httpPost = new HttpPost(url);
           
//            httpPost.setEntity(new UrlEncodedFormEntity(params));
 
            HttpResponse httpResponse = httpClient.execute(httpPost);
			String tempResponse = EntityUtils.toString(httpResponse.getEntity());
			
			return tempResponse;
 
        } 
		catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } 
		catch (ClientProtocolException e) {
            e.printStackTrace();
        } 
		catch (IOException e) {
            e.printStackTrace();
        }
		
		return "";
    }
	
	
	public static String getJSONFromUrlWithPostRequest(String url, List<NameValuePair> params) {
		
		try {
			HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
           
            httpPost.setEntity(new UrlEncodedFormEntity(params));
 
            HttpResponse httpResponse = httpClient.execute(httpPost);
			String tempResponse = EntityUtils.toString(httpResponse.getEntity());
			
			return tempResponse;
 
        } 
		catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } 
		catch (ClientProtocolException e) {
            e.printStackTrace();
        } 
		catch (IOException e) {
            e.printStackTrace();
        }
		
		return "";
    }
	
}
