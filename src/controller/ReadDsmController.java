package controller;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import model.Dsm;


public class ReadDsmController {
	ArrayList<Dsm> dsms;
	
	public ReadDsmController() {
		dsms = new ArrayList<Dsm>();
	}
	
	public int getNumber() {
		return dsms.size();
	}
	
	public Dsm getDsm(int index) {
		Optional<Dsm> find = dsms.stream()
			.filter(dsm -> dsm.getIndex() == index)
			.findFirst();

		if (find.isPresent())
			return find.get();	// found
		else
			return null;		// cannot found
	}

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

			// FIXME: �떎�젣 �뜲�씠�꽣�뿉�뒗 怨듬갚�씠 �뾾�뒗�뜲 nextLine�뿉�꽌 �븯�굹 �굹�샂. 洹몃옒�꽌 洹멸구 �젣嫄�.
			scanner.nextLine();
			
			// Set name
			for (int i=0; i < number; i++) {
				if (scanner.hasNextLine())
					dsms.get(i).setName(scanner.nextLine());
				//System.out.println(i + "th dsm: " + dsms.get(i).getName());
			}
			
			scanner.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void writeFile(File file) {
		try {
			//BufferedWriter out = new BufferedWriter(new FileWriter("src/res/out.txt"));
			
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			
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

	public void insertItem(int index, String name) {
		dsms.add(new Dsm(index, name));
	}

	public void deleteItem(int index) {
		// remove from dsm array
		this.dsms.removeIf((Dsm d) -> { return d.getIndex() == index; });
		
		// remove dependency for Item in each dsm  
		for (Dsm dsm : dsms) {
			dsm.removeDependency(index);
		}
	}

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