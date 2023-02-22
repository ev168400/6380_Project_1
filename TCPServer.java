import java.io.IOException;
import java.net.ServerSocket;

//Server Class
public class TCPServer {
    String hostName;
    int portNumber;
    int UID;
    ServerSocket serverSocket;
    Node node;
    
    public TCPServer(){}

    public TCPServer(String hostName, int portNumber, int UID){
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.UID = UID;
        
    }

    public TCPServer(Node _dsNode) {
		// hard coded local host for now
		this(_dsNode.hostName, _dsNode.listeningPort, _dsNode.nodeUID);
		this.node = _dsNode;
	}

	public void listenSocket() {
		try {
			serverSocket = new ServerSocket(portNumber);
		} catch (IOException e) {
			// System.out.println("Could not listen on port 4444");
			System.out.println(e);
			System.exit(-1);
		}
    }

    
}
