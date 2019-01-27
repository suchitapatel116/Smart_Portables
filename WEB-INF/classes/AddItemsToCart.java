import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/AddItemsToCart")

public class AddItemsToCart extends HttpServlet
{
	//If not signed in just display the login page
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
		throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		HttpSession session = request.getSession();
		Utilities utility = new Utilities(request, pw);

		//the person is not loged in
		if(session.getAttribute("username") == null){
			request.setAttribute("msg", "Please Login to Add items to your Cart");
			RequestDispatcher rd = request.getRequestDispatcher("/Login");
			rd.forward(request, response);
			return;
			/*
			session.setAttribute("msg", "Please Login to Add items to your Cart");
			response.sendRedirect("Login");
			return;
			*/
		}
		String uname = session.getAttribute("username").toString();

		String id = request.getParameter("pid");
		String name = request.getParameter("pname");
		String price = request.getParameter("pprice");
		String image = request.getParameter("pimage");
		String manufacturer = request.getParameter("pmanuf");
		String condition = request.getParameter("pcondition");
		String discount = request.getParameter("pdiscount");
		String asccess = request.getParameter("passcessories");
		String type = request.getParameter("type");

		System.out.println(asccess);

		Product product = new Product(id, name, price, image, manufacturer,condition,discount,null);

		List<Product> items = null;
		//add products to the user specific cart
		
		items = CartData.getUserCart(uname);
		if(items == null){
			items = new ArrayList<>();
			//System.out.println("null");
		}
		else{
			CartData.removeUser(uname);
		}
		
		//System.out.println("before size: "+items.size());
		items.add(product);
		//System.out.println("after size: "+items.size());

		CartData.setUserCart(uname, items);

		//request.setAttribute("msg",errorMsg);
		request.setAttribute("newItem", "New product");
		RequestDispatcher rd = request.getRequestDispatcher("/ViewCart");  
		rd.forward(request, response); 


	}
}