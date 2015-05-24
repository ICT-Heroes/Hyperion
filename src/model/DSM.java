package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Scanner;

public class DSM {
	public int number;	// Number of model
	public boolean[][] matrix;
	public LinkedList<String> models; // 자료의 추가, 수정을 빠르게 하기 위해 연결리스트로 함
	
	public void readFile(File file) {
		try {
			Scanner scanner = new Scanner(file);
			number = scanner.nextInt();
			
			matrix = new boolean[number][number];
			for (int i = 0; i < number; i++) {
				for (int j = 0; j < number; j++) {
					if (scanner.hasNextInt()) {
						/*if (scanner.nextInt() == 0)
							matrix[i][j] = false;
						else if (scanner.nextInt() == 1)
							matrix[i][j] = true;
						
						if (matrix[i][j])
							System.out.print("O ");
						else
							System.out.print("X ");*/
						System.out.print(scanner.nextInt() + " ");
					}
				}
				System.out.println("");
			}
			
			models = new LinkedList<String>();
			for (int i=0; i < number; i++) {
				if (scanner.hasNextLine())
					models.add(scanner.nextLine());
				System.out.println(models.get(i));
			}
			
			
			scanner.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
/*		FileReader fileReader;
		try {
			fileReader = new FileReader(file);
			BufferedReader reader = new BufferedReader(fileReader);
			
			String line = null;
			
			// Read the number of model
			line = reader.readLine();
			number = Integer.valueOf(line);
			System.out.println(number);
			
			
			for (int i = 0; i < number; i++) {
				for (int j = 0; j < number; j++) {
					//System.out.println(reader.read());
				}
			}
			
			reader.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
	}
}
