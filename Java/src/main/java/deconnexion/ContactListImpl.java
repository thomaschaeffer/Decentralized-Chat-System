package deconnexion;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ContactListImpl implements ContactList {
    private final Map<String, Contact> contacts = new ConcurrentHashMap<>();

    @Override
    public void add(String ip, String pseudo) { contacts.put(ip, new Contact(pseudo)); }

    @Override
    public void remove(String ip) { contacts.remove(ip); }

    @Override
    public void setState(String ip, UserState state) {
        Contact c = contacts.get(ip);
        if (c != null) c.setState(state);
    }

    @Override
    public void touch(String ip) {
        Contact c = contacts.get(ip);
        if (c != null) c.touch();
    }

    @Override
    public Map<String, Contact> getContacts() { return contacts; }
}