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
	
	public int Length(){
		return Length(this);
	}
	
	public Data GetItem(int index){
		return GetItem(this, index);
	}
	
	private int Length(Data c){
		int length = 0;
		int ret = 0;
		if(c.child != null){	length = c.child.size();	}
		if(length == 0){	ret = 1;	}
		else{
			ret = length;
			for(int i = 0 ; i < length ; i ++){
				ret += Length(c.child.get(i));
			}
		}
		return ret;
	}
	
	private Data GetItem(Data c, int index){
		int curIndex = index;
		if(Length(c) < index){
			return new Data("null");
		}else{
			for(int i = 0 ; i < c.child.size() ; i ++){
				curIndex -= Length(c.child.get(i));
				if(curIndex < 0){
					curIndex += Length(c.child.get(i));
					return GetItem(c.child.get(i), curIndex);
				}else if(curIndex == 0){
					return c.child.get(i);
				}
			}
		}
		return new Data("");
	}
}
