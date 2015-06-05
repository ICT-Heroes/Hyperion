package test.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

import controller.DataController;

/**
 * 클러스터링까지한 Data가 잘 작동하는지에 대한 Test
 *
 */
public class DataClsxTest {

	DataController dataController;
	File readDsmFile;
	File readClsxFile;

	@Before
	public void setup() {
		dataController = new DataController();
		readDsmFile = new File("data/test.dsm");
		readClsxFile = new File("data/testReadFile.clsx");
		dataController.loadDsm(readDsmFile);
	}

	@Test
	public void checkData() {
		assertThat("test", is(dataController.data.getDependencyData(0)));
	}

}
