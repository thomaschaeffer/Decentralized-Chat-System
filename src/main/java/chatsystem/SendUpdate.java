package chatsystem;

/**
 * This thread is responsible for telling every user that I'm still alive.
 * It periodically sends a broadcast to all the users on the network.
 */
public class SendUpdate extends Thread{
    private long timeline = 1000;
    private Sender sender;
    public SendUpdate(Sender sender){this.sender = sender;}
    /**
     * Starts the SendUpdate thread.
     * Every second, the thread sends an update broadcast.
     */
    @Override
    public void run(){
        while(!Thread.currentThread().isInterrupted()){
            try{
                sleep(timeline);
                sender.sendBroadcastUpdate();
            } catch (InterruptedException e){
                break;
            }  
            catch (Exception i) {
                System.out.println(i);
            }

        }

    }
}
