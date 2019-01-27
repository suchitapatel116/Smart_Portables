import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

public class CartData
{

	//<username, his cart items>
	public static HashMap<String, List<Product>> user_cart = new HashMap<>();

	public static List<Product> getUserCart(String user){
		return (user_cart.get(user));
	}
	public static void setUserCart(String user, List<Product> list){
		user_cart.put(user, list);
	}
	public static void removeUser(String user){
		user_cart.remove(user);
	}
	public static double getCartTotal(String user)
	{
		List<Product> list = user_cart.get(user);

		if(list == null)
			return 0.0;
		else
		{
			Product p;
			double sum = 0.0;
			String temp;
			for(int i=0; i<list.size(); i++)
			{
				p = list.get(i);
				temp = p.getPrice();
				temp = temp.replace("$","");
				temp = temp.replace(" ","");
				sum = sum + Double.parseDouble(temp);
			}
			//DecimalFormat df = new DecimalFormat("###.##");
			//sum = df.format(sum);
			return Double.parseDouble(String.format("%.2f", sum));
		}
	}
	public static void removeUserItem(String user, String pname)
	{
		List<Product> list = user_cart.get(user);

		if(list != null) {
			Product p;
			for(int i=0; i<list.size(); i++)
			{
				p = list.get(i);
				if(p.getName().equalsIgnoreCase(pname)){
					list.remove(i);
					break;
				}
			}
			user_cart.put(user, list);
		}
	}
	public static int getUserCartCount(String user)
	{
		List<Product> list = user_cart.get(user);

		if(list == null)
			return 0;
		else
			return list.size();
	}

}