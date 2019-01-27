import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

@WebServlet("/Startup")

public class Startup extends HttpServlet
{
	public void init() throws ServletException
    {
    	//fetch products from the xml file (only here)
		SAXParser_DataStore productsDataStore = new SAXParser_DataStore();
		Map<String, List<Product>> map = productsDataStore.getProductMap();

		//Store in the global hash map
		ProductsHashMap.setProductsMap(map);

		//insert products into database
		MySqlDataStoreUtilities.insertProductsInDB(map);
    }
}
