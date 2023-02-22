import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;


public class Node {
    int nodeUID;
    String hostName;
    int listeningPort;
    HashMap<Integer, ArrayList<Node>> Neighbors;
    boolean candidate;
    boolean leaderFound;
    int currentHighestUID;
    int phase;
    int pulseNum;
    int distanceFromHighest;
    BlockingQueue messageQueue;

    public Node(){}
    
    //Constructor
    public Node(int nodeUID, String hostName, int listeningPort, HashMap<Integer, ArrayList<Node>> Neighbors){
        this.nodeUID = nodeUID;
		this.hostName = hostName;
		this.listeningPort = listeningPort;
		this.Neighbors = Neighbors;
        this.currentHighestUID = nodeUID;
    }

    //Setters
    public void setDistance(){distanceFromHighest++;}
    public void setHighestUID(int newBiggest){ currentHighestUID = newBiggest;}
    public void addMessageToQueue(Messages newMessage){messageQueue.add(newMessage);}

    //Getters
    public int getNodeUID() {return this.nodeUID;}
	public int getNodeListeningPort() {return this.listeningPort;}
	public String getNodeHostName() {return this.hostName;}
	public HashMap<Integer, ArrayList<Node>> getNeighbors() {return this.Neighbors;}
    public int getDistanceFromHighest(){return distanceFromHighest;}
    public int getCurrentHighestUID(){return currentHighestUID;}
    public Messages getMessage(){return (Messages) messageQueue.poll();}
    public Messages checkMessage(){return (Messages) messageQueue.peek();}

}
