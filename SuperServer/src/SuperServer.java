import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class SuperServer {
	
	//10 slots for clients
	static ClientWorker[] workers = new ClientWorker[10];

	public static void main(String[] args) {
		
		Socket clientSocket = null; 
		
		try {
			ServerSocket server = new ServerSocket(4444);
			
			while(true) {
				//waiting for client
				clientSocket = server.accept();
				//checking is lot empty
				for (int i = 0; i <= 9 ; i++) {
					//on empty spot thread is created, which will work with client
					if(workers[i] == null) {
						workers[i] = new ClientWorker(clientSocket, workers);
						new Thread(workers[i]).start();
						break;
					}
				}
				
				
				
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
}
