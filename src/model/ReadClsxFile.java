package model;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ReadClsxFile {
	private static int groupSequence;
	private static int itemSequence;

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

				/*
				 * XPath xpath = XPathFactory.newInstance().newXPath(); Node
				 * node = (Node) xpath.evaluate("//group/group/item", file,
				 * XPathConstants.NODE);
				 */

				// TagName이 Group인 것들을 List에 전부 집어 넣는다. (순서는 있는데, 상속 관계가 표현이
				// 안된다.)
				NodeList grouplist = doc.getElementsByTagName("group");
				// group의 총 갯수만큼 반복한다.
				for (int i = 0; i < grouplist.getLength(); i++) {
					// group으로 들어가서 해당 attribute의 첫번째 item 즉 name을 가져온다. (한마디로
					// group name)
					Node groupNode = grouplist.item(i);

					String groupName = groupNode.getAttributes().item(0)
							.getTextContent();

					printGroupList(groupName);

					// 그룹 이름을 출력한 뒤 하위에 item이 있는지 없는지를 체크한다. 없으면 item이라는 것이고 있다면
					// group이라는 뜻
					if (groupNode.getNodeType() == Node.ELEMENT_NODE) {
						Element groupElmnt = (Element) groupNode;
						if (checkHasGroupList(groupElmnt)) {
							// groupList가 있다는 것은 Group이라는 것
							System.out.println("Item을 가지고 있지 않습니다");
							System.out.println();
						} else {
							// groupList가 없다는것은 Item이라는 것
							NodeList itemList = groupElmnt
									.getElementsByTagName("item");
							for (int j = 0; j < itemList.getLength(); j++) {
								Node itemNode = itemList.item(j);
								if (itemNode.getNodeType() == Node.ELEMENT_NODE) {
									System.out.println("item name    : "
											+ (j + 1)
											+ "번째 "
											+ itemList.item(j).getAttributes()
													.item(0).getTextContent());
								}
							}
						}
					}

					System.out
							.println("---------------------------------------------");
					System.out.println();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
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
