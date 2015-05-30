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
	
	public void SetDependancy(String name, String name2){
		
	}
	
	/*
	 * File 을 받으면 Dsm 정보를 읽고 Data로 변환하여 Data 를 리턴
	 */
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
	
	public Data LoadClsx(Clsx c){
		return MakeClsxToData(c);
	}
	
	
	/*
	 * 두 파일 정보를 다 갖고 있을 때 사용할 수 있다.
	 * 두 파일 정보가 꼭 dsm 을 로드하였거나 clsx 파일을 로드하지 않아도 동작한다.
	 * 적당히 조작된 Data 구조를 넣어줘도 동작한다.
	 * 
	 * 리턴되어 나오는 Data 는 두번째 인자의 트리구조를 따라하며,
	 * 첫번재 인자의 dependancy 정보를 가져온다.
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
			System.out.println("두 Data가 같은 .dsm을 사용하여 만들어지지 않았습니다");
			retData.name = "null";
		}
		return retData;
	}

	

	/*
	 * Clsx를 Data 로 바꾸는 함수
	 * Dsm 정보는 없고 Clsx 의 트리구조만 갖는 Data가 리턴된다
	 * read 전용
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
	 * 데이터를 clsx 로 바꾸는 함수
	 * 데이터의 Dsm 정보는 빼고 저장하여 리턴한다.
	 * write 전용
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
	 * Data 를 ArrayList<Dsm> 으로 바꾸는 함수
	 * 순서는 Data 의 item 순서 그대로 가져온다.
	 * write 전용
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
	 * 두 Data 객체의 노드들의 이름들이 같은지를 조사.
	 * true면 서로 같은 dsm 데이터를 갖고 조작한 clsx 트리
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
	 * 생성자
	 */
	public DataController(){
		readClsx = new ReadClsxController();
		readDsm = new ReadDsmController();
	}
}
