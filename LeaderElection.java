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

    }

    public void sendMax(){
        Messages message;
        //If this is the first round
        if(node.phase == 0){
            //create a message with the nodes own UID as the highest
            message = new Messages(node.nodeUID, node.phase, node.nodeUID, Type.SEND);

        }//Else check the message the message queue
        else{
            if(node.messageQueue != null){
                //compare it to the highest known 
                if(node.checkMessage().highestUID > node.getCurrentHighestUID()){
                    node.setHighestUID(node.getMessage().highestUID);
                    node.setDistance();
                }//Otherwise you can discard the message
                else{
                    node.getMessage();
                }
            }

            //Create new message with highest 
            message = new Messages(node.currentHighestUID, node.phase, node.nodeUID, Type.SEND);
        }

        //Send out the new message
        //dsNode.connectedClients.forEach((clientHandler) ->{
          //  clientHandler.getOutputWriter().writeObject(message);
        //});

        //After the message has been sent increment the phase
        node.phase++;
    }

    public void broadcastLeader(){

    }

    
    
}
