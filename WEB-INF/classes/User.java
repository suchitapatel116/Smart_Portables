import java.io.*;
import java.io.Serializable; 
/* 
	This is a bean for maintaining users information.
	Users class contains class variables id,name,password,usertype.
	Users class has a constructor with Arguments name, String password, String usertype.
	Users  class contains getters and setters for id,name,password,usertype.
*/

public class User implements Serializable
{
	private String userName;
	private String password;
	private String userType;
	private String emailID;

	User()
	{}
	User(String uname, String password, String userType)
	{
		this.userName = uname;
		this.password = password;
		this.userType = userType;
		this.emailID = null;
	}
	User(String uname, String password, String userType, String email)
	{
		this.userName = uname;
		this.password = password;
		this.userType = userType;
		this.emailID = email;
	}
	
	public String getUserName()
	{
		return(this.userName);
	}
	public String getPassword()
	{
		return(this.password);
	}
	public String getUserType()
	{
		return(this.userType);
	}
	public String getEmailID()
	{
		return(this.emailID);
	}
	
	public void setUserName(String uname)
	{
		this.userName = uname;
	}
	public void setPassword(String password)
	{
		this.password = password;
	}
	public void setUserType(String userType)
	{
		this.userType = userType;
	}
	public void setEmailID(String email)
	{
		this.emailID = email;
	}
}
