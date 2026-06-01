package PC01;

public class Caso03_Operacion {
	public static void main(String[] args)
	{
		Operacion op = new Operacion();
		
		System.out.println("Suma de Enteros:"
		+ op.sumar(15, 28));
		System.out.println("Suma de decimales:"
				+op.sumar(5.3, 4.9));
		System.out.println("Suma de 3 enteros: "
				+op.sumar(12, 31, 32));
	}
}
