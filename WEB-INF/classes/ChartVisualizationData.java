import com.google.gson.Gson;
import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet("/ChartVisualizationData")

public class ChartVisualizationData extends HttpServlet 
{
    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException 
	{
		//for product quantity bar chart
		List<Product> products_list = MySqlDataStoreUtilities.getDBProductsList(null);

		String jsonString = new Gson().toJson(products_list);

		response.setContentType("application/JSON");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonString);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException 
	{
		//for total sales bar chart
		//<oi, count of no of sales>, total sales = count*oi.price
		Map<OrderItem, Integer> map = MySqlDataStoreUtilities.getProductSoldCount();
		Map<String, Double> outputData = new HashMap<>();
		
		OrderItem oi;
		Iterator itr = map.entrySet().iterator();
		while(itr.hasNext())
		{
			Map.Entry entry = (Map.Entry)itr.next();
			oi = (OrderItem)entry.getKey();
			int count = Integer.parseInt(entry.getValue().toString());
			outputData.put(oi.getProduct_name(), oi.getPrice()*count);
		}

		String jsonString = new Gson().toJson(outputData);

		response.setContentType("application/JSON");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonString);

	}
}