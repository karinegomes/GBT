import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class GRASP {
	
	List<String> professores;
	List<String> classes;
	List<String> horarios;
	int[][] eventos;
	int[][] grade;
	int[][] eventosDivididos;

	public GRASP() throws ParserConfigurationException, SAXException, IOException {
		Parser parser = new Parser("BrazilInstance3.xml");
		
		professores = parser.recuperarProfessores();
		classes = parser.recuperarClasses();
		horarios = parser.recuperarHorarios();
		eventos = parser.recuperarEventos(classes, professores);		
		grade = parser.recuperarHorariosIndisponiveis(professores, horarios);
		
		eventosDivididos = parser.restricaoDistribuirEventosDivididos(classes, professores);
	}

	public TreeMap<Integer, Integer> recuperarHorariosCriticos() {

		Map<Integer, Integer> horariosCriticos = new HashMap<Integer, Integer>();

		/*for(int i = 0; i < grade.length; i++) {
			for(int j = 0; j < grade[i].length; j++) {
				if(grade[i][j] == -1) {
					if(horariosCriticos.get(j) == null) {
						horariosCriticos.put(j, 1);
					}
					else {
						horariosCriticos.put(j, horariosCriticos.get(j) + 1);
					}
				}
				else if(grade[i][j] > 0) {
					if(horariosCriticos.get(j) == null) {
						horariosCriticos.put(j, 0);
					}
					else {
						horariosCriticos.put(j, horariosCriticos.get(j) - 1);
					}
				}
			}
		}*/
		
		for(int i = 0; i < grade.length; i++) {
			for(int j = 0; j < grade[i].length; j++) {
				if(grade[i][j] == 0) {
					if(horariosCriticos.get(j) == null) {
						horariosCriticos.put(j, 1);
					}
					else {
						horariosCriticos.put(j, horariosCriticos.get(j) + 1);
					}
				}
			}
		}

		//printMap(horariosCriticos);

		TreeMap<Integer, Integer> horariosCriticosOrdenados = ordenacaoCrescente(horariosCriticos); // horarios criticos ordenados

		//printMap(sortedMap);

		return horariosCriticosOrdenados;

	}

	public TreeMap<Integer, Integer> recuperarListaProfessoresOrdenada() {

		Map<Integer, Integer> cargaHoraria = new HashMap<Integer, Integer>();

		for(int i = 0; i < eventos.length; i++) {
			for(int j = 0; j < eventos[i].length; j++) {
				if(cargaHoraria.get(i) == null) {
					cargaHoraria.put(i, eventos[i][j]);
				}
				else {
					cargaHoraria.put(i, cargaHoraria.get(i) + eventos[i][j]);
				}
			}
		}

		TreeMap<Integer, Integer> cargaHorariaOrdenada = ordenacaoDecrescente(cargaHoraria);

		return cargaHorariaOrdenada;
	}

	@SuppressWarnings("rawtypes")
	public TreeMap<Integer, Integer> criarLRC(Map<Integer, Integer> listaProfessores, double alpha) {
		
		int tamanho = (int) (listaProfessores.size() * alpha);

		TreeMap<Integer, Integer> lrc = new TreeMap<Integer, Integer>();		
		Iterator it = listaProfessores.entrySet().iterator();		
		int i = 0;

		while(it.hasNext() && i < tamanho) {
			Map.Entry pair = (Map.Entry) it.next();			
			int key = (int) pair.getKey();
			int value = (int) pair.getValue();
			
			if(value > 0) {
				lrc.put(key, value);
			}

			i++;
		}

		return lrc;

	}

	public int escolherProfessor(Map<Integer, Integer> lrc) {

		Random random = new Random();
		List<Integer> keys = new ArrayList<Integer>(lrc.keySet());
		
		System.out.println("keys.size(): " + keys.size());
		
		if(keys.size() == 0) {
			return -1;
		}
		
		int randomKey = keys.get(random.nextInt(keys.size()));
		
		return randomKey;

	}
	
	public int escolherTurma(List<Integer> turmas) {
		
		Random random = new Random();
		int randomTurma = turmas.get(random.nextInt(turmas.size()));
		
		return randomTurma;
		
	}

	@SuppressWarnings("rawtypes")
	public void construcao(double tamanhoLRC) {

		TreeMap<Integer, Integer> horariosCriticos = new TreeMap<Integer, Integer>();
		Map<Integer, Integer> listaProfessores = new TreeMap<Integer, Integer>();
		Map<Integer, Integer> lrc = new TreeMap<Integer, Integer>();
		List<Integer> turmas = new ArrayList<Integer>();
		
		horariosCriticos = recuperarHorariosCriticos();
		
		System.out.println("Horários críticos:");
		printMap(horariosCriticos);
		System.out.println("\n");
		
		listaProfessores = recuperarListaProfessoresOrdenada();
		lrc = criarLRC(listaProfessores, tamanhoLRC);
		int professor = escolherProfessor(lrc);
		
		if(professor == -1) {
			return;
		}
		
		System.out.println("Lista de professores:");
		printMap(listaProfessores);
		System.out.println("\n");
		
		System.out.println("Professor escolhido: " + professor);
		
		/*for(int i = 0; i < eventos.length; i++) {
			for(int j = 0; j < eventos[i].length; j++) {
				System.out.print(eventos[i][j] + " ");
			}
			System.out.println("\n");
		}*/
		
		for(int i = 0; i < eventos[professor].length; i++) {
			if(eventos[professor][i] != 0) {
				turmas.add(i);
			}
		}
		
		System.out.println("Turmas:");
		for(int turma: turmas) {
			System.out.print(turma + "  ");
		}
		
		System.out.println("");
		
		int turma = escolherTurma(turmas);
		
		System.out.println("Turma escolhida: " + turma);
		
		printMap(horariosCriticos);
		
		System.out.println("Horário crítico: " + horariosCriticos.firstKey());
		
		Iterator it = horariosCriticos.entrySet().iterator();
		
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			
			int key = (int) pair.getKey();
			
			if(grade[professor][key] == 0) {
				boolean existe = false;
				
				for(int i = 0; i < grade.length; i++) {
					if(grade[i][key] == turma + 1) {
						existe = true;
						
						break;
					}
				}
				
				if(existe == false) {
					grade[professor][key] = turma + 1;
					// tratar restrição de dividir horarios
					
					if(eventosDivididos[professor][turma] > 0) {
						eventosDivididos[professor][turma]--;						
						eventos[professor][turma] = eventos[professor][turma] - 2;
					}
					else {
						eventos[professor][turma] = 0;
					}
					
					break;
				}
			}
		}
		
		System.out.println("Grade:\n");
		for(int i = 0; i < grade.length; i++) {
			for(int j = 0; j < grade[i].length; j++) {
				System.out.print(grade[i][j] + " ");
			}
			System.out.println("\n");
		}
		
		System.out.println("Eventos:\n");
		for(int i = 0; i < eventos.length; i++) {
			for(int j = 0; j < eventos[i].length; j++) {
				System.out.print(eventos[i][j] + " ");
			}
			System.out.println("\n");
		}
		
		System.out.println("Eventos divididos:");
		for(int i = 0; i < eventosDivididos.length; i++) {
			for(int j = 0; j < eventosDivididos[i].length; j++) {
				System.out.print(eventosDivididos[i][j] + " ");
			}
			System.out.println("\n");
		}
		
		construcao(tamanhoLRC);
		
	}

	@SuppressWarnings("rawtypes")
	public void printMap(Map mp) {
		Iterator it = mp.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
			//it.remove(); // avoids a ConcurrentModificationException
		}
	}

	public TreeMap<Integer, Integer> ordenacaoDecrescente(Map<Integer, Integer> unsortedMap) {

		ValueComparator vc =  new ValueComparator(unsortedMap);
		TreeMap<Integer, Integer> sortedMap = new TreeMap<Integer, Integer>(vc);

		sortedMap.putAll(unsortedMap);

		return sortedMap;

	}
	
	public TreeMap<Integer, Integer> ordenacaoCrescente(Map<Integer, Integer> unsortedMap) {

		ValueComparator2 vc =  new ValueComparator2(unsortedMap);
		TreeMap<Integer, Integer> sortedMap = new TreeMap<Integer, Integer>(vc);

		sortedMap.putAll(unsortedMap);

		return sortedMap;

	}

}
