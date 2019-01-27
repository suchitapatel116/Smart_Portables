import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/ViewOrders")

public class ViewOrders extends HttpServlet
{
	//public final String HOME_DIR = System.getenv("ANT_HOME");
	//public final String ORDER_DETAILS_FILE = "\\webapps\\Assignment4\\OrderDetails.txt";

	//For just clicking users
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();

		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");
		
		String errorMsg = "";
		String contentData = "<div id='content'>"
		+"<h3> Your orders </h3></br>";

        HttpSession session = request.getSession();
        if(session.getAttribute("username") == null){
			request.setAttribute("msg", "Please Login to View Orders");
			RequestDispatcher rd = request.getRequestDispatcher("/Login");  
			rd.forward(request, response);
			return;
		}
		String uname = session.getAttribute("username").toString();
		//String fna = HOME_DIR + ORDER_DETAILS_FILE;
		HashMap<String, List<OrderItem>> hm_orders = new HashMap<>();
		try{
			if(MySqlDataStoreUtilities.getConnection())
			{
				//hm_orders will have the orders of all the customers, so filter for the current user
				hm_orders = MySqlDataStoreUtilities.getCustomerOrders();
				if(hm_orders == null || hm_orders.isEmpty())
					errorMsg = "No orders have been placed currently.";
			}
			else{
				contentData += "<h2>Database server is down. Please try again!</h2>";
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("exception");
		}

		if(!hm_orders.isEmpty())
		{
			//Clear the existing data
			UserOrderDetails.clearAllUserOrderMap();
			UserOrderDetails.loadUserOrderMap(hm_orders);

			List<OrderItem> list = new ArrayList<>();
			list = hm_orders.get(uname);

			OrderItem o;
			contentData += "<table id='cart_best_seller'>";
			contentData += "<tr style='font-weight: bold;'><td>Order Id</td><td>Product Name</td><td>Price</td><td>Total</td><td>Order Date</td><td>Delivery Date</td><td></td></tr>"
						+"<tr><td></br></td></tr>";
			if(list != null)
			{
				for(int i=0; i<list.size(); i++)
				{
					o = list.get(i);
					contentData += "<tr>"
									+"<td>"+o.getOrder_id()+"</td><td>"+o.getProduct_name()+"</td><td>$"+o.getPrice()+"</td><td>$"
									+o.getTotal()+"</td><td>"+o.getOrder_date()+"</td><td>"+o.getDelivery_date()+"</td><td>"
									+"<form action='CancelOrder' method='get'>"
									+"<input type='submit' name='cancelbutton' class='btncancel' value='Cancel Order'>"
									+"<input type='hidden' name='oid' value='"+o.getOrder_id()+"'>"
									+"<input type='hidden' name='pname' value='"+o.getProduct_name()+"'>"
									+"</form></td></tr>";
				}
			}
		}

		contentData += errorMsg + "</table></div>";
        //contentData += "</div>";

        pw.println(contentData);
		utility.printHtml("Footer.html");
	}

}