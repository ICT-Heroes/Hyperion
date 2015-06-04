package service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import model.Dsm;

/**
 * Read dsm file and make Dsm list
 * 
 * @param file
 */
public class DsmService {

	private static DsmService instance;

	public static DsmService getInstance() {
		if (instance == null) {
			instance = new DsmService();
		}
		return instance;
	}

	public DsmService() {

	}

	/**
	 * Read dsm file and make Dsm list
	 * 
	 * @param file
	 */
	public Dsm readFromeFile(File file) {
		Dsm dsm = null;

		try {
			Scanner scanner = new Scanner(file);
			int number = scanner.nextInt();

			dsm = new Dsm(number);

			for (int i = 0; i < number; i++) {
				for (int j = 0; j < number; j++) {
					if (scanner.hasNextInt()) {
						int intValue = scanner.nextInt();
						boolean value;
						if (intValue == 1) {
							value = true;
						} else {
							value = false;
						}

						dsm.setDependency(value, i, j);
					}
				}
			}

			scanner.nextLine();

			for (int i = 0; i < number; i++) {
				if (scanner.hasNextLine()) {
					dsm.addName(scanner.nextLine());
				}
			}

			scanner.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return dsm;
	}

	/**
	 * 
	 */
	public void writeFile() {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(
					"src/res/out.txt"));

			out.close();

		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

}
