package chatsystem;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
/**
 * Handles the sending of UDP messages to the other users
 * 
 * The Sender class is responsible for creating and sending messages.
 * Messages contains differents flags.
 */

public class Sender {
    private final int senderPort;
    private final DatagramSocket socket;
    private byte[] buff;

    /**
     * Creates a Sender
     * 
     * @param senderPort        destination port on which receivers listen
     * @param socketPort        local port used by this sender's socket
     * @param buffLen           buffer size
     * @throws SocketException  if the socket cannot be created
     */
    public Sender(int senderPort, int socketPort, int buffLen) throws SocketException {
        this.senderPort = senderPort;
        this.socket = new DatagramSocket(socketPort);
        this.buff = new byte[buffLen];
    }

    /**
     * Sends a UDP message to a specified destination address (by default it's a broadcast)
     * The message is : "flag,content"
     * 
     * @param strAdrDest    destination IP address
     * @param flag          message type (connection ,update, deconnection, change_name)
     * @param content       additional information (for exemple name when it's a change_name flag)
     * @throws IOException  if the address can't be solved or the packet can't be sent
     */
    private void sendUDP(String strAdrDest, String flag, String content) throws IOException {
        InetAddress adrDest;
        //Use the broadcast address by default
        if (strAdrDest.isEmpty()) {
            adrDest = InetAddress.getByName("127.255.255.255");
        } else {
            adrDest = InetAddress.getByName(strAdrDest);
        }

        //Create the message
        String data = String.join(",", Arrays.asList(flag, content));
        buff = data.getBytes();
        //Create and send the UDP packet
        DatagramPacket outPacket = new DatagramPacket(buff, buff.length, adrDest, senderPort);
        socket.send(outPacket);
    }
    /**
     * Broadcasts a connection message to annouce that the user who sent it is joining the network
     * 
     * @throws IOException  if the message can't be sent
     */
    public void sendBroadcastConnexion() throws IOException{
        sendUDP("", "connection", "");
    }

    /**
     * Broadcasts an update message that indicate that the user who sent it is still active
     * 
     * @throws IOException  if the message can't be sent
     */
    public void sendBroadcastUpdate() throws IOException{
        sendUDP("", "update", "");
    }

    /**
     * Broadcasts a disconnection message to announce that the user who sent it is leaving the network
     * 
     * @throws IOException  if the message can't be sent
     */
    public void sendBroadcastDeconnexion() throws IOException{
        sendUDP("", "deconnection", "");
    }

    /**
     * Broadcast a username change to the other users
     * 
     * @param name          new username
     * @throws IOException  if the message can't be sent
     */
    public void sendBroadcastName(String name) throws IOException{
        sendUDP("", "change_name", name);
    }

    /**
     * Closes the UDP socket 
     */
    public void close() {
        socket.close();
    }
}
