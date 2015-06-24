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
	int[][] duracaoAulas;

	public GRASP(List<String> professores, List<String> classes, List<String> horarios) throws ParserConfigurationException, SAXException, IOException {
		
		Parser parser = new Parser("BrazilInstance3.xml");
		
		/*professores = parser.recuperarProfessores();
		classes = parser.recuperarClasses();
		horarios = parser.recuperarHorarios();*/
		eventos = parser.recuperarEventos(classes, professores);
		
		this.professores = professores;
		this.classes = classes;
		this.horarios = horarios;
		grade = parser.recuperarHorariosIndisponiveis(professores, horarios);
		duracaoAulas = new int[professores.size()][horarios.size()];
		
		//eventosDivididos = parser.restricaoDistribuirEventosDivididos(classes, professores);
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
	public int[][] construcao(double tamanhoLRC) {

		TreeMap<Integer, Integer> horariosCriticos = new TreeMap<Integer, Integer>();
		Map<Integer, Integer> listaProfessores = new TreeMap<Integer, Integer>();
		Map<Integer, Integer> lrc = new TreeMap<Integer, Integer>();
		List<Integer> turmas = new ArrayList<Integer>();
		
		horariosCriticos = recuperarHorariosCriticos();
		
		listaProfessores = recuperarListaProfessoresOrdenada();
		lrc = criarLRC(listaProfessores, tamanhoLRC);
		int professor = escolherProfessor(lrc);
		
		if(professor == -1) {
			return grade;
		}
		
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
		
		int turma = escolherTurma(turmas);
		
		Iterator it = horariosCriticos.entrySet().iterator();
		
		/*for(int i = 0; i < grade[professor].length; i++) {
			if(grade[professor][i] == 0) {
				boolean existe = validarRestricoes(i, turma, professor);
				
				if(existe == false) {
					grade[professor][i] = turma + 1;
					
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
		}*/
		
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			
			int key = (int) pair.getKey();
			
			if(grade[professor][key] == 0) {
				boolean existe = validarRestricoes(key, turma, professor);
				
				if(existe == false) {
					grade[professor][key] = turma + 1;
					
					if(eventos[professor][turma] >= 2) {
						duracaoAulas[professor][key] = 2;
						eventos[professor][turma] -= 2;
					}
					else {
						duracaoAulas[professor][key] = 1;
						eventos[professor][turma] = 0;
					}
					
					/*if(eventosDivididos[professor][turma] > 0) {
						eventosDivididos[professor][turma]--;						
						eventos[professor][turma] = eventos[professor][turma] - 2;
					}
					else {
						eventos[professor][turma] = 0;
					}*/
					
					break;
				}
			}
		}
		
		/*System.out.println("Grade:\n");
		for(int i = 0; i < grade.length; i++) {
			for(int j = 0; j < grade[i].length; j++) {
				System.out.print(grade[i][j] + " ");
			}
			System.out.println("\n");
		}*/
		
		construcao(tamanhoLRC);
		
		return grade;
		
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
	
	// verifica se a turma selecionada já está tendo aula no mesmo horário com outro professor
	public boolean validarChoqueTurma(int key, int turma) {
		
		for(int i = 0; i < grade.length; i++) {
			if(grade[i][key] == turma + 1) {
				return true;
			}
		}
		
		return false;
		
	}
	
	public boolean validarEspalhamentoTurma(int key, int professor, int turma) {
		
		String dia = horarios.get(key).split("_")[0];
		
		for(String horario: horarios) {
			if(horario.contains(dia)) {
				int idHorario = horarios.indexOf(horario);
				
				if(grade[professor][idHorario] == turma + 1) {
					return true;
				}
				
				if(horario.split("_")[1].equals("5")) {
					break;
				}
			}
		}
		
		return false;
		
	}
	
	public boolean validarRestricoes(int key, int turma, int professor) {
		
		boolean restricao1 = validarChoqueTurma(key, turma);
		boolean restricao2 = validarEspalhamentoTurma(key, professor, turma);
		
		if(restricao1 == true || restricao2 == true) {
			return true;
		}
		
		return false;
		
	}
	
	public int[][] recuperarDuracaoAulas() {
		
		return duracaoAulas;
		
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
