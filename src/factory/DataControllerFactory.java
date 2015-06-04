package factory;

import java.util.HashMap;
import java.util.Map;

import controller.DataController;

/**
 * DataController객체를 관리하는 Factory이다. windowName에 해당하는 DataController객체가 있으면 해당
 * 객체를 전달해주고 windowName에 해당하는 객체가 없으면 해당 DataController객체를 만든뒤 리턴해준다.
 * 
 * @author Hyperion
 *
 */
public class DataControllerFactory {
	private static Map<String, DataController> dataControllerMap = new HashMap<>();

	public static DataController getInstance() {
		return getInstance("default");
	}

	/**
	 * windowName에 해당하는 객체를 리턴해주는 메서드, 해당하는 객체가 없으면 windowName을 이름으로 하는 객체를 만든 뒤
	 * 해당 객체를 리턴한다.
	 * 
	 * @param windowName
	 *            View의 window에 해당하는 이름. 보통은 default window객체에 해당하는
	 *            DataController가 호출되지만 duplicate 될 경우 새로운 windowName을 가진 객체를
	 *            필요로 한다.
	 * @return windowName에 해당하는 DataController 객체를 리턴한다.
	 */
	public static DataController getInstance(String windowName) {
		if (!dataControllerMap.containsKey(windowName))
			dataControllerMap.put(windowName, new DataController());
		return dataControllerMap.get(windowName);
	}
}
