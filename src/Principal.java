import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Principal {
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		
		Parser parser = new Parser("BrazilInstance3.xml");
		
		List<String> professores = parser.recuperarProfessores();
		List<String> classes = parser.recuperarClasses();
		List<String> horarios = parser.recuperarHorarios();
		parser.recuperarEventos();
		
		//String[] professores = {"P1", "P2", "P3", "P4", "P5", "P6", "P7", "P8", "P9", "P10", "P11", "P12", "P13", "P14", "P15", "P16"};
		//String[] turmas = {"T1", "T2", "T3", "T4", "T5", "T6", "T7", "T8"};
		//String[] horarios = {"Mon1", "Mon2", "Mon3", "Mon4", "Mon5", "Tue1", "Tue2", "Tue3", "Tue4", "Tue5", "Wed1", "Wed2", "Wed3", "Wed4", "Wed5", "Wed6"};
		int[][] eventos = {
				{5, 5, 5, 0, 0, 0, 0, 0},
				{5, 5, 5, 0, 0, 0, 0, 0},
				{4, 4, 4, 0, 0, 0, 0, 0},
				{2, 2, 2, 0, 0, 0, 0, 0},
				{3, 3, 3, 0, 0, 2, 2, 2},
				{3, 0, 0, 2, 2, 2, 2, 2},
				{1, 1, 1, 1, 1, 1, 1, 1},
				{2, 2, 2, 1, 1, 1, 1, 1},
				{0, 3, 3, 2, 2, 0, 0, 0},
				{0, 0, 0, 5, 5, 0, 0, 0},
				{0, 0, 0, 5, 5, 0, 0, 0},
				{0, 0, 0, 3, 3, 3, 3, 3},
				{0, 0, 0, 0, 0, 5, 5, 5},
				{0, 0, 0, 0, 0, 5, 5, 5},
				{0, 0, 0, 3, 3, 3, 3, 3},
				{0, 0, 0, 3, 3, 3, 3, 3}
		};
		
		GBT gbt = new GBT();
		
		gbt.graspTabuSearch(eventos);
		
	}

}
