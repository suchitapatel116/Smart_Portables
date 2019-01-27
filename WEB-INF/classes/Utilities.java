import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.io.IOException;
import java.io.PrintWriter;

//You can call Servlet as welcome-page using @WebServlet annotation.
@WebServlet("/Utilities")

public class Utilities extends HttpServlet
{
	String url;
	PrintWriter pw;
	HttpServletRequest request;
	HttpSession session;
	//static HashMap<String, Integer> cart_count = new HashMap<>();

	public Utilities(HttpServletRequest req, PrintWriter p)
	{
		this.request = req;
		this.pw = p;
		this.url = getFullUrl();
		this.session = req.getSession(true);
	}

	public String getFullUrl()
	{
		//StringBuffer url = new StringBuffer();
		String url = "http://localhost:80/" + request.getContextPath() + "/";
		return url;
	}

	public void printHtml(String htmlFile)
	{
		//change username
		String fileData = ReadHtmlToString(htmlFile);

		if(htmlFile == "Header.html")
		{
			fileData += "<div id='menu2'><ul float='right'>";

			if(session.getAttribute("username") != null)
			{
				String userName = session.getAttribute("username").toString();
				if(userName.equalsIgnoreCase("admin"))
				{
					fileData += "<li><a href='Register'>Register Customer</a></li>"
							+"<li><a href='DeleteCustomer'>Delete Customer</a></li>"
							+"<li><a>Hi, ADMIN</a></li>"
							+"<li><a href='Logout'>Logout</a></li>";
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
							+"<li><a href='Logout'>Logout</a></li>";
				}
				else
				{
					int count = CartData.getUserCartCount(userName);
				//String userName = session.getAttribute("username").toString();
				userName = userName.toUpperCase();
				fileData += "<li><a href='ViewOrders'>View Orders</a></li>"
							+"<li><a>Hi, "+ userName +"</a></li>"
							+"<li><a href='Logout'>Logout</a></li>"
							+"<li><a href='ViewCart'><i class='glyphicon glyphicon-shopping-cart'> Cart("+count+")</i></a></li>";
				}
			}
			else
			{
				//"<li class='start selected'><a href=''>View Orders</a></li>"+
				fileData +="<li><a href='Login'>View Orders</a></li>"
	            			+"<li><a href='Register'>Register</a></li>"
	        				+"<li><a href='Login'>Login</a></li>"
	        				//"<li class='end'><a href='#'>Cart</a></li>";
		    				+"<li><a href='Login'><i class='glyphicon glyphicon-shopping-cart'> Cart</i></a></li>";
			}
	        fileData += "</ul></div>";
        }
		pw.print(fileData);
	}

	public String ReadHtmlToString(String htmlFile)
	{
		String fileContent = null;
		try {
			String webPage = url + htmlFile;
			URL url = new URL(webPage);
			URLConnection conn = url.openConnection();
			InputStream is = conn.getInputStream();
			InputStreamReader reader = new InputStreamReader(is);

			int numCharRead;
			char[] ch = new char[1024];
			StringBuffer sb = new StringBuffer();
			while((numCharRead = reader.read(ch)) > 0)
				sb.append(ch, 0, numCharRead);

			fileContent = sb.toString();

		}
		catch(Exception e)
		{}
		return fileContent;
	}
	public void logout()
	{
		session.removeAttribute("username");
		session.removeAttribute("usertype");
	}

}