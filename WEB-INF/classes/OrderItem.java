import java.io.*;
import java.util.*;

public class OrderItem implements Serializable
{
	private int order_id;
	private String username;
	private String product_name;
	private double price;
	private int quantity;
	private double total;
	private String user_addr;
	private double contact_no;
	private String credit_card_no;
	private int cvv;
	private String delivery_date;
	private String order_date;
	private String state;

	OrderItem()
	{}
	OrderItem(String product_name, double price)
	{
		this.product_name = product_name;
		this.price = price;
	}
	OrderItem(int order_id, String username, String product_name, double price, int quantity, double total,
			String user_addr, double contact_no, String credit_card_no, int cvv, String delivery_date, String order_date, String state) {
		this.order_id = order_id;
		this.username = username;
		this.product_name = product_name;
		this.price = price;
		this.quantity = quantity;
		this.total = total;
		this.user_addr = user_addr;
		this.contact_no = contact_no;
		this.credit_card_no = credit_card_no;
		this.cvv = cvv;
		this.delivery_date = delivery_date;
		this.order_date = order_date;
		this.state = state;
	}

	public int getOrder_id() {
		return order_id;
	}
	public void setOrder_id(int order_id) {
		this.order_id = order_id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public double getTotal() {
		return total;
	}
	public void setTotal(double total) {
		this.total = total;
	}
	public String getUser_addr() {
		return user_addr;
	}
	public void setUser_addr(String user_addr) {
		this.user_addr = user_addr;
	}
	public double getContact_no() {
		return contact_no;
	}
	public void setContact_no(double contact_no) {
		this.contact_no = contact_no;
	}
	public String getCredit_card_no() {
		return credit_card_no;
	}
	public void setCredit_card_no(String credit_card_no) {
		this.credit_card_no = credit_card_no;
	}
	public int getCvv() {
		return cvv;
	}
	public void setCvv(int cvv) {
		this.cvv = cvv;
	}
	public String getDelivery_date() {
		return delivery_date;
	}
	public void setDelivery_date(String delivery_date) {
		this.delivery_date = delivery_date;
	}
	public String getOrder_date() {
		return order_date;
	}
	public void setOrder_date(String order_date) {
		this.order_date = order_date;
	}
	
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}