import java.util.ArrayList;
import java.util.List;


public class BuscaTabu {
	
	List<String> professores;
	List<String> classes;
	List<String> horarios;
	int[][] eventos;
	
	BuscaTabu(List<String> professores, List<String> classes, List<String> horarios, int[][] eventos) {
		this.professores = professores;
		this.classes = classes;
		this.horarios = horarios;
		this.eventos = eventos;
	}
	
	public void buscaLocal(int[][] Q, int[][] duracaoAulas) {
		
		int[][] _Q = Q;
		int i = 0;
		int melhorI = 0;
		int[] t = null;
		int BTmax = 100;
		
		System.out.println(f(_Q, duracaoAulas));
		
		while(i - melhorI < BTmax) {
			i++;
			
		}
		
	}
	
	public int f(int[][] Q, int[][] duracaoAulas) {
		
		int violacao1 = validarRestricaoAtribuirHorariosEDuracao2h(Q, duracaoAulas);
		int violacao2 = validarRestricaoDividirEventos(duracaoAulas);
		
		int soma = violacao1 + violacao2;
		
		return soma;
		
	}
	
	// verifica se todas as aulas foram preenchidas na grade, e se a carga horária foi dividida no máximo de aulas com 2h de duração possível
	public int validarRestricaoAtribuirHorariosEDuracao2h(int[][] Q, int[][] duracaoAulas) {
		
		int violacao = 0;
		
		for(int i = 0; i < Q.length; i++) {
			List<Integer> turmas = new ArrayList<Integer>();
			
			for(int j = 0; j < eventos[i].length; j++) {
				if(eventos[i][j] != 0) {
					turmas.add(j);
				}
			}
			
			for(int turma: turmas) {
				int cargaHoraria = eventos[i][turma];
				int cargaHorariaTemp = 0;
				int qtdeDuracao2h = (int) cargaHoraria/2;
				int qtdeDuracao2hTemp = 0;
				
				for(int j = 0; j < Q[i].length; j++) {
					if(Q[i][j] - 1 == turma) {
						cargaHorariaTemp += duracaoAulas[i][j];
						
						if(duracaoAulas[i][j] == 2) {
							qtdeDuracao2hTemp++;
						}
					}
				}
				
				if(qtdeDuracao2hTemp != qtdeDuracao2h) {
					violacao++;
				}
				
				if(cargaHorariaTemp != cargaHoraria) {
					violacao++;
				}
			}			
		}
		
		return violacao;
		
	}
	
	// verifica se todas as aulas tem duração de 1 ou 2 horas, não mais que isso
	public int validarRestricaoDividirEventos(int[][] duracaoAulas) {
		
		int violacao = 0;
		
		for(int i = 0; i < duracaoAulas.length; i++) {
			for(int j = 0; j < duracaoAulas[i].length; j++) {
				if(duracaoAulas[i][j] > 2) {
					violacao++;
				}
			}
		}
		
		return violacao;
		
	}

}
