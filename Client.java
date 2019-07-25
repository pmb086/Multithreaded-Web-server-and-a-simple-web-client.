package Socket;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Client {                          //client class
	
	final static String CRLF = "\r\n";
	private static String fileName;

	public static void main(String args[]) throws Exception{
		long ts, te, tu;                             // variables for calculating rtt
		ts = System.nanoTime();                      // finding time at time of connection initiation.gives elapsed time in milliseconds
		
		
	  final Socket socket = new Socket("localhost",8997);                 // client and server socket connection
		System.out.println("connection successful");
		File f = new File("C:\\Users\\dhiri\\Desktop\\root\\index.html");    //getting the file
		fileName=f.getName();
		
		PrintStream pw = new PrintStream(socket.getOutputStream());        // printing all relevant details on client side in java as well
		pw.println("GET" +" /"+ fileName + " "+ "HTTP/1.1"+ CRLF);
		System.out.println("Port is " + socket.getPort());
		System.out.println("TCP No Delay is  " + socket.getTcpNoDelay());
		System.out.println("Connection received from : " + socket.getInetAddress().getHostName()); // getting the ip address
		
		
		
		System.out.println("Timeout : " + socket.getSoTimeout());
		System.out.println("server responding");
		
		try{
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			StringBuilder sb = new StringBuilder();                                                   // this is the response from server
			String string;
			while ((string = br.readLine()) != null) {                                              // stringbuilder to read the file line by line.
			    sb.append(string + "\n");
			}

			br.close();
			System.out.println(sb.toString());
			socket.close();
			te = System.nanoTime();     // we can calculate rtt only after closing the socket and hence its got here after we close the socket.
			tu = (te-ts) / 1000000;     // in order to calculate in milliseconds we divide by 10^6.                                                                 
			System.out.println("RTT=" +  (double)Math.round(tu * 100)/100+" ms");  //   we use (double) to round of to nearest integer.           
		} 
		catch (IOException ex) {
			System.out.println(ex);                                           
		}
	}
}
