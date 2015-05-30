package model;

import java.util.ArrayList;

public class Data {
	public String name;
	public ArrayList<Data> child;
	public ArrayList<Data> depend;

	public Data(String name){
		this.name = name;
		child = new ArrayList<Data>();
		depend = new ArrayList<Data>();
	}
	
	public Data(){
		this("");
	}
	
	public Data(Data data){
		name = data.name;
		Data[] newDatas = new Data[data.child.size()];
		for(int i = 0 ; i < newDatas.length ; i ++){
			newDatas[i] = new Data(data.child.get(i));
		}
		for(int i = 0; i < newDatas.length ; i ++){
			child.add(newDatas[i]);
		}
	}
	
	public String toString(){
		return name;
	}
	
	
	/*
	 * �̸��� ���� ��带 ã�� �Լ�
	 */
	public Data Find(String name){
		Data retData = new Data("null");
		int length = ItemCount();
		for(int i = 0 ; i < length ; i ++){
			if(GetItem(i).name == name){
				return GetItem(i);
			}
		}
		return retData;
	}
	

	
	/*
	 * �ڽ��� ������ �ڱ� ���� ��� �������� ������ ����
	 * ����Ÿ�� ������� ��带 �̾߱���, �׷���� ������� �� �� ����
	 */
	public int DataCount(){
		int length = 0;
		int ret = 1;
		if(child != null){	length = child.size();	}
		for(int i = 0 ; i < length ; i ++){	ret += child.get(i).DataCount();	}
		return ret;
	}
	
	/*
	 * �ڽ��� ������ �ڱ� ���� ��� ������ �߿� ���° �����͸� ����
	 */
	public Data GetData(int index){
		int curIndex = index;
		if(DataCount() < index){
			return new Data("null");
		}else{
			if(curIndex == 0){	return this;	}
			curIndex--;
			for(int i = 0 ; i < child.size() ; i ++){
				if(curIndex == 0){	return child.get(i);	}
				curIndex -= child.get(i).DataCount();
				if(curIndex < 0){
					curIndex += child.get(i).DataCount();
					return child.get(i).GetData(curIndex);
				}
			}
		}
		return new Data("");
	}

	
	public int FindItemIndex(String name){
		int length = ItemCount();
		for(int i = 0 ; i < length ; i ++){
			if(GetItem(i).name == name){
				return i;
			}
		}
		return -1;
	}
	
	/*
	 * �ڱ� ���� ��� �������� ������ ����
	 * �������� �׷��� ������ ������带 �̾߱���(child �� ���� ���)
	 */
	public int ItemCount(){
		int length = 0;
		int ret = 0;
		if(child != null){	length = child.size();	}
		if(length == 0){	ret = 1;	}
		else{
			for(int i = 0 ; i < length ; i ++){
				ret += child.get(i).ItemCount();
			}
		}
		return ret;
	}
	
	/* 
	 * �ڱ� ���� ��� ������ �߿� ���° �����͸� ����
	 */
	public Data GetItem(int index){
		int curIndex = index;
		if(ItemCount() < index){
			return new Data("null");
		}else{
			for(int i = 0 ; i < child.size() ; i ++){
				curIndex -= child.get(i).ItemCount();
				if(curIndex < 0){
					curIndex += child.get(i).ItemCount();
					return child.get(i).GetItem(curIndex);
				}else if(curIndex == 0){
					return child.get(i);
				}
			}
		}
		return new Data("");
	}
}
