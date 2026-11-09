package deconnexion;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/* Cette classe cree un objet Deconnexion pour chaque utilisateur
chaque utilisateur broadcast x secondes
si ça fait plus de y secondes qu'on a pas d'information -> absente

 */
public class Disconnection extends Thread{
    private final ContactList contactList;
    private boolean running = true;
    private final long TIMEOUT_MS = 6_000;
    private static final int PORT = 4445;
    private Thread heartbeatThread;

    public Disconnection(ContactList contactList){
        this.contactList = contactList;
    }
    @Override
    public void run() {

        DatagramSocket socket = null;
        try{
            socket = new DatagramSocket(PORT);
            socket.setSoTimeout(1000); // pour vérifier chaque seconde si l'utilisateur est absent
            byte[] buf = new byte[256];


            while(running){
                DatagramPacket inPacket  = new DatagramPacket(buf, buf.length);
                try {
                    socket.receive(inPacket);
                    String received = new String(inPacket.getData(), 0, inPacket.getLength());
                    InetAddress senderAddress = inPacket.getAddress();
                    String ip = inPacket.getAddress().getHostAddress();
                    System.out.println("Reçu: " + received + ", IP adress :"+ senderAddress.getHostAddress());
                    if (received.startsWith(Flag.DISCONNECT.name())) {
                        contactList.remove(ip);
                    } else {
                        contactList.touch(ip);
                    }

                }catch (SocketTimeoutException e){

                }
                checkAbsentUsers();
            }
        } catch(IOException i){
            System.out.println(i);
        }


    }

    public void checkAbsentUsers(){
        long now = System.currentTimeMillis();
        for (Map.Entry<String, Contact> entry : contactList.getContacts().entrySet()) {
            Contact c = entry.getValue();
            if (c.getState() == UserState.CONNECTED && now - c.getLastSeen() > TIMEOUT_MS) {
                contactList.setState(entry.getKey(), UserState.ABSENT);
                System.out.println(entry.getKey() + " timed out, removed");
                contactList.remove(entry.getKey());
            }
        }
    }

    public static  void SendName(String msg) {
        DatagramSocket socket = null;
        try{
            socket = new DatagramSocket();
            socket.setBroadcast(true); // broadcast de chaque utilisateur
            byte[] buf = msg.getBytes(StandardCharsets.UTF_8);


            InetAddress senderAddress = InetAddress.getByName("172.19.255.255");
            DatagramPacket inPacket  = new DatagramPacket(buf, buf.length,senderAddress, PORT);
            socket.send(inPacket);
            System.out.println("IP adress: " + senderAddress.getHostAddress());
            socket.close();
        }
        catch(IOException i){
            System.out.println(i);
        }
    }

    public static void sendMessage(String host, String msg) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setBroadcast(true);
            byte[] buf = msg.getBytes(StandardCharsets.UTF_8);
            socket.send(new DatagramPacket(buf, buf.length,
                    InetAddress.getByName(host), PORT));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Starts a thread that sends "ALIVE:pseudo" every intervalMs milliseconds. */
    public void startHeartbeat(String host, String pseudo, long intervalMs) {
        if (heartbeatThread != null && heartbeatThread.isAlive()) {
            return;   // already running
        }
        heartbeatThread = new Thread(() -> {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    sendMessage(host, Flag.ALIVE.name() + ":" + pseudo);
                    Thread.sleep(intervalMs);
                }
            } catch (InterruptedException e) {
                // interrupted during sleep: leave run() and die peacefully
            }
        }, "heartbeat-" + pseudo);
        heartbeatThread.start();
    }

    /** Stops the heartbeat only (simulates a crash); we keep listening. */
    public void stopHeartbeat() {
        if (heartbeatThread != null) {
            heartbeatThread.interrupt();
        }
    }

    /** Stops everything: the listening loop and the heartbeat. */
    public void stopListening() {
        running = false;
        stopHeartbeat();
    }

}
