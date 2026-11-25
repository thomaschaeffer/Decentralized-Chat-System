import chatsystem.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.SocketException;

import static org.junit.jupiter.api.Assertions.*;
/**
 * Tests for the contact discovery feature.
 * They check that the Receiver, the Sender, SendUpdate, Disconnection and UserList
 * Each test releases its sockets in a finally block, so the ports are free for the next test.
 */
public class ContactDiscoveryTest {

    private static final int senderPort = 4445;
    private static final int socketPort = 4444;

    /**
     * Checks that a Receiver can be started and stopped without error.
     */
    @Test
    void ReceiverTest() throws SocketException{
        UserList myList = new UserList();
        Receiver receiver = new Receiver(myList);
        try{
            receiver.setDaemon(true);
            receiver.start();

        } finally {
            receiver.terminate();
        }
    }

    /**
     * Checks that the Sender correctly sends every kind of broadcast
     * (connection, name change, update, disconnection).
     */
    @Test
    void SenderTest() throws SocketException, IOException, InterruptedException {
        UserList myList = new UserList();
        Receiver receiver = new Receiver(myList);
        Sender msgSender = null;
        try{
            receiver.setDaemon(true);
            receiver.start();
            Thread.sleep(1000);
            msgSender = new Sender(senderPort, socketPort, 256);

            msgSender.sendBroadcastConnexion();
            Thread.sleep(1000);

            for (String IP: myList.GetIP()){
                assertEquals("10.1.5.14", myList.GetName(IP));
            }
            msgSender.sendBroadcastName("here");

            Thread.sleep(1000);
            for (String IP: myList.GetIP()){
                assertEquals("here", myList.GetName(IP));
            }

            msgSender.sendBroadcastUpdate();
            Thread.sleep(1000);
            for (String IP: myList.GetIP()){
                assertEquals("here", myList.GetName(IP));
            }
            msgSender.sendBroadcastDeconnexion();
            Thread.sleep(1000);
            for (String IP: myList.GetIP()){
                assertEquals("",myList.GetName(IP));
            }
        }finally {
            if (msgSender!= null){
                msgSender.close();
            }
            receiver.terminate();
        }
    }

    /**
     * Checks that SendUpdate periodically refreshes the timestamp of the user.
     */
    @Test
    void SendUpdateTest() throws Exception{
        UserList myList = new UserList();
        Receiver receiver = new Receiver(myList);
        Sender msgSender = null;
        try{
            receiver.setDaemon(true);
            receiver.start();
            Thread.sleep(1000);
            msgSender = new Sender(senderPort, socketPort, 256);
            msgSender.sendBroadcastConnexion();
            // Timestamp before SendUpdate starts
            long time1 = 0;
            for (String IP: myList.GetIP()){
                time1 = myList.GetTimestamp(IP);
            }
            SendUpdate sendUpdate = new SendUpdate(msgSender);
            sendUpdate.start();
            Thread.sleep(2000);
            // Timestamp after at least one update has been sent
            long time2 = 0;
            for (String IP: myList.GetIP()){
                time2 = myList.GetTimestamp(IP);
            }
            for (String IP: myList.GetIP()){
                assertTrue(time2 > time1, "Le timestamp a été mis à jour");
            }
        }finally {
            if (msgSender!= null){
                msgSender.close();
            }
            receiver.terminate();
        }
    }

    /**
     * Checks that the Disconnection thread removes the users that stopped sending updates.
     */
    @Test
    void DisconnectionTest() throws SocketException, IOException, InterruptedException{
        UserList myList = new UserList();
        Receiver receiver = new Receiver(myList);
        Sender msgSender = new Sender(senderPort, socketPort, 256);
        try{
            receiver.setDaemon(true);
            receiver.start();
            Disconnection disconnection = new Disconnection(myList);
            disconnection.start();
            msgSender.sendBroadcastConnexion();

            Thread.sleep(2000);
            for (String IP: myList.GetIP()){
                assertEquals("",myList.GetName(IP));
            }
        }finally {
            msgSender.close();
            receiver.terminate();
        }
    }

    /**
     * Checks the UserList methods: add, remove, nickname modification and timestamp update.
     */
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
