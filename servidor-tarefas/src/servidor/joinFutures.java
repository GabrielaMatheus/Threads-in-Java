package servidor;

import java.io.PrintStream;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class joinFutures implements Callable<Void> {//não devolve nada

	private Future<String> futureWs;
	private Future<String> futureBanco;
	private PrintStream saidaCliente;

	public joinFutures(Future<String> futureWs, Future<String> futureBanco, PrintStream saidaCliente) {
		this.futureWs = futureWs;
		this.futureBanco = futureBanco;
		this.saidaCliente = saidaCliente;
	}

	@Override
	public Void call()  {
		System.out.println("Waiting results from Future");
		
		try {
			String numeroMagico = this.futureWs.get(20, TimeUnit.SECONDS);
			String numeroMagico2 = this.futureBanco.get(20, TimeUnit.SECONDS); //espera receber por 15 segundos
			
			this.saidaCliente.println("C2 command result: " + numeroMagico + " " + numeroMagico2);
		} catch (InterruptedException | ExecutionException | TimeoutException e) {
			System.out.println("Timeout: JoinFutures execution canceled");
			this.saidaCliente.println("Timeout c2 command execution");
			this.futureBanco.cancel(true);
			this.futureWs.cancel(true); //garante que realmente cancellará
		} 
		
		System.out.println("Ended joinFutures");
		
		return null;
	} 

	
}
