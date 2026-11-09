package deconnexion;

import java.util.Map;

public interface ContactList {
    void add(String ip, String pseudo);
    void remove(String ip);
    void setState(String ip, UserState state);
    void touch(String ip);
    Map<String, Contact> getContacts();
}
