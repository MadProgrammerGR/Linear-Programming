package LinearProgramming;

import java.util.Scanner;

import LinearProgramming.Simplex.Result;

public class Main {
	static Scanner input;

	public static void main(String[] args) {
		input = new Scanner(System.in);
		System.out.println("=== Linear Programming - Simplex Method ===\n");
		Simplex simplex = new Simplex();		

		System.out.print("Number of variables(non slacks): ");
		int variables = readInt();
		System.out.print("Number of constraints: ");
		int constraints = readInt();

		System.out.println("\nCoefficients in minZ: (example: minZ = 2*x1 + 3,2*x2 - 7*x3 ---> 2 3,2 -7)");
		float[] coefficients = new float[variables + constraints];
		
		for (int i = 0; i < variables; i++) {
			coefficients[i] = readFloat();
		}
		for(int i=variables;i<variables+constraints;i++){
			coefficients[i] = 0;
		}

		System.out.println("\nConstraints (must be <= example: x1 + 4*x2 - 3,5*x3 <= 10 ---> 1 4 -3,5 10)");
		float[][] A = new float[constraints][variables];
		float[] B = new float[constraints];
		
		for (int i = 0; i < constraints; i++) {
			System.out.print("\tconstrain " + (i + 1) + ":");
			for (int j = 0; j < variables; j++) {
				A[i][j] = readFloat();
			}
			B[i] = readFloat();
		}
		
		//set arrays for simplex and then solve
		simplex.setA(A);
		simplex.setB(B);
		simplex.setC(coefficients);
		Result result = simplex.solve();
		
		//show result
		if(result == Result.UNIQUE_BEST_SOLUTION){
			System.out.println("==Unique best solution==");
			float[] solution = simplex.getSolution();
			printSolution(solution);
			System.out.println("Z = " + simplex.calculateZ(solution));
		}
		else if(result == Result.INFINITY_BEST_SOLUTIONS){
			System.out.println("==Infinity best solutions==\nOne such solution is:");
			float[] solution = simplex.getSolution();
			printSolution(solution);
			System.out.println("Z = " + simplex.calculateZ(solution));
		}
		else{
			System.out.println("Impossible or Z -> infinity");
		}
		input.close();
	}

	private static void printSolution(float[] solution) {
		for(int i=0;i<solution.length;i++){
			System.out.println("x"+(i+1)+" = "+solution[i]);
		}
	}
	
	private static float readFloat(){
		while (!input.hasNextFloat()) {
			System.out.println("invalid number " + input.next());
		}
		return input.nextFloat();
	}

	private static int readInt() {
		int number = 0;
		while (true) {
			while (!input.hasNextInt()) {
				input.next();
				System.out.println("Thats not an integer.");
			}
			number = input.nextInt();

			if (number <= 0) {
				System.out.println("Input a positive value.");
			} else if (number > 500) {
				System.out.println("Input a value in range [1-500].");
			} else {
				break;
			}
		}
		return number;
	}

}
