package controller;

import java.util.ArrayList;

import model.Dsm;

public class Partitioner {
	private Dsm dsm;
	private int head, faketail, fakehead, tail;
	private ArrayList<Integer> heads, tails;
	private ArrayList<Integer> aheads, atails, abody;
	
	public void setDsm(Dsm dsm) {
		this.dsm = dsm;
	}
	
	public ArrayList<Integer> getSizeList() {
		return abody;
	}

    /**
     * Pre proccess for partitioning
     */

	public void preProcessing() {
		head = 0;
		tail = dsm.getNumber();
		
		fakehead = head;
		faketail = tail;

		aheads = new ArrayList<Integer>();
		atails = new ArrayList<Integer>();
		
		// column이 0인 것들을 tail쪽으로 보냄
		while (true) {
			tails = new ArrayList<Integer>();
			
			for (int i = fakehead; i < tail; i++) {
				boolean result = true;
				for (int j = fakehead; j < tail; j++) {
					if (dsm.getDependency(j, i)) {
						result = false;
						continue;
					}
				}
				
				if (result) {
					tails.add(i);
				}
			}
			
			for (int i = 0; i < tails.size(); i++) {
				int column = tails.get(i);
				dsm.changeOrder(column, tail-1);
				tail -= 1;
			}
			
			if (tails.size() == 0)
				break;
			
			atails.add(tails.size());
		}
		
		faketail = tail;

		// row가 0인 것들을 head쪽으로 보냄
		while (true) {
			heads = new ArrayList<Integer>();
			
			for (int i = head; i < faketail; i++) {
				boolean result = true;
				for (int j = head; j < faketail; j++) {
					if (dsm.getDependency(i, j)) {
						result = false;
						continue;
					}
				}
				
				if (result) {
					heads.add(i);
				}
			}
			
			for (int i = 0; i < heads.size(); i++) {
				int row = heads.get(i);
				dsm.changeOrder(row, head);
				head += 1;
			}
			
			if (heads.size() == 0)
				break;
			
			aheads.add(heads.size());
		}
		
		fakehead = head;
	}
	
    /**
     * Path Searching Algorithm for partitioning There is no empty row or column
     * between head and tail
     */

	public void pathSearching() {
		abody = new ArrayList<Integer>();
    	while (head < tail) {
            ArrayList<Integer> temp = new ArrayList<Integer>();

            temp.add(head);
            for (int i = head;;) {

                for (int j = head; j < tail; j++) {
                    if (dsm.getDependency(i, j)) {
                        i = j;
                        break;
                    }
                }

                if (temp.contains(i)) {
                    break;
                } else {
                    temp.add(i);
                }
            }

            for (Integer val : temp) {
                dsm.changeOrder(head, val);
                head++;
            }

            abody.add(temp.size());
    	}

	}
	
}
