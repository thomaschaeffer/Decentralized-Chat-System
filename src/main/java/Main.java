import java.io.IOException;
import java.lang.InterruptedException;

public class Main {
    public static void main(String[] args) throws IOException{
        String username = "10.1.5.14";
        UserList myList = new UserList();
        
        Thread receiver = new Receiver(myList);
        receiver.start();
        Thread disconnection = new Disconnection(myList);
        disconnection.start();

        Sender msgSender = new Sender(4445, 4444, 256);

        msgSender.sendUDP("", "connection", username);
        
        int i = 0;
        while (i < 100000) {
            i = i + 1;
        }

        for (String IP: myList.GetIP()){
            System.out.println(myList.GetName(IP));
            System.out.println(myList.GetTimestamp(IP));
        }

        msgSender.sendUDP("", "change_name", "AHHHHHHHHHHHHHHHH");


                 i = 0;
        while (i < 100000) {
            i = i + 1;
        }

        for (String IP: myList.GetIP()){
            System.out.println(myList.GetName(IP));
        }

        msgSender.sendUDP("", "update", "Thomas");

         i = 0;
        while (i < 100000) {
            i = i + 1;
        }

        for (String IP: myList.GetIP()){
            System.out.println(myList.GetName(IP));
            System.out.println(myList.GetTimestamp(IP));
        }

        msgSender.sendUDP("", "deconnection", "Thomas");

         i = 0;
        while (i < 100000) {
            i = i + 1;
        }
        System.out.println("List vide");

        for (String IP: myList.GetIP()){
            System.out.println(myList.GetName(IP));
        }

        msgSender.sendUDP("", "connection", username);
        msgSender.sendUDP("", "change_name", "MJ");
                 i = 0;

        while (i < 100000) {
            i = i + 1;
        }
        for (String IP: myList.GetIP()){
            System.out.println(myList.GetName(IP));
        }
        i = 0;
        while (i < 10000000) {
            i = i + 1;
            //System.out.print(i);
        }
        System.out.println("supposement vide");
        for (String IP: myList.GetIP()){
            System.out.println(myList.GetName(IP));
        }
    }  
}