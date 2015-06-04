package test;

import java.io.File;

import model.Data;
import model.Dsm;
import controller.DataController;

public class DataTest {
	public static void main(String argv[]) {
		DataController dd = new DataController();
		File dsmFile = new File(
				"C:\\Users\\hyobin\\Desktop\\titan\\titan\\data dir\\titan\\titan.dsm");
		File clsxFile = new File(
				"C:\\Users\\hyobin\\Desktop\\titan\\titan\\data dir\\ffff\\titan_ACDC.clsx");
		Data dsmData = dd.LoadDsm(dsmFile);
		Data clsxData = dd.LoadClsx(clsxFile);
		System.out.println(dsmData.ItemCount());
		System.out.println(clsxData.ItemCount());

		for (int i = 0; i < dsmData.ItemCount(); i++) {
			String ss = "";
			for (int j = 0; j < dsmData.ItemCount(); j++) {
				boolean iii = false;
				for (int k = 0; k < dsmData.GetItem(i).GetDependLength(); k++) {
					if (dsmData.GetItem(i).GetDepend(k).name.equals(dsmData
							.GetItem(j).name)) {
						iii = true;
					}
				}
				if (iii) {
					ss += " 1";
				} else {
					ss += " .";
				}

			}
			// System.out.println(ss);
		}

		// Data sum = dd.LoadDsmClsx(dsmFile, clsxFile);
		// System.out.println("itemcount : " + sum.ItemCount());
		for (int i = 0; i < dsmData.DataCount(); i++) {
			// System.out.println(dsmData.GetData(i).name);
		}

		Dsm dsm = dd.MakeDataToDsm(dsmData);
		System.out.println("number : " + dsm.getNumber());
		for (int i = 0; i < dsm.getNumber(); i++) {
			// System.out.println("name : " + dsm.getName(i));
		}

		for (int i = 0; i < dsm.getNumber(); i++) {
			String ss = "";
			for (int j = 0; j < dsm.getNumber(); j++) {
				if (dsm.getDependency(i, j)) {
					ss += " 1";
				} else {
					ss += " .";
				}

			}
			System.out.println(ss);
		}

		// dd.dsmData.GetItem(0);
		// System.out.println(dc.clsxData.child.get(1).child.size());

		/*
		 * Clsx c = new Clsx("root"); c.item = new Clsx[4]; c.item[0] = new
		 * Clsx("aaaa"); c.item[1] = new Clsx("aaaa"); c.item[2] = new
		 * Clsx("aaaa"); c.item[3] = new Clsx("aaaa");
		 * 
		 * c.item[0].item = new Clsx[2]; c.item[0].item[0] = new Clsx("fdsfds");
		 * c.item[0].item[1] = new Clsx("fd");
		 * 
		 * 
		 * dc.LoadClsx(c);
		 * System.out.println(dc.clsxData.child.get(1).child.size());
		 */
	}

}
