package controller;

import java.io.File;
import java.util.ArrayList;

import model.Clsx;
import model.Data;
import model.Dsm;

public class DataController {
	
	private ReadClsxController readClsx;
	private ReadDsmController readDsm;
	
	
	//private WriteClsxController writeClsx;
	//private WriteDsmController writeDsm;
	

	public void MoveUp(Data data, String name){
		Data parent = FindParent(data, name);
		int index = 0;
		for(; index < parent.child.size() ; index++){
			if(parent.child.get(index).name == name){
				break;
			}
		}
		System.out.println(index);
		if(0 < index){

			Data cur, forward;
			cur = parent.child.get(index);
			forward = parent.child.get(index -1);
			parent.child.set(index, forward);
			parent.child.set(index-1, cur);
		}
	}
	
	public void MoveDown(Data data, String name){
		Data parent = FindParent(data, name);
		int index = 0;
		for(; index < parent.child.size() ; index++){
			if(parent.child.get(index).name == name){
				break;
			}
		}
		if(index < parent.child.size()-1){
			Data cur, back;
			cur = parent.child.get(index);
			back = parent.child.get(index+1);
			parent.child.set(index, back);
			parent.child.set(index+1, cur);
		}
	}
	
	private Data FindParent(Data data, String name){
		int dataIndex = data.FindDataIndex(name);
		for(int i = 1 ; i <= dataIndex ; i ++){
			Data fdata = data.FindData(data.GetData(dataIndex - i).name);
			if(fdata.FindData(name).name != "null"){
				return fdata;
			}
		}
		return new Data();
	}
	
	/*
	 * data�� ��� ��� ��ü�� �̸��� �ٲٴ� �Լ�
	 */
	public void SetName(Data data, String exName, String newName){	data.FindData(exName).name = newName;	}
	public void SetName(Data data, int dataIndex, String newName){	data.GetData(dataIndex).name = newName;	}
	
	
	/*
	 * data �� itemName �� �ش��ϴ� Data��ü�� depend �� dependItemName �� ������ �̸��� ����
	 * Data ��ü�� �߰��ϰų� ������.
	 * �̹� �������� ���� ������ ����
	 * �������� ������ �߰�
	 * toggle
	 */
	public void SetDependancy(Data data, String itemName, String dependItemName){
		Data item, depItem;
		item = data.FindItem(itemName);
		depItem = data.FindItem(dependItemName);
		if(isDepend(data, itemName, dependItemName)){
			item.depend.remove(depItem);
		}else{
			item.depend.add(depItem);
		}
	}
	
	/*
	 * data �� itemName �� dependItemName ���� �����ϰ� �ִ°�?
	 */
	public boolean isDepend(Data data, String itemName, String dependItemName){
		Data item, depItem;
		item = data.FindItem(itemName);
		depItem = data.FindItem(dependItemName);
		int length = item.depend.size();
		for(int i = 0 ; i < length ; i ++){
			if(item.depend.get(i).name == depItem.name){
				return true;
			}
		}
		return false;
	}
	
	/*
	 * data ���� ������ �� �߰��ϰ���� �ڸ��� �̸��� �ι�° �μ��� ������
	 * �� �ڸ��� newData ��� �̸����� �������� �߰��Ѵ�.
	 */
	public void AddItem(Data data, String itemName){
		AddItem(data, itemName, "newData");
	}
	public void AddItem(Data data, String itemName, String newItemName){
		Data newData;
		int i = 0;
		while(data.FindData(newItemName + i).name != "null"){
			i++;
			if(100000<i){
				System.out.println("������ �߰� ����, �ʹ� ���� �������� �̸� ���� ���� �߰��Ƿ� �ϰ��ִ�.");
				return;
			}
		}
		if(i == 0){	newData = new Data(newItemName);		}
		else{		newData = new Data(newItemName + i);	}
		if(0 < data.FindData(itemName).child.size()){
			data.FindData(itemName).child.add(newData);
		}else{
			Data parent = FindParent(data, itemName);
			parent.child.add(newData);
		}
	}
	
	/*
	 * data ���� �������� �����.
	 */
	public void DeleteItem(Data data, String itemName){
		//���� �����
		for(int i = 0 ; i < data.ItemCount() ; i++){
			int depLength = data.GetItem(i).depend.size();
			for(int j = 0 ; j < depLength ; j++){
				if(data.GetItem(i).depend.get(j).name == itemName){
					data.GetItem(i).depend.remove(j);
				}
			}
		}
		//�������� ������ �����
		Data parent = FindParent(data, itemName);
		for(int i = 0 ; i < parent.child.size() ; i ++){
			if(parent.child.get(i).child.size() == 0){
				if(parent.child.get(i).name == itemName){
					parent.child.remove(i);
				}
			}
		}
	}
	
	/*
	 * ���� �θ� ���� ���� ������ ������ �� �ִ�.
	 * startName �� �׷��� �����ϴ� ���, endName �� �׷��� ������ ���
	 * startName, endName ��� �׷� �ȿ� ����.
	 */
	public void CreateGroup(Data data, String startName, String endName){
		CreateGroup(data, startName, endName, "newGroup");
	}
	public void CreateGroup(Data data, String startName, String endName, String groupName){
		int index = 0;
		while(data.FindData(groupName + index).name != "null"){
			index++;
			if(100000<index){
				System.out.println("�׷����� ����, �ʹ� ���� �׷��� ����� �̸��� ���� �ִ�.");
				return;
			}
		}
		if(startName != endName){
			if(FindParent(data, startName).name == FindParent(data, endName).name){
				int start, end;
				start = end = 0;
				Data parent = FindParent(data, startName);
				for(int i = 0 ; i < parent.child.size() ; i++){
					if(parent.child.get(i).name == startName){	start = i;	}
					if(parent.child.get(i).name == endName){	end = i;	}
				}
				if(end < start){
					int a = end;
					end = start;
					start = a;
				}
				Data newData;
				if(index == 0){		newData = new Data(groupName);	}
				else{				newData = new Data(groupName + index);	}
				for(int i = 0 ; i < end-start+1 ; i ++){
					newData.child.add(parent.child.get(start + i));
				}
				parent.child.set(start, newData);
				for(int i = 0 ; i < end-start ; i ++){
					parent.child.remove(start+1);
				}
			}
		}
	}
	
	
	/*
	 * �׷�Ǯ��
	 * �׷��� �̸��� �ι�° ���ڷ� ������ �� �׷��� Ǭ��.
	 */
	public void DeleteGroup(Data data, String groupName){
		Data parent = FindParent(data, groupName);
		Data Group = data.FindData(groupName);
		int index = parent.FindChildIndex(groupName);
		int size = Group.child.size();
		for(int i = 0 ; i < size ; i ++){
			parent.child.add(index+1, Group.child.get(size-i-1));
		}
		parent.child.remove(index);
	}
	
	/*
	 * File �� ������ Dsm ������ �а� Data�� ��ȯ�Ͽ� Data �� ����
	 */
	public Data LoadDsm(File file){
		Data dsmData;
		readDsm = new ReadDsmController();
		readDsm.readFile(file);
		
		int nodeNumber = readDsm.getNumber();
		dsmData = new Data("root");
		
		//��� ����
		for(int i = 0 ; i < nodeNumber ; i ++){
			dsmData.child.add(new Data(readDsm.getDsm(i).getName()));
		}
		
		//dependancy ����
		for(int i = 0 ; i < nodeNumber ; i ++){
			for(int j = 0 ; j < nodeNumber ; j ++){
				if(readDsm.getDsm(i).isDependent(j)){
					dsmData.child.get(i).depend.add(dsmData.child.get(j));
				}
			}
		}
		return dsmData;
	}
	
	public Data LoadClsx(File file){
		return MakeClsxToData(readClsx.ReadFile(file));
	}
	
	public Data LoadClsx(Clsx c){
		return MakeClsxToData(c);
	}
	
	
	/*
	 * �� ���� ������ �� ���� ���� �� ����� �� �ִ�.
	 * �� ���� ������ �� dsm �� �ε��Ͽ��ų� clsx ������ �ε����� �ʾƵ� �����Ѵ�.
	 * ������ ���۵� Data ������ �־��൵ �����Ѵ�.
	 * 
	 * ���ϵǾ� ������ Data �� �ι�° ������ Ʈ�������� �����ϸ�,
	 * ù���� ������ dependancy ������ �����´�.
	 */
	public Data LoadDsmClsx(File dsmFile, File clsxFile){	return SumData(LoadClsx(clsxFile),LoadDsm(dsmFile));	}
	public Data LoadDsmClsx(Data dsmData, File clsxFile){	return SumData(LoadClsx(clsxFile),dsmData);				}
	public Data LoadDsmClsx(File dsmFile, Data clsxData){	return SumData(clsxData,LoadDsm(dsmFile));				}
	public Data LoadDsmClsx(Data dsmData, Data clsxData){	return SumData(clsxData, dsmData);						}
	private Data SumData(Data clsxData, Data dsmData){
		Data retData = new Data("root");
		if(CheckSameData(clsxData, dsmData)){
			retData = new Data(clsxData);
			int length = retData.ItemCount();
			for(int i = 0 ; i < length ; i ++){
				Data dsmDatai = dsmData.FindItem(retData.GetItem(i).name);
				int depSize = dsmDatai.depend.size();
				for(int j = 0 ; j < depSize ; j ++){
					Data retDataji = retData.FindItem(dsmDatai.depend.get(j).name);
					retData.GetItem(i).depend.add(retDataji);
				}
			}
		}else{
			System.out.println("�� Data�� ���� .dsm�� ����Ͽ� ��������� �ʾҽ��ϴ�");
			retData.name = "null";
		}
		return retData;
	}

	

	/*
	 * Clsx�� Data �� �ٲٴ� �Լ�
	 * Dsm ������ ���� Clsx �� Ʈ�������� ���� Data�� ���ϵȴ�
	 * read ����
	 */
	private Data MakeClsxToData(Clsx c){
		Data newData = new Data(c.getName());
		if(c.item != null){
			for(int i = 0 ; i < c.item.length ; i ++){
				newData.child.add(MakeClsxToData(c.item[i]));
			}
		}
		return newData;
	}
	
	/*
	 * �����͸� clsx �� �ٲٴ� �Լ�
	 * �������� Dsm ������ ���� �����Ͽ� �����Ѵ�.
	 * write ����
	 */
	private Clsx MakeDataToClsx(Data d){
		Clsx newClsx = new Clsx(d.name);
		if(d.child != null){
			int length = d.child.size();
			newClsx.item = new Clsx[length];
			for(int i = 0 ; i < length ; i ++){
				newClsx.item[i] = MakeDataToClsx(d.child.get(i));
			}
		}
		return newClsx;
	}
	
	
	/*
	 * Data �� ArrayList<Dsm> ���� �ٲٴ� �Լ�
	 * ������ Data �� item ���� �״�� �����´�.
	 * write ����
	 */
	private ArrayList<Dsm> MakeDataToDsm(Data data){
		ArrayList<Dsm> retList = new ArrayList<Dsm>();
		int length = data.ItemCount();
		for(int i = 0 ; i < length ; i ++)
			retList.add(new Dsm(i, data.GetItem(i).name));
		for(int i = 0 ; i < length ; i++){
			int depLength = data.GetItem(i).depend.size();
			for(int j = 0 ; j < depLength ; j ++){
				int itemIndex = data.FindItemIndex(data.GetItem(i).depend.get(j).name);
				retList.get(i).addModel(retList.get(itemIndex));
			}
		}
		return retList;
	}


	/*
	 * �� Data ��ü�� ������ �̸����� �������� ����.
	 * true�� ���� ���� dsm �����͸� ���� ������ clsx Ʈ��
	 */
	private boolean CheckSameData(Data clsxData, Data dsmData){
		int nodeNumber = clsxData.ItemCount();
		int dataNumber = dsmData.ItemCount();
		if(nodeNumber != dataNumber){	return false;	}
		for(int i = 0 ; i < nodeNumber ; i ++){
			boolean ret = false;
			for(int j = 0 ; j < dataNumber ; j ++ )
				if(clsxData.GetItem(i).name == dsmData.GetItem(j).name)
					ret = true;
			if(!ret)
				return false;
		}
		return true;
	}
	
	/*
	 * ������
	 */
	public DataController(){
		readClsx = new ReadClsxController();
		readDsm = new ReadDsmController();
	}
}

