import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/SearchedProductData")

public class SearchedProductData extends HttpServlet
{
	//final static String SESSION_ADDTOCART = "add_to_cart";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException 
	{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");

		Product product_data = (Product)request.getAttribute("product_data");
		String contentData = "<div id='content'>";
		contentData += "<div id='products_holder'>"
						+ "<div class='entry'>"
							+ "<table id='best_seller'>";

		contentData += "<tr><td>"
					+"<div class='item_holder'>"
						+"<h3>"+product_data.getName()+"</h3>"
						+"<strong>$"+product_data.getPrice()+"</strong>"
						+"<ul>"
							+"<li class='zoom' id='item'><img src='images/"+product_data.getImage()+"'></li>"
							+"<li>"
								+"<form action='AddItemsToCart' method='post'>"
									+"<input type='submit' name='buybutton' class='btnbuy' value='Add to Cart'>"
									+"<input type='hidden' name='pid' value='"+product_data.getId()+"'>"
									+"<input type='hidden' name='pname' value='"+product_data.getName()+"'>"
									+"<input type='hidden' name='pprice' value='"+product_data.getPrice()+"'>"
									+"<input type='hidden' name='pimage' value='"+product_data.getImage()+"'>"
									+"<input type='hidden' name='pmanuf' value='"+product_data.getManufacturer()+"'>"
									+"<input type='hidden' name='pcondition' value='"+product_data.getCondition()+"'>"
									+"<input type='hidden' name='pdiscount' value='"+product_data.getDiscount()+"'>"
									+"<input type='hidden' name='passcessories' value='"+product_data.getAccessories()+"'>"
									+"<input type='hidden' name='type' value='phones'>"													
								+"</form>"
							+"</li>"
							+"<li class='b'>"
								+"<details class='btnview'> <summary>View Details</summary>"
									+"<ul>"
										+"<li>Retailer : "+product_data.getManufacturer()+"</li>"
										+"<li>Condition: "+product_data.getCondition()+"</li>"
										+"<li>Discount : "+product_data.getDiscount()+"</li>"
									+"</ul>"
								+"</details>"
							+"</li>"
							+"<li>"
								+"<form action='WriteReview' method='get'>"
								+"<input type='submit' name='writeReviewBtn' class='btnReviewLeft' value='Write Review'>"
								+"<input type='hidden' name='pname' value='"+product_data.getName()+"'>"
								+"<input type='hidden' name='type' value='Phone'>"
								+"<input type='hidden' name='pprice' value='"+product_data.getPrice()+"'>"
								+"<input type='hidden' name='pdiscount' value='"+product_data.getDiscount()+"'>"
								+"<input type='hidden' name='pmanuf' value='"+product_data.getManufacturer()+"'>"
								+"</form>"
								+"<form action='ViewReview' method='get'>"
								+"<input type='submit' name='viewReviewBtn' class='btnReviewRight' value='View Review'>"
								+"<input type='hidden' name='pname' value='"+product_data.getName()+"'>"
								+"</form>"
							+"</li>"
						+"</ul>"
					+"</div>"
				+"</td></tr>";

		contentData += "</table></div></div></div>";
	
		pw.println(contentData);
		utility.printHtml("Footer.html");
	}
}