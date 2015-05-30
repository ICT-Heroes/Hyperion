package controller;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import model.Clsx;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadClsxController {
	
	public ReadClsxController(){}
	
	public Clsx ReadFile(File file){
		Clsx clsx;
		try {
			DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuild = docBuildFact.newDocumentBuilder();
			Document doc = docBuild.parse(file);
			doc.getDocumentElement().normalize();
			clsx = new Clsx();
			Node node = doc.getFirstChild().getChildNodes().item(1);
			makeNode(clsx, node);
			return clsx;
		} catch (Exception e) {

		}
		
		return null;
	}

	private void makeNode(Clsx clsx, Node node) {
		clsx.setName(node.getAttributes().item(0).getTextContent());
		for (int i = 0; i < clsx.item.length; i++) {
			makeNextNode(clsx, node.getChildNodes());
		}
	}

	private void makeNextNode(Clsx clsx, NodeList nodeList) {
		clsx.item = new Clsx[nodeList.getLength() / 2];
		for (int k = 0; k < nodeList.getLength() / 2; k++) {
			clsx.item[k] = new Clsx();
		}
		for (int i = 0; i < nodeList.getLength() / 2; i++) {
			makeNode(clsx.item[i], nodeList.item(i * 2 + 1));
		}
	}
	


}
