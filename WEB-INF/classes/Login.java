import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/Login")

public class Login extends HttpServlet
{
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException,IOException
	{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");

		String contentData = "<div id='content'>"
		+"<h2 style='color: #4076AB;'>LOGIN</h2>"
        +"<fieldset>"
            //+"<legend>Form legend</legend>"
            +"<form action='LoginData' method='post'>"
            	//+"<p>"
            		//+"<input type='radio' name='utype' value='customer' class='radioButton' checked> Customer "
            		//+"<input type='radio' name='utype' value='salesman' class='radioButton'> Salesman "
            		//+"<input type='radio' name='utype' value='storemanager' class='radioButton'> Store Manager "
            		+"<div style='margin-left:110px; display: block;'>"
            		+"<label class='radio-inline'><input type='radio' name='usertype' id='customer' value='customer' checked>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Customer </label>"
            		+"<label class='radio-inline'><input type='radio' name='usertype' id='salesman' value='salesman'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Salesman </label>"
            		+"<label class='radio-inline'><input type='radio' name='usertype' id='storemanager' value='storemanager'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Store Manager </label>"
            		+"</div>"

                +"</br><p>"
                	+"<label for='uname'>Username:</label>"
                    +"<input type='text' name='uname' placeholder='Enter Username' required />"
                +"</p><p>"
                    +"<label for='password'>Password:</label>"
                    +"<input type='password' name='password' value='' placeholder='Enter Password' required />"
                +"</p>"
                +"<p id='test'><input name='login' style='margin-left: 150px;' class='regButton' value='Login' type='submit' /></p>"
                +"</br>"
                +"<a href='Register' style='padding-left: 150px;'>Not a user? Register here</a>"
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
			//pw.println("<div style='background-color:red;color:white;'>Error login</div>");  
			request.setAttribute("msg","");
		}

		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");

		String contentData = "<div id='content'>"
		+"<h2 style='color: #4076AB;'>LOGIN</h2>"
        +"<fieldset>"
            //+"<legend>Form legend</legend>"
            +"<form action='LoginData' method='post'>";

            if(error_msg != null)
            	contentData += "<p style='color:red; font-size:20px; margin-left: 150px; margin-bottom:10px;'>" + error_msg
            					+"</p>";
            	
        contentData	+="<div style='margin-left:110px; display: block;'>"
            		+"<label class='radio-inline'><input type='radio' name='usertype' id='customer' value='customer' checked>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Customer </label>"
            		+"<label class='radio-inline'><input type='radio' name='usertype' id='salesman' value='salesman'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Salesman </label>"
            		+"<label class='radio-inline'><input type='radio' name='usertype' id='storemanager' value='storemanager'>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Store Manager </label>"
            		+"</div>"

                +"</br><p>"
                	+"<label for='uname'>Username:</label>"
                    +"<input type='text' name='uname' placeholder='Enter Username' required />"
                +"</p><p>"
                    +"<label for='password'>Password:</label>"
                    +"<input type='password' name='password' value='' placeholder='Enter Password' required />"
                +"</p>"
                +"<p id='test'><input name='login' style='margin-left: 150px;' class='regButton' value='Login' type='submit' /></p>"
                +"</br>"
                +"<a href='Register' style='padding-left: 150px;'>Not a user? Register here</a>"
            +"</form>"
        +"</fieldset>"
        +"</div>";
		//--dont end body tag
		
		pw.println(contentData);
		utility.printHtml("Footer.html");
	}

}