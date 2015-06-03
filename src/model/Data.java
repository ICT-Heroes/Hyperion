package model;

import java.util.ArrayList;

public class Data {
	public String name;
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
	public Data FindData(String name){
		Data retData = new Data("null");
		int length = DataCount();
		for(int i = 0 ; i < length ; i ++){
			if(isSameString(GetData(i).name, name)){
				return GetData(i);
			}
		}
		return retData;
	}
	
	/*
	 * 이름을 통해 Item을 찾는 함수
	 */
	public Data FindItem(String name){
		Data retData = new Data("null");
		int length = ItemCount();
		for(int i = 0 ; i < length ; i ++){
			if(isSameString(GetItem(i).name, name)){
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

	public int FindDataIndex(String name){
		int length = DataCount();
		for(int i = 0 ; i < length ; i ++){
			if(isSameString(GetData(i).name, name)){
				return i;
			}
		}
		return -1;
	}
	
	public int FindItemIndex(String name){
		int length = ItemCount();
		
		for(int i = 0 ; i < length ; i ++){
			if(isSameString(GetItem(i).name, name)){
				return i;
			}
		}
		return -1;
	}
	
	public int FindChildIndex(String name){
		int index = -1;
		for(int i = 0 ; i < child.size() ; i ++){
			if(isSameString(child.get(i).name, name)){
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
			return new Data("null_itemCountLessThenIndex");
		}else{
			for(int i = 0 ; i < child.size() ; i ++){
				if(curIndex == 0){
					if(child.get(i).child.size() == 0){
						return child.get(i);
					}else{
						return child.get(i).GetItem(0);
					}
				}
				curIndex -= child.get(i).ItemCount();
				if(curIndex < 0){
					curIndex += child.get(i).ItemCount();
					return child.get(i).GetItem(curIndex);
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
	public Data GetChild(int index){				return child.get(index);	}
	public Data GetDepend(int index){				return depend.get(index);	}

	public int GetChildLength(){					return child.size();		}
	public int GetDependLength(){					return depend.size();		}
	
	public void AddChild(Data data){				child.add(data);			}
	public void AddChild(int index, Data data){		child.add(index, data);		}
	public void AddDepend(Data data){				depend.add(data);			}
	public void AddDepend(int index, Data data){	depend.add(index, data);	}
	
	public void RemoveChild(int index){				child.remove(index);		}
	public void RemoveChild(Data data){				child.remove(data);			}
	public void RemoveDepend(int index){			depend.remove(index);		}
	public void RemoveDepend(Data data){			depend.remove(data);		}
	
	public void SetChild(int index, Data data){		child.set(index, data);		}
	public void SetDepend(int index, Data data){	depend.set(index, data);	}
	

}