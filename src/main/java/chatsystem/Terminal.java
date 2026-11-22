package chatsystem;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;

/**
 * Creates a terminal envorinment for the user to execute commands and control the program
 * 
 * The Terminal class is responsible for listening for user input and to execute functions accordingly
 */
public class Terminal {
    
    /**
     * Creates a Terminal
     * 
     * @param app        reference to the Application object that manages the program
     */
    private Application app;
    public Terminal(Application app) {
        this.app = app;
    } 

    /**
     * Internal function used to verify the number of arguments passed by the user for a given function
     */
    private String checkArgs(int len, int expected, String cmd) {
        if (len != expected) {
            return "Error: "+cmd+" expects "+expected+" arguments, but "+len+" was given.";
        } 
        return "";
    } 

    /**
     * The main handler, executes the command entered in the terminal by the user 
     * Uses a switch case for branching logic based on command name entered
     * Executes callback functions defined in the Application class, as to hide implementation from this class. chatsystem.Terminal knows nothing outside chatsystem.Application
     * Reacts to potential error messages received from call to app object
     * (Is subject to future modification to become more generalised)
     */
    private void executeCmd(String input) {
        String[] parts = input.split(" ");
        String command = parts[0]; 
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);
        int nArgs = args.length;
        String errorArgs;

        switch (command) {
            case "help":
                System.out.println("(WIP) Here is all commands: ");
                System.out.println("test_connection # Broadcasts a ping to simulate connection behaviour\ntest_update # Broadcasts a signal to simulate a periodic 'sign of life' signal\nchange_username\nlist_active\n");
                System.out.println("Commands starting with 'test_' serves as manual tests for behaviour ment to be automatic in final implementation");
                break;
            case "test_connection":
                if (this.app.test_connection() != 0) {
                    System.out.println("Error: UDP Broadcast failed");
                }
                break;
            case "test_update":
                if (this.app.test_update() != 0) {
                    System.out.println("Error: UDP Broadcast failed");
                }
                break;
            case "change_username":
                errorArgs=checkArgs(nArgs, 1, command);
                if (errorArgs.equals("")) {
                    int i = this.app.changeUsername(args[0]);
                    if (i == 1) {
                        System.out.println("Error: UDP Broadcast failed");
                    } else if (i == 2) {
                        System.out.println("Error: Affection failed. Already a user named "+args[0]);
                    } 
                } else {System.out.println(errorArgs);} 
                break;
            case "list_active":
                errorArgs=checkArgs(nArgs, 0, command);
                if (errorArgs.equals("")) {
                    this.app.listActive();
                } else {System.out.println(errorArgs);} 
                
                break;
            default:
                System.out.println("Error: Unknown command: "+command);
        } 
    }  

    /**
     * Starts the terminal loop
     * Continously listens for input from the user and calls the handler to validate and execute the user input
     */
    public void start() throws IOException {
        // chatsystem.Main while loop responding to user commands
        boolean running = true;
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (running) {
            System.out.print("Chatsystem > ");
            String command = reader.readLine();

            if (command.equals("exit")) {
                System.out.println("Bye!");
                running = false;
                break;                
            } 
            
            executeCmd(command);
        }  
    } 
}   