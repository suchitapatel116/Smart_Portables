import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/SalesReports")

public class SalesReports extends HttpServlet
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
			request.setAttribute("msg", "Please Login to View the Sales Reports");
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
		String contentData = "<h3 style='color: #4076AB;'>Sales Report</h3></br>"
				+"<form action='SalesReports' method='post'>"
					+"<table id='best_seller'>"
						+"<tr><td>Select: </td>"
							+"<td><input type='radio' style='width:11%;' name='rb_salesReportType' value='rb_prdSold' checked>Number of Products Sold &nbsp;&nbsp;&nbsp;"
							+"<input type='radio' style='width:10%;' name='rb_salesReportType' value='rb_graph' >Graphical Representation &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
							+"<input type='radio' style='width:10%;' name='rb_salesReportType' value='rb_dailySales' >Daily Sales<br>"
							+"</td></tr>"

						+"<tr><td></td><td colspan='2' style='padding:30px;'>"
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
			request.setAttribute("msg", "Please Login to View the Sales Reports");
			RequestDispatcher rd = request.getRequestDispatcher("/Login");
			rd.forward(request, response);
			return;
		}
		String uname = session.getAttribute("username").toString();
		String contentData = "<div id='content'>";
		contentData += printData();

		String selectedButton = request.getParameter("rb_salesReportType");

		if(selectedButton.equalsIgnoreCase("rb_prdSold"))
		{
			Map<OrderItem, Integer> map = MySqlDataStoreUtilities.getProductSoldCount();
			OrderItem oi;
			if(map != null) {
				contentData += "<h3 style='color: #4076AB;'>Number of Products Sold</h3><br>"
					+"<table id='best_seller'>"
					+"<tr style='font-weight:bold;'><td>Product Name</td><td>Price</td><td>Items Sold</td><td>Total Sales</td></tr>";
				Iterator itr = map.entrySet().iterator();
				while(itr.hasNext())
				{
					Map.Entry entry = (Map.Entry)itr.next();
					oi = (OrderItem)entry.getKey();
					int count = Integer.parseInt(entry.getValue().toString());
					contentData += "<tr><td>"+oi.getProduct_name()+"</td><td>$ "+oi.getPrice()+"</td><td>"+count+"</td><td>$ "+(oi.getPrice()*count)+"</td></tr>";
				}
				contentData += "</table>";
			}
			else
				contentData += "MySQL server is not up and running.";
		}
		else if(selectedButton.equalsIgnoreCase("rb_graph"))
		{
			contentData += "<h3 style='color: #4076AB;'>Graphical Representation of Total Sales</h3><br>"
				+"<div id='totalSales_barChart'>Chart</div></br>";

			pw.println("<script src='http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js'></script>");
			pw.println("<script type='text/javascript' src='https://www.google.com/jsapi'></script>");
			pw.println("<script type='text/javascript' src=\"https://www.gstatic.com/charts/loader.js\"></script>");
			pw.println("<script type='text/javascript' src='TotalSalesVisualizationChart.js'></script>");
		}
		else if(selectedButton.equalsIgnoreCase("rb_dailySales"))
		{
			Map<String, Double> map = MySqlDataStoreUtilities.getDailyTransactions();
			//OrderItem oi;
			if(map != null) {
				contentData += "<h3 style='color: #4076AB;'>Daily Sales Transactions</h3><br>"
					+"<table id='best_seller'>"
					+"<tr style='font-weight:bold;'><td>Date</td><td>Total Sales</td></tr>";
				Iterator itr = map.entrySet().iterator();
				while(itr.hasNext())
				{
					Map.Entry entry = (Map.Entry)itr.next();
					double totalSales = Double.parseDouble(entry.getValue().toString());
					contentData += "<tr><td>"+entry.getKey().toString()+"</td><td>$ "+totalSales+"</td></tr>";
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