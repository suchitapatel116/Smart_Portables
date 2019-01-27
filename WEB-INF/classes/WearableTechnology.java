import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/WearableTechnology")

public class WearableTechnology extends HttpServlet
{
	final static String SESSION_ADDTOCART = "add_to_cart";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");
		String contentData = "<div id='content'>";
		String title = "Wearable Technology";

		//SAXParser_DataStore productsDataStore = new SAXParser_DataStore();
		List<Product> wearable_list = null;

		String productCategory = request.getParameter("maker");
		if(productCategory == null)
		{
			//add all
			wearable_list = new ArrayList<>();
			wearable_list.addAll(ProductsHashMap.getSmartWatchList());
			wearable_list.addAll(ProductsHashMap.getHeadphoneList());
			wearable_list.addAll(ProductsHashMap.getVirtualRealityList());
			wearable_list.addAll(ProductsHashMap.getPetTrackerList());
		}
		else if(productCategory.equalsIgnoreCase("smartwatch")){
			wearable_list = ProductsHashMap.getSmartWatchList();
			title = "Smart Watches";
		}
		else if(productCategory.equalsIgnoreCase("headphones")){
			wearable_list = ProductsHashMap.getHeadphoneList();
			title = "Headphones";
		}
		else if(productCategory.equalsIgnoreCase("virtualreality")){
			wearable_list = ProductsHashMap.getVirtualRealityList();
			title = "Virtual Reality";
		}
		else if(productCategory.equalsIgnoreCase("pettracker")){
			wearable_list = ProductsHashMap.getPetTrackerList();
			title = "Pet Trackers";
		}

		HttpSession session = request.getSession();
		Product product;
		if(wearable_list == null)
		{
			contentData += "null data</div>";
		}
		else if(wearable_list != null)
		{
			//contentData += "<div>no of products = "+wearable_list.size()+" </div>";
			contentData += "<div id='holder'>"
							+ "<h3 id='product_header'>"+title+"</h3>"
							+ "<div id='products_holder'>"
								+ "<div class='entry'>"
									+ "<table id='best_seller'>";

			for(int i=0; i<wearable_list.size(); i++)
			{
				product = (Product)wearable_list.get(i);
				session.setAttribute(SESSION_ADDTOCART, product);

				//Even numbered products(0,2,4,..)-> new row
				if(i%2 == 0)
					contentData += "<tr>";

				contentData += "<td>"
									+"<div class='item_holder'>"
										+"<h3>"+product.getName()+"</h3>"
										+"<strong>$"+product.getPrice()+"</strong>"
										+"<ul>"
											+"<li class='zoom' id='item'><img src='images/"+product.getImage()+"'></li>"
											+"<li>"
												+"<form action='AddItemsToCart' method='post'>"
													+"<input type='submit' name='buybutton' class='btnbuy' value='Add to Cart'>"
													+"<input type='hidden' name='pid' value='"+product.getId()+"'>"
													+"<input type='hidden' name='pname' value='"+product.getName()+"'>"
													+"<input type='hidden' name='pprice' value='"+product.getPrice()+"'>"
													+"<input type='hidden' name='pimage' value='"+product.getImage()+"'>"
													+"<input type='hidden' name='pmanuf' value='"+product.getManufacturer()+"'>"
													+"<input type='hidden' name='pcondition' value='"+product.getCondition()+"'>"
													+"<input type='hidden' name='pdiscount' value='"+product.getDiscount()+"'>"
													+"<input type='hidden' name='passcessories' value='"+product.getAccessories()+"'>"
													+"<input type='hidden' name='type' value='WearableTechnology'>"													
													+"<input type='hidden' name='maker' value='"+title+"'>"
													//+"<input type='hidden' name='accessory' value=''>"
												+"</form>"
											+"</li>"
											+"<li class='b'>"
												+"<details class='btnview'> <summary>View Details</summary>"
													+"<ul>"
														+"<li>Retailer : "+product.getManufacturer()+"</li>"
														+"<li>Condition: "+product.getCondition()+"</li>"
														+"<li>Discount : "+product.getDiscount()+"</li>"
													+"</ul>"
												+"</details>"
											+"</li>"
											+"<li>"
												+"<form action='WriteReview' method='get'>"
												+"<input type='submit' name='writeReviewBtn' class='btnReviewLeft' value='Write Review'>"
												+"<input type='hidden' name='pname' value='"+product.getName()+"'>"
												+"<input type='hidden' name='type' value='Wearable Technology'>"
												+"<input type='hidden' name='pprice' value='"+product.getPrice()+"'>"
												+"<input type='hidden' name='pdiscount' value='"+product.getDiscount()+"'>"
												+"<input type='hidden' name='pmanuf' value='"+product.getManufacturer()+"'>"
												+"</form>"
												+"<form action='ViewReview' method='get'>"
												+"<input type='submit' name='viewReviewBtn' class='btnReviewRight' value='View Review'>"
												+"<input type='hidden' name='pname' value='"+product.getName()+"'>"
												+"</form>"
											+"</li>"
										+"</ul>"
									+"</div>"
								+"</td>";

				if(i%2 != 0)
					contentData += "</tr>";
			}

			contentData += "</table></div></div></div></div>";
		}

		pw.println(contentData);
		utility.printHtml("Footer.html");
	}
}