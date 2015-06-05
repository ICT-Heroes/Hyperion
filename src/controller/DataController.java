package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import model.Clsx;
import model.Data;
import model.Dsm;
import service.ClsxService;
import service.DsmService;

public class DataController {
	public Data data;

	public void sort(int index) {
		Data newData = data.getData(index);
		Collections.sort(newData.getChildData(), new NameDescCompare());
	}

	public void moveUp(Data leafData) {
		moveUp(data.getDataIndex(leafData));
	}

	public void moveUp(int index) {
		Data parent = findParent(index);
		int i = 0;
		for (; i < parent.getChildLength(); i++) {
			if (parent.getChildData(i) == data.getData(index)) {
				break;
			}
		}
		if (0 < i) {
			Data cur, forward;
			cur = parent.getChildData(i);
			forward = parent.getChildData(i - 1);
			parent.setChildData(i, forward);
			parent.setChildData(i - 1, cur);
		}
	}

	public void moveDown(Data leafData) {
		moveDown(data.getDataIndex(leafData));
	}

	public void moveDown(int dataIndex) {
		Data parent = findParent(dataIndex);
		int i = 0;
		for (; i < parent.getChildLength(); i++) {
			if (parent.getChildData(i) == data.getData(dataIndex)) {
				break;
			}
		}
		if (i < parent.getChildLength() - 1) {
			Data cur, back;
			cur = parent.getChildData(i);
			back = parent.getChildData(i + 1);
			parent.setChildData(i, back);
			parent.setChildData(i + 1, cur);
		}
	}

	private Data findParent(int dataIndex) {
		Data indexData = data.getData(dataIndex);
		for (int i = 1; i <= data.countData(); i++) {
			Data parentData = data.getData(dataIndex - i);
			for (int j = 0; j < parentData.getChildLength(); j++) {
				if (parentData.getChildData(j) == indexData) {
					return parentData;
				}
			}
		}
		return new Data();
	}

	/**
	 * data의 어느 노드 객체의 이름을 바꾸는 함수
	 */
	public void changeNodeName(int dataIndex, String newName) {
		data.getData(dataIndex).setName(newName);
	}

	/**
	 * data 의 itemName 에 해당하는 Data객체의 depend 중 dependItemName 과 동일한 이름을 가진 Data
	 * 객체를 추가하거나 삭제함. 이미 의존성을 갖고 있으면 삭제 의존성이 없으면 추가 toggle
	 */
	private void setDependency(int dataIndex, int dependDataIndex) {
		Data item, depItem;
		item = data.getData(dataIndex);
		depItem = data.getData(dependDataIndex);
		if (isDependent(dataIndex, dependDataIndex)) {
			item.removeDependencyData(depItem);
		} else {
			item.addDependencyData(depItem);
		}
	}

	private void setDependency(int dataIndex, int dependDataIndex,
			boolean depend) {
		Data item, depItem;
		item = data.getData(dataIndex);
		depItem = data.getData(dependDataIndex);
		if (depend) {
			if (!isDependent(dataIndex, dependDataIndex)) {
				item.addDependencyData(depItem);
			}
		} else {
			if (isDependent(dataIndex, dependDataIndex)) {
				item.removeDependencyData(depItem);
			}
		}
	}

	/**
	 * data 의 itemName 이 dependItemName 에게 의존하고 있는가?
	 */

	private boolean isDependent(int dataIndex, int dependDataIndex) {
		Data item, depItem;
		item = data.getData(dataIndex);
		depItem = data.getData(dependDataIndex);
		int length = item.getDependencyLength();
		for (int i = 0; i < length; i++) {
			if (item.getDependencyData(i).isSameName(depItem.getName())) {
				return true;
			}
		}
		return false;
	}

	private boolean isDependforItem(int itemIndex, int dependitemIndex) {
		Data item, depItem;
		item = data.getItem(itemIndex);
		depItem = data.getItem(dependitemIndex);
		int length = item.getDependencyLength();
		for (int i = 0; i < length; i++) {
			if (item.getDependencyData(i).isSameName(depItem.getName())) {
				return true;
			}
		}
		return false;
	}

	private boolean isDependChilds(int dataIndex, int dependDataIndex) {
		Data data1 = data.getData(dataIndex);
		Data data2 = data.getData(dependDataIndex);
		if (data1.getChildLength() == 0) {
			if (data2.getChildLength() == 0) {
				if (isDependent(dataIndex, dependDataIndex)) {
					return true;
				}
			} else {
				for (int j = 0; j < data2.getItemCount(); j++) {
					if (isDependent(dataIndex,
							data.getDataIndex(data2.getItem(j)))) {
						return true;
					}
				}
			}
		} else {
			for (int i = 0; i < data1.getItemCount(); i++) {
				if (data2.getChildLength() == 0) {
					if (isDependent(data.getDataIndex(data1.getItem(i)),
							dependDataIndex)) {
						return true;
					}
				} else {
					for (int j = 0; j < data2.getItemCount(); j++) {
						if (isDependent(data.getDataIndex(data1.getItem(i)),
								data.getDataIndex(data2.getItem(j)))) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * data 내의 아이템 중 추가하고싶은 자리의 이름을 두번째 인수로 적으면 그 자리에 newData 라는 이름으로 아이템을 추가한다.
	 */

	public void addItem(int dataIndex, String newItemName) {
		Data newData = new Data(newItemName);
		if (0 < data.getData(dataIndex).getChildLength()) {
			data.getData(dataIndex).addChildData(newData);
		} else {
			Data parent = findParent(dataIndex);
			parent.addChildData(newData);
		}
	}

	/**
	 * DSM만 Load한 뒤 추가할 수 있는기 기능 구현
	 * 
	 * @param newDsmName
	 */
	public void addDsm(String newDsmName) {
		Data newData = new Data(newDsmName);
		data.addChildData(newData);
	}

	/**
	 * 데이터 내의 아이템을 지운다
	 * 
	 * @param dataIndex
	 *            지우고자 하는 데이터의 인덱스
	 */
	public void deleteItem(int dataIndex) {
		// 연결 지우기
		for (int i = 0; i < data.getItemCount(); i++) {
			int depLength = data.getItem(i).getDependencyLength();
			for (int j = 0; j < depLength; j++) {
				if (data.getItem(i).getDependencyData(j) == data
						.getData(dataIndex)) {
					data.getItem(i).removeDependencyData(j);
					depLength--;
				}
			}
		}
		// 직접적인 데이터 지우기
		Data parent = findParent(dataIndex);
		for (int i = 0; i < parent.getChildLength(); i++) {
			if (parent.getChildData(i).getChildLength() == 0) {
				if (parent.getChildData(i) == data.getData(dataIndex)) {
					parent.removeChildData(i);
				}
			}
		}
	}

	public void deleteDsm(int dataIndex) {
		data.removeChildData(dataIndex - 1);
	}

	/**
	 * 같은 부모를 가진 노드들 끼리만 결합할 수 있다. 연속성이 있는 아이템끼리 그룹을 짓는 함수 start, end 시작부터 끝까지
	 * 모두 그룹 안에 들어간다.
	 * 
	 * @param startIndex
	 *            그룹핑을 시작하는 인덱스
	 * @param endIndex
	 *            그룹핑을 끝내는 인덱스
	 * @param groupName
	 *            그룹을 짓고자 하는 이름
	 */
	public void createGroup(int startIndex, int endIndex, String groupName) {
		Data start = data.getData(startIndex);
		Data end = data.getData(endIndex);
		if (start != end) {
			if (findParent(startIndex) == findParent(endIndex)) {
				int startCount, endCount;
				startCount = endCount = 0;
				Data parent = findParent(startIndex);
				for (int i = 0; i < parent.getChildLength(); i++) {
					if (parent.getChildData(i) == data.getData(startIndex)) {
						startCount = i;
					}
					if (parent.getChildData(i) == data.getData(endIndex)) {
						endCount = i;
					}
				}
				if (endCount < startCount) {
					int a = endCount;
					endCount = startCount;
					startCount = a;
				}
				Data newData = new Data(groupName);
				for (int i = 0; i < endCount - startCount + 1; i++) {
					newData.addChildData(parent.getChildData(startCount + i));
				}
				parent.setChildData(startCount, newData);
				for (int i = 0; i < endCount - startCount; i++) {
					parent.removeChildData(startCount + 1);
				}
			}
		}
	}

	/**
	 * 같은 부모를 가진 노드들 끼리만 결합할 수 있다. 연속성이 없어도 되는 함수
	 * 
	 * @param dataIndex
	 *            그룹핑 하기 위해 지정된 인덱스들
	 * @param groupName
	 *            그룹을 짓고싶은 이름
	 */
	public void createGroup(int[] dataIndex, String groupName) {
		Data parent = findParent(dataIndex[0]);
		boolean sameMother = true;
		for (int i = 0; i < dataIndex.length; i++) {
			if (findParent(dataIndex[i]) != parent) {
				sameMother = false;
				return;
			}
		}
		Data newData = new Data(groupName);
		for (int i = 0; i < dataIndex.length; i++) {
			newData.addChildData(data.getData(dataIndex[i]));
		}
		for (int i = 1; i < dataIndex.length; i++) {
			parent.removeChildData(newData.getChildData(i));
		}
		parent.setChildData(dataIndex[0] - 1, newData);
	}

	/**
	 * 그룹풀기 그룹의 이름을 두번째 인자로 넣으면 그 그룹을 푼다.
	 */
	public void deleteGroup(int groupIndex) {
		Data parent = findParent(groupIndex);
		Data Group = data.getData(groupIndex);
		int index = parent.getChildIndex(data.getData(groupIndex).getName());
		int size = Group.getChildLength();
		for (int i = 0; i < size; i++) {
			parent.addChildData(index + 1, Group.getChildData(size - i - 1));
		}
		parent.removeChildData(index);
	}

	/**
	 * data 내의 GroupName 만을 따로 떼내서 새로 복제해 만든다. data 내의 dependancy 가 복잡하게 얽혀있는데,
	 * 단순히 일부분만 따로 떼내서 복제한다면 null dependancy 를 갖고 올 수도 있으므로 일부분을 제외한 다른 곳과의
	 * dependancy 는 무시하도록 복제한다.
	 */
	public Data duplicate(int DataIndex) {
		Data exData = data.getData(DataIndex);
		Data newData = new Data("root");
		newData.addChildData(exData);
		if (exData.getChildLength() > 0) {
			int length = exData.getItemCount();
			if (length > 0) {
				for (int i = 0; i < length; i++) {
					for (int j = 0; j < length; j++) {
						if (isDependent(data.getDataIndex(exData.getItem(i)),
								data.getDataIndex(exData.getItem(j)))) {
							setDependency(
									newData.getDataIndex(newData.getItem(i)),
									newData.getDataIndex(newData.getItem(j)));
						}
					}
				}
			}
		}
		return newData;
	}

	/**
	 * File 을 받으면 Dsm 정보를 읽고 Data로 변환하여 Data 를 리턴
	 * 
	 * @return
	 */
	public Data loadDsm(File file) {
		Data data = new Data("root");
		Dsm dsm = DsmService.getInstance().readFromeFile(file);

		int nodeNumber = dsm.getNumber();

		// 노드 생성
		for (int i = 0; i < nodeNumber; i++) {
			data.addChildData(new Data(dsm.getName(i)));
		}

		// dependancy 연결
		for (int i = 0; i < nodeNumber; i++) {
			for (int j = 0; j < nodeNumber; j++) {
				if (dsm.getDependency(i, j)) {
					data.getChildData(i)
							.addDependencyData(data.getChildData(j));
				}
			}
		}
		this.data = data;
		return data;

	}

	public Data loadClsx(File file) {
		return makeClsxToData(new ClsxService().readFile(file));
	}

	public Data loadClsx(Clsx c) {
		return makeClsxToData(c);
	}

	/**
	 * 두 파일 정보를 다 갖고 있을 때 사용할 수 있다. 두 파일 정보가 꼭 dsm 을 로드하였거나 clsx 파일을 로드하지 않아도
	 * 동작한다. 적당히 조작된 Data 구조를 넣어줘도 동작한다.
	 * 
	 * Data 는 clsxData의 트리구조를 따라하며, dependancy 정보는 data에서 가져온다.
	 */
	public Data loadDsmClsx(File dsmFile, File clsxFile) {
		return sumData(loadClsx(clsxFile), loadDsm(dsmFile));
	}

	private Data sumData(Data clsxData, Data dsmData) {
		Data retData = new Data("root");
		if (checkSameData(clsxData, dsmData)) {
			retData = new Data(clsxData);

			int length = retData.getItemCount();
			for (int i = 0; i < length; i++) {
				for (int j = 0; j < length; j++) {
					if (isDependforItem(
							dsmData.getItemIndex(retData.getItem(i).getName()),
							dsmData.getItemIndex(retData.getItem(j).getName()))) {
						setDependency(retData.getDataIndex(retData.getItem(i)),
								retData.getDataIndex(retData.getItem(j)), true);
					}
				}
			}

		} else {
			retData.setName("null");
		}
		this.data = retData;
		return retData;
	}

	/**
	 * Clsx를 Data 로 바꾸는 함수 Dsm 정보는 없고 Clsx 의 트리구조만 갖는 Data가 리턴된다 read 전용
	 */
	public Data makeClsxToData(Clsx clsx) {
		Data data = new Data();
		data.setName(clsx.getName());
		if (clsx.getItem() != null) {
			for (int i = 0; i < clsx.getItem().size(); i++) {
				data.addChildData(makeClsxToData(clsx.getItem().get(i)));
			}
		}
		return data;
	}

	/**
	 * Clsx을 파일로 저장
	 * 
	 * @param filePath
	 * @param data
	 */
	public void saveClsx(String filePath, Data data) {
		ClsxService.getInstance().WriteFile(filePath, makeDataToClsx(data));
	}

	/**
	 * 데이터를 clsx 로 바꾸는 함수 데이터의 Dsm 정보는 빼고 저장하여 리턴한다. write 전용
	 */
	private Clsx makeDataToClsx(Data data) {
		Clsx clsx = new Clsx();
		clsx.setName(data.getName());
		if (data.getChildLength() != 0) {
			clsx.setItem(new ArrayList<>());
			for (int i = 0; i < data.getChildLength(); i++) {
				clsx.addItem(makeDataToClsx(data.getChildData(i)));
			}
		}
		return clsx;
	}

	/**
	 * Data 를 ArrayList<Dsm> 으로 바꾸는 함수 순서는 Data 의 item 순서 그대로 가져온다. write 전용
	 */

	public Dsm makeDataToDsm(Data data) {
		Dsm dsm = new Dsm(data.getItemCount());
		int length = data.getItemCount();

		for (int i = 0; i < length; i++)
			dsm.addName(data.getItem(i).getName());

		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				dsm.setDependency(false, i, j);
			}
		}

		for (int i = 0; i < length; i++) {
			int depLength = data.getItem(i).getDependencyLength();
			for (int j = 0; j < length; j++) {
				for (int k = 0; k < depLength; k++) {
					dsm.setDependency(
							true,
							i,
							data.getItemIndex(data.getItem(i)
									.getDependencyData(k).getName()));
				}
			}
		}
		return dsm;
	}

	/**
	 * 두 Data 객체의 노드들의 이름들이 같은지를 조사. true면 서로 같은 dsm 데이터를 갖고 조작한 clsx 트리
	 */
	private boolean checkSameData(Data clsxData, Data dsmData) {
		int nodeNumber = clsxData.getItemCount();
		int dataNumber = dsmData.getItemCount();
		if (nodeNumber != dataNumber) {
			// System.out.println("number : " + nodeNumber + ", " + dataNumber);
			return false;
		}
		for (int i = 0; i < nodeNumber; i++) {
			boolean ret = false;
			for (int j = 0; j < dataNumber; j++) {
				if (clsxData.getItem(i).getName()
						.equals(dsmData.getItem(j).getName())) {
					// System.out.println("name : " + i + ",  " +
					// dsmData.GetItem(j).name);
					ret = true;
				}
			}
			if (!ret) {
				// System.out.println("name1 : " + clsxData.GetItem(i).name );
				// return false;
			}
		}
		return true;
	}

	static class NameDescCompare implements Comparator<Data> {
		/**
		 * 내림차순(DESC)
		 */
		@Override
		public int compare(Data arg0, Data arg1) {
			return arg0.getName().compareTo(arg1.getName());
		}
	}

	/**
	 * 
	 * @param data
	 * @param visualIndexes
	 *            : 현재 폴더가 열려서 보이는 트리의 번호들을 입력받는다.
	 * @return 서로의 dependancy 를 이차원배열로 리턴한다.
	 */

	public boolean[][] getDependArray(int[] visualIndexes) {
		System.out.println("start");
		int count = data.getItemCount();
		boolean[] drawIndex = new boolean[visualIndexes.length];
		for (int i = 0; i < visualIndexes.length; i++) {
			Data local = data.getData(visualIndexes[i]);
			drawIndex[i] = false;
			for (int j = 0; j < local.getChildLength(); j++) {
				for (int k = i; k < visualIndexes.length; k++) {
					if (local.getChildData(j) == data.getData(visualIndexes[k])) {
						drawIndex[i] = true;
						break;
					}
				}
			}
			if (!drawIndex[i]) {
				count -= (local.getItemCount() - 1);
			}
		}
		boolean[][] ret = new boolean[count][count];
		int locali = 0;
		int localj = 0;
		for (int i = 0; i < count; i++) {
			for (; locali < visualIndexes.length; locali++) {
				if (!drawIndex[i + locali])
					break;
			}

			for (int j = 0; j < count; j++) {
				for (; localj < visualIndexes.length; localj++) {
					if (!drawIndex[j + localj])
						break;
				}
				if (i == j) {
					ret[i][j] = false;
				} else {
					ret[i][j] = isDependChilds(visualIndexes[i + locali],
							visualIndexes[j + localj]);
				}
			}
		}
		return ret;
	}

	public void setName(int index, String name) {
		Data findData = data.getData(index);
		findData.setName(name);
	}

	/**
	 * 생성자
	 */
	public DataController() {
		data = new Data("root");
	}

	public Data getRootData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}
}