import java.io.IOException;

public class LeaderElection {
    Node node;

    public LeaderElection() {}

    public LeaderElection(Node node) {this.node = node;}

    // getter
    public Node getNode() { return node;}

    public void startLeaderElection() {
        node.phase = 0;

        // While a leader has not yet been found send the max seen
        while (node.phase < node.Neighbors.size()) {
            sendMax();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Once the leader has been found
        System.out.println("New leader has been elected: " + node.currentHighestUID);
        if (node.getNodeUID() == node.getCurrentHighestUID()) {
            node.setLeader();
        }
    }

    public void sendMax() {
        Messages message;
        // If this is the first round
        if (node.phase == 0) {
            // create a message with the nodes own UID as the highest
            message = new Messages(node.getNodeUID(), node.phase, node.getNodeUID(), Type.SEND);

        } // Else check the message the message queue
        else {
            if (!node.messageQueue.isEmpty()) {
                int biggest = node.biggestFromQueue();
                // compare it to the highest known
                if (biggest > node.getCurrentHighestUID()) {
                    node.setHighestUID(biggest);
                    node.setDistance();
                } // Otherwise you can discard it
            }

            // Create new message with highest
            message = new Messages(node.currentHighestUID, node.phase, node.getNodeUID(), Type.SEND);
        }

        // Send out the new message
        node.connectedClients.forEach((clientHandler) -> {
            try {
                clientHandler.getOutputWriter().writeObject(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        // After the message has been sent increment the phase
        node.phase++;
    }
}
