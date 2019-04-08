import java.util.ArrayList;

public class MessengerService implements Runnable {

    private int name;
    private String IP;

    MessengerService(int name, String IP){
        this.name = name;
        this.IP = IP;
    }

    public ArrayList get_request(String req){
        return HttpRest.executeGet(this.IP + req);
    }

    public void put_request(String req){

    }

    public void build_request(){

        ArrayList recived = get_request("/rooms");
        System.out.println(this.name + ": " + recived);


    }

    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
        //while (true) {
            build_request();
        //}
    }

    public int getName() {
        return name;
    }
}
