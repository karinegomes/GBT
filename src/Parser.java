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
		
		/*for(String professor: professores) {
			System.out.println(professor);
		}*/
		
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
		
		/*for(String classe: classes) {
			System.out.println(classe);
		}*/
		
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
		}
		
		/*for(String horario: horarios) {
			System.out.println(horario);
		}*/
		
		return horarios;
		
	}
	
	public int[][] recuperarEventos(List<String> classes, List<String> professores) {
		
		int numClasses = classes.size();
		int numProfessores = professores.size();
		
		int[][] eventos = new int[numProfessores][numClasses];
		int linha = 0, coluna = 0;
		
		NodeList listEvents = raiz.getElementsByTagName("Events");
		Element events = (Element) listEvents.item(0);
		
		NodeList listEvent = events.getElementsByTagName("Event");
		
		for(int i = 0; i < listEvent.getLength(); i++) {
			Element event = (Element) listEvent.item(i);
			NodeList listResource = event.getElementsByTagName("Resource");
			
			for(int j = 0; j < listResource.getLength(); j++) {
				Element resource = (Element) listResource.item(j);
				Attr reference = resource.getAttributeNode("Reference");
				Node role = resource.getElementsByTagName("Role").item(0).getFirstChild();
				
				if(role.getNodeValue().equals("Teacher")) {
					linha = recuperarIndice(professores, reference.getNodeValue());
				}
				else if(role.getNodeValue().equals("Class")) {
					coluna = recuperarIndice(classes, reference.getNodeValue());
				} 
			}
			
			NodeList listDuration = event.getElementsByTagName("Duration");
			Node duration = listDuration.item(0).getFirstChild();
			
			eventos[linha][coluna] = Integer.parseInt(duration.getNodeValue());
		}
		
		/*for(int i = 0; i < eventos.length; i++) {
			for(int j = 0; j < eventos[i].length; j++) {
				System.out.print(eventos[i][j]);
			}
			System.out.println("\n");
		}*/
		
		return eventos;
		
	}
	
	public int[][] recuperarHorariosIndisponiveis(List<String> professores, List<String> horarios) {
		
		int numHorarios = horarios.size();
		int numProfessores = professores.size();
		
		int[][] tabela = new int[numProfessores][numHorarios];
		int linha = 0, coluna = 0;
		
		NodeList listAvoidUnavailableTimesConstraint = raiz.getElementsByTagName("AvoidUnavailableTimesConstraint");
		
		for(int i = 0; i < listAvoidUnavailableTimesConstraint.getLength(); i++) {
			Element restricao = (Element) listAvoidUnavailableTimesConstraint.item(i);
			
			NodeList listResource = restricao.getElementsByTagName("Resource");
			
			Element resource = (Element) restricao.getElementsByTagName("Resource").item(0);
			
			Attr reference = resource.getAttributeNode("Reference");
			
			linha = recuperarIndice(professores, reference.getNodeValue()); // indice do professor
			
			NodeList listTime = restricao.getElementsByTagName("Time");
			
			for(int j = 0; j < listTime.getLength(); j++) {
				Element time = (Element) listTime.item(j);
				Attr horario = time.getAttributeNode("Reference");
				
				coluna = recuperarIndice(horarios, horario.getNodeValue()); // indice do horario				
				tabela[linha][coluna] = -1;
			}
		}
		
		/*for(int i = 0; i < tabela.length; i++) {
			for(int j = 0; j < tabela[i].length; j++) {
				System.out.print(tabela[i][j]);
			}
			System.out.println("\n");
		}*/
		
		return tabela;
		
	}
	
	public int[][] restricaoDistribuirEventosDivididos(List<String> classes, List<String> professores) {
		
		int numClasses = classes.size();
		int numProfessores = professores.size();
		
		int[][] eventosDivididos = new int[numProfessores][numClasses];
		int linha = 0, coluna = 0;
		
		NodeList listDistributeSplitEventsConstraint = raiz.getElementsByTagName("DistributeSplitEventsConstraint");
		
		for(int i = 0; i < listDistributeSplitEventsConstraint.getLength(); i++) {
			Element distributeSplit = (Element) listDistributeSplitEventsConstraint.item(i);
			NodeList listEventGroup = distributeSplit.getElementsByTagName("EventGroup");			
			int minimum = Integer.parseInt(distributeSplit.getElementsByTagName("Minimum").item(0).getFirstChild().getNodeValue());
			
			for(int j = 0; j < listEventGroup.getLength(); j++) {
				Element eventGroup = (Element) listEventGroup.item(j);
				Attr reference = eventGroup.getAttributeNode("Reference");
				String eventoPartesTemp = reference.getValue().split("_")[1];
				String professor = eventoPartesTemp.split("-")[0];
				String turma = eventoPartesTemp.split("-")[1];
				
				linha = recuperarIndice(professores, professor);
				coluna = recuperarIndice(classes, turma);
				
				eventosDivididos[linha][coluna] = minimum;
			}
		}
		
		/*System.out.println("Eventos divididos:\n");
		for(int i = 0; i < eventosDivididos.length; i++) {
			for(int j = 0; j < eventosDivididos[i].length; j++) {
				System.out.print(eventosDivididos[i][j] + " ");
			};
			System.out.println("\n");
		}
		System.out.println("\n");*/
		
		return eventosDivididos;
		
	}
	
	public int recuperarIndice(List<String> lista, String nome) {
		
		for(String item: lista) {
			if(item.equals(nome)) {
				int indice = lista.indexOf(item);
				
				return indice;
			}
		}
		
		return -1;
		
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
