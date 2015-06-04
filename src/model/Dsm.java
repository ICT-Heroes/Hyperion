package model;

import java.util.ArrayList;
import java.util.Collections;

public class Dsm {
	private int number;
	private ArrayList<String> names;
	private boolean[][] dep;

	public Dsm(int number) {
		this.number = number;
		dep = new boolean[number][number];
		names = new ArrayList<String>(number);
	}
	
	public String getName(int index) {
		return names.get(index);
	}
	
	public void addName(String name) {
		names.add(name);
	}
	
	public boolean getDependency(int a, int b) {
		if (a > number || b > number) {
			// Out of bound.
			// FIXME Error를 보여줘야함.
			return false;
		}
		else {
			return dep[a][b];
		}
	}
	
	public void setDependency(boolean val, int a, int b) {
		if (a > number || b > number) {
			// Out of bound.
		}
		else {
			dep[a][b] = val;
		}
	}

	public void removeDependency(int a, int b) {
		if (a > number || b > number) {
			// Out of bound.
		}
		else {
			dep[a][b] = !dep[a][b];	
		}
	}
	
	public void setName(int index, String name) {
		if (index > number) {
			// Out of bound.
		}
		else {
			this.names.set(index, name);			
		}
		
	}
	
	public int getNumber() {
		return number;
	}
	
	/**
	 * a와 b 노드의 순서를 바꾼다.
	 * @param a
	 * @param b
	 */
	public void changeOrder(int a, int b) {
		// (a,a)와 (b,b) 성분을 바꾼다.
		swapElement(a, a, b, b);
		
		// (a,b)와 (b,a) 성분을 바꾼다.
		swapElement(a, b, b, a);
		
		// 나머지 행과 열을 바꾼다.
		for (int i = 0; i < getNumber(); i++) {
			if (i == a || i == b) 
				continue;
			
			swapElement(a, i, b, i);
			
			swapElement(i, a, i, b);
		}
		
		Collections.swap(names, a, b);
	}
	
	/**
	 * (i1, j1)과 (i2, j2) 성분을 바꾼다.
	 * @param a1
	 * @param b1
	 * @param a2
	 * @param b2
	 */
	private void swapElement(int a1, int b1, int a2, int b2)
	{
		boolean temp = dep[a1][b1];
		dep[a1][b1] = dep[a2][b2];
		dep[a2][b2] = temp;
	}
	
	public void printDependencies() {
		for (int i = 0; i < number; i++) {
			for (int j = 0; j < number; j++) {
				if (dep[i][j])
					System.out.print("O ");
				else
					System.out.print("X ");	
			}
			System.out.println("");
		}
	}
	
	public void printNames() {
		for (String name : names) {
			System.out.println(name);
		}
	}
	
	public void print() {
		printDependencies();
		printNames();
	}
	
	
}
