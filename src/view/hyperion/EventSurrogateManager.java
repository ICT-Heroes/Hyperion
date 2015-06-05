  package view.hyperion;

import java.util.HashMap;

public class EventSurrogateManager{
	static private HashMap<HyperionWindow, EventSurrogate> surMap = null;
	static private HashMap<Object, HyperionWindow> lnkMap = null;
	
	static{
		surMap = new HashMap<HyperionWindow, EventSurrogate>();
		lnkMap = new HashMap<Object, HyperionWindow>();
	}
	
	static public EventSurrogate selectSurrogate(HyperionWindow wnd){
		System.out.println("Surrogate, Titan");
		return surMap.get(wnd);
	}
	
	static public EventSurrogate selectSurrogate(Object obj){
		System.out.println("Surrogate, Object");
		return surMap.get(lnkMap.get(obj));
	}
	
	static public boolean addSurrogate(HyperionWindow wnd){
		if(surMap.containsKey(wnd) == true){
			return false;
		}else{
			surMap.put(wnd, new EventSurrogate(wnd));
			return true;
		}
	}
	
	static public boolean linkSurrogate(Object key, HyperionWindow parent){		
		if(lnkMap.containsKey(key)){
			return false;
		}else{
			lnkMap.put(key, parent);
			return true;
		}
	}	
}
