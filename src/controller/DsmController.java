package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import model.Dsm;


public class DsmController {
	ArrayList<Dsm> dsms;
	
	public DsmController() {
		dsms = new ArrayList<Dsm>();
	}
	
	public int getNumber() {
		return dsms.size();
	}
	
	public Dsm getDsm(int index) {
		Optional<Dsm> find = dsms.stream()
			.filter(dsm -> dsm.getIndex() == index)
			.findFirst();

		// FIXME
		if (find.isPresent())
			return find.get();	// found
		else
			return null;		// cannot found
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
				dsms.add(new Dsm(i));
			}
			
			// Set dependency
			for (int i = 0; i < number; i++) {
				Dsm model = dsms.get(i);
				for (int j = 0; j < number; j++) {
					if (scanner.hasNextInt()) {
						int val = scanner.nextInt();
						if (val == 1) {
							model.addModel(dsms.get(j));
						}
					}
				}
			}

			// FIXME: 실제 데이터에는 공백이 없는데 nextLine에서 하나 나옴. 그래서 그걸 제거.
			scanner.nextLine();
			
			// Set name
			for (int i=0; i < number; i++) {
				if (scanner.hasNextLine())
					dsms.get(i).setName(scanner.nextLine());
				System.out.println(i + "th dsm: " + dsms.get(i).getName());
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
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter("src/res/out.txt"));
			
			int number = dsms.size();
			out.write(number+""); 
			out.newLine();
			
			for (int i = 0; i < number  ; i++) {
				for (int j = 0; j < number; j++) {
					if (dsms.get(i).isDependent(j)) 
						out.write(1 + " ");
					else
						out.write(0 + " ");
				}
				out.newLine();
			}
			
			for (int i = 0; i < number; i++) {
				out.write(dsms.get(i).getName());
				out.newLine();
			}
			
			out.close();
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param index
	 * @param name
	 */
	public void insertItem(int index, String name) {
		dsms.add(new Dsm(index, name));
	}

	/**
	 * 
	 * @param index
	 */
	public void deleteItem(int index) {
		// remove from dsm array
		this.dsms.removeIf((Dsm d) -> { return d.getIndex() == index; });
		
		// remove dependency for Item in each dsm  
		for (Dsm dsm : dsms) {
			dsm.removeDependency(index);
		}
	}

	/**
	 * 
	 * @param a
	 * @param b
	 */
	public void changeDependency(int a, int b) {
		if (dsms.get(a).isDependent(b)) {
			dsms.get(a).removeDependency(b);
			dsms.get(b).removeDependency(a);	
		}
		else {
			dsms.get(a).addModel(dsms.get(b));
			dsms.get(b).addModel(dsms.get(a));
		}
	}
	
	public void printDependency() {
		int number = dsms.size();
		for (int i = 0; i < number; i++) {
			for (int j = 0; j < number; j++) {
				if (dsms.get(i).isDependent(j)) {
					System.out.print("O ");
				}
				else {
					System.out.print("X ");
				}
			}
			System.out.println("");
			
			dsms.get(i);
		}
	}

	public void printModels() {
		int number = dsms.size();
		for (int i = 0; i < number; i++) {
			System.out.println(dsms.get(i).getName());
		}
	}

}
