package test.controller;

import model.Dsm;

import org.junit.Test;

import controller.PartitionController;

public class PartitionTest {
	Dsm dsm;
	PartitionController partitioner;

	@Test
	public void partitionTest() {
		partitioner = new PartitionController();
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
		for (int i = 0; i < dsm.getNumber(); i++) {
			System.out.println(dsm.getName(i));
		}
	}

	public void print() {
		printDependencies();
		printNames();
	}
}
