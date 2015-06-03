package test;

import java.io.File;

import controller.DataController;
import model.Clsx;
import model.Data;

public class DataTest {
	public static void main(String argv[]) {
		DataController dd = new DataController();
		dd.LoadDsm(new File("C:\\Users\\hyobin\\Desktop\\titan\\titan\\data dir\\titan\\titan.dsm"));
		
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
