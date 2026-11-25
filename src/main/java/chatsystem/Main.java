package chatsystem;

import java.io.IOException;
import java.net.*;
/**
 * Entry point of the ChatSystem application
 * The Main class creat the Application instance and start its execution
 * 
 */
public class Main {
    /**
     * Creates a new Application instance and start it
     * 
     * @throws IOException      if an input/output error occur during application start
     * @throws SocketException  if a network socket cannot be created
     */
    public static void main(String[] args) throws IOException , SocketException {
        Application app = new Application();
        app.start();        
    }  
}