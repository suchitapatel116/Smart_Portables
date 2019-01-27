import java.io.*;
import java.util.*;

public class TopSoldZip
{
	private String zip;
	private String count;

	public TopSoldZip(String zip, String cnt){
		this.zip = zip;
		this.count = cnt;
	}

	public String getZip(){
		return zip;
	}
	public String getCount(){
		return count;
	}
}