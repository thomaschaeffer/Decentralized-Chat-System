import java.io.IOException;
import java.net.*;


public class Receiver extends Thread {
    private boolean running = true;
    public UserList activeList;

    public Receiver(UserList activeList) {
        this.activeList = activeList;
    }

    @Override
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(4445);
            byte[] buf = new byte[256];

            while (running) {
                DatagramPacket inPacket  = new DatagramPacket(buf, buf.length);
                socket.receive(inPacket);
                handler(inPacket);
            }
            socket.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void handler(DatagramPacket received) {
        String[] data = (new String(received.getData(), 0, received.getLength())).split(",");
        String flag = data[0];
        String content = data[1];
        String address = received.getAddress().getHostAddress();

        switch(flag) {
            case "update":
                System.out.println("update");
                this.activeList.UpdateTimestamp(address);
                break;
            case "connection":
                System.out.println("connection");
                this.activeList.Add(address, content, System.currentTimeMillis());
                break;
            case "deconnection":
                System.out.println("deconnection");
                this.activeList.Remove(address);
                break;
            case "change_name":
                System.out.println("change name");
                this.activeList.ModifyNickName(address, content);
                break;
            default:
                System.out.println("Packet with non matching flag: "+flag+" received from: "+address+" ("+content+")");
        }
    }

    public void terminate() {
        running = false;
    }
}