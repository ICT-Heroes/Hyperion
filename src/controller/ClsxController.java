package controller;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import model.Clsx;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ClsxController {
	{
		try {
			// clsx파일을 읽어온다.
			File file = new File("src/res/titan_DRH+ACDC.clsx");
			DocumentBuilderFactory docBuildFact = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuild = docBuildFact.newDocumentBuilder();
			Document doc = docBuild.parse(file);
			doc.getDocumentElement().normalize();

			// clsx 객체를 만든다.
			Clsx clsxTree = new Clsx();

			// 함수를 통해 트리 구조를 만든다. 처음은 cluster 임으로 root에서부터 시작한다.
			// rootNode
			Node node = doc.getFirstChild().getChildNodes().item(1);

			makeNode(clsxTree, node);

		} catch (Exception e) {

		}
	}

	public Clsx readFile(File file) {
		Clsx clsx;
		try {
			DocumentBuilderFactory docBuildFact = DocumentBuilderFactory
					.newInstance();
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

	public void writeFile() {

		File file = new File("src/res/titan_DRH+ACDC.clsx");
		ClsxController clsxcontroller = new ClsxController();
		Clsx clsx = clsxcontroller.readFile(file);
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			// root elements
			Document doc = docBuilder.newDocument();

			Element rootElement = doc.createElement("cluster");
			doc.appendChild(rootElement);

			Element staff = doc.createElement("group");
			rootElement.appendChild(staff);

			Attr attr = doc.createAttribute("name");
			attr.setValue("1");
			staff.setAttributeNode(attr);

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			// StreamResult result = new StreamResult(new File("C:\\file.xml"));
			StreamResult result = new StreamResult(System.out);
			transformer.transform(source, result);

		} catch (Exception e) {

		}

	}

	private void makeNode(Clsx clsx, Node node) {
		// node의 이름과 item의 크기를 할당
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
