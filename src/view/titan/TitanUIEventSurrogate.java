package view.titan;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.ezware.dialog.task.TaskDialogs;

public class TitanUIEventSurrogate{
	TitanWindow owner;
	Class<?> ownerClsInf;
	HashMap<String, Method> mthdList = new HashMap<String, Method>();
	
	TitanUIEventSurrogate(TitanWindow wnd){
		owner = wnd;
		
		try{
			ownerClsInf = Class.forName("view.titan.TitanWindow");
		}catch(ClassNotFoundException cnfe){
			TaskDialogs.showException(cnfe);
		}
	}
	
	public void bind(String methodName, String evtName, Class<?>[] paramTypes){
		try{
			Method[] mtds = ownerClsInf.getDeclaredMethods();
			
			for(Method m : mtds){
				System.out.println(m.getName());
				if(m.getName().equals(methodName)){
					Method mtd = ownerClsInf.getDeclaredMethod(methodName, paramTypes);
					mtd.setAccessible(true);
					mthdList.put(evtName, mtd);
					break;
				}
			}
		}catch(Exception e){
			TaskDialogs.showException(e);
		}		
	}
	
	public void bind(String methodName, String evtName){
		try{
			Method[] mtds = ownerClsInf.getDeclaredMethods();
			
			for(Method m : mtds){
				if(m.getName().equals(methodName)){
					if(m.isAccessible() == false){
						m.setAccessible(true);
					}
					mthdList.put(evtName, m);
					break;
				}
			}
		}catch(Exception e){
			TaskDialogs.showException(e);
		}
	}
	
	public Object invoke(String name, Object[] params){
		Method mtd = mthdList.get(name);
		try{
			return mtd.invoke(owner, params);
		}catch(IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e){
			TaskDialogs.showException(e);
		}
		return null;
	}
	
	public Object invoke(String name){
		Method mtd = mthdList.get(name);
		try{
			return mtd.invoke(owner);
		}catch(IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e){
			TaskDialogs.showException(e);
		}
		return null;
	}
}
