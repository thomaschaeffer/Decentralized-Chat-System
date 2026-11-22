package chatsystem;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;


public class Sender {
    private final int senderPort;
    private final DatagramSocket socket;
    private byte[] buff;

    public Sender(int senderPort, int socketPort, int buffLen) throws SocketException {
        this.senderPort = senderPort;
        this.socket = new DatagramSocket(socketPort);
        this.buff = new byte[buffLen];
    }

    private void sendUDP(String strAdrDest, String flag, String content) throws IOException {
        InetAddress adrDest;
        if (strAdrDest.isEmpty()) {
            //adrDest = getBroadcast();
            adrDest = InetAddress.getByName("10.1.255.255");
        } else {
            adrDest = InetAddress.getByName(strAdrDest);
        }
        String data = String.join(",", Arrays.asList(flag, content));
        buff = data.getBytes();
        DatagramPacket outPacket = new DatagramPacket(buff, buff.length, adrDest, senderPort);
        socket.send(outPacket);
    }

    public void sendBroadcastConnexion() throws IOException{
        sendUDP("", "connection", "");
    }

    public void sendBroadcastUpdate() throws IOException{
        sendUDP("", "update", "");
    }

    public void sendBroadcastDeconnexion() throws IOException{
        sendUDP("", "deconnection", "");
    }

    public void sendBroadcastName(String name) throws IOException{
        sendUDP("", "change_name", name);
    }

    public void close() {
        socket.close();
    }
}
