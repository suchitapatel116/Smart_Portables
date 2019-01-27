import java.io.*;
import java.util.*;

public class Product
{
	private String id;
	private String name;
	private String price;
	private String image;
	private String manufacturer;
	private String condition;
	private String discount;
	private List<String> accessories;
	private String type;
	private String quantity;
	private String manufacturerRebate;
	
	Product()
	{}
	Product(String id, String name, String price, String image, String manufacturer, String condition, String discount, List<String> accessories)
	{
		this.id = id;
		this.name = name;
		this.price = price;
		this.image = image;
		this.manufacturer = manufacturer;
		this.condition = condition;
		this.discount = discount;
		this.accessories = accessories;
	}
	Product(String id, String name, String price, String image, String manufacturer, String condition, String discount, List<String> accessories,
		String type, String quantity, String manufacturerRebate)
	{
		this.id = id;
		this.name = name;
		this.price = price;
		this.image = image;
		this.manufacturer = manufacturer;
		this.condition = condition;
		this.discount = discount;
		this.accessories = accessories;
		this.type = type;
		this.quantity = quantity;
		this.manufacturerRebate = manufacturerRebate;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public List<String> getAccessories() {
		return accessories;
	}

	public void setAccessories(List<String> accessories) {
		this.accessories = accessories;
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", price=" + price + ", image=" + image + ", manufacturer=" + manufacturer
				+ ", condition=" + condition + ", discount=" + discount + "]";
	}

	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getManufacturerRebate() {
		return manufacturerRebate;
	}
	public void setManufacturerRebate(String manufacturerRebate) {
		this.manufacturerRebate = manufacturerRebate;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}