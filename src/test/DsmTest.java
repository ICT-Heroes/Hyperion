package test;

import java.io.File;

import model.Dsm;

import org.junit.Before;
import org.junit.Test;

import controller.DsmController;

public class DsmTest {
	Dsm dsm;
	DsmController controller;
	File file;
	
	@Before
	public void setup() {
		dsm = new Dsm();
		controller = new DsmController(dsm);
		
		file = new File("src/res/titan.dsm");
		
	}
	
	@Test
	public void readFileTest() {
		controller.readFile(file);
		controller.printDependency();
	}

}
