package test.factory;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import controller.DataController;
import factory.DataControllerFactory;

public class DataControllerFactoryTest {
	
	@Test
	public void newDataController(){
		DataController dataInstance = DataControllerFactory.getInstance();
		assertThat(dataInstance, notNullValue());
	}
	
	@Test
	public void sameInstance(){
		DataController dataExistsInstance = DataControllerFactory.getInstance();
		DataController dataNewInstance = DataControllerFactory.getInstance();
		assertThat(dataExistsInstance, is(equalTo(dataNewInstance)));
	}
}
