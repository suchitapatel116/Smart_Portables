import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/WriteReview")

public class WriteReview extends HttpServlet
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
			request.setAttribute("msg", "Please Login to Submit a Review");
			RequestDispatcher rd = request.getRequestDispatcher("/Login");  
			rd.forward(request, response);
			return;
		}
		String uname = session.getAttribute("username").toString();

		String pname = request.getParameter("pname");
		String pprice = request.getParameter("pprice");
		String type = request.getParameter("type");
		String pmanuf = request.getParameter("pmanuf");
		String pdiscount = request.getParameter("pdiscount");
		String onSale = "True";
		if(pdiscount.equals("0.0") || pdiscount.equals("0"))
			onSale = "False";

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
		Calendar cal = Calendar.getInstance();		//Getting current date
		String date = sdf.format(cal.getTime());

		String contentData = "<div id='content'>"
		+"<h2 style='color: #4076AB;'>Write Review</h2>"
		+"<fieldset>"
            +"<form action='SubmitReview' method='post'>"
                +"<p>"
                	+"<label for='pname'>Product Model Name:</label>"+pname
                	+"<input type='hidden' name='pname' value='"+pname+"'/>"
				+"</p><p>"
					+"<label for='pprice'>Product Price:</label>$"+pprice
					+"<input type='hidden' name='pprice' value='"+pprice+"'/>"
				+"</p><p>"
					+"<label for='type'>Product Category:</label>"+type
					+"<input type='hidden' name='type' value='"+type+"'/>"
				+"</p><p>"
					+"<label for='pdiscount'>Product On Sale:</label>"+onSale
					+"<input type='hidden' name='pdiscount' value='"+onSale+"'/>"
				+"</p><p>"
					+"<label for='pmanuf'>Manufacturer Name:</label>"+pmanuf
					+"<input type='hidden' name='pmanuf' value='"+pmanuf+"'/>"
				+"</p><p>"
					+"<label for='pmnufRebate'>Manufacturer Rebate:</label>Yes"
					+"<input type='hidden' name='pmnufRebate' value='Yes'/>"
				+"</p><p>"
                	+"<label for='retname'>Retailer Name:</label>"
                    +"<input type='text' name='retname' value='Smart Portables' />"
                +"</p><p>"
	                +"<label for='retzip'>Retailer Zip:</label>"
	                +"<input type='text' name='retzip' value='60616' />"
	            +"</p><p>"
	                +"<label for='retcity'>Retailer City:</label>"
	                +"<input type='text' name='retcity' value='Chicago' />"
	            +"</p><p>"
	                +"<label for='retstate'>Retailer State:</label>"
	                +"<input type='text' name='retstate' value='Illinois' />"
                +"</p></br><p>"
                	+"<label for='userid'>User ID:</label>"+uname
                +"</p><p>"
                    +"<label for='userage'>User Age:</label>"
                    +"<input type='text' name='userage' placeholder='Enter the Age' required/>"
                +"</p><p>"
                    +"<label for='usergender'>User Gender:</label>"
                    +"<input type='text' name='usergender' placeholder='Enter the Gender' required/>"
                +"</p><p>"
                    +"<label for='useroccup'>User Occupation:</label>"
                    +"<input type='text' name='useroccup' placeholder='Enter the Occupation' />"
                +"</p><p>"
					+"<label for='reviewrating'>Review Rating:</label>"
					+"<select name='reviewrating'>"
					+"<option value='1' selected>1</option>"
					+"<option value='2'>2</option>"
					+"<option value='3'>3</option>"
					+"<option value='4'>4</option>"
					+"<option value='5'>5</option>"
					+"</select>"
				+"</p><p>"
					+"<label for='reviewdate'>Review Date:</label>"
					+"<input type='text' name='reviewdate' value='"+date+"' required/>"
				+"</p><p>"
					+"<label for='reviewtext'>Review Text:</label>"
					+"<textarea name='reviewtext' rows='4' placeholder='Enter the review text' required></textarea>"
                +"</p>"
                +"<p id='test'><input name='write_review' style='margin-left: 150px;' class='regButton' value='Submit Review' type='submit' /></p>"
            +"</form>"
        +"</fieldset>";
        
		contentData += "</div>";
		pw.println(contentData);
		utility.printHtml("Footer.html");
	}
}