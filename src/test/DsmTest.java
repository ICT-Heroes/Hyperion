package test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;

import model.Dsm;

import org.junit.Before;
import org.junit.Test;

import controller.DsmController;

public class DsmTest {
	DsmController controller;
	File file;
	
	@Before
	public void setup() {
		controller = new DsmController();
		file = new File("src/res/titan.dsm");
		controller.readFile(file);
	}
	
	@Test
	public void readFileTest() {
		controller.printDependency();
	}
	
	@Test
	public void getDsmTest() {
		Dsm dsm = controller.getDsm(1);
		assertThat(dsm.getName(), is(equalTo("edu.drexel.cs.rise.titan.action.ExportExcelAction")));
	}
	
	@Test
	public void writeFileTest() {
		int number = controller.getNumber();
		Dsm dsm = controller.getDsm(1);
		
		controller.writeFile();
		
		File newFile = new File("src/res/out.txt");
		controller.readFile(newFile);
		
		int newNumber = controller.getNumber();
		Dsm newDsm = controller.getDsm(1);
		
		assertThat(newNumber, is(equalTo(newNumber)));
		assertThat(newDsm.getName(), is(equalTo(dsm.getName())));
		assertThat(newDsm.getIndex(), is(equalTo(dsm.getIndex())));
	}

}
