package test.controller;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.util.Comparator;

import model.Data;
import model.Dsm;

import org.junit.Before;
import org.junit.Test;

import controller.DataController;

public class DataControllerTest {
	private DataController dataController;
	private File dsmFile, clsxFile;
	
	@Before
	public void setup(){
		dataController = new DataController();
		dsmFile = new File("src/res/titan.dsm");
		clsxFile = new File("src/res/titan_DRH+ACDC.clsx");		
		dataController.loadDsm(dsmFile);
		System.out.println("Root Data : " + dataController.getRootData());
		//for(Data childData : dataController.getRootData().getChild()){
		//	System.out.println(childData);
		//}
		dataController.loadClsx(clsxFile);
	}
	
	@Test
	public void sort(){
		String testString = "edu.drexel.cs.rise.titan.ui.ClusterViewer";
		Data beforeSortedData = dataController.getRootData().findData(testString);
		dataController.sort(testString);
		Data afterSortedData = dataController.getRootData().findData(testString);
		assertThat(beforeSortedData.getChild(), not(equalTo(afterSortedData)));
		for(Data childData : beforeSortedData.getChild() ){
			System.out.println(childData.getName());
		}
		System.out.println("-----after------");
		for(Data childData : afterSortedData.getChild() ){
			System.out.println(childData.getName());
		}
	}
	
	public static void main(String argv[]) {
		DataController dataController = new DataController();
		File dsmFile = new File("src/res/titan.dsm");
		File clsxFile = new File("src/res/titan_DRH+ACDC.clsx");
		dataController.loadDsm(dsmFile);
		dataController.loadClsx(clsxFile);
		/*
		System.out.println(dsmData.countItem());
		System.out.println(clsxData.countItem());

		
		for(int i = 0 ; i < dsmData.countItem() ; i++){
			String ss = "";
			for(int j = 0 ; j < dsmData.countItem() ; j++){
				boolean iii = false;
				for(int k = 0 ; k < dsmData.getItem(i).getDependencyLength() ; k++){
					if(dsmData.getItem(i).getDependencyData(k).getName().equals(dsmData.getItem(j).getName())){
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
		for(int i = 0 ; i < dsmData.countData() ; i++){
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
