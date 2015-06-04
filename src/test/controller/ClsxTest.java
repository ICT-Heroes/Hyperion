package test.controller;

import java.io.File;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import model.Clsx;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ClsxTest {

	public static void main(String argv[]) {

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
				Clsx clsx = new Clsx();

				// 함수를 통해 트리 구조를 만든다. doc.getFirstChild() 는 cluster 이다.

				Node node = doc.getFirstChild().getChildNodes().item(1);
				NodeList nodeList = doc.getElementsByTagName("item");
				for (int i = 0; i < nodeList.getLength(); i++) {
					System.out.println("Root node' item : "
							+ nodeList.item(i).getAttributes().item(0)
									.getTextContent());
				}

				NodeList nodeList2 = doc.getFirstChild().getChildNodes();

				for (int i = 0; i < nodeList2.getLength(); i++) {
					System.out.println("Root node' item : "
							+ nodeList2.item(i).getNodeName());
				}

				System.out.println("Root node: "
						+ doc.getFirstChild().getNodeName());

				makeNode(clsx, node);

				for (int i = 0; i < clsx.getItem().size(); i++) {
					System.out.println("Cluster's Nodes's name "
							+ clsx.getItem().get(i).getName());
				}

				for (int i = 0; i < clsx.getItem().get(0).getItem().size(); i++) {
					System.out.println("Cluster's L0's node's name "
							+ clsx.getItem().get(0).getName());
				}

			} catch (Exception e) {
				System.out.println("파일을 읽을 수가 없습니다");
				e.printStackTrace();
			}
		}
	}

	private static void makeNode(Clsx clsx, Node node) {
		// node의 이름과 item의 크기를 할당
		clsx.setName(node.getAttributes().item(0).getTextContent());
		for (int i = 0; i < node.getChildNodes().getLength() / 2; i++) {
			makeNextNode(clsx, node.getChildNodes());
		}
	}

	private static void makeNextNode(Clsx clsx, NodeList nodeList) {
		clsx.setItem(new ArrayList<>());
		for (int k = 0; k < nodeList.getLength() / 2; k++) {
			clsx.getItem().set(k,new Clsx());
		}
		for (int i = 0; i < nodeList.getLength() / 2; i++) {
			makeNode(clsx.getItem().get(i), nodeList.item(2 * i + 1));
		}
	}

}
