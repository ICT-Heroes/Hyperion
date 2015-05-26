package model;

import java.util.ArrayList;

public class DsmModel {
	int index;
	String name;
	ArrayList<DsmModel> dependent;
	
	public DsmModel() {
		
	}
	
	public DsmModel(int index) {
		this(index, "");
	}
	
	public DsmModel(int index, String name) {
		this.index = index;
		this.name = name;
		
		dependent = new ArrayList<DsmModel>();
	}
	
	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void addModel(DsmModel model) {
		this.dependent.add(model);
	}
	
	public boolean isDependent(int index) {
		boolean result = false;
		
		for (int i = 0; i < dependent.size(); i++) {
			if (dependent.get(i).index == index) {
				result = true;
				break;
			}
		}
		
		return result;
	}
}
