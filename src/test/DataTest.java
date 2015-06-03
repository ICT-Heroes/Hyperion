package test;

import java.io.File;

import model.Dsm;

import org.junit.Before;
import org.junit.Test;

import controller.ReadDsmController;

public class DataTest {
	Dsm dsm;
	ReadDsmController controller = new ReadDsmController();
	
	@Before
	public void setup() {
		File file = new File("src/res/moka.dsm");
		dsm = controller.readFromeFile(file);
	}
	
	@Test
	public void readDsmTest() {
		dsm.print();
	}

}
