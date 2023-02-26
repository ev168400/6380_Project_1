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

		Node mainNode = BuildNode(clientHostName);

		Runnable serverRunnable = new Runnable() {
			public void run() {
				TCPServer server = new TCPServer(mainNode);
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
											m.getNodeHostName(), node.getValue().hostName, m.getNodeUID(), mainNode);
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
			while (mainNode.connectedClients.size() < mainNode.Neighbors.get(mainNode.getNodeUID()).size()) {
				Thread.sleep(10000);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Leader Election Initiated");
		LeaderElection algorithm = new LeaderElection(mainNode);
		algorithm.startLeaderElection();

		mainNode.messageQueue.clear();

		System.out.println("Tree Construction Started");
		BFSTree tree = new BFSTree(mainNode);
		tree.startBFSTree();
		System.out.println("Node " + mainNode.getNodeUID() + " Degree: " + mainNode.getDegree() + " Parent: "
				+ mainNode.getParent() + " Children: " + mainNode.getChildren());
	}

	public static Node BuildNode(String clientHostName) {
		Node mainNode = new Node();
		try {
			mainNode = Parser.parseFile("/home/010/e/ej/ejv190000/CS6380/Proj1/launch/config.txt", clientHostName);
		} catch (Exception e) {
			throw new RuntimeException("Unable to get nodeList", e);
		}
		return mainNode;
	}
}