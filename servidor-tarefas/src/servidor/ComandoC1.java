package servidor;

import java.io.PrintStream;

public class ComandoC1 implements Runnable{
	
	private PrintStream saida;
	
	public ComandoC1(PrintStream saida) {
		this.saida = saida;
	}

	@Override
	public void run() {
		
		System.out.println("C1 command executing");
		
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		
		saida.println("C1 command executing with success");
		
	}

}
