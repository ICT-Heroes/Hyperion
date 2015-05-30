package controller;

import java.io.File;
import java.util.ArrayList;

import model.Clsx;
import model.Data;
import model.Dsm;

public class DataController {
	
	private ReadClsxController readClsx;
	private ReadDsmController readDsm;
	
	public DataController(){
		readClsx = new ReadClsxController();
		readDsm = new ReadDsmController();
	}
	
	public Data LoadDsm(File file){
		
		Data dsmData;
		readDsm = new ReadDsmController();
		readDsm.readFile(file);
		
		int nodeNumber = readDsm.getNumber();
		dsmData = new Data("root");
		
		//노드 생성
		for(int i = 0 ; i < nodeNumber ; i ++){
			dsmData.child.add(new Data(readDsm.getDsm(i).getName()));
		}
		
		//dependancy 연결
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
	
	public Data LoadClsx(Clsx clsx){
		return MakeClsxToData(clsx);
	}
	
	
	/*
	private void SumData(){
		if(CheckClsxAndData()){
			data = new Data(clsxData);
		}else{
			System.out.println("Clsx 와 Dsm 이 서로 맞지 않습니다.");
		}
	}
	
	private void SetDep(Data info, ArrayList<Data> array, String name){
		
	}
	
	private void FindClsxItem(String name){
		
	}
	
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
	 * 혹시 쓸일이 있을까봐 만들어 놓음
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
	*/



	private boolean CheckSameData(Clsx clsxData, Data dsmData){
		int nodeNumber = readClsx.GetItemLength(clsxData);
		int dataNumber = dsmData.ItemCount();
		if(nodeNumber != dataNumber){
			return false;
		}
		for(int i = 0 ; i < nodeNumber ; i ++){
			boolean ret = false;
			for(int j = 0 ; j < dataNumber ; j ++ ){
				if(readClsx.GetItem(clsxData, i).getName() == dsmData.GetItem(j).name){
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





