package test;

import java.io.File;

import model.Clsx;
import model.Data;
import model.Dsm;

import org.junit.Before;
import org.junit.Test;

import controller.DataController;

public class DataTest {
	public static void main(String argv[]) {
		DataController dataController = new DataController();
		File dsmFile = new File("C:\\Users\\hyobin\\Desktop\\titan\\titan\\data dir\\titan\\titan.dsm");
		File clsxFile = new File("C:\\Users\\hyobin\\Desktop\\titan\\titan\\data dir\\ffff\\titan_DRH+ACDC.clsx");
		Data dsmData = dataController.loadDsm(dsmFile);
		Data clsxData = dataController.loadClsx(clsxFile);
		//System.out.println(dsmData.getItemCount());
		//System.out.println(clsxData.getItemCount());

		
		for(int i = 0 ; i < dsmData.getItemCount() ; i++){
			String ss = "";
			for(int j = 0 ; j < dsmData.getItemCount() ; j++){
				boolean iii = false;
				for(int k = 0 ; k < dsmData.getItem(i).getDependLength() ; k++){
					if(dsmData.getItem(i).getDepend(k).getName().equals(dsmData.getItem(j).getName())){
						iii = true;
					}
				}
				if(iii){
					ss += " 1";
				}else{
					ss += " .";
				}
				
			}
			//System.out.println(ss);
		}
		for(int i = 0 ; i < dsmData.getDataCount() ; i++){
			System.out.println(dsmData.getData(i).getName());
		}
		System.out.println("___________________________________________");
		int[] bb = new int[5];
		bb[0] = 3;
		bb[1] = 5;
		bb[2] = 7;
		bb[3] = 9;
		bb[4] = 11;
		
		dataController.createGroup(bb, "fdsafdsa");
		for(int i = 0 ; i < dsmData.getDataCount() ; i++){
			System.out.println(dsmData.getData(i).getName());
		}
		
		Data sum = dataController.loadDsmClsx(dsmFile, clsxFile);
		int[] aa = new int[37] ;
		for(int i = 0 ; i < 37 ; i ++){
			aa[i] = i;
		}
		
		
		
		/*
		boolean[][] ret = dataController.getDependArray(dsmData, aa);
		System.out.println("itemcount : " + dsmData.getDataCount());
		for(int i = 0 ; i < dsmData.getDataCount() ; i++){
			//System.out.println(dsmData.GetData(i).getName());
		}
		Dsm dsm = dataController.makeDataToDsm(dsmData);
		
		
		
		for(int i = 0 ; i < ret.length ; i++){
			//System.out.println(sum.getData(aa[i]).getName() + "");
			String ss = "";
			for(int j = 0 ; j < ret.length ; j++){
				//if(dsm.getDependency(i, j)){
				if(ret[i][j]){
					ss += " 1";
				}else{
					ss += " .";
				}
			}
			System.out.println(ss);
		}
		*/
		
		
		
		//dd.dsmData.GetItem(0);		
		//System.out.println(dc.clsxData.child.get(1).child.size());
		
		/*
		Clsx c = new Clsx("root");
		c.item = new Clsx[4];
		c.item[0] = new Clsx("aaaa");
		c.item[1] = new Clsx("aaaa");
		c.item[2] = new Clsx("aaaa");
		c.item[3] = new Clsx("aaaa");
		
		c.item[0].item = new Clsx[2];
		c.item[0].item[0] = new Clsx("fdsfds");
		c.item[0].item[1] = new Clsx("fd");


		Data clsxData1 = dataController.loadClsx(c);
		Clsx makec = dataController.makeDataToClsx(clsxData1);
		System.out.println(makec.getName());
		System.out.println(makec.item[0].getName());
		System.out.println(makec.item[1].getName());
		*/
	}

}
