package view.titan;

import java.util.HashMap;

public class TitanUIEventSurrogateManager{
	static private HashMap<TitanWindow, TitanUIEventSurrogate> surMap = null;
	static private HashMap<Object, TitanWindow> lnkMap = null;
	
	static{
		surMap = new HashMap<TitanWindow, TitanUIEventSurrogate>();
		lnkMap = new HashMap<Object, TitanWindow>();
	}
	
	static public TitanUIEventSurrogate selectSurrogate(TitanWindow wnd){
		System.out.println("Surrogate, Titan");
		return surMap.get(wnd);
	}
	
	static public TitanUIEventSurrogate selectSurrogate(Object obj){
		System.out.println("Surrogate, Object");
		return surMap.get(lnkMap.get(obj));
	}
	
	static public boolean addSurrogate(TitanWindow wnd){
		if(surMap.containsKey(wnd) == true){
			return false;
		}else{
			surMap.put(wnd, new TitanUIEventSurrogate(wnd));
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
