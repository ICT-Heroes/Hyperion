package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clsx 모델 클래스 트리 구조를 위해 자기 자신을 게속 가지고 있는 구조로 만들었다. 각 clsx는 이름을 가지고 있고, 만약
 * group이라면 (하위 노드가 있다면) item을 하위 노드 갯수만큼 가지고 있다.
 */
public class Clsx {
	private String name;
	private List<Clsx> item;

	public Clsx() {
		item = new ArrayList<>();
	}

	public Clsx(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Clsx> getItem() {
		if (item == null) {
			return null;
		}
		return item;
	}

	public void setItem(List<Clsx> item) {
		this.item = item;
	}

	public void addItem(Clsx clsx) {
		item.add(clsx);
	}

}
