import java.net.*;
import java.util.Map;

public class Main {
    public static void main(String[] args){
        String clientHostName = "";
		try {
			clientHostName = InetAddress.getLocalHost().getHostName();
			//Changes dc02.utdallas.edu to dc02
			//clientHostName = clientHostName.substring(0,4);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		Node dsNode = BuildNode(clientHostName);
		
        Runnable serverRunnable = new Runnable() {
			public void run() {
				TCPServer server = new TCPServer(dsNode);
				// start listening for client requests
				server.listenSocket();
			}
		};
		Thread serverthread = new Thread(serverRunnable);
		serverthread.start();
		
        for (Map.Entry<String, Node> node : Parser.nodeList.entrySet()) {
			
			if (node.getValue().hostName.equals(clientHostName) ) {
				node.getValue().Neighbors.entrySet().forEach((neighbour) -> {

					//dsNode.clientsOnMachineCount = dsNode.clientsOnMachineCount + 1;
					Runnable clientRunnable = new Runnable() {
						public void run() {
							try {
								Thread.sleep(10000);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							for(Node m: neighbour.getValue()){
								TCPClient client = new TCPClient(node.getValue().getNodeUID(), m.getNodeListeningPort(), m.getNodeHostName(), node.getValue().hostName, neighbour.getKey(), dsNode);
								client.clientListeningSocket();
								client.recieveMessage();
							}						  
						}
					};
					Thread clientthread = new Thread(clientRunnable);
					clientthread.start();	
				});
				break;
			}
		}

		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		LeaderElection algorithm = new LeaderElection(dsNode);
		algorithm.startLeaderElection();
    }

    public static Node BuildNode(String clientHostName) {
		Node dsNode = new Node();
		try {
			dsNode = Parser.parseFile("config.txt",clientHostName);
			System.out.println("Node UID: " + dsNode.getNodeUID() + " Port: " + dsNode.getNodeListeningPort());
		} catch (Exception e) {
			throw new RuntimeException("Unable to get nodeList", e);
		}
		return dsNode;
	}
}
