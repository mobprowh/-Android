package com.libraries.dataparser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xml.sax.SAXException;
import android.util.Log;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.models.Category;
import com.models.Photo;
import com.models.Restaurant;
import com.libraries.xmlparser.XMLRequest;

public class DataParser  {
	
	public InputStream retrieveStream(String url) {
		
        DefaultHttpClient client = new DefaultHttpClient(); 
        HttpGet getRequest = new HttpGet(url);
          
        try {
           
           HttpResponse getResponse = client.execute(getRequest);
           final int statusCode = getResponse.getStatusLine().getStatusCode();
           
           if (statusCode != HttpStatus.SC_OK) { 
              Log.w(getClass().getSimpleName(), 
                  "Error " + statusCode + " for URL " + url); 
              
              return null;
           }

           HttpEntity getResponseEntity = getResponse.getEntity();
           InputStream stream = getResponseEntity.getContent();
           return stream;
        } 
        catch (IOException e) {
           getRequest.abort();
           Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
        }
        
        return null;
     }
	
	
	public JsonNode getJsonRootNode(String url)	{
		
		InputStream source = retrieveStream(url);
		if(source == null)
			return null;
		
		ObjectMapper mapper = new ObjectMapper();
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
	
	public ArrayList<Object> getXML(String urlStr) {
		
		XMLRequest req = new XMLRequest(urlStr);
		ArrayList<Object> obj = null;
		
		try {
			obj = req.parseXML();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return obj;
	}
	
	public ArrayList<Object> getJSON(JsonNode rootNode ) {
		
		ArrayList<Object> obj = new ArrayList<Object>();
		
		JsonNode restaurantNode = rootNode.get("restaurants");
		Iterator<JsonNode> iter = restaurantNode.iterator();
		while(iter.hasNext()) {
			JsonNode node = iter.next();
			try {
				Restaurant entry = createRestaurant(node);
				if(entry != null)
					obj.add(entry);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		JsonNode categoriesNode = rootNode.get("categories");
		iter = categoriesNode.iterator();
		while(iter.hasNext()) {
			JsonNode node = iter.next();
			try {
				Category entry = createCategory(node);
				if(entry != null)
					obj.add(entry);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		JsonNode photoNode = rootNode.get("photos");
		iter = photoNode.iterator();
		while(iter.hasNext()) {
			JsonNode node = iter.next();
			try {
				Photo entry = createPhoto(node);
				if(entry != null)
					obj.add(entry);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}

		return obj;
	}
	
	public ArrayList<Restaurant> getJSONRestaurant(JsonNode rootNode ) {
		
		JsonNode restaurantNode = rootNode.get("restaurants");
		ArrayList<Restaurant> list = new ArrayList<Restaurant>();
		Iterator<JsonNode> iter = restaurantNode.iterator();
		
		while(iter.hasNext()) {
			
			JsonNode node = iter.next();
			try {
				Restaurant entry = createRestaurant(node);
				
				if(entry != null)
					list.add(entry);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	
	private Restaurant createRestaurant(JsonNode jsonObj) {
			
		Restaurant res = new Restaurant();
		
		if(jsonObj.get("address") != null)
			res.address = jsonObj.get("address").asText();
		
		if(jsonObj.get("amenities") != null)
			res.amenities = jsonObj.get("amenities").asText();
		
		if(jsonObj.get("created_at") != null)
			res.created_at = jsonObj.get("created_at").asText();
		
		if(jsonObj.get("desc1") != null)
			res.desc = jsonObj.get("desc1").asText();
		
		if(jsonObj.get("email") != null)
			res.email = jsonObj.get("email").asText();
		
		if(jsonObj.get("food_rating") != null)
			res.food_rating = jsonObj.get("food_rating").asDouble();
		
		if(jsonObj.get("hours") != null)
			res.hours = jsonObj.get("hours").asText();
		
		if(jsonObj.get("lat") != null)
			res.lat = jsonObj.get("lat").asText();
		
		if(jsonObj.get("lon") != null)
			res.lon = jsonObj.get("lon").asText();
		
		if(jsonObj.get("name") != null)
			res.name = jsonObj.get("name").asText();
		
		if(jsonObj.get("phone") != null)
			res.phone = jsonObj.get("phone").asText();
		
		if(jsonObj.get("price_rating") != null)
			res.price_rating = jsonObj.get("price_rating").asDouble();
		
		if(jsonObj.get("restaurant_id") != null)
			res.restaurant_id = jsonObj.get("restaurant_id").asInt();
		
		if(jsonObj.get("category_id") != null)
			res.category_id = jsonObj.get("category_id").asInt();
		
		if(jsonObj.get("website") != null)
			res.website = jsonObj.get("website").asText();
		
		if(jsonObj.get("featured") != null)
			res.featured = jsonObj.get("featured").asText();
		
		return res;
	}
	
	
	public ArrayList<Category> getJSONCategory(JsonNode rootNode ) {
		
		JsonNode cateogoryNode = rootNode.get("categories");
		ArrayList<Category> list = new ArrayList<Category>();
		Iterator<JsonNode> iter = cateogoryNode.iterator();
		
		while(iter.hasNext()) {
			
			JsonNode node = iter.next();
			try {
				Category entry = createCategory(node);
				
				if(entry != null)
					list.add(entry);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	private Category createCategory(JsonNode jsonObj) {
		
		Category res = new Category();
		
		if(jsonObj.get("category") != null)
			res.category = jsonObj.get("category").asText();
		
		if(jsonObj.get("category_id") != null)
			res.category_id = jsonObj.get("category_id").asInt();
		
		if(jsonObj.get("created_at") != null)
			res.created_at = jsonObj.get("created_at").asText();
		
		return res;
	}
	
	
	public ArrayList<Photo> getJSONPhoto(JsonNode rootNode ) {
		
		JsonNode cateogoryNode = rootNode.get("photos");
		ArrayList<Photo> list = new ArrayList<Photo>();
		Iterator<JsonNode> iter = cateogoryNode.iterator();
		
		while(iter.hasNext()) {
			
			JsonNode node = iter.next();
			try {
				Photo entry = createPhoto(node);
				
				if(entry != null)
					list.add(entry);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	private Photo createPhoto(JsonNode jsonObj) {
		
		Photo res = new Photo();
		
		if(jsonObj.get("created_at") != null)
			res.created_at = jsonObj.get("created_at").asText();
		
		if(jsonObj.get("photo_id") != null)
			res.photo_id = jsonObj.get("photo_id").asInt();
		
		if(jsonObj.get("photo_url") != null)
			res.photo_url = jsonObj.get("photo_url").asText();
		
		if(jsonObj.get("restaurant_id") != null)
			res.restaurant_id = jsonObj.get("restaurant_id").asInt();
		
		if(jsonObj.get("thumb_url") != null)
			res.thumb_url = jsonObj.get("thumb_url").asText();
		
		return res;
	}
	
}
