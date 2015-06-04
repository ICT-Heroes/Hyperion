package test;

import java.io.File;

import model.Dsm;

import org.junit.Before;
import org.junit.Test;

import org.hamcrest.CoreMatchers;
import org.junit.matchers.JUnitMatchers; 

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import service.DsmService;
import controller.Partitioner;


public class DsmTest {
	Dsm dsm;
	DsmService service = new DsmService();
	Partitioner partitioner;

	@Before
	public void setup() {
		File file = new File("src/res/moka.dsm");
		dsm = service.readFromeFile(file);
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
	public void partitionTest() {
		partitioner = new Partitioner();
		partitioner.setDsm(dsm);
		
		partitioner.preProcessing();
		partitioner.pathSearching();
	
		printDependencies();
	}
	
	public void printDependencies() {
		for (int i = 0; i < dsm.getNumber(); i++) {
			for (int j = 0; j < dsm.getNumber(); j++) {
				if (dsm.getDependency(i, j))
					System.out.print("O ");
				else
					System.out.print("X ");	
			}
			System.out.println("");
		}
	}
	
	public void printNames() {
		for (int i=0; i<dsm.getNumber(); i++) {
			System.out.println(dsm.getName(i));
		}
	}
	
	public void print() {
		printDependencies();
		printNames();
	}

}
