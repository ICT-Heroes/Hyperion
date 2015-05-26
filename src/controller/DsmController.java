package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import model.Dsm;


public class DsmController {
	ArrayList<Dsm> dsm;
	
	public DsmController() {
		dsm = new ArrayList<Dsm>();
	}
	
	/**
	 * Read dsm file and make Dsm list
	 * @param file
	 */
	public void readFile(File file) {
		try {
			Scanner scanner = new Scanner(file);
			int number = scanner.nextInt();
			
			// Make DsmModel
			for (int i = 0; i < number; i++) {
				dsm.add(new Dsm(i));
			}
			
			for (int i = 0; i < number; i++) {
				Dsm model = dsm.get(i);
				for (int j = 0; j < number; j++) {
					if (scanner.hasNextInt()) {
						int val = scanner.nextInt();
						if (val == 1) {
							model.addModel(dsm.get(j));
						}
					}
				}
			}
			
			for (int i=0; i < number; i++) {
				if (scanner.hasNextLine())
					dsm.get(i).setName(scanner.nextLine());
			}
			
			scanner.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	/**
	 * 
	 */
	public void writeFile() {
		
	}

	/**
	 * 
	 * @param index
	 * @param name
	 */
	public void insertItem(int index, String name) {
		
	}

	/**
	 * This method consists of two parts.
	 * First is removing from dsm array and 
	 * second is removing dependency
	 * @param index
	 */
	public void deleteItem(int index) {
		// remove from dsm array
		this.dsm.removeIf((Dsm d) -> { return d.getIndex() == index; });
		
		// remove dependency for Item in each dsm  
		for (Dsm dsm : dsm) {
			dsm.removeDependency(index);
		}
	}

	/**
	 * 
	 * @param a
	 * @param b
	 */
	public void changeDependency(int a, int b) {
		if (dsm.get(a).isDependent(b)) {
			dsm.get(a).removeDependency(b);
			dsm.get(b).removeDependency(a);	
		}
		else {
			dsm.get(a).addModel(dsm.get(b));
			dsm.get(b).addModel(dsm.get(a));
		}
	}
	
	public void printDependency() {
		int number = dsm.size();
		for (int i = 0; i < number; i++) {
			for (int j = 0; j < number; j++) {
				if (dsm.get(i).isDependent(j)) {
					System.out.print("O ");
				}
				else {
					System.out.print("X ");
				}
			}
			System.out.println("");
			
			dsm.get(i);
		}
	}

	public void printModels() {
		int number = dsm.size();
		for (int i = 0; i < number; i++) {
			System.out.println(dsm.get(i).getName());
		}
	}

}
