  package view.titan;

import java.util.HashMap;

public class EventSurrogateManager{
	static private HashMap<TitanWindow, EventSurrogate> surMap = null;
	static private HashMap<Object, TitanWindow> lnkMap = null;
	
	static{
		surMap = new HashMap<TitanWindow, EventSurrogate>();
		lnkMap = new HashMap<Object, TitanWindow>();
	}
	
	static public EventSurrogate selectSurrogate(TitanWindow wnd){
		System.out.println("Surrogate, Titan");
		return surMap.get(wnd);
	}
	
	static public EventSurrogate selectSurrogate(Object obj){
		System.out.println("Surrogate, Object");
		return surMap.get(lnkMap.get(obj));
	}
	
	static public boolean addSurrogate(TitanWindow wnd){
		if(surMap.containsKey(wnd) == true){
			return false;
		}else{
			surMap.put(wnd, new EventSurrogate(wnd));
			return true;
		}
	}
	
	static public boolean linkSurrogate(Object key, TitanWindow parent){		
		if(lnkMap.containsKey(key)){
			return false;
		}else{
			lnkMap.put(key, parent);
			return true;
		}
	}	
}
