package main;
import factory.DataControllerFactory;
import view.titan.TreeContainer;
import view.titan.TitanWindow;

public class Main{
	public static void main(String[] args){
		TitanWindow wnd = new TitanWindow();
		wnd.setDataController(DataControllerFactory.getInstance());
		wnd.attachMenuBar();
		wnd.attachToolBar();
		wnd.pack();
		wnd.setVisible(true);
	}
}