package model;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Design Structure Matrix(DSM) 노드들의 이름과 서로간의 dependency를 가지고 있는 모델
 * 
 * @author Hajin
 */
public class Dsm {
	private int number;
	private ArrayList<String> names;
	private boolean[][] dependency;

	public Dsm(int number) {
		this.number = number;
		dependency = new boolean[number][number];
		names = new ArrayList<String>(number);
	}

	public String getName(int index) {
		return names.get(index);
	}

	public ArrayList<String> getNames() {
		return this.names;
	}

	public void addName(String name) {
		names.add(name);
	}

	public boolean[][] getDependencyMatrix() {
		return dependency;
	}

	public boolean getDependency(int a, int b) {
		if (a > number || b > number) {
			// Out of bound.
			// FIXME Error를 보여줘야함.
			return false;
		} else {
			return dependency[a][b];
		}
	}

	public void setDependency(boolean val, int a, int b) {
		if (a > number || b > number) {
			// Out of bound.
		} else {
			dependency[a][b] = val;
		}
	}

	public void removeDependency(int a, int b) {
		if (a > number || b > number) {
			// Out of bound.
		} else {
			dependency[a][b] = !dependency[a][b];
		}
	}

	public void setName(int index, String name) {
		if (index > number) {
			// Out of bound.
		} else {
			this.names.set(index, name);
		}

	}

	public int getNumber() {
		return number;
	}

	@Override
	public boolean equals(Object obj) {
		Dsm target = (Dsm) obj;

		boolean result = true;

		if (result)
			if (this.number != target.getNumber())
				result = false;

		if (result)
			for (int i = 0; i < number; i++) {
				for (int j = 0; j < number; j++) {
					if (this.dependency[i][j] != target.getDependency(i, j)) {
						result = false;
						break;
					}
				}
			}

		if (result)
			for (int i = 0; i < number; i++) {
				if (!this.names.get(i).equals(target.getName(i))) {
					result = false;
					break;
				}
			}

		return result;
	}
}
