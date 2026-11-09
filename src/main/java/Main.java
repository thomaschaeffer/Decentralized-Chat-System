import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException{
        String username = "10.1.5.14";
        UserList test1 = new UserList();
        
        Thread receiver = new Receiver(test1);
        receiver.start();

        Sender msgSender = new Sender(4445, 4444, 256);

        msgSender.sendUDP("", "connection", username);
        
        int i = 0;
        while (i < 100000) {
            i = i + 1;
        }

        for (String IP: test1.GetIP()){
            System.out.println(test1.GetName(IP));
            System.out.println(test1.GetTimestamp(IP));
        }

        msgSender.sendUDP("", "change_name", "Thomas");


                 i = 0;
        while (i < 100000) {
            i = i + 1;
        }

        for (String IP: test1.GetIP()){
            System.out.println(test1.GetName(IP));
        }

        msgSender.sendUDP("", "update", "Thomas");

         i = 0;
        while (i < 100000) {
            i = i + 1;
        }

        for (String IP: test1.GetIP()){
            System.out.println(test1.GetName(IP));
            System.out.println(test1.GetTimestamp(IP));
        }

        msgSender.sendUDP("", "deconnection", "Thomas");

         i = 0;
        while (i < 100000) {
            i = i + 1;
        }

        for (String IP: test1.GetIP()){
            System.out.println(test1.GetName(IP));
        }
    }     
}