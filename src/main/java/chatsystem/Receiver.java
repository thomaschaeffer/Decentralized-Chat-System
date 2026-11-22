package chatsystem;

import java.io.IOException;
import java.net.*;

import javax.management.RuntimeErrorException;

/**
 * Handles the reception of UDP messages from the other users
 * 
 * The Receiver class is responsible for receiving and treating the different messages.
 * Messages contains differents flags, according to which the class handles the messages appropriately  .
 */

public class Receiver extends Thread {
    private volatile boolean running = true;
    public UserList activeList;
    private final DatagramSocket socket;


    /**
     * Creates a Receiver
     * 
     * @param activeList        reference to the list of active users from the Applcation class
     * @throws SocketException  if the socket cannot be created
     */
    public Receiver(UserList activeList) throws SocketException{
        this.activeList = activeList;
        this.socket = new DatagramSocket(4445);
    }


    /**
     * Starts the Receiver thread.
     * Continously listens for incoming packages and executes a handler to treat the messages
     */
    @Override
    public void run() {
        try {
            byte[] buf = new byte[256];

            while (running) {
                DatagramPacket inPacket  = new DatagramPacket(buf, buf.length);
                socket.receive(inPacket); 
                if (running){
                     handler(inPacket);
                }
            }
        } catch (SocketException e){
            if (running) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            socket.close();
        }
    }

    /**
     * Retrieves the content of the package and executes different functions depending on the received flag
     */
    private void handler(DatagramPacket received) {
        String[] data = (new String(received.getData(), 0, received.getLength())).split(",",2);
        if (data.length < 2 ) {
            return;
        }
        String flag = data[0];
        String content = data[1];
        String address = received.getAddress().getHostAddress();

        switch(flag) {
            case "update":
                if ( this.activeList.IsKnow(address)){
                    this.activeList.UpdateTimestamp(address);
                } else {
                    this.activeList.Add(address,address,System.currentTimeMillis());
                }
                break;
            case "connection":
                System.out.println("connection");
                if ( this.activeList.IsKnow(address)){
                    this.activeList.UpdateTimestamp(address);
                } else {
                    this.activeList.Add(address,address,System.currentTimeMillis());
                }                
                break;
            case "deconnection":
                System.out.println("deconnection");
                this.activeList.Remove(address);
                break;
            case "change_name":
                System.out.println("change name");
                if (this.activeList.IsKnow(address)){
                    this.activeList.ModifyNickName(address, content);
                } else {
                    this.activeList.Add(address,content,System.currentTimeMillis());
                }
                break;
            default:
                System.out.println("Packet with non matching flag: "+flag+" received from: "+address+" ("+content+")");
        }
    }

    /**
     * Terminates the thread
     */
    public void terminate() {
        running = false;
        socket.close();
    }
}