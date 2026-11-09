import java.io.IOException;

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

        msgSender.sendUDP("", "change_name", "Thomas");


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
    }     
}