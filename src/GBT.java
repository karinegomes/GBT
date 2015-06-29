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
	
	int[][] gradeInicial;
	
	List<String> professores;
	List<String> classes;
	List<String> horarios;
	int[][] eventos;
	
	GBT() throws ParserConfigurationException, SAXException, IOException {
		Parser parser = new Parser("BrazilInstance3.xml");
		
		professores = parser.recuperarProfessores();
		classes = parser.recuperarClasses();
		horarios = parser.recuperarHorarios();
		eventos = parser.recuperarEventos(classes, professores);
		gradeInicial = parser.recuperarHorariosIndisponiveis(professores, horarios);
		
		//this.grasp = new GRASP(professores, classes, horarios);
		//this.buscaTabu = new BuscaTabu(professores, classes, horarios, eventos);
	}

	public void graspTabuSearch() throws ParserConfigurationException, SAXException, IOException {
		
		int[][] melhorGrade = null;
		int[][] duracaoAulas = null;
		int funcaoObjetivo = 999;
		int GBTmax = 75;
		
		for(int i = 0; i < GBTmax; i++) {		
			grasp = new GRASP(professores, classes, horarios);
			buscaTabu = new BuscaTabu(professores, classes, horarios, eventos);
			
			int[][] solucaoInicial = grasp.construcao(0.5);			
			int[][] duracaoAulasInicial = grasp.recuperarDuracaoAulas();
			
			buscaTabu.buscaLocal(solucaoInicial, duracaoAulasInicial);
			
			if(buscaTabu.melhorSolucao < funcaoObjetivo) {
				melhorGrade = buscaTabu.melhorGrade;
				duracaoAulas = buscaTabu.duracaoAulas;
				funcaoObjetivo = buscaTabu.melhorSolucao;
				
				//System.out.println("Função de custo: " + funcaoObjetivo);
			}
			
			System.out.println("Função de custo: " + funcaoObjetivo);
		}
		
		System.out.println("-----------------------------------------");
		
		System.out.println(funcaoObjetivo);
		
	}
	
	public int[][] resetarGrade() throws ParserConfigurationException, SAXException, IOException {
		
		Parser parser = new Parser("BrazilInstance3.xml");
		
		int[][] grade = parser.recuperarHorariosIndisponiveis(professores, horarios);
		
		return grade;
		
	}
	
	public void imprimirGrade(int[][] grade) {
		
		for(int i = 0; i < grade.length; i++) {
			for(int j = 0; j < grade[i].length; j++) {
				System.out.print(grade[i][j] + " ");
			}
			System.out.println("\n");
		}
		
	}
	
}