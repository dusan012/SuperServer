import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.SocketAddress;


public class ClientWorker implements Runnable  {
	
	String conversionType;
	String numberToConvert;
	
	BufferedReader in;
	PrintStream out;
	Socket socketForComm;
	ClientWorker[]  workers;
	SocketAddress socketAdr;
	
	String supportedConversions;
	StringBuffer results = new StringBuffer("Results: /n");
	 
	
	
	//Constructor that allows to one instance of this class to connects on SuperServer 
	//and also whole array from SuperServer, so we can shift between it's elements
	public ClientWorker (Socket socket, ClientWorker[] cworker ) {
		
		this.socketForComm = socket;
		this.workers = cworker;
		
	}
	
	public void run() {
		
		try {
			//generating input and output stream 
			in = new BufferedReader(new InputStreamReader(socketForComm.getInputStream()));
			out = new PrintStream(socketForComm.getOutputStream());
		
		boolean valid = true;
		//while loop will be executed as long as client requests conversion
		while(valid) {
			out.println("Enter the conversion types that you support in this format: "
					+ "\n 5to2 7to8 - you can number more.");
		
			supportedConversions = in.readLine();
		
			out.println("Now, enter your desired number and conversion type in this format."
					+ "\n5to2&20 - before & is a conversion type, and after is a number for conversion");
		
			//storing in conversionType conversion type that client requests
			conversionType = in.readLine().substring(0, in.readLine().indexOf('&'));
		
			//storing  a number in numberToConvert that client wants to be converted
			numberToConvert = in.readLine().substring(in.readLine().indexOf('&'));
		
			//stores remote address of the client 
			socketAdr = socketForComm.getRemoteSocketAddress();
		
		
			out.println("IP Addresses of the acceptable client: ");
		
			/*checking every client in array workers. conditions: one must support desirable
		 	conversion type. spot in array must not be empty, and request client will
		 	be excluded from checking. If contions are fullfiled, IP address of client will
		 	be sent to request client.
			 */
			int counter = 0;
			for (int i = 0; i <=9 ; i++) {
			
				if(workers[i] != null && workers[i] != this && 
						workers[i].supportedConversions.contains(conversionType)) {
					out.println(workers[i].socketAdr);
					counter++;
				}	
			}
			if(counter == 0) out.println("There is no available IP address.");
			
			/*If client finished the conversion session with other client, he than returns
			  All the results are stored in result object.
			  After that, client is asked for another conversion. If he responds with no,
			  the session is closed.
			*/
			if(in.readLine().startsWith("Results: ")) {
				String line = in.readLine().substring((in.readLine().indexOf(':'))+2);
				results.append(line);
				out.println("Do you want another try?(yes/no)");
				if(in.readLine().startsWith("no")) valid = false;
			}
			
		}
			out.println("Session is over.");
			socketForComm.close();;
			
		}catch (IOException ex) {
			ex.printStackTrace();
		}
		//array slot becoms null
		for (int i = 0; i <=9; i++) {
			if(workers[i] == this) {
				workers[i] = null;
			}
		}
		
		
	}
	
	

}
