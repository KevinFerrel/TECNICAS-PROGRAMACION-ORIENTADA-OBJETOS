package PC01;

import java.util.Scanner;

public class Caso01_Calculadora {

	public static double calcular(double a, double b, int opcion) {

		switch (opcion) {

		case 1:
			return a + b;

		case 2:
			return a - b;

		case 3:
			return a * b;

		case 4:
			if (b != 0)
				return a / b;
			else {
				System.out.println("No se puede dividir entre cero.");
				return 0;
			}

		default:
			System.out.println("Opción inválida.");
			return 0;
		}
	}

	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);

		System.out.print("Ingrese el primer número: ");
		double num1 = sc.nextDouble();

		System.out.print("Ingrese el segundo número: ");
		double num2 = sc.nextDouble();

		System.out.println("\nSeleccione una operación:");
		System.out.println("1. Suma");
		System.out.println("2. Resta");
		System.out.println("3. Multiplicación");
		System.out.println("4. División");

		int opcion = sc.nextInt();

		double resultado = calcular(num1, num2, opcion);

		System.out.println("Resultado: " + resultado);

		sc.close();
	}
}
