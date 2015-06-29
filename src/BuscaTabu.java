import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;


public class BuscaTabu {
	
	List<String> professores;
	List<String> classes;
	List<String> horarios;
	int[][] eventos;
	int[][] melhorGrade;
	int[][] melhorGradeVizinho;
	List<int[]> listaTabu = new ArrayList<int[]>();
	int[][] duracaoAulas;
	int[][] duracaoAulasMelhorVizinho;
	int melhorSolucao;
	int melhorSolucaoVizinho;
	
	int[] aulasGeminadas;
	int[] maximoDias;
	
	BuscaTabu(List<String> professores, List<String> classes, List<String> horarios, int[][] eventos) throws ParserConfigurationException, SAXException, IOException {
		
		Parser parser = new Parser("BrazilInstance3.xml");
		
		this.professores = professores;
		this.classes = classes;
		this.horarios = horarios;
		this.eventos = eventos;
		this.melhorSolucaoVizinho = 999;
		this.aulasGeminadas = parser.restricaoAulasGeminadas(classes, professores);
		this.maximoDias = parser.restricaoNumeroMaximoDias(professores);
	}
	
	public void buscaLocal(int[][] solucaoInicial, int[][] duracaoAulasInicial) throws ParserConfigurationException, SAXException, IOException {
		
		int i = 0;
		int melhorI = 0;
		int BTmax = 10;
		
		melhorGrade = solucaoInicial;
		duracaoAulas = duracaoAulasInicial;
		melhorSolucao = funcaoAvaliacao(melhorGrade, duracaoAulasInicial);
		
		while(i - melhorI < BTmax) {
			i++;
			
			escolherMelhorVizinho();
			
			if(melhorSolucaoVizinho < melhorSolucao) {
				melhorGrade = melhorGradeVizinho;
				duracaoAulas = duracaoAulasMelhorVizinho;
				melhorSolucao = melhorSolucaoVizinho;
				melhorI = i;				
			}
		}
		
		//System.out.println("---------------------------------------------");
		
		//System.out.println("Melhor iteração: " + melhorI);
		
		listaTabu.clear();
		
		//imprimirGrade(melhorGrade);
		
	}
	
	public void escolherMelhorVizinho() throws ParserConfigurationException, SAXException, IOException {
		
		int[][] solucaoAtual = melhorGrade;
		int[][] duracaoAulasAtual = duracaoAulas;
		int[] troca = new int[3];
		
		for(int i = 0; i < melhorGrade.length; i++) {
			for(int j = 0; j < melhorGrade[i].length; j++) {
				for(int k = 0; k < melhorGrade[i].length; k++) {					
					if(j != k && melhorGrade[i][j] != -1 && melhorGrade[i][k] != -1 && melhorGrade[i][j] != melhorGrade[i][k]) {						
						int[][] grade2 = new int[melhorGrade.length][melhorGrade[0].length];
						int[][] duracaoAulas2 = new int[duracaoAulas.length][duracaoAulas[0].length];
						
						for(int a = 0; a < melhorGrade.length; a++) {
							for(int b = 0; b < melhorGrade[a].length; b++) {
								grade2[a][b] = melhorGrade[a][b];
								duracaoAulas2[a][b] = duracaoAulas[a][b];
							}
						}
						
						int temp = grade2[i][j];
						grade2[i][j] = grade2[i][k];
						grade2[i][k] = temp;
						
						int temp2 = duracaoAulas2[i][j];
						duracaoAulas2[i][j] = duracaoAulas2[i][k];
						duracaoAulas2[i][k] = temp2;
						
						int nivelInviabilidade = funcaoRestricoesEssenciais(grade2, duracaoAulas2);			
						
						if(nivelInviabilidade == 0) {
							
							int funcaoObjetivo = funcaoRestricoesNaoEssenciais(grade2, duracaoAulas2);
							
							if(funcaoObjetivo < melhorSolucaoVizinho && (!verificarTrocaListaTabu(troca) || funcaoObjetivo < melhorSolucao)) {
								melhorSolucaoVizinho = funcaoObjetivo;
								troca[0] = i;
								troca[1] = j;
								troca[2] = k;
							}
							
							//System.out.println("Função objetivo: " + funcaoObjetivo);
						}
					}
				}
			}
		}
		
		//System.out.println("Troca:");
		//System.out.println(troca[0] + " " + troca[1] + " " + troca[2]);
		
		int temp = solucaoAtual[troca[0]][troca[1]];
		solucaoAtual[troca[0]][troca[1]] = solucaoAtual[troca[0]][troca[2]];
		solucaoAtual[troca[0]][troca[2]] = temp;
		
		int temp2 = duracaoAulasAtual[troca[0]][troca[1]];
		duracaoAulasAtual[troca[0]][troca[1]] = duracaoAulasAtual[troca[0]][troca[2]];
		duracaoAulasAtual[troca[0]][troca[2]] = temp2;
		
		melhorGradeVizinho = solucaoAtual;
		duracaoAulasMelhorVizinho = duracaoAulasAtual;
		
		//System.out.println("Nova melhor solução: " + melhorSolucaoVizinho);
		
		if(listaTabu.size() > 17) {
			listaTabu.remove(0);
		}
		
		listaTabu.add(troca);
		
	}
	
	public int funcaoAvaliacao(int[][] Q, int[][] duracaoAulas) throws ParserConfigurationException, SAXException, IOException {
		
		int nivelInviabilidade = funcaoRestricoesEssenciais(Q, duracaoAulas);
		int qualidade = funcaoRestricoesNaoEssenciais(Q, duracaoAulas);
		
		return nivelInviabilidade + qualidade;
		
	}
	
	public int funcaoRestricoesEssenciais(int[][] grade, int[][] duracaoAulas) {
		
		//int violacao1 = validarRestricaoAtribuirHorariosEDuracao2h(grade, duracaoAulas);
		//int violacao2 = validarRestricaoDividirEventos(duracaoAulas);
		int violacao3 = validarRestricao1EventoPorDia(grade);
		
		if(violacao3 > 0) {
			return violacao3;
		}
		
		int violacao4 = validarRestricaoChoqueHorario(grade);
		
		if(violacao4 > 0) {
			return violacao4;
		}
		
		return 0;
		
		//int soma = violacao1 + violacao2 + violacao3 + violacao4;
		
		//return soma;
		
	}
	
	public int funcaoRestricoesNaoEssenciais(int[][] grade, int[][] duracaoAulas) throws ParserConfigurationException, SAXException, IOException {
		
		int restricao1 = validarRestricaoAulasGeminadas(aulasGeminadas, grade, duracaoAulas);
		int restricao2 = 3 * validarRestricaoSemPeriodosOciosos(grade);
		int restricao3 = 9 * validarRestricaoNumMaxDias(grade, maximoDias);
		
		int soma = restricao1 + restricao2 + restricao3;
		
		return soma;
		
	}
	
	public boolean verificarTrocaListaTabu(int[] troca) {
		
		if(listaTabu.isEmpty())
			return false;
		
		for(int[] trocaTabu: listaTabu) {
			for(int i = 0; i < trocaTabu.length; i++) {
				if(trocaTabu[i] != troca[i]) {
					return false;
				}
			}
		}
		
		return true;
		
	}
	
	// ------------------------------------- RESTRIÇÕES ESSENCIAIS -------------------------------------
	
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
	
	public int validarRestricao1EventoPorDia(int[][] grade) {
		
		int violacao = 0;
		int[][] periodosSemana = recuperarPeriodosSemana();
		boolean _break = false;
		
		for(int i = 0; i < grade.length; i++) {
			_break = false;
			
			for(int j = 0; j < periodosSemana.length; j++) {				
				for(int k = periodosSemana[j][0]; k <= periodosSemana[j][1]; k++) {
					if(grade[i][k] == -1) break;
					
					if(_break) {
						_break = false;
						
						break;
					}
					
					if(grade[i][k] > 0) {
						for(int l = k + 1; l <= periodosSemana[j][1]; l++) {
							if(grade[i][k] == grade[i][l]) {
								violacao++;
								
								_break = true;
								
								return violacao;
								
								//break;
							}
						}
					}
					
					
				}
				
			}
		}
		
		return violacao;
		
	}
	
	public int validarRestricaoChoqueHorario(int[][] grade) {
		
		int violacao = 0;
		
		for(int i = 0; i < grade.length; i++) {
			for(int j = 0; j < grade[i].length; j++) {
				if(grade[i][j] != 0 && grade[i][j] != -1) {
					for(int k = 0; k < grade.length; k++) {
						if(k != i && grade[i][j] == grade[k][j]) {
							violacao++;
							
							return violacao;
						}
					}
				}
			}
		}
		
		return violacao;
		
	}
	
	// ------------------------------------- RESTRIÇÕES NÃO-ESSENCIAIS -------------------------------------
	
	public int validarRestricaoAulasGeminadas(int[] aulasGeminadas, int[][] grade, int[][] duracaoAulas) {
		
		int[] aulasGeminadasTemp = new int[grade.length];
		int violacao = 0;
		
		for(int i = 0; i < grade.length; i++) {
			for(int j = 0; j < grade[i].length; j++) {
				if(grade[i][j] != 0 && grade[i][j] != -1) {					
					int qtdePeriodos = grade[i].length - 1;
					
					if(j != qtdePeriodos && j + 1 != qtdePeriodos) {
						String dia1 = horarios.get(j).split("_")[0];
						String dia2 = horarios.get(j + 1).split("_")[0];
						
						if(grade[i][j + 1] != 0 && grade[i][j + 1] != -1 && dia1.equals(dia2)) {
							aulasGeminadasTemp[i]++;
							
							//System.out.println("Encontradas aulas geminadas no professor T" + i + ": " + j + " " + (j + 1));
							
							if(duracaoAulas[i][j] < 2 || duracaoAulas[i][j + 1] < 2) {
								violacao++;
								
								//System.out.println("Aulas com 1h de duração.");
							}
							
							// Verifica se existem 3 aulas seguidas de um professor
							if(j + 2 != qtdePeriodos) {
								String dia3 = horarios.get(j + 2).split("_")[0];
								
								if(grade[i][j + 2] != 0 && grade[i][j + 2] != -1 && dia1.equals(dia3)) {
									//System.out.println("violação 3 aulas encontrada na linha: " + i);
									
									violacao++;
									j++;
								}
							}
							
							if(aulasGeminadas[i] < aulasGeminadasTemp[i]) {
								//System.out.println("violação encontrada na linha: " + i);
								violacao++;
								
								break;
							}
							
						}
					}
				}
			}
			
			if(aulasGeminadas[i] > aulasGeminadasTemp[i]) {
				//System.out.println("opa");
				//System.out.println("violação encontrada na linha: " + i);
				violacao++;
			}
			
			//System.out.println("Violações: " + violacao);
		}
		
		//System.out.println(violacao);
		
		return violacao;
		
	}
	
	public int validarRestricaoSemPeriodosOciosos(int[][] grade) {
		
		int[][] periodosSemana = recuperarPeriodosSemana();
		int violacao = 0;
		boolean _break = false;
		
		for(int i = 0; i < grade.length; i++) {
			for(int j = 0; j < periodosSemana.length; j++) {
				for(int k = periodosSemana[j][0] + 1; k <= periodosSemana[j][1]; k++) {
					
					if(_break == true) {
						_break = false;
						
						break;
					}
					
					if(grade[i][k] == -1) {
						break;
					}
					else if(grade[i][k] == 0 && grade[i][k - 1] > 0) {
						for(int l = k + 1; l <= periodosSemana[j][1]; l++) {
							if(grade[i][l] > 0) {
								violacao++;
								
								_break = true;
								
								break;
							}
						}
					}
				}
			}
		}
		
		return violacao;
		
	}
	
	public int validarRestricaoNumMaxDias(int[][] grade, int[] maximoDias) {
		
		int[] maximoDiasTemp = new int[maximoDias.length];
		int[][] periodosSemana = recuperarPeriodosSemana();
		int violacao = 0;
		boolean _break = false;
		
		for(int i = 0; i < grade.length; i++) {
			_break = false;
			
			for(int j = 0; j < periodosSemana.length; j++) {
				if(_break) {
					_break = false;
					
					break;
				}
				
				for(int k = periodosSemana[j][0]; k <= periodosSemana[j][1]; k++) {
					if(grade[i][k] == -1) {
						break;
					}
					
					if(grade[i][k] > 0) {
						maximoDiasTemp[i]++;
						
						if(maximoDiasTemp[i] > maximoDias[i]) {
							violacao++;
							
							_break = true;
						}
						
						break;
					}
				}
				
			}
		}
		
		return violacao;
		
	}
	
	// --------------------------------------------------------------------------------------------
	
	public int[][] recuperarPeriodosSemana() {
		
		int[][] periodosSemana = new int[5][2];
		int linha = 0;
		
		for(String horario: horarios) {
			if(horario.split("_")[1].equals("1")) {
				periodosSemana[linha][0] = horarios.indexOf(horario);
			}
			else if(horario.split("_")[1].equals("5")) {
				periodosSemana[linha][1] = horarios.indexOf(horario);
				
				linha++;
			}
		}
		
		return periodosSemana;
		
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
