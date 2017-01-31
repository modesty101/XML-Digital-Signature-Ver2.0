package com.niko.xml.digsig;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.niko.xml.digsig.FileIO;

public class NewXmlDoc {

	/**
	 * ���ڵ�(Base64) ���� �߰��� XML ������ �����Ѵ�.
	 * 
	 * @param encodedFile
	 * @throws Exception
	 */
	public static void createXML(byte[] encodedFile, String fileName) throws Exception {
		String value = new String(encodedFile, "UTF-8");
		DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuild = docBuildFact.newDocumentBuilder();
		Document doc = null;

		doc = docBuild.newDocument();
		Element rootElement = doc.createElement("company");
		doc.appendChild(rootElement);

		// staff elements
		Element staff = doc.createElement("Staff");
		rootElement.appendChild(staff);

		// set attribute to staff element
		Attr attr = doc.createAttribute("id");
		attr.setValue("1");
		staff.setAttributeNode(attr);

		Element firstname = doc.createElement("firstname");
		firstname.appendChild(doc.createTextNode(value));
		staff.appendChild(firstname);

		Element lastname = doc.createElement("lastname");
		lastname.appendChild(doc.createTextNode("mook kim"));
		staff.appendChild(lastname);

		// nickname elements
		Element nickname = doc.createElement("nickname");
		nickname.appendChild(doc.createTextNode("mkyong"));
		staff.appendChild(nickname);

		// salary elements
		Element salary = doc.createElement("salary");
		salary.appendChild(doc.createTextNode("100000"));
		staff.appendChild(salary);

		TransformerFactory transFac = TransformerFactory.newInstance();
		Transformer transformer = transFac.newTransformer();
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File(fileName));
		transformer.transform(source, result);

		System.out.println("File saved!!");
	}

	/**
	 * ���Ͽ��� ���ڵ��� ���� �����մϴ�.
	 * 
	 * @throws Exception
	 */
	public static void extractEncodingValue(String fileName, String xmlFile) throws Exception {
		byte[] bytes = FileIO.loadFile(fileName);
		createXML(bytes, xmlFile);
	}
}
