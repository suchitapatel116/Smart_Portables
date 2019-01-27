import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/Home")

public class Home extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);

		String fileData = utility.ReadHtmlToString("Header.html");

			fileData += "<div id='menu2'><ul float='right'>";
			HttpSession session = request.getSession(true);
			if(session.getAttribute("username") != null)
			{
				String userName = session.getAttribute("username").toString();
				if(userName.equalsIgnoreCase("admin"))
				{
					fileData += "<li><a href='Register'>Register Customer</a></li>"
							+"<li><a href='DeleteCustomer'>Delete Customer</a></li>"
							+"<li><a>Hi, ADMIN</a></li>"
							+"<li><a href='Logout'>Logout</a></li>"
							+"</ul></div>";
				}
				else if(userName.equalsIgnoreCase("storeadmin"))
				{
					fileData += "<li><a href='UpdateProducts'>Update Products</a></li>"
							+"<li><a href='DataVisualization'>Trending</a></li>"
							+"<li><a href='DataAnalytics'>Data Analytics</a></li>"
							
							+"<li><a href='Inventory'>Inventory</a></li>"
							+"<li><a href='SalesReports'>Sales</a></li>"
							+"<li><a href='DataExplorationUtility'>Data Exploration</a></li>"
							+"<li><a>Hi, MANAGER</a></li>"
							+"<li><a href='Logout'>Logout</a></li>"
							+"</ul></div>";
				}
				else
				{
					int count = CartData.getUserCartCount(userName);
				userName = userName.toUpperCase();
				fileData += "<li><a href='ViewOrders'>View Orders</a></li>"
							+"<li><a>Hi, "+ userName +"</a></li>"
							+"<li><a href='Logout'>Logout</a></li>"
							+"<li><a href='ViewCart'><i class='glyphicon glyphicon-shopping-cart'> Cart ("+count+")</i></a></li>"
							+"</ul></div>";
				}
			}
			else
			{
				fileData +="<li><a href='Login'>View Orders</a></li>"
	            			+"<li><a href='Register'>Register</a></li>"
	        				+"<li><a href='Login'>Login</a></li>"
		    				+"<li><a href='Login'><i class='glyphicon glyphicon-shopping-cart'> Cart</i></a></li>"
		    				+"</ul></div>";

	        				//+"<img class='header-image' src='images/home7.jpg' alt='Buildings' />";
			}

		pw.print(fileData);

		utility.printHtml("LeftNavigationBar.html");		
		//utility.printHtml("Content.html");
		RequestDispatcher rd = request.getRequestDispatcher("DealMatches");
		rd.include(request, response);

		utility.printHtml("Footer.html");
	}

}