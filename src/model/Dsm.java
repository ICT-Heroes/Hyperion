package model;

import java.util.LinkedList;

public class Dsm {
	LinkedList<DsmModel> models; // 자료의 추가, 수정을 빠르게 하기 위해 연결리스트로 함
	
	public Dsm() {
		models = new LinkedList<DsmModel>();
	}
	
	public void addModel(DsmModel model) {
		this.models.add(model);
	}
	
	public DsmModel getModel(int index) {
		return models.get(index);
	}
	
	public int getNumber() {
		return models.size();
	}
}
