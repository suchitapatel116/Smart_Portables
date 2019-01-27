import java.io.*;
import java.util.*;
import java.text.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet("/autocomplete")

public class AutoCompleteServlet extends HttpServlet {

	private ServletContext context;
	String searchId = null;

	@Override
	public void init(ServletConfig config) throws ServletException {
		this.context = config.getServletContext();
	}

	/** 
	 * Handles the HTTP <code>GET</code> method.
	 * @param request servlet request
	 * @param response servlet response
	 * @throws ServletException if a servlet-specific error occurs
	 * @throws IOException if an I/O error occurs
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException 
	{
		try 
		{
			String action = request.getParameter("action");
			searchId = request.getParameter("searchId");

			StringBuffer sb = new StringBuffer();
			boolean namesAdded = false;
			if(searchId == null)
				context.getRequestDispatcher("/Error").forward(request, response);
			else if (action.equals("complete"))
				searchId = searchId.trim().toLowerCase();

			if (action.equals("complete"))
			{
				if (!searchId.equals(""))
				{
					AjaxUtility ajax_utility = new AjaxUtility();
					sb = ajax_utility.readData(searchId);
					if(sb != null || !sb.equals(""))
						namesAdded = true;
					if (namesAdded)
					{
						response.setContentType("text/xml");
						response.getWriter().write("<products>" + sb.toString() + "</products>");
					}
					else
						response.setStatus(HttpServletResponse.SC_NO_CONTENT);
				}
			}
			if(action.equals("lookup"))
			{
				HashMap<String, Product> data_map = AjaxUtility.getData();
				if(searchId != null && data_map.containsKey(searchId.trim()))
				{
					request.setAttribute("product_data", data_map.get(searchId.trim()));
					RequestDispatcher rd = context.getRequestDispatcher("/SearchedProductData");
					rd.forward(request, response);
				}
			}
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

}