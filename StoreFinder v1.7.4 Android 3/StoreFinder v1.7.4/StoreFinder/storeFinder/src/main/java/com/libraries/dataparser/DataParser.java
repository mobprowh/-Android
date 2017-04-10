package com.libraries.dataparser;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.Data;
import com.models.DataNews;
import com.models.DataResponse;
import com.models.DataWeather;
import com.models.ResponseRating;
import com.models.ResponseReview;
import com.models.ResponseStore;
import com.models.Status;
import com.models.User;
import com.libraries.ssl.MGHTTPClient;

public class DataParser  {
	
	public InputStream retrieveStream(String url) {
        try {
            HttpClient httpClient = MGHTTPClient.getNewHttpClient();
            HttpPost httpPost = new HttpPost(url);
//            DefaultHttpClient  httpClient = new DefaultHttpClient();
//            HttpPost httpPost = MGHTTPClient.getNonHttpsPost(url);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            final int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("Status Code", "Error " + statusCode + " for URL " + url); 
                return null;
             }
            HttpEntity getResponseEntity = httpResponse.getEntity();
            InputStream stream = getResponseEntity.getContent();
           return stream;
        } 
        catch (IOException e) {
        	
           Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
        }
        return null;
     }
	
	
	public JsonNode getJsonRootNode(String url)	{
		InputStream source = retrieveStream(url);
		if(source == null)
			return null;
		
		JsonFactory f = new JsonFactory();
        f.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
        
        ObjectMapper mapper = new ObjectMapper(f);
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		JsonNode rootNode = null;
		try  {
			rootNode = mapper.readTree(source);
		} 
		catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return rootNode;
	}
	
	public Data getData(String url)	{
		InputStream source = retrieveStream(url);
		if(source == null)
			return null;
		
		JsonFactory f = new JsonFactory();
        f.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
        
        ObjectMapper mapper = new ObjectMapper(f);
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		Data data = new Data();
		
		try  {
			data = mapper.readValue(source, Data.class);
		} 
		catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}
	
	public DataNews getDataNews(String url)	{
		
		InputStream source = retrieveStream(url);
		if(source == null)
			return null;
		
		JsonFactory f = new JsonFactory();
        f.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
        
        ObjectMapper mapper = new ObjectMapper(f);
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		DataNews data = new DataNews();
		try  {
			data = mapper.readValue(source, DataNews.class);
		} 
		catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	
	public static DataResponse getJSONFromUrlWithPostRequest(String url, List<NameValuePair> params) {
		
		try {
			HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            if(params != null)
            	httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            
            HttpResponse httpResponse = httpClient.execute(httpPost);
            final int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("Status Code", "Error " + statusCode + " for URL " + url); 
                return null;
			}
            
            HttpEntity getResponseEntity = httpResponse.getEntity();
            InputStream source = getResponseEntity.getContent();
            JsonFactory f = new JsonFactory();
            f.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
            ObjectMapper mapper = new ObjectMapper(f);
            mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
//            mapper.enableDefaultTyping(DefaultTyping.JAVA_LANG_OBJECT, As.WRAPPER_OBJECT);
//            mapper.configure(DeserializationFeature.ALLOW_UNQUOTED_FIELD_NAMES, true);
//            mapper.registerSubtypes(User.class);
//            mapper.registerSubtypes(Status.class);
            
            DataResponse data = new DataResponse();
    		try  {
//    			JsonNode rootNode = mapper.readTree(source);
//    			JsonNode status = rootNode.get("status");
//    			JsonNode userInfo = rootNode.get("user_info");
//    			data.setStatus(createStatus(status));
//    			data.setUser_info(createUser(userInfo));
    			data = mapper.readValue(source, DataResponse.class);
    			return data;
    		} 
    		catch (JsonParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} 
    		catch (JsonMappingException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} 
    		catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
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
		return null;
    }
	
	
	public static Status createStatus(JsonNode jsonObj) {
		Status res = new Status();
		if(jsonObj.get("status_code") != null)
			res.setStatus_code(jsonObj.get("status_code").asInt());
		
		if(jsonObj.get("status_text") != null)
			res.setStatus_text(jsonObj.get("status_text").asText());
		
		return res;
	}
	
	public static User createUser(JsonNode jsonObj) {
		User res = new User();
		if(jsonObj.get("created_at") != null)
			res.setCreated_at(jsonObj.get("created_at").asInt());
		
		if(jsonObj.get("email") != null)
			res.setEmail(jsonObj.get("email").asText());
		
		if(jsonObj.get("facebook_id") != null)
			res.setFacebook_id(jsonObj.get("facebook_id").asText());
		
		if(jsonObj.get("full_name") != null)
			res.setFull_name(jsonObj.get("full_name").asText());
		
		if(jsonObj.get("is_deleted") != null)
			res.setIs_deleted(jsonObj.get("is_deleted").asInt());
		
		if(jsonObj.get("login_hash") != null)
			res.setLogin_hash(jsonObj.get("login_hash").asText());
		
		if(jsonObj.get("phone_no") != null)
			res.setPhone_no(jsonObj.get("phone_no").asText());
		
		if(jsonObj.get("photo_url") != null)
			res.setPhoto_url(jsonObj.get("photo_url").asText());
		
		if(jsonObj.get("sms_no") != null)
			res.setSms_no(jsonObj.get("sms_no").asText());
		
		if(jsonObj.get("thumb_url") != null)
			res.setThumb_url(jsonObj.get("thumb_url").asText());
		
		if(jsonObj.get("twitter_id") != null)
			res.setTwitter_id(jsonObj.get("twitter_id").asText());
		
		if(jsonObj.get("updated_at") != null)
			res.setUpdated_at(jsonObj.get("updated_at").asInt());
		
		if(jsonObj.get("user_id") != null)
			res.setUser_id(jsonObj.get("user_id").asInt());
		
		if(jsonObj.get("username") != null)
			res.setUsername(jsonObj.get("username").asText());
		
		return res;
	}
	
	
	public static ResponseReview getJSONFromUrlReview(String url, List<NameValuePair> params) {
		
		try {
			HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            if(params != null)
            	httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            
            HttpResponse httpResponse = httpClient.execute(httpPost);
            final int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("Status Code", "Error " + statusCode + " for URL " + url); 
                return null;
             }
            
            HttpEntity getResponseEntity = httpResponse.getEntity();
            InputStream source = getResponseEntity.getContent();
            JsonFactory f = new JsonFactory();
//            f.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
            
            ObjectMapper mapper = new ObjectMapper(f);
    		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            ResponseReview data = new ResponseReview();
    		try  {
    			data = mapper.readValue(source, ResponseReview.class);
    			return data;
    		} 
    		catch (JsonParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} 
    		catch (JsonMappingException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} 
    		catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
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
		return null;
    }

	public static ResponseRating getJSONFromUrlRating(String url, List<NameValuePair> params) {
		try {
			HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            if(params != null)
            	httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            
            HttpResponse httpResponse = httpClient.execute(httpPost);
            final int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("Status Code", "Error " + statusCode + " for URL " + url); 
                return null;
			}
            
            HttpEntity getResponseEntity = httpResponse.getEntity();
            InputStream source = getResponseEntity.getContent();
            JsonFactory f = new JsonFactory();
            f.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
            
            ObjectMapper mapper = new ObjectMapper(f);
    		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            ResponseRating data = new ResponseRating();
    		try  {
    			data = mapper.readValue(source, ResponseRating.class);
    			return data;
    		} 
    		catch (JsonParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} 
    		catch (JsonMappingException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} 
    		catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
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
		return null;
    }
	
	
	public static ResponseStore getJSONFromUrlStore(String url, List<NameValuePair> params) {
		try {
			HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            if(params != null)
            	httpPost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            
            HttpResponse httpResponse = httpClient.execute(httpPost);
            final int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                Log.w("Status Code", "Error " + statusCode + " for URL " + url); 
                return null;
             }
            
            HttpEntity getResponseEntity = httpResponse.getEntity();
            
            InputStream source = getResponseEntity.getContent();
            JsonFactory f = new JsonFactory();
            f.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
            
            ObjectMapper mapper = new ObjectMapper(f);
    		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            
            ResponseStore data = new ResponseStore();
    		
    		try  {
    			
    			data = mapper.readValue(source, ResponseStore.class);
    			return data;
    		} 
    		catch (JsonParseException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} 
    		catch (JsonMappingException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		} 
    		catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    		
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
		
		return null;
    }
	
	
	public DataWeather getDataWeather(String url)	{
		
		InputStream source = retrieveStream(url);
		if(source == null)
			return null;
		
		JsonFactory f = new JsonFactory();
        f.enable(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
        
        ObjectMapper mapper = new ObjectMapper(f);
		mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		DataWeather data = new DataWeather();
		
		try  {
			data = mapper.readValue(source, DataWeather.class);
		} 
		catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return data;
	}
}
