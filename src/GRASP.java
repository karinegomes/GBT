import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
	List<Integer> indicesHorarios;
	int[][] eventos;
	int[][] grade;
	int[][] eventosDivididos;
	int[][] duracaoAulas;

	public GRASP(List<String> professores, List<String> classes, List<String> horarios) throws ParserConfigurationException, SAXException, IOException {
		
		Parser parser = new Parser("BrazilInstance3.xml");
		eventos = parser.recuperarEventos(classes, professores);
		
		this.professores = professores;
		this.classes = classes;
		this.horarios = horarios;
		indicesHorarios = recuperarIndicesHorarios();
		grade = parser.recuperarHorariosIndisponiveis(professores, horarios);
		duracaoAulas = new int[professores.size()][horarios.size()];
		
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

	public int[][] construcao(double tamanhoLRC) {

		Map<Integer, Integer> listaProfessores = new TreeMap<Integer, Integer>();
		Map<Integer, Integer> lrc = new TreeMap<Integer, Integer>();
		List<Integer> turmas = new ArrayList<Integer>();
		Random rand = new Random();
			
		listaProfessores = recuperarListaProfessoresOrdenada();
		lrc = criarLRC(listaProfessores, tamanhoLRC);
		
		int professor = escolherProfessor(lrc);		
		
		if(professor == -1) {
			return grade;
		}
		
		for(int i = 0; i < eventos[professor].length; i++) {
			if(eventos[professor][i] != 0) {
				turmas.add(i);
			}
		}
		
		int turma = escolherTurma(turmas);
		
		boolean preencheu = false;
		
		List<Integer> horariosRandom = criarListaHorariosRandom();
		
		for(int horarioRandom: horariosRandom) {			
			if(grade[professor][horarioRandom] == 0) {
				boolean existe = validarRestricoes(horarioRandom, turma, professor);
				
				if(existe == false) {
					grade[professor][horarioRandom] = turma + 1;
					preencheu = true;
					
					if(eventos[professor][turma] >= 2) {
						int randomNum = rand.nextInt(2) + 1;
						
						duracaoAulas[professor][horarioRandom] = randomNum;
						eventos[professor][turma] -= randomNum;
					}
					else {
						duracaoAulas[professor][horarioRandom] = 1;
						eventos[professor][turma] = 0;
					}
					
					break;
				}
			}
		}
		
		if(preencheu == false) {		
			for(int horarioRandom: horariosRandom) {				
				if(grade[professor][horarioRandom] == 0) {
					boolean existe = validarChoqueTurma(horarioRandom, turma);
					
					if(existe == false) {
						grade[professor][horarioRandom] = turma + 1;
						preencheu = true;
						
						if(eventos[professor][turma] >= 2) {
							int randomNum = rand.nextInt(2) + 1;
							
							duracaoAulas[professor][horarioRandom] = randomNum;
							eventos[professor][turma] -= randomNum;
						}
						else {
							duracaoAulas[professor][horarioRandom] = 1;
							eventos[professor][turma] = 0;
						}
						
						break;
					}
				}
			}
		}
		
		if(preencheu == false) {		
			for(int horarioRandom: horariosRandom) {
				if(grade[professor][horarioRandom] == 0) {
					grade[professor][horarioRandom] = turma + 1;

					if(eventos[professor][turma] >= 2) {
						int randomNum = rand.nextInt(2) + 1;
						
						duracaoAulas[professor][horarioRandom] = randomNum;
						eventos[professor][turma] -= randomNum;
					}
					else {
						duracaoAulas[professor][horarioRandom] = 1;
						eventos[professor][turma] = 0;
					}

					break;
				}
			}
		}
		
		construcao(tamanhoLRC);
		
		return grade;
		
	}

	@SuppressWarnings("rawtypes")
	public void printMap(Map mp) {
		Iterator it = mp.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			System.out.println(pair.getKey() + " = " + pair.getValue());
		}
	}
	
	// verifica se a turma selecionada j� est� tendo aula no mesmo hor�rio com outro professor
	public boolean validarChoqueTurma(int key, int turma) {
		
		for(int i = 0; i < grade.length; i++) {
			if(grade[i][key] == turma + 1) {
				return true;
			}
		}
		
		return false;
		
	}
	
	// verifica se a turma j� est� tendo aula com o professor em um determinado dia
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
	
	public List<Integer> recuperarIndicesHorarios() {
		
		List<Integer> indiceHorarios = new ArrayList<Integer>();
		
		for(String horario: horarios) {
			indiceHorarios.add(horarios.indexOf(horario));
		}
		
		return indiceHorarios;
		
	}
	
	public List<Integer> criarListaHorariosRandom() {
		
		Collections.shuffle(indicesHorarios);
		
		return indicesHorarios;
		
	}

}
