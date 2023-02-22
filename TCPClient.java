import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

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

    public TCPClient(int UID, int serverPortNumber, String serverHostName, String clientHostName, int serverUID,
    Node _dsNode){
        this.UID = UID;
        this.serverHostName = serverHostName;
		this.serverPortNumber = serverPortNumber;
		this.clientHostName = clientHostName;
		this.serverUID = serverUID;
    }

    public void clientListeningSocket(){
        try {
            clientsocket = new Socket(serverHostName, serverPortNumber, InetAddress.getByName(clientHostName), 0);
            out = new ObjectOutputStream(clientsocket.getOutputStream());
            out.flush();

			in = new ObjectInputStream(clientsocket.getInputStream());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }	
    }

    public void recieveMessage(){
        try {
            Messages messageRecieved = (Messages) in.readObject();
            node.addMessageToQueue(messageRecieved);
            if(messageRecieved.typeOfMessage == Type.LEADER){
                node.leaderFound = true;
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
