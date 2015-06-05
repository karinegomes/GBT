/*import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

public class Principal2 {

	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		dbf.setValidating(true);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setNamespaceAware(true);
		dbf.setIgnoringComments(true);
	    
		Document doc = db.parse("BrazilInstance3.xml");	
		
		Element raiz = doc.getDocumentElement();
		System.out.println("O elemento raiz é: " + raiz.getNodeName());
		
		removeWhitespaceNodes(raiz);
		
		NodeList filhosDaRaiz = raiz.getChildNodes();
		
		for(int i = 0; i < filhosDaRaiz.getLength(); i++) {
			Node node = filhosDaRaiz.item(i);
			System.out.println(node.getNodeName());
		}
		
		System.out.println("-----------");
		
		NodeList resources = raiz.getElementsByTagName("Resource");
		
		for(int j = 0; j < resources.getLength(); j++) {
			Element resource = (Element) resources.item(j);
			Attr id = resource.getAttributeNode("Id");
			
			if(id != null) {
				System.out.println(id.getNodeValue());
			}
			else {
				break;
			}
			
			NodeList listNames = resource.getElementsByTagName("Name");
			Node name = listNames.item(0).getFirstChild();
			
			if(name != null) {
				System.out.println("Name: " + name.getNodeValue());
			}
			
			NodeList listResourceTypes = resource.getElementsByTagName("ResourceType");
			Element resourceType = (Element) listResourceTypes.item(0);
			Attr reference = resourceType.getAttributeNode("Reference");
			
			System.out.println("Reference: " + reference.getNodeValue());
			//Node resourceType = listResourceTypes.item(0).getFirstChild();
			
			
		}
		
		Node bla1 = raiz.getLastChild();
		
		System.out.println(bla1.getNodeName());
		
		System.out.println(instances.getLength());
		
		Node filho1 = instances.item(0);
		
		System.out.println(filho1.getTextContent());
		
		NodeList timeGroups = raiz.getElementsByTagName("TimeGroups");
		System.out.println(timeGroups.getLength());
		
		for (int i = 0; i < timeGroups.getLength(); i++) {			 
			//como cada elemento do NodeList é um nó, precisamos fazer o cast
			Element contato = (Element) timeGroups.item(i);
			
			System.out.println(contato.getFirstChild());
			Attr id = contato.getAttributeNode("Id");
			
			System.out.println("Contato id: " + id.getNodeValue());
		}
		
	}
	
	public static void removeWhitespaceNodes(Element e) {
		NodeList children = e.getChildNodes();
		for (int i = children.getLength() - 1; i >= 0; i--) {
			Node child = children.item(i);
			if (child instanceof Text && ((Text) child).getData().trim().length() == 0) {
				e.removeChild(child);
			}
			else if (child instanceof Element) {
				removeWhitespaceNodes((Element) child);
			}
		}
	}
	
}*/