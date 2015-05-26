package controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import model.Dsm;
import model.DsmModel;

public class DsmController {
	Dsm dsm;
	
	public DsmController(Dsm dsm) {
		this.dsm = dsm;
	}
	
	public void readFile(File file) {
		try {
			Scanner scanner = new Scanner(file);
			int number = scanner.nextInt();
			
			// Make DsmModel
			for (int i = 0; i < number; i++) {
				dsm.addModel(new DsmModel(i));
			}
			
			for (int i = 0; i < number; i++) {
				DsmModel model = new DsmModel(i);
				for (int j = 0; j < number; j++) {
					if (scanner.hasNextInt()) {
						int val = scanner.nextInt();
						if (val == 1) {
							model.addModel(dsm.getModel(j));
							System.out.println("추가됨");
						}
					}
					
				}
			}
			
			//models = new LinkedList<String>();
			for (int i=0; i < number; i++) {
				if (scanner.hasNextLine())
					dsm.getModel(i).setName(scanner.nextLine());
			}
			
			scanner.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void writeFile() {
		
	}
	
	public void addModel(String name) {
		
	}
	
	public void printDependency() {
		int number = dsm.getNumber();
		for (int i = 0; i < number; i++) {
			/*for (int j = 0; j < number; j++) {
				if (dsm.getModel(i).isDependent(j)) {
					System.out.print("O ");
				}
				else {
					System.out.print("X ");
				}
			}
			System.out.println("");*/
			
			dsm.getModel(i).
		}
	}
	
	public void printModels() {
		int number = dsm.getNumber();
		for (int i = 0; i < number; i++) {
			System.out.println(dsm.getModel(i).getName());
		}
	}

}
