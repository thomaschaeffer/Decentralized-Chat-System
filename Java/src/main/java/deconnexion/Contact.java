package deconnexion;

public class Contact {
    private String pseudo;
    private UserState state;
    private long lastSeen;

    public Contact(String pseudo){
        this.pseudo = pseudo;
        this.state = UserState.CONNECTED;
        this.lastSeen = System.currentTimeMillis();
    }
    public String getPseudo() { return pseudo; }
    public UserState getState() { return state; }
    public long getLastSeen() { return lastSeen; }

    public void setState(UserState state) { this.state = state; }
    public void touch() { this.lastSeen = System.currentTimeMillis(); }
}
