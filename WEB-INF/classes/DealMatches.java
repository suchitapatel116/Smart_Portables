import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/DealMatches")

public class DealMatches extends HttpServlet
{
	public static String HOME_DIR = System.getenv("ANT_HOME");
	public static String DEAL_MATCHES_FILE = HOME_DIR + "\\webapps\\A5\\DealMatches.txt";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);

		String contentData = "<div id='content'>";

		//display tweets
		contentData += "<div><h2><a href='#'>Welcome to Smart Portables</a></h2></div>"
					+"<div id='holder'>"

					+"<h3 id='product_header'>We beat our competitors in all aspects. Price-Match Guaranteed.</h3>"
					+"<div id='products_holder'>";

		BufferedReader reader = null;
		//<product name, product>
		HashMap<String, Product> selected_products = new HashMap<>();
		try {
			String line, line_org, name;
			List<Product> allProducts = MySqlDataStoreUtilities.getDBProductsList(null);
			for(Product pd : allProducts)
			{
				name = pd.getName().toString().toLowerCase();
				if(selected_products.size()>2)
					break;
				else if(selected_products.size() <= 2 && !selected_products.containsKey(name))
				{
					reader = new BufferedReader(new FileReader(DEAL_MATCHES_FILE));
					line_org = reader.readLine();
					line = line_org.toLowerCase();
					if(line == null)
					{
						contentData += "<h3 style='font-size: 18px;'>No offers found.</h3>";
					}
					else
					{
						do{
							line = line_org.toLowerCase();
							if(line.contains(name)) {
								contentData += "</br><p><a>"+line_org+"</a></p>";
								selected_products.put(pd.getName().toString(), pd);	
							}
						}while((line_org = reader.readLine()) != null && (selected_products.size() < 2));
					}
				}
			}
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			contentData += "<h3 style='font-size: 18px;'>No offers found.</h3>";
		}
		finally{
			if(reader != null)
				reader.close();
		}

		//Display products matching those tweets
		if(selected_products.size() == 0)
		{
			contentData += "</br><h3 id='product_header'>Matching Deals</h3>"
						+"<h3 style='font-size: 18px;'>No matching deals found.</h3>";
		}
		else
		{
			contentData += "</br><h3 id='product_header'>Matching Deals</h3>"
					+"<div id='products_holder'>"
						+"<div class='entry'>"
							+"<table id='best_seller'>"
								+"<tr>";

			for(Map.Entry<String, Product> entry : selected_products.entrySet())
			{
				contentData +="<td>"
							+"<div class='item_holder'>"
								+"<h3>"+entry.getValue().getName()+"</h3>"
									+"<strong>$"+entry.getValue().getPrice()+"</strong>"
									+"<ul>"
										+"<li class='zoom' id='item'><img src='images/"+entry.getValue().getImage()+"'></li>"
										+"<li>"
											+"<form action='AddItemsToCart' method='post'>"
												+"<input type='submit' name='buybutton' class='btnbuy' value='Add to Cart'>"
												+"<input type='hidden' name='pid' value='"+entry.getValue().getId()+"'>"
												+"<input type='hidden' name='pname' value='"+entry.getValue().getName()+"'>"
												+"<input type='hidden' name='pprice' value='"+entry.getValue().getPrice()+"'>"
												+"<input type='hidden' name='pimage' value='"+entry.getValue().getImage()+"'>"
												+"<input type='hidden' name='pmanuf' value='"+entry.getValue().getManufacturer()+"'>"
												+"<input type='hidden' name='pcondition' value='"+entry.getValue().getCondition()+"'>"
												+"<input type='hidden' name='pdiscount' value='"+entry.getValue().getDiscount()+"'>"
												+"<input type='hidden' name='passcessories' value='"+entry.getValue().getAccessories()+"'>"
												+"<input type='hidden' name='type' value='"+entry.getValue().getType()+"'>"
											+"</form>"
										+"</li>"
										+"<li class='b'>"
											+"<details class='btnview'> <summary>View Details</summary>"
												+"<ul>"
													+"<li>Retailer : "+entry.getValue().getManufacturer()+"</li>"
													+"<li>Condition: "+entry.getValue().getCondition()+"</li>"
													+"<li>Discount : "+entry.getValue().getDiscount()+"</li>"
												+"</ul>"
											+"</details>"
										+"</li>"
										+"<li>"
											+"<form action='WriteReview' method='get'>"
												+"<input type='submit' name='writeReviewBtn' class='btnReviewLeft' value='Write Review'>"
												+"<input type='hidden' name='pname' value='"+entry.getValue().getName()+"'>"
												+"<input type='hidden' name='type' value='"+entry.getValue().getType()+"'>"
												+"<input type='hidden' name='pprice' value='"+entry.getValue().getPrice()+"'>"
												+"<input type='hidden' name='pdiscount' value='"+entry.getValue().getDiscount()+"'>"
												+"<input type='hidden' name='pmanuf' value='"+entry.getValue().getManufacturer()+"'>"
											+"</form>"
											+"<form action='ViewReview' method='get'>"
												+"<input type='submit' name='viewReviewBtn' class='btnReviewRight' value='View Review'>"
												+"<input type='hidden' name='pname' value='"+entry.getValue().getName()+"'>"
											+"</form>"
										+"</li>"
									+"</ul>"
								+"</div>"
							+"</td>";
			}
			contentData += "</tr></table></div></div>";
		}

		contentData += "</div></div></div>";
		pw.println(contentData);
	}
}