package chatsystem;

import java.io.IOException;
import java.lang.InterruptedException;

public class SendUpdate extends Thread{
    private long timeline = 1000;
    private Sender sender;
    public SendUpdate(Sender sender){this.sender = sender;}

    @Override
    public void run(){
        while(true){
            try{
                sleep(timeline);
                sender.sendUDP("", "update", "Thomas");
            }catch (Exception i) {
                System.out.println(i);
            }

        }

    }
}
