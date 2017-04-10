package com.libraries.json;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class MGJSONHelper {

	public static String getJSONFromUrl(String url) {
		
		try {
			HttpClient httpClient = new DefaultHttpClient();
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
	
}
