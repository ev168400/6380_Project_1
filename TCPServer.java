import java.net.*;
import java.io.*;

public class TCPServer {
	String HostName;
	int PortNumber;
    int UID;
	ServerSocket serversocket;
	Node dsNode;

	public TCPServer(int UID, int serverPort, String hostName) {
		this.HostName = hostName;
		this.PortNumber = serverPort;
		this.UID = UID;
	}

	public TCPServer(Node _dsNode) {
		// hard coded local host for now
		this(_dsNode.nodeUID, _dsNode.listeningPort, _dsNode.hostName);
		this.dsNode = _dsNode;
	}

	public void listenSocket() {
		try {
			serversocket = new ServerSocket(PortNumber);
		} catch (IOException e) {
			System.out.println(e);
			System.exit(-1);
		}
		while (true) {
			//ClientRequestHandler reqHandler;
			try {
				// server.accept returns a client connection
				Socket clientreqSocket = serversocket.accept();
				//reqHandler = new ClientRequestHandler(clientreqSocket, this.dsNode);

				// add all the connected clients
				//dsNode.addClient(reqHandler);

				// assign each client request to a separate thread
				//Thread t = new Thread(reqHandler);
				//t.start();

			} catch (IOException e) {
				System.out.println("Accept failed");
				System.exit(-1);
			}
		}
	}
}