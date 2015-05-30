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
	 * 이름이 같은 노드를 찾는 함수
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
	 * 자신을 포함한 자기 밑의 모든 데이터의 갯수를 리턴
	 * 데이타는 통상적인 노드를 이야기함, 그룹노드와 리프노드 둘 다 포함
	 */
	public int DataCount(){
		int length = 0;
		int ret = 1;
		if(child != null){	length = child.size();	}
		for(int i = 0 ; i < length ; i ++){	ret += child.get(i).DataCount();	}
		return ret;
	}
	
	/*
	 * 자신을 포함한 자기 밑의 모든 데이터 중에 몇번째 데이터를 리턴
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
	 * 자기 밑의 모든 아이템의 갯수를 리턴
	 * 아이템은 그룹을 제외한 리프노드를 이야기함(child 가 없는 노드)
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
	 * 자기 밑의 모든 아이템 중에 몇번째 데이터를 리턴
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
