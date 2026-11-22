package chatsystem;

public class SendUpdate extends Thread{
    private long timeline = 1000;
    private Sender sender;
    public SendUpdate(Sender sender){this.sender = sender;}

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
