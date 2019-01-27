import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/CancelOrder")

public class CancelOrder extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException,IOException
	{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");

		HttpSession session = request.getSession();
		if(session.getAttribute("username") == null){
			request.setAttribute("msg", "Please Login to Cancel Order");
			RequestDispatcher rd = request.getRequestDispatcher("/Login");  
			rd.forward(request, response);
			return;
		}
		String uname = session.getAttribute("username").toString();
		String oid = request.getParameter("oid");
		String pname = request.getParameter("pname");
		String contentData = "<div id='content'>";
		
		try{
			if(MySqlDataStoreUtilities.getConnection())
			{
				int order_no = Integer.parseInt(oid);
				MySqlDataStoreUtilities.deleteCustomerOrder(order_no, uname, pname);
				contentData += "<h3>Your order no "+oid+" has been cancelled.</h3>";
			}
			else{
				contentData += "<h2>Database server is down. Please try again!</h2>";
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			contentData += "<p style='font-size:18px;'>Error during order cancellation. Please try again.</p>";
		}

		contentData += "</div>";
		pw.println(contentData);
		utility.printHtml("Footer.html");
	}
}