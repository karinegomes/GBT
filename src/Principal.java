import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Principal {
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {		
		
		/*GRASP grasp = new GRASP();
		
		grasp.construcao(0.5);*/
		
		GBT gbt = new GBT();
		
		gbt.graspTabuSearch();
		
	}

}
