package chatsystem;

import java.io.IOException;
import java.net.*;


public class Receiver extends Thread {
    private volatile boolean running = true;
    public UserList activeList;
    private final DatagramSocket socket;

    public Receiver(UserList activeList) throws SocketException{
        this.activeList = activeList;
        this.socket = new DatagramSocket(4445);
    }

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
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            socket.close();
        }
    }

    private void handler(DatagramPacket received) {
        System.out.println("handling a received package");
        String[] data = (new String(received.getData(), 0, received.getLength())).split(",",2);
        if (data.length < 2 )
        {
            return;
        }
        String flag = data[0];
        String content = data[1];
        String address = received.getAddress().getHostAddress();

        switch(flag) {
            case "update":
                System.out.println("update");
                if ( this.activeList.IsKnow(address)){
                    this.activeList.UpdateTimestamp(address);
                } else {
                    this.activeList.Add(address,"",System.currentTimeMillis());
                }
                break;
            case "connection":
                System.out.println("connection");
                if ( this.activeList.IsKnow(address)){
                    this.activeList.UpdateTimestamp(address);
                } else {
                    this.activeList.Add(address,"",System.currentTimeMillis());
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

    public void terminate() {
        running = false;
        socket.close();
    }
}