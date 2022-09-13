package servidor;

import java.io.PrintStream;
import java.util.Random;
import java.util.concurrent.Callable;

//simula o acesso ao banco de dados
public class ComandoC2AcessaBanco implements Callable<String> { // String é o tipo de retorno

	private PrintStream saida;

	public ComandoC2AcessaBanco(PrintStream saida) {
		this.saida = saida;
	}

	@Override
	public String call() throws Exception {

		System.out.println("Server received command c2");
		saida.println("C2 command processing - Banco");

		Thread.sleep(15000);
		int numero = new Random().nextInt(100) + 1;

		System.out.println("Server finished command c2 - Banco");
		return Integer.toString(numero);

	}

}
