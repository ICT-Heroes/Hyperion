package controller;

import java.util.ArrayList;
import java.util.Collections;

import model.Dsm;

/**
 * @author DSM을 읽고 Partitioning을 수행하는 클래스
 * 
 */
public class PartitionController {
	/**
	 * Partitioning에 사용될 dsm
	 */
	private Dsm dsm;
	/**
	 * 관심 영역의 첫 번째 인덱스 pre-processing을 수행한 후에는 body 영역의 첫 번째 인덱스
	 */
	private int head;
	/**
	 * 관심 영역의 마지막 인덱스 pre-processing을 수행한 후에는 body 영역의 마지막 인덱스+1
	 */
	private int tail;
	/**
	 * Dependency matrix의 처음부터 시작하여 head 영역 그룹들의 크기 배열
	 */
	private ArrayList<Integer> aheads;
	/**
	 * Dependency matrix의 마지막부터 시작하여 tail 영역 그룹들의 크기 배열
	 */
	private ArrayList<Integer> atails;
	/**
	 * Dependency matrix에서 head와 tail 영역을 제외한 나머지 부분 그룹들의 크기 배열
	 */
	private ArrayList<Integer> abody;

	/**
	 * @return 정렬된 Dependency matrix
	 */
	public boolean[][] getOrderedDependency() {
		return this.dsm.getDependencyMatrix();
	}

	/**
	 * Partitioning을 수행할 DSM 모델을 설정
	 * 
	 * @param dsm
	 *            파일을 로드한 Dsm 객체
	 */
	public void setDsm(Dsm dsm) {
		this.dsm = dsm;
	}

	/**
	 * @return 정렬된 Dependency matrix의 head 영역 그룹들의 크기 배열
	 */
	public ArrayList<Integer> getAheads() {
		return aheads;
	}

	/**
	 * @return 정렬된 Dependency matrix의 tail 영역 그룹들의 크기 배열
	 */
	public ArrayList<Integer> getAtails() {
		return atails;
	}

	/**
	 * @return 정렬된 Dependency matrix의 body 영역 그룹들의 크기 배열
	 */
	public ArrayList<Integer> getAbody() {
		return abody;
	}

	/**
	 * Partitioning을 수행하기 위한 전처리 과정. <br>
	 * head와 tail을 하나씩 증가, 감소 시키면서 dependency가 존재하지 않는 행, 열을 차례대로 위쪽, 왼쪽으로
	 * 이동시킨다.
	 */
	public void preProcessing() {
		head = 0;
		tail = dsm.getNumber();

		int fakehead = head;
		int faketail = tail;

		aheads = new ArrayList<Integer>();
		atails = new ArrayList<Integer>();

		// column이 0인 것들을 tail쪽으로 보냄
		while (true) {
			ArrayList<Integer> tails = new ArrayList<Integer>();

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
				changeOrder(column, tail - 1);
				tail -= 1;
			}

			if (tails.size() == 0)
				break;

			atails.add(tails.size());
		}

		faketail = tail;

		// row가 0인 것들을 head쪽으로 보냄
		while (true) {
			ArrayList<Integer> heads = new ArrayList<Integer>();

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
				changeOrder(row, head);
				head += 1;
			}

			if (heads.size() == 0)
				break;

			aheads.add(heads.size());
		}

		fakehead = head;
	}

	/**
	 * <b> Path Searching Algorithm </b><br>
	 * 이 메소드를 수행하기 전에 head와 tail사이에 dependency가 없는 행, 열은 존재하면 안된다.
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
				changeOrder(head, val);
				head++;
			}

			abody.add(temp.size());
		}

	}

	/**
	 * a와 b 노드의 순서를 바꾼다.
	 * 
	 * @param a
	 *            노드 a
	 * @param b
	 *            노드 b
	 */
	public void changeOrder(int a, int b) {
		// (a,a)와 (b,b) 성분을 바꾼다.
		swapElement(a, a, b, b);

		// (a,b)와 (b,a) 성분을 바꾼다.
		swapElement(a, b, b, a);

		// 나머지 행과 열을 바꾼다.
		for (int i = 0; i < dsm.getNumber(); i++) {
			if (i == a || i == b)
				continue;

			swapElement(a, i, b, i);

			swapElement(i, a, i, b);
		}

		Collections.swap(dsm.getNames(), a, b);
	}

	/**
	 * (a1, b1)과 (a2, b2) 성분을 바꾼다.
	 * 
	 * @param a1
	 *            첫 번째 노드의 행
	 * @param b1
	 *            첫 번째 노드의 열
	 * @param a2
	 *            두 번째 노드의 행
	 * @param b2
	 *            두 번째 노드의 열
	 */
	private void swapElement(int a1, int b1, int a2, int b2) {
		boolean val1 = dsm.getDependency(a1, b1);
		boolean val2 = dsm.getDependency(a2, b2);
		dsm.setDependency(val2, a1, b1);
		dsm.setDependency(val1, a2, b2);
	}

}
