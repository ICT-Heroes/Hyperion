package controller;

import java.io.File;

import model.Clsx;
import model.Data;
import model.Dsm;
import service.ClsxService;
import service.DsmService;

public class DataController {

	public Data data;

	public void Sort() {

	}

	public void MoveUp(String name) {
		MoveUp(data, name);
	}

	public void MoveUp(Data data, String name) {
		Data parent = FindParent(data, name);
		int index = 0;
		for (; index < parent.getChildLength(); index++) {
			if (parent.getChild(index).isSameName(name)) {
				break;
			}
		}

		if (0 < index) {
			Data cur, forward;
			cur = parent.getChild(index);
			forward = parent.getChild(index - 1);
			parent.setChild(index, forward);
			parent.setChild(index - 1, cur);
		}
	}

	public void MoveDown(String name) {
		MoveDown(data, name);
	}

	public void MoveDown(Data data, String name) {
		Data parent = FindParent(data, name);
		int index = 0;
		for (; index < parent.getChildLength(); index++) {
			if (parent.getChild(index).isSameName(name)) {
				break;
			}
		}
		if (index < parent.getChildLength() - 1) {
			Data cur, back;
			cur = parent.getChild(index);
			back = parent.getChild(index + 1);
			parent.setChild(index, back);
			parent.setChild(index + 1, cur);
		}
	}

	private Data FindParent(String name) {
		return FindParent(data, name);
	}

	private Data FindParent(Data data, String name) {
		int dataIndex = data.findDataIndex(name);
		for (int i = 1; i <= dataIndex; i++) {
			Data fdata = data.findData(data.getData(dataIndex - i).getName());
			if (!fdata.findData(name).isSameName("null")) {
				return fdata;
			}
		}
		return new Data();
	}

	/*
	 * data의 어느 노드 객체의 이름을 바꾸는 함수
	 */
	public void SetName(Data data, String exName, String newName) {
		data.findData(exName).setName(newName);
	}

	public void SetName(Data data, int dataIndex, String newName) {
		data.getData(dataIndex).setName(newName);
	}

	public void SetName(String exName, String newName) {
		data.findData(exName).setName(newName);
	}

	public void SetName(int dataIndex, String newName) {
		data.getData(dataIndex).setName(newName);
	}

	/*
	 * data 의 itemName 에 해당하는 Data객체의 depend 중 dependItemName 과 동일한 이름을 가진 Data
	 * 객체를 추가하거나 삭제함. 이미 의존성을 갖고 있으면 삭제 의존성이 없으면 추가 toggle
	 */
	public void SetDependancy(String itemName, String dependItemName) {
		SetDependancy(data, itemName, dependItemName);
	}

	public void SetDependancy(Data data, String itemName, String dependItemName) {
		Data item, depItem;
		item = data.findItem(itemName);
		depItem = data.findItem(dependItemName);
		if (isDepend(data, itemName, dependItemName)) {
			item.removeDepend(depItem);
		} else {
			item.addDepend(depItem);
		}
	}

	/*
	 * data 의 itemName 이 dependItemName 에게 의존하고 있는가?
	 */
	public boolean isDepend(String itemName, String dependItemName) {
		return isDepend(data, itemName, dependItemName);
	}

	public boolean isDepend(Data data, String itemName, String dependItemName) {
		Data item, depItem;
		item = data.findItem(itemName);
		depItem = data.findItem(dependItemName);
		int length = item.getDependLength();
		for (int i = 0; i < length; i++) {
			if (item.getDepend(i).isSameName(depItem.getName())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * data 내의 아이템 중 추가하고싶은 자리의 이름을 두번째 인수로 적으면 그 자리에 newData 라는 이름으로 아이템을 추가한다.
	 */
	public void AddItem(String itemName) {
		AddItem(itemName, "newData");
	}

	public void AddItem(String itemName, String newItemName) {
		AddItem(data, itemName, newItemName);
	}

	public void AddItem(Data data, String itemName) {
		AddItem(data, itemName, "newData");
	}

	public void AddItem(Data data, String itemName, String newItemName) {
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
			data.findData(itemName).addChild(newData);
		} else {
			Data parent = FindParent(data, itemName);
			parent.addChild(newData);
		}
	}

	/*
	 * data 내의 아이템을 지운다.
	 */
	public void DeleteItem(String itemName) {
		DeleteItem(data, itemName);
	}

	public void DeleteItem(Data data, String itemName) {
		// 연결 지우기
		for (int i = 0; i < data.countItem(); i++) {
			int depLength = data.getItem(i).getDependLength();
			for (int j = 0; j < depLength; j++) {
				if (data.getItem(i).getDepend(j).isSameName(itemName)) {
					data.getItem(i).removeDepend(j);
				}
			}
		}
		// 직접적인 데이터 지우기
		Data parent = FindParent(data, itemName);
		for (int i = 0; i < parent.getChildLength(); i++) {
			if (parent.getChild(i).getChildLength() == 0) {
				if (parent.getChild(i).isSameName(itemName)) {
					parent.removeChild(i);
				}
			}
		}
	}

	/*
	 * 같은 부모를 가진 노드들 끼리만 결합할 수 있다. startName 은 그룹을 시작하는 노드, endName 은 그룹을 끝내는 노드
	 * startName, endName 모두 그룹 안에 들어간다.
	 */
	public void CreateGroup(String startName, String endName) {
		CreateGroup(data, startName, endName);
	}

	public void CreateGroup(String startName, String endName, String groupName) {
		CreateGroup(data, startName, endName, groupName);
	}

	public void CreateGroup(Data data, String startName, String endName) {
		CreateGroup(data, startName, endName, "newGroup");
	}

	public void CreateGroup(Data data, String startName, String endName,
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
			if (FindParent(data, startName).isSameName(
					FindParent(data, endName).getName())) {
				int start, end;
				start = end = 0;
				Data parent = FindParent(data, startName);
				for (int i = 0; i < parent.getChildLength(); i++) {
					if (parent.getChild(i).isSameName(startName)) {
						start = i;
					}
					if (parent.getChild(i).isSameName(endName)) {
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
					newData.addChild(parent.getChild(start + i));
				}
				parent.setChild(start, newData);
				for (int i = 0; i < end - start; i++) {
					parent.removeChild(start + 1);
				}
			}
		}
	}

	/*
	 * 그룹풀기 그룹의 이름을 두번째 인자로 넣으면 그 그룹을 푼다.
	 */
	public void DeleteGroup(String groupName) {
		DeleteGroup(groupName);
	}

	public void DeleteGroup(Data data, String groupName) {
		Data parent = FindParent(data, groupName);
		Data Group = data.findData(groupName);
		int index = parent.findChildIndex(groupName);
		int size = Group.getChildLength();
		for (int i = 0; i < size; i++) {
			parent.addChild(index + 1, Group.getChild(size - i - 1));
		}
		parent.removeChild(index);
	}

	/*
	 * data 내의 GroupName 만을 따로 떼내서 새로 복제해 만든다. data 내의 dependancy 가 복잡하게 얽혀있는데,
	 * 단순히 일부분만 따로 떼내서 복제한다면 null dependancy 를 갖고 올 수도 있으므로 일부분을 제외한 다른 곳과의
	 * dependancy 는 무시하도록 복제한다.
	 */
	public Data Dupicate(String GroupName) {
		return Dupicate(GroupName);
	}

	public Data Dupicate(Data data, String GroupName) {
		Data exData = data.findData(GroupName);
		Data newData = new Data(exData);

		int length = exData.countItem();

		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (isDepend(data, exData.getItem(i).getName(),
						exData.getItem(j).getName())) {
					SetDependancy(newData, newData.getItem(i).getName(),
							newData.getItem(j).getName());
				}
			}
		}

		return newData;
	}

	/*
	 * File 을 받으면 Dsm 정보를 읽고 Data로 변환하여 Data 를 리턴
	 */
	public Data LoadDsm(File file) {
		Data dsmData;
		Dsm dsm = DsmService.getInstance().readFromeFile(file);

		int nodeNumber = dsm.getNumber();
		dsmData = new Data("root");

		// 노드 생성
		for (int i = 0; i < nodeNumber; i++) {
			dsmData.addChild(new Data(dsm.getName(i)));

		}

		// dependancy 연결
		for (int i = 0; i < nodeNumber; i++) {
			for (int j = 0; j < nodeNumber; j++) {
				if (dsm.getDependency(i, j)) {
					dsmData.getChild(i).addDepend(dsmData.getChild(j));
				}
			}
		}
		return dsmData;
	}

	public Data LoadClsx(File file) {
		return MakeClsxToData(ClsxService.getInstance().readFile(file));
	}

	public Data LoadClsx(Clsx c) {
		return MakeClsxToData(c);
	}

	/*
	 * 두 파일 정보를 다 갖고 있을 때 사용할 수 있다. 두 파일 정보가 꼭 dsm 을 로드하였거나 clsx 파일을 로드하지 않아도
	 * 동작한다. 적당히 조작된 Data 구조를 넣어줘도 동작한다.
	 * 
	 * 리턴되어 나오는 Data 는 두번째 인자의 트리구조를 따라하며, 첫번재 인자의 dependancy 정보를 가져온다.
	 */
	public Data LoadDsmClsx(File dsmFile, File clsxFile) {
		return SumData(LoadClsx(clsxFile), LoadDsm(dsmFile));
	}

	public Data LoadDsmClsx(Data dsmData, File clsxFile) {
		return SumData(LoadClsx(clsxFile), dsmData);
	}

	public Data LoadDsmClsx(File dsmFile, Data clsxData) {
		return SumData(clsxData, LoadDsm(dsmFile));
	}

	public Data LoadDsmClsx(Data dsmData, Data clsxData) {
		return SumData(clsxData, dsmData);
	}

	private Data SumData(Data clsxData, Data dsmData) {
		Data retData = new Data("root");
		if (CheckSameData(clsxData, dsmData)) {
			retData = new Data(clsxData);

			int length = retData.countItem();
			for (int i = 0; i < length; i++) {
				for (int j = 0; j < length; j++) {
					if (isDepend(dsmData, retData.getItem(i).getName(), retData
							.getItem(j).getName())) {
						SetDependancy(retData, retData.getItem(i).getName(),
								retData.getItem(j).getName());
					}
				}
			}

		} else {
			System.out.println(CheckSameData(clsxData, dsmData) + "");
			System.out.println("두 Data가 같은 .dsm을 사용하여 만들어지지 않았습니다");
			retData.setName("null");
		}
		return retData;
	}

	/*
	 * Clsx를 Data 로 바꾸는 함수 Dsm 정보는 없고 Clsx 의 트리구조만 갖는 Data가 리턴된다 read 전용
	 */
	private Data MakeClsxToData(Clsx c) {
		Data newData = new Data(c.getName());
		if (c.item != null) {
			for (int i = 0; i < c.item.length; i++) {
				newData.addChild(MakeClsxToData(c.item[i]));
			}
		}
		return newData;
	}

	/*
	 * 데이터를 clsx 로 바꾸는 함수 데이터의 Dsm 정보는 빼고 저장하여 리턴한다. write 전용
	 */
	private Clsx MakeDataToClsx() {
		return MakeDataToClsx(data);
	}

	private Clsx MakeDataToClsx(Data d) {
		Clsx newClsx = new Clsx(d.getName());
		int length = d.getChildLength();
		newClsx.item = new Clsx[length];
		for (int i = 0; i < length; i++) {
			newClsx.item[i] = MakeDataToClsx(d.getChild(i));
		}
		return newClsx;
	}

	/*
	 * Data 를 ArrayList<Dsm> 으로 바꾸는 함수 순서는 Data 의 item 순서 그대로 가져온다. write 전용
	 */
	private Dsm MakeDataToDsm() {
		return MakeDataToDsm(data);
	}

	public Dsm MakeDataToDsm(Data data) {
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
			int depLength = data.getItem(i).getDependLength();
			for (int j = 0; j < length; j++) {
				for (int k = 0; k < depLength; k++) {
					dsm.setDependency(
							true,
							i,
							data.findItemIndex(data.getItem(i).getDepend(k)
									.getName()));
				}
			}
		}
		return dsm;
	}

	/*
	 * 두 Data 객체의 노드들의 이름들이 같은지를 조사. true면 서로 같은 dsm 데이터를 갖고 조작한 clsx 트리
	 */
	private boolean CheckSameData(Data data) {
		return CheckSameData(data, this.data);
	}

	private boolean CheckSameData(Data clsxData, Data dsmData) {
		int nodeNumber = clsxData.countItem();
		int dataNumber = dsmData.countItem();
		if (nodeNumber != dataNumber) {
			// System.out.println("number : " + nodeNumber + ", " + dataNumber);
			return false;
		}
		for (int i = 0; i < nodeNumber; i++) {
			boolean ret = false;
			for (int j = 0; j < dataNumber; j++) {
				if (clsxData.getItem(i)
						.isSameName(dsmData.getItem(j).getName())) {
					// System.out.println("name : " + i + ",  " +
					// dsmData.GetItem(j).getName());
					ret = true;
				}
			}
			if (!ret) {
				// System.out.println("name1 : " + clsxData.GetItem(i).getName()
				// );
				// return false;
			}
		}
		return true;
	}

	/*
	 * 생성자
	 */
	public DataController() {
		data = new Data("root");
	}

	public Data GetRoot() {
		return data;
	}
}