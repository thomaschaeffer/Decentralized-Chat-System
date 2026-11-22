package chatsystem;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
/**
 * Manages the list of users currently connected to the network.
 * Each user is identified by their IP address and associated
 * with their nickname and last activity timestamp.
 */
public class UserList {

    /**
     * Stores user using their IP adress as the key.
     * Each value contains the user's nickname and his last activity timestamp.
     */
    private Map<String,CustomTuple<String,Long>> userlist;
    /**
     * Creates an empty user list.
     */
    public UserList(){
        this.userlist = new Hashtable<>();
    }

    /**
     * Adds a new user to the list
     *
     * @param ip        User's ip
     * @param nickname  user's nickname
     * @param time      user's last activity timestamp
     */
    public void Add(String ip,String nickname,long time){
        CustomTuple<String,Long> tuple = new CustomTuple<>(nickname,time);
        this.userlist.put(ip,tuple);
    }
    
    /**
     * Removes a user from the list using their IP adress.
     *
     * @param ip        User's ip
     */
    public void Remove(String ip){
        this.userlist.remove(ip);
    }

    /**
     * Checks if the nickname is already used by another user.
     *
     * @param nickname  the nickname we are searching for
     * @return          true if the nickname is already present, false otherwise
     */
    public boolean IsPresent(String nickname){
        for (CustomTuple tuple: this.userlist.values()){
            if (tuple.getFirst().equals(nickname)){
                return true;
            }
        }
        return false;
    }

    /**
     * Modify the nickname associated with a given IP address
     *
     * @param ip        User's IP address
     * @param nickname  The new nickname
     */
    public void ModifyNickName(String ip,String nickname){
        this.userlist.get(ip).setFirst(nickname);
    }


    /**
     * Updates the last activity timestamp of a user
     *
     * @param ip User's IP address
     */
    public void UpdateTimestamp(String ip) {
        this.userlist.get(ip).setSecond(System.currentTimeMillis());
    }

    /**
     * Return the nickname associated with the given IP address
     *
     * @param ip    the IP address of the user
     * @return      the user's nickname
     */
    public String GetName(String ip){
        return  this.userlist.get(ip).getFirst();
    }

    /**
     * Returns the IP addresses of all users in the user's list
     *
     * @return  A set containing all user IP addresses
     */
    public Set<String> GetIP(){
        return new HashSet<>(this.userlist.keySet());
    }
    
    /**
     * Returns the last activity timestamp of a user
     *
     * @param ip    the user's IP addresse
     * @return      the last activity timestamp
     */
    public long GetTimestamp(String ip){
        return  this.userlist.get(ip).getSecond();
    }

    /**
     * checks wether a user is known by their IP address
     *
     * @param ip    The IP address we are searching for
     * @return      true if the IP address exists in the list, false otherwise
     */
    public boolean IsKnow(String ip){
        return this.userlist.containsKey(ip);
    }
}

