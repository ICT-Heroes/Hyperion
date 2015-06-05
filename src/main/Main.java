package main;

import view.titan.TitanWindow;
import factory.DataControllerFactory;

public class Main {
	public static void main(String[] args) {
		TitanWindow wnd = new TitanWindow();
		wnd.setDataController(DataControllerFactory.getInstance());
		wnd.attachMenuBar();
		wnd.attachToolBar();
		wnd.pack();
		wnd.setVisible(true);
	}
}