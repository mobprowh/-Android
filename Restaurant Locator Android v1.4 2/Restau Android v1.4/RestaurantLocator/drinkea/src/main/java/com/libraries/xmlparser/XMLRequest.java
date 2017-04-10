package com.libraries.xmlparser;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class XMLRequest {
	
	private String strUrl;
	
	public XMLRequest(String strUrl) {
		
		this.strUrl = strUrl;
	}

	public ArrayList<Object> parseXML() throws ParserConfigurationException, SAXException, IOException {
		
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		SAXParser parser = parserFactory.newSAXParser();
 
		XMLReader xmlReader = parser.getXMLReader();
		XMLHandler currHandler = new XMLHandler();
		xmlReader.setContentHandler(currHandler);
       
		URL url = new URL(strUrl);
		xmlReader.parse(new InputSource( url.openStream()));
 
		return currHandler.getParsedXML();        
	}
	
}
