package chatsystem;

import java.io.IOException;
import java.net.*;

public class Application {
    private String username;
    public UserList myList;
    private Receiver receiver;
    private Disconnection disconnection;
    private SendUpdate sendUpdate;
    private Sender msgSender;
    private Terminal terminal;

    public Application() throws SocketException {
        username = "10.1.5.14";
        myList = new UserList();
        receiver = new Receiver(myList);
        disconnection = new Disconnection(myList);
        msgSender = new Sender(4445, 4444, 256);
        sendUpdate = new SendUpdate(msgSender);
        terminal = new Terminal(this);
    } 

    public int test_connection() {
        System.out.println("sent fake connection message");
        try {
            msgSender.sendBroadcastConnexion();
        } catch (IOException e) {
            return 1;
        } 
        return 0;
    }

    public int test_update() {
        System.out.println("Sent fake connection ping from simulated user");
        try {
            msgSender.sendBroadcastUpdate();
        } catch (IOException e) {
            return 1;
        } 
        
        return 0;
    }

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

    public void listActive() {
        System.out.println("Showing active users:");
        for (String IP: myList.GetIP()) {
            System.out.println(myList.GetName(IP));
        } 
    }   

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

    public void shutdown(){
        System.out.println("Shuttind everything down");
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
