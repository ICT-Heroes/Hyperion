package service;

import java.io.File;
import java.util.ArrayList;

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

/**
 * ClsxService Clsx에 관련된 모든 정보를 제공해주는 클래스
 *
 */
public class ClsxService {
	Document doc;

	private static ClsxService instance;

	public static ClsxService getInstance() {
		if (instance == null) {
			instance = new ClsxService();
		}
		return instance;
	}

	/**
	 * clsx형식의 파일을 읽어온 뒤 clsx모델에 맞게 parsing 하여 clsx모델을 만든다.
	 * 
	 * @param file
	 *            읽어온 파일
	 * @return clsx clsx 객체를반환
	 */
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
			e.printStackTrace();
		}

		return clsx;
	}

	/**
	 * 파일을 읽어와 clsx 형식에 맞게 parsing 한 뒤 정해진 경로에 저장한다.
	 * 
	 * @param filePath
	 *            파일을 저장할 경로
	 * @param clsx
	 *            저장할 clsx 파일
	 */
	public void WriteFile(String filePath, Clsx clsx) {
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
			StreamResult result = new StreamResult(new File(filePath));
			// StreamResult result = new StreamResult(System.out);
			transformer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 파일을 parsing하여 clsx 객체에 저장한다.
	 * 
	 * @param clsx
	 *            저장하기 위한 clsx 객체
	 * @param node
	 *            parsing한 노드
	 */
	private void makeNode(Clsx clsx, Node node) {
		// node의 이름과 item의 크기를 할당
		clsx.setName(node.getAttributes().item(0).getTextContent());
		for (int i = 0; i < node.getChildNodes().getLength(); i++) {
			makeNextNode(clsx, node.getChildNodes());
		}
	}

	/**
	 * 파일을 parsing하여 clsx 객체에 저장한다.
	 * 
	 * @param clsx
	 *            저장하기 위한 clsx 객체
	 * @param nodeList
	 *            node에서 꺼내온 하위 nodeList
	 */
	private void makeNextNode(Clsx clsx, NodeList nodeList) {
		clsx.setItem(new ArrayList<>());
		for (int k = 0; k < nodeList.getLength() / 2; k++) {
			clsx.getItem().add(new Clsx());
		}
		for (int i = 0; i < nodeList.getLength() / 2; i++) {
			makeNode(clsx.getItem().get(i), nodeList.item(2 * i + 1));
		}
	}

	/**
	 * clsx에서 하나씩 꺼내서 file구조로 만든다.
	 * 
	 * @param clsx
	 *            file로 만들 clsx
	 * @param element
	 *            clsx에서 꺼낸 노드
	 */
	private void makeWriteNode(Clsx clsx, Element element) {
		if (rightItem(clsx)) {
			Element item = doc.createElement("item");
			item.setAttribute("name", clsx.getName());
			element.appendChild(item);
		} else {
			Element group = doc.createElement("group");
			group.setAttribute("name", clsx.getName());
			element.appendChild(group);
			for (int i = 0; i < clsx.getItem().size(); i++) {
				makeWriteNode(clsx.getItem().get(i), group);
			}
		}
	}

	/**
	 * 파일을 만들 때 item인지 group인지 확인하기 위한 장치
	 * 
	 * @param clsx
	 *            확인할 clsx
	 * @return item이면 true, group이면 false
	 */
	private static boolean rightItem(Clsx clsx) {
		if (clsx.getItem() == null) {
			return true;
		} else
			return false;

	}

}
