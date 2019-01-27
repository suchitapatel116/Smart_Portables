import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/RemoveFromCart")

public class RemoveFromCart extends HttpServlet
{
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		HttpSession session = request.getSession();
		Utilities utility = new Utilities(request, pw);

		String uname = session.getAttribute("username").toString();
		String pname = request.getParameter("pname");

		CartData.removeUserItem(uname, pname);

		RequestDispatcher rd = request.getRequestDispatcher("/ViewCart");  
		rd.forward(request, response);
	}
}