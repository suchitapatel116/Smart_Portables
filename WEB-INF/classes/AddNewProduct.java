import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/AddNewProduct")

public class AddNewProduct extends HttpServlet
{
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException,IOException
	{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");

		String contentData = "<div id='content'>";
		String action = request.getParameter("action");

		if(action.equalsIgnoreCase("AddNew"))
		{
			String ptype = request.getParameter("prodType");
			String pname = request.getParameter("name");
			String pprice = request.getParameter("price");
			String pimage = request.getParameter("image");
			String pmanuf = request.getParameter("manuf");
			String pcondition = request.getParameter("condition");
			String pdiscount = request.getParameter("discount");
			String paccessories = request.getParameter("access");

			ArrayList<String> acc_list = new ArrayList<>();
			//acc_list contains all accessories in one string - to change in proper form
			acc_list.add(paccessories);
			Product pd = new Product(pname, pname, pprice, pimage, pmanuf, pcondition, pdiscount, acc_list, ptype, null, null);
			
			//add product in hash map
			
			
			//add product in database
			boolean isInserted = MySqlDataStoreUtilities.insertProduct(pd);
			if(isInserted) {
				contentData += "<h3>Product '"+pname+"' added successfully.</h3>";
				//add product in hash map
				ProductsHashMap.addProductInMap(pd);
			}
			else
				contentData += "<h3>Error inserting the product.</h3>";
		}
		else if(action.equalsIgnoreCase("delete"))
		{
			String ptype = request.getParameter("ptype");
			String pname = request.getParameter("pname");

			//add product in database
			boolean isDeleted = MySqlDataStoreUtilities.deleteProduct(pname);
			if(isDeleted) {
				contentData += "<h3>Product '"+pname+"' deleted successfully.</h3>";
				//add product in hash map
				ProductsHashMap.removeProductInMap(ptype, pname);
			}
			else
				contentData += "<h3>Error deleting the product.</h3>";
		}
		else if(action.equalsIgnoreCase("update"))
		{
			String ptype = request.getParameter("prodType");
			String pname = request.getParameter("pname");
			String pprice = request.getParameter("price");
			String pimage = request.getParameter("image");
			String pmanuf = request.getParameter("manuf");
			String pcondition = request.getParameter("condition");
			String pdiscount = request.getParameter("discount");
			String paccessories = request.getParameter("access");

			ArrayList<String> acc_list = new ArrayList<>();
			//acc_list contains all accessories in one string - to change in proper form
			acc_list.add(paccessories);
			Product pd = new Product(pname, pname, pprice, pimage, pmanuf, pcondition, pdiscount, acc_list, ptype, null, null);
			
			//add product in hash map
			

			//add product in database
			boolean isUpdated = MySqlDataStoreUtilities.updateProduct(pd);
			if(isUpdated) {
				contentData += "<h3>Product '"+pname+"' updated successfully.</h3>";
				//add product in hash map
				ProductsHashMap.updateProductInMap(ptype, pname, pd);
			}
			else
				contentData += "<h3>Error updating the product.</h3>";
		}


		contentData += "</div>";
		pw.println(contentData);
		utility.printHtml("Footer.html");
	}
}