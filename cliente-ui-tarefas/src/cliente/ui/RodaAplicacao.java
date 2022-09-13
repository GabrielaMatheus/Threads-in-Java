package cliente.ui;

import java.io.IOException;
import java.net.Socket;

public class RodaAplicacao {

	public static void main(String[] args){

		try(Socket socket = new Socket("localhost", 12345)){
			
			InterfaceGrafica ig = new InterfaceGrafica(socket);
			ig.montaTela();
			ig.imprime("Conection on");
			initializaThreadQueRecebeAResposta(socket, ig);
			ig.imprime("Server closed");
			
		} catch (IOException | InterruptedException e) {
			throw new RuntimeException(e);
		} 

	}

	private static void initializaThreadQueRecebeAResposta(Socket socket, InterfaceGrafica ig)
			throws InterruptedException {
		
		Runnable threadRecebeResposta = new TarefaRecebeResposta(socket, ig);
		Thread threadResposta = new Thread(threadRecebeResposta);
		threadResposta.start();
		threadResposta.join(); //MAIN vai esperar a threadResposta terminar
	}

}
