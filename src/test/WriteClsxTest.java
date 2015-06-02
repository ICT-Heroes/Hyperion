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

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import controller.ClsxController;

public class WriteClsxTest {
	public static void main(String[] args) {
		ClsxController clsxController = new ClsxController();
		File file = new File("src/res/titan_DRH+ACDC.clsx");
		Clsx clsx = clsxController.readFile(file);
		try {

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			// root elements
			Document doc = docBuilder.newDocument();

			Element rootElement = doc.createElement("cluster");
			doc.appendChild(rootElement);

			Element group = doc.createElement("group");
			group.appendChild(doc.createTextNode(" "));
			rootElement.appendChild(group);

			Attr attr = doc.createAttribute("name");
			attr.setValue(clsx.getName());
			group.setAttributeNode(attr);

			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,
					"yes");
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("file.xml"));
			// StreamResult result = new StreamResult(System.out);
			transformer.transform(source, result);

		} catch (Exception e) {
			System.out.println(e);
			System.out.println("Error");
		}

	}
}
