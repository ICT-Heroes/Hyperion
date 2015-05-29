package controller;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import model.Clsx;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadClsxController {
	
	public Clsx clsx;
	
	public ReadClsxController(File f)
	{
		try {
			DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuild = docBuildFact.newDocumentBuilder();
			Document doc = docBuild.parse(f);
			doc.getDocumentElement().normalize();
			clsx = new Clsx();
			Node node = doc.getFirstChild().getChildNodes().item(1);
			makeNode(clsx, node);
			
		} catch (Exception e) {

		}
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
	
	public Clsx GetItem(Clsx c, int index){
		int curIndex = index;
		if(GetItemLength(c) < index){
			return new Clsx("null");
		}else{
			for(int i = 0 ; i < c.item.length ; i ++){
				curIndex -= GetItemLength(c.item[i]);
				if(curIndex < 0){
					curIndex += GetItemLength(c.item[i]);
					return GetItem(c.item[i], curIndex);
				}else if(curIndex == 0){
					return c.item[i];
				}
			}
		}
		return new Clsx();
	}
	
	public int GetItemLength(Clsx c){
		int length = 0;
		int ret = 0;
		if(c.item != null){
			length = c.item.length;
		}
		if(length == 0){
			ret = 1;
		}else{
			ret = length;
			for(int i = 0 ; i < length ; i ++){
				ret += GetItemLength(c.item[i]);
			}
		}
		return ret;
	}

}
