import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/LoginData")

public class LoginData extends HttpServlet
{
	private String errorMsg = null;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException,IOException
	{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");
		
		String contentData = "<div id='content'>";

		//Add data to the data store
		String userName = request.getParameter("uname");
		String password = request.getParameter("password");
		String userType = request.getParameter("usertype");

		if(userType.equals("salesman"))
		{
			HttpSession session = request.getSession();
			if(userName.equals("admin") && password.equals("admin"))
			{		
				session.setAttribute("username", userName);
				session.setAttribute("usertype", userType);
					
				response.sendRedirect("Home");
				return;
			}
			else{
				request.setAttribute("msg", "Error login. Please try again");
				RequestDispatcher rd = request.getRequestDispatcher("/Login");  
				rd.forward(request, response); 
				return;
			}
		}
		if(userType.equals("storemanager"))
		{
			HttpSession session = request.getSession();
			if(userName.equals("storeadmin") && password.equals("storeadmin"))
			{		
				session.setAttribute("username", userName);
				session.setAttribute("usertype", userType);
					
				response.sendRedirect("UpdateProducts");/*
				request.setAttribute("username", userName);
				request.setAttribute("usertype", userType);
				RequestDispatcher rd = request.getRequestDispatcher("/UpdateProducts");  
				rd.forward(request, response); */
				return;
			}
			else{
				request.setAttribute("msg", "Error login. Please try again");
				RequestDispatcher rd = request.getRequestDispatcher("/Login");  
				rd.forward(request, response); 
				return;
			}
		}

		//String fna = HOME_DIR + USER_DETAILS_FILE;
		//ArrayList user_list = new ArrayList<User>();
		HashMap<String,User> hm_users = new HashMap<String,User>();

		try{
			if(MySqlDataStoreUtilities.getConnection()){
				hm_users = MySqlDataStoreUtilities.getUsers();

				if(hm_users == null || hm_users.isEmpty())
					errorMsg = "No user named '"+userName+"' Exists";
			}
			else{
				contentData += "<h2>Database server is down. Please try again!</h2>";
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		if(!hm_users.isEmpty() && hm_users.containsKey(userName))
		{
			User user = (User)hm_users.get(userName);
			if(user.getPassword().equals(password) && user.getUserType().equals(userType))
			{
				if(userType.equals("customer"))
				{
					HttpSession session = request.getSession(true);
					session.setAttribute("username", user.getUserName());
					session.setAttribute("usertype", user.getUserType());
					
					response.sendRedirect("Home");
					return;
					
				}
				//else
					//errorMsg = "";
			}
			else
				errorMsg = "Invalid credentials! Try again";
		}
		else
			errorMsg = "No user named '"+userName+"' Exists";

		if(errorMsg != null)
		{
			request.setAttribute("msg",errorMsg);
			RequestDispatcher rd = request.getRequestDispatcher("/Login");  
			rd.forward(request, response); 
		}

		contentData += "</div>";
		pw.println(contentData);
		utility.printHtml("Footer.html");
	}
}