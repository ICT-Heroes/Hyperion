package main;
import java.io.File;

import model.Data;
import controller.DataController;
import view.titan.TitanTreeContainer;
import view.titan.TitanWindow;

class Test{
	private static DataController dataCtrl;	
	public DataController getController(){
		if(dataCtrl == null){
			dataCtrl = new DataController();
		}
		return dataCtrl;
	}
}
public class Main{
	public static void main(String[] args){
		DataController dc = new DataController();
		Data data = dc.LoadDsm(new File("C:\\Users\\hyobin\\Desktop\\titan\\titan\\data dir\\titan\\titan.dsm"));
		
		TitanWindow wnd = new TitanWindow();
		wnd.setDataController(data);
		wnd.attachMenuBar();
		wnd.attachToolBar();
		wnd.pack();
		wnd.setVisible(true);
	}
}
