import java.util.*;

public class Node {
    int nodeUID;
    String hostName;
    int listeningPort;
    HashMap<Integer, ArrayList<Node>> Neighbors;
    boolean leader;
    boolean visited;
    int currentHighestUID;
    int phase;
    int distanceFromHighest;
    int parent;
    int degree;
    ArrayList<Integer> children;
    Queue<Messages> messageQueue;
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
        this.parent = -1;
        this.children = new ArrayList<>();
        this.visited = false;
    }

    //Setters
    public void setDistance(){distanceFromHighest++;}
    public void setHighestUID(int newBiggest){ currentHighestUID = newBiggest;}
    public void setLeader(){this.leader = true;}
    public void setLeaderFound(){this.leader = true;}
    public void addMessageToQueue(Messages newMessage){messageQueue.add(newMessage);}
    public void setParent(int parent){this.parent = parent;}
    public void addChild(int child){children.add(child);}
    public void setVisited(){this.visited = true;}

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
    public int getParent(){return this.parent;}
    public int getDegree(){return children.size() + 1;}
    public boolean getVisited(){return this.visited;}
    public int getChildrenSize(){return this.children.size();}
    public String getChildren(){
        String childs = "";
        for(Integer i: children){
            childs = childs + "  " + Integer.toString(i);
        }
        
        return childs;
    }
    public int biggestFromQueue(){
        List<Integer> big =new ArrayList<>();
            for(int i = 0; i<messageQueue.size(); i++){
                 Messages hold =(Messages) messageQueue.poll();
                 big.add(hold.highestUID);
            }

	    return Collections.max(big);
    }

    public void addClient(Handler client) {
        synchronized (connectedClients) {
            connectedClients.add(client);
        }
    }
}