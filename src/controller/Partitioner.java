package controller;

import java.util.ArrayList;
import java.util.Collections;

import model.Dsm;

public class Partitioner {
	// Original dsms
	private ArrayList<Dsm> dsms;
	// Result of partitioning
	private ArrayList<Dsm> processedDsms;
	// Array index
	int start, end;

	Dsm startDsm;
	private ArrayList<Dsm> completeLoop;
	private ArrayList<ArrayList<Dsm>> groups;

	public void setDsm(ArrayList<Dsm> dsm) {
		this.dsms = dsm;
	}

	public ArrayList<Dsm> getResultDsm() {
		return processedDsms;
	}

	public void preProcessing() {
		// Initialization
		start = 0;
		end = dsms.size();

		// This should be deepcopy.
		processedDsms = (ArrayList<Dsm>) dsms.clone();

		// No one uses this elements. This will be placed leftmost.
		for (int i = 0; i < dsms.size(); i++) {
			boolean used = false;
			for (Dsm dsm2 : dsms) {
				if (dsms.get(i) == dsm2)
					continue;
				for (Dsm dsm3 : dsm2.getDependents()) {
					if (dsm3 == dsms.get(i))
						used = true;
				}
			}

			if (!used) {
				Collections.swap(processedDsms, 0, i); // move to head of array
				start++;
			}
		}

		// This doesn't uses any elements. This will be placed rightmost.
		for (int i = start; i < dsms.size(); i++) {
			if (dsms.get(i).getDependents().size() == 0) {
				Dsm target = dsms.get(i);
				if (processedDsms.remove(target)) {
					processedDsms.add(target); // move to tail of array
					end--;
				}
			}
		}

	}

	private void recursiveSearch(Dsm dsm, boolean first) {
		// Complete loop is finished
		if (dsm == startDsm && !first) {
			System.out.println("A group1 " + completeLoop.size());
			for (Dsm dsm2 : completeLoop) {
				System.out.println("[" + dsm2.getIndex() + ":" + dsm2.getName() + "]");
			}
		}
		//
		else if (dsm.getDependents().size() > 0 && !completeLoop.contains(dsm)) {
			completeLoop.add(dsm);
			for (int i = 0; i < dsm.getDependents().size(); i++) {
				recursiveSearch(dsm.getDependent(i), false);
			}
		}
		//
		else if(!completeLoop.contains(dsm)) {
			completeLoop.add(dsm);
			System.out.println("A group2 " + completeLoop.size());
			for (Dsm dsm2 : completeLoop) {
				System.out.println("[" + dsm2.getIndex() + ":" + dsm2.getName() + "]");
			}
		}
	}

	public void partitionByPathSearching() {

		preProcessing();
		groups = new ArrayList<ArrayList<Dsm>>();

		// Path Searching Algorithm
		for (int i = start; i < end;) {
			startDsm = processedDsms.get(i);
			completeLoop = new ArrayList<Dsm>();
			// ArrayList<Dsm> dependent = dsms.get(i).getDependent();

			// Loop를 이루는 노드를 찾는다.
			recursiveSearch(startDsm, true);
			groups.add(completeLoop);
			end -= completeLoop.size();
			
			processedDsms.removeAll(completeLoop);
			for (Dsm dsm2 : completeLoop) {
				for (Dsm dsm : processedDsms) {
					if (!dsm.isDependent(dsm2.getIndex()))
							break;
					dsm.getDependents().remove(dsm2);
				}
			}


			/*
			 * // complete loop에 있는 것들을 left로 모은다. if (completeLoop.size() != 0)
			 * { // 가장 작은 인덱스를 찾는다. int index = processedDsms.size(); for (int j
			 * = 0; j < completeLoop.size(); j++) { int val =
			 * processedDsms.indexOf(completeLoop.get(j)); if (index > val)
			 * index = val; }
			 * 
			 * // ArrayList<Dsm> temp = (ArrayList<Dsm>)processedDsms.clone();
			 * // 인덱스부터 차례대로 completeLoop에 있는 것들을 모은다. for (int j = index; j <
			 * processedDsms.size(); j++) { if
			 * (completeLoop.contains(processedDsms.get(j))) {
			 * Collections.swap(processedDsms, index, j); index += 1; start +=
			 * 1; i++; } } }
			 * 
			 * // i를 증가시킨다.
			 */
			/*System.out.println("A group " + completeLoop.size());
			for (Dsm dsm : completeLoop) {
				System.out.println("[" + dsm.getIndex() + ":" + dsm.getName() + "]");
			}*/
		}
		
		for (int i = 0; i < groups.size(); i++) {
			for (int j = 0; j < groups.get(i).size(); j++) {
				//groups.get(i).get(j).print();
			}
		}

	}

	public void partitionByAdjacencyMatrix() {

	}

}
