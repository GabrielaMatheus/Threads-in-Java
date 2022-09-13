package servidor;

import java.util.concurrent.BlockingQueue;

//classe para consumir a fila de comandos
public class TarefaConsumir implements Runnable {
	
	private BlockingQueue<String> filaComandos;
	
	

	public TarefaConsumir(BlockingQueue<String> filaComandos) {
		super();
		this.filaComandos = filaComandos;
	}



	@Override
	public void run() {
		try {
			String comando = null;
			while(( comando = filaComandos.take()) != null) {
				System.out.println("Consuming command c3 " + comando + Thread.currentThread().getName());
				Thread.sleep(10000);
			}
			
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

}
