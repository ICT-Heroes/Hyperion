package test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;

import model.Dsm;

import org.junit.Before;
import org.junit.Test;

import controller.Partitioner;
import controller.ReadDsmController;

public class DsmTest {
	Dsm dsm;
	ReadDsmController controller = new ReadDsmController();
	Partitioner partitioner;
	
	@Before
	public void setup() {
		File file = new File("src/res/titan.dsm");
		dsm = controller.readFromeFile(file);
		partitioner = new Partitioner();
		partitioner.setDsm(dsm);
	}
	
	@Test
	public void readDsmTest() {
		dsm.print();
	}
	
	@Test
	public void partitionTest() {
		partitioner.preProcessing();
		partitioner.postProcessing();
		dsm.print();
		for (int val : partitioner.getSizeList()) {
			System.out.println(val);
		}
	}

}
