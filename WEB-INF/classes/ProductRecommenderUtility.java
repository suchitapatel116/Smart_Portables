import java.sql.*;
import java.util.*;
import java.io.*;

public class ProductRecommenderUtility
{
	static Connection conn = null;
	public static String HOME_DIR = System.getenv("ANT_HOME");
	public static String RECOMMENDERS_FILE = HOME_DIR + "\\webapps\\Assignment5\\userProductRecommendations.csv";
	//userProductRecommendations.csv

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

	public HashMap<String, String> readOutputFile()
	{
		String line, csvSplit = ",";
		BufferedReader reader = null;
		//<user name, recommended product names>
		HashMap<String, String> recommender_map = new HashMap<>();
		try {
			reader = new BufferedReader(new FileReader(RECOMMENDERS_FILE));
			while ((line = reader.readLine()) != null) 
			{
				String []prod_recom = line.split(csvSplit, 2);
				recommender_map.put(prod_recom[0], prod_recom[1]);
			}
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(reader != null)
				try{
					reader.close();
				}
				catch(IOException e){
					e.printStackTrace();
				}
		}
		return recommender_map;
	}

	public static Product getProduct(String name)
	{
		Product pd = null;
		try{
			getConnection();
			String product_query = "SELECT * FROM ProductDetails WHERE productName = '"+name+"';";

			PreparedStatement pst = conn.prepareStatement(product_query);
			ResultSet rs = pst.executeQuery();
			String []acc_arr;
			List<String> acc_list;

			if(rs.next())
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
			}
		}
		catch(Exception e){		
			System.out.println("Error: getProduct: Cannot fetch product data");
			pd = null;
		}
		return pd;
	}

}