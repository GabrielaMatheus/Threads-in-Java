package servidor;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ServidorTarefas {

	private ExecutorService threadPool;
	private ServerSocket servidor;
	private AtomicBoolean estaRodando;
	private BlockingQueue<String> filaComandos;

	public ServidorTarefas() throws IOException {
		System.out.println("Starting server");
		this.servidor = new ServerSocket(12345);
		this.threadPool = Executors.newCachedThreadPool( new FabricaDeThreads());//newCachedThreadPool();
		this.estaRodando = new AtomicBoolean(true);
		this.filaComandos = new ArrayBlockingQueue<>(2); //conceito de fila nas threads
		iniciarConsumidores();
	}

	private void iniciarConsumidores() {
		int qtdConsumidores = 2;
		for (int i = 0; i < qtdConsumidores; i++) {
			TarefaConsumir tarefa = new TarefaConsumir(filaComandos);
			this.threadPool.execute(tarefa);
			
		}
	}
	
	

	public void rodar() throws IOException {
		while (this.estaRodando.get()) {
			try {
				Socket socket = servidor.accept();
				System.out.println("Accepting new customer " + socket.getPort());
				DistribuirTarefas distribuirTarefas = new DistribuirTarefas(threadPool,filaComandos,socket,this);
				threadPool.execute(distribuirTarefas);
			} catch (SocketException e) {
				System.out.println("SocketException, are running? " + this.estaRodando);
			}

		}
	}
	
	public void parar() throws IOException {
		this.estaRodando.set(false);;
		servidor.close();
		threadPool.shutdown();
		//System.exit(0); //força a maquina virtual a parar
	}

	public static void main(String[] args) throws Exception {

		ServidorTarefas servidor = new ServidorTarefas();
		servidor.rodar();
		servidor.parar();

	}

}
