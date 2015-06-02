package controller;

import java.util.ArrayList;

import model.Dsm;

public class Partitioner {
	private Dsm dsm;
	private int head, faketail, fakehead, tail;
	private ArrayList<Integer> heads, tails;
	private ArrayList<Integer> aheads, atails;
	private ArrayList<Integer> sizeList;
	
	public void setDsm(Dsm dsm) {
		this.dsm = dsm;
	}
	
	public ArrayList<Integer> getSizeList() {
		return sizeList;
	}

	public void preProcessing() {
		head = 0;
		tail = dsm.getNumber();
		
		fakehead = head;
		faketail = tail;

		aheads = new ArrayList<Integer>();
		atails = new ArrayList<Integer>();
		sizeList = new ArrayList<Integer>();
		
		while(true) {
			heads = new ArrayList<Integer>();
			tails = new ArrayList<Integer>();
			
			// row가 0인 것들을 tail쪽으로 보냄
			for (int i = fakehead; i < tail; i++) {
				boolean result = true;
				for (int j = fakehead; j < tail; j++) {
					if (dsm.getDependency(i, j)) {
						result = false;
						continue;
					}
				}
				
				if (result) {
					tails.add(i);
				}
			}
			
			for (int i = 0; i < tails.size(); i++) {
				int row = tails.get(i);
				dsm.changeOrder(row, tail-1);
				tail -= 1;
			}
			
			// column이 0인 것들을 head쪽으로 보냄
			for (int i = head; i < faketail; i++) {
				boolean result = true;
				for (int j = head; j < faketail; j++) {
					if (dsm.getDependency(j, i)) {
						result = false;
						continue;
					}
				}
				
				if (result) {
					heads.add(i);
				}
			}
			
			for (int i = 0; i < heads.size(); i++) {
				int column = heads.get(i);
				dsm.changeOrder(column, head);
				head += 1;
			}
			
			// 범위 축소
			fakehead = head;
			faketail = tail;
			
			if (heads.size() == 0 && tails.size() == 0)
				break;
			
			aheads.add(heads.size());
			atails.add(tails.size());
		}
	}
	
	public void pathSearching() {
		
	}
	
	public void postProcessing() {
		int middleSize = dsm.getNumber();
		
		for (Integer val : aheads) {
			middleSize -= val;
		}
		for (Integer val : atails) {
			middleSize -= val;
		}
		
		sizeList.addAll(aheads);
		sizeList.add(middleSize);
		sizeList.addAll(atails);
	}
}
