package service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Element;

import model.Clsx;
import model.Dsm;

/**
 * Read dsm file and make Dsm list
 * 
 * @param file
 */
public class DsmService {

	/**
	 * Read dsm file and make Dsm list
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
						int intValue= scanner.nextInt();
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
	 * 파일을 읽어와 dsm 형식에 맞게 parsing 한 뒤 정해진 경로에 저장한다.
	 * 
	 * @param filePath
	 *            파일 이름과 확장자를 포함한 절대 경로.
	 * @param dsm
	 *            저장할 dsm 파일
	 */
	public void WriteFile(String filePath, Dsm dsm) {
		try {
            BufferedWriter out = new BufferedWriter(new FileWriter(filePath));
            int number = dsm.getNumber();
            
            out.write(number + "");
            out.newLine();
            
            for (int i = 0; i < number; i++) {
				for (int j = 0; j < number; j++) {
					if (dsm.getDependency(i, j)) {
						out.write("1 ");
					}
					else {
						out.write("0 ");
					}
				}
				out.newLine();
			}
            
            for (String name : dsm.getNames()) {
				out.write(name);
				out.newLine();
			}
            
            out.close();
            
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
