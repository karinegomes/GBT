public class GBT {
	
	// _Q = melhor grade de hor�rios
	// _f = valor da F.O. da melhor grade de hor�rios
	// GBTmax = n�mero m�ximo de itera��es
	// Q0 = grade inicial
	
	GRASP grasp;
	
	GBT(GRASP grasp) {
		this.grasp = grasp;
	}

	public void graspTabuSearch(int[][] eventos) {
		
		int[][] _Q = null;
		int _f = 0;
		int GBTmax = 100;
		
		for(int i = 0; i < GBTmax; i++) {
			int Q0 = 0; // fazer
		}
		
	}
	
}