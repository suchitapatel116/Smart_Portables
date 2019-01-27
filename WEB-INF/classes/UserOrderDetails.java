import java.io.*;
import java.util.*;

//Map stores all current session orders of all customers
public class UserOrderDetails implements Serializable
{
	private static HashMap<String, List<OrderItem>> user_orders = new HashMap<>();
	//private static int orderID = 1000;

	public static List<OrderItem> getUserOrders(String user){
		return (user_orders.get(user));
	}
	public static void setUserOrder(String user, List<OrderItem> list){
		user_orders.put(user, list);
	}
	public static void cancelOrder(String user, String orderNo){
		//user_orders.remove(user);
	}
	public static void clearUserOrderMap(String user){
		user_orders.remove(user);
	}
	public static void clearAllUserOrderMap(){
		user_orders = null;
		user_orders = new HashMap<>();
	}
	public static void loadUserOrderMap(HashMap<String, List<OrderItem>> map){
		user_orders.putAll(map);
	}
}