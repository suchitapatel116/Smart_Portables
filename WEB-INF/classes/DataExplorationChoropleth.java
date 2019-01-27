import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import com.google.gson.Gson;
import org.json.*;

@WebServlet("/DataExplorationChoropleth")

public class DataExplorationChoropleth extends HttpServlet
{
	public final String HOME_DIR = System.getenv("ANT_HOME");
	public final String DATA_FILE = "\\webapps\\A5\\data_file.json";
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException,IOException
	{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");
		HttpSession session = request.getSession();

		if(session.getAttribute("username") == null){
			request.setAttribute("msg", "Please Login to access Data Exploration");
			RequestDispatcher rd = request.getRequestDispatcher("/Login");
			rd.forward(request, response);
			return;
		}
		String uname = session.getAttribute("username").toString();
		String contentData = "<div id='content'>";
		//contentData += printData() +"<hr>";

		
		ArrayList<StateProductPrices> list = new ArrayList<>();	
			
		String selectedButton = request.getParameter("rb_explorationType");
		if(selectedButton.equalsIgnoreCase("rb_totalPrice"))
		{
			list.clear();
			contentData += "<h3 style='color: #4076AB;'>Total price of products sold</h3><br>"
						+"<div id='map'></div>";

			Map<String, Double> hash_map = MySqlDataStoreUtilities.getTotalPriceOfProductsInEachState();

			Iterator itr = hash_map.entrySet().iterator();
			while(itr.hasNext())
			{
				Map.Entry entry = (Map.Entry)itr.next();
				String state = entry.getKey().toString();
				double price = Double.parseDouble(entry.getValue().toString());

				StateProductPrices spp = new StateProductPrices(state, price);
				list.add(spp);
			}
			/*String hash_map = "[{'state' : 'Pennsylvania','price' : 180 },"
							+"{'state' : 'texas','price' : 60 },"
							+"{'state' : 'florida','price' : 70 },"
							+"{'state' : 'new york','price' : 100 },"
							+"{'state' : 'maryland','price' : 40 },"
							+"{'state' : 'california','price' : 130 }]";*/
		}
		else if(selectedButton.equalsIgnoreCase("rb_avgPrice"))
		{
			list.clear();
			contentData += "<h3 style='color: #4076AB;'>Average price of products sold</h3><br>"
						+"<div id='map'></div>";

			Map<String, Double> hash_map = MySqlDataStoreUtilities.getAveragePriceInEachState();

			Iterator itr = hash_map.entrySet().iterator();
			while(itr.hasNext())
			{
				Map.Entry entry = (Map.Entry)itr.next();
				String state = entry.getKey().toString();
				double price = Double.parseDouble(entry.getValue().toString());

				StateProductPrices spp = new StateProductPrices(state, price);
				list.add(spp);
			}
		}
		else if(selectedButton.equalsIgnoreCase("rb_reviewedRating5"))
		{
			list.clear();
			contentData += "<h3 style='color: #4076AB;'>Total number of products reviewed with Rating 5</h3><br>"
						+"<div id='map'></div>";

			list = MongoDBDataStoreUtilities.selectReviewCount_rating5_everyState();
		}
		else if(selectedButton.equalsIgnoreCase("rb_prodBought"))
		{
			list.clear();
			contentData += "<h3 style='color: #4076AB;'>Total number of products bought per State</h3><br>"
						+"<div id='map'></div>";

			Map<String, Double> hash_map = MySqlDataStoreUtilities.productSoldCount_statewise();
		
			Iterator itr = hash_map.entrySet().iterator();
			while(itr.hasNext())
			{
				Map.Entry entry = (Map.Entry)itr.next();
				String state = entry.getKey().toString();
				double price = Double.parseDouble(entry.getValue().toString());

				StateProductPrices spp = new StateProductPrices(state, price);
				list.add(spp);
			}
		}
		else if(selectedButton.equalsIgnoreCase("rb_prodReviewed"))
		{
			list.clear();
			contentData += "<h3 style='color: #4076AB;'>Total number of products reviewed per State</h3><br>"
						+"<div id='map'></div>";
			
			list = MongoDBDataStoreUtilities.selectReviewCount_everyState();
		}
		
		String fna = HOME_DIR + DATA_FILE;
		try 
		{
			JSONArray json_arr = new JSONArray();
			for(int i=0; i<list.size(); i++)
			{
				JSONObject obj = new JSONObject();
				obj.put("state_name", list.get(i).getState_name());
				obj.put("total_price", list.get(i).getTotal_price());
				json_arr.put(obj);
				System.out.println(obj);
			}	
			File file = new File(fna);
			if(file.exists())
				file.delete();
			file.createNewFile();

			FileWriter writer = new FileWriter(file);
			writer.write(json_arr.toString());
			writer.flush();
	      	writer.close();

			System.out.println(json_arr.toString());
		}
		catch(Exception e) {
			e.printStackTrace();
		}

		String header_style = "<meta charset='utf-8'>"
			+"<style>"
				+"* { font-family: 'Helvetica Neue'; }"
				+"p { font-size: 0.85em; }"
				+".state{ fill: #e3e3e3; stroke: #333; stroke-width: 0.5; }"
			+"</style>";
		pw.println(header_style);
		pw.println("<script src='https://d3js.org/d3.v5.min.js'></script>");
		pw.println("<script src='http://cdnjs.cloudflare.com/ajax/libs/d3/4.2.2/d3.min.js'></script>");
		pw.println("<script src='http://d3js.org/topojson.v1.min.js'></script>");
		//pw.println("<script type='text/javascript' src='DataExplorationVisualization.js'></script>");	


		contentData += "</div>";
		pw.println(contentData);
		pw.println("<script type='text/javascript' src='DataExplorationVisualization.js'></script>");
		utility.printHtml("Footer.html");
	}

}