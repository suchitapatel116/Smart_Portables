import java.io.*;
import java.util.*;

public class Review
{
	private String pname;
	private String ptype;
	private String pprice;
	private String retName;
	private String retZip;
	private String retCity;
	private String retState;
	private String onSale;
	private String pmanufacturer;
	private String manufRebate;
	private String uname;
	private String userAge;
	private String userGender;
	private String userOccupation;
	private String reviewRating;
	private String reviewDate;
	private String reviewText;

	public Review(){}

	public Review(String pname, String retZip, String reviewRating, String reviewText){
		this.pname = pname;
		this.retZip = retZip;
		this.reviewRating = reviewRating;
		this.reviewText = reviewText;
	}
	
	public Review(String pname, String ptype, String pprice, String retName, String retZip, String retCity,
			String retState, String onSale, String pmanufacturer, String manufRebate, String uname, String userAge,
			String userGender, String userOccupation, String reviewRating, String reviewDate, String reviewText) {
		this.pname = pname;
		this.ptype = ptype;
		this.pprice = pprice;
		this.retName = retName;
		this.retZip = retZip;
		this.retCity = retCity;
		this.retState = retState;
		this.onSale = onSale;
		this.pmanufacturer = pmanufacturer;
		this.manufRebate = manufRebate;
		this.uname = uname;
		this.userAge = userAge;
		this.userGender = userGender;
		this.userOccupation = userOccupation;
		this.reviewRating = reviewRating;
		this.reviewDate = reviewDate;
		this.reviewText = reviewText;
	}

	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPtype() {
		return ptype;
	}
	public void setPtype(String ptype) {
		this.ptype = ptype;
	}
	public String getPprice() {
		return pprice;
	}
	public void setPprice(String pprice) {
		this.pprice = pprice;
	}
	public String getRetName() {
		return retName;
	}
	public void setRetName(String retName) {
		this.retName = retName;
	}
	public String getRetZip() {
		return retZip;
	}
	public void setRetZip(String retZip) {
		this.retZip = retZip;
	}
	public String getRetCity() {
		return retCity;
	}
	public void setRetCity(String retCity) {
		this.retCity = retCity;
	}
	public String getRetState() {
		return retState;
	}
	public void setRetState(String retState) {
		this.retState = retState;
	}
	public String getOnSale() {
		return onSale;
	}
	public void setOnSale(String onSale) {
		this.onSale = onSale;
	}
	public String getPmanufacturer() {
		return pmanufacturer;
	}
	public void setPmanufacturer(String pmanufacturer) {
		this.pmanufacturer = pmanufacturer;
	}
	public String getManufRebate() {
		return manufRebate;
	}
	public void setManufRebate(String manufRebate) {
		this.manufRebate = manufRebate;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getUserAge() {
		return userAge;
	}
	public void setUserAge(String userAge) {
		this.userAge = userAge;
	}
	public String getUserGender() {
		return userGender;
	}
	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}
	public String getUserOccupation() {
		return userOccupation;
	}
	public void setUserOccupation(String userOccupation) {
		this.userOccupation = userOccupation;
	}
	public String getReviewRating() {
		return reviewRating;
	}
	public void setReviewRating(String reviewRating) {
		this.reviewRating = reviewRating;
	}
	public String getReviewDate() {
		return reviewDate;
	}
	public void setReviewDate(String reviewDate) {
		this.reviewDate = reviewDate;
	}
	public String getReviewText() {
		return reviewText;
	}
	public void setReviewText(String reviewText) {
		this.reviewText = reviewText;
	}

	@Override
	public String toString() {
		return "Review [pname=" + pname + ", ptype=" + ptype + ", pprice=" + pprice + ", retName=" + retName
				+ ", retZip=" + retZip + ", retCity=" + retCity + ", retState=" + retState + ", onSale=" + onSale
				+ ", pmanufacturer=" + pmanufacturer + ", manufRebate=" + manufRebate + ", uname=" + uname
				+ ", userAge=" + userAge + ", userGender=" + userGender + ", userOccupation=" + userOccupation
				+ ", reviewRating=" + reviewRating + ", reviewDate=" + reviewDate + ", reviewText=" + reviewText + "]";
	}
}