package client_code;

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

    public ArrayList post_request(String req, String payload){
         return HttpRest.executePost(this.IP+ req, payload);
    }

    public boolean make_booking(String room, int day, String time, int att){
        String booking_tmp = ("{\"room_name\": \""+ room +"\", \"day\": "+ day +", \"time\":\""+ time +"\", \"num_attendees\": "+ att +"}");
        String error = "Error, booking already exists";
        return (!post_request("/bookings", booking_tmp).get(0).toString().equals(error));
    }

    public void get_rooms(){
        System.out.println(this.name + " :" + get_request("/rooms"));
    }

    public void get_checkRoom(String name, int day, String time){
        String checkRoom_tmp = "/checkRoom/rooms/"+ name +"/day/"+ day +"/time/"+ time;
        System.out.println(this.name + " :" + get_request(checkRoom_tmp));
    }

    public void get_timetable(String room, int s_day, int e_day){
       String timetable_tmp = "/timetableWeek/rooms/"+ room +"/startDay/"+ s_day +"/endDay/"+ e_day;
       System.out.println(this.name + " :" + get_request(timetable_tmp));
    }

    public void build_request(){
        get_r           ooms();
        get_checkRoom("CG04", 2, "16:00");
        get_timetable("CG04", 2, 7);
        System.out.println(this.name + " :" + make_booking("CG04", 2, "16:00", 80));
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
