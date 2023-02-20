import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

//This file will be used to parse the configuration file
public class Parser {
    public static void main(String args[]) throws Exception{
        Node node = new Node();
        HashMap<Integer, Node> nodeList = new HashMap<>();
        HashMap<Integer, ArrayList<Node>> NeighborNodes = new HashMap<>();

        //TODO - Path is hardcoded and needs to be changed
        String path = "config.txt";
        BufferedReader b = new BufferedReader(new FileReader(path));
       
        String readLine = "";

        //Reads "# number of nodes in system"
		b.readLine();
		int numberOfNodes = Integer.parseInt(b.readLine());

        //Reads "# nodeUID hostName listeningPort"
        b.readLine();
        int UID = 0;
        String Hostname = "";
        int Port = 0;

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
            nodeList.put(UID, new Node(UID, Hostname, Port, NeighborNodes));

            //Initiate the neighbor list for each node by entering all nodes in the nodeList
            nodeList.get(UID).Neighbors.put(UID, new ArrayList<>());
        }        
        
        
        //Reads "# space delimited list of neighbors for each node"
        b.readLine();

        //While loop to get each node's neighbors
        while((readLine = b.readLine()) != null){
            readLine = readLine.trim();
            String[] s = readLine.split("\\s+");
            for (int i = 1; i < s.length; i++) {
                //Check if the neighbor is already listed
                Boolean present = nodeList.get(Integer.parseInt(s[0])).Neighbors.get(Integer.parseInt(s[0])).contains(nodeList.get(Integer.parseInt(s[i])));
                //If it is not add it to the neighbor list
                if(!present){
                    nodeList.get(Integer.parseInt(s[0])).Neighbors.get(Integer.parseInt(s[0])).add(nodeList.get(Integer.parseInt(s[i])));
                }

                //Make sure the connection is made both ways
                present = nodeList.get(Integer.parseInt(s[i])).Neighbors.get(Integer.parseInt(s[i])).contains(nodeList.get(Integer.parseInt(s[0])));
                //If it is not add it to the neighbor list
                if(!present){
                    nodeList.get(Integer.parseInt(s[i])).Neighbors.get(Integer.parseInt(s[i])).add(nodeList.get(Integer.parseInt(s[0])));
                }
            }
        }

        b.close();
    }
    
}
