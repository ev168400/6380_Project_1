import java.util.HashMap;


public class Node {
    int nodeUID;
    String hostName;
    int listeningPort;
    HashMap<Integer, Node> Neighbors;
    
    //Constructor
    public Node(int nodeUID, String hostName, int listeningPort, HashMap<Integer, Node> Neighbors){
        this.nodeUID = nodeUID;
		this.hostName = hostName;
		this.listeningPort = listeningPort;
		this.Neighbors = Neighbors;
    }

    //Getters
    public int getNodeUID() {return this.nodeUID;}
	public int getNodeListeningPort() {return this.listeningPort;}
	public String getNodeHostName() {return this.hostName;}
	public HashMap<Integer, Node> getNeighbors() {return this.Neighbors;}

}
