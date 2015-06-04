package test;

import java.io.File;

import model.Data;
import model.Dsm;

import org.junit.Before;
import org.junit.Test;

import controller.DataController;

public class DataTest {
	public static void main(String argv[]) {
		DataController dataController = new DataController();
		File dsmFile = new File("C:\\Users\\hyobin\\Desktop\\titan\\titan\\data dir\\titan\\titan.dsm");
		File clsxFile = new File("C:\\Users\\hyobin\\Desktop\\titan\\titan\\data dir\\ffff\\titan_ACDC.clsx");
		Data dsmData = dataController.loadDsm(dsmFile);
		Data clsxData = dataController.loadClsx(clsxFile);
		System.out.println(dsmData.getItemCount());
		System.out.println(clsxData.getItemCount());

		
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
		
		//Data sum = dd.LoadDsmClsx(dsmFile, clsxFile);
		//System.out.println("itemcount : " + sum.ItemCount());
		for(int i = 0 ; i < dsmData.getDataCount() ; i++){
			//System.out.println(dsmData.GetData(i).getName());
		}
		
		Dsm dsm = dataController.makeDataToDsm(dsmData);
		System.out.println("number : " + dsm.getNumber());
		for(int i = 0 ; i < dsm.getNumber() ; i++){
			//System.out.println("name : " + dsm.getName(i));
		}
		
		for(int i = 0 ; i < dsm.getNumber() ; i++){
			String ss = "";
			for(int j = 0 ; j < dsm.getNumber() ; j++){
				if(dsm.getDependency(i, j)){
					ss += " 1";
				}else{
					ss += " .";
				}
				
			}
			System.out.println(ss);
		}
		
		
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


		dc.LoadClsx(c);
		System.out.println(dc.clsxData.child.get(1).child.size());
		*/
	}

}
