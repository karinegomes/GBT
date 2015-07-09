import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class GBT {
	
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
	}

	public void graspTabuSearch() throws ParserConfigurationException, SAXException, IOException {
		
		long startCount = System.currentTimeMillis();
		long endCount = 0;
		
		DefaultCategoryDataset ds = new DefaultCategoryDataset();
		
		int[][] melhorGrade = null;
		int[][] duracaoAulas = null;
		int funcaoObjetivo = 999;
		int GBTmax = 500;
		
		for(int i = 0; i < GBTmax; i++) {		
			grasp = new GRASP(professores, classes, horarios);
			buscaTabu = new BuscaTabu(professores, classes, horarios, eventos);
			
			int[][] solucaoInicial = grasp.construcao(0.4);			
			int[][] duracaoAulasInicial = grasp.recuperarDuracaoAulas();
			
			buscaTabu.buscaLocal(solucaoInicial, duracaoAulasInicial);
			
			if(buscaTabu.melhorSolucao < funcaoObjetivo) {
				melhorGrade = buscaTabu.melhorGrade;
				duracaoAulas = buscaTabu.duracaoAulas;
				funcaoObjetivo = buscaTabu.melhorSolucao;
			}
			
			System.out.println("Função objetivo: " + funcaoObjetivo);
			
			endCount = System.currentTimeMillis();
			
			long totalCount = (endCount - startCount)/1000;
			
			ds.addValue(funcaoObjetivo, "", totalCount + "");
		}
		
		System.out.println("-----------------------------------------");
		
		System.out.println("Melhor função objetivo: " + funcaoObjetivo);
		
		System.out.println("----------------------------------------");
		
		imprimirGrade(melhorGrade);
		
		//System.out.println("---------------------------------");
		
		//imprimirGrade(duracaoAulas);
		
		JFreeChart grafico = ChartFactory.createLineChart("Resultado", "Tempo (s)", 
			    "Função objetivo", ds, PlotOrientation.VERTICAL, true, true, false);		 
		
		OutputStream arquivo = new FileOutputStream("grafico.png");
		ChartUtilities.writeChartAsPNG(arquivo, grafico, 1900, 600);
		
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