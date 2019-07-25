package Socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
	
	public static void main(String[] args) throws IOException {
		
		try {
			ServerSocket socket = new ServerSocket(8997); 	//initiate socket connection
			while(true) {
				
				Socket client = socket.accept();			//accept connection request from client
				HttpRequest req = new HttpRequest(client);
				Thread t = new Thread(req);
				t.start();									//start multi-threaded connection
						
			}		
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		

	}

}
	
}

