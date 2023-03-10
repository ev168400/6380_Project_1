import java.io.IOException;
import java.util.*;

public class BFSTree {
    Node root;
    ArrayList<Integer> notChild;
    boolean ParentReachedOut;

    public BFSTree() {
    }

    public BFSTree(Node node) {
        this.root = node;
        this.notChild = new ArrayList<>();
        this.ParentReachedOut = false;
    }

    public void startBFSTree() {
        // While the node has not recieved confirmations from all neighbors on
        // parent/child status
        while (notChild.size() + root.getChildrenSize() < root.getNeighbors().get(root.getNodeUID()).size()) {
            CreateTree();
        }
        System.out.println("Tree is complete");
    }

    public void CreateTree() {
        // If the node is the leader it is the first to send out a message
        if (root.leader) {
            // If the parent has not yet sent its preliminary message
            if (!ParentReachedOut) {
                // Create message for neighbors
                Messages parentReach = new Messages(root.getNodeUID(), root.phase, root.getNodeUID(), Type.SEARCH);

                // Send out the new message to all clients
                root.connectedClients.forEach((clientHandler) -> {
                    try {
                        clientHandler.getOutputWriter().writeObject(parentReach);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                this.ParentReachedOut = true;
            }
            // Wait for acknowledgement from children
            // Check queue
            if (!root.messageQueue.isEmpty()) {
                // If there is a message waiting from the Child set the information
                if (root.checkMessage().getTypeOfMessage() == Type.RESPONSE) {
                    // Make sure this node is the intended parent and not just a neighbor
                    // Highest UID is holding intended parent UID
                    if (root.getNodeUID() == root.checkMessage().highestUID) {
                        // make sure we don't already have the child
                        if (!root.children.contains(root.checkMessage().UIDofSender)) {
                            root.addChild(root.getMessage().UIDofSender);
                        } else {
                            // discard the repeat response
                            root.getMessage();
                        }
                    } // If it is not the intended parent
                    else {
                        // If they have not yet been informed this node is not their child, add it to a
                        // list
                        if (!notChild.contains(root.checkMessage().UIDofSender)) {
                            notChild.add(root.getMessage().UIDofSender);
                        } // Else discard the message
                        else {
                            root.getMessage();
                        }
                    }
                } // Else this is a search message from a child -> discard
                else {
                    root.getMessage();
                    // Create message for neighbors
                    Messages parentResponse = new Messages(root.getNodeUID(), root.phase, root.getNodeUID(),
                            Type.RESPONSE);

                    // Send out the new message to all clients
                    root.connectedClients.forEach((clientHandler) -> {
                        try {
                            clientHandler.getOutputWriter().writeObject(parentResponse);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            // If the message queue is empty give the children a second to respond
            else {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        } // If the node is not the leader, check message queue for parent notifications
        else {
            // Give the root node a second to send out its first message
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Check queue
            if (!root.messageQueue.isEmpty()) {
                // If it is a search
                if (root.checkMessage().getTypeOfMessage() == Type.SEARCH) {
                    // If node has not been visited then set the parent
                    if (root.getVisited() == false) {
                        root.setVisited();
                        root.setParent(root.getMessage().getUIDofSender());
                    } // Else it already has a parent
                    else {
                        root.getMessage();
                    }
                    // Create acknowledgement message to send
                    // Format message = new Messages(root.ParentUID, root.phase,
                    // root.UID,Type.RESPONSE);
                    Messages childAcknowledgment = new Messages(root.getParent(), root.phase, root.getNodeUID(),
                            Type.RESPONSE);

                    // Send response for parent
                    root.connectedClients.forEach((clientHandler) -> {
                        try {
                            clientHandler.getOutputWriter().writeObject(childAcknowledgment);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Send search as well
                    // Create message for neighbors
                    Messages parentReach = new Messages(root.getNodeUID(), root.phase, root.getNodeUID(), Type.SEARCH);
                    // Send response for parent
                    root.connectedClients.forEach((clientHandler) -> {
                        try {
                            clientHandler.getOutputWriter().writeObject(parentReach);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                } // Else if it is a response
                else if (root.checkMessage().getTypeOfMessage() == Type.RESPONSE) {
                    // Check who the intended parent is
                    // Highest UID is holding intended parent UID
                    if (root.getNodeUID() == root.checkMessage().highestUID) {

                        // make sure we don't already have the child
                        if (!root.children.contains(root.checkMessage().UIDofSender)) {
                            root.addChild(root.getMessage().UIDofSender);
                        } else {
                            // discard the repeat response
                            root.getMessage();
                        }
                    } // If this is not the intended parent then add to not child list
                    else {
                        // If they have not yet been informed this node is not their child, add it to
                        // the not child list
                        if (!notChild.contains(root.checkMessage().UIDofSender)) {
                            notChild.add(root.getMessage().UIDofSender);
                        } // Else discard the message
                        else {
                            root.getMessage();
                        }
                    }
                } // Discard messages
                else {
                    root.getMessage();
                }
            }
        }
    }
}
