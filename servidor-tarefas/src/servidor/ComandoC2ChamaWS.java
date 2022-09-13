package servidor;

import java.io.PrintStream;
import java.util.Random;
import java.util.concurrent.Callable;

//simula a chamada de um web service
public class ComandoC2ChamaWS implements Callable<String> { // String é o tipo de retorno

	private PrintStream saida;

	public ComandoC2ChamaWS(PrintStream saida) {
		this.saida = saida;
	}

	@Override
	public String call() throws Exception {

		System.out.println("Server received command c");
		saida.println("C2 command processing - Banco WS");

		Thread.sleep(15000);
		int numero = new Random().nextInt(100) + 1;

		System.out.println("Server finished command c2 - Banco WS");
		return Integer.toString(numero);

	}

}
