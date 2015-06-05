import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

public class Parser {
	
	Element raiz;
	
	Parser(String xml) throws ParserConfigurationException, SAXException, IOException {
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder        db  = dbf.newDocumentBuilder();
		
		dbf.setValidating(true);
		dbf.setIgnoringElementContentWhitespace(true);
		dbf.setNamespaceAware(true);
		dbf.setIgnoringComments(true);
	    
		Document doc = db.parse(xml);
		this.raiz = doc.getDocumentElement();
		
		removeWhitespaceNodes(raiz);
		
	}
	
	public List<String> recuperarProfessores() {
		
		List<String> professores = new ArrayList<String>();
		NodeList resources = raiz.getElementsByTagName("Resource");
		
		for(int j = 0; j < resources.getLength(); j++) {
			Element resource = (Element) resources.item(j);
			Attr id = resource.getAttributeNode("Id");
			
			if(id == null) {
				break;
			}
			
			NodeList listNames = resource.getElementsByTagName("Name");
			Node name = listNames.item(0).getFirstChild();
			
			NodeList listResourceTypes = resource.getElementsByTagName("ResourceType");
			Element resourceType = (Element) listResourceTypes.item(0);
			Attr reference = resourceType.getAttributeNode("Reference");
			
			if(reference.getNodeValue().equals("Teacher")) {
				professores.add(name.getNodeValue());
			}
		}
		
		for(String professor: professores) {
			System.out.println(professor);
		}
		
		return professores;
		
	}
	
	public List<String> recuperarClasses() {
		
		List<String> classes = new ArrayList<String>();
		NodeList resources = raiz.getElementsByTagName("Resource");
		
		for(int j = 0; j < resources.getLength(); j++) {
			Element resource = (Element) resources.item(j);
			Attr id = resource.getAttributeNode("Id");
			
			if(id == null) {
				break;
			}
			
			NodeList listNames = resource.getElementsByTagName("Name");
			Node name = listNames.item(0).getFirstChild();
			
			NodeList listResourceTypes = resource.getElementsByTagName("ResourceType");
			Element resourceType = (Element) listResourceTypes.item(0);
			Attr reference = resourceType.getAttributeNode("Reference");
			
			if(reference.getNodeValue().equals("Class")) {
				classes.add(name.getNodeValue());
			}
		}
		
		for(String classe: classes) {
			System.out.println(classe);
		}
		
		return classes;
		
	}
	
	public List<String> recuperarHorarios() {
		
		List<String> horarios = new ArrayList<String>();
		NodeList times = raiz.getElementsByTagName("Time");
		
		for(int j = 0; j < times.getLength(); j++) {
			Element resource = (Element) times.item(j);
			Attr id = resource.getAttributeNode("Id");
			
			if(id == null) {
				break;
			}
			
			NodeList listNames = resource.getElementsByTagName("Name");
			Node name = listNames.item(0).getFirstChild();
			
			horarios.add(name.getNodeValue());
			
			/*NodeList listResourceTypes = resource.getElementsByTagName("ResourceType");
			Element resourceType = (Element) listResourceTypes.item(0);
			Attr reference = resourceType.getAttributeNode("Reference");
			
			if(reference.getNodeValue().equals("Class")) {
				horarios.add(name.getNodeValue());
			}*/
		}
		
		for(String horario: horarios) {
			System.out.println(horario);
		}
		
		return horarios;
		
	}
	
	public /*int[][]*/ void recuperarEventos() {
		
		int[][] eventos;
		int linha, coluna = 0;
		/*NodeList events = raiz.getElementsByTagName("Event");
		
		System.out.println(events.getLength());*/
		
		NodeList listEvents = raiz.getElementsByTagName("Events");
		Element events = (Element) listEvents.item(0);
		
		System.out.println(events.getChildNodes().getLength());
		NodeList listEvent = events.getElementsByTagName("Event");
		
		System.out.println(listEvent.getLength());
		
		// criar a matriz. acho q pode apagar o de baixo VVV
		
		/*for(int j = 0; j < events.getLength(); j++) {
			Element event = (Element) events.item(j);
			Attr id = event.getAttributeNode("Id");
			
			if(id == null) {
				break;
			}
			
			NodeList listNames = event.getElementsByTagName("Name");
			Node name = listNames.item(0).getFirstChild();
			
			horarios.add(name.getNodeValue());
		}
		
		for(String horario: horarios) {
			System.out.println(horario);
		}
		
		return horarios;*/
		
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

}
