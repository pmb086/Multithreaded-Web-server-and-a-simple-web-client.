package Socket;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.StringTokenizer;

public class HttpRequest implements Runnable {
	
static String Filename ="";
Socket client;

                           
public HttpRequest(Socket client) {                 // Constructor for socket
	
	this.client=client;
	
}
	
public void run()										//initiator function for multi threading	
{	
	try {
			response();									
	    } catch (Exception e){
		e.printStackTrace();
	   }
}

public void response() throws Exception {      //multi-threaded function	
	OutputStream ous = client.getOutputStream();  				//output stream to send out streams
	DataOutputStream os = new DataOutputStream(ous);			//send streams to the web client
	BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream())); 	//input stream for the socket	
	String readLine = br.readLine();
	String headerLine = null;
	
	System.out.println("Header:  "+readLine);
	
	
	HttpRequest.getName(readLine);			//get name of the file transferred from the request
	File f = new File(Filename);     
		
	System.out.println();
	System.out.println(readLine);
	System.out.println("TCP No Delay : " + client.getTcpNoDelay());       //  printing all connection parameters
	System.out.println("Timeout : " + client.getSoTimeout());
	
	System.out.println("Connection received from " + client.getInetAddress().getHostName());
	System.out.println("Port : " + client.getPort());
	
	
	FileInputStream fis = null;                               // requested file to be opened
	boolean fileExists = true;                            
	try {
		fis = new FileInputStream(f);
	} catch (FileNotFoundException e) {
		fileExists = false;
	}
	
	
	String statusLine = null, contentTypeLine = null, entityBody = null;  // Construct the response message.
	if (fileExists) {
		statusLine = "HTTP/1.1 200 OK";
		contentTypeLine = "Content-type: " + contentType( Filename ) + "\r\n";
		
	} else {
		statusLine = "HTTP/1.1 404 Not Found";
		contentTypeLine = "no contents \n";
		entityBody = "<HTML><HEAD><TITLE>Not Found</TITLE></HEAD><BODY>Not Found</BODY></HTML>";
	}
	
	
	os.writeBytes(statusLine);                     // Send the status line.
	
	os.writeBytes(contentTypeLine);                // Send the content type line
	
	
	os.writeBytes("\r\n");    //send a blank line to indicate the end of the header lines.
	
	
	if (fileExists) {                              // sending entity body
		sendBytes(fis, os);
		fis.close();
		os.writeBytes(statusLine);
		os.writeBytes(contentTypeLine);
		os.writeBytes("\r\n");
		} else {
		os.writeBytes(entityBody);
		os.writeBytes(statusLine);
		os.writeBytes(contentTypeLine);
		os.writeBytes("\r\n");
		
		}
		
		os.flush();
		os.close();
		br.close();
		client.close();
}

public static void getName(String readLine) {		 
	StringTokenizer tokens = new StringTokenizer(readLine); //Extract the filename from the request line.
	tokens.nextToken();                                       // skip over the method, which should be "GET"          
	String fileName = tokens.nextToken();
	Filename =  "C:\\Users\\dhiri\\Desktop\\root\\" + fileName;
}

private  String contentType(String fileName)                     // to check  type of file
{
if(fileName.endsWith(".htm") || fileName.endsWith(".html")) {
return "text/html";
}
if(fileName.endsWith(".gif")) {
	return "image/gif";}

return "application/octetâ€�stream";
}


private  void sendBytes(FileInputStream fis, OutputStream os)throws Exception{

byte[] buffer = new byte[1024];                        // Construct a 1K buffer to hold bytes on their way to the socket.
int bytes = 0;

while((bytes = fis.read(buffer)) != -1 ) {             // Copy requested file into the socket's output stream.
os.write(buffer, 0, bytes);
}
}

}
