package test;

import java.io.File;

import model.Dsm;

import org.junit.Before;
import org.junit.Test;

import service.DsmService;
import controller.Partitioner;

public class DsmTest {
	Dsm dsm;
	DsmService controller = new DsmService();
	Partitioner partitioner;

	@Before
	public void setup() {
		File file = new File("src/res/moka.dsm");
		dsm = controller.readFromeFile(file);
	}
	
	@Test
	public void readDsmTest() {
		File file = new File("src/res/moka.dsm");
		dsm = controller.readFromeFile(file);
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
