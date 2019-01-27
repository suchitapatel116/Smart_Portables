import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/Logout")

public class Logout extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException,IOException
	{
		Utilities utility = new Utilities(request, null);
		utility.logout();
		response.sendRedirect("Home");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException 
	{
		Utilities utility = new Utilities(request, null);
		utility.logout();
		response.sendRedirect("Home");
	}
}