import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.*;

public class SAXParser_DataStore extends DefaultHandler
{
	//String currentItem = null;
	
	//String xmlFileName = null;
	public static String HOME_DIR = System.getenv("ANT_HOME");
	public final String PRODUCT_CATALOG_FILE = HOME_DIR + "\\webapps\\A5\\ProductCatalog.xml";
	
	Product product = null;
	List<String> accessories_list = null;
	String elementValue = null;

	static HashMap<String, List<Product>> products_map;// = new HashMap<>();
	static List<Product> smartWatch_list;// = new ArrayList<>();
	static List<Product> headphone_list;// = new ArrayList<>();
	static List<Product> virtualReality_list;// = new ArrayList<>();
	static List<Product> petTracker_list;// = new ArrayList<>();
	static List<Product> phone_list;// = new ArrayList<>();
	static List<Product> laptop_list;// = new ArrayList<>();
	static List<Product> speaker_list;// = new ArrayList<>();
	static List<Product> externalStorage_list;// = new ArrayList<>();
	static List<Product> accessory_catalog_list;// = new ArrayList<>();

	boolean nameFlag = false;
	boolean priceFlag = false;
	boolean imageFlag = false;
	boolean manufacturerFlag = false;
	boolean conditionFlag = false;
	boolean discountFlag = false;
	boolean accessoriesFlag = false;

	final static String KEY_SMART_WATCH = "smart_watch";
	final static String KEY_HEADPHONES = "headphones";
	final static String KEY_VIRTUAL_REALITY = "virtual_reality";
	final static String KEY_PET_TRACKER = "pet_tracker";
	final static String KEY_PHONE = "phone";
	final static String KEY_LAPTOP = "laptop";
	final static String KEY_SPEAKERS = "speakers";
	final static String KEY_EXTERNAL_STORAGE = "external_storage";
	final static String KEY_ACCESSORIES_ITEMS = "accessory_item";

	SAXParser_DataStore()
	{
		products_map = new HashMap<>();
		smartWatch_list = new ArrayList<>();
		headphone_list = new ArrayList<>();
		virtualReality_list = new ArrayList<>();
		petTracker_list = new ArrayList<>();
		phone_list = new ArrayList<>();
		laptop_list = new ArrayList<>();
		speaker_list = new ArrayList<>();
		externalStorage_list = new ArrayList<>();
		accessory_catalog_list = new ArrayList<>();

		//this.xmlFileName = xmlFileName;
		parseDocument();
	}
	private void parseDocument()
	{
		try{
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser SAXParser = factory.newSAXParser();
			
			DefaultHandler handler = this;
			File f = new File(PRODUCT_CATALOG_FILE);
			SAXParser.parse(f, handler);
		}
		catch (ParserConfigurationException e) {
            System.out.println("ParserConfig error");
        } catch (SAXException e) {
            System.out.println("SAXException : xml not well formed");
        } catch (IOException e) {
            System.out.println("IO error");
        }
		catch(Exception e){}
	}

	//This method is called everytime the parser gets an open tag <
	//It identifies which tag is being opened
	@Override
	public void startElement(String uri, String localName, String elementName, Attributes attributes) throws SAXException
	{
		String item_id = null;
		if(elementName.equalsIgnoreCase("SmWatch") ||
			elementName.equalsIgnoreCase("Headphone") ||
			elementName.equalsIgnoreCase("VirtualReality") ||
			elementName.equalsIgnoreCase("PetTracker") ||
			elementName.equalsIgnoreCase("Phone") ||
			elementName.equalsIgnoreCase("Laptop") ||
			elementName.equalsIgnoreCase("speakers") ||
			elementName.equalsIgnoreCase("ExternalStorage"))
		{
			item_id = attributes.getValue("id");
			product = new Product();
			product.setId(item_id);
			accessories_list = new ArrayList<>();
		}
		else if(elementName.equalsIgnoreCase("accessoryItem")){
			item_id = attributes.getValue("id");
			product = new Product();
			product.setId(item_id);
			//It will have no accessories
			accessories_list = null;
		}
		else if(elementName.equalsIgnoreCase("name"))
			nameFlag = true;
		else if(elementName.equalsIgnoreCase("price"))
			priceFlag = true;
		else if(elementName.equalsIgnoreCase("image"))
			imageFlag = true;
		else if(elementName.equalsIgnoreCase("manufacturer"))
			manufacturerFlag = true;
		else if(elementName.equalsIgnoreCase("condition"))
			conditionFlag = true;
		else if(elementName.equalsIgnoreCase("discount"))
			discountFlag = true;
		else if(elementName.equalsIgnoreCase("accessory"))
			accessoriesFlag = true;

		if(product != null)
		{
			if(elementName.equalsIgnoreCase("SmWatch"))
				product.setType(KEY_SMART_WATCH);
			else if(elementName.equalsIgnoreCase("Headphone"))
				product.setType(KEY_HEADPHONES);
			else if(elementName.equalsIgnoreCase("VirtualReality"))
				product.setType(KEY_VIRTUAL_REALITY);
			else if(elementName.equalsIgnoreCase("PetTracker"))
				product.setType(KEY_PET_TRACKER);
			else if(elementName.equalsIgnoreCase("Phone"))
				product.setType(KEY_PHONE);
			else if(elementName.equalsIgnoreCase("Laptop"))
				product.setType(KEY_LAPTOP);
			else if(elementName.equalsIgnoreCase("speakers"))
				product.setType(KEY_SPEAKERS);
			else if(elementName.equalsIgnoreCase("ExternalStorage"))
				product.setType(KEY_EXTERNAL_STORAGE);
			else if(elementName.equalsIgnoreCase("accessoryItem"))
				product.setType(KEY_ACCESSORIES_ITEMS);
		}
		
	}
	@Override
	public void endElement(String uri, String localName, String elementName) throws SAXException
	{
		if(elementName.equalsIgnoreCase("SmWatch")) {
			smartWatch_list.add(product);
			products_map.put(KEY_SMART_WATCH, smartWatch_list);
		}
		else if(elementName.equalsIgnoreCase("Headphone")) {
			headphone_list.add(product);
			products_map.put(KEY_HEADPHONES, headphone_list);
		}
		else if(elementName.equalsIgnoreCase("VirtualReality")) {
			virtualReality_list.add(product);
			products_map.put(KEY_VIRTUAL_REALITY, virtualReality_list);
		}
		else if(elementName.equalsIgnoreCase("PetTracker")) {
			petTracker_list.add(product);
			products_map.put(KEY_PET_TRACKER, petTracker_list);
		}
		else if(elementName.equalsIgnoreCase("Phone")) {
			phone_list.add(product);
			products_map.put(KEY_PHONE, phone_list);
		}
		else if(elementName.equalsIgnoreCase("Laptop")) {
			laptop_list.add(product);
			products_map.put(KEY_LAPTOP, laptop_list);
		}
		else if(elementName.equalsIgnoreCase("speakers")) {
			speaker_list.add(product);
			products_map.put(KEY_SPEAKERS, speaker_list);
		}
		else if(elementName.equalsIgnoreCase("ExternalStorage")) {
			externalStorage_list.add(product);
			products_map.put(KEY_EXTERNAL_STORAGE, externalStorage_list);
		}
		else if(elementName.equalsIgnoreCase("accessoryItem")) {
			accessory_catalog_list.add(product);
			products_map.put(KEY_ACCESSORIES_ITEMS, accessory_catalog_list);
		}
		else if(elementName.equalsIgnoreCase("accessories")) {
			product.setAccessories(accessories_list);
			//accessories_list = null;
		}
		/*
		else if(elementName.equalsIgnoreCase("AccessoryCatalogItem")) {
			accessory_catalog_list.add(product);
			products_map.put(KEY_EXTERNAL_STORAGE, accessory_catalog_list);
		}
		*/

	}

	//print data stored between < and > tags
	@Override
    public void characters(char ch[], int start, int length) throws SAXException {
        elementValue = new String(ch, start, length);

        if(nameFlag) {
        	product.setName(elementValue);
        	nameFlag = false;
        }
        else if(priceFlag) {
        	product.setPrice(elementValue);
        	priceFlag = false;
        }
        else if(imageFlag) {
        	product.setImage(elementValue);
        	imageFlag = false;
        }
        else if(manufacturerFlag) {
        	product.setManufacturer(elementValue);
        	manufacturerFlag = false;
        }
        else if(conditionFlag) {
        	product.setCondition(elementValue);
        	conditionFlag = false;
        }
        else if(discountFlag) {
        	product.setDiscount(elementValue);
        	discountFlag = false;
        }
        if(accessoriesFlag) {
        	accessories_list.add(elementValue);
        	//product.setAccessories(accessories_list);
        	accessoriesFlag = false;
        }

    }

	
    public Map<String, List<Product>> getProductMap(){
    	return products_map;
    }
    
}
