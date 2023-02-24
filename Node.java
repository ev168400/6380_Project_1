import java.util.*;

public class Node {
    int nodeUID;
    String hostName;
    int listeningPort;
    HashMap<Integer, ArrayList<Node>> Neighbors;
    boolean candidate;
    boolean leaderFound; //used? 
    int currentHighestUID;
    int phase;
    int pulseNum;
    int distanceFromHighest;
    Queue messageQueue;

    List<Handler> connectedClients = Collections.synchronizedList(new ArrayList<Handler>());

    public Node(){}

    //Constructor
    public Node(int nodeUID, String hostName, int listeningPort, HashMap<Integer, ArrayList<Node>> Neighbors){
        this.nodeUID = nodeUID;
        this.hostName = hostName;
        this.listeningPort = listeningPort;
        this.Neighbors = Neighbors;
        this.currentHighestUID = nodeUID;
        this.messageQueue = new LinkedList<Messages>();
    }

    //Setters
    public void setDistance(){distanceFromHighest++;}
    public void setHighestUID(int newBiggest){ currentHighestUID = newBiggest;}
    public void addMessageToQueue(Messages newMessage){
        /*boolean added = messageQueue.add(newMessage); System.out.println("added" + added);*/ 
        System.out.println("Message from: " + newMessage.UIDofSender + " stating: " + newMessage.highestUID);
        messageQueue.add(newMessage);
        System.out.println("Is the queue empty: " + messageQueue.isEmpty());
        

    }

    //Getters
    public int getNodeUID() {return this.nodeUID;}
    public int getNodeListeningPort() {return this.listeningPort;}
    public String getNodeHostName() {return this.hostName;}
    public HashMap<Integer, ArrayList<Node>> getNeighbors() {return this.Neighbors;}
    public int getDistanceFromHighest(){return distanceFromHighest;}
    public int getCurrentHighestUID(){return currentHighestUID;}
    public Messages getMessage(){return (Messages) messageQueue.poll();}
    public Messages checkMessage(){return (Messages) messageQueue.peek();}
    public List<Handler> getAllConnectedClients() {return this.connectedClients;}

    public void addClient(Handler client) {
        synchronized (connectedClients) {
            connectedClients.add(client);
        }
    }
}
