package chatsystem;

public class SendUpdate extends Thread{
    private long timeline = 1000;
    private Sender sender;
    public SendUpdate(Sender sender){this.sender = sender;}

    @Override
    public void run(){
        while(true){
            try{
                sleep(timeline);
                sender.sendBroadcastUpdate();
            }catch (Exception i) {
                System.out.println(i);
            }

        }

    }
}
