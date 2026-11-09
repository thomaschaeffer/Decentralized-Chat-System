package deconnexion;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException{
        ContactListImpl contactList = new ContactListImpl();
        Disconnection d = new Disconnection(contactList);
        d.start();

        contactList.add("127.0.0.1", "Alice");                      // normally done by the connection phase
        d.startHeartbeat("127.0.0.1", "Alice", 2_000);              // x = 2 s

        Thread.sleep(10_000);
        System.out.println("Should contain Alice: " + contactList.getContacts().keySet());

        d.stopHeartbeat();                                          // Alice "crashes"
        Thread.sleep(9_000);
        System.out.println("Should be empty: " + contactList.getContacts().keySet());

        d.stopListening();


    }
}