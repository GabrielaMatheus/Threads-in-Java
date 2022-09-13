package servidor;

import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class DistribuirTarefas implements Runnable {

	private Socket socket;
	private ServidorTarefas servidor;
	private ExecutorService threadPool;
	private BlockingQueue<String> filaComandos;

	public DistribuirTarefas(ExecutorService threadPool, BlockingQueue<String> filaComandos, Socket socket, ServidorTarefas servidor) {
		super();
		this.threadPool = threadPool;
		this.filaComandos = filaComandos;
		this.socket = socket;
		this.servidor = servidor;
	}
	
	

	@Override
	public void run() {

		try {
			System.out.println("Distributing tasks for: " + socket);
			Scanner entradaCliente = new Scanner(socket.getInputStream()); // pega o comando do cliente

			PrintStream saidaCliente = new PrintStream(socket.getOutputStream());

			while (entradaCliente.hasNextLine()) {
				String comando = entradaCliente.nextLine();
				System.out.println(comando);

				switch (comando) {
				case "c1": {
					saidaCliente.println("C1 command confirming");
					ComandoC1 c1 = new ComandoC1(saidaCliente);
					this.threadPool.execute(c1);
					break;
				}
				case "c2": {
					saidaCliente.println("C2 command confirming");
					ComandoC2ChamaWS c2WS = new ComandoC2ChamaWS(saidaCliente);
					ComandoC2AcessaBanco c2Banco = new ComandoC2AcessaBanco(saidaCliente);
					Future<String> futureWs = this.threadPool.submit(c2WS);
					Future<String> futureBanco = this.threadPool.submit(c2Banco);
					
					//pra não travar essa thread, criamos uma só pra receber o resultado dos future
					this.threadPool.submit(new joinFutures(futureWs,futureBanco,saidaCliente)); 
					break;
					
				}case "c3": {
					this.filaComandos.put(comando);//bloqueia
					saidaCliente.println("C3 command added in queue");
					break;
				}case "fim": {
					saidaCliente.println("Shutting down server");
					servidor.parar();
					break;
				}
				default: {
					saidaCliente.println("Command not found");
					break;
				}
				}
			}
			saidaCliente.close();
			entradaCliente.close();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

}
