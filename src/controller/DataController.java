package controller;

import java.io.File;
import java.util.ArrayList;

import model.Clsx;
import model.Data;
import model.Dsm;

public class DataController {
	private boolean isLoadClsx;
	
	private ReadClsxController clsx;
	private ReadDsmController dsm;
	public Data data, dsmData, clsxData;
	
	public DataController(){
		isLoadClsx = false;
	}
	

	public Clsx GetClsx(){
		return clsx.clsx;
	}
	
	public ArrayList<Dsm> GetDsm(){
		return dsm.dsms;
	}
	
	public void LoadDsm(File file){
		dsm = new ReadDsmController();
		dsm.readFile(file);
		
		int nodeNumber = dsm.getNumber();
		dsmData = new Data("root");
		
		//노드 생성
		for(int i = 0 ; i < nodeNumber ; i ++){
			dsmData.child.add(new Data(dsm.getDsm(i).getName()));
		}
		
		//dependancy 연결
		for(int i = 0 ; i < nodeNumber ; i ++){
			for(int j = 0 ; j < nodeNumber ; j ++){
				if(dsm.getDsm(i).isDependent(j)){
					dsmData.child.get(i).depend.add(dsmData.child.get(j));
				}
			}
		}
		
		if(isLoadClsx && CheckClsxAndData()){
			Data newData = new Data("root");
			
		}else{
			isLoadClsx = false;
			clsx = null;
		}
	}

	
	
	public void LoadClsx(File file){
		clsx = new ReadClsxController(file);
		clsxData = MakeClsxData(clsx.clsx);
	}
	
	private void SumData(){
		data = new Data(clsxData);
	}
	
	
	/*
	public void LoadClsx(Clsx c){
		clsxData = MakeClsxData(c);
	}
	*/
	
	private Data MakeClsxData(Clsx c){
		Data newData = new Data(c.getName());
		if(c.item != null){
			for(int i = 0 ; i < c.item.length ; i ++){
				newData.child.add(MakeClsxData(c.item[i]));
			}
		}
		return newData;
	}



	private boolean CheckClsxAndData(){
		int nodeNumber = clsx.GetItemLength(clsx.clsx);
		int dataNumber = data.Length();
		if(nodeNumber != dataNumber){
			return false;
		}
		for(int i = 0 ; i < nodeNumber ; i ++){
			boolean ret = false;
			for(int j = 0 ; j < dataNumber ; j ++ ){
				if(clsx.GetItem(clsx.clsx, i).getName() == data.GetItem(j).name){
					ret = true;
				}
			}
			if(!ret){
				return false;
			}
		}
		return true;
	}
}


