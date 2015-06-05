package main;


import view.hyperion.HyperionWindow;
import factory.DataControllerFactory;

public class Main {
	public static void main(String[] args) {
		HyperionWindow wnd = new HyperionWindow();
		wnd.setDataController(DataControllerFactory.getInstance());
		wnd.attachMenuBar();
		wnd.attachToolBar();
		wnd.pack();
		wnd.setVisible(true);
	}
}