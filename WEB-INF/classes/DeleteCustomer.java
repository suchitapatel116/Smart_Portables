import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/DeleteCustomer")

public class DeleteCustomer extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException,IOException
	{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");

		String contentData = "<div id='content'>";
		HashMap<String,User> hm_users = new HashMap<String,User>();
		String errorMsg = null;

		try{
			if(MySqlDataStoreUtilities.getConnection())
			{
				hm_users = MySqlDataStoreUtilities.getUsers();

				if(hm_users == null || hm_users.isEmpty())
					errorMsg = "No Users Registered";
			}
			else{
				contentData += "<h2>Database server is down. Please try again!</h2>";
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		if(!hm_users.isEmpty())
		{
			contentData += "<table id='cart_best_seller'><br/><br/>";
			contentData += "<tr style='font-weight: bold;'><td></td><td>Name</td><td>User Type</td><td>Email ID</td><td></td><td></td></tr>"
						+"<tr><td></br></td></tr>";

			int i=1;
			User user;
			for(Map.Entry<String, User> entry: hm_users.entrySet())
			{
				user = entry.getValue();
				if(user == null)
					continue;
				contentData += "<tr>"
								+"<td>"+(i++)+"</td><td>"+user.getUserName()+"</td><td>"+user.getUserType()+"</td><td>"+user.getEmailID()+"</td><td>"
								+"<form action='#' method=''>"
								+"<input type='submit' name='rembutton' class='btnremove' value='Update'>"
								+"<input type='hidden' name='pname' value='"+user.getUserName()+"'>"
								+"</form>"
								+"</td><td>"
								+"<form action='DeleteCustomerData' method='get'>"
								+"<input type='submit' name='rembutton' class='btnremove' value='Delete'>"
								+"<input type='hidden' name='uname' value='"+user.getUserName()+"'>"
								+"</form></td></tr>";
			}
			contentData += "</table>";
		}
		if(errorMsg != null)
		{
			request.setAttribute("msg", errorMsg);
			RequestDispatcher rd = request.getRequestDispatcher("/DeleteCustomerData");  
			rd.forward(request, response);
		}

		contentData += "</div>";
		pw.println(contentData);
		utility.printHtml("Footer.html");
	}
}