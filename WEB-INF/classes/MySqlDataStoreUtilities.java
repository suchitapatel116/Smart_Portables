import java.sql.*;
import java.util.*;

public class MySqlDataStoreUtilities
{
	static Connection conn = null;

	public static boolean getConnection(){
		boolean isConnected = true;
		try{
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ewaA2sqlDB", "root", "root");
		}
		catch(Exception e){
			System.out.println("Error: No connection to database");
			isConnected = false;
		}
		return isConnected;
	}

	public static void addUser(String uname, String password, String usertype, String email){
		try{
			getConnection();
			String insertUser_query = "INSERT into Registration(username, password, usertype, email) VALUES(?,?,?,?);";

			PreparedStatement pst = conn.prepareStatement(insertUser_query);
			pst.setString(1, uname);
			pst.setString(2, password);
			pst.setString(3, usertype);
			pst.setString(4, email);
			pst.execute();
		}
		catch(Exception e){
			System.out.println("Error: addUser: In user insertion");
		}
	}

	//can be done by salesman
	public static boolean deleteUser(String uname){
		boolean isDeleted = true;
		try{
			getConnection();
			String delUser_query = "DELETE FROM Registration where username=?";
			PreparedStatement pst = conn.prepareStatement(delUser_query);
			pst.setString(1, uname);
			pst.executeUpdate();
		}
		catch(Exception e){
			System.out.println("Error: deleteUser: In user deletion");
			isDeleted = false;
		}
		return isDeleted;
	}

	public static HashMap<String, User> getUsers(){
		HashMap<String, User> map = null;
		try{
			getConnection();
			String selectUser_query = "SELECT * FROM Registration";

			Statement statement = conn.createStatement();
			ResultSet rs = statement.executeQuery(selectUser_query);
			User user;
			map = new HashMap<>();
			while(rs.next()){
				user = new User(rs.getString("username"), rs.getString("password"), rs.getString("usertype"), rs.getString("email"));
				map.put(rs.getString("username"), user);
			}
		}
		catch(Exception e){
			System.out.println("Error: getUsers: Cannot fetch the Users data");
		}
		return map;
	}

	public static void insertCustomerOrder(int orderId, String uname, String pname, double price, int qty, double total, String address, double contact, String creditCardNo, int cvv, String date, String orderDate, String state){
		try{
			getConnection();
			String insertOrder_query = "INSERT into CustomerOrders(OrderId, username, productName, productPrice, quantity, total, userAddress, contactNo, creditCardNo, cvv, deliveryDate, orderDate, state) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?);";

			PreparedStatement pst = conn.prepareStatement(insertOrder_query);
			pst.setInt(1, orderId);
			pst.setString(2, uname);
			pst.setString(3, pname);
			pst.setDouble(4, price);
			pst.setInt(5, qty);
			pst.setDouble(6, total);
			pst.setString(7, address);
			pst.setDouble(8, contact);
			pst.setString(9, creditCardNo);
			pst.setInt(10, cvv);
			pst.setString(11, date);
			pst.setString(12, orderDate);
			pst.setString(13, state);
			pst.execute();
		}
		catch(Exception e){
			System.out.println("Error: insertCustomerOrder: In Customer Order insertion");
		}
	}

	public static void deleteCustomerOrder(int orderId, String uname, String pname){
		try{
			getConnection();
			String delOrder_query = "DELETE FROM CustomerOrders where OrderId=? AND username=? AND productName=?;";

			PreparedStatement pst = conn.prepareStatement(delOrder_query);
			pst.setInt(1, orderId);
			pst.setString(2, uname);
			pst.setString(3, pname);
			pst.executeUpdate();
		}
		catch(Exception e){
			System.out.println("Error: deleteCustomerOrder: In Customer Order deletion");
		}
	}

	//Every customer will have 1 array of products he bought
	//<customer, list of products bought>
	public static HashMap<String, List<OrderItem>> getCustomerOrders(){

		HashMap<String, List<OrderItem>> orders_map = new HashMap<>();
		try{
			getConnection();
			String selectOrders_query = "SELECT * FROM CustomerOrders;";
			PreparedStatement pst = conn.prepareStatement(selectOrders_query);
			ResultSet rs = pst.executeQuery();
			List<OrderItem> cust_orders_list = null;
			OrderItem oi = null;

			while(rs.next())
			{
				//if user is not there then add
				if(!orders_map.containsKey(rs.getString("username")))
					orders_map.put(rs.getString("username"), new ArrayList<OrderItem>());

				cust_orders_list = orders_map.get(rs.getString("username"));
				//(OrderId, username, productName, productPrice, quantity, total, userAddress, contactNo, creditCardNo, cvv, deliveryDate)
				oi = new OrderItem(rs.getInt("OrderId"), rs.getString("username"), rs.getString("productName"), 
					rs.getDouble("productPrice"), rs.getInt("quantity"), rs.getDouble("total"), rs.getString("userAddress"), 
					rs.getDouble("contactNo"), rs.getString("creditCardNo"), rs.getInt("cvv"), rs.getString("deliveryDate"), 
					rs.getString("orderDate"), rs.getString("state"));

				cust_orders_list.add(oi);
				orders_map.put(rs.getString("username"), cust_orders_list);
			}
		}
		catch(Exception e){
			System.out.println("Error: getCustomerOrders: Cannot fetch customers orders");
		}
		return orders_map;
	}

	public static int getNextOrderId(){
		int id = 1000;
		try{
			getConnection();
			String query = "SELECT max(OrderId) as maxVal FROM CustomerOrders;";
			PreparedStatement pst = conn.prepareStatement(query);
			ResultSet rs = pst.executeQuery();

			if(rs.next())
				id = rs.getInt("maxVal");
			if(id < 1000)
				id = 1001;
		}
		catch(Exception e){
			System.out.println("Error: getNextOrderId: Cannot fetch next order id");
		}
		return (id+1);
	}


	public static void insertProductsInDB(Map<String, List<Product>> productmap)
	{
		try{
			getConnection();
			String insert_query = "INSERT into ProductDetails(productType, pid, productName, productPrice, productImage, productManufacturer,"
				+"productCondition, productDiscount, pAccessoryList, productQuantity, manufacturerRebate) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement pst = conn.prepareStatement(insert_query);
			
			String manufRebate;
			Random random;
			int qty;

			List<Product> list = new ArrayList<>();
			for(String key: productmap.keySet())
			{
				pst.setString(1, key);
				list.clear();
				list.addAll(productmap.get(key));
				Product pd;
				String acc_list;
				List<String> lst = new ArrayList<>();
				for(int i=0; i<list.size(); i++)
				{
					try{
						manufRebate = "No";
						random = new Random();
						qty = random.nextInt(1000)+1;
						if(qty%5 == 0)
							manufRebate = "Yes";
						
						pd = list.get(i);
						//currently pid = productname, needs to be modified
						pst.setString(2, pd.getName());
						pst.setString(3, pd.getName());
						pst.setDouble(4, Double.parseDouble(pd.getPrice()));
						pst.setString(5, pd.getImage());
						pst.setString(6, pd.getManufacturer());
						pst.setString(7, pd.getCondition());
						pst.setDouble(8, Double.parseDouble(pd.getDiscount()));
						if(pd.getAccessories() != null){
							acc_list = "";
							lst = pd.getAccessories();
							for(String str: lst)
								acc_list += str +",";
							pst.setString(9, acc_list);
						}
						else
							pst.setString(9, null);
						pst.setInt(10, qty);
						pst.setString(11, manufRebate);
						pst.execute();
					}
					catch(Exception ex){ }
				}
			}
		}
		catch(Exception e){
			System.out.println("Error: insertProducts");
			System.out.println(e.getMessage());
		}
	}


	public static boolean insertProduct(Product pd)
	{
		boolean isInserted = true;
		try{
			getConnection();
			String insert_query = "INSERT into ProductDetails(productType, pid, productName, productPrice, productImage, productManufacturer,"
				+"productCondition, productDiscount, pAccessoryList, productQuantity, manufacturerRebate) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement pst = conn.prepareStatement(insert_query);
			
			String manufRebate, acc_list;
			Random random;
			int qty;
			List<String> lst = new ArrayList<>();

			pst.setString(1, pd.getType());
			manufRebate = "No";
			random = new Random();
			qty = random.nextInt(1000)+1;
			if(qty%5 == 0)
				manufRebate = "Yes";
						
			//currently pid = productname, needs to be modified
			pst.setString(2, pd.getName());
			pst.setString(3, pd.getName());
			pst.setDouble(4, Double.parseDouble(pd.getPrice()));
			pst.setString(5, pd.getImage());
			pst.setString(6, pd.getManufacturer());
			pst.setString(7, pd.getCondition());
			pst.setDouble(8, Double.parseDouble(pd.getDiscount()));
			if(pd.getAccessories() != null){
				acc_list = "";
				lst = pd.getAccessories();
				for(String str: lst)
					acc_list += str +",";
					pst.setString(9, acc_list);
				}
			else
				pst.setString(9, null);
			pst.setInt(10, qty);
			pst.setString(11, manufRebate);
			pst.execute();
		}
		catch(Exception e){
			System.out.println("Error: insertProduct in DB");
			System.out.println(e.getMessage());
			isInserted = false;
		}
		return isInserted;
	}

	public static boolean deleteProduct(String pname){
		boolean isDeleted = true;
		try{
			getConnection();
			String delProduct_query = "DELETE FROM ProductDetails where productName = ?";
			PreparedStatement pst = conn.prepareStatement(delProduct_query);
			pst.setString(1, pname);
			pst.executeUpdate();
		}
		catch(Exception e){
			System.out.println("Error: deleteProduct: In user deletion");
			isDeleted = false;
		}
		return isDeleted;
	}

	public static boolean updateProduct(Product pd)
	{
		boolean isUpdated = true;
		try{
			getConnection();
			String update_query = "UPDATE ProductDetails SET productType = ?, productPrice = ?, productImage = ?, productManufacturer = ?, "
				+"productCondition = ?, productDiscount = ?, pAccessoryList = ? WHERE productName = ?;";

			PreparedStatement pst = conn.prepareStatement(update_query);
			
			String acc_list;
			List<String> lst = new ArrayList<>();

			pst.setString(1, pd.getType());		
			pst.setDouble(2, Double.parseDouble(pd.getPrice()));
			pst.setString(3, pd.getImage());
			pst.setString(4, pd.getManufacturer());
			pst.setString(5, pd.getCondition());
			pst.setDouble(6, Double.parseDouble(pd.getDiscount()));
			if(pd.getAccessories() != null){
				acc_list = "";
				lst = pd.getAccessories();
				for(String str: lst)
					acc_list += str +",";
					pst.setString(7, acc_list);
				}
			else
				pst.setString(7, null);
			pst.setString(8, pd.getName());
			pst.execute();
		}
		catch(Exception e){
			System.out.println("Error: updateProduct in DB");
			System.out.println(e.getMessage());
			isUpdated = false;
		}
		return isUpdated;
	}

	public static List<Product> getDBProductsList(String category)
	{
		List<Product> list = new ArrayList<>();
		try{
			getConnection();
			String products_query;
			if(category == null || category.equals(""))
				products_query = "SELECT * FROM ProductDetails;";
			else
				products_query = "SELECT * FROM ProductDetails WHERE productType = '"+category+"';";

			PreparedStatement pst = conn.prepareStatement(products_query);
			ResultSet rs = pst.executeQuery();
			String []acc_arr;
			List<String> acc_list;

			Product pd;
			while(rs.next())
			{
				if(rs.getString("pAccessoryList") != null){
					acc_list = new ArrayList<>();
					acc_arr = rs.getString("pAccessoryList").split(",");
					for(String entry: acc_arr)
						if(!entry.equals(""))
							acc_list.add(entry);
				}
				else
					acc_list = null;

				pd = new Product(
					rs.getString("pid"),
					rs.getString("productName"),
					""+rs.getString("productPrice"),
					rs.getString("productImage"),
					rs.getString("productManufacturer"),
					rs.getString("productCondition"),
					""+rs.getString("productDiscount"),
					acc_list,
					rs.getString("productType"),
					""+rs.getString("productQuantity"),
					rs.getString("manufacturerRebate"));

				list.add(pd);
			}
		}
		catch(Exception e){		
			System.out.println("Error: getDBProductsList: Cannot fetch products data");
			list = null;
		}
		return list;
	}




	public static Map<OrderItem, Integer> getProductSoldCount()
	{
		HashMap<OrderItem, Integer> map = new HashMap<>();
		try{
			getConnection();
			String productSold_query;
			productSold_query = "SELECT *, count(*) AS soldCount FROM CustomerOrders GROUP BY productName;";

			PreparedStatement pst = conn.prepareStatement(productSold_query);
			ResultSet rs = pst.executeQuery();

			OrderItem oi;
			while(rs.next())
			{				
				oi = new OrderItem(
					rs.getInt("OrderId"),
					rs.getString("username"),
					rs.getString("productName"),
					rs.getDouble("productPrice"),
					rs.getInt("quantity"),
					rs.getDouble("total"),
					rs.getString("userAddress"),
					rs.getDouble("contactNo"),
					rs.getString("creditCardNo"),
					rs.getInt("cvv"),
					rs.getString("deliveryDate"),
					rs.getString("orderDate"),
					rs.getString("state"));
				map.put(oi, rs.getInt("soldCount"));
			}
		}
		catch(Exception e){		
			System.out.println("Error: getProductSoldCount: Cannot fetch customer orders data");
			map = null;
		}
		return map;
	}

	public static Map<String, Double> getDailyTransactions()
	{
		TreeMap<String, Double> map = new TreeMap<>();
		try{
			getConnection();
			String daily_query;
			daily_query = "SELECT *, sum(productPrice) AS dailySales FROM CustomerOrders GROUP BY orderDate ORDER BY orderDate ASC;";

			PreparedStatement pst = conn.prepareStatement(daily_query);
			ResultSet rs = pst.executeQuery();

			OrderItem oi;
			while(rs.next())
			{				
				oi = new OrderItem(
					rs.getInt("OrderId"),
					rs.getString("username"),
					rs.getString("productName"),
					rs.getDouble("productPrice"),
					rs.getInt("quantity"),
					rs.getDouble("total"),
					rs.getString("userAddress"),
					rs.getDouble("contactNo"),
					rs.getString("creditCardNo"),
					rs.getInt("cvv"),
					rs.getString("deliveryDate"),
					rs.getString("orderDate"),
					rs.getString("state"));
				map.put(rs.getString("orderDate"), rs.getDouble("dailySales"));
			}
		}
		catch(Exception e){		
			System.out.println("Error: getDailyTransactions: Cannot fetch customer orders data");
			map = null;
		}
		return map;
	}

	//Assignment 4
	public static Map<String, Double> getTotalPriceOfProductsInEachState()
	{
		TreeMap<String, Double> map = new TreeMap<>();
		try
		{
			getConnection();
			String price_query;
			price_query = "SELECT *, sum(productPrice) AS totalPrice FROM CustomerOrders GROUP BY state;";

			PreparedStatement pst = conn.prepareStatement(price_query);
			ResultSet rs = pst.executeQuery();

			OrderItem oi;
			while(rs.next())
			{				
				oi = new OrderItem(
					rs.getInt("OrderId"),
					rs.getString("username"),
					rs.getString("productName"),
					rs.getDouble("productPrice"),
					rs.getInt("quantity"),
					rs.getDouble("total"),
					rs.getString("userAddress"),
					rs.getDouble("contactNo"),
					rs.getString("creditCardNo"),
					rs.getInt("cvv"),
					rs.getString("deliveryDate"),
					rs.getString("orderDate"),
					rs.getString("state"));
				map.put(rs.getString("state"), rs.getDouble("totalPrice"));
			}
		}
		catch(Exception e){		
			System.out.println("Error: getTotalPriceOfProductsInEachState:");
			map = null;
		}
		return map;
	}

	public static Map<String, Double> getAveragePriceInEachState()
	{
		TreeMap<String, Double> map = new TreeMap<>();
		try
		{
			getConnection();
			String price_query;
			price_query = "SELECT *, avg(productPrice) AS totalPrice FROM CustomerOrders GROUP BY state;";

			PreparedStatement pst = conn.prepareStatement(price_query);
			ResultSet rs = pst.executeQuery();

			OrderItem oi;
			while(rs.next())
			{				
				oi = new OrderItem(
					rs.getInt("OrderId"),
					rs.getString("username"),
					rs.getString("productName"),
					rs.getDouble("productPrice"),
					rs.getInt("quantity"),
					rs.getDouble("total"),
					rs.getString("userAddress"),
					rs.getDouble("contactNo"),
					rs.getString("creditCardNo"),
					rs.getInt("cvv"),
					rs.getString("deliveryDate"),
					rs.getString("orderDate"),
					rs.getString("state"));
				map.put(rs.getString("state"), rs.getDouble("totalPrice"));
			}
		}
		catch(Exception e){		
			System.out.println("Error: getAveragePriceInEachState:");
			map = null;
		}
		return map;
	}

	public static Map<String, Double> productSoldCount_statewise()
	{
		TreeMap<String, Double> map = new TreeMap<>();
		try
		{
			getConnection();
			String price_query;
			price_query = "SELECT *, count(*) AS soldCount FROM CustomerOrders GROUP BY state;";

			PreparedStatement pst = conn.prepareStatement(price_query);
			ResultSet rs = pst.executeQuery();

			OrderItem oi;
			while(rs.next())
			{				
				oi = new OrderItem(
					rs.getInt("OrderId"),
					rs.getString("username"),
					rs.getString("productName"),
					rs.getDouble("productPrice"),
					rs.getInt("quantity"),
					rs.getDouble("total"),
					rs.getString("userAddress"),
					rs.getDouble("contactNo"),
					rs.getString("creditCardNo"),
					rs.getInt("cvv"),
					rs.getString("deliveryDate"),
					rs.getString("orderDate"),
					rs.getString("state"));
				map.put(rs.getString("state"), rs.getDouble("soldCount"));
			}
		}
		catch(Exception e){		
			System.out.println("Error: productSoldCount_statewise:");
			map = null;
		}
		return map;
	}


}