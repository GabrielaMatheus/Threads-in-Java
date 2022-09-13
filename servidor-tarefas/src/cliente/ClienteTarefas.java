package cliente;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class ClienteTarefas {
	public static void main(String[] args) throws Exception {
		Socket socket = new Socket("localhost", 12345); // comunicação estabelecida

		System.out.println("Connection on");

		// classe anonima
		Thread threadEnviaComando = new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					PrintStream saida = new PrintStream(socket.getOutputStream());

					Scanner teclado = new Scanner(System.in);

					while (teclado.hasNextLine()) {
						String linha = teclado.nextLine();

						if (linha.trim().equals("")) {
							break;
						}

						saida.println(linha); // enviando um comando antes de travar a thread
					}
					saida.close();
					teclado.close();
				} catch (IOException e) {

					throw new RuntimeException(e);
				}
			}

		});

		Thread threadRecebeResposta = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					System.out.println("Getting data from server");
					Scanner resposatServidor = new Scanner(socket.getInputStream());
					while (resposatServidor.hasNextLine()) {
						String linha = resposatServidor.nextLine();
						System.out.println(linha);
					}
					resposatServidor.close();
				} catch (IOException e) {

					throw new RuntimeException(e);
				}

			}

		});

		threadRecebeResposta.start();
		threadEnviaComando.start();

		threadEnviaComando.join(); // espera a thread terminar pra fechar o socket

		System.out.println("Closed customer socket");
		socket.close();
	}

}
