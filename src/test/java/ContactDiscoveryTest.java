
import chatsystem.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.SocketException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ContactDiscoveryTest {

    private static final int senderPort = 4445;
    private static final int socketPort = 4444;


    @Test
    void ReceiverTest(){
        UserList myList = new UserList();
        Receiver receiver = new Receiver(myList);
        //receiver.addObserver(message -> System.out.println(message.content));
        receiver.setDaemon(true);
        receiver.start();
    }

    @Test
    void SenderTest() throws SocketException, IOException, InterruptedException {
        UserList myList = new UserList();
        Receiver receiver = new Receiver(myList);
        receiver.setDaemon(true);
        receiver.start();
        Thread.sleep(1000);
        Sender msgSender = new Sender(senderPort, socketPort, 256);

        msgSender.sendUDP("", "connection", "10.1.5.14");
        Thread.sleep(1000);

        for (String IP: myList.GetIP()){
            assertEquals("10.1.5.14", myList.GetName(IP));
        }
        msgSender.sendUDP("", "change_name", "here");

        Thread.sleep(1000);
        for (String IP: myList.GetIP()){
            assertEquals("here", myList.GetName(IP));
        }

        long time = System.currentTimeMillis() ;
        msgSender.sendUDP("", "update", "here");
        Thread.sleep(1000);
        for (String IP: myList.GetIP()){
            assertEquals("here", myList.GetName(IP));
        }
        msgSender.sendUDP("", "deconnection", "Thomas");
        Thread.sleep(1000);
        for (String IP: myList.GetIP()){
            assertEquals("",myList.GetName(IP));
        }
    }

    @Test
    void SendUpdateTest() throws Exception{
        UserList myList = new UserList();
        Receiver receiver = new Receiver(myList);
        receiver.setDaemon(true);
        receiver.start();
        Thread.sleep(1000);
        Sender msgSender = new Sender(senderPort, socketPort, 256);
        msgSender.sendUDP("", "connection", "10.1.5.14");

        long time1 = 0;
        for (String IP: myList.GetIP()){
            time1 = myList.GetTimestamp(IP);
        }
        SendUpdate sendUpdate = new SendUpdate(msgSender);
        sendUpdate.start();
        Thread.sleep(2000);
        long time2 = 0;
        for (String IP: myList.GetIP()){
            time2 = myList.GetTimestamp(IP);
        }
        for (String IP: myList.GetIP()){
            assertTrue(time2 > time1, "Le timestamp a été mis à jour");
        }
    }

    @Test
    void DisconnectionTest() throws SocketException, IOException, InterruptedException{
        UserList myList = new UserList();
        Receiver receiver = new Receiver(myList);
        receiver.setDaemon(true);
        receiver.start();
        Disconnection disconnection = new Disconnection(myList);
        disconnection.start();
        Sender msgSender = new Sender(senderPort, socketPort, 256);
        msgSender.sendUDP("", "connection", "10.1.5.14");

        Thread.sleep(2000);
        for (String IP: myList.GetIP()){
            assertEquals("",myList.GetName(IP));
        }
    }

    @Test
    void UserListTest(){
        UserList myList = new UserList();
        long time = System.currentTimeMillis();

        myList.Add("10.29.153.93","toto",time);
        for (String IP: myList.GetIP()){
            assertEquals("10.29.153.93",IP);
        }
        for (String IP: myList.GetIP()){
            assertEquals("toto",myList.GetName(IP));
        }

        myList.Remove("10.29.153.93");
        for (String IP: myList.GetIP()){
            assertEquals("",myList.GetName(IP));
        }

        myList.Add("10.29.153.93","Armand",time);
        myList.ModifyNickName("10.29.153.93","Thomas");
        for (String IP: myList.GetIP()){
            assertEquals("Thomas",myList.GetName(IP));
        }

        myList.UpdateTimestamp("10.29.153.93");
        for (String IP: myList.GetIP()){
            assertTrue(myList.GetTimestamp("10.29.153.93") > time, "Le timestamp a été mis à jour");
        }
    }

}
