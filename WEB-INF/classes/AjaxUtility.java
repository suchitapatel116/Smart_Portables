import java.io.*;
import java.util.*;
import java.text.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class AjaxUtility
{
	static Connection conn = null;
	boolean namesAdded = false;
	
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

	public StringBuffer readData(String searchId)
	{
		StringBuffer sb = new StringBuffer();
		Product pd;
		HashMap<String, Product> data_map;
		data_map = getData();

		Iterator it = data_map.entrySet().iterator();	
		while (it.hasNext()) 
		{
			Map.Entry entry = (Map.Entry)it.next();
			if(entry != null)
			{
				pd = (Product)entry.getValue();                   
				if (pd.getName().toLowerCase().startsWith(searchId))
				{
					sb.append("<product>");
					sb.append("<id>" + pd.getId() + "</id>");
					sb.append("<productName>" + pd.getName() + "</productName>");
					sb.append("</product>");
                }
			}
       }
	   return sb;
	}

	public static HashMap<String, Product> getData()
	{
		HashMap<String, Product> map = new HashMap<>();
		try
		{
			getConnection();
			String selectProduct_query = "SELECT * FROM ProductDetails;";
			PreparedStatement pst = conn.prepareStatement(selectProduct_query);
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

				map.put(rs.getString("pid"), pd);
			}
		}
		catch(Exception e) {
			map = null;
			e.printStackTrace();
		}
		return map;			
	}

	public static void storeData(HashMap<String, Product> productdata)
	{
		try
		{
			getConnection();
			String insert_query = "INSERT into ProductDetails(productType, pid, productName, productPrice, productImage, productManufacturer,"
				+"productCondition, productDiscount, pAccessoryList, productQuantity, manufacturerRebate) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement pst = conn.prepareStatement(insert_query);
			StringBuffer accessories_sb;

			for(Map.Entry<String, Product> entry : productdata.entrySet())
			{
				pst.setString(1, entry.getValue().getType());
				pst.setString(2, entry.getValue().getName());
				pst.setString(3, entry.getValue().getName());
				pst.setDouble(4, Double.parseDouble(entry.getValue().getPrice()));
				pst.setString(5, entry.getValue().getImage());
				pst.setString(6, entry.getValue().getManufacturer());
				pst.setString(7, entry.getValue().getCondition());
				pst.setDouble(8, Double.parseDouble(entry.getValue().getDiscount()));

				accessories_sb = new StringBuffer();
				List<String> list = entry.getValue().getAccessories();
				if(list == null)
					pst.setString(9, null);
				else{
					for(String str: list)
						accessories_sb.append(str).append(",");
					pst.setString(9, accessories_sb.toString());
				}

				pst.setInt(10, Integer.parseInt(entry.getValue().getQuantity()));
				pst.setString(11, entry.getValue().getManufacturerRebate());

				pst.execute();
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}
}