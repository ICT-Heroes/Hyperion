package controller;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import model.Clsx;

import org.w3c.dom.Document;
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
