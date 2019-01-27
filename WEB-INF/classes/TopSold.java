import java.io.*;
import java.util.*;

public class TopSold
{
	private String pname;
	private String count;

	public TopSold(String pname, String cnt){
		this.pname = pname;
		this.count = cnt;
	}

	public String getPname(){
		return pname;
	}
	public String getCount(){
		return count;
	}
}