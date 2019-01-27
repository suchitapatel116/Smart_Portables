import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import org.json.*;

@WebServlet("/DataExplorationUtility")

public class DataExplorationUtility extends HttpServlet
{
	public final String HOME_DIR = System.getenv("ANT_HOME");
	public final String DATA_FILE = "\\webapps\\A5\\data_file.json";

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
			request.setAttribute("msg", "Please Login to access Data Exploration");
			RequestDispatcher rd = request.getRequestDispatcher("/Login");
			rd.forward(request, response);
			return;
		}
		String uname = session.getAttribute("username").toString();
		String contentData = "<div id='content'>";
		contentData += printData();


		contentData += "</div>";
		pw.println(contentData);
		utility.printHtml("Footer.html");	
	}

	private String printData() {
		String contentData = "<h3 style='color: #4076AB;'>Data Exploration</h3></br>"
				+"<form action='DataExplorationChoropleth' method='post' accept-charset=utf-8>"
					+"<table id='best_seller'>"
						+"<tr><td>Select: </td>"
						+"<td><input type='radio' style='width:12%;' name='rb_explorationType' value='rb_prodReviewed' checked>Total number of products reviewed per State<br>"
							+"<input type='radio' style='width:12%;' name='rb_explorationType' value='rb_prodBought' >Total number of products bought per State<br>"
							+"<input type='radio' style='width:12%;' name='rb_explorationType' value='rb_reviewedRating5' >Total number of products reviewed with Rating 5 in Every State<br>"
							+"<input type='radio' style='width:12%;' name='rb_explorationType' value='rb_avgPrice' >Average Price of Products sold in Every State<br>"
							+"<input type='radio' style='width:12%;' name='rb_explorationType' value='rb_totalPrice' >Total price of products sold in Every State"
							+"</td></tr>"

						+"<tr><td></td>"
						+"<td colspan='2' style='padding:30px;'>"
							+"<input type='submit' name='btnfind' style='width:100%;' class='regButton' value='Find Data'/></td>"
						+"</tr>"
					+"</table></form>"
					+"<meta charset='utf-8'>";
		return contentData;
	}

}
