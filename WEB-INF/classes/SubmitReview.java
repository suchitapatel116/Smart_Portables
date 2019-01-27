import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/SubmitReview")

public class SubmitReview extends HttpServlet
{
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
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
		String type = request.getParameter("type");
		String pprice = request.getParameter("pprice");
		String pmanuf = request.getParameter("pmanuf");
		String prodOnSale = request.getParameter("pdiscount");
		
		String retname = request.getParameter("retname");
		String retzip = request.getParameter("retzip");
		String retcity = request.getParameter("retcity");
		String retstate = request.getParameter("retstate");
		String manufrebate = request.getParameter("pmnufRebate");
		String userage = request.getParameter("userage");
		String usergender = request.getParameter("usergender");
		String useroccupation = request.getParameter("useroccup");
		String reviewrating = request.getParameter("reviewrating");
		String reviewdate = request.getParameter("reviewdate");
		String reviewtext = request.getParameter("reviewtext");
	
		String contentData = "<div id='content'>";
		
		String result = MongoDBDataStoreUtilities.insertReview(pname,type,pprice,retname,retzip,retcity,retstate,prodOnSale,
			pmanuf,manufrebate,uname,userage,usergender,useroccupation,reviewrating,reviewdate,reviewtext);

		if(result.equalsIgnoreCase("Successfull")){
			contentData +="<h3>Review for '"+pname+"' was stored successfully!</h3>";
		}
		else{
			contentData += "<h3>MongoDB server is not up and running</h3>";
		}
		

		contentData += "</div>";
		pw.println(contentData);
		utility.printHtml("Footer.html");
	}
}