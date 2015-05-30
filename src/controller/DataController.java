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
	
	
	/*
	 * Data �� ArrayList<Dsm> ���� �ٲٴ� �Լ�
	 * ������ Data �� item ���� �״�� �����´�.
	 * write �� �� ���� ���� ����
	 */
	public ArrayList<Dsm> MakeDataToDsm(Data data){
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
	

	
	
	public Data LoadClsx(File file){
		return MakeClsxToData(readClsx.ReadFile(file));
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
	
	
	public void MoveUp(Data data, String name){
		
	}
	
	public void MoveDown(Data data, String name){
		
	}
	
	public void SetDependancy(){
		
	}
	
	
	private Data SumData(Data clsxData, Data dsmData){
		Data retData = new Data("root");
		if(CheckSameData(clsxData, dsmData)){
			retData = new Data(clsxData);
			int length = retData.ItemCount();
			for(int i = 0 ; i < length ; i ++){
				Data dsmDatai = dsmData.Find(retData.GetItem(i).name);
				int depSize = dsmDatai.depend.size();
				for(int j = 0 ; j < depSize ; j ++){
					Data retDataji = retData.Find(dsmDatai.depend.get(j).name);
					retData.GetItem(i).depend.add(retDataji);
				}
			}
		}else{
			System.out.println("�� Data�� ���� .dsm�� ����Ͽ� ��������� �ʾҽ��ϴ�");
			retData.name = "null";
		}
		return retData;
	}
	

	
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
	public Clsx MakeDataToClsx(Data d){
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
	 * �� Data ��ü�� ������ �̸����� �������� ����.
	 * ������ ���� ���� dsm �����͸� ���� ������ clsx Ʈ����
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
}

