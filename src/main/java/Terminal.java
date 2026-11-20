import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Arrays;

public class Terminal {
    private Application app;
    public Terminal(Application app) {
        this.app = app;
    } 

    private String checkArgs(int len, int expected, String cmd) {
        if (len != expected) {
            return "Error: "+cmd+" expects "+expected+" arguments, but "+len+" was given.";
        } 
        return "";
    } 

    private void executeCmd(String input) {
        // Case switch on different commands with default fallback
        // Maybe more advanced command handling with arguments and more generalised error handling!
        // Store all possible commands with desc in dict? or other form?
        // Executes app.function defined in Application class so that Terminal knows nothing outside Application
        String[] parts = input.split(" ");
        String command = parts[0]; 
        String[] args = Arrays.copyOfRange(parts, 1, parts.length);
        int nArgs = args.length;
        String errorArgs;

        switch (command) {
            case "help":
                // Function in this class listing all commands with descriptions?
                // Maybe possible to do help cmd to have specific on cmd
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

    public void start() throws IOException {
        // Main while loop responding to user commands
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