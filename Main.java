import java.net.*;
import java.util.*;
import java.lang.*;

public class Main {
	public static void main(String[] args) {
		String clientHostName = "";
		try {
			clientHostName = InetAddress.getLocalHost().getHostName();
			clientHostName = clientHostName.substring(0, 4);
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
			if (node.getValue().hostName.equals(clientHostName)) {
				node.getValue().Neighbors.entrySet().forEach((neighbour) -> {
					if (neighbour.getKey() == node.getValue().getNodeUID()) {

						for (Node m : neighbour.getValue()) {
							// dsNode.clientsOnMachineCount = dsNode.clientsOnMachineCount + 1;
							Runnable clientRunnable = new Runnable() {
								public void run() {
									try {
										Thread.sleep(10000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}

									TCPClient client = new TCPClient(node.getValue().getNodeUID(),
											m.getNodeListeningPort(),
											m.getNodeHostName(), node.getValue().hostName, m.getNodeUID(), dsNode);
									client.clientListeningSocket();
									client.establishConnection();
									client.recieveMessage();
								}
							};
							Thread clientthread = new Thread(clientRunnable);
							clientthread.start();
						}

					}

				});
				break;
			}
		}
		try {
			while (dsNode.connectedClients.size() < dsNode.Neighbors.get(dsNode.getNodeUID()).size()) {
				Thread.sleep(10000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Node: " + dsNode.getNodeUID() + " Client Size: " + dsNode.connectedClients.size());
		// LeaderElection algorithm = new LeaderElection(dsNode);
		// algorithm.startLeaderElection();

	}

	public static Node BuildNode(String clientHostName) {
		Node dsNode = new Node();
		try {
			dsNode = Parser.parseFile("/home/010/e/ej/ejv190000/CS6380/Proj1/launch/config.txt", clientHostName);
		} catch (Exception e) {
			throw new RuntimeException("Unable to get nodeList", e);
		}
		return dsNode;
	}
}