package servidor;

import java.util.concurrent.ThreadFactory;

//cada thread que for criada dentro desse pool vai utilizar esse tratador de exceção.
public class FabricaDeThreads implements ThreadFactory {
	private static int numero = 1;

	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r, "Thread Server Tasks Number: " + numero);
		numero++;

		thread.setUncaughtExceptionHandler(new TratadorDeExcecao());
		return thread;
	}

}
