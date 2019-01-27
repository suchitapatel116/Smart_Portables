import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/ViewReview")

public class ViewReview extends HttpServlet
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
			request.setAttribute("msg", "Please Login to View Reviews");
			RequestDispatcher rd = request.getRequestDispatcher("/Login");
			rd.forward(request, response);
			return;
		}
		String uname = session.getAttribute("username").toString();
		String pname = request.getParameter("pname");

		String contentData = "<div id='content'>";

		HashMap<String, ArrayList<Review>> reviews_map = MongoDBDataStoreUtilities.selectReview();
		ArrayList<Review> product_review_list = new ArrayList<>();
		if(reviews_map == null){
			contentData += "<h3>MongoDB server is not up and running.</h3>";
		}
		else if(reviews_map.isEmpty()){
			contentData += "<h3>No reviews are present currently.</h3>";
		}
		else if(reviews_map.get(pname) == null || reviews_map.get(pname).isEmpty()){
			contentData += "<h3>Cuurently no reviews are available for "+pname+"</h3>";
		}
		else
		{
			Review rv;
			String productName;
			product_review_list = reviews_map.get(pname);
			contentData += "<h3 style='color: #4076AB;'>Reviews for "+pname+"</h3></br>";

			for(int i=0; i<product_review_list.size(); i++)
			{
				rv = product_review_list.get(i);
				productName = rv.getPname();
				//System.out.println("..."+productName+i);
				
				contentData += "<table id='cart_best_seller'>"
							+"<tr><td><strong>Product Model Name: </strong></td><td>"+rv.getPname()+"</td></tr>"
							+"<tr><td><strong>Product Price: </strong></td><td>$"+rv.getPprice()+"</td></tr>"
							+"<tr><td><strong>Product Category: </strong></td><td>"+rv.getPtype()+"</td></tr>"
							+"<tr><td><strong>Product On Sale: </strong></td><td>"+rv.getOnSale()+"</td></tr>"
							+"<tr><td><strong>Manufacturer Name: </strong></td><td>"+rv.getPmanufacturer()+"</td></tr>"
							+"<tr><td><strong>Manufacturer Rebate: </strong></td><td>"+rv.getManufRebate()+"</td></tr>"
							+"<tr><td><strong>Retailer Name: </strong></td><td>"+rv.getRetName()+"</td></tr>"
							+"<tr><td><strong>Retailer Zip: </strong></td><td>"+rv.getRetZip()+"</td></tr>"
							+"<tr><td><strong>Retailer City: </strong></td><td>"+rv.getRetCity()+"</td></tr>"
							+"<tr><td><strong>Retailer State: </strong></td><td>"+rv.getRetState()+"</td></tr>"
							+"<tr><td><strong>User ID: </strong></td><td>"+rv.getUname()+"</td></tr>"
							+"<tr><td><strong>User Age: </strong></td><td>"+rv.getUserAge()+"</td></tr>"
							+"<tr><td><strong>User Gender: </strong></td><td>"+rv.getUserGender()+"</td></tr>"
							+"<tr><td><strong>User Occupation: </strong></td><td>"+rv.getUserOccupation()+"</td></tr>"
							+"<tr><td><strong>Review Rating: </strong></td><td>"+rv.getReviewRating()+"</td></tr>"
							+"<tr><td><strong>Review Date: </strong></td><td>"+rv.getReviewDate()+"</td></tr>"
							+"<tr><td><strong>Review Text: </strong></td><td>"+rv.getReviewText()+"</td></tr>"
							+"</table><hr>";
			}
		}

		contentData += "</div>";
		pw.println(contentData);
		utility.printHtml("Footer.html");
	}
}