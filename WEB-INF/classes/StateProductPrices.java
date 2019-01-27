class StateProductPrices
{
	private String state_name;
	private double total_price;

	public StateProductPrices(String state, double price){
		this.state_name = state;
		this.total_price = price;
	}

	public String getState_name(){
		return state_name;
	}
	public double getTotal_price(){
		return total_price;
	}
}