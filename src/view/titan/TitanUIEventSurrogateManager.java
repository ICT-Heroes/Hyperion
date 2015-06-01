package view.titan;

import java.util.HashMap;

public class TitanUIEventSurrogateManager{
	static private HashMap<TitanWindow, TitanUIEventSurrogate> surMap = null;
	
	static{
		surMap = new HashMap<TitanWindow, TitanUIEventSurrogate>();
	}
	
	static public TitanUIEventSurrogate selectSurrogate(TitanWindow wnd){
		return surMap.get(wnd);
		
	}
	
	static public boolean addSurrogate(TitanWindow wnd){
		if(surMap.containsKey(wnd) == true){
			return false;
		}else{
			surMap.put(wnd, new TitanUIEventSurrogate(wnd));
			return true;
		}
	}
}
