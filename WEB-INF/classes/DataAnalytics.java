import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/DataAnalytics")

public class DataAnalytics extends HttpServlet
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
			request.setAttribute("msg", "Please Login to View Data Analytics");
			RequestDispatcher rd = request.getRequestDispatcher("/Login");
			rd.forward(request, response);
			return;
		}
		String uname = session.getAttribute("username").toString();

		String contentData = "<div id='content'>"
						+"<h3 style='color: #4076AB;'>Data Analytics on Reviews</h3></br>";

		contentData += "<form action='DataAnalytics' method='post'>"
					+"<table id='best_seller'>"
					+"<tr><td><input type='checkbox' name='cb_pcat' value=''></input></td>"
						+"<td>Product Name: </td>"
						+"<td><select name='pcat'>"
							+"<option value='all' selected>All Categories</option>"
							+"<option value='Wearable Technology'>Wearable Technology</option>"
							+"<option value='Phone'>Phones</option>"
							+"<option value='Laptop'>Laptops</option>"
							+"<option value='Miscellaneous Products'>Miscellaneous Products</option>"
							+"</select></td>"
						+"<td></td></tr>"

						+"<tr><td><input type='checkbox' name='cb_pprice' value=''></input></td>"
						+"<td>Product Price: </td>"
						+"<td><input type='text' name='pprice'></td>"
						+"<td><input type='radio' style='width:20%;' name='rb_price' value='eq' checked>Equals<br>"
							+"<input type='radio' style='width:20%;' name='rb_price' value='gt' >Greater than<br>"
							+"<input type='radio' style='width:20%;' name='rb_price' value='lt' >Less than<br>"
							+"</td></tr>"

						+"<tr><td><input type='checkbox' name='cb_reviewratings' value=''></input></td>"
						+"<td>Review Ratings: </td>"
						+"<td><select name='reviewratings'>"
							+"<option value='1' selected>1</option>"
							+"<option value='2'>2</option>"
							+"<option value='3'>3</option>"
							+"<option value='4'>4</option>"
							+"<option value='5'>5</option>"
							+"</select></td>"
						+"<td><input type='radio' style='width:20%;' name='rb_reviewratings' value='eq' checked>Equals</br>"
							+"<input type='radio' style='width:20%;' name='rb_reviewratings' value='gt' >Greater than</br>"
							+"</td></tr>"

						+"<tr><td><input type='checkbox' name='cb_retcity' value=''></input></td>"
						+"<td>Retailer City: </td>"
						+"<td><input type='text' name='retcity'></td>"
						+"<td></td></tr>"

						+"<tr><td><input type='checkbox' name='cb_groupby'value=''/></td>"
						+"<td>Group By:</td>"
						+"<td><select name='g_area'>"
							+"<option value='city' selected>City</option>"
							+"<option value='zip'>Zip code</option>"
							+"</select></td>"
						+"<td><input type='radio' style='width:20%;' name='rb_groupby' value='count' checked>Count<br>"
							+"<input type='radio' style='width:20%;' name='rb_groupby' value='detail' >Detail<br>"
							+"</td>"
						+"</tr>"

						+"<tr><td></td>"
						+"<td></td>"
						+"<td><input type='submit' name='btnfind' class='regButton' value='Find Data'/></td>"
						+"</tr>"

					+"</table></form>";

		contentData += "</div>";
		pw.println(contentData);
		utility.printHtml("Footer.html");
	}

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
			request.setAttribute("msg", "Please Login to View Data Analytics");
			RequestDispatcher rd = request.getRequestDispatcher("/Login");
			rd.forward(request, response);
			return;
		}
		String uname = session.getAttribute("username").toString();

		String contentData = "<div id='content'>"
						+"<h3 style='color: #4076AB;'>Data Analytics on Reviews</h3></br>";
		
		String cbpcat = request.getParameter("cb_pcat");
		String cbpprice = request.getParameter("cb_pprice");
		String cbrr = request.getParameter("cb_reviewratings");
		String cbretcity = request.getParameter("cb_retcity");
		String cbgroupby = request.getParameter("cb_groupby");

		HashMap<String, ArrayList<Review>> allReviews = MongoDBDataStoreUtilities.selectReview();
		if(allReviews == null)
			contentData += "<h3>MongoDB server is not up and running.</h3>";
		else if(allReviews.isEmpty())
			contentData += "<h3>No reviews are present currently.</h3>";
		else
		{
			HashMap<String, ArrayList<Review>> all_rv_temp = new HashMap<>();
			all_rv_temp.putAll(allReviews);
			ArrayList<Review> list = new ArrayList<>();

			if(cbpcat != null)
			{
//System.out.println("--------------"+all_rv_temp);
				String pcat = request.getParameter("pcat");
				
				ArrayList<Review> all_revs = new ArrayList<>();
				//ArrayList<Review> temp = new ArrayList<>();
				for(String key: all_rv_temp.keySet())
				{
					all_revs.addAll(all_rv_temp.get(key));
				}
				//Get  reviews for only those products that are requested
				if(pcat.equalsIgnoreCase("all"))
				{
					list.addAll(all_revs);
				}
				else
				{
					Review r;
					for(int i=0; i<all_revs.size(); i++){
						r = all_revs.get(i);
						if(r.getPtype().equals(pcat))
							list.add(r);
					} 
				}
				
			}
			if(cbpprice != null){

			}
			if(cbrr != null)
			{
				int rating = Integer.parseInt(request.getParameter("reviewratings").toString());
				String opertaion = request.getParameter("rb_reviewratings");
				
				ArrayList<Review> all_revs = new ArrayList<>();
				for(String key: all_rv_temp.keySet())
					all_revs.addAll(all_rv_temp.get(key));

				Review r;
				for(int i=0; i<all_revs.size(); i++){
					r = all_revs.get(i);
					int rate = Integer.parseInt(r.getReviewRating());
					if(opertaion.equalsIgnoreCase("eq") && rate == rating)
						list.add(r);
					else if(opertaion.equalsIgnoreCase("gt") && rate >= rating)
						list.add(r);
				}
			}
			if(cbretcity != null)
			{
				String city = request.getParameter("retcity");
				
				ArrayList<Review> all_revs = new ArrayList<>();
				for(String key: all_rv_temp.keySet())
					all_revs.addAll(all_rv_temp.get(key));

				Review r;
				for(int i=0; i<all_revs.size(); i++){
					r = all_revs.get(i);
					if(r.getRetCity().equalsIgnoreCase(city))
						list.add(r);
				} 
			}
			if(cbgroupby != null)
			{
				String groupby = request.getParameter("g_area");
				if(groupby.equals("city")){

				}
				else if(groupby.equals("zip")){

				}

			}
			//All the conditions are set now fire the query and display the output
			Review rv;
			for(int i=0; i<list.size(); i++)
			{
				rv = list.get(i);
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
			if(list.size() == 0)
				contentData += "</br>No reviews found.";
		}
		contentData += "</div>";
		pw.println(contentData);
		utility.printHtml("Footer.html");
	}
}