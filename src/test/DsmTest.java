package test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;

import model.Dsm;

import org.junit.Before;
import org.junit.Test;

import service.DsmService;


public class DsmTest {
	Dsm dsm;
	DsmService service = new DsmService();

	@Before
	public void setup() {
		File file = new File("src/res/moka.dsm");
		dsm = service.readFromeFile(file);
	}
	
	@Test
	public void readDsmTest() {
		
	}
	
	@Test
	public void writeDsmTest() {
		String path = "src/res/titan.dsm";
		File file = new File(path);
		Dsm dsmOriginal = service.readFromeFile(file);
		
		String path2 = "src/res/writetest.dsm";
		service.WriteFile(path2, dsmOriginal);
		File file2 = new File(path2);
		Dsm dsmTest = service.readFromeFile(file2);
		
		assertThat(dsmOriginal.equals(dsmTest), is(true));
	}
	
	@Test
	

}
