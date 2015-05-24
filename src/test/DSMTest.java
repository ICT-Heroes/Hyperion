package test;

import static org.junit.Assert.*;

import java.io.File;

import model.DSM;

import org.junit.Before;
import org.junit.Test;

public class DSMTest {
	DSM dsmModel;
	File file;
	
	@Before
	public void setup() {
		dsmModel = new DSM();
		file = new File("src/res/titan.dsm");
	}
	
	@Test
	public void readFileTest() {
		dsmModel.readFile(file);
		assertEquals(35, dsmModel.number);
	}

}
