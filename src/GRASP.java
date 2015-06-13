import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class GRASP {
	
	int[][] horariosIndisponiveis;
	
	GRASP(int[][] horariosIndisponiveis) {
		this.horariosIndisponiveis = horariosIndisponiveis;
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
