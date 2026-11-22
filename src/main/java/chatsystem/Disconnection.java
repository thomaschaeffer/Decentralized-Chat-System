package chatsystem;

import java.lang.InterruptedException;

public class Disconnection extends Thread {

    private long timeLimite= 10000;
    public UserList activeList;

    public Disconnection(UserList activeList) {
        this.activeList = activeList;
    }

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

    private void handler(String ip) {
        activeList.Remove(ip);
    }
}