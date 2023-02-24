import java.io.IOException;

public class LeaderElection {
    Node node;

    public LeaderElection(){}

    public LeaderElection(Node node){
        this.node = node;
    }

    //getter
    public Node getNode(){return node;}

    public void startLeaderElection(){
        node.phase = 0;

        //While a leader has not yet been found send the max seen 
        while(node.phase < node.Neighbors.size()){
            sendMax();
        }

	//Once the leader has been found
        System.out.println("New leader has been elected: " + node.currentHighestUID);

    }

    public void sendMax(){
        Messages message;
        //If this is the first round
        if(node.phase == 0){
            //create a message with the nodes own UID as the highest
            message = new Messages(node.getNodeUID(), node.phase, node.getNodeUID(), Type.SEND);

        }//Else check the message the message queue
        else{            
            if(node.messageQueue != null){
                //compare it to the highest known
                if(node.messageQueue != null && node.checkMessage().highestUID > node.getCurrentHighestUID()){
                    node.setHighestUID(node.getMessage().highestUID);
                    node.setDistance();
                    System.out.println("New biggest: " + node.getCurrentHighestUID());
                }//Otherwise you can discard the message
                else{
                    System.out.println("ignored");
                    node.getMessage();
                }
            }

            //Create new message with highest
            message = new Messages(node.currentHighestUID, node.phase, node.getNodeUID(), Type.SEND);
        }

	//Send out the new message
        node.connectedClients.forEach((clientHandler) ->{
            try {
                clientHandler.getOutputWriter().writeObject(message);
                System.out.println("Node: " + node.getNodeUID() + " sent highest UID: " + node.getCurrentHighestUID());
            } catch (IOException e) {
                e.printStackTrace();
            }
	});

	//After the message has been sent increment the phase
        node.phase++;
        System.out.println("Phase: " + node.phase);
    }

    public void broadcastLeader(){
    }
}