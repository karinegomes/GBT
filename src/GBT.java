import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class GBT {
	
	// _Q = melhor grade de horários
	// _f = valor da F.O. da melhor grade de horários
	// GBTmax = número máximo de iterações
	// Q0 = grade inicial
	
	GRASP grasp;
	BuscaTabu buscaTabu;
	
	GBT() throws ParserConfigurationException, SAXException, IOException {
		Parser parser = new Parser("BrazilInstance3.xml");
		
		List<String> professores = parser.recuperarProfessores();
		List<String> classes = parser.recuperarClasses();
		List<String> horarios = parser.recuperarHorarios();
		int[][] eventos = parser.recuperarEventos(classes, professores);
		
		this.grasp = new GRASP(professores, classes, horarios);
		this.buscaTabu = new BuscaTabu(professores, classes, horarios, eventos);
	}

	public void graspTabuSearch() throws ParserConfigurationException, SAXException, IOException {
		
		int[][] melhorGrade = null;
		int funcaoObjetivo = 0;
		int GBTmax = 100;
		
		//for(int i = 0; i < GBTmax; i++) {
			int[][] solucaoInicial = grasp.construcao(0.5);
			int[][] duracaoAulas = grasp.recuperarDuracaoAulas();
			
			for(int j = 0; j < solucaoInicial.length; j++) {
				for(int k = 0; k < solucaoInicial[j].length; k++) {
					System.out.print(solucaoInicial[j][k] + " ");
				}
				System.out.println("\n");
			}
			
			buscaTabu.buscaLocal(solucaoInicial, duracaoAulas);
			
			
			
			/*for(int i = 0; i < duracaoAulas.length; i++) {
				for(int j = 0; j < duracaoAulas[i].length; j++) {
					System.out.print(duracaoAulas[i][j] + " ");
				}
				System.out.println("\n");
			}*/
		//}
		
	}
	
}