package LinearProgramming;

import java.util.Arrays;

public class Simplex {

	public enum Result{
		UNIQUE_BEST_SOLUTION,
		INFINITY_BEST_SOLUTIONS,
		IMPOSSIBLE
	}
	private float[][] A; //coefficients of constraints
	private float[] Xk, _C, Z; //constants
	private int[] K; //base
	
	public Result solve() {
		int constraints = A.length;
		int variables = A[0].length; //with slacks vars included
		
		//init base K so that Ak = In
		K = new int[constraints]; 
		for(int i=0;i<K.length;i++){
			K[i] = variables - constraints + i;
		}

		int minimum = minC();
		while(_C[minimum] < 0){
			int s = minimum;
						
			// find minimum xj/yjs
			int r = -1;
			float theta = Float.MAX_VALUE; 
			for(int j=0;j<A.length;j++){
				float yjs = A[j][s];
				float xj = Xk[j];
				if(yjs > 0 && xj / yjs < theta){
					r = j;
					theta = xj / yjs;
				}
			}
			if(r == -1){ // all Ys <= 0
				return Result.IMPOSSIBLE;
			}
			
			float yrs = A[r][s]; //pivot
			R1(A, r, 1/yrs);
			Xk[r] /= yrs;
			for(int i=0;i<A.length;i++){
				if(i!=r){
					float yis = A[i][s];
					R2(A, i, r, -yis);
					Xk[i] += -yis*Xk[r];
				}
			}
			float c = _C[s];
			int columns = _C.length;
			for(int col=0;col<columns;col++){
				_C[col] += -c*A[r][col];
			}
			
			K[r] = s;// K <- K+{s}-{r}
			
			minimum = minC();
		}
		
		if(_C[minimum] == 0) {
			return Result.INFINITY_BEST_SOLUTIONS;
		}
		return Result.UNIQUE_BEST_SOLUTION;
	}

	private int minC(){
		boolean[] insideBase = new boolean[_C.length];
		for(int i=0;i<K.length;i++){
			insideBase[K[i]] = true;
		}
		
		int minIndex = -1;
		float minimum = Float.MAX_VALUE;
		for(int i=0;i<_C.length;i++){
			if(!insideBase[i] && _C[i] < minimum){
				minimum = _C[i];
				minIndex = i;
			}
		}
		return minIndex;
	}
	
	// Ri -> c*Ri
	private void R1(float[][] array, int i, float c){
		int columns = array[0].length;
		for(int col=0;col<columns;col++){
			array[i][col] *= c;
		}
	}
	
	// Ri -> Ri + c*Rj
	private void R2(float[][] array, int i, int j, float c){
		int columns = array[0].length;
		for(int col=0;col<columns;col++){
			array[i][col] += c*array[j][col];
		}
	}
	
	public float[] getSolution() {
		int variables = A[0].length;
		float[] solution = new float[variables];
		Arrays.fill(solution, 0);
		for(int i=0;i<K.length;i++){
			solution[K[i]] = Xk[i];
		}
		return solution;
	}
	
	public float calculateZ(float[] solution) {
		float z = 0;
		for(int i=0;i<Z.length;i++){
			z += Z[i]*solution[i];
		}
		return z;
	}

	public void setA(float[][] a) {
		// extend array a so that A = a + In
		A = new float[a.length][a[0].length + a.length];
		for(int i=0;i<a.length;i++){
			for(int j=0;j<a[0].length;j++){
				A[i][j] = a[i][j];
			}
			for(int j=a[0].length;j<A[0].length;j++){
				A[i][j] = (i+a[0].length==j ? 1 : 0);
			}
		}
	}

	public void setB(float[] x) {
		Xk = x;
	}

	public void setC(float[] c) {
		_C = c;
		Z = new float[A[0].length - A.length]; //total variables - slack variables
		for(int i=0;i<Z.length;i++){
			Z[i] = _C[i];
		}
	}
	
	public float[][] getA() {
		return A;
	}


	public float[] getC() {
		return _C;
	}
	
}
