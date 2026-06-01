package PC01;

public class Contador {
	private static int contador = 0;
	public Contador () {
		contador ++;
	}
	
	public static int getContador () {
		return contador;
	}

}
