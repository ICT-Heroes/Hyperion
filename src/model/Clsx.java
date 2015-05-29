package model;

public class Clsx {
	private String name;
	public Clsx[] item;
	
	public Clsx(String name){
		this.name = name;
		item = null;
	}
	
	public Clsx(){
		this("");
	}
	
	public Clsx[] getItem() {
		return item;
	}

	public void setItem(Clsx[] item) {
		this.item = item;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
