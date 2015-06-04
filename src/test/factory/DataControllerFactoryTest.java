package test.factory;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import controller.DataController;
import factory.DataControllerFactory;

public class DataControllerFactoryTest {
	
	@Test
	public void getInstance(){
		//Assert newDataController is not null
		DataController dataInstance = DataControllerFactory.getInstance();
		assertThat(dataInstance, notNullValue());
		
		//Assert existsInstance is same instance with newInstance
		DataController dataExistsInstance = DataControllerFactory.getInstance();
		DataController dataNewInstance = DataControllerFactory.getInstance();
		assertThat(dataExistsInstance, is(equalTo(dataNewInstance)));
	}
}
