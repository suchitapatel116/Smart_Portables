import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/UpdateProducts")

public class UpdateProducts extends HttpServlet
{
	final static String KEY_SMART_WATCH = "smart_watch";
	final static String KEY_HEADPHONES = "headphones";
	final static String KEY_VIRTUAL_REALITY = "virtual_reality";
	final static String KEY_PET_TRACKER = "pet_tracker";
	final static String KEY_PHONE = "phone";
	final static String KEY_LAPTOP = "laptop";
	final static String KEY_SPEAKERS = "speakers";
	final static String KEY_EXTERNAL_STORAGE = "external_storage";
	final static String KEY_ACCESSORIES_ITEMS = "accessory_item";

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException,IOException
	{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");
		
		String contentData = "<div id='content'>";

		List<Product> allProducts = new ArrayList<>();

		//SAXParser_DataStore productsDataStore = new SAXParser_DataStore();
		List<Product> list = ProductsHashMap.getSmartWatchList();
		allProducts.addAll(list);
		list = ProductsHashMap.getHeadphoneList();
		allProducts.addAll(list);
		list = ProductsHashMap.getPhoneList();
		allProducts.addAll(list);
		list = ProductsHashMap.getLaptopList();
		allProducts.addAll(list);
		list = ProductsHashMap.getSpeakersList();
		allProducts.addAll(list);
		list = ProductsHashMap.getExternalStorageList();
		allProducts.addAll(list);
		list = ProductsHashMap.getPetTrackerList();
		allProducts.addAll(list);
		list = ProductsHashMap.getVirtualRealityList();
		allProducts.addAll(list);

		contentData +="<h2 style='color: #4076AB;'>Add New Product</h2>"
        +"<fieldset>"
            +"<form action='AddNewProduct' method='post'>"
            	+"<input type='hidden' name='action' value='AddNew'>"
                +"<p>"
               		+"<label for='prodType'>Product Type:</label>"
                    +"<select name='prodType' required>"
                    	+"<option value='smart_watch' >Smart Watch</option> "
                    	+"<option value='headphones' >Headphones</option> "
                    	+"<option value='virtual_reality' >Virtual Reality</option> "
                    	+"<option value='pet_tracker' >Pet Tracker</option> "
                    	+"<option value='phone' >Phone</option> "
				 		+"<option value='laptop' >Laptop</option> "
				 		+"<option value='speakers' >Speakers</option> "
				 		+"<option value='external_storage' >External Storage</option> "
				 		+"<option value='accessory_item' >Accessory Item</option> "
				 	+"</select>"
				+"</p><p>"	
                	+"<label for='name'>Product Name:</label>"
                    +"<input type='text' name='name' placeholder='Enter Product Name' required />"
                +"</p><p>"
	                +"<label for='price'>Price:</label>"
	                +"<input type='text' name='price' placeholder='Enter Price' required />"
	            +"</p><p>"
	            	+"<label for='image'>Image Url:</label>"
                    +"<input type='text' name='image' placeholder='Enter Image Url' required />"
                +"</p><p>"
                    +"<label for='manuf'>Manufacturer:</label>"
                    +"<input type='text' name='manuf' placeholder='Enter Manufacturer' required/>"
                +"</p><p>"
                    +"<label for='condition'>Condition:</label>"
                    +"<input type='text' name='condition' placeholder='Enter Condition' required/>"
                +"</p><p>"
                    +"<label for='discount'>Discount:</label>"
                    +"<input type='text' name='discount' placeholder='Enter Discount' required/>"
                +"</p><p>"
                    +"<label for='access'>Accessories:</label>"
                    +"<input type='text' name='access' placeholder='Enter comma seperated Accessories' required/>"
                +"</p>"
                +"<p id='test'><input name='addItem' style='margin-left: 150px;' class='regButton' value='Add Item' type='submit' /></p>"
                +"<input type='hidden' name='name' value=''>"
            +"</form>"
        +"</fieldset>";

		contentData += "<h2 style='color: #4076AB;'>Update Product</h2>"
					+"<table id='cart_best_seller'>";
		contentData += "<tr style='font-weight: bold;'><td></td><td>Name</td><td>Price</td><td></td><td></td><td></td></tr>"
						+"<tr><td></br></td></tr>";

		Product p;
		for(int i=0; i<allProducts.size(); i++)
		{
			p = allProducts.get(i);
			contentData += "<tr>"
								+"<td></td><td>"+p.getName()+"</td><td>$"+p.getPrice()+"</td><td>"//+user.getEmailID()+"</td><td>"
								+"<form action='UpdateProducts' method='post'>"
									//+"<input type='hidden' name='action' value='update'>"
									+"<input type='submit' name='rembutton' class='btncancel' value='Update'>"
									+"<input type='hidden' name='pname' value='"+p.getName()+"'>"
									+"<input type='hidden' name='ptype' value='"+p.getType()+"'>"
									//+"<input type='hidden' name='prodObject' value='"+p+"'>"
									+"<input type='hidden' name='pprice' value='"+p.getPrice()+"'>"
									+"<input type='hidden' name='pmanuf' value='"+p.getManufacturer()+"'>"
									+"<input type='hidden' name='pimage' value='"+p.getImage()+"'>"
									+"<input type='hidden' name='pcondition' value='"+p.getCondition()+"'>"
									+"<input type='hidden' name='pdiscount' value='"+p.getDiscount()+"'>"
									+"<input type='hidden' name='paccess' value='"+p.getAccessories()+"'>"
								+"</form>"
								+"</td><td>"
								+"<form action='AddNewProduct' method='post'>"
									+"<input type='hidden' name='action' value='delete'>"
									+"<input type='submit' name='rembutton' class='btncancel' value='Delete'>"
									+"<input type='hidden' name='pname' value='"+p.getName()+"'>"
									+"<input type='hidden' name='ptype' value='"+p.getType()+"'>"
								+"</form></td></tr>";
		}
		contentData += "</table> </br></br>";

		contentData += "</div>";
		pw.println(contentData);
		utility.printHtml("Footer.html");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException,IOException
	{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");
		
		String contentData = "<div id='content'>";

		String ptype = request.getParameter("ptype");
		String pname = request.getParameter("pname");
		String pprice = request.getParameter("pprice");
		String pimage = request.getParameter("pimage");
		String pmanuf = request.getParameter("pmanuf");
		String pcondition = request.getParameter("pcondition");
		String pdiscount = request.getParameter("pdiscount");
		String paccess = request.getParameter("paccess");

		paccess = paccess.replace("[", "");
		paccess = paccess.replace("]", "");
		paccess = paccess.replace(", ", ",");
		paccess = paccess.replace(" ,", ",");

		contentData +="<h2 style='color: #4076AB;'>Update Product</h2>"
        +"<fieldset>"
            +"<form action='AddNewProduct' method='post'>"
            	+"<input type='hidden' name='action' value='update'>"
                +"<p>"
               		+"<label for='prodType'>Product Type:</label>"
                    +"<select name='prodType' required>";

                    	if(ptype.equals(KEY_SMART_WATCH))
                    		contentData +="<option value='smart_watch' selected >Smart Watch</option> ";
                    	else
                    		contentData +="<option value='smart_watch' >Smart Watch</option> ";
						if(ptype.equals(KEY_HEADPHONES))
                    		contentData +="<option value='headphones' selected >Headphones</option> ";
                    	else
                    		contentData +="<option value='headphones' >Headphones</option> ";
						if(ptype.equals(KEY_VIRTUAL_REALITY))
                    		contentData +="<option value='virtual_reality' selected >Virtual Reality</option> ";
                    	else
                    		contentData +="<option value='virtual_reality' >Virtual Reality</option> ";
						if(ptype.equals(KEY_PET_TRACKER))
                    		contentData +="<option value='pet_tracker' selected >Pet Tracker</option> ";
                    	else
                    		contentData +="<option value='pet_tracker' >Pet Tracker</option> ";
						if(ptype.equals(KEY_PHONE))
                    		contentData +="<option value='phone' selected >Phone</option> ";
                    	else
                    		contentData +="<option value='phone' >Phone</option> ";
						if(ptype.equals(KEY_LAPTOP))
                    		contentData +="<option value='laptop' selected >Laptop</option> ";
                    	else
                    		contentData +="<option value='laptop' >Laptop</option> ";
						if(ptype.equals(KEY_SPEAKERS))
                    		contentData +="<option value='speakers' selected >Speakers</option> ";
                    	else
                    		contentData +="<option value='speakers' >Speakers</option> ";
						if(ptype.equals(KEY_EXTERNAL_STORAGE))
                    		contentData +="<option value='external_storage' selected >External Storage</option> ";
                    	else
                    		contentData +="<option value='external_storage' >External Storage</option> ";
						if(ptype.equals(KEY_ACCESSORIES_ITEMS))
                    		contentData +="<option value='accessory_item' selected >Accessory Item</option> ";
                    	else
                    		contentData +="<option value='accessory_item' >Accessory Item</option> ";

		contentData +="</select>"
				+"</p><p>"	
                	+"<label for='name'>Product Name:</label>"+pname
                    +"<input type='hidden' name='pname' value='"+pname+"' />"
                +"</p><p>"
	                +"<label for='price'>Price:</label>"
	                +"<input type='text' name='price' value='"+pprice+"' required />"
	            +"</p><p>"
	            	+"<label for='image'>Image Url:</label>"
                    +"<input type='text' name='image' value='"+pimage+"' required />"
                +"</p><p>"
                    +"<label for='manuf'>Manufacturer:</label>"
                    +"<input type='text' name='manuf' value='"+pmanuf+"' required/>"
                +"</p><p>"
                    +"<label for='condition'>Condition:</label>"
                    +"<input type='text' name='condition' value='"+pcondition+"' required/>"
                +"</p><p>"
                    +"<label for='discount'>Discount:</label>"
                    +"<input type='text' name='discount' value='"+pdiscount+"' required/>"
                +"</p><p>"
                    +"<label for='access'>Accessories:</label>"
                    +"<input type='text' name='access' value='"+paccess+"' required/>"
                +"</p>"
                +"<p id='test'><input name='updateProd' style='margin-left: 150px;' class='regButton' value='Update Product' type='submit' /></p>"
                +"<input type='hidden' name='name' value=''>"
            +"</form>"
        +"</fieldset>";

		contentData += "</div>";
		pw.println(contentData);
		utility.printHtml("Footer.html");
	}
}