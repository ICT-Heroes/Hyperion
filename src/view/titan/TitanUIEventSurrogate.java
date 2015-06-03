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
			Method mtd = ownerClsInf.getMethod(methodName, paramTypes);
			mthdList.put(evtName, mtd);
		}catch(NoSuchMethodException | SecurityException e){
			TaskDialogs.showException(e);
		}
		
	}
	
	public void bind(String methodName, String evtName){
		try{
			Method mtd = ownerClsInf.getMethod(methodName);
			mthdList.put(evtName, mtd);
		}catch(NoSuchMethodException | SecurityException e){
			TaskDialogs.showException(e);
		}
	}
	
	public void call(String name, Class[] params){
		Method mtd = mthdList.get(name);
		try{
			mtd.invoke(owner, params);
		}catch(IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e){
			TaskDialogs.showException(e);
		}
	}
	
	public void call(String name){
		Method mtd = mthdList.get(name);
		try{
			mtd.invoke(owner);
		}catch(IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e){
			TaskDialogs.showException(e);
		}
	}
	
	public TitanWindow getOwner(){
		return owner;
	}
}
