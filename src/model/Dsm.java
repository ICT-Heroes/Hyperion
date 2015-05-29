package model;

import java.util.ArrayList;

public class Dsm {
	private int index;
	private String name;
	private ArrayList<Dsm> dependent;
	
	public Dsm() {
		
	}
	
	public Dsm(int index) {
		this(index, "");
	}
	
	public Dsm(int index, String name) {
		this.index = index;
		this.name = name;
		
		dependent = new ArrayList<Dsm>();
	}
	
	public void removeDependency(int index) {
		dependent.removeIf((Dsm model) -> {return model.index == index;});
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
	
	public ArrayList<Dsm> getDependents() {
		return dependent;
	}
	
	public Dsm getDependent(int index) {
		return dependent.get(index);
	}
	
	public void addModel(Dsm model) {
		this.dependent.add(model);
	}
	
	public boolean isDependent(int index) {
		boolean result = false;
		
		for (Dsm model : dependent) {
			if (model.index == index) {
				result = true;
				break;
			}
		}
		
		return result;
	}
	
	public void print() {
		System.out.println("[" + index + ":" + name + "]");
		
		for (Dsm dsm : dependent) {
			System.out.println(dsm.getIndex() + ":" + dsm.getName());
		}
	}

	@Override
	public boolean equals(Object obj) {
		Dsm dsm = (Dsm) obj;
		return (this.index == dsm.getIndex()) && (this.name == dsm.getName());
	}
	
	
}
