package chatsystem;

import java.io.IOException;
import java.net.*;

/**
 * Main controller of the ChatSystem application
 * 
 * The Application class initializes and coordinates the different components
 * of the chat system.
 */
public class Application {
    private String username;
    public UserList myList;
    private Receiver receiver;
    private Disconnection disconnection;
    private SendUpdate sendUpdate;
    private Sender msgSender;
    private Terminal terminal;

    /**
     * Initalizes the application and creates all its components.
     * 
     * @throws SocketException  if one of the sockets can't be created
     */
    public Application() throws SocketException {
        username = "10.1.5.14";
        myList = new UserList();
        receiver = new Receiver(myList);
        disconnection = new Disconnection(myList);
        msgSender = new Sender(4445, 4444, 256);
        sendUpdate = new SendUpdate(msgSender);
        terminal = new Terminal(this);
    } 

    /**
     * Send a connection message to simulate a user joining the network
     * 
     * @return  0 if the message was sucessfuly sent, 1 if an error occured
     */
    public int test_connection() {
        System.out.println("sent fake connection message");
        try {
            msgSender.sendBroadcastConnexion();
        } catch (IOException e) {
            return 1;
        } 
        return 0;
    }

    /**
     * Sends an update message to simulate a user's periodic activity.
     * 
     * @return  0 if the message was sucessfuly sent, 1 if an error occured
     */
    public int test_update() {
        System.out.println("Sent fake connection ping from simulated user");
        try {
            msgSender.sendBroadcastUpdate();
        } catch (IOException e) {
            return 1;
        } 
        
        return 0;
    }

    /**
     * Sends a message to change our username
     * The name must not already be used by another user.
     * If the change is accepted, it send a broadcast to the network.
     * 
     * @param name  the new username
     * @return      0 if the change was successful  
     *              1 if the broadcast fail 
     *              2 if the username is already used
     */
    public int changeUsername(String name) {
        System.out.println("Trying to change username to "+name);
        if (!myList.IsPresent(name)) {
            username = name;
            try {
                msgSender.sendBroadcastName(username);
            } catch (IOException e) {
                return 1;
            } 
            return 0;
        }
        return 2;
    }   

    /**
     * Displays the list of active users
     */
    public void listActive() {
        System.out.println("Showing active users:");
        for (String IP: myList.GetIP()) {
            System.out.println(myList.GetName(IP));
        } 
    }   

    /**
     * Start the application: start all the background threads
     * annouces the user's presence on the network and launch the terminal interface.
     * 
     * @throws IOException  If an error occurs while sending the initial connection broadcast
     */
    public void start() throws IOException {
        // Here goes everything automatic before terminal is launched and takes execution flow

        receiver.start();
        msgSender.sendBroadcastConnexion();
        sendUpdate.start();
        disconnection.start();

        try {
            terminal.start();
        } finally {
            shutdown();
        }
    } 

    /**
     * Stop the application and releases it ressources
     * 
     * Wait for all the threads to terminate and send a broadcast disconnection
     * and close the UDP sender.
     */
    public void shutdown(){
        System.out.println("Shutting everything down");
        sendUpdate.interrupt();
        disconnection.interrupt();
        receiver.terminate();

        try{
            sendUpdate.join();
            disconnection.join();
            receiver.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        try{
            msgSender.sendBroadcastDeconnexion();
        } catch (IOException e){
            System.out.println("Failed to broadcast a disconnection flag");
        }

        msgSender.close();
        System.out.println("Application stopped !");
    }
}   
