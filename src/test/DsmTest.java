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
		File file = new File("src/res/moka.dsm");
		dsm = controller.readFromeFile(file);
		partitioner = new Partitioner();
		partitioner.setDsm(dsm);
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
	
	
	@Test
	public void readDsmTest() {
	}
	
	@Test
	public void partitionTest() {
		partitioner.preProcessing();
	
		partitioner.pathSearching();
	
		partitioner.postProcessing();
	}

}
