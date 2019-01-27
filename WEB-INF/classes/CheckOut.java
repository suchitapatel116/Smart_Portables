import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/CheckOut")

public class CheckOut extends HttpServlet
{
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		HttpSession session = request.getSession();
		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");

		String total = request.getParameter("total");
		//String orderno = request.getParameter("orderno");


		String contentData = "<div id='content'>"
		+"<h2 style='color: #4076AB;'>Billing Information</h2>"
        +"<fieldset>"
            +"<form action='CheckOutData' method='post'>"
                +"<p>"
                	+"<label for='name'>Full Name:</label>"
                    +"<input type='text' name='name' placeholder='Enter Full Name' required />"
                +"</p><p>"
	                +"<label for='addr'>Address:</label>"
	                +"<input type='text' name='addr' placeholder='Enter Address' required />"
	            +"</p><p>"
                    +"<label for='state'>State:</label>"
                    +"<input type='text' name='state' placeholder='Enter State' required />"
                +"</p><p>"
	            	+"<label for='contact'>Contact:</label>"
                    +"<input type='text' name='contact' placeholder='Enter Contact Number' required />"
                +"</p></br><p>"
                	+"<label for='total'>TOTAL:</label><strong>$"+total+"</strong>"
                    //+"<input type='text' name='total' placeholder='Enter Username' />"
                +"</p><p>"
                    +"<label for='cardno'>Credit Card No:</label>"
                    +"<input type='text' name='cardno' placeholder='Enter Card Number' required/>"
                +"</p><p>"
                    +"<label for='cvv'>CVV:</label>"
                    +"<input type='password' name='cvv' value='' placeholder='Enter CVV' required />"
                +"</p>"
                +"<p id='test'><input name='pay' style='margin-left: 150px;' class='regButton' value='Make Payment' type='submit' /></p>"
                //+"<input type='hidden' name='orderno' value='"+orderno+"'>"
            +"</form>"
        +"</fieldset>"
        +"</div>";

		pw.println(contentData);
		utility.printHtml("Footer.html");
	}
}