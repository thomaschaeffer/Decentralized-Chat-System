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

    public void sendUDP(String strAdrDest, String flag, String content) throws IOException {
        InetAddress adrDest;
        if (strAdrDest.isEmpty()) {
            //adrDest = getBroadcast();
            adrDest = InetAddress.getByName("255.255.255.255");
        } else {
            adrDest = InetAddress.getByName(strAdrDest);
        }
        String data = String.join(",", Arrays.asList(flag, content));
        buff = data.getBytes();
        DatagramPacket outPacket = new DatagramPacket(buff, buff.length, adrDest, senderPort);
        socket.send(outPacket);
    }
}
