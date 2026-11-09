import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

class UserList {

    private Map<String,CustomTuple<String,Long>> userlist;
    public UserList(){
        this.userlist = new Hashtable<>();
    }

    public void Add(String ip,String nickname,long time){
        CustomTuple<String,Long> tuple = new CustomTuple<>(nickname,time);
        this.userlist.put(ip,tuple);
    }
    
    public void Remove(String ip){
        this.userlist.remove(ip);
    }
    public boolean IsPresent(String nickname){
        for (CustomTuple tuple: this.userlist.values()){
            if (tuple.getFirst().equals(nickname)){
                return true;
            }
        }
        return false;
    }

    public void ModifyNickName(String ip,String nickname){
        this.userlist.get(ip).setFirst(nickname);
    }


    public void UpdateTimestamp(String ip) {
        this.userlist.get(ip).setSecond(System.currentTimeMillis());
    }

    public String GetName(String ip){
        return  this.userlist.get(ip).getFirst();
    }

    public Set<String> GetIP(){
        return this.userlist.keySet();
    }
    
    public long GetTimestamp(String ip){
        return  this.userlist.get(ip).getSecond();
    }
}

