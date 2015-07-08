import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
		int GBTmax = 50;
		
		/*long startGrasp = 0;
		long endGrasp = 0;
		long startTabu = 0;
		long endTabu = 0;*/
		
		for(int i = 0; i < GBTmax; i++) {		
			grasp = new GRASP(professores, classes, horarios);
			buscaTabu = new BuscaTabu(professores, classes, horarios, eventos);
			
			//startGrasp = System.currentTimeMillis();
			
			int[][] solucaoInicial = grasp.construcao(0.4);
			
			//endGrasp = System.currentTimeMillis();
			
			int[][] duracaoAulasInicial = grasp.recuperarDuracaoAulas();
			
			//startTabu = System.currentTimeMillis();
			
			buscaTabu.buscaLocal(solucaoInicial, duracaoAulasInicial);
			
			//endTabu = System.currentTimeMillis();
			
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
		
		System.out.println("----------------------------------------");
		
		imprimirGrade(melhorGrade);
		
		System.out.println("---------------------------------");
		
		imprimirGrade(duracaoAulas);
		
		/*System.out.println("-----------------------------------");
		
		long totalGrasp = endGrasp - startGrasp;
		long totalTabu = endTabu - startTabu;
		
		System.out.println("Duração Grasp:" + totalGrasp);
		System.out.println("Duração Tabu: " + totalTabu);*/
		
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