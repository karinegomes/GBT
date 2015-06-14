import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class GRASP {

	int[][] horariosIndisponiveis;
	int[][] eventos;

	GRASP(int[][] horariosIndisponiveis, int[][] eventos) {
		this.horariosIndisponiveis = horariosIndisponiveis;
		this.eventos = eventos;
	}

	public TreeMap<Integer, Integer> recuperarHorariosCriticos() {

		Map<Integer, Integer> horariosCriticos = new HashMap<Integer, Integer>();

		for(int i = 0; i < horariosIndisponiveis.length; i++) {
			for(int j = 0; j < horariosIndisponiveis[i].length; j++) {
				if(horariosIndisponiveis[i][j] == -1) {
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

		TreeMap<Integer, Integer> horariosCriticosOrdenados = sortByValue(horariosCriticos); // horarios criticos ordenados

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

		TreeMap<Integer, Integer> cargaHorariaOrdenada = sortByValue(cargaHoraria);

		return cargaHorariaOrdenada;
	}

	@SuppressWarnings("rawtypes")
	public Map<Integer, Integer> criarLRC(TreeMap<Integer, Integer> professores, int tamanho) {

		Map<Integer, Integer> lrc = new TreeMap<Integer, Integer>();		
		Iterator it = professores.entrySet().iterator();		
		int i = 0;

		while(it.hasNext() && i < tamanho) {
			Map.Entry pair = (Map.Entry) it.next();			
			int key = (int) pair.getKey();
			int value = (int) pair.getValue();

			lrc.put(key, value);

			i++;
		}

		return lrc;

	}

	public void escolherProfessor(TreeMap<Integer, Integer> lrc) {

		Random       random    = new Random();
		List<Integer> keys      = new ArrayList<Integer>(lrc.keySet());
		int       randomKey = keys.get( random.nextInt(keys.size()) );
		int       value     = lrc.get(randomKey);
		
		System.out.println(value);



	}

	public void construcao() {

		Map<Integer, Integer> horariosCriticos = recuperarHorariosCriticos();		

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

	public TreeMap<Integer, Integer> sortByValue(Map<Integer, Integer> unsortedMap) {

		ValueComparator vc =  new ValueComparator(unsortedMap);
		TreeMap<Integer, Integer> sortedMap = new TreeMap<Integer, Integer>(vc);

		sortedMap.putAll(unsortedMap);

		return sortedMap;

	}

}
