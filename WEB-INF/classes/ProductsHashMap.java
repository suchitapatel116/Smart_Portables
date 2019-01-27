import java.io.*;
import java.util.*;

//Map stores all current session products
public class ProductsHashMap implements Serializable
{
	private static Map<String, List<Product>> products_map = new HashMap<>();

	public static Map<String, List<Product>> getProductsMap(String user){
		return products_map;
	}
	public static void setProductsMap(Map<String, List<Product>> map){
		products_map.putAll(map);
	}
	public static void clearProductsMap(){
		products_map.clear();
		products_map = null;
		products_map = new HashMap<>();
	}
    public static void addProductInMap(Product pd)
    {
        String type = pd.getType();
        List<Product> list = products_map.get(type);
        if(list == null)
            list = new ArrayList<>();
        list.add(pd);
        products_map.put(type, list);
    }
    public static void removeProductInMap(String type, String pname)
    {

        List<Product> prod_list = products_map.get(type);
        if(prod_list == null)
            return;
        Product pd;
        for(int i=0; i<prod_list.size(); i++)
        {
            pd = (Product)prod_list.get(i);
            if(pname.equals(pd.getName().toString()))
            {
                prod_list.remove(i);
                products_map.put(type, prod_list);
                break;
            }
        }
    }
    public static void updateProductInMap(String type, String pname, Product prod)
    {
        List<Product> prod_list = products_map.get(type);
        if(prod_list == null)
            return;
        Product pd;
        for(int i=0; i<prod_list.size(); i++)
        {
            pd = (Product)prod_list.get(i);
            if(pname.equals(pd.getName().toString()))
            {
                prod_list.remove(i);
                prod_list.add(prod);
                products_map.put(type, prod_list);
                break;
            }
        }
    }


	final static String KEY_SMART_WATCH = "smart_watch";
	final static String KEY_HEADPHONES = "headphones";
	final static String KEY_VIRTUAL_REALITY = "virtual_reality";
	final static String KEY_PET_TRACKER = "pet_tracker";
	final static String KEY_PHONE = "phone";
	final static String KEY_LAPTOP = "laptop";
	final static String KEY_SPEAKERS = "speakers";
	final static String KEY_EXTERNAL_STORAGE = "external_storage";
	final static String KEY_ACCESSORIES_ITEMS = "accessory_item";

	public static Map<String, List<Product>> getProductMap(){
    	return products_map;
    }
    public static List<Product> getSmartWatchList(){
    	return (products_map.get(KEY_SMART_WATCH));
    }
    public static List<Product> getHeadphoneList(){
    	return (products_map.get(KEY_HEADPHONES));
    }
    public static List<Product> getVirtualRealityList(){
    	return (products_map.get(KEY_VIRTUAL_REALITY));
    }
    public static List<Product> getPetTrackerList(){
    	return (products_map.get(KEY_PET_TRACKER));
    }
    public static List<Product> getPhoneList(){
    	return (products_map.get(KEY_PHONE));
    }
    public static List<Product> getLaptopList(){
    	return (products_map.get(KEY_LAPTOP));
    }
	public static List<Product> getSpeakersList(){
    	return (products_map.get(KEY_SPEAKERS));
    }
    public static List<Product> getExternalStorageList(){
    	return (products_map.get(KEY_EXTERNAL_STORAGE));
    }
    public static List<Product> getAccessory_catalog_list(){
    	return (products_map.get(KEY_ACCESSORIES_ITEMS));
    }

}