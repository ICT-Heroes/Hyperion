package service;

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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ezware.dialog.task.TaskDialogs;

/**
 * @author NakOh
 *
 */
/**
 * @author NakOh
 *
 */
public class ClsxService {

	static Document doc;

	public Clsx readFile(File file) {
		Clsx clsx = new Clsx();
		try {
			DocumentBuilderFactory docBuildFact = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuild = docBuildFact.newDocumentBuilder();
			doc = docBuild.parse(file);
			doc.getDocumentElement().normalize();
			Node node = doc.getFirstChild().getChildNodes().item(1);
			makeNode(clsx, node);
		} catch (Exception e) {
			TaskDialogs.showException(e);
		}
		return clsx;

	}

	public void writeFile(Clsx clsx) {
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
			TaskDialogs.showException(e);
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

	/**
	 * 
	 * @param clsx
	 * @param node
	 */
	private void makeNode(Clsx clsx, Node node) {

		clsx.setName(node.getAttributes().item(0).getTextContent());
		for (int i = 0; i < node.getChildNodes().getLength() / 2; i++) {
			makeNextNode(clsx, node.getChildNodes());
		}
	}

	/**
	 * @param clsx
	 * @param nodeList
	 */
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
