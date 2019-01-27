import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/RegisterData")

public class RegisterData extends HttpServlet
{
	private String errorMsg;
	//public final String HOME_DIR = System.getenv("ANT_HOME");
	//public final String USER_DETAILS_FILE = "\\webapps\\Assignment4\\UserDetails.txt";

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
		String email = request.getParameter("email");
		String userType = request.getParameter("userType");

		User user = new User(userName, password, userType, email);
		HashMap<String,User> hm_userdetails = new HashMap<String,User>();

		//String fna = HOME_DIR + USER_DETAILS_FILE;

		try{
			if(MySqlDataStoreUtilities.getConnection())
			{
				hm_userdetails = MySqlDataStoreUtilities.getUsers();

				//No user has registered yet
				if(hm_userdetails == null){
					hm_userdetails = new HashMap<String,User>();
				}
				if(!hm_userdetails.isEmpty() && hm_userdetails.containsKey(userName))
				{
					errorMsg = "Username already taken!";
					request.setAttribute("uname",userName);
					request.setAttribute("email",email);
					request.setAttribute("msg",errorMsg);
					RequestDispatcher rd = request.getRequestDispatcher("/Register");
					rd.forward(request, response);
				}
				else{
					hm_userdetails.put(userName, user);

					//Now put in database
					MySqlDataStoreUtilities.addUser(userName, password, userType, email);
					
					contentData += "<h3>Registered successfully!!</h3>"
									+"</br><p>You can <a href='Login'>Login here</a></p>";
				}			
			}
			else{
				contentData += "<h2>Database server is down. Please try again!</h2>";
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	
		contentData += "</div>";
		pw.println(contentData);
		utility.printHtml("Footer.html");
	}
}