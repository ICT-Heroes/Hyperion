package test;

import java.io.File;

import model.Dsm;

import org.junit.Before;
import org.junit.Test;

import service.DsmService;
import controller.PartitionController;

public class DsmTest {
	Dsm dsm;
	DsmService controller = new DsmService();
	PartitionController partitioner;

	@Before
	public void setup() {
		File file = new File("src/res/moka.dsm");
		dsm = controller.readFromeFile(file);
		partitioner = new PartitionController();
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
