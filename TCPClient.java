import java.net.*;
import java.io.*;

public class TCPClient {

    String ServerHost; //dcxx
    String ClientHost; //dcxx
	int ServerPort; //listening port
    int UID; //client uid
    int serverUID; //server uid
	Socket ClientSocket; 
	ObjectInputStream inStream;
	ObjectOutputStream outStream;
	Node dsNode;

    public TCPClient(int UID, int serverPort, String ServerHost, String ClientHost, int serverUID, Node _dsNode) {
		this.ServerHost = ServerHost;
		this.ServerPort = serverPort;
		this.UID = UID;
		this.ClientHost = ClientHost;
		this.serverUID = serverUID;
		this.dsNode = _dsNode;
	}

    public void listenSocket() {
		// Create socket connection
		try {
			ClientSocket = new Socket(ServerHost, ServerPort, InetAddress.getByName(ClientHost), 0);
			outStream = new ObjectOutputStream(ClientSocket.getOutputStream());
			outStream.flush();

			inStream = new ObjectInputStream(ClientSocket.getInputStream());
			System.out.println("After inputStream, listenSocket");
		} catch (UnknownHostException e) {
			System.out.println("Unknown host:" + ServerHost);
			System.exit(1);
		} catch (IOException e) {
			System.out.println("No I/O" + e);
			System.exit(1);
		}
	}
}
