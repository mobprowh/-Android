package com.libraries.xmlparser;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.models.Category;
import com.models.Photo;
import com.models.Restaurant;

public class XMLHandler extends DefaultHandler{
	  
	ArrayList<Object> listNodes = new ArrayList<Object>();
	
	private Restaurant restaurantNode = null;
	private Photo photoNode = null;
	private Category categoryNode = null;
	private String tempVal;
	
	@Override
	public void startElement( String namespaceURI, 
			String localName, String qName, Attributes attrib) throws SAXException {
		
		super.startElement(namespaceURI, localName, qName, attrib);
		
		tempVal = "";
		if(localName.equals("restaurants")) {
			restaurantNode = new Restaurant();
			photoNode = null;
			categoryNode = null;
		}
		
		if(localName.equals("photos")) {
			photoNode = new Photo();
			categoryNode = null;
			restaurantNode = null;
		}
		
		if(localName.equals("categories")) {
			categoryNode = new Category();
			restaurantNode = null;
			photoNode = null;
		}
		
		if(localName.equals("items")) {
			listNodes = new ArrayList<Object>();
		}
		
	}
	
	
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		// TODO Auto-generated method stub
		super.characters(ch, start, length);
		tempVal += new String(ch, start, length);
	}

	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		// TODO Auto-generated method stub
		super.endElement(uri, localName, qName);
		
		if(localName.equals("item")) {
			
			if(restaurantNode != null) {
				listNodes.add(restaurantNode);
				restaurantNode = new Restaurant();
			}
			
			if(categoryNode != null) {
				listNodes.add(categoryNode);
				categoryNode = new Category();
			}
			
			if(photoNode != null) {
				listNodes.add(photoNode);
				photoNode = new Photo();
			}
		}
		
		else {
			if(restaurantNode != null) {
				if(qName.equalsIgnoreCase("address")) {
					restaurantNode.address = tempVal;
				}
				
				if(qName.equalsIgnoreCase("amenities")) {
					restaurantNode.amenities = tempVal;
				}
				
				if(qName.equalsIgnoreCase("created_at")) {
					restaurantNode.created_at = tempVal;
				}
				
				if(qName.equalsIgnoreCase("desc1")) {
					restaurantNode.desc = tempVal;
				}
				
				if(qName.equalsIgnoreCase("email")) {
					restaurantNode.email = tempVal;
				}
				
				if(qName.equalsIgnoreCase("featured")) {
					restaurantNode.featured = tempVal;
				}
				
				if(qName.equalsIgnoreCase("food_rating")) {
					restaurantNode.food_rating = Float.parseFloat(tempVal);
				}
				
				if(qName.equalsIgnoreCase("hours")) {
					restaurantNode.hours = tempVal;
				}
				
				if(qName.equalsIgnoreCase("lat")) {
					restaurantNode.lat = tempVal;
				}
				
				if(qName.equalsIgnoreCase("lon")) {
					restaurantNode.lon = tempVal;
				}
				
				if(qName.equalsIgnoreCase("name")) {
					restaurantNode.name = tempVal;
				}
				
				if(qName.equalsIgnoreCase("phone")) {
					restaurantNode.phone = tempVal;
				}
				
				if(qName.equalsIgnoreCase("price_rating")) {
					restaurantNode.price_rating = Float.parseFloat(tempVal);
				}
				
				if(qName.equalsIgnoreCase("restaurant_id")) {
					restaurantNode.restaurant_id = Integer.parseInt(tempVal);
				}
				
				if(qName.equalsIgnoreCase("category_id")) {
					restaurantNode.category_id = Integer.parseInt(tempVal);
				}
				
				if(qName.equalsIgnoreCase("website")) {
					restaurantNode.website = tempVal;
				}
			}
			
			if(categoryNode != null) {
				if(qName.equalsIgnoreCase("category")) {
					categoryNode.category = tempVal;
				}
				
				if(qName.equalsIgnoreCase("category_id")) {
					categoryNode.category_id = Integer.parseInt(tempVal);
				}
				
				if(qName.equalsIgnoreCase("created_at")) {
					categoryNode.created_at = tempVal;
				}
			}
			
			if(photoNode != null) {
				if(qName.equalsIgnoreCase("created_at")) {
					photoNode.created_at = tempVal;
				}
				
				if(qName.equalsIgnoreCase("photo_id")) {
					photoNode.photo_id = Integer.parseInt(tempVal);
				}
				
				if(qName.equalsIgnoreCase("photo_url")) {
					photoNode.photo_url = tempVal;
				}
				
				if(qName.equalsIgnoreCase("restaurant_id")) {
					photoNode.restaurant_id = Integer.parseInt(tempVal);
				}
				
				if(qName.equalsIgnoreCase("thumb_url")) {
					photoNode.thumb_url = tempVal;
				}
			}
		}
		
		
	}
	
	public ArrayList<Object> getParsedXML() {
		return listNodes;
	} 

}
