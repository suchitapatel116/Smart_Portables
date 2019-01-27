import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/ViewCart")

public class ViewCart extends HttpServlet
{
	//For just clicking users
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();

		HttpSession session = request.getSession();
		//the person is not loged in
		if(session.getAttribute("username") == null){
			request.setAttribute("msg", "Please Login to View Cart");
			RequestDispatcher rd = request.getRequestDispatcher("/Login");  
			rd.forward(request, response);
			return;
		}

		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");

		String contentData = "<div id='content'>"
		+"<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css'>"
		+"<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js'></script>"
		+"<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js'></script>"
		+"<h3> Your cart items</h3></br>";

		//called from additemtocart
		if(request.getAttribute("newItem") != null)
		{
			request.setAttribute("newItem","");
			
			String name = request.getParameter("pname");
			String price = request.getParameter("pprice");
		}
		
		String no_cart_data = null;
		String uname = session.getAttribute("username").toString();

		boolean accessFlag = false;
		//CartData will have products list
		List<Product> items = CartData.getUserCart(uname);
		if(items == null)
			no_cart_data = "Your cart is empty!!";
		else
		{
			//save orders in map
			//orderItems_list will have the same products as ordered items with extra fields
			List<OrderItem> orderItems_list = new ArrayList<>();

			contentData += "<table id='cart_best_seller'>";

			Product p;
			OrderItem o;
			for(int i=0; i<items.size(); i++)
			{
				p = items.get(i);
				contentData += "<tr>"
								+"<td>"+(i+1)+"</td><td>"+p.getName()+"</td><td>$"+p.getPrice()+"</td><td>"
								+"<form action='RemoveFromCart' method='post'>"
								+"<input type='submit' name='rembutton' class='btnremove' value='Remove'>"
								+"<input type='hidden' name='pname' value='"+p.getName()+"'>"
								+"</form></td></tr>";
				double pr = 0;
				try{
					pr = Double.parseDouble(p.getPrice());
				}
				catch(Exception e){
					System.out.println("Error: ViewCart: Error parsing the product data");
				}
				//(int order_id, String username, String product_name, double price, int quantity,
				//double total,String user_addr, double contact_no, String credit_card_no, int cvv, String delivery_date, order date)
				o = new OrderItem(-1, uname, p.getName(), pr, 1, pr, null, 0, null, 0, null, null, null);
				orderItems_list.add(o);

				if(p.getAccessories() != null)
					accessFlag = true;
			}
			double total = CartData.getCartTotal(uname);
			contentData += "<tr><td></br></td></tr><tr style='font-weight: bold;'>"
								+"<td></td><td>Total amount</td><td>$"+total+"</td><td>"
								+"<form action='CheckOut' method='post'>"
								+"<input type='submit' name='checkout' class='btnremove' value='Checkout'>"
								+"<input type='hidden' name='total' value='"+total+"'>"
								+"</form></td></tr>";
			
			for(int i=0; i<orderItems_list.size(); i++)
			{
				((OrderItem)(orderItems_list.get(i))).setTotal(total);
			}
			UserOrderDetails.setUserOrder(uname, orderItems_list);
		}

        contentData += "</table> </br></br>";
    
        //For Carousel
        ProductRecommenderUtility prod_recomm_utility = new ProductRecommenderUtility();
        HashMap<String, String> recommender_map = new HashMap<>();
        recommender_map = prod_recomm_utility.readOutputFile();

        String products;
        Product pp;
        for(String user: recommender_map.keySet())
        {
        	if(user.equals(uname))
        	{
        		contentData += "<h3>Recommended Products</h3>";
	        	contentData += "<table class='carousel_table'><tr><td>"
	        			+"<div id='carousel_holder' class='carousel slide' data-ride='carousel'>"
	        			+"<div class='carousel-inner'>";

        		products = recommender_map.get(user);
        		products = products.replace("[","");
        		products = products.replace("]","");
        		products = products.replace("\""," ");
        		ArrayList<String> list = new ArrayList<>(Arrays.asList(products.split(",")));

        		int i=0;
        		for(String prod : list)
        		{
        			prod = prod.replace("'", "");
        			pp = ProductRecommenderUtility.getProduct(prod.trim());

        			if(i==0){
	        			contentData +="<div class='item active'>"
									+"<div style='text-align: center'>"
										+"<img class='product-image' style='width: 150px; height:150px;' src='images/"+pp.getImage()+"'>"
									+"</div>"
									+"<div style='text-align: center'>"
										+pp.getName()+"<br/>$"+pp.getPrice()
										+"<form action='AddItemsToCart' method='post'>"
											+"<input type='submit' name='buybutton' class='btnbuy' value='Add to Cart'>"
											+"<input type='hidden' name='pid' value='"+pp.getId()+"'>"
											+"<input type='hidden' name='pname' value='"+pp.getName()+"'>"
											+"<input type='hidden' name='pprice' value='"+pp.getPrice()+"'>"
											+"<input type='hidden' name='pimage' value='"+pp.getImage()+"'>"
											+"<input type='hidden' name='pmanuf' value='"+pp.getManufacturer()+"'>"
											+"<input type='hidden' name='pcondition' value='"+pp.getCondition()+"'>"
											+"<input type='hidden' name='pdiscount' value='"+pp.getDiscount()+"'>"
											+"<input type='hidden' name='type' value='phones'>"	
										+"</form>"
									+"</div>"
								+"</div>";
						i++;
	        		}
	        		else{
	        			contentData +="<div class='item'>"
									+"<div style='text-align: center'>"
										+"<img class='product-image' style='width: 150px; height:150px;' src='images/"+pp.getImage()+"'>"
									+"</div>"
									+"<div style='text-align: center'>"
										+pp.getName()+"<br/>$"+pp.getPrice()
										+"<form action='AddItemsToCart' method='post'>"
											+"<input type='submit' name='buybutton' class='btnbuy' value='Add to Cart'>"
											+"<input type='hidden' name='pid' value='"+pp.getId()+"'>"
											+"<input type='hidden' name='pname' value='"+pp.getName()+"'>"
											+"<input type='hidden' name='pprice' value='"+pp.getPrice()+"'>"
											+"<input type='hidden' name='pimage' value='"+pp.getImage()+"'>"
											+"<input type='hidden' name='pmanuf' value='"+pp.getManufacturer()+"'>"
											+"<input type='hidden' name='pcondition' value='"+pp.getCondition()+"'>"
											+"<input type='hidden' name='pdiscount' value='"+pp.getDiscount()+"'>"
											+"<input type='hidden' name='type' value='phones'>"	
										+"</form>"
									+"</div>"
								+"</div>";
	        		}
        		}
        		contentData +="<a class='left carousel-control' href='#carousel_holder' data-slide='prev'>"
							+"<span class='glyphicon glyphicon-chevron-left'></span>"
							+"<span class='sr-only'>Previous</span>"
					  	+"</a>"
					  	+"<a class='right carousel-control' href='#carousel_holder' data-slide='next'>"
							+"<span class='glyphicon glyphicon-chevron-right'></span>"
							+"<span class='sr-only'>Next</span>"
					  	+"</a>";
        
        		contentData += "</div></div></td></tr></table>";
	        	break;
        	}
        }
        //End: For Carousel

        contentData += "</div>";
        pw.println(contentData);

		utility.printHtml("Footer.html");
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException
	{
		//displayCartItems(request, response);
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();

		HttpSession session = request.getSession();
		//the person is not loged in
		if(session.getAttribute("username") == null){
			request.setAttribute("msg", "Please Login to View Cart");
			RequestDispatcher rd = request.getRequestDispatcher("/Login");  
			rd.forward(request, response);
			return;
		}

		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");

		String contentData = "<div id='content'>"
		+"<link rel='stylesheet' href='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css'>"
		+"<script src='https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js'></script>"
		+"<script src='https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js'></script>"
		+"<h3> Your cart items</h3></br>";

		String no_cart_data = null;
		String uname = session.getAttribute("username").toString();

		boolean accessFlag = false;
		List<Product> items = CartData.getUserCart(uname);
		if(items == null){
			System.out.println("items null");
			no_cart_data = "Your cart is empty!!";
			contentData += "<h3>"+no_cart_data+"</h3>";
		}
		else
		{
			contentData += "<table id='cart_best_seller'>";

			Product p;
			for(int i=0; i<items.size(); i++)
			{
				p = items.get(i);
				contentData += "<tr>"
								+"<td>"+(i+1)+"</td><td>"+p.getName()+"</td><td>$"+p.getPrice()+"</td><td>"
								+"<form action='RemoveFromCart' method='post'>"
								+"<input type='submit' name='rembutton' class='btnremove' value='Remove'>"
								+"<input type='hidden' name='pname' value='"+p.getName()+"'>"
								+"</form></td></tr>";

				if(p.getAccessories() != null)
					accessFlag = true;
			}
			String total = ""+CartData.getCartTotal(uname);
			contentData += "<tr><td></br></td></tr><tr style='font-weight: bold;'>"
								+"<td></td><td>Total amount</td><td>$"+total+"</td><td>"
								+"<form action='CheckOut' method='post'>"
								+"<input type='submit' name='checkout' class='btnremove' value='Checkout'>"
								+"<input type='hidden' name='total' value='"+total+"'>"
								+"</form></td></tr>";

			contentData += "</table> </br></br>";

			//For Carousel
	        ProductRecommenderUtility prod_recomm_utility = new ProductRecommenderUtility();
	        HashMap<String, String> recommender_map = new HashMap<>();
	        recommender_map = prod_recomm_utility.readOutputFile();

	        String products;
	        Product pp;
	        for(String user: recommender_map.keySet())
	        {
	        	if(user.equals(uname))
	        	{
	        		contentData += "<h3>Recommended Products</h3>";
	        		contentData += "<table class='carousel_table'><tr><td>"
	        			+"<div id='carousel_holder' class='carousel slide' data-ride='carousel'>"
	        			+"<div class='carousel-inner'>";

	        		products = recommender_map.get(user);
	        		products = products.replace("[","");
	        		products = products.replace("]","");
	        		products = products.replace("\""," ");
	        		ArrayList<String> list = new ArrayList<>(Arrays.asList(products.split(",")));

	        		int i=0;
	        		for(String prod : list)
	        		{
	        			prod = prod.replace("'", "");
	        			pp = ProductRecommenderUtility.getProduct(prod.trim());

	        			if(i==0){
		        			contentData +="<div class='item active'>"
										+"<div style='text-align: center'>"
											+"<img class='product-image' style='width: 150px; height:150px;' src='images/"+pp.getImage()+"'>"
										+"</div>"
										+"<div style='text-align: center'>"
											+pp.getName()+"<br/>$"+pp.getPrice()
											+"<form action='AddItemsToCart' method='post'>"
												+"<input type='submit' name='buybutton' class='btnbuy' value='Add to Cart'>"
												+"<input type='hidden' name='pid' value='"+pp.getId()+"'>"
												+"<input type='hidden' name='pname' value='"+pp.getName()+"'>"
												+"<input type='hidden' name='pprice' value='"+pp.getPrice()+"'>"
												+"<input type='hidden' name='pimage' value='"+pp.getImage()+"'>"
												+"<input type='hidden' name='pmanuf' value='"+pp.getManufacturer()+"'>"
												+"<input type='hidden' name='pcondition' value='"+pp.getCondition()+"'>"
												+"<input type='hidden' name='pdiscount' value='"+pp.getDiscount()+"'>"
												+"<input type='hidden' name='type' value='phones'>"	
											+"</form>"
										+"</div>"
									+"</div>";
							i++;
		        		}
		        		else{
		        			contentData +="<div class='item'>"
										+"<div style='text-align: center'>"
											+"<img class='product-image' style='width: 150px; height:150px;' src='images/"+pp.getImage()+"'>"
										+"</div>"
										+"<div style='text-align: center'>"
											+pp.getName()+"<br/>$"+pp.getPrice()
											+"<form action='AddItemsToCart' method='post'>"
												+"<input type='submit' name='buybutton' class='btnbuy' value='Add to Cart'>"
												+"<input type='hidden' name='pid' value='"+pp.getId()+"'>"
												+"<input type='hidden' name='pname' value='"+pp.getName()+"'>"
												+"<input type='hidden' name='pprice' value='"+pp.getPrice()+"'>"
												+"<input type='hidden' name='pimage' value='"+pp.getImage()+"'>"
												+"<input type='hidden' name='pmanuf' value='"+pp.getManufacturer()+"'>"
												+"<input type='hidden' name='pcondition' value='"+pp.getCondition()+"'>"
												+"<input type='hidden' name='pdiscount' value='"+pp.getDiscount()+"'>"
												+"<input type='hidden' name='type' value='phones'>"	
											+"</form>"
										+"</div>"
									+"</div>";
		        		}
	        		}
	        		contentData +="<a class='left carousel-control' href='#carousel_holder' data-slide='prev'>"
								+"<span class='glyphicon glyphicon-chevron-left'></span>"
								+"<span class='sr-only'>Previous</span>"
						  	+"</a>"
						  	+"<a class='right carousel-control' href='#carousel_holder' data-slide='next'>"
								+"<span class='glyphicon glyphicon-chevron-right'></span>"
								+"<span class='sr-only'>Next</span>"
						  	+"</a>";
	        
	        		contentData += "</div></div></td></tr></table>";
		        	break;
	        	}
	        }
	        //End: For Carousel        

        }

        contentData += "</div>";
        pw.println(contentData);

		utility.printHtml("Footer.html");
	}

}