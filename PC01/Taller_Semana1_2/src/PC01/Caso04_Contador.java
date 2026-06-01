package PC01;

public class Caso04_Contador {
	public static void main(String[] args)
	{
		new Contador ();
		new Contador ();
		new Contador ();
		new Contador ();
		new Contador ();
		
		System.out.println("Objetos creados: "+ Contador.getContador());
	}
}
