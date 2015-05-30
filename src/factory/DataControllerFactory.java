package factory;

import java.util.HashMap;
import java.util.Map;

import controller.DataController;


public class DataControllerFactory {
	private static Map<String, DataController> dataControllerMap = new HashMap<>(); 
	
	public static DataController getInstance(){
		return getInstance("default");
	}
	
	public static DataController getInstance(String windowName){
		if(!dataControllerMap.containsKey(windowName))
			dataControllerMap.put(windowName, new DataController());
		return dataControllerMap.get(windowName);
	}
}
