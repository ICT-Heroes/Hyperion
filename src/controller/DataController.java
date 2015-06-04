package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import service.ClsxService;
import service.DsmService;
import model.Clsx;
import model.Data;
import model.Dsm;

public class DataController {
	public Data data;
	
	public void sort(String name){
		Data parent = findParent(name);
		Collections.sort(parent.getChild(), new NameDescCompare());
	}

	public void moveUp(String name) {
		Data parent = findParent(name);
		int index = 0;
		for (; index < parent.getChildLength(); index++) {
			if (parent.getChildData(index).isSameName(name)) {
				break;
			}
		}
		System.out.println(index);
		if (0 < index) {
			Data cur, forward;
			cur = parent.getChildData(index);
			forward = parent.getChildData(index - 1);
			parent.setChildData(index, forward);
			parent.setChildData(index - 1, cur);
		}
	}

	public void moveDown(String name) {
		Data parent = findParent(name);
		int index = 0;
		for (; index < parent.getChildLength(); index++) {
			if (parent.getChildData(index).isSameName(name)) {
				break;
			}
		}
		if (index < parent.getChildLength() - 1) {
			Data cur, back;
			cur = parent.getChildData(index);
			back = parent.getChildData(index + 1);
			parent.setChildData(index, back);
			parent.setChildData(index + 1, cur);
		}
	}

	private Data findParent(String name) {
		int dataIndex = data.findDataIndex(name);
		for (int i = 1; i <= dataIndex; i++) {
			Data findParentData = data.findData(data.getData(dataIndex - i).getName());
			if (!findParentData.findData(name).isSameName("null")) {
				return findParentData;
			}
		}
		return new Data();
	}

	/**
	 * data의 특정 노드 객체의 이름을 바꾸는 함수
	 */
	public void setName(String exName, String newName) {
		data.findData(exName).setName(newName);
	}

	public void setName(int dataIndex, String newName) {
		data.getData(dataIndex).setName(newName);
	}

	/*
	 * data 의 itemName 에 해당하는 Data객체의 depend 중 dependItemName 과 동일한 이름을 가진 Data
	 * 객체를 추가하거나 삭제함. 이미 의존성을 갖고 있으면 삭제 의존성이 없으면 추가 toggle
	 */
	public void setDependancy(String itemName, String dependItemName) {
		setDependancy(data, itemName, dependItemName);
	}

	public void setDependancy(Data data, String itemName, String dependItemName) {
		Data item, depItem;
		item = data.findItem(itemName);
		depItem = data.findItem(dependItemName);
		if (isDependent(data, itemName, dependItemName)) {
			item.removeDependencyData(depItem);
		} else {
			item.addDependencyData(depItem);
		}
	}

	/*
	 * data 의 itemName 이 dependItemName 에게 의존하고 있는가?
	 */
	public boolean isDependent(String itemName, String dependItemName) {
		return isDependent(data, itemName, dependItemName);
	}

	public boolean isDependent(Data data, String itemName, String dependItemName) {
		Data item, depItem;
		item = data.findItem(itemName);
		depItem = data.findItem(dependItemName);
		int length = item.getDependencyLength();
		for (int i = 0; i < length; i++) {
			if (item.getDependencyData(i).isSameName(depItem.getName())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * data 내의 아이템 중 추가하고싶은 자리의 이름을 두번째 인수로 적으면 그 자리에 newData 라는 이름으로 아이템을 추가한다.
	 */
	public void addItem(String itemName) {
		addItem(itemName, "newData");
	}

	public void addItem(String itemName, String newItemName) {
		addItem(data, itemName, newItemName);
	}

	public void addItem(Data data, String itemName) {
		addItem(data, itemName, "newData");
	}

	public void addItem(Data data, String itemName, String newItemName) {
		Data newData;
		int i = 0;
		while (!data.findData(newItemName + i).isSameName("null")) {
			i++;
			if (100000 < i) {
				System.out.println("아이템 추가 실패, 너무 많은 아이템이 이름 변경 없이 추가되려 하고있다.");
				return;
			}
		}
		if (i == 0) {
			newData = new Data(newItemName);
		} else {
			newData = new Data(newItemName + i);
		}
		if (0 < data.findData(itemName).getChildLength()) {
			data.findData(itemName).addChildData(newData);
		} else {
			Data parent = findParent(itemName);
			parent.addChildData(newData);
		}
	}

	/*
	 * data 내의 아이템을 지운다.
	 */
	public void deleteItem(String itemName) {
		deleteItem(data, itemName);
	}

	public void deleteItem(Data data, String itemName) {
		// 연결 지우기
		for (int i = 0; i < data.countItem(); i++) {
			int depLength = data.getItem(i).getDependencyLength();
			for (int j = 0; j < depLength; j++) {
				if (data.getItem(i).getDependencyData(j).isSameName(itemName)) {
					data.getItem(i).removeDependencyData(j);
				}
			}
		}
		// 직접적인 데이터 지우기
		Data parent = findParent(itemName);
		for (int i = 0; i < parent.getChildLength(); i++) {
			if (parent.getChildData(i).getChildLength() == 0) {
				if (parent.getChildData(i).isSameName(itemName)) {
					parent.removeChildData(i);
				}
			}
		}
	}

	/*
	 * 같은 부모를 가진 노드들 끼리만 결합할 수 있다. startName 은 그룹을 시작하는 노드, endName 은 그룹을 끝내는 노드
	 * startName, endName 모두 그룹 안에 들어간다.
	 */
	public void createGroup(String startName, String endName) {
		createGroup(data, startName, endName);
	}

	public void createGroup(String startName, String endName, String groupName) {
		createGroup(data, startName, endName, groupName);
	}

	public void createGroup(Data data, String startName, String endName) {
		createGroup(data, startName, endName, "newGroup");
	}

	public void createGroup(Data data, String startName, String endName,
			String groupName) {
		int index = 0;
		while (data.findData(groupName + index).getName() != "null") {
			index++;
			if (100000 < index) {
				System.out.println("그룹짓기 실패, 너무 많은 그룹이 비슷한 이름을 갖고 있다.");
				return;
			}
		}
		if (startName != endName) {
			if (findParent(startName).isSameName(
					findParent(endName).getName())) {
				int start, end;
				start = end = 0;
				Data parent = findParent(startName);
				for (int i = 0; i < parent.getChildLength(); i++) {
					if (parent.getChildData(i).isSameName(startName)) {
						start = i;
					}
					if (parent.getChildData(i).isSameName(endName)) {
						end = i;
					}
				}
				if (end < start) {
					int a = end;
					end = start;
					start = a;
				}
				Data newData;
				if (index == 0) {
					newData = new Data(groupName);
				} else {
					newData = new Data(groupName + index);
				}
				for (int i = 0; i < end - start + 1; i++) {
					newData.addChildData(parent.getChildData(start + i));
				}
				parent.setChildData(start, newData);
				for (int i = 0; i < end - start; i++) {
					parent.removeChildData(start + 1);
				}
			}
		}
	}

	/*
	 * 그룹풀기 그룹의 이름을 두번째 인자로 넣으면 그 그룹을 푼다.
	 */
	public void deleteGroup(String groupName) {
		Data parent = findParent(groupName);
		Data Group = data.findData(groupName);
		int index = parent.findChildIndex(groupName);
		int size = Group.getChildLength();
		for (int i = 0; i < size; i++) {
			parent.addChildData(index + 1, Group.getChildData(size - i - 1));
		}
		parent.removeChildData(index);
	}

	/*
	 * data 내의 GroupName 만을 따로 떼내서 새로 복제해 만든다. data 내의 dependancy 가 복잡하게 얽혀있는데,
	 * 단순히 일부분만 따로 떼내서 복제한다면 null dependancy 를 갖고 올 수도 있으므로 일부분을 제외한 다른 곳과의
	 * dependancy 는 무시하도록 복제한다.
	 */
	public Data duplicate(String GroupName) {
		return duplicate(GroupName);
	}

	public Data duplicate(Data data, String GroupName) {
		Data exData = data.findData(GroupName);
		Data newData = new Data(exData);

		int length = exData.countItem();

		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (isDependent(data, exData.getItem(i).getName(),
						exData.getItem(j).getName())) {
					setDependancy(newData, newData.getItem(i).getName(),
							newData.getItem(j).getName());
				}
			}
		}

		return newData;
	}

	/*
	 * File 을 받으면 Dsm 정보를 읽고 Data로 변환하여 Data 를 리턴
	 */
	public void loadDsm(File file) {
		DsmService dsmService = new DsmService();
		Dsm dsm = dsmService.readFromeFile(file);

		int nodeNumber = dsm.getNumber();
		data = new Data("root");

		// 노드 생성
		for (int i = 0; i < nodeNumber; i++) {
			data.addChildData(new Data(dsm.getName(i)));
		}

		// dependancy 연결
		for (int i = 0; i < nodeNumber; i++) {
			for (int j = 0; j < nodeNumber; j++) {
				if (dsm.getDependency(i, j)) {
					data.getChildData(i).addDependencyData(data.getChildData(j));
				}
			}
		}
	}

	public void loadClsx(File file) {
		makeClsxToData(new ClsxService().readFile(file));
	}

	public void loadClsx(Clsx c) {
		makeClsxToData(c);
	}

	/*
	 * 두 파일 정보가 꼭 dsm 을 로드하였거나 clsx 파일을 로드하지 않아도
	 * 동작한다. 적당히 조작된 Data 구조를 넣어줘도 동작한다.
	 * 
	 * Data 는 clsxData의 트리구조를 따라하며, dependancy 정보는 data에서 가져온다.
	 */
	public void loadDsmClsx(File clsxFile) {
		mergeData(makeClsxToData(new ClsxService().readFile(clsxFile)));
	}

	public void loadDsmClsx(Data clsxData) {
		mergeData(clsxData);
	}
	
	/**
	 * clsxData정보가 날라오면 기존 dsmData와 병합한다.
	 * @param clsxData
	 */
	private void mergeData(Data clsxData) {
		Data retData = new Data("root");
		if (checkSameData(clsxData)) {
			retData = new Data(clsxData);

			int length = retData.countItem();
			for (int i = 0; i < length; i++) {
				for(int j = 0 ; j < length ; j ++){
					if(isDependent(data,retData.getItem(i).getName(), retData.getItem(j).getName())){
						setDependancy(retData, retData.getItem(i).getName(), retData.getItem(j).getName());
					}
				}
			}

		} else {
			System.out.println(checkSameData(clsxData) + "");
			System.out.println("두 Data가 같은 .dsm을 사용하여 만들어지지 않았습니다");
			retData.setName("null");
		}
		this.data = retData;
	}

	/**
	 * Clsx를 Data 로 바꾸는 함수이다. Dsm 정보는 없고 Clsx 의 트리구조만 갖는 Data가 리턴된다 read 전용
	 */
	private Data makeClsxToData(Clsx c) {
		Data data = new Data(c.getName());		
		if (c.getItem() != null) {
			for (int i = 0; i < c.getItem().size(); i++) {
				data.addChildData(makeClsxToData(c.getItem().get(i)));
			}
		}
		return data;
	}

	/*
	 * 데이터를 clsx 로 바꾸는 함수 데이터의 Dsm 정보는 빼고 저장하여 리턴한다. write 전용
	 */

	public Clsx makeDataToClsx(Data data) {
		Clsx newClsx = new Clsx(data.getName());
		int length = data.getChildLength();
		newClsx.setItem(new ArrayList<>());
		for (int i = 0; i < length; i++) {
			newClsx.addItem(makeDataToClsx(data.getChildData(i)));
		}
		return newClsx;
	}

	/*
	 * Data 를 ArrayList<Dsm> 으로 바꾸는 함수 순서는 Data 의 item 순서 그대로 가져온다. write 전용
	 */

	public Dsm makeDataToDsm(Data data) {
		Dsm dsm = new Dsm(data.countItem());
		int length = data.countItem();
		
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
					dsm.setDependency(true, i, data.findItemIndex(data.getItem(i).getDependencyData(k).getName()));
				}
			}
		}
		return dsm;
	}

	/*
	 * Dsm데이터와 ClsxData 객체의 노드들의 이름들이 같은지를 조사. true면 서로 같은 dsm 데이터를 갖고 조작한 clsx 트리
	 */
	private boolean checkSameData(Data clsxData) {
		int nodeNumber = clsxData.countItem();
		int dataNumber = data.countItem();
		if (nodeNumber != dataNumber) {
			//System.out.println("number : " + nodeNumber + ", " + dataNumber);
			return false;
		}
		for (int i = 0; i < nodeNumber; i++) {
			boolean ret = false;
			for (int j = 0; j < dataNumber; j++){
				if (clsxData.getItem(i).isSameName(data.getItem(j).getName())){
					//System.out.println("name : " + i + ",  " + dsmData.GetItem(j).getName());
					ret = true;
				}
			}
			if (!ret){
				//System.out.println("name1 : " + clsxData.GetItem(i).getName() );
				//return false;
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
			return arg1.getName().compareTo(arg0.getName());
		} 
	}

	/*
	 * 생성자
	 */
	public DataController() {
		data = new Data("root");
	}

	public Data getRootData() {
		return data;
	}
}