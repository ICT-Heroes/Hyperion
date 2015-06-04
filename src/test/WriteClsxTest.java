package test;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import model.Clsx;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import service.ClsxService;

public class WriteClsxTest {

	private static Document doc;

	public static void main(String[] args) {
		ClsxService clsxController = new ClsxService();
		File file = new File("file.xml");
		Clsx clsx = new Clsx();
		clsx = clsxController.readFile(file);
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			// root elements
			doc = docBuilder.newDocument();
			// cluster는 객체가 가지고 있지 않기 떄문에 수동으로 추가해준다.
			Element rootElement = doc.createElement("cluster");
			doc.appendChild(rootElement);
			makeWriteNode(clsx, rootElement);

			/*
			 * // 이 후부터는 객체 안의 정보를 순서대로 꺼내온다. Element group =
			 * doc.createElement("group"); //
			 * group.appendChild(doc.createTextNode(" "));
			 * rootElement.appendChild(group); // group의 이름을 지정
			 * group.setAttribute("name", clsx.getName());
			 * 
			 * Element item = doc.createElement("item"); //
			 * item.setAttribute("name", );
			 * 
			 * Element item2 = doc.createElement("item");
			 * item2.setAttribute("name", clsx.getName());
			 * group.appendChild(item); group.appendChild(item2);
			 */
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("file.xml2"));
			// StreamResult result = new StreamResult(System.out);
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error");
		}

	}

	/**
	 * 
	 * @param clsx
	 * @param element
	 */
	private static void makeWriteNode(Clsx clsx, Element element) {
		if (rightItem(clsx)) {
			Element item = doc.createElement("item");
			item.setAttribute("name", clsx.getName());
			element.appendChild(item);
		} else {
			Element group = doc.createElement("group");
			group.setAttribute("name", clsx.getName());
			element.appendChild(group);
			for (int i = 0; i < clsx.item.length; i++) {
				makeWriteNode(clsx.item[i], group);
			}
		}
	}

	private static boolean rightItem(Clsx clsx) {
		if (clsx.item == null) {
			return true;
		} else
			return false;

	}
}
