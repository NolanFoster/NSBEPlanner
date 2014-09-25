package me.nolanfoster.mm.client;

public class TaskDate {
	private String symbol;
	private String price;
	private String change;

	public TaskDate() {
	}

	public TaskDate(String symbol, String price2, String change2) {
		this.symbol = symbol;
		this.price = price2;
		this.change = change2;
	}

	public String getSymbol() {
		return this.symbol;
	}

	public String getPrice() {
		return this.price;
	}

	public String getChange() {
		return this.change;
	}

	
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setChange(String change) {
		this.change = change;
	}
}
