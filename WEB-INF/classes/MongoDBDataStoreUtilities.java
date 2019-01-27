import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.AggregationOutput;
import java.util.*;
import com.mongodb.*;

public class MongoDBDataStoreUtilities
{
	static DBCollection myReviews;

	public static DBCollection getConnection()
	{
		MongoClient mongo = new MongoClient("localhost", 27017);
		DB db = mongo.getDB("customerReviews");
		myReviews = db.getCollection("myReviews");
		return myReviews;
	}

	public static String insertReview(String productname,String producttype,String productprice,String retname,String retzip,
		String retcity,String retstate,String prodOnSale,String productmaker,String manufrebate,String username,String userage,
		String usergender,String useroccupation,String reviewrating,String reviewdate,String reviewtext)
	{
		try{
			getConnection();
			BasicDBObject document = new BasicDBObject("title", "myReviews").
				append("user_name", username).
				append("product_name", productname).
				append("product_price", (int)Double.parseDouble(productprice)).
				append("product_type", producttype).
				append("retailer_name", retname).
				append("retailer_zip", retzip).
				append("retailer_city", retcity).
				append("retailer_state", retstate).
				append("sale", prodOnSale).
				append("manuf_name", productmaker).
				append("manuf_rebate", manufrebate).
				append("user_age", (int)Integer.parseInt(userage)).
				append("user_gender", usergender).
				append("user_occupation", useroccupation).
				append("review_rating", (int)Integer.parseInt(reviewrating)).
				append("review_date", reviewdate).
				append("review_text", reviewtext);
			myReviews.insert(document);
			return "Successfull";
		}
		catch(Exception e){
			return "UnSuccessfull";
		}
	}

	public static HashMap<String, ArrayList<Review>> selectReview()
	{
		HashMap<String, ArrayList<Review>> reviews = null;
		try{
			getConnection();
			//return null to indicate that the server is not up and running and empty map for no reviews
			reviews = new HashMap<>();
			DBCursor cursor = myReviews.find();
			ArrayList<Review> list = null;
			ArrayList<Review> pReviewList = null;
			
			while(cursor.hasNext()){
				BasicDBObject doc = (BasicDBObject)cursor.next();
				if(!reviews.containsKey(doc.getString("product_name")))
				{
					list = new ArrayList<>();
					reviews.put(doc.getString("product_name"), list);
				}
				pReviewList = reviews.get(doc.getString("product_name"));
				Review rv = new Review(doc.getString("product_name"),doc.getString("product_type"),doc.getString("product_price"),
					doc.getString("retailer_name"),doc.getString("retailer_zip"),doc.getString("retailer_city"),doc.getString("retailer_state"),
					doc.getString("sale"),doc.getString("manuf_name"),doc.getString("manuf_rebate"),doc.getString("user_name"),
					doc.getString("user_age"),doc.getString("user_gender"),doc.getString("user_occupation"),
					doc.getString("review_rating"),doc.getString("review_date"),doc.getString("review_text"));
				pReviewList.add(rv);
				reviews.put(doc.getString("product_name"), pReviewList);
			}
		}
		catch(Exception e){			
			reviews = null;
		}
		return reviews;
	}

	public static ArrayList<TopRatings> topRatingProducts()
	{
		ArrayList<TopRatings> list = new ArrayList<>();
		HashMap<String, String> map = new HashMap<>();
		try{
			getConnection();
			
			BasicDBObject sorted_doc = new BasicDBObject();
			//set the order to desc ie. 5,4,3,2,1 for top rating products
			sorted_doc.put("review_rating", -1);
			DBCursor cursor = myReviews.find().sort(sorted_doc);
			BasicDBObject doc_obj;
			int count=0;
			while(cursor.hasNext())
			{
				doc_obj = (BasicDBObject)cursor.next();

		  		String pname = doc_obj.get("product_name").toString();
		  		String rating = doc_obj.get("review_rating").toString();
				//System.out.println("...."+pname+", "+rating+", "+doc_obj.get("user_name"));

		  		if(map.containsKey(pname))
		  			continue;
		  		
		  		TopRatings obj = new TopRatings(pname, rating);
		  		list.add(obj);
		  		map.put(pname, rating);
		  		count++;
		  		if(count > 5)
		  			break;
			}
		}
		catch(Exception e){			
			list = null;
		}
		return list;
	}

	public static ArrayList<TopSoldZip> mostSoldProducts_Zip()
	{
		ArrayList<TopSoldZip> list = new ArrayList<>();
		try{
			getConnection();
			
			BasicDBObject groupProducts = new BasicDBObject("_id","$retailer_zip");
			groupProducts.put("count", new BasicDBObject("$sum",1));
			BasicDBObject group = new BasicDBObject("$group", groupProducts);

			BasicDBObject limit_cnt = new BasicDBObject("$limit", 5);
			
			BasicDBObject sort_doc = new BasicDBObject("count", -1);
			BasicDBObject sort = new BasicDBObject("$sort", sort_doc);

			AggregationOutput query_output = myReviews.aggregate(group, sort, limit_cnt);
			TopSoldZip topSold_obj;

			for(DBObject res : query_output.results())
			{
				String zip = res.get("_id").toString();
				String cnt = res.get("count").toString();
				topSold_obj = new TopSoldZip(zip, cnt);
				list.add(topSold_obj);					
			}
		}
		catch(Exception e){
			System.out.println("Error in MongoDB connection: "+e.getMessage());		
			list = null;
		}
		return list;
	}

	public static ArrayList<TopSold> mostSoldProducts()
	{
		ArrayList<TopSold> list = new ArrayList<>();
		try{
			getConnection();

			//group by and sum of the count of the products
			BasicDBObject groupProducts = new BasicDBObject("_id","$product_name");	//to sort key is pname
			groupProducts.put("count", new BasicDBObject("$sum",1));				//on that sorted data apply sql count fn by (sum,1)
			BasicDBObject group = new BasicDBObject("$group", groupProducts);		//final product of the group query

			//limit to #products
			BasicDBObject limit_cnt = new BasicDBObject("$limit", 5);				//upper 5 records

			//sort
			BasicDBObject sort_doc = new BasicDBObject("count", -1);				//acc to the count col defined above sort in desc (-1)
			BasicDBObject sort = new BasicDBObject("$sort", sort_doc);				//final result of the sort query

			AggregationOutput query_output = myReviews.aggregate(group, sort, limit_cnt);
			TopSold topSold_obj;
			String pname, cnt;

			for(DBObject result : query_output.results())
			{
				pname = result.get("_id").toString();		//"_id" col contains the pname
				cnt = result.get("count").toString();		//"count" col in above query contains the count produced by the query result
				topSold_obj = new TopSold(pname, cnt);
				list.add(topSold_obj);			
			}

		}
		catch(Exception e){
			System.out.println("Error in MongoDB connection: "+e.getMessage());		
			list = null;
		}
		return list;
	}

	public static ArrayList<Review> selectReviews_Chart()
	{
		ArrayList<Review> list = new ArrayList<>();
		try{
			getConnection();
			Map<String, Object> map = new HashMap<>();
			map.put("retailer_zip", "$retailer_zip");
			map.put("product_name", "$product_name");
			//map.put("reviewCount", "$count");

			BasicDBObject groupFields = new BasicDBObject("_id", new BasicDBObject(map));
			groupFields.put("count", new BasicDBObject("$sum", 1));
			BasicDBObject group = new BasicDBObject("$group", groupFields);

			BasicDBObject projectFields = new BasicDBObject("_id", 0);
			projectFields.put("retailer_zip", "$_id");
			projectFields.put("product_name", "$product_name");
			projectFields.put("reviewCount", "$count");
			BasicDBObject project = new BasicDBObject("$project", projectFields);			

			BasicDBObject sort = new BasicDBObject();
			sort.put("reviewCount", -1);
			BasicDBObject orderby = new BasicDBObject();
			orderby = new BasicDBObject("$sort", sort);

			AggregationOutput query_output = myReviews.aggregate(group, project, orderby);
			BasicDBObject dbobj, dbobj2, resObj;
			Object obj, obj2;
			Review rv;

			for(DBObject res: query_output.results())
			{
				resObj = (BasicDBObject)res;

				obj = com.mongodb.util.JSON.parse(resObj.getString("retailer_zip"));
				dbobj = (BasicDBObject)obj;

				obj2 = com.mongodb.util.JSON.parse(resObj.toString());//.getString("retailer_zip"));
				dbobj2 = (BasicDBObject)obj2;

				rv = new Review(dbobj.getString("product_name"), dbobj.getString("retailer_zip"), dbobj2.getString("reviewCount"), null);
				list.add(rv);
			}
		}
		catch(Exception e){
			System.out.println("Error in MongoDB connection: "+e.getMessage());		
			list = null;
		}
		return list;
	}

	//Assignment 4
	public static ArrayList<StateProductPrices> selectReviewCount_rating5_everyState()
	{
		ArrayList<StateProductPrices> list = new ArrayList<>();
		try
		{
			getConnection();

			BasicDBObject rating = new BasicDBObject("review_rating", 5);
			//rating.put("review_rating", "5");
			BasicDBObject match = new BasicDBObject("$match", rating);
			
			BasicDBObject groupProducts = new BasicDBObject("_id","$retailer_state");	//to sort key is pname
			groupProducts.put("count", new BasicDBObject("$sum",1));				//on that sorted data apply sql count fn by (sum,1)
			BasicDBObject group = new BasicDBObject("$group", groupProducts);		//final product of the group query

			//sort
			BasicDBObject sort_doc = new BasicDBObject("count", 1);				//acc to the count col defined above sort in desc (-1)
			BasicDBObject sort = new BasicDBObject("$sort", sort_doc);				//final result of the sort query

			AggregationOutput query_output = myReviews.aggregate(match, group, sort);
			String state;
			double count;
			StateProductPrices obj;

			for(DBObject result : query_output.results())
			{
				state = result.get("_id").toString();		//"_id" col contains the pname
				count = Double.parseDouble(result.get("count").toString());		//"count" col in above query contains the count produced by the query result
				obj = new StateProductPrices(state, count);
				list.add(obj);
			}

		}
		catch(Exception e){
			System.out.println("Error in MongoDB connection: "+e.getMessage());		
			list = null;
		}
		return list;
	}

	public static ArrayList<StateProductPrices> selectReviewCount_everyState()
	{
		ArrayList<StateProductPrices> list = new ArrayList<>();
		try
		{
			getConnection();
			
			BasicDBObject groupProducts = new BasicDBObject("_id","$retailer_state");	//to sort key is pname
			groupProducts.put("count", new BasicDBObject("$sum",1));				//on that sorted data apply sql count fn by (sum,1)
			BasicDBObject group = new BasicDBObject("$group", groupProducts);		//final product of the group query

			//sort
			BasicDBObject sort_doc = new BasicDBObject("count", 1);				//acc to the count col defined above sort in desc (-1)
			BasicDBObject sort = new BasicDBObject("$sort", sort_doc);				//final result of the sort query

			AggregationOutput query_output = myReviews.aggregate(group, sort);
			String state;
			double count;
			StateProductPrices obj;

			for(DBObject result : query_output.results())
			{
				state = result.get("_id").toString();		//"_id" col contains the pname
				count = Double.parseDouble(result.get("count").toString());		//"count" col in above query contains the count produced by the query result
				obj = new StateProductPrices(state, count);
				list.add(obj);
			}
		}
		catch(Exception e){
			System.out.println("Error in MongoDB connection: "+e.getMessage());		
			list = null;
		}
		return list;
	}

}