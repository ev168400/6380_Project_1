import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

//Client Class
//TODO change variable names
public class TCPClient {
    String serverHostName;
    String clientHostName;
	int serverPortNumber;
    int UID;
    int serverUID;
	Socket clientsocket;
	ObjectInputStream in;
	ObjectOutputStream out;
	Node node;

    public TCPClient(){}

    public TCPClient(String serverHostName, String clientHostName, int serverPortNumber, int UID, int serverUID, Socket clientsocket, ObjectInputStream in, ObjectOutputStream out, Node node){
        this.serverHostName = serverHostName;
		this.serverPortNumber = serverPortNumber;
		this.UID = UID;
		this.clientHostName = clientHostName;
		this.serverUID = serverUID;
		this.node = node;                  
    }
}
