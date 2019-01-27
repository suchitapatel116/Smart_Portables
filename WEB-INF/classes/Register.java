import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/Register")

public class Register extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException,IOException
	{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");

		//String fname=(String)request.getAttribute("fname");
		//String lname=(String)request.getAttribute("lname");
		//String uname=(String)request.getAttribute("uname");

		String contentData = "<div id='content'>"
		+"<h2 style='color: #4076AB;'>Registration Form</h2>"
        +"<fieldset>"
            //+"<legend>Form legend</legend>"
            +"<form action='RegisterData' method='post'>"
                +"<p>"
                	+"<label for='userType'>User Type:</label>"
                    +"<select name='userType' required>"
                    	+"<option value='customer' >Customer</option> "
				 		+"<option value='salesman' >Salesman</option> "
				 		+"<option value='storemanager' >Store Manager</option> "
				 	+"</select>"
                +"</p><p>"
                	+"<label for='uname'>Name:</label>"
                    +"<input type='text' name='uname' placeholder='Enter Username' required />"
                +"</p><p>"
                    +"<label for='email'>Email:</label>"
                    +"<input type='email' name='email' placeholder='Enter Email ID' required/>"
                +"</p><p>"
                    +"<label for='password'>Password:</label>"
                    +"<input type='password' name='password' value='' placeholder='Enter Password' required />"
                +"</p>"
                +"<p id='test'><input name='register' style='margin-left: 150px;' class='regButton' value='Register' type='submit' /></p>"
            +"</form>"
        +"</fieldset>"
        +"</div>";
		//--dont end body tag
		
		pw.println(contentData);
		utility.printHtml("Footer.html");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException,IOException
	{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		String error_msg = null;

		if(request.getAttribute("msg") != null )
		{
			error_msg = (String)request.getAttribute("msg");
			//pw.println("<div style='background-color:red;color:white;'>Username already exists ! Please Select Another Username</div>");  
			request.setAttribute("msg","");

		}

		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");

		//String fname=(String)request.getAttribute("fname");
		//String lname=(String)request.getAttribute("lname");
		//String uname=(String)request.getAttribute("uname");

		String contentData = "<div id='content'>"
		+"<h2 style='color: #4076AB;'>Registration Form</h2>"
        +"<fieldset>"
            //+"<legend>Form legend</legend>"
            +"<form action='RegisterData' method='post'>";

            if(error_msg != null)
            	contentData += "<p style='color:red; font-size:20px; margin-left: 150px; margin-bottom:10px;'>" + error_msg
            					+"</p>";

    contentData +="<p>"
                	+"<label for='userType'>User Type:</label>"
                    +"<select name='userType' required>"
                    	+"<option value='customer' >Customer</option> "
				 		+"<option value='salesman' >Salesman</option> "
				 		+"<option value='storemanager' >Store Manager</option> "
				 	+"</select>"
                +"</p><p>"
                	+"<label for='uname'>Name:</label>"
                    +"<input type='text' name='uname' placeholder='Enter Username' required />"
                +"</p><p>"
                    +"<label for='email'>Email:</label>"
                    +"<input type='email' name='email' placeholder='Enter Email ID' required/>"
                +"</p><p>"
                    +"<label for='password'>Password:</label>"
                    +"<input type='password' name='password' value='' placeholder='Enter Password' required />"
                +"</p>"
                +"<p id='test'><input name='register' style='margin-left: 150px;' class='regButton' value='Register' type='submit' /></p>"
            +"</form>"
        +"</fieldset>"
        +"</div>";
		//--dont end body tag
		
		pw.println(contentData);
		utility.printHtml("Footer.html");
	}


}