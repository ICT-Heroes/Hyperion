package model;

import java.util.ArrayList;

/**
 * DSM과 Clsx정보를 담고 있는 모델 객체
 * 처음에는 DSM 구조만 담고 있다가 Clsx를 불러올경우
 * Clsx정보도 읽어서 갖는다.
 */

public class Data {
	private String name;
	private ArrayList<Data> child;
	private ArrayList<Data> depend;

	public Data(String name){
		this.name = new String(name);
		child = new ArrayList<Data>();
		depend = new ArrayList<Data>();
	}
	
	public Data(){
		this("");
	}
	

	
	public Data(Data data){
		name = new String(data.name);
		child = new ArrayList<Data>();
		depend = new ArrayList<Data>();
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
	
	
	/*
	 * 이름을 통해 Data를 찾는 함수
	 */
	public Data getData(String name){
		Data retData = new Data("null");
		int length = getDataCount();
		for(int i = 0 ; i < length ; i ++){
			if(getData(i).name.equals(name)){
				return getData(i);
			}
		}
		return retData;
	}
	
	/*
	 * 이름을 통해 Item을 찾는 함수
	 */
	public Data getItem(String name){
		Data retData = new Data("null");
		int length = getItemCount();
		for(int i = 0 ; i < length ; i ++){
			if(getItem(i).name.equals(name)){
				return getItem(i);
			}
		}
		return retData;
	}
	

	
	
	/**
	 * 자신을 포함한 자기 밑의 모든 데이터의 갯수를 리턴한다.
	 * 데이타는 통상적인 노드를 이야기하는데, 그룹노드와 리프노드 둘 다 포함한다.
	 */
	public int getDataCount(){
		int length = 0;
		int ret = 1;
		if(child != null){	length = child.size();	}
		for(int i = 0 ; i < length ; i ++){	ret += child.get(i).getDataCount();	}
		return ret;
	}
	
	/*
	 * 자신을 포함한 자기 밑의 모든 데이터 중에 몇번째 데이터를 리턴
	 */
	public Data getData(int index){
		int curIndex = index;
		if(getDataCount() < index){
			return new Data("null");
		}else{
			if(curIndex == 0){	return this;	}
			curIndex--;
			for(int i = 0 ; i < child.size() ; i ++){
				if(curIndex == 0){	return child.get(i);	}
				curIndex -= child.get(i).getDataCount();
				if(curIndex < 0){
					curIndex += child.get(i).getDataCount();
					return child.get(i).getData(curIndex);
				}
			}
		}
		return new Data("");
	}

	public int getDataIndex(String name){
		int length = getDataCount();
		for(int i = 0 ; i < length ; i ++){
			if(getData(i).name.equals(name)){
				return i;
			}
		}
		return -1;
	}
	
	public int getItemIndex(String name){
		int length = getItemCount();
		
		for(int i = 0 ; i < length ; i ++){
			if(getItem(i).name.equals(name)){
				return i;
			}
		}
		return -1;
	}
	
	public int getChildIndex(String name){
		int index = -1;
		for(int i = 0 ; i < child.size() ; i ++){
			if(child.get(i).name.equals(name)){
				index = i;
				break;
			}
		}
		return index;
	}
	
	/*
	 * 자기 밑의 모든 아이템의 갯수를 리턴
	 * 아이템은 그룹을 제외한 리프노드를 이야기함(child 가 없는 노드)
	 */
	public int getItemCount(){
		int length = 0;
		int ret = 0;
		if(child != null){	length = child.size();	}
		if(length == 0){	ret = 1;	}
		else{
			for(int i = 0 ; i < length ; i ++){
				ret += child.get(i).getItemCount();
			}
		}
		return ret;
	}
	
	/* 
	 * 자기 밑의 모든 아이템 중에 몇번째 데이터를 리턴
	 */
	public Data getItem(int index){
		int curIndex = index;
		if(getItemCount() < index){
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
				curIndex -= child.get(i).getItemCount();
				if(curIndex < 0){
					curIndex += child.get(i).getItemCount();
					return child.get(i).getItem(curIndex);
				}
			}
		}
		return new Data("nnnnnnnnn");
	}
	
	
	/*
	 * Getter, Setter 
	 */
	
	public String getName(){
		return name;
	}
	public void setName(String name){
		this.name = name;
	}
	
	public Data getChild(int index){				return child.get(index);	}
	public Data getDepend(int index){				return depend.get(index);	}

	public int getChildLength(){					return child.size();		}
	public int getDependLength(){					return depend.size();		}
	
	public void addChild(Data data){				child.add(data);			}
	public void addChild(int index, Data data){		child.add(index, data);		}
	public void addDepend(Data data){				depend.add(data);			}
	public void addDepend(int index, Data data){	depend.add(index, data);	}
	
	public void removeChild(int index){				child.remove(index);		}
	public void removeChild(Data data){				child.remove(data);			}
	public void removeDepend(int index){			depend.remove(index);		}
	public void removeDepend(Data data){			depend.remove(data);		}
	
	public void setChild(int index, Data data){		child.set(index, data);		}
	public void setDepend(int index, Data data){	depend.set(index, data);	}
	

}