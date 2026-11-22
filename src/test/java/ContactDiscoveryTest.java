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
    void ReceiverTest() throws SocketException, InterruptedException {
        UserList myList = new UserList();
        Receiver receiver = new Receiver(myList);
        try{
            receiver.setDaemon(true);
            receiver.start();
            assertTrue(receiver.isAlive(), "The Receiver thread should be running after start()");
        } finally {
            receiver.terminate();
        }
        receiver.join(100);
        assertFalse(receiver.isAlive(), "The Receiver thread should stop after terminate()");
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
            Thread.sleep(100);
            msgSender = new Sender(senderPort, socketPort, 256);

            // Connection: the user is added, its nickname is its IP by default
            msgSender.sendBroadcastConnexion();
            Thread.sleep(100);
            assertEquals(1, myList.GetIP().size(), "Expected exactly one user after the connection packet");
            for (String IP: myList.GetIP()){
                assertEquals(IP, myList.GetName(IP), "The default nickname should be the IP address");

            }

            // Name change: the nickname is modified
            msgSender.sendBroadcastName("here");
            Thread.sleep(100);
            assertEquals(1, myList.GetIP().size(), "The name change should not add a new user");
            for (String IP: myList.GetIP()){
                assertEquals("here", myList.GetName(IP),"The nickname was not changed");
            }

            // Update: the user stays in the list and keeps its nickname
            msgSender.sendBroadcastUpdate();
            Thread.sleep(100);
            assertEquals(1, myList.GetIP().size(), "The update should not add or remove a user");
            for (String IP: myList.GetIP()){
                assertEquals("here", myList.GetName(IP), "The update should not change the nickname");
            }

            // Disconnection: the user is removed from the list
            msgSender.sendBroadcastDeconnexion();
            Thread.sleep(100);
            assertTrue(myList.GetIP().isEmpty(), "The list should be empty after the disconnection packet");
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
        SendUpdate sendUpdate = null;

        try{
            receiver.setDaemon(true);
            receiver.start();
            Thread.sleep(1500);
            msgSender = new Sender(senderPort, socketPort, 256);
            msgSender.sendBroadcastConnexion();
            Thread.sleep(1500);

            // Timestamp before SendUpdate starts
            assertEquals(1, myList.GetIP().size(), "Expected exactly one user after the connection packet");
            long time1 = 0;
            for (String IP: myList.GetIP()){
                time1 = myList.GetTimestamp(IP);
            }
            sendUpdate = new SendUpdate(msgSender);
            sendUpdate.setDaemon(true);
            sendUpdate.start();
            Thread.sleep(1500);
            // Timestamp after at least one update has been sent
            assertEquals(1, myList.GetIP().size(), "The user disappeared during the test");
            long time2 = 0;
            for (String IP: myList.GetIP()){
                time2 = myList.GetTimestamp(IP);
            }
            for (String IP: myList.GetIP()){
                assertTrue(time2 > time1, "The timestamp was not refreshed by SendUpdate");
            }
        }finally {
            if (sendUpdate != null) {
                sendUpdate.interrupt();
            }
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
        Sender msgSender = null;
        Disconnection disconnection = new Disconnection(myList);
        try{
            receiver.setDaemon(true);
            receiver.start();
            disconnection.start();
            msgSender = new Sender(senderPort, socketPort, 256);
            msgSender.sendBroadcastConnexion();
            Thread.sleep(300);
            assertEquals(1, myList.GetIP().size(), "The connection packet was not received");
            Thread.sleep(12000);
            assertTrue(myList.GetIP().isEmpty(), "The user is not disconnected");
        }finally {
            disconnection.interrupt();
            if (msgSender != null) {
                msgSender.close();
            }
            receiver.terminate();
        }
    }

    /**
     * Checks the UserList methods: add, remove, nickname modification and timestamp update.
     */
    @Test
    void UserListTest() throws InterruptedException{
        UserList myList = new UserList();
        long time = System.currentTimeMillis();

        // Add
        myList.Add("10.10.40.245","toto",time);
        assertEquals(1, myList.GetIP().size(), "The user was not added");
        assertTrue(myList.IsKnow("10.10.40.245"), "The user should be known after Add");

        // Remove
        myList.Remove("10.10.40.245");
        assertTrue(myList.GetIP().isEmpty(), "The list should be empty after Remove");

        // Nickname modification
        myList.Add("10.10.40.245","Armand",time);
        myList.ModifyNickName("10.10.40.245","Thomas");
        assertEquals("Thomas", myList.GetName("10.10.40.245"), "The nickname was not modified");

        // Timestamp update
        Thread.sleep(10);
        myList.UpdateTimestamp("10.10.40.245");
        assertTrue(myList.GetTimestamp("10.10.40.245") > time, "The timestamp was not updated");

    }

}
