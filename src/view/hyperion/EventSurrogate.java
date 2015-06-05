 package view.hyperion;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import com.ezware.dialog.task.TaskDialogs;

/**
 *	 UI 컴포넌트간 이벤트 처리를 위한 이벤트 대리자
 *	메서드와 별명, 매개변수의 타입 정보를 바인딩하면
 *	invoke메서드를 사용하여 별명으로 메서드를 호출할 수 있음.
 */
public class EventSurrogate{
	//이벤트 대리자를 소유한 윈도우 클래스
	HyperionWindow owner;
	
	//소유자의 클래스 정보
	Class<?> ownerClsInf;
	
	//바인딩 된 메서드들의 호출 정보
	HashMap<String, Method> mthdList = new HashMap<String, Method>();
	
	/**
	 * 써로게이트 생성자
	 * @param	wnd 소유자가 될 TitanWindow 객체
	 */
	EventSurrogate(HyperionWindow wnd){
		owner = wnd;
		
		try{
			//클래스 정보 추출
			ownerClsInf = Class.forName("view.hyperion.HyperionWindow");
		}catch(ClassNotFoundException cnfe){
			//정보 추출에 실패하면 예외 출력
			TaskDialogs.showException(cnfe);
		}
	}
	
	/**
	 * 메서드 바인딩 메서드
	 * 바인딩하려는 메서드 이름과 이벤트 이름 그리고 파라미터의 타입 정보를 전달해준다.
	 * <pre><b>Example</b><br />
	 * class A{
	 * 	void callme(Integer b){
	 * 		//something to do
	 * 	}
	 * 	public A(){
	 * 		surrogate.bind("callme", "callmebaby", new Class[]{Integer.class});
	 * 		surrogate.invoke("callmebaby", new Object[]{new Integer(30)}); 
	 * 	}
	 * }
	 * </pre>
	 * <b>주의! 이 메서드로 바인딩하는 경우 접근제한자를 무시하므로
	 * non-public 메서드에 대해 바인딩 하는 경우 주의가 필요하다</b><br/><br/>
	 * 
	 * @param methodName	메서드 이름
	 * @param evtName		이벤트 이름
	 * @param paramTypes	파라미터 타입 정보 배열
	 */
	public void bind(String methodName, String evtName, Class<?>[] paramTypes){
		try{
			//정의된 모든 메서드를 추출
			Method[] mtds = ownerClsInf.getDeclaredMethods();
			
			//메서드 리스트를 탐색하면서 바인딩할 메서드가 있는지 탐색
			for(Method m : mtds){
				//일치하는 메서드를 찾음
				if(m.getName().equals(methodName)){
					//파라미터 타입이 일치하는 메서드가 존재하는지 확인
					//왜냐하면 다형성때문에 같은 이름의 메서드지만 파라미터를
					//받아들이지 않는 메서드가 존재할 수 있기 때문이다.
					try{
						Method mtd = ownerClsInf.getDeclaredMethod(methodName, paramTypes);
						mtd.setAccessible(true);
						mthdList.put(evtName, mtd);
					}catch(SecurityException | NoSuchMethodException e){
						//일치하는 메서드가 없거나 보안예외로 리플렉션이 불가능						
					}
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
