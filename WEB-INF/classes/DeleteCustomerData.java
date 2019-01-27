import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/DeleteCustomerData")

public class DeleteCustomerData extends HttpServlet
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

		String msg = "Error deleting the user";
		
		String uname = request.getParameter("uname");

		try{
			if(MySqlDataStoreUtilities.getConnection()){
				
				if(MySqlDataStoreUtilities.deleteUser(uname))
					msg = "Customer '"+uname+"' deleted sucessfully!";
			}
			else{
				contentData += "<h2>Database server is down. Please try again!</h2>";
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}

		contentData += "<h3>"+msg+"</h3>";

		contentData += "</div>";
		pw.println(contentData);
		utility.printHtml("Footer.html");
	}
}