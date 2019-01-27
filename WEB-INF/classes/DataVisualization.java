import com.google.gson.Gson;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import com.mongodb.AggregationOutput;

@WebServlet("/DataVisualization")

public class DataVisualization extends HttpServlet 
{
    static DBCollection myReviews;

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException 
	{
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Utilities utility = new Utilities(request, pw);
		utility.printHtml("Header.html");
		utility.printHtml("LeftNavigationBar.html");
		
		String contentData = "<div id='content'>"
						
						+"<h3 style='color: #4076AB;'>Data Visualization</h3>"
						+"</br><button id='dataVizChartBtn' class='btnReviewLeft'>View Chart</button>"
						+"</br><div id='chart_div'></div>";

		contentData += "</div>";
		pw.println(contentData);
		pw.println("<script type='text/javascript' src=\"https://www.gstatic.com/charts/loader.js\"></script>");
		pw.println("<script type='text/javascript' src='DataVisualization.js'></script>");
		utility.printHtml("Footer.html");
    }

    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException 
	{
		try{
			ArrayList<Review> reviews = MongoDBDataStoreUtilities.selectReviews_Chart();
			ArrayList<Review> topReviews_list = getTop3Reviews_inEveryCity(reviews);

System.out.println("\n............topReviews_list "+ topReviews_list);

			String json = new Gson().toJson(topReviews_list);
System.out.println();
System.out.println();
System.out.println("....json string "+json);

			response.setContentType("application/JSON");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json);
		}
		catch(Exception e){
			System.out.println("DataVisualization Error:"+e.getMessage());
		}
	}

	//Returns top 3 reviews present in each city
	private static ArrayList<Review> getTop3Reviews_inEveryCity(ArrayList<Review> list)
	{
		ArrayList<Review> top3_list = null;
System.out.println("************");
System.out.println("------list-----"+list.size());

		try{
		Collections.sort(list, new Comparator<Review>() {

			public int compare(Review rv1, Review rv2){
				System.out.println("------ojhhhhh-----"+rv2.getReviewRating());
				return Integer.parseInt(rv2.getReviewRating()) - Integer.parseInt(rv1.getReviewRating());
			}
		});
System.out.println("-----------");
		//list of zip codes in which products are reviewed
		Set<String> zip_list = new HashSet<>();
		String zip;
		for(Review rv: list) {
			zip = rv.getRetZip();
			zip_list.add(zip);
		}
System.out.println("^^^^^^^^^^^^^^^^^^^");
		top3_list = new ArrayList<>();
		ArrayList<Review> arr;
		for(String entry: zip_list){
			arr = new ArrayList<>();
			for(Review rv: list) {
				if(rv.getRetZip().equals(entry) && arr.size()<3)
					arr.add(rv);
			}
			top3_list.addAll(arr);
		}
		}
		catch(Exception e){
			System.out.println("getTop3Reviews_inEveryCity Error:"+e.getMessage());
		}
		if(top3_list == null)
			System.out.println(".....");
		else
			System.out.println("...not null.."+top3_list);
		return top3_list;
	}
}
