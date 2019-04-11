package client_code;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
        String error = "Error, booking already exists";
        return (!post_request("/bookings", booking_tmp).get(0).toString().equals(error));
    }

    public ArrayList get_rooms(){
        return get_request("/rooms");
    }

    public ArrayList get_checkRoom(String name, int day, String time){
        return get_request("/checkRoom/rooms/"+ name +"/day/"+ day +"/time/"+ time);
    }

    public ArrayList get_timetable(String room, int s_day, int e_day){
       return get_request("/timetableWeek/rooms/"+ room +"/startDay/"+ s_day +"/endDay/"+ e_day);
    }

    public void build_request(){
        System.out.println(get_rooms());
        System.out.println(get_checkRoom("CG04", 2, "16:00"));
        System.out.println(get_timetable("CG04", 2, 7));
        System.out.println(this.name + " :" + make_booking("CG04", 2, "16:00", 80));
    }

    public Map<String, Object> jsonToMap(String s) {
        Map<String, Object> m;
        try {
            m = new ObjectMapper().readValue(s, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
            m = new HashMap<>();
        }
        return m;
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
