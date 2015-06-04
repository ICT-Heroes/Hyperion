package controller;

import java.io.File;
import java.util.ArrayList;

import service.ClsxService;
import service.DsmService;
import model.Clsx;
import model.Data;
import model.Dsm;

public class DataController {

	public Data data;
	
	public void sort(){
		
	}

	public void moveUp(Data data, Data leafData) {
		moveUp(data, data.getDataIndex(leafData));
	}
	
	public void moveUp(Data data, int index) {
		Data parent = findParent(data, index);
		int i = 0;
		for (; i < parent.getChildLength(); i++) {
			if (parent.getChild(i) == data.getData(index)) {
				System.out.println("" + data.getData(index).getName());
				break;
			}
		}
		if (0 < i) {
			Data cur, forward;
			cur = parent.getChild(i);
			forward = parent.getChild(i - 1);
			parent.setChild(i, forward);
			parent.setChild(i - 1, cur);
		}
	}
	
	public void moveDown(Data data, Data leafData) {
		moveDown(data, data.getDataIndex(leafData));
	}
	
	public void moveDown(Data data, int dataIndex) {
		Data parent = findParent(data, dataIndex);
		int i = 0;
		for (; i < parent.getChildLength(); i++) {
			if (parent.getChild(i) == data.getData(dataIndex)) {
				break;
			}
		}
		if (i < parent.getChildLength() - 1) {
			Data cur, back;
			cur = parent.getChild(i);
			back = parent.getChild(i + 1);
			parent.setChild(i, back);
			parent.setChild(i + 1, cur);
		}
	}
	
	private Data findParent(Data data, int index) {
		Data indexData = data.getData(index);
		for (int i = 1; i <= data.getDataCount(); i++) {
			Data fdata = data.getData(index - i);
			for(int j = 0 ; j <fdata.getChildLength() ; j++){
				if (fdata.getChild(j) == indexData) {
					return fdata;
				}
			}
		}
		return new Data();
	}

	/**
	 * data의 어느 노드 객체의 이름을 바꾸는 함수
	 */
	public void setName(Data data, int dataIndex, String newName) {
		data.getData(dataIndex).setName(newName);
	}
	/**
	 * data 의 itemName 에 해당하는 Data객체의 depend 중 dependItemName 과 동일한 이름을 가진 Data
	 * 객체를 추가하거나 삭제함. 이미 의존성을 갖고 있으면 삭제 의존성이 없으면 추가 toggle
	 */
	public void setDependancy(Data data, int dataIndex, int dependDataIndex) {
		Data item, depItem;
		item = data.getData(dataIndex);
		depItem = data.getData(dependDataIndex);
		if (isDepend(data, dataIndex, dependDataIndex)) {
			item.removeDepend(depItem);
		} else {
			item.addDepend(depItem);
		}
	}
	
	public void setDependancy(Data data, int dataIndex, int dependDataIndex, boolean depend) {
		Data item, depItem;
		item = data.getData(dataIndex);
		depItem = data.getData(dependDataIndex);
		if(depend){
			if (!isDepend(data, dataIndex, dependDataIndex)) {
				item.addDepend(depItem);
			}
		}else{
			if (isDepend(data, dataIndex, dependDataIndex)) {
				item.removeDepend(depItem);
			}
		}
	}

	/**
	 * data 의 itemName 이 dependItemName 에게 의존하고 있는가?
	 */
	
	public boolean isDepend(Data data, int dataIndex, int dependDataIndex) {
		Data item, depItem;
		item = data.getData(dataIndex);
		depItem = data.getData(dependDataIndex);
		int length = item.getDependLength();
		for (int i = 0; i < length; i++) {
			if (item.getDepend(i) == depItem) {
				System.out.println("isDepend : " + item.getName() + "  to  " + depItem.getName());
				return true;
			}
		}
		return false;
	}
	
	private boolean isDependforItem(Data data, int itemIndex, int dependitemIndex) {
		Data item, depItem;
		item = data.getItem(itemIndex);
		depItem = data.getItem(dependitemIndex);
		int length = item.getDependLength();
		for (int i = 0; i < length; i++) {
			if (item.getDepend(i) == depItem) {
				//System.out.println("isDepend : " + item.getName() + "  to  " + depItem.getName());
				return true;
			}
		}
		return false;
	}
	
	private boolean isDependChilds(Data data, int dataIndex, int dependDataIndex) {
		Data data1 = data.getData(dataIndex);
		Data data2 = data.getData(dependDataIndex);
		for(int i = 0 ; i < data1.getItemCount()  ; i ++){
			for(int j = 0 ; j < data2.getItemCount()  ; j ++){
				if(isDepend(data, data.getDataIndex(data1.getItem(i)), data.getDataIndex(data2.getItem(j)))){
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * data 내의 아이템 중 추가하고싶은 자리의 이름을 두번째 인수로 적으면 그 자리에 newData 라는 이름으로 아이템을 추가한다.
	 */
	
	public void addItem(Data data, int dataIndex, String newItemName) {
		Data newData = new Data(newItemName);
		if (0 < data.getData(dataIndex).getChildLength()) {
			data.getData(dataIndex).addChild(newData);
		} else {
			Data parent = findParent(data, dataIndex);
			parent.addChild(newData);
		}
	}

	/**
	 * data 내의 아이템을 지운다.
	 */
	public void deleteItem(Data data, int dataIndex) {
		// 연결 지우기
		for (int i = 0; i < data.getItemCount(); i++) {
			int depLength = data.getItem(i).getDependLength();
			for (int j = 0; j < depLength; j++) {
				if (data.getItem(i).getDepend(j) == data.getData(dataIndex)) {
					data.getItem(i).removeDepend(j);
					depLength--;
				}
			}
		}
		// 직접적인 데이터 지우기
		Data parent = findParent(data, dataIndex);
		for (int i = 0; i < parent.getChildLength(); i++) {
			if (parent.getChild(i).getChildLength() == 0) {
				if (parent.getChild(i) == data.getData(dataIndex)) {
					parent.removeChild(i);
				}
			}
		}
	}

	/**
	 * 같은 부모를 가진 노드들 끼리만 결합할 수 있다. startName 은 그룹을 시작하는 노드, endName 은 그룹을 끝내는 노드
	 * startName, endName 모두 그룹 안에 들어간다.
	 */
	public void createGroup(Data data, int startIndex, int endIndex,
			String groupName) {
		Data start = data.getData(startIndex);
		Data end = data.getData(endIndex);
		if (start != end) {
			if (findParent(data, startIndex) ==	findParent(data, endIndex)) {
				int startCount, endCount;
				startCount = endCount = 0;
				Data parent = findParent(data, startIndex);
				for (int i = 0; i < parent.getChildLength(); i++) {
					if (parent.getChild(i) == data.getData(startIndex)) {
						startCount = i;
					}
					if (parent.getChild(i) == data.getData(endIndex)) {
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
					newData.addChild(parent.getChild(startCount + i));
				}
				parent.setChild(startCount, newData);
				for (int i = 0; i < endCount - startCount; i++) {
					parent.removeChild(startCount + 1);
				}
			}
		}
	}

	/**
	 * 그룹풀기 그룹의 이름을 두번째 인자로 넣으면 그 그룹을 푼다.
	 */	
	public void deleteGroup(Data data, int groupIndex) {
		Data parent = findParent(data, groupIndex);
		Data Group = data.getData(groupIndex);
		int index = parent.getChildIndex(data.getData(groupIndex).getName());
		int size = Group.getChildLength();
		for (int i = 0; i < size; i++) {
			parent.addChild(index + 1, Group.getChild(size - i - 1));
		}
		parent.removeChild(index);
	}
	
	

	/**
	 * data 내의 GroupName 만을 따로 떼내서 새로 복제해 만든다. data 내의 dependancy 가 복잡하게 얽혀있는데,
	 * 단순히 일부분만 따로 떼내서 복제한다면 null dependancy 를 갖고 올 수도 있으므로 일부분을 제외한 다른 곳과의
	 * dependancy 는 무시하도록 복제한다.
	 */
	public Data duplicate(Data data, int DataIndex) {
		Data exData = data.getData(DataIndex);
		Data newData = new Data(exData);

		int length = exData.getItemCount();

		for (int i = 0; i < length; i++) {
			for (int j = 0; j < length; j++) {
				if (isDepend(data, data.getDataIndex(exData.getItem(i)), data.getDataIndex(exData.getItem(j)))) {
					setDependancy(newData, newData.getDataIndex(newData.getItem(i)),newData.getDataIndex(newData.getItem(j)));
				}
			}
		}

		return newData;
	}

	/**
	 * File 을 받으면 Dsm 정보를 읽고 Data로 변환하여 Data 를 리턴
	 */
	public Data loadDsm(File file) {
		Data dsmData;
		DsmService dsmService = new DsmService();
		Dsm dsm = dsmService.readFromeFile(file);

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
	 * 리턴되어 나오는 Data 는 두번째 인자의 트리구조를 따라하며, 첫번재 인자의 dependancy 정보를 가져온다.
	 */
	public Data loadDsmClsx(File dsmFile, File clsxFile) {
		return sumData(loadClsx(clsxFile), loadDsm(dsmFile));
	}

	public Data loadDsmClsx(Data dsmData, File clsxFile) {
		return sumData(loadClsx(clsxFile), dsmData);
	}

	public Data loadDsmClsx(File dsmFile, Data clsxData) {
		return sumData(clsxData, loadDsm(dsmFile));
	}

	public Data loadDsmClsx(Data dsmData, Data clsxData) {
		return sumData(clsxData, dsmData);
	}

	private Data sumData(Data clsxData, Data dsmData) {
		Data retData = new Data("root");
		if (checkSameData(clsxData, dsmData)) {
			retData = new Data(clsxData);

			int length = retData.getItemCount();
			for (int i = 0; i < length; i++) {
				for(int j = 0 ; j < length ; j ++){
					if(isDependforItem(dsmData, dsmData.getItemIndex(retData.getItem(i).getName()), dsmData.getItemIndex(retData.getItem(j).getName()))){
						setDependancy(retData, retData.getDataIndex(retData.getItem(i)), retData.getDataIndex(retData.getItem(j)), true);
					}
				}
			}

		} else {
			System.out.println(checkSameData(clsxData, dsmData) + "");
			System.out.println("두 Data가 같은 .dsm을 사용하여 만들어지지 않았습니다");
			retData.setName("null");
		}
		return retData;
	}

	/**
	 * Clsx를 Data 로 바꾸는 함수 Dsm 정보는 없고 Clsx 의 트리구조만 갖는 Data가 리턴된다 read 전용
	 */
	private Data makeClsxToData(Clsx c) {
		Data newData = new Data(c.getName());
		if (c.item != null) {
			for (int i = 0; i < c.item.length; i++) {
				newData.addChild(makeClsxToData(c.item[i]));
			}
		}
		return newData;
	}

	/**
	 * 데이터를 clsx 로 바꾸는 함수 데이터의 Dsm 정보는 빼고 저장하여 리턴한다. write 전용
	 */
	private Clsx makeDataToClsx() {
		return makeDataToClsx(data);
	}

	private Clsx makeDataToClsx(Data d) {
		Clsx newClsx = new Clsx(d.getName());
		int length = d.getChildLength();
		newClsx.item = new Clsx[length];
		for (int i = 0; i < length; i++) {
			newClsx.item[i] = makeDataToClsx(d.getChild(i));
		}
		return newClsx;
	}

	/**
	 * Data 를 ArrayList<Dsm> 으로 바꾸는 함수 순서는 Data 의 item 순서 그대로 가져온다. write 전용
	 */
	private Dsm makeDataToDsm() {
		return makeDataToDsm(data);
	}

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
			int depLength = data.getItem(i).getDependLength();
			for (int j = 0; j < length; j++) {
				for (int k = 0; k < depLength; k++) {
					dsm.setDependency(true, i, data.getItemIndex(data.getItem(i).getDepend(k).getName()));
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
			//System.out.println("number : " + nodeNumber + ", " + dataNumber);
			return false;
		}
		for (int i = 0; i < nodeNumber; i++) {
			boolean ret = false;
			for (int j = 0; j < dataNumber; j++){
				if (clsxData.getItem(i).getName().equals(dsmData.getItem(j).getName())){
					//System.out.println("name : " + i + ",  " + dsmData.GetItem(j).name);
					ret = true;
				}
			}
			if (!ret){
				//System.out.println("name1 : " + clsxData.GetItem(i).name );
				//return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 * @param data
	 * @param visualIndexes : 현재 폴더가 열려서 보이는 트리의 번호들을 입력받는다.
	 * @return 서로의 dependancy 를 이차원배열로 리턴한다.
	 */

	public boolean[][] getDependArray(Data data, int[] visualIndexes){
		System.out.println("start");
		int count = data.getItemCount();
		boolean[] drawIndex = new boolean[visualIndexes.length];
		for(int i = 0 ; i < visualIndexes.length ; i ++){
			Data local = data.getData(visualIndexes[i]);
			drawIndex[i] = false;
			for(int j = 0 ; j < local.getChildLength() ; j ++){
				for(int k = i ; k < visualIndexes.length ; k++){
					if(local.getChild(j) == data.getData(visualIndexes[k])){
						drawIndex[i] = true;
						break;
					}
				}
			}
			if(!drawIndex[i]){
				count -= (local.getItemCount() -1);
			}
		}
		System.out.println("last Count : " + count);
		boolean[][] ret = new boolean[count][count];
		int locali = 0;
		int localj = 0;
		for(int i = 0 ; i < count ; i ++){
			for(; locali < visualIndexes.length; locali++){
				if(!drawIndex[i + locali])break;
			}
			
			for(int j = 0 ; j < count ; j ++){
				for(; localj < visualIndexes.length; localj++){
					if(!drawIndex[j + localj])break;
				}
				if(i==j){
					ret[i][j] = false;
				}else{
					boolean localret = isDependChilds(data, visualIndexes[i+locali], visualIndexes[j+localj]);
					if(localret){
						ret[i][j] = localret;
					}
					/*
					if(data.getData(visualIndexes[i]).getChildLength() == 0){
						if(data.getData(visualIndexes[j]).getChildLength() == 0){
							ret[i][j] = isDepend(data, visualIndexes[i], visualIndexes[j]);
						}else{
							Data local = data.getData(visualIndexes[j]);
							boolean localret = false;
							for(int l2 = 0; l2 < local.getDependLength() ; l2 ++){
								if(!localret){
									localret = isDepend(data, visualIndexes[i], data.getDataIndex(local.getDepend(l2)));
								}
							}
							ret[i][j] = localret;
						}
					}else{
						if(data.getData(visualIndexes[j]).getChildLength() == 0){
							Data local = data.getData(visualIndexes[i]);
							boolean localret = false;
							for(int l = 0; l < local.getDependLength() ; l ++){
								if(!localret){
									localret = isDepend(data, data.getDataIndex(local.getDepend(l)), visualIndexes[j]);
								}
							}
							ret[i][j] = localret;
						}else{
							Data local = data.getData(visualIndexes[i]);
							Data local2 = data.getData(visualIndexes[j]);
							boolean localret = isDependChilds(data, visualIndexes[i], visualIndexes[j]);
							if(localret){
								System.out.println("local : " + data.getData(visualIndexes[i]).getName() + ", local2 : " + local2.getName());
								ret[i][j] = localret;
							}
						}
					}
					*/
				}
			}
		}
		return ret;
	}
	
	
	/**
	 * 
	 * @param front : 첫번째 데이타와
	 * @param back : 두번째 데이터의
	 * @return String name 을 비교하여 front 가  앞에 있으면 true, 뒤에 있으면 false
	 */
	private boolean compareString(Data front, Data back){
		int frontLength = front.getName().length();
		int backLength = back.getName().length();
		/*
		if(int i = 0 ; i < 10 ; i ++){
			if((int)front.name.charAt(i) < (int)back.name.charAt(i)){
				
			}
		}
		*/
		
		return true;
	}

	/**
	 * 생성자
	 */
	public DataController() {
		data = new Data("root");
	}

	public Data GetRoot() {
		return data;
	}
}