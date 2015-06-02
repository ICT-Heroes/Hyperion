package test.controller;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import model.Clsx;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ClsxTest {
	private static int groupSequence;

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
				Clsx clsxTree = new Clsx();

				// 함수를 통해 트리 구조를 만든다. 처음은 cluster 임으로 root에서부터 시작한다.
				// rootNode
				Node node = doc.getFirstChild().getChildNodes().item(1);
				System.out.println("Minsoo's Test " + node.getNodeName());
				makeNode(clsxTree, node);

				/*
				 * clsxTree.setName(firstNodelist.item(3 * i - 2)
				 * .getAttributes().item(0).getTextContent()); clsxTree.item =
				 * new Clsx[firstNodelist.getLength() / 3]; XPath xpath =
				 * XPathFactory.newInstance().newXPath(); Node node = (Node)
				 * xpath.evaluate("//group/group/item", file,
				 * XPathConstants.NODE);
				 */

				// TagName이 Group인 것들을 List에 전부 집어 넣는다. (순서는 있는데, 상속 관계가 표현이
				// 안된다.)
				// item도 전부 들어간다.
				/*
				 * NodeList grouplist = doc.getElementsByTagName("*");
				 * 
				 * for (int i = 0; i < grouplist.getLength(); i++) { Node
				 * groupNode = grouplist.item(i);
				 * 
				 * String groupName = groupNode.getAttributes().item(0)
				 * .getTextContent();
				 * 
				 * if (groupNode == groupNode.getParentNode()) {
				 * System.out.println(groupNode.getParentNode() .getNodeName() +
				 * " : " + groupNode.getNodeName() + " : " + groupName); } else
				 * {
				 * 
				 * } } // group의 총 갯수만큼 반복한다. for (int i = 0; i <
				 * grouplist.getLength(); i++) { // group으로 들어가서 해당 attribute의
				 * 첫번째 item 즉 name을 가져온다. (한마디로 // // group name) Node groupNode
				 * = grouplist.item(i);
				 * 
				 * String groupName = groupNode.getAttributes().item(0)
				 * .getTextContent();
				 * 
				 * printGroupList(groupName);
				 * 
				 * // 그룹 이름을 출력한 뒤 하위에 item이 있는지 없는지를 체크한다. 없으면 item이라는 것이고 있다면
				 * // group이라는 뜻 if (groupNode.getNodeType() ==
				 * Node.ELEMENT_NODE) { Element groupElmnt = (Element)
				 * groupNode; if (checkHasGroupList(groupElmnt)) { // groupList가
				 * 있다는 것은 Group이라는 것 if (groupNode.hasChildNodes() == false) {
				 * // item의 경우 childnodes가 없기 때문에 true가야한다.
				 * System.out.println("Item을 가지고 있지 않습니다");
				 * System.out.println(); doc.getFirstChild(); } else {
				 * System.out.println("Item입니다"); }
				 * 
				 * } else { // groupList가 없다는것은 Item이라는 것 NodeList itemList =
				 * groupElmnt .getElementsByTagName("item"); for (int j = 0; j <
				 * itemList.getLength(); j++) { Node itemNode =
				 * itemList.item(j); if (itemNode.getNodeType() ==
				 * Node.ELEMENT_NODE) { System.out.println("item name    : " +
				 * (j + 1) + "번째 " + itemList.item(j).getAttributes()
				 * .item(0).getTextContent()); } } } }
				 * 
				 * System.out
				 * .println("---------------------------------------------");
				 * System.out.println(); }
				 */
			} catch (Exception e) {
				System.out.println("파일을 읽을 수가 없습니다");
				e.printStackTrace();
			}
		}
	}

	private static void makeNode(Clsx clsx, Node node) {
		// node의 이름과 item의 크기를 할당
		clsx.setName(node.getAttributes().item(0).getTextContent());
		System.out.println("MyLastTest clsx's node Name : " + clsx.getName());
		for (int i = 0; i < node.getChildNodes().getLength() / 2; i++) {
			makeNextNode(clsx, node.getChildNodes());
		}
	}

	private static void makeNextNode(Clsx clsx, NodeList nodeList) {
		clsx.item = new Clsx[nodeList.getLength() / 2];
		for (int k = 0; k < nodeList.getLength() / 2; k++) {
			clsx.item[k] = new Clsx();
		}
		System.out.println("My LastTest" + "item's Length " + clsx.item.length);
		for (int i = 0; i < nodeList.getLength() / 2; i++) {
			makeNode(clsx.item[i], nodeList.item(2 * i + 1));
		}
	}

	private static boolean checkHasGroupList(Element checkElmnt) {

		NodeList checkList = checkElmnt.getElementsByTagName("group");

		if (checkList.getLength() > 0) {
			return true;
		} else {
			return false;
		}
	}

	private static void printGroupList(String Name) {
		System.out.println("---------- groupNode " + groupSequence
				+ "번째 ------------------");
		System.out.println("Group name :" + Name);
		System.out.println("---------------------------------------------");
		System.out.println();
		groupSequence++;

	}

}
