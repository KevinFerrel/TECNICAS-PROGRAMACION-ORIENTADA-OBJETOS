package PC01;

import java.util.ArrayList;
import java.util.Scanner;
public class Caso05_Estudiantes {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Scanner sc = new Scanner(System.in);
		ArrayList<String> estudiantes = new ArrayList<>();
		try 
		{
			System.out.print("¿Cuantos estudiantes desea registrar?: ");
			int cantidad = Integer.parseInt(sc.nextLine());
			for (int i=1; i<= cantidad; i++) {
				System.out.print("Ingrese nombre del estudiante " + i + ": ");
                String nombre = sc.nextLine();
                estudiantes.add(nombre);
			}
			System.out.println("\n==  Lista de Estudiantes  ==");
			for (String estudiante : estudiantes) {
                System.out.println(estudiante);
			}
			
		}
		catch (Exception e) {
			System.out.println("Error: Debe ingresar una cantidad valida.");
		}
		sc.close();
	}

}
