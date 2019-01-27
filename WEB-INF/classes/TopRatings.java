import java.io.*;
import java.util.*;

public class TopRatings
{
	private String pname;
	private String rating;

	public TopRatings(String name, String rate){
		this.pname = name;
		this.rating = rate;
	}

	public String getPname(){
		return pname;
	}
	public String getRating(){
		return rating;
	}
}