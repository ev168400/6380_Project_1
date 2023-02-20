import java.util.ArrayList;
import java.util.HashMap;


public class Node {
    int nodeUID;
    String hostName;
    int listeningPort;
    HashMap<Integer, ArrayList<Node>> Neighbors;
    boolean candidate;
    int currentHighestUID;
    int phase;
    int pulseNum;
    int distanceFromHighest;

    public Node(){}
    
    //Constructor
    public Node(int nodeUID, String hostName, int listeningPort, HashMap<Integer, ArrayList<Node>> Neighbors){
        this.nodeUID = nodeUID;
		this.hostName = hostName;
		this.listeningPort = listeningPort;
		this.Neighbors = Neighbors;
    }

    //Getters
    public int getNodeUID() {return this.nodeUID;}
	public int getNodeListeningPort() {return this.listeningPort;}
	public String getNodeHostName() {return this.hostName;}
	public HashMap<Integer, ArrayList<Node>> getNeighbors() {return this.Neighbors;}

}
