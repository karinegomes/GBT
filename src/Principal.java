import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Principal {
	
	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		
		Parser parser = new Parser("BrazilInstance3.xml");
		
		List<String> professores = parser.recuperarProfessores();
		List<String> classes = parser.recuperarClasses();
		List<String> horarios = parser.recuperarHorarios();
		int[][] eventos = parser.recuperarEventos(classes, professores);		
		int[][] horariosIndisponiveis = parser.recuperarHorariosIndisponiveis(professores, horarios);
		
		recuperarClassesPrioridade(eventos);
		
		GRASP grasp = new GRASP(horariosIndisponiveis, eventos);
		
		TreeMap<Integer, Integer> cargaHorariaOrdenada = grasp.recuperarListaProfessoresOrdenada();
		
		//System.out.println(cargaHorariaOrdenada.getClass());
		
		grasp.criarLRC(cargaHorariaOrdenada, 5);
		
		/*GBT gbt = new GBT();
		
		gbt.graspTabuSearch(eventos);*/
		
	}
	
	// retorna as turmas com a média da carga horária de seus professores
	public static void recuperarClassesPrioridade(int[][] eventos) {
		
		Map<Integer, Double> mediaCargaHorariaTemp = new HashMap<Integer, Double>();
		int[] cargaHoraria = new int[eventos.length];
		int[][] eventosTransposta = new int[eventos[0].length][eventos.length];
		
		for(int i = 0; i < eventos.length; i++) {
			for(int j = 0; j < eventos[i].length; j++) {
				cargaHoraria[i] = cargaHoraria[i] + eventos[i][j]; 
			}
		}
		
		/*for(int i = 0; i < cargaHoraria.length; i++) {
			System.out.println(cargaHoraria[i]);
		}*/

		for (int i = 0; i < eventos.length; i++) {
			for (int j = 0; j < eventos[0].length ; j++ ) {
				eventosTransposta[j][i] = eventos[i][j];
			}
		}

		for (int i = 0; i < eventosTransposta.length; i++) {
			int bla = 0;
			int count = 0;
			double media = 0.0;
			
			for (int j = 0; j < eventosTransposta[i].length ; j++ ) {
				if(eventosTransposta[i][j] != 0) {
					bla += cargaHoraria[j];
					count++;
				}
			}
			media = (double) bla/count;
			
			mediaCargaHorariaTemp.put(i, media);			
		}

	}
	
	/*@SuppressWarnings("rawtypes")
	public static void printMap(Map mp) {
		Iterator it = mp.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
			//it.remove(); // avoids a ConcurrentModificationException
		}
	}*/

}
