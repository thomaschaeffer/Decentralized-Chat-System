package chatsystem;

import java.lang.InterruptedException;

/**
 * This is a thread responsible for checking all the absent user
 * It check periodically the last activity timestamp of each users
 * if it's above timeLimite it remove them from our list
 */
public class Disconnection extends Thread {

    private long timeLimite= 10000;
    public UserList activeList;

    public Disconnection(UserList activeList) {
        this.activeList = activeList;
    }
    /**
     * Start the disconnection thread.
     * Every second, the thread checks the last activity
     * of all the user in our list and removes those who
     * have not give an udpate in the last timeLimite
     */
    @Override
    public void run(){
        while (!Thread.currentThread().isInterrupted()){
            try{
            sleep(1000);
            for (String IP: activeList.GetIP()){
                if(System.currentTimeMillis() - activeList.GetTimestamp(IP) > timeLimite){
                    handler(IP);
                }
            }
        }
        catch (InterruptedException e ){
            break;
        }
    }
}
    /**
     * Removes a disconnected user from the active user list
     * 
     * @param ip the IP address of the user we want to disconnected
     */
    private void handler(String ip) {
        activeList.Remove(ip);
    }
}