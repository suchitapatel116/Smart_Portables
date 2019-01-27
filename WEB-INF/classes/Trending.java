import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/Trending")

public class Trending extends HttpServlet
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
		String contentData = "<div id='content'>";

		//1. Top five most liked products (top 5 higest ratings products)
		ArrayList<TopRatings> topRatings_list = MongoDBDataStoreUtilities.topRatingProducts();
		if(topRatings_list == null)
			contentData += "<h3>MongoDB server is not up and running.</h3>";
		else if(topRatings_list.isEmpty())
			contentData += "<h3>No reviews are available currently.</h3>";
		else {
			contentData += "<h3 style='color: #4076AB;'>Top 5 Most Liked products</h3></br>";
			contentData += "<table id='best_seller_trending'>"
						+"<tr><td><strong>Product Name</strong></td><td><strong>Top Review Ratings</strong></td></tr>";
			TopRatings tr;
			for(int i=0; i<topRatings_list.size(); i++)
			{
				tr = topRatings_list.get(i);
				contentData += "<tr><td>"+tr.getPname()+"</td><td>"+tr.getRating()+"</td></tr>";
			}
			contentData += "</table></br>";
		}

		//2. Top five zip-codes where maximum number of products sold
		ArrayList<TopSoldZip> mostSoldZip_list = MongoDBDataStoreUtilities.mostSoldProducts_Zip();
		if(mostSoldZip_list == null)
			contentData += "<h3>MongoDB server is not up and running.</h3>";
		else if(mostSoldZip_list.isEmpty())
			contentData += "<h3>No reviews are available currently.</h3>";
		else {
			contentData += "<h3 style='color: #4076AB;'>Top 5 Zip Codes where Maximum products sold</h3></br>";
			contentData += "<table id='best_seller_trending'>"
						+"<tr><td><strong>Zip Code</strong></td><td><strong>No of Products Reviewed</strong></td></tr>";
			TopSoldZip tr;
			for(int i=0; i<mostSoldZip_list.size(); i++)
			{
				tr = mostSoldZip_list.get(i);
				contentData += "<tr><td>"+tr.getZip()+"</td><td>"+tr.getCount()+"</td></tr>";
			}
			contentData += "</table></br>";
		}

		//3. Top five most sold products regardless of the rating
		ArrayList<TopSold> mostSold_list = MongoDBDataStoreUtilities.mostSoldProducts();
		if(mostSold_list == null)
			contentData += "<h3>MongoDB server is not up and running.</h3>";
		else if(mostSold_list.isEmpty())
			contentData += "<h3>No reviews are available currently.</h3>";
		else {
			contentData += "<h3 style='color: #4076AB;'>Top 5 most sold Products</h3></br>";
			contentData += "<table id='best_seller_trending'>"
						+"<tr><td><strong>Product Name</strong></td><td><strong>No of Products Sold</strong></td></tr>";
			TopSold tr;
			for(int i=0; i<mostSold_list.size(); i++)
			{
				tr = mostSold_list.get(i);
				contentData += "<tr><td>"+tr.getPname()+"</td><td>"+tr.getCount()+"</td></tr>";
			}
			contentData += "</table></br>";
		}

		contentData += "</div>";
		pw.println(contentData);
		utility.printHtml("Footer.html");
	}
}