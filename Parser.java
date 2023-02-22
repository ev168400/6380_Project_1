import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

//This file will be used to parse the configuration file
public class Parser {

    final static HashMap<String, Node> nodeList = new HashMap<>();
    
    public static Node parseFile(String path, String hostName) throws Exception{
        Node node = new Node();
        HashMap<Integer, ArrayList<Node>> NeighborNodes = new HashMap<>();
        HashMap<Integer, Node> NeighborCreator = new HashMap<>();

        BufferedReader b = new BufferedReader(new FileReader(path));
       
        String readLine = "";

        //Reads "# number of nodes in system"
		b.readLine();
		int numberOfNodes = Integer.parseInt(b.readLine());

        b.readLine();
        //Reads "# nodeUID hostName listeningPort"
        b.readLine();
        int UID = 0;
        String Hostname = "";
        int Port = 0;
        String orderHostname[] = new String[numberOfNodes];

        //While loop to read each line and get info on each node
        for(int i = 0; i < numberOfNodes; i++){
            //The system uses Trim to handle leading and trailing white space
            readLine = b.readLine().trim();

            //Split the line based on spaces
            String[] s = readLine.split("\\s+");

            //Assign the variables from the line
			for(int j=0;j<s.length;j++){
				UID = Integer.parseInt(s[0]);
				Hostname = s[1];
				Port = Integer.parseInt(s[2]);
            }
            Node addNode = new Node(UID, Hostname, Port, NeighborNodes);
            nodeList.put(Hostname, addNode);
            NeighborCreator.put(UID, addNode);
            orderHostname[i] = Hostname;

            //Initiate the neighbor list for each node by entering all nodes in the nodeList
            nodeList.get(Hostname).Neighbors.put(UID, new ArrayList<>());
        }        
        
        b.readLine();
        //Reads "# space delimited list of neighbors for each node"
        b.readLine();

        int counter = 0;
        //While loop to get each node's neighbors
        while((readLine = b.readLine()) != null){
            //Read the line and trim off leading or trailing space
            readLine = readLine.trim();

            //Split based on space
            String[] s = readLine.split("\\s+");
            
            //Add Neighbor
            for (int i = 0; i < s.length; i++) {
                nodeList.get(orderHostname[counter]).Neighbors.get(nodeList.get(orderHostname[counter]).nodeUID).add(NeighborCreator.get(Integer.parseInt(s[i])));
            }
            counter++;
        }

        //Close the buffered reader
        b.close();
        
        for(int i = 0; i < orderHostname.length; i++){
            if(nodeList.get(orderHostname[i]).hostName.compareTo(hostName) == 0){
                node = nodeList.get(orderHostname[i]);
            }
        }
        return node;
    }
    
}
