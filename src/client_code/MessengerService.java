package client_code;

import java.util.ArrayList;

public class MessengerService implements Runnable {

    private int name;
    private String IP;

    public MessengerService(int name, String IP){
        this.name = name;
        this.IP = IP;
    }

    public ArrayList get_request(String req){

        return HttpRest.executeGet(this.IP + req);
    }

    public ArrayList post_request(String req, String payload){
         return HttpRest.executePost(this.IP+ req, payload);
    }

    public boolean make_booking(String room, int day, String time, int att){
        String booking_tmp = ("{\"room_name\": \""+ room +"\", \"day\": "+ day +", \"time\":\""+ time +"\", \"num_attendees\": "+ att +"}");
        return (post_request("/bookings", booking_tmp).get(0) != "Error, booking already exists");
    }

    public void build_request(){

        ArrayList received = get_request("/rooms");
        System.out.println(this.name + ": " + received);
        System.out.println(make_booking("CG04", 2, "13:00", 80));
    }

    @Override
    public void run() {
        //noinspection InfiniteLoopStatement
            build_request();
    }

    public int getName() {
        return name;
    }
}
