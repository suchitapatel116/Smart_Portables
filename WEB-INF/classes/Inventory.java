import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/Inventory")

public class Inventory extends HttpServlet
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
			request.setAttribute("msg", "Please Login to View the Inventory");
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
		String contentData = "<h3 style='color: #4076AB;'>Inventory Report</h3></br>"
				+"<form action='Inventory' method='post'>"
					+"<table id='best_seller'>"
						+"<tr><td>Select: </td>"
						+"<td><input type='radio' style='width:30%;' name='rb_inventoryType' value='rb_prdQty' checked>Available Product Quantity<br>"
							+"<input type='radio' style='width:30%;' name='rb_inventoryType' value='rb_graph' >Graphical Representation"
						+"<td><input type='radio' style='width:30%;' name='rb_inventoryType' value='rb_onSale' >Products on Sale<br>"
							+"<input type='radio' style='width:30%;' name='rb_inventoryType' value='rb_manufRebate' >Products with Manufacturer Rebate"
							+"</td></tr>"

						+"<tr><td></td>"
						+"<td colspan='2' style='padding:30px;'>"
							+"<input type='submit' name='btnfind' style='width:100%;' class='regButton' value='Find Data'/></td>"
						+"</tr>"
					+"</table></form>";
		return contentData;
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
			request.setAttribute("msg", "Please Login to View the Inventory");
			RequestDispatcher rd = request.getRequestDispatcher("/Login");
			rd.forward(request, response);
			return;
		}
		String uname = session.getAttribute("username").toString();
		String contentData = "<div id='content'>";
		contentData += printData() +"<hr>";

		String selectedButton = request.getParameter("rb_inventoryType");

		List<Product> products_list = MySqlDataStoreUtilities.getDBProductsList(null);
		if(selectedButton.equalsIgnoreCase("rb_prdQty"))
		{
			Product pd;
			if(products_list != null) {
				contentData += "<h3 style='color: #4076AB;'>Available Product Quantity</h3><br>"
					+"<table id='best_seller'>"
					+"<tr style='font-weight:bold;'><td style='width:50%;'>Product Name</td><td>Price</td><td>Quantity</td></tr>";
				for(int i=0; i<products_list.size(); i++) {				
					pd = products_list.get(i);
					contentData += "<tr><td style='width:50%;'>"+pd.getName()+"</td><td>$ "+pd.getPrice()+"</td><td>"+pd.getQuantity()+"</td></tr>";
				}
				contentData += "</table>";
			}
			else
				contentData += "MySQL server is not up and running.";
		}
		else if(selectedButton.equalsIgnoreCase("rb_graph"))
		{
			contentData += "<h3 style='color: #4076AB;'>Graphical Representation of Product Quantity</h3><br>"
				+"<div id='productQty_barChart'>Chart</div></br>";
				
			pw.println("<script src='http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js'></script>");
			pw.println("<script type='text/javascript' src='https://www.google.com/jsapi'></script>");
			pw.println("<script type='text/javascript' src=\"https://www.gstatic.com/charts/loader.js\"></script>");
			pw.println("<script type='text/javascript' src='QtyVisualizationChart.js'></script>");
		}
		else if(selectedButton.equalsIgnoreCase("rb_onSale"))
		{
			Product pd;
			if(products_list != null) {
				contentData += "<h3 style='color: #4076AB;'>Products on Sale</h3><br>"
					+"<table id='best_seller'>"
					+"<tr style='font-weight:bold;'><td style='width:50%;'>Product Name</td><td>Price</td><td>Discount</td></tr>";
				for(int i=0; i<products_list.size(); i++) {				
					pd = products_list.get(i);
					if(!pd.getDiscount().startsWith("0"))
						contentData += "<tr><td style='width:50%;'>"+pd.getName()+"</td><td>$ "+pd.getPrice()+"</td><td>"+pd.getDiscount()+" %</td></tr>";
				}
				contentData += "</table>";
			}
			else
				contentData += "MySQL server is not up and running.";

		}
		else if(selectedButton.equalsIgnoreCase("rb_manufRebate"))
		{
			Product pd;
			if(products_list != null) {
				contentData += "<h3 style='color: #4076AB;'>Products with Manufacturer Rebate</h3><br>"
					+"<table id='best_seller'>"
					+"<tr style='font-weight:bold;'><td style='width:50%;'>Product Name</td><td>Price</td><td>Manufacturer Rebate</td></tr>";
				for(int i=0; i<products_list.size(); i++) {				
					pd = products_list.get(i);
					if(pd.getManufacturerRebate().equalsIgnoreCase("Yes"))
						contentData += "<tr><td style='width:50%;'>"+pd.getName()+"</td><td>$ "+pd.getPrice()+"</td><td>"+pd.getManufacturerRebate()+"</td></tr>";
				}
				contentData += "</table>";
			}
			else
				contentData += "MySQL server is not up and running.";
		}

		contentData += "</div>";
		pw.println(contentData);
		utility.printHtml("Footer.html");	
	}

}