package model;

import java.util.ArrayList;
import java.util.List;

/**
 * DSM과 Clsx정보를 담고 있는 모델 객체
 * 처음에는 DSM 구조만 담고 있다가 Clsx를 불러올경우
 * Clsx정보도 읽어서 갖는다.
 */
public class Data {
	private String name;
	private List<Data> child;
	private List<Data> dependency;

	public Data(String name){
		this.name = new String(name);
		this.child = new ArrayList<Data>();
		this.dependency = new ArrayList<Data>();
	}
	
	public Data(){
		this("");
	}
	
	public Data(Data data){
		name = new String(data.name);
		child = new ArrayList<Data>();
		dependency = new ArrayList<Data>();
		//System.out.println("new Data name : " + name);
		if(data.child != null){
			for(int i = 0; i < data.child.size() ; i ++){
				child.add(new Data(data.child.get(i)));
			}
		}
	}
	
	public String toString(){
		return name;
	}
	
	
	/**
	 * 이름을 통해 Data를 찾는 함수
	 */
	public Data findData(String name){
		for(Data childData : child){
			if(childData.getName().equals("name")){
				return childData;
			}
			return childData.findData(name);
		}
		System.out.println("Can't find Data");
		return null;
	}
	
	/**
	 * 이름을 통해 Item을 찾는 함수
	 */
	public Data findItem(String name){
		Data retData = new Data("null");
		int length = countItem();
		for(int i = 0 ; i < length ; i ++){
			if(isSameString(getItem(i).name, name)){
				return getItem(i);
			}
		}
		return retData;
	}
	

	
	/**
	 * 자신을 포함한 자기 밑의 모든 데이터의 갯수를 리턴한다.
	 * 데이타는 통상적인 노드를 이야기하는데, 그룹노드와 리프노드 둘 다 포함한다.
	 */
	public int countData(){
		int length = 0;
		int ret = 1;
		if(child != null){	length = child.size();	}
		for(int i = 0 ; i < length ; i ++){	ret += child.get(i).countData();	}
		return ret;
	}
	
	/**
	 * 자신을 포함한 자기 밑의 모든 데이터 중에 index번째 데이터를 리턴한다.
	 */
	public Data getData(int index){
		int curIndex = index;
		if(countData() < index){
			System.out.println("countData() < index error");
			return new Data("null");
		}else{
			if(curIndex == 0){	return this;	}
			curIndex--;
			for(int i = 0 ; i < child.size() ; i ++){
				if(curIndex == 0){	return child.get(i);	}
				curIndex -= child.get(i).countData();
				if(curIndex < 0){
					curIndex += child.get(i).countData();
					return child.get(i).getData(curIndex);
				}
			}
		}
		return new Data("");
	}

	/**
	 * name에 해당하는 Data의 인덱스값을 반환해준다.
	 * @param name
	 * @return Data의 인덱스 번호
	 */
	public int findDataIndex(String name){
		int length = countData();
		for(int i = 0 ; i < length ; i ++){
			if(isSameString(getData(i).name, name)){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * name에 해당하는 Item의 인덱스값을 반환해준다.
	 * @param name
	 * @return Item의 인덱스 번호
	 */
	public int findItemIndex(String name){
		int length = countItem();
		
		for(int i = 0 ; i < length ; i ++){
			if(isSameString(getItem(i).name, name)){
				return i;
			}
		}
		return -1;
	}
	
	
	public int findChildIndex(String name){
		int index = -1;
		for(int i = 0 ; i < child.size() ; i ++){
			if(isSameString(child.get(i).name, name)){
				index = i;
				break;
			}
		}
		return index;
	}
	
	/**
	 * 자기 밑의 모든 아이템의 갯수를 리턴한다.
	 * 아이템은 그룹을 제외한 리프노드를 이야기한다.(child 가 없는 노드)
	 */
	public int countItem(){
		int length = 0;
		int ret = 0;
		if(child != null){	length = child.size();	}
		if(length == 0){	ret = 1;	}
		else{
			for(int i = 0 ; i < length ; i ++){
				ret += child.get(i).countItem();
			}
		}
		return ret;
	}
	
	/** 
	 * 자기 밑의 모든 아이템 중에 index값에 해당하는 데이터를 리턴해준다.
	 */
	public Data getItem(int index){
		int curIndex = index;
		if(countItem() < index){
			return new Data("null_itemCountLessThenIndex");
		}else{
			for(int i = 0 ; i < child.size() ; i ++){
				if(curIndex == 0){
					if(child.get(i).child.size() == 0){
						return child.get(i);
					}else{
						return child.get(i).getItem(0);
					}
				}
				curIndex -= child.get(i).countItem();
				if(curIndex < 0){
					curIndex += child.get(i).countItem();
					return child.get(i).getItem(curIndex);
				}
			}
		}
		return new Data("nnnnnnnnn");
	}
	
	public boolean isSameName(String name){
		return isSameString(name, this.name);
	}
	
	private boolean isSameString(String s1, String s2){
		if(s1.length() == s2.length()){
			char[] c1, c2;
			c1 = s1.toCharArray();
			c2 = s2.toCharArray();
			for(int i = 0 ; i < s1.length(); i ++){
				if(c1[i]!=c2[i]){
					return false;
				}
			}
		}else{
			return false;
		}
		return true;
	}
	
	
	/*
	 * Getter, Setter 
	 */
	public List<Data> getChild(){	return child;	}
	public List<Data> getDependency(){	return dependency; }
	
	public Data getChildData(int index){				return child.get(index);	}
	public Data getDependencyData(int index){				return dependency.get(index);	}

	public int getChildLength(){					return child.size();		}
	public int getDependencyLength(){					return dependency.size();		}
	
	public void addChildData(Data data){				child.add(data);			}
	public void addChildData(int index, Data data){		child.add(index, data);		}
	public void addDependencyData(Data data){				dependency.add(data);			}
	public void addDependencyData(int index, Data data){	dependency.add(index, data);	}
	
	public void removeChildData(int index){				child.remove(index);		}
	public void removeChildData(Data data){				child.remove(data);			}
	public void removeDependencyData(int index){			dependency.remove(index);		}
	public void removeDependencyData(Data data){			dependency.remove(data);		}
	
	public void setChildData(int index, Data data){		child.set(index, data);		}
	public void setDependencyData(int index, Data data){	dependency.set(index, data);	}
	public String getName() {		return name; 	}
	public void setName(String name) {		this.name = name;	}

}