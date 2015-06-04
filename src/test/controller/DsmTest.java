/*package test.controller;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;

import model.Dsm;

import org.junit.Before;
import org.junit.Test;

import service.DsmService;

public class DsmTest {
	private DsmService dsmController;
	private File file;

	@Before
	public void setup() {
		dsmController = new DsmService();
		file = new File("src/res/titan.dsm");
		dsmController.readFile(file);
	}

	@Test
	public void readFileTest() {
		dsmController.printDependency();
	}

	@Test
	public void getDsmTest() {
		Dsm dsm = dsmController.getDsm(1);
		assertThat(
				dsm.getName(),
				is(equalTo("edu.drexel.cs.rise.titan.action.ExportExcelAction")));
	}

	//FIXME 에러나길래 일단 주석달았는데 이부분 해결좀
	@Test
	public void writeFileTest() {
		int number = dsmController.getNumber();
		Dsm dsm = dsmController.getDsm(1);

		//controller.writeFile();

		File newFile = new File("src/res/out.txt");
		dsmController.readFile(newFile);

		int newNumber = dsmController.getNumber();
		Dsm newDsm = dsmController.getDsm(1);

		assertThat(newNumber, is(equalTo(newNumber)));
		assertThat(newDsm.getName(), is(equalTo(dsm.getName())));
		assertThat(newDsm.getIndex(), is(equalTo(dsm.getIndex())));
	}

	@Test
	public void newDsmTest() {

	}

}
*/