import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/CheckOutData")

public class CheckOutData extends HttpServlet
{
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");

		HttpSession session = request.getSession(true);
		if(session.getAttribute("username") == null){
			request.setAttribute("msg", "Please Login to Place Order");
			RequestDispatcher rd = request.getRequestDispatcher("/Login");  
			rd.forward(request, response);
		}
		String contentData = "<div id='content'>";

		//String orderno = request.getParameter("orderno");
		String uname = session.getAttribute("username").toString();

		String address = request.getParameter("addr");
		String state = request.getParameter("state");
		String credit_card_no = request.getParameter("cardno");
		//String contact = request.getParameter("total");
		double contact = 0;
		int cvv = 0;
		try{
			//int qty = Integer.parseInt(request.getParameter("qty"));
			contact = Double.parseDouble(request.getParameter("contact"));
			cvv = Integer.parseInt(request.getParameter("cvv"));
		}
		catch(Exception e){
			System.out.println("CheckOutData: Error converting string to int");
		}

		//String fna = HOME_DIR + ORDER_DETAILS_FILE;
		HashMap<String, List<OrderItem>> hm_orders = new HashMap<>();

		try{
			if(MySqlDataStoreUtilities.getConnection())
			{
				//generate new orderid while inserting the order in db
				int order_id = MySqlDataStoreUtilities.getNextOrderId();

				//Fetch the current date and add next 14 days date
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
				Calendar cal = Calendar.getInstance();		//Getting current date					
		        cal.add(Calendar.DAY_OF_MONTH, 14);			//Number of Days to adding					
				String newDate = sdf.format(cal.getTime());	//Date after adding the days to the current date

				Date date = new Date();
				String orderDate = sdf.format(date);

				List<OrderItem> orderedItems_in_checkout = UserOrderDetails.getUserOrders(uname);
				if(orderedItems_in_checkout != null && !orderedItems_in_checkout.isEmpty())
				{
					OrderItem oi = null;
					for(int i=0; i<orderedItems_in_checkout.size(); i++)
					{
						oi = orderedItems_in_checkout.get(i);

						MySqlDataStoreUtilities.insertCustomerOrder(order_id, uname, oi.getProduct_name(), oi.getPrice(), 1, oi.getTotal(),
															address, contact, credit_card_no, cvv, newDate, orderDate, state);
					}
				}
	
					//CartData.setUserCart(uname, null);

					contentData += "<h3>Your order has been placed successfully. Order number is "+order_id+"</h3>"
								+"</br><p style:'font-size:18px;'>Expected delivery date: "+newDate+"</p>";
				//}

				//Clean up the cart
				CartData.setUserCart(uname, null);
				//Clear the ordered products data for current user as order is placed and an entry indb is made
				UserOrderDetails.clearUserOrderMap(uname);
			}
			else{
				contentData += "<h2>Database server is down. Please try again!</h2>";
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			contentData += "<h4>Error in placing order.</h4>";
		}

		//String contentData = "<div id='content'>Your order has been placed successfully.";

		contentData += "</div>";
		pw.println(contentData);
		utility.printHtml("Footer.html");
	}
}