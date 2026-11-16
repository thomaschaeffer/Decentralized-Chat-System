import java.io.IOException;
import java.net.*;

public class Application {
    private String username;
    public UserList myList;
    private Thread receiver;
    private Thread disconnection;
    private Sender msgSender;
    private Terminal terminal;

    public Application() throws SocketException {
        username = "10.1.5.14";
        myList = new UserList();
        receiver = new Receiver(myList);
        disconnection = new Disconnection(myList);
        msgSender = new Sender(4445, 4444, 256);
        terminal = new Terminal(this);
    } 

    public int sendDiscovery() {
        System.out.println("Discovery!");
        try {
            msgSender.sendUDP("", "connection", "Thomas");
            System.out.println("sending");
        } catch (IOException e) {
            return 1;
        } 
        
        return 0;
    }

    public int sendFake() {
        System.out.println("Sent fake connection ping from simulated user!");
        try {
            msgSender.sendUDP("", "update", "Thomas");
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
                msgSender.sendUDP("", "change_name", username);
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
        disconnection.start();
        terminal.start();
    } 
}   